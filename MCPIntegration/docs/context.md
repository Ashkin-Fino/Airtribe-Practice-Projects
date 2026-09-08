# Context for MCP Integration Project

## Overview

This project aims to transition from tightly coupled custom tools to a standardized architecture using the **Model Context Protocol (MCP)**. The goal is to create a modular, scalable, and extensible system where AI agents interact with external resources through MCP servers. This will enable cleaner architecture, better modularity, and improved scalability.

---

## Key Components

1. **LLMPoweredFileSystem**:
   - Contains file system utilities such as file reading, searching, and summarizing.
   - These tools will be migrated into MCP resources and exposed via an MCP-compliant server.

2. **RAGBasedProfileMatching**:
   - Contains the LangGraph-based matching agent for resume ingestion, metadata extraction, chunking, embedding, and vector storage.
   - The agent will be refactored to consume MCP resources instead of directly interacting with file system utilities.

---

## Objectives

1. Implement an MCP-compliant filesystem server (`filesystem_mcp_server.py`).
2. Refactor the LangGraph agent (`matching_agent.py`) to use an MCP client.
3. Ensure modularity, scalability, and extensibility of the system.
4. Support additional MCP capabilities:
   - `watch_directory()`: Monitor directories for new files.
   - `batch_process()`: Process multiple files efficiently.

---

## Folder Structure (Confirmed)

- **LLMPoweredFileSystem**:
  - Contains file system utilities to be migrated into MCP resources.

- **RAGBasedProfileMatching**:
  - Contains the LangGraph-based matching agent to be refactored.

- **docs**:
  - Includes `problem_statement.md`, `context.md`, `architecture.md`, and `implementation_plan.md`.

- **tests**:
  - Contains test cases for MCP server and agent integration.

---

## Next Steps (Completed)

1. **Folder Structure Confirmation**:
   - Verified the existence of `LLMPoweredFileSystem` and `RAGBasedProfileMatching` directories.
   - Confirmed the presence of expected files and subdirectories.

2. **Utility Identification**:
   - Reviewed `LLMPoweredFileSystem` for file-related utilities (e.g., file reading, searching, summarizing).
   - Reviewed `RAGBasedProfileMatching` for resume ingestion, metadata extraction, chunking, embedding, and vector storage functionalities.

3. **Implementation Initiation**:
   - Began implementing the MCP server (`filesystem_mcp_server.py`).
   - Started refactoring the LangGraph agent (`matching_agent.py`) to use MCP resources.

---

## Expected Outcome

Upon completion of this project, the system will evolve from using tightly coupled custom tools to a standardized MCP-based architecture. The LangGraph agent will interact with external resources through MCP servers, enabling a cleaner, more extensible, and production-ready system that can easily integrate additional services in the future.