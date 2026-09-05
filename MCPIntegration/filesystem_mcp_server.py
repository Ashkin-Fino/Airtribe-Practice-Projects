"""MCP server exposing the existing filesystem capabilities.

The server is intentionally small. MCP handles JSON-RPC 2.0, validation,
and transport; this module focuses on filesystem behavior.
"""

import json
import os
import time
from datetime import datetime, timezone
from pathlib import Path
from typing import Any
from urllib.parse import unquote

from mcp.server import MCPServer


SUPPORTED_EXTENSIONS = {".txt", ".pdf", ".docx"}
BASE_DIR = Path(os.getenv("FILESYSTEM_BASE_DIR", Path.cwd())).resolve()

mcp = MCPServer(
    "filesystem-mcp-server",
    instructions=(
        "Provides controlled filesystem access for resume and document "
        "processing. Supported files: TXT, PDF and DOCX."
    ),
)


def resolve_path(path: str) -> Path:
    """Resolve a path relative to the configured server base directory."""
    input_path = Path(path).expanduser()
    if input_path.is_absolute():
        return input_path.resolve()
    return (BASE_DIR / input_path).resolve()


def _ensure_file_exists(path: Path) -> None:
    if not path.exists():
        raise FileNotFoundError(f"File does not exist: {path}")
    if not path.is_file():
        raise ValueError(f"Path is not a file: {path}")


def _ensure_supported(path: Path) -> None:
    if path.suffix.lower() not in SUPPORTED_EXTENSIONS:
        raise ValueError(
            f"Unsupported file type: {path.suffix}. "
            f"Supported types: {', '.join(sorted(SUPPORTED_EXTENSIONS))}"
        )


def list_supported_files(directory: str, extension: str | None = None) -> list[dict[str, Any]]:
    """Return supported files with basic metadata."""
    folder = resolve_path(directory)
    if not folder.exists():
        raise FileNotFoundError(f"Folder does not exist: {folder}")
    if not folder.is_dir():
        raise ValueError(f"Path is not a directory: {folder}")

    requested_extension = extension.lower() if extension else None
    if requested_extension and not requested_extension.startswith("."):
        requested_extension = f".{requested_extension}"

    results = []
    for path in sorted(folder.iterdir(), key=lambda p: p.name.lower()):
        if not path.is_file() or path.suffix.lower() not in SUPPORTED_EXTENSIONS:
            continue
        if requested_extension and path.suffix.lower() != requested_extension:
            continue
        stat = path.stat()
        results.append(
            {
                "name": path.name,
                "path": str(path),
                "extension": path.suffix.lower(),
                "size": stat.st_size,
                "modified_at": datetime.fromtimestamp(
                    stat.st_mtime, tz=timezone.utc
                ).isoformat(),
            }
        )
    return results


def extract_text(file_path: str) -> str:
    """Extract text from TXT, PDF or DOCX."""
    path = resolve_path(file_path)
    _ensure_file_exists(path)
    _ensure_supported(path)

    extension = path.suffix.lower()
    if extension == ".txt":
        return path.read_text(encoding="utf-8")

    if extension == ".pdf":
        from PyPDF2 import PdfReader

        reader = PdfReader(str(path))
        return "\n".join(
            text for page in reader.pages if (text := page.extract_text())
        )

    if extension == ".docx":
        from docx import Document

        document = Document(str(path))
        return "\n".join(
            paragraph.text.strip()
            for paragraph in document.paragraphs
            if paragraph.text.strip()
        )

    raise ValueError(f"Unsupported file type: {extension}")


# ---------------------------------------------------------------------------
# Resources: read-only filesystem data
# ---------------------------------------------------------------------------

@mcp.resource("file://{file_path}", mime_type="text/plain")
def read_file_resource(file_path: str) -> str:
    """Read a supported document as text."""
    return extract_text(unquote(file_path))


@mcp.resource("directory://{directory_path}", mime_type="application/json")
def list_files_resource(directory_path: str) -> str:
    """List supported files in a directory with metadata."""
    return json.dumps(
        list_supported_files(unquote(directory_path)),
        indent=2,
    )


@mcp.resource("filesystem://capabilities", mime_type="application/json")
def capabilities_resource() -> str:
    """Describe the filesystem resources and tools exposed by this server."""
    return json.dumps(
        {
            "server": "filesystem-mcp-server",
            "supported_extensions": sorted(SUPPORTED_EXTENSIONS),
            "resources": [
                "file://{file_path}",
                "directory://{directory_path}",
                "filesystem://capabilities",
            ],
            "tools": [
                "search_files",
                "write_file",
                "summarize_file",
                "generate_summary_file",
                "watch_directory",
                "batch_process",
            ],
        },
        indent=2,
    )


# ---------------------------------------------------------------------------
# Tools: operations that perform computation or modify the filesystem
# ---------------------------------------------------------------------------

