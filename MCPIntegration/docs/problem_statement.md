# Problem Statement

## Overview

Modern AI agents often rely on custom-built tools to interact with external systems such as file storage, databases, and web services. While functional, these custom integrations tightly couple the agent to specific implementations, making the system difficult to extend, maintain, and reuse.

The **Model Context Protocol (MCP)** provides a standardized interface for exposing tools and resources to AI agents. By replacing custom tools with MCP servers, agents can interact with external resources using a common protocol, resulting in cleaner architecture, better modularity, and improved scalability.

This assignment focuses on migrating the existing file system tools from previous milestones into an MCP server and refactoring the LangGraph-based matching agent to consume these tools through an MCP client.

---

# Objectives

The project aims to achieve the following objectives:

- Understand the fundamentals of the **Model Context Protocol (MCP)**.
- Replace custom file system tools with standardized MCP resources.
- Implement a production-ready MCP server following the JSON-RPC 2.0 specification.
- Refactor the existing LangGraph agent to communicate with the MCP server instead of directly accessing file system utilities.
- Design a modular architecture that can be extended with multiple MCP servers in the future.

---

# Project Tasks

## Part A – MCP Server Implementation (50%)

Create a new file named:

```
filesystem_mcp_server.py
```

### Task 1: Convert File System Tools into MCP Resources

Migrate all file system utilities developed in Milestone 1 into MCP resources by implementing the Model Context Protocol.

The server should expose all existing file operations while providing a standardized interface for AI agents.

### Task 2: Implement Additional MCP Capabilities

Extend the server with the following capabilities:

- **watch_directory()**
  - Continuously monitor a directory for newly added resume files.
  - Notify or expose new files through MCP resources.

- **batch_process()**
  - Efficiently process multiple files in a single operation.
  - Reduce repeated overhead when handling large batches of resumes.

### Task 3: MCP Server Features

The MCP server should include:

- JSON-RPC 2.0 compliant communication
- Standard request and response handling
- Proper error handling and status codes
- Resource discovery endpoints
- Configuration management for server settings

---

## Part B – Agent Refactoring (30%)

Update the existing:

```
matching_agent.py
```

### Task 1: Replace Custom File System Tools

Refactor the LangGraph agent to:

- Remove direct interactions with file system utilities.
- Connect to the MCP server through an MCP client.
- Preserve all existing functionality while improving modularity and maintainability.

### Task 2: Multi-MCP Integration (Bonus)

Extend the agent to communicate with multiple MCP servers, such as:

- Web Search MCP Server
- Database MCP Server
- Other external MCP-compatible services

Demonstrate the agent using resources from more than one MCP server.

---

# Functional Requirements

The solution should:

- Implement an MCP-compliant filesystem server.
- Expose all file system operations as MCP resources.
- Support JSON-RPC 2.0 communication.
- Allow resource discovery.
- Provide robust error handling.
- Support directory monitoring.
- Support batch file processing.
- Enable the LangGraph agent to consume MCP resources.
- Maintain all functionality from previous milestones.

---

# Non-Functional Requirements

The implementation should be:

- Modular
- Scalable
- Extensible
- Easy to maintain
- Standards-compliant
- Well documented
- Production-ready

---

# Deliverables

The final submission should include the following:

1. **filesystem_mcp_server.py**
   - MCP-based filesystem server implementation.

2. **matching_agent.py**
   - Refactored LangGraph agent using an MCP client.

3. **JSON-RPC 2.0 compliant MCP server**
   - Including resource discovery support.

4. **Implementation of:**
   - `watch_directory()`
   - `batch_process()`

5. **Agent Workflow Diagram**
   - A state machine or workflow diagram illustrating communication between the LangGraph agent and the MCP server.

6. **Test Scenarios**
   - Demonstrate MCP resource discovery.
   - Validate MCP tool invocation.
   - Show successful end-to-end agent execution.

7. **Demo Video (5–6 minutes)**

   The demonstration should cover:

   - MCP server implementation
   - Resource discovery
   - Agent integration
   - End-to-end execution
   - Usage of MCP resources

---

# Expected Outcome

Upon completion of this assignment, the project should evolve from using tightly coupled custom tools to a standardized MCP-based architecture. The LangGraph agent should interact with external resources through MCP servers, enabling a cleaner, more extensible, and production-ready system that can easily integrate additional services in the future.