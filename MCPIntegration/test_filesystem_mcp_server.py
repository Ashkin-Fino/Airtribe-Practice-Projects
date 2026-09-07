from pathlib import Path

import pytest

# Importing the module requires the MCP SDK. Install requirements.txt first.
from mcp_file_server import (
    extract_text,
    list_supported_files,
    resolve_path,
    search_files,
    write_file,
)


def test_resolve_relative_path(tmp_path, monkeypatch):
    monkeypatch.setattr("filesystem_mcp_server.BASE_DIR", tmp_path)
    assert resolve_path("resumes") == (tmp_path / "resumes").resolve()


def test_list_supported_files(tmp_path, monkeypatch):
    monkeypatch.setattr("filesystem_mcp_server.BASE_DIR", tmp_path)
    (tmp_path / "a.txt").write_text("Python", encoding="utf-8")
    (tmp_path / "b.pdf").write_bytes(b"dummy")
    (tmp_path / "ignore.md").write_text("ignore", encoding="utf-8")

    files = list_supported_files(".")
    assert [item["name"] for item in files] == ["a.txt", "b.pdf"]


def test_extract_txt(tmp_path, monkeypatch):
    monkeypatch.setattr("filesystem_mcp_server.BASE_DIR", tmp_path)
    path = tmp_path / "resume.txt"
    path.write_text("Java and Python developer", encoding="utf-8")
    assert extract_text("resume.txt") == "Java and Python developer"


def test_search_files(tmp_path, monkeypatch):
    monkeypatch.setattr("filesystem_mcp_server.BASE_DIR", tmp_path)
    (tmp_path / "a.txt").write_text("Python developer", encoding="utf-8")
    (tmp_path / "b.txt").write_text("Java developer", encoding="utf-8")

    matches = search_files(".", "python")
    assert len(matches) == 1
    assert Path(matches[0]).name == "a.txt"


def test_write_file(tmp_path, monkeypatch):
    monkeypatch.setattr("filesystem_mcp_server.BASE_DIR", tmp_path)
    result = write_file("output/result.txt", "hello")

    assert result["status"] == "success"
    assert (tmp_path / "output/result.txt").read_text(encoding="utf-8") == "hello"


def test_missing_file(tmp_path, monkeypatch):
    monkeypatch.setattr("filesystem_mcp_server.BASE_DIR", tmp_path)
    with pytest.raises(FileNotFoundError):
        extract_text("missing.txt")
