Agentic Profile Matching

An agentic candidate profile-matching system that combines LangGraph, MCP (Model Context Protocol), and a RAG-based profile matching engine to create an end-to-end hiring workflow.

The system allows an AI hiring agent to discover and access resume files through an MCP filesystem server, process candidate profiles using a semantic/RAG matching pipeline, and orchestrate multiple candidate-evaluation steps using LangGraph.

1. Project Overview

Traditional profile matching systems generally perform a single matching operation:

Job Description
       ↓
Resume Matching
       ↓
Candidate Ranking

This project extends that approach into an agentic workflow:

Job Description
       ↓
LangGraph Hiring Agent
       ↓
Requirement Extraction
       ↓
Candidate Matching
       ↓
Candidate Intelligence
       ↓
Candidate Comparison
       ↓
Interview Plan
       ↓
Final Hiring Report

The agent uses an MCP filesystem server as its standardized interface for interacting with resume files.

2. Key Technologies

Python

LangGraph – agent workflow orchestration

MCP (Model Context Protocol) – filesystem tool/resource integration

JSON-RPC 2.0 – communication protocol used by MCP

ChromaDB – vector database

Sentence Transformers – resume embeddings

RAG-based semantic search

Hybrid candidate matching

MCP Client/Server architecture

3. Architecture

The project consists of three major layers.

MCP Layer

Responsible for interacting with the filesystem.

LangGraph Agent
      ↓
AgentTools
      ↓
MCP Client
      ↓
MCP Server
      ↓
Resume Files

Matching Layer

The existing RAG-based matching engine is responsible for:

Resume loading

Resume chunking

Embedding generation

Vector search

Semantic matching

Skill matching

Experience matching

Candidate ranking

Agent Layer

LangGraph orchestrates the complete hiring workflow.

load_job
    ↓
extract_requirements
    ↓
match_candidates
    ↓
candidate_intelligence
    ↓
compare_candidates
    ↓
generate_interview_plan
    ↓
generate_explanations
    ↓
build_report

4. MCP Filesystem Server

The MCP filesystem server provides standardized access to resume files.

MCP Tools

Tool

Purpose

search_files

Search files using a keyword

write_file

Create/write files

summarize_file

Generate a summary of a file

generate_summary_file

Generate and save a summary

watch_directory

Monitor a directory for changes

batch_process

Process multiple files in a single operation

MCP Resources

The server also supports resource discovery.

Examples:

filesystem://capabilities
file://{+file_path}
directory://{+directory_path}

This allows the client to discover available resources and interact with files through the MCP interface.

5. Agent ↔ MCP Interaction

The LangGraph agent does not directly access resume files.

Instead, file access is abstracted through AgentTools and the MCP client.

                 Hiring Agent
                      │
                      ▼
                 AgentTools
                      │
                      ▼
                MCP Client
                      │
              JSON-RPC / MCP
                      │
                      ▼
                MCP Server
                      │
                      ▼
                 File System
                      │
                      ▼
                   Resumes

This separation keeps the agent independent of the underlying filesystem implementation.

6. LangGraph Workflow

The hiring agent is implemented as a sequential LangGraph state machine.

START
  │
  ▼
load_job
  │
  ▼
extract_requirements
  │
  ▼
match_candidates
  │
  ▼
candidate_intelligence
  │
  ▼
compare_candidates
  │
  ▼
generate_interview_plan
  │
  ▼
generate_explanations
  │
  ▼
build_report
  │
  ▼
END

The agent maintains a shared state containing:

Job description

Extracted job requirements

Candidate matching results

Candidate intelligence

Comparison results

Interview plan

Final report

Execution reasoning

7. Candidate Matching Pipeline

The matching engine combines semantic and structured matching.

Resume
  ↓
Resume Loader
  ↓
Resume Chunking
  ↓
Embeddings
  ↓
ChromaDB
  ↓
Semantic Search
  ↓
Skill / Experience / Education Analysis
  ↓
Hybrid Ranking
  ↓
Top Candidates

The matching engine uses the existing RAG-based profile matching implementation.

The final candidate ranking incorporates multiple factors including:

Semantic similarity

Required skills

Experience

Education

Overall match score

8. Agent Candidate Intelligence

After candidate matching, the agent enriches each candidate with additional information.

For each candidate the system generates:

Match category

Overall score

Strengths

Weaknesses

Risk level

Candidate summary

Matching reasoning

Example:

Candidate: John Doe

Match Score: 87.4
Category: Strong Match
Risk Level: LOW

Strengths:
- Meets required experience
- Strong overall matching score
- Relevant technical background

Potential Weaknesses:
- Areas requiring validation during interview

9. Candidate Comparison

The agent compares the matched candidates and produces a ranked list.

Example:

Candidate Ranking

1. John Doe
   Score: 87.4
   Risk: LOW

2. Alice Smith
   Score: 72.1
   Risk: MEDIUM

The system also produces a final recommendation based on the candidate ranking.

10. Interview Plan

The agent generates an interview plan for each candidate.

The interview plan contains:

