import asyncio
import json
import os
import sys
from pathlib import Path
from threading import Thread
from typing import Any
from urllib.parse import quote

from mcp import Client, StdioServerParameters


PROJECT_ROOT = Path(__file__).resolve().parents[1]
WORKSPACE_ROOT = PROJECT_ROOT.parent
DEFAULT_SERVER_PATH = WORKSPACE_ROOT / "MCPIntegration" / "mcp_file_server.py"


class MCPFileSystemClient:
    def __init__(
        self,
        server_path: str | Path | None = None,
        workspace_root: str | Path | None = None,
    ):
        self.workspace_root = Path(
            workspace_root or WORKSPACE_ROOT
        ).resolve()

        self.server_path = Path(
            os.getenv("MCP_FILESYSTEM_SERVER")
            or server_path
            or DEFAULT_SERVER_PATH
        ).resolve()

    def _run(self, coro):
        try:
            asyncio.get_running_loop()
        except RuntimeError:
            return asyncio.run(coro)

        result = {}
        error = {}

        def runner():
            try:
                result["value"] = asyncio.run(coro)
            except Exception as exc:
                error["value"] = exc

        thread = Thread(target=runner)
        thread.start()
        thread.join()

        if "value" in error:
            raise error["value"]

        return result["value"]

    def _parameters(self) -> StdioServerParameters:
        env = {
            "FILESYSTEM_BASE_DIR": str(self.workspace_root),
        }

        for name in ("GROQ_API_KEY", "MODEL_NAME"):
            value = os.getenv(name)
            if value:
                env[name] = value

        return StdioServerParameters(
            command=sys.executable,
            args=[str(self.server_path)],
            env=env,
        )

    async def _discover_async(self) -> dict[str, Any]:
        async with Client(self._parameters()) as client:
            tools = await client.list_tools()
            resources = await client.list_resources()
            templates = await client.list_resource_templates()

            return {
                "server": "filesystem-mcp-server",
                "tools": [tool.name for tool in tools.tools],
                "resources": [str(resource.uri) for resource in resources.resources],
                "resource_templates": [
                    str(template.uri_template)
                    for template in templates.resource_templates
                ],
            }

    def discover(self) -> dict[str, Any]:
        return self._run(self._discover_async())

    def _relative_path(self, value: str | Path) -> Path:
        path = Path(value)

        if not path.is_absolute():
            return path

        try:
            return path.resolve().relative_to(self.workspace_root)
        except ValueError:
            return path

    async def _read_resource_async(self, uri: str) -> str:
        async with Client(self._parameters()) as client:
            result = await client.read_resource(uri)

            if not result.contents:
                return ""

            content = result.contents[0]

            if hasattr(content, "text"):
                return content.text

            if hasattr(content, "blob"):
                return content.blob

            return str(content)

    def read_file(self, file_path: str) -> str:
        relative = self._relative_path(file_path)

        # The server uses {+file_path}, so '/' must remain unescaped.
        uri = f"file://{quote(relative.as_posix(), safe='/')}"
        return self._run(self._read_resource_async(uri))

    async def _read_directory_async(self, uri: str):
        async with Client(self._parameters()) as client:
            result = await client.read_resource(uri)

            if not result.contents:
                return []

            content = result.contents[0]

            if hasattr(content, "text"):
                return json.loads(content.text)

            return content

    def list_files(self, directory: str):
        relative = self._relative_path(directory)
        uri = f"directory://{quote(relative.as_posix(), safe='/')}"
        return self._run(self._read_directory_async(uri))

    async def _call_tool_async(self, name: str, arguments: dict[str, Any]):
        async with Client(self._parameters()) as client:
            result = await client.call_tool(name, arguments)

            structured = getattr(result, "structured_content", None)

            if structured is not None:
                if isinstance(structured, dict) and "result" in structured:
                    return structured["result"]
                return structured

            if result.content:
                content = result.content[0]

                if hasattr(content, "text"):
                    try:
                        return json.loads(content.text)
                    except (TypeError, ValueError):
                        return content.text

            return None

    def call_tool(self, name: str, arguments: dict[str, Any]):
        return self._run(self._call_tool_async(name, arguments))

    def search_files(self, directory: str, keyword: str):
        return self.call_tool(
            "search_files",
            {"directory": directory, "keyword": keyword},
        )

    def summarize_file(self, file_path: str):
        return self.call_tool(
            "summarize_file",
            {"file_path": file_path},
        )

    def generate_summary_file(
        self,
        file_path: str,
        output_path: str | None = None,
    ):
        args = {"source_file_path": file_path}
        if output_path:
            args["output_file_path"] = output_path

        return self.call_tool("generate_summary_file", args)

    def batch_process(self, file_paths: list[str], operation: str = "read"):
        return self.call_tool(
            "batch_process",
            {
                "file_paths": file_paths,
                "operation": operation,
            },
        )

    def watch_directory(
        self,
        directory: str,
        duration_seconds: int = 30,
        poll_interval_seconds: int = 2,
    ):
        return self.call_tool(
            "watch_directory",
            {
                "directory": directory,
                "duration_seconds": duration_seconds,
                "poll_interval_seconds": poll_interval_seconds,
            },
        )
