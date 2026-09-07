Agentic Profile Matching - Workflow Diagram

1. Complete System Workflow

flowchart TD

    A[Job Description] --> B[LangGraph Hiring Agent]

    B --> C[load_job]

    C --> D[extract_requirements]

    D --> E[match_candidates]

    E --> F[AgentTools]

    F --> G[MCP Client]

    G -->|JSON-RPC 2.0 / MCP| H[MCP Filesystem Server]

    H --> I[Resume Files]

    I --> H
    H --> G
    G --> F

    F --> J[RAG Matching Engine]

    J --> K[Resume Embeddings]

    K --> L[ChromaDB]

    L --> J

    J --> M[Hybrid Candidate Ranking]

    M --> E

    E --> N[candidate_intelligence]

    N --> O[Candidate Comparison]

    O --> P[Interview Plan]

    P --> Q[Candidate Explanations]

    Q --> R[Final Hiring Report]

    R --> S[END]

2. LangGraph State Machine

The hiring agent is implemented as a sequential LangGraph state machine.

stateDiagram-v2

    [*] --> load_job

    load_job --> extract_requirements

    extract_requirements --> match_candidates

    match_candidates --> candidate_intelligence

    candidate_intelligence --> compare_candidates

    compare_candidates --> generate_interview_plan

    generate_interview_plan --> generate_explanations

    generate_explanations --> build_report

    build_report --> [*]

3. Agent ↔ MCP Interaction

The agent does not directly access resume files.

All filesystem interaction is abstracted through AgentTools, which communicates with the MCP client.

sequenceDiagram

    participant A as LangGraph Agent
    participant T as AgentTools
    participant C as MCP Client
    participant S as MCP Server
    participant F as Resume Files

    A->>T: Search / read / process resumes

    T->>C: Invoke MCP operation

    C->>S: JSON-RPC 2.0 MCP request

    S->>F: Access filesystem

    F-->>S: File data / metadata

    S-->>C: MCP response

    C-->>T: Structured result

    T-->>A: Candidate/file information

4. MCP Tool Discovery

The MCP client first discovers the capabilities exposed by the filesystem server.

sequenceDiagram

    participant C as MCP Client
    participant S as MCP Server

    C->>S: Initialize MCP connection

    S-->>C: Server capabilities

    C->>S: List tools

    S-->>C: Tool definitions

    C->>S: List resources

    S-->>C: Resource definitions

    C->>S: List resource templates

    S-->>C: Resource templates

    Note over C,S: Agent now knows which MCP capabilities are available

5. Resume Processing Flow

flowchart LR

    A[Resume File] --> B[MCP Filesystem Server]

    B --> C[MCP Client]

    C --> D[AgentTools]

    D --> E[Resume Loader]

    E --> F[Resume Chunker]

    F --> G[Embedding Model]

    G --> H[ChromaDB]

    H --> I[Vector Search]

    I --> J[Candidate Matching]

6. Candidate Evaluation Flow

flowchart TD

    A[Job Description]

    A --> B[Job Requirement Extraction]

    B --> C[Required Skills]

    B --> D[Required Experience]

    B --> E[Required Education]

    A --> F[Semantic Search]

    F --> G[Candidate Retrieval]

    C --> H[Skill Matching]

    D --> I[Experience Matching]

    E --> J[Education Matching]

    G --> K[Hybrid Ranking]

    H --> K
    I --> K
    J --> K

    K --> L[Top Candidates]

    L --> M[Candidate Intelligence]

    M --> N[Comparison]

    N --> O[Interview Plan]

    O --> P[Final Recommendation]

    P --> Q[Hiring Report]

7. Complete End-to-End Architecture

flowchart TB

    subgraph USER["User / Recruiter"]
        JD[Job Description]
    end

    subgraph AGENT["LangGraph Agent"]
        LG[HiringAgent]

        N1[load_job]
        N2[extract_requirements]
        N3[match_candidates]
        N4[candidate_intelligence]
        N5[compare_candidates]
        N6[generate_interview_plan]
        N7[generate_explanations]
        N8[build_report]

        LG --> N1
        N1 --> N2
        N2 --> N3
        N3 --> N4
        N4 --> N5
        N5 --> N6
        N6 --> N7
        N7 --> N8
    end

    subgraph TOOLS["Agent Integration Layer"]
        AT[AgentTools]
    end

    subgraph MCP["MCP Layer"]
        MC[MCP Client]
        MS[MCP Filesystem Server]

        MC --> MS
    end

    subgraph FILES["Filesystem"]
        RF[Resume Files]
    end

    subgraph RAG["RAG Matching Layer"]
        RL[Resume Loader]
        RC[Resume Chunker]
        EM[Embedding Model]
        VS[ChromaDB]
        JM[Hybrid Job Matcher]

        RL --> RC
        RC --> EM
        EM --> VS
        VS --> JM
    end

    subgraph OUTPUT["Hiring Output"]
        CR[Candidate Ranking]
        CI[Candidate Intelligence]
        IP[Interview Plan]
        FR[Final Hiring Report]
    end

    JD --> LG

    N3 --> AT

    AT --> MC
    MS --> RF
    MC --> AT

    AT --> RL
    JM --> N3

    N4 --> CI
    N5 --> CR
    N6 --> IP
    N8 --> FR

8. Key Design Boundary

The most important architectural separation is:

┌──────────────────────────────────────────────┐
│              LangGraph Agent                 │
│                                              │
│  Workflow orchestration and state management │
└───────────────────────┬──────────────────────┘
                        │
                        ▼
┌──────────────────────────────────────────────┐
│                 AgentTools                   │
│                                              │
│       Integration / adaptation layer         │
└───────────────┬──────────────────┬───────────┘
                │                  │
                ▼                  ▼
       ┌────────────────┐   ┌─────────────────┐
       │   MCP Client   │   │  RAG Matcher    │
       └───────┬────────┘   └─────────────────┘
               │
               ▼
       ┌────────────────┐
       │   MCP Server   │
       └───────┬────────┘
               │
               ▼
       ┌────────────────┐
       │ Resume Files   │
       └────────────────┘

This separation allows the agent workflow to remain independent from the underlying filesystem implementation.
