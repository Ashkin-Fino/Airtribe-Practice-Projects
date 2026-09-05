# MCP Integration - Design

## 1. Overview

The project migrates the existing file-system utilities to a standardized **Model Context Protocol (MCP)** architecture.

The main components are:

- **LLMPoweredFileSystem** — provides file reading, searching, summarizing, and related file operations.
- **RAGBasedProfileMatching** — contains the LangGraph matching agent for resume ingestion, metadata extraction, chunking, embeddings, and vector storage.

The goal is to remove direct coupling between the agent and filesystem utilities. The agent will communicate with the filesystem through an MCP client/server interface.

## 2. High-Level Architecture

```text
                    +-----------------------------+
                    |      LangGraph Agent        |
                    |     matching_agent.py       |
                    +-------------+---------------+
                                  |
                                  | MCP Client
                                  | JSON-RPC 2.0
                                  v
                    +-----------------------------+
                    |       MCP File Server        |
                    | filesystem_mcp_server.py     |
                    +-------------+---------------+
                                  |
                                  v
                    +-----------------------------+
                    |   File System Utilities      |
                    | Read / Search / Summarize    |
                    | Watch / Batch Processing     |
                    +-------------+---------------+
                                  |
                                  v
                    +-----------------------------+
                    |       Resume Files           |
                    +-----------------------------+
```

## 3. MCP Server

### File

`filesystem_mcp_server.py`

The server exposes the existing filesystem functionality through MCP.

### Core capabilities

- Read file
- Search files
- Summarize file/content
- Discover available resources
- Watch a directory for new resume files
- Batch process multiple files
- Return standardized errors

The server should keep filesystem-specific logic inside the server rather than exposing filesystem implementation details to the agent.

## 4. MCP Client / Agent

### File

`matching_agent.py`

The LangGraph agent will:

1. Receive the matching request.
2. Connect to the MCP filesystem server.
3. Discover/use required MCP resources.
4. Request resume files or file operations through MCP.
5. Continue the existing resume-processing workflow.
6. Perform metadata extraction, chunking, embedding, vector storage, and matching.
7. Return the existing matching result.

The existing business logic should be preserved as much as possible.

## 5. Communication

The MCP server and client communicate using MCP with JSON-RPC 2.0 request/response semantics.

Basic flow:

```text
Agent
  |
  | initialize / discover resources
  v
MCP Server
  |
  | resource/tool response
  v
Agent
  |
  | process resume data
  v
Matching Result
```

The implementation should provide:

- Standard request handling
- Standard responses
- Error handling
- Resource discovery
- Configuration for server settings

## 6. Directory Watching

`watch_directory()` monitors a configured resume directory.

Minimal implementation:

1. Monitor the configured directory.
2. Detect newly added resume files.
3. Make detected files available through the MCP interface.
4. Allow the agent to process them.

The implementation should remain simple and avoid unnecessary background infrastructure.

## 7. Batch Processing

`batch_process()` accepts multiple files and processes them in one request.

Example flow:

```text
Multiple Resume Files
        |
        v
 batch_process()
        |
        v
Read / Extract / Return results
        |
        v
LangGraph Agent
```

This reduces repeated MCP calls when processing multiple resumes.

## 8. Error Handling

Handle common failures such as:

- File not found
- Invalid file/path
- Unsupported operation
- Invalid request
- MCP communication errors
- Processing errors

Errors should be returned in a clear, standardized response rather than causing the agent to fail silently.

## 9. Configuration

Keep configuration minimal and centralized.

Example settings:

- Resume directory
- Supported file extensions
- Server name
- Server port/transport if applicable
- Batch size

Avoid introducing additional configuration systems unless required.

## 10. Testing Strategy

Focus on the critical paths:

### MCP Server

- Server starts successfully.
- Resource discovery works.
- File reading works.
- File searching works.
- `watch_directory()` detects new files.
- `batch_process()` handles multiple files.
- Invalid requests return errors.

### Agent Integration

- Agent connects to MCP server.
- Agent no longer directly calls filesystem utilities.
- Resume processing still works.
- End-to-end profile matching works.

## 11. Future Extension

The architecture should allow additional MCP servers later, for example:

```text
                    +------------------+
                    |  LangGraph Agent |
                    +--------+---------+
                             |
              +--------------+--------------+
              |              |              |
              v              v              v
        Filesystem MCP   Web MCP       Database MCP
```

Multi-MCP integration is optional/bonus functionality and should not block the core implementation.
