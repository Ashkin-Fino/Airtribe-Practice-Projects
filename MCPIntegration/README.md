# MCP Integration - Phase 1

## Purpose

Phase 1 introduces the MCP filesystem server for the existing file-system functionality.
The LangGraph agent is intentionally not changed yet; that is Phase 3.

## Structure

```text
MCPIntegration/
├── filesystem_mcp_server.py
├── requirements.txt
├── README.md
└── tests/
    └── test_filesystem_mcp_server.py
```

The server is designed to run from the parent `Projects` directory so the eventual
agent can connect to the MCP server as a sibling project.

## Exposed MCP resources

- `file://{file_path}` - read TXT/PDF/DOCX content
- `directory://{directory_path}` - list supported files and metadata
- `filesystem://capabilities` - discover the server capabilities

## Exposed MCP tools

- `search_files(directory, keyword)`
- `write_file(file_path, content)`
- `summarize_file(file_path)`
- `generate_summary_file(source_file_path, output_file_path)`

Read-only data is exposed as MCP resources; operations such as search/write/summarize
are exposed as tools. MCP itself handles the JSON-RPC 2.0 protocol layer.

## Setup

From `Projects/MCPIntegration`:

```bash
pip install -r requirements.txt
```

Optional: set the filesystem root explicitly:

```bash
set FILESYSTEM_BASE_DIR=C:\path\to\Projects
```

On Linux/macOS:

```bash
export FILESYSTEM_BASE_DIR=/path/to/Projects
```

`FILESYSTEM_BASE_DIR` defaults to the current working directory.

## Run

From `Projects/MCPIntegration`:

```bash
python filesystem_mcp_server.py
```

The server uses stdio by default. This is intended for an MCP client/host to launch
as a subprocess.

For development, after installing the CLI extra, the MCP Inspector can be used:

```bash
mcp dev filesystem_mcp_server.py
```

## Test

```bash
pytest -q
```

## Phase 1 completion

- MCP server created
- Filesystem resources exposed
- Resource discovery supported by MCP
- File reading supported for TXT/PDF/DOCX
- Directory listing supported
- File search supported
- File writing supported
- File summarization supported
- Basic error handling implemented
- JSON-RPC 2.0 handled by the MCP SDK
