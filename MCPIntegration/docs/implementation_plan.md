# MCP Integration - Implementation Plan

## Goal

Complete the MCP migration with the minimum implementation required by the assignment while preserving the existing profile-matching functionality.

The project already has the filesystem and LangGraph components identified, and implementation of the MCP server and agent refactoring has been started. fileciteturn0file0L50-L61

---

## Phase 1 - MCP Filesystem Server

### Objective
Create the basic MCP server in `filesystem_mcp_server.py`.

### Tasks

- Set up the MCP server.
- Add server configuration.
- Implement MCP/JSON-RPC request handling.
- Expose resource discovery.
- Migrate existing filesystem operations:
  - Read file
  - Search files
  - Summarize file/content
- Add basic error handling.

### Deliverable

`filesystem_mcp_server.py` with working MCP filesystem operations.

### Completion Criteria

- Server starts.
- Client can discover resources.
- A file can be read through MCP.
- Search operation works.
- Errors are returned correctly.

---

## Phase 2 - Additional MCP Capabilities

### Objective
Implement the two required additional capabilities.

### Tasks

#### `watch_directory()`

- Accept/configure a directory.
- Detect newly added resume files.
- Expose the detected files through MCP.
- Keep implementation simple; avoid unnecessary infrastructure.

#### `batch_process()`

- Accept multiple files.
- Process them in one MCP request.
- Return the processed results.

### Deliverable

Working `watch_directory()` and `batch_process()` implementations.

### Completion Criteria

- New resume files can be detected.
- Multiple resumes can be processed in one operation.

---

## Phase 3 - Refactor LangGraph Agent

### Objective
Change `matching_agent.py` to consume filesystem functionality through MCP.

### Tasks

- Create/use an MCP client.
- Connect to `filesystem_mcp_server.py`.
- Discover available MCP resources.
- Replace direct filesystem utility calls with MCP calls.
- Keep the existing LangGraph workflow.
- Preserve:
  - Resume ingestion
  - Metadata extraction
  - Chunking
  - Embeddings
  - Vector storage
  - Profile matching

### Deliverable

Refactored `matching_agent.py`.

### Completion Criteria

```text
LangGraph Agent
      |
      v
   MCP Client
      |
      v
Filesystem MCP Server
      |
      v
Resume Files
```

The agent should no longer directly depend on the filesystem implementation.

---

## Phase 4 - Integration Testing

### Objective
Verify the complete system.

### Tests

1. Start MCP server.
2. Verify resource discovery.
3. Read a resume through MCP.
4. Search resumes through MCP.
5. Test `watch_directory()`.
6. Test `batch_process()`.
7. Start the LangGraph agent.
8. Run an end-to-end profile matching request.
9. Verify the final matching result.

### Deliverable

Tests under `tests/` covering the critical functionality.

### Completion Criteria

The complete flow works:

```text
Resume Files
     |
     v
MCP Filesystem Server
     |
     v
MCP Client
     |
     v
LangGraph Agent
     |
     v
Profile Matching Result
```

---

## Phase 5 - Documentation & Demo

### Objective
Prepare the final submission without adding unnecessary documentation overhead.

### Tasks

- Update `README.md` with:
  - Project overview
  - Setup
  - How to start MCP server
  - How to run the agent
  - Example workflow
- Add/update architecture diagram.
- Add a short explanation of MCP communication.
- Document test scenarios.
- Prepare the 5–6 minute demo.

### Demo Order

1. Explain the old tightly coupled approach.
2. Show MCP architecture.
3. Start the MCP server.
4. Show resource discovery.
5. Demonstrate a filesystem operation.
6. Demonstrate agent → MCP communication.
7. Run end-to-end profile matching.
8. Briefly show `watch_directory()` and `batch_process()`.

---

# Priority Order

Because of the time constraint, implement in this order:

| Priority | Phase | Importance |
|---|---|---|
| 1 | Phase 1 | Mandatory core |
| 2 | Phase 3 | Mandatory agent integration |
| 3 | Phase 2 | Mandatory assignment capabilities |
| 4 | Phase 4 | Mandatory validation |
| 5 | Phase 5 | Submission/demo |

## Skip/Defer

Do **not** spend significant time on bonus functionality until the core system is complete.

### Multi-MCP integration

Web Search MCP, Database MCP, and other external MCP servers are bonus functionality. The problem statement explicitly identifies multi-MCP integration as bonus work. fileciteturn0file1L81-L87

### Design principle

> **Get one complete MCP server + one working LangGraph MCP client + end-to-end execution working first.**

This satisfies the main migration goal while keeping the implementation manageable within the time constraint.