Technical focus areas

Candidate-specific questions

Areas requiring validation

Potential profile gaps

Example:

Interview Plan: John Doe

Focus Areas:
- Python
- Java
- Spring Boot
- AWS

Questions:
- Walk me through a project relevant to this role.
- Explain your experience with Python.
- Explain your experience with Java.
- Describe a backend problem you solved.

11. Final Hiring Report

The final report combines all information generated during the workflow.

Job Requirements
       +
Candidate Ranking
       +
Candidate Intelligence
       +
Comparison
       +
Final Recommendation
       +
Interview Plan

The final output is returned as a structured Python dictionary and can be consumed by another application or presentation layer.

12. Project Structure

AgenticProfileMatching/
│
├── agent/
│   ├── agent_tools.py
│   ├── agent_state.py
│   ├── candidate_models.py
│   ├── config.py
│   ├── graph_builder.py
│   ├── graph_nodes.py
│   └── mcp_file_system_client.py
│
├── matching_agent.py
├── main.py
├── demo.py
├── test_phase3.py
├── job_description.txt
│
├── docs/
│   ├── workflow.md
│   └── testing.md
│
└── README.md

The MCP server is implemented as part of the MCP integration layer and communicates with the agent through the MCP client.

13. Running the Project

Prerequisites

The project requires:

Python 3.x

Required Python dependencies

MCP filesystem server

Resume files

ChromaDB vector store

Run the Agent

python main.py

The agent reads the job description and executes the complete LangGraph workflow.

Run the Demo

A dedicated demo script demonstrates the complete workflow with readable log messages and delays between major steps.

python demo.py

The demo covers:

MCP Discovery
     ↓
MCP Search
     ↓
MCP Batch Processing
     ↓
Resume Indexing
     ↓
Job Description
     ↓
LangGraph Agent
     ↓
Requirement Extraction
     ↓
Candidate Matching
     ↓
Candidate Intelligence
     ↓
Candidate Comparison
     ↓
Interview Plan
     ↓
Final Hiring Report

14. Testing

The project includes an end-to-end test suite:

python test_phase3.py

Test Scenarios

#

Scenario

Purpose

1

MCP Discovery

Verify MCP tools/resources can be discovered

2

MCP Read

Verify file/resource reading

3

MCP Search

Verify resume search

4

MCP Batch

Verify batch processing

5

Resume Indexing

Verify MCP + RAG integration

6

Requirement Extraction

Verify job requirement processing

7

Candidate Matching

Verify candidate retrieval

8

Candidate Intelligence

Verify candidate enrichment

9

Candidate Comparison

Verify candidate ranking

10

Interview Plan

Verify interview-plan generation

11

Final Report

Verify final report generation

12

Full LangGraph Agent

Verify complete end-to-end workflow

A successful execution should report all scenarios as passed.

15. Deliverables

This project satisfies the following deliverables:

MCP-based filesystem server implementation

Refactored LangGraph agent with MCP client integration

JSON-RPC 2.0 compliant MCP communication with resource discovery

watch_directory() capability

batch_process() capability

Agent ↔ MCP workflow/state-machine documentation

End-to-end test scenarios

Dedicated 5–6 minute demonstration script

Deliverable Mapping

Deliverable

Project Component

MCP filesystem server

MCP filesystem server

LangGraph agent

matching_agent.py

MCP client integration

agent/mcp_file_system_client.py

Agent integration layer

agent/agent_tools.py

Workflow/state machine

docs/workflow.md

Test scenarios

test_phase3.py

Demo

demo.py

16. Design Principles

Separation of Responsibilities

MCP Server
    → File access

MCP Client
    → MCP communication

AgentTools
    → Integration/adaptation

RAG Matcher
    → Candidate matching

LangGraph
    → Workflow orchestration

Final Report
    → Hiring decision output

Reuse Existing Components

The agent builds on the existing RAG profile matching implementation instead of duplicating its functionality.

Tool-Based Agent Architecture

Filesystem operations are exposed as tools through MCP rather than being hard-coded directly into the agent workflow.

Stateful Workflow

LangGraph maintains the state of the hiring process across multiple processing stages.

17. Future Improvements

Possible future enhancements include:

LLM-powered candidate explanations

LLM-generated personalized interview questions

More sophisticated candidate risk analysis

Real-time resume ingestion using watch_directory()

Web UI for recruiters

Persistent hiring reports

Human-in-the-loop candidate approval

Additional MCP tools for HR workflows

Multi-agent candidate evaluation

18. Conclusion

The project demonstrates how MCP, RAG, and LangGraph can be combined to create an agentic hiring workflow.

MCP provides standardized access to candidate files, the RAG pipeline provides semantic profile matching, and LangGraph orchestrates the complete decision-making workflow.

The resulting system moves beyond simple resume matching toward a reusable agent architecture capable of:

Discover
   ↓
Retrieve
   ↓
Understand
   ↓
Match
   ↓
Analyze
   ↓
Compare
   ↓
Plan
   ↓
Recommend

The complete system can be demonstrated using:

python demo.py