@mcp.tool()
def search_files(directory: str, keyword: str) -> list[str]:
    """Search supported files in a directory for a case-insensitive keyword."""
    folder = resolve_path(directory)
    if not folder.exists():
        raise FileNotFoundError(f"Folder does not exist: {folder}")
    if not folder.is_dir():
        raise ValueError(f"Path is not a directory: {folder}")

    matches = []
    needle = keyword.lower()
    for metadata in list_supported_files(directory):
        path = Path(metadata["path"])
        try:
            if needle in extract_text(str(path)).lower():
                matches.append(str(path))
        except Exception:
            # Keep search behavior tolerant of an unreadable individual file.
            continue
    return matches


@mcp.tool()
def write_file(file_path: str, content: str) -> dict[str, Any]:
    """Write text content to a file, creating parent directories when needed."""
    path = resolve_path(file_path)
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(content, encoding="utf-8")
    return {
        "status": "success",
        "path": str(path),
        "message": f"Content written successfully to: {path}",
    }


@mcp.tool()
def summarize_file(file_path: str) -> str:
    """Summarize a supported document using the configured Groq model."""
    content = extract_text(file_path)
    if not content.strip():
        return "File is empty"

    from dotenv import load_dotenv
    from groq import Groq

    load_dotenv()
    api_key = os.getenv("GROQ_API_KEY")
    if not api_key:
        raise RuntimeError("GROQ_API_KEY is missing in environment variables")

    model_name = os.getenv("MODEL_NAME", "llama-3.1-8b-instant")
    truncated_content = content[:12000]
    response = Groq(api_key=api_key).chat.completions.create(
        model=model_name,
        messages=[
            {"role": "system", "content": "You are a document summarizer."},
            {
                "role": "user",
                "content": f"Summarize the following document.\n\nDocument:\n{truncated_content}",
            },
        ],
        temperature=0.2,
    )
    return response.choices[0].message.content or ""


@mcp.tool()
def generate_summary_file(
    source_file_path: str,
    output_file_path: str | None = None,
) -> dict[str, Any]:
    """Generate a document summary and save it as a text file."""
    source_path = resolve_path(source_file_path)
    output_path = (
        resolve_path(output_file_path)
        if output_file_path
        else resolve_path(f"summaries/{source_path.stem}_summary.txt")
    )
    summary = summarize_file(str(source_path))
    output_path.parent.mkdir(parents=True, exist_ok=True)
    output_path.write_text(summary, encoding="utf-8")
    return {
        "status": "success",
        "source": str(source_path),
        "output": str(output_path),
    }


@mcp.tool()
def watch_directory(
    directory: str,
    duration_seconds: int = 30,
    poll_interval_seconds: int = 2,
) -> list[dict[str, Any]]:
    """Watch a directory for newly added supported files for a limited time.

    This intentionally uses simple polling so Phase 2 has no extra dependency.
    The tool returns when the duration expires or when a new file is detected.
    """
    if duration_seconds <= 0:
        raise ValueError("duration_seconds must be greater than 0")
    if poll_interval_seconds <= 0:
        raise ValueError("poll_interval_seconds must be greater than 0")

    folder = resolve_path(directory)
    if not folder.exists():
        raise FileNotFoundError(f"Folder does not exist: {folder}")
    if not folder.is_dir():
        raise ValueError(f"Path is not a directory: {folder}")

    known_files = {
        Path(item["path"]).resolve()
        for item in list_supported_files(directory)
    }
    detected = []
    deadline = time.monotonic() + duration_seconds

    while time.monotonic() < deadline:
        current_files = list_supported_files(directory)
        for item in current_files:
            path = Path(item["path"]).resolve()
            if path not in known_files:
                detected.append(item)
                known_files.add(path)

        if detected:
            return detected

        time.sleep(min(poll_interval_seconds, max(0, deadline - time.monotonic())))

    return detected


@mcp.tool()
def batch_process(
    file_paths: list[str],
    operation: str = "read",
) -> list[dict[str, Any]]:
    """Process multiple supported files in one MCP request.

    Supported operations are ``read`` and ``metadata``. Each file is processed
    independently so an error in one file does not fail the entire batch.
    """
    if not file_paths:
        return []
    if operation not in {"read", "metadata"}:
        raise ValueError("operation must be either 'read' or 'metadata'")

    results = []
    for file_path in file_paths:
        try:
            path = resolve_path(file_path)
            _ensure_file_exists(path)
            _ensure_supported(path)

            if operation == "read":
                results.append({
                    "path": str(path),
                    "status": "success",
                    "content": extract_text(str(path)),
                })
            else:
                stat = path.stat()
                results.append({
                    "path": str(path),
                    "status": "success",
                    "name": path.name,
                    "extension": path.suffix.lower(),
                    "size": stat.st_size,
                    "modified_at": datetime.fromtimestamp(
                        stat.st_mtime, tz=timezone.utc
                    ).isoformat(),
                })
        except Exception as exc:
            results.append({
                "path": str(resolve_path(file_path)),
                "status": "error",
                "error": str(exc),
            })

    return results


if __name__ == "__main__":
    # MCPServer defaults to stdio, which is the simplest local MCP transport.
    mcp.run()
