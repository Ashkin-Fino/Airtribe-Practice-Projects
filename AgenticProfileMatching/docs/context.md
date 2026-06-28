# context.md — Hiring Agent Project (Milestone 3)

## 1) Project Identity

This project is the **third milestone** built on top of the first two completed milestones:

- **Milestone 1:** LLMPoweredFileSystem
- **Milestone 2:** RAGBasedProfileMatching
- **Milestone 3 (current project):** **LangGraph-based Hiring / Matching Agent**

The purpose of this document is to act as the **single source of context** for the current project.  
It combines:

1. The problem statement and requirements of **Milestone 3**
2. The architecture and implementation details of **Milestone 1**
3. The architecture and implementation details of **Milestone 2**
4. The way Milestone 3 should **reuse and orchestrate** Milestones 1 and 2

In future chats for this project, this file should be sufficient as the main reference document.

---

# 2) Big Picture: What This New Project Is

## Core Idea

The new project is an **AI hiring assistant / matching agent** that helps a recruiter or hiring manager interactively find and evaluate candidates from a resume corpus.

It is **not** a completely new system from scratch. Instead, it is an **agent layer** built on top of:

- the **file understanding and file tools** from **Milestone 1**
- the **resume indexing, semantic retrieval, ranking, and reasoning pipeline** from **Milestone 2**

The new system should behave like an **interactive recruiting copilot** that can:

- understand natural-language hiring requests
- search and rank candidates from the resume database
- compare candidates
- explain why a candidate ranked high or low
- allow requirement changes in the middle of the conversation
- re-run matching and explain what changed
- support multi-round screening and final hire/no-hire style recommendations

---

# 3) Milestone 3 Problem Statement Summary

## Assignment Requirements Overview

The project is divided into 3 main parts:

---

## Part A — Agent Architecture (40%)

Create **`matching_agent.py`** using **LangGraph**.

### Agent State Design must support:
- **Conversation history**
- **Current understanding of job requirements**
- **Candidate shortlist**
- **Reasoning / explanation artifacts**

### Required graph workflow

```text
START
  → Parse JD
  → Extract Requirements
  → Search Resumes
  → Rank Candidates
  → Generate Report
  → Human Feedback Loop
  → END
```

### Tools available to the agent

#### From Milestone 1
- File system tools / file assistant capabilities

#### From Milestone 2
- RAG search and candidate matching capabilities

#### Additional tools required in Milestone 3
- `extract_requirements(jd: str)`
  - Parse must-have vs nice-to-have requirements from a job description
- `compare_candidates(candidate_ids: list)`
  - Perform side-by-side comparison of selected candidates
- `generate_interview_questions(candidate_id: str)`
  - Generate screening / interview questions for a candidate based on gaps and fit

---

## Part B — Interactive Features (30%)

### Conversational interface
The system must accept natural language recruiter queries such as:
- “Find me candidates with React and 3+ years experience”
- “Compare the top 3 matches side by side”
- “Why did John rank higher than Jane?”

### Iterative refinement
The system must support changing requirements during the conversation:
- user updates criteria
- agent re-ranks candidates
- agent explains ranking changes

---

## Part C — Advanced Capabilities (30%)

### Multi-round screening
The system should support progressive screening, for example:
1. **Round 1:** Retrieve top 10 from 100 resumes
2. **Round 2:** Deep analysis of the top 10
3. **Round 3:** Produce final hire / no-hire style recommendations

### Explainability
The system must generate:
- detailed match reports
- strengths and gaps per candidate
- suggestions for borderline candidates

---

## Submission Expectations
The assignment expects:
- LangGraph-based agent implementation
- state machine / graph diagram
- chat interface (CLI or Streamlit/Gradio)
- at least **5 test conversation scenarios**
- a **5–6 minute demo video** showing agent reasoning

---

# 4) How Milestone 3 Builds on Earlier Milestones

Milestone 3 should **not duplicate** the logic already implemented in Milestones 1 and 2.  
Instead, it should **compose** them.

## Milestone 1 contributes
A generic **LLM-powered file assistant layer** that can:
- read files / folders
- search files
- summarize files
- process natural language file requests

## Milestone 2 contributes
A domain-specific **resume RAG + candidate matching engine** that can:
- load resumes
- extract metadata
- chunk resumes
- create embeddings
- store vectors in ChromaDB
- retrieve candidate matches for a job description
- score and rank candidates
- provide reasoning for matches

## Milestone 3 contributes
An **agent orchestration layer** that:
- maintains recruiter conversation state
- decides when to use Milestone 1 tools vs Milestone 2 tools
- supports iterative refinement and comparison workflows
- turns one-shot matching into a multi-turn recruiting assistant

---

# 5) Milestone 1 Context — LLMPoweredFileSystem

# 5.1 Purpose of Milestone 1

Milestone 1 created an **LLM-powered file assistant** that can process natural language queries and execute file-related operations.

Its job was to bridge:
- **natural language user requests**
- **file system operations / file analysis tools**

It serves as a reusable utility layer for any project that needs to:
- read files
- search across files
- summarize files
- use LLMs to route user requests to the correct file operation

---

# 5.2 Milestone 1 High-Level Architecture

Milestone 1 architecture had 4 layers:

## 1. User Interaction Layer
Purpose:
- accept user queries in natural language
- display results

Possible interface:
- CLI
- simple GUI

## 2. Query Processing Layer
Purpose:
- understand the user query
- identify the intent
- choose the correct tool

Responsibilities:
- natural language understanding using LLM
- query classification
- tool selection

## 3. Tool Execution Layer
Purpose:
- execute the selected file operation

Examples:
- read files
- search files for keyword / content
- summarize files

## 4. Data Layer
Purpose:
- interact with the local file system

Responsibilities:
- file access
- file retrieval
- file format handling

---

# 5.3 Milestone 1 Core Modules

## `llm_file_assistant.py`
Entry point of the application.

Responsibilities:
- accept user input
- send query for processing
- receive tool result
- display output

## `query_processor.py`
Responsibilities:
- process user query
- identify intent
- map intent to a tool/function

## `file_tools.py`
Responsibilities:
- actual file operations

Expected functions:
- `read_files_in_folder(folder_path)`
- `search_files_for_keyword(folder_path, keyword)`
- `summarize_file(file_path)`

## `llm_integration.py`
Responsibilities:
- communicate with LLM API
- send prompts / queries
- receive and parse responses

---

# 5.4 Milestone 1 Workflow

Typical Milestone 1 flow:

1. User asks something like:
   - “Read all resumes in the resumes folder”
   - “Find files mentioning Python”
   - “Summarize resume_john_doe.pdf”

2. `llm_file_assistant.py` receives the request

3. `query_processor.py` determines intent:
   - read folder
   - search keyword
   - summarize file

4. Correct tool in `file_tools.py` is invoked

5. Results are returned to the user

---

# 5.5 Milestone 1 Error Handling Expectations

Milestone 1 must handle:
- invalid / unsupported queries
- missing file paths
- file not found errors
- parsing failures

The assistant should either:
- return a helpful error
- ask the user to rephrase
- surface a fallback explanation

---

# 5.6 Milestone 1 Technology and Extensibility

## Tech stack
- Python
- LLM API integration (e.g. OpenAI / Groq style integration)
- file libraries such as `os`, `PyPDF2`, etc.

## Planned extensibility
- add `.docx` support
- support more LLM providers
- build a web UI later

---

# 5.7 Milestone 1 Implementation Plan Summary

## Phase 1 — Project setup
- create project structure
- set up environment
- define module interfaces
- create basic CLI

## Phase 2 — LLM integration
- connect to LLM provider
- test prompt/response flow
- build query classification

## Phase 3 — File operations
- implement file reading
- implement keyword search
- implement summarization

## Phase 4 — End-to-end integration
- connect CLI + query processing + tools
- test complete workflows

## Phase 5 — Documentation and testing
- write docs
- write unit tests
- run integration tests

## Phase 6 — Deployment / extensibility
- package project
- document usage
- plan future enhancements

---

# 5.8 What Milestone 1 Gives to Milestone 3

Milestone 3 can reuse Milestone 1 for **general file-oriented tasks** such as:

- reading raw resume files when needed
- summarizing a resume or report
- searching supporting documents or job descriptions in folders
- inspecting artifacts generated by the pipeline
- exposing file-level tools to the agent when the recruiter asks file-related questions

Examples inside Milestone 3:
- “Summarize this candidate’s full resume”
- “Search all resumes for Kubernetes certifications”
- “Read the uploaded job description file”
- “Open the report generated for the top 5 candidates”

---

# 6) Milestone 2 Context — RAGBasedProfileMatching

# 6.1 Purpose of Milestone 2

Milestone 2 created the **resume retrieval and profile matching engine**.

Its purpose was to take:
- a collection of resumes
- a job description or hiring criteria

and produce:
- top candidate matches
- scores / rankings
- explainable reasons

Milestone 2 is the **domain engine** of the overall hiring system.

---

# 6.2 Milestone 2 High-Level Architecture

Milestone 2 has two major components:

## A. Resume Ingestion & Indexing Pipeline (`resume_rag.py`)
Responsible for preparing resumes for retrieval.

## B. Job Matching & Retrieval Engine (`job_matcher.py`)
Responsible for searching, scoring, filtering, and explaining candidate matches.

---

# 6.3 Milestone 2 Pipeline Overview

```text
Resume Files
   ↓
Resume Loader
   ↓
Metadata Extraction
   ↓
Section Chunking
   ↓
Embedding Generation
   ↓
ChromaDB Vector Store
   ↓
Job Matcher / Retrieval
   ↓
Ranking & Filtering
   ↓
Final Recommendations
```

---

# 6.4 Milestone 2 Detailed Components

## 6.4.1 ResumeLoader
Purpose:
- load resumes from the filesystem

Supported formats:
- TXT
- PDF
- DOCX

Methods:
- `load_txt()`
- `load_pdf()`
- `load_docx()`
- `load_resume()`

Typical output:
```python
{
    "file_name": "...",
    "file_path": "...",
    "content": "..."
}
```

---

## 6.4.2 MetadataExtractor
Purpose:
- extract candidate metadata from resume text

Expected fields:
```python
{
    "name": "",
    "skills": [],
    "experience_years": 0,
    "education": ""
}
```

Suggested techniques:
- regex
- keyword matching
- rule-based parsing
- lightweight NLP

---

## 6.4.3 ResumeChunker
Purpose:
- split resume into retrieval-friendly sections

Typical sections:
- Summary
- Skills
- Experience
- Education
- Certifications
- Projects

Typical output:
```python
{
    "chunk_id": "",
    "section": "Experience",
    "text": "..."
}
```

---

## 6.4.4 EmbeddingService
Purpose:
- generate semantic embeddings for resume chunks

Recommended model:
- `sentence-transformers/all-MiniLM-L6-v2`

Methods:
- `generate_embedding(text)`
- `generate_embeddings(chunks)`

---

## 6.4.5 VectorStore
Purpose:
- store embeddings and metadata in a vector database

Recommended database:
- **ChromaDB**

Stored data shape:
```python
{
    "id": "",
    "embedding": [],
    "document": "...",
    "metadata": {
        "name": "",
        "skills": [],
        "experience_years": 0,
        "education": "",
        "section": ""
    }
}
```

Persistence location:
- `data/chroma_db/`

---

## 6.4.6 Resume Indexing Pipeline
Purpose:
- create the full indexing workflow

Flow:
```text
Resume
   ↓
Loader
   ↓
Metadata Extractor
   ↓
Chunker
   ↓
Embedding Generator
   ↓
ChromaDB
```

Public methods:
- `index_resume(path)`
- `index_directory(resume_folder)`

---

## 6.4.7 JobDescriptionProcessor
Purpose:
- parse job descriptions into structured requirements

Expected extracted fields:
```python
{
    "skills": ["Python", "AWS"],
    "experience": 5,
    "education": "Bachelor"
}
```

Responsibilities:
- extract required skills
- extract experience requirements
- extract education requirements

---

## 6.4.8 SemanticSearcher
Purpose:
- find similar resumes using embedding similarity

Flow:
```text
Job Description
    ↓
Generate Embedding
    ↓
Vector Similarity Search
    ↓
Top K Results
```

Typical retrieval:
- `top_k = 10`

---

## 6.4.9 HybridSearcher
Purpose:
- combine semantic and symbolic matching

Inputs:
- semantic similarity score
- skill overlap score
- experience match score

Suggested formula:
```text
Final Score =
70% Semantic Similarity
20% Skill Match
10% Experience Match
```

---

## 6.4.10 RequirementFilter
Purpose:
- filter candidates who do not satisfy mandatory constraints

Examples:
- Python >= 5 years
- AWS certification
- React experience

Possible behavior:
- hard exclude candidate
- or apply heavy score penalty

---

## 6.4.11 RankingEngine
Purpose:
- convert retrieval output into business-friendly ranking scores

Output scale:
- 0–100

Suggested score bands:
- 90–100 → Excellent
- 75–89 → Strong
- 60–74 → Moderate
- 0–59 → Weak

---

## 6.4.12 MatchReasoner
Purpose:
- explain candidate selection

Expected output:
```python
{
    "matched_skills": [],
    "relevant_excerpts": [],
    "reasoning": "..."
}
```

Reasoning should be grounded in retrieved chunks.

---

## 6.4.13 Final Response Generator
Purpose:
- produce final structured matching output

Example:
```json
{
  "job_description": "...",
  "top_matches": []
}
```

---

# 6.5 Milestone 2 Assumptions

- resumes are in English
- dataset size is small to medium
- real-time updates are not required
- vector DB is local ChromaDB
- resume corpus lives under a `resumes/` directory

---

# 6.6 Milestone 2 Implementation Plan Summary

## Phase 1 — Foundation
Create project structure and basic interfaces.

## Phase 2 — Resume ingestion
Implement loaders for TXT / PDF / DOCX.

## Phase 3 — Metadata extraction
Extract candidate fields.

## Phase 4 — Chunking
Split resumes into semantic sections.

## Phase 5 — Embeddings
Generate vector representations.

## Phase 6 — Vector DB
Persist embeddings and metadata in ChromaDB.

## Phase 7 — Resume indexing pipeline
Build end-to-end ingestion pipeline.

## Phase 8 — Job description processing
Parse job requirements.

## Phase 9 — Semantic search
Retrieve top similar candidates.

## Phase 10 — Hybrid search
Combine semantic similarity with structured matching.

## Phase 11 — Requirement filtering
Remove or penalize unsuitable candidates.

## Phase 12 — Ranking
Produce normalized candidate scores.

## Phase 13 — Reasoning
Generate explainable match reasoning.

## Phase 14 — Final output
Return final top matches in assignment format.

---

# 6.7 What Milestone 2 Gives to Milestone 3

Milestone 2 is the **core matching backend** that Milestone 3 should call whenever the user asks questions like:

- “Find me backend engineers with Python and AWS”
- “Show top 10 candidates for this JD”
- “Which candidates have React and 3+ years experience?”
- “Why is Candidate A ranked above Candidate B?”
- “Generate a shortlist for this role”

Milestone 3 should treat Milestone 2 as the **retrieval + scoring + reasoning engine**.

---

# 7) Milestone 3 Functional Goals

Milestone 3 should transform Milestone 2’s one-shot job matching into a **multi-turn hiring agent**.

## Main user-facing goals
The user should be able to:
1. give a full job description or a short hiring query
2. get a ranked shortlist
3. ask follow-up questions about specific candidates
4. compare candidates
5. refine requirements
6. trigger a second screening round
7. request interview questions
8. ask why rankings changed
9. ask for strengths, gaps, and recommendations
10. get a final recommendation report

---

# 8) Recommended Milestone 3 Scope and Responsibilities

Milestone 3 should primarily introduce the following new layer:

## `matching_agent.py`
This is the main orchestration layer.

### Responsibilities
- maintain conversation state
- interpret recruiter intent
- call the right tools
- coordinate retrieval, ranking, comparison, and explanation
- manage human feedback loop
- support multi-round evaluation

Potentially, Milestone 3 may also include helper modules if you want cleaner separation, for example:
- `agent_tools.py`
- `state_models.py`
- `reporting.py`
- `ui_cli.py` or `app.py`

But the assignment explicitly requires **`matching_agent.py`**.

---

# 9) Recommended Milestone 3 Project Structure

A clean project structure could look like this:

```text
HiringAgentProject/
│
├── resumes/
├── data/
│   └── chroma_db/
│
├── docs/
│   ├── problem_statement.md
│   ├── context.md
│   ├── architecture.md
│   └── implementation_plan.md
│
├── matching_agent.py                 # main LangGraph agent
├── agent_tools.py                    # tool wrappers used by agent
├── state_models.py                   # state schema / dataclasses / typed dicts
├── reporting.py                      # report generation helpers
├── ui_cli.py                         # optional CLI chat interface
├── app.py                            # optional Streamlit/Gradio UI
│
├── milestone1/
│   ├── llm_file_assistant.py
│   └── modules/
│       ├── query_processor.py
│       ├── file_tools.py
│       └── llm_integration.py
│
├── milestone2/
│   ├── resume_rag.py
│   ├── job_matcher.py
│   └── ...
│
├── tests/
│   ├── test_agent_flow.py
│   ├── test_requirement_refinement.py
│   ├── test_compare_candidates.py
│   ├── test_interview_questions.py
│   └── ...
│
├── requirements.txt
└── README.md
```

If Milestones 1 and 2 already exist in the same repository, the actual layout can be adapted.  
The important part is that Milestone 3 can **import and call** their reusable components.

---

# 10) Milestone 3 Architecture — Recommended Design

## 10.1 High-Level Architecture

Milestone 3 should have 4 conceptual layers:

### A. Conversation / UI Layer
Handles:
- recruiter messages
- chat session
- display of rankings, comparisons, explanations, and follow-up outputs

Possible interface:
- CLI
- Streamlit
- Gradio

### B. Agent Orchestration Layer
Implemented with **LangGraph**.

Handles:
- state tracking
- routing
- tool invocation
- iterative refinement
- feedback loop

This is the heart of the project.

### C. Matching / Retrieval Layer
Mostly Milestone 2 functionality:
- parse JD
- search vector store
- rank candidates
- generate reasoning

### D. File / Document Utility Layer
Mostly Milestone 1 functionality:
- read files
- search documents
- summarize files
- inspect generated reports / resumes / JD files

---

# 11) Milestone 3 LangGraph Design

## 11.1 Required Base Flow

The assignment specifies this graph:

```text
START
  → Parse JD
  → Extract Requirements
  → Search Resumes
  → Rank Candidates
  → Generate Report
  → Human Feedback Loop
  → END
```

## 11.2 Practical Interpretation of Each Node

### 1. Parse JD
Input:
- full job description text
- or recruiter’s natural language query

Responsibilities:
- normalize the input
- detect whether it is a full JD or a short requirement query
- prepare text for requirement extraction

### 2. Extract Requirements
Responsibilities:
- identify must-have skills
- identify nice-to-have skills
- infer years of experience requirement
- infer education / certification requirements
- store structured requirements in state

This may call:
- Milestone 2 `JobDescriptionProcessor`
- Milestone 3 helper `extract_requirements(jd: str)`

### 3. Search Resumes
Responsibilities:
- query Milestone 2 retrieval engine
- get top candidate matches
- gather candidate evidence chunks

This should call:
- semantic search
- hybrid search
- filtering logic

### 4. Rank Candidates
Responsibilities:
- compute / normalize scores
- apply requirement filters
- build shortlist

This may use:
- `HybridSearcher`
- `RequirementFilter`
- `RankingEngine`

### 5. Generate Report
Responsibilities:
- produce recruiter-friendly output
- summarize why each candidate matched
- highlight strengths / gaps
- optionally include category labels like Excellent / Strong / Moderate / Weak

### 6. Human Feedback Loop
Responsibilities:
- accept follow-up user instructions
- refine criteria
- compare candidates
- generate interview questions
- rerun ranking if needed

This is where the system becomes an **interactive agent** instead of a one-shot pipeline.

---

# 12) Recommended Agent State Design

The assignment explicitly says the state should track:
- conversation history
- job requirements understanding
- candidate shortlist and reasoning

A good state object for LangGraph can include the following:

```python
from typing import List, Dict, Any, Optional

AgentState = {
    "messages": [],                     # full conversation history
    "current_query": "",               # latest user message
    "job_description": "",             # original or current JD text
    "requirements": {                  # parsed requirements
        "must_have_skills": [],
        "nice_to_have_skills": [],
        "experience_years": None,
        "education": None,
        "certifications": [],
        "keywords": []
    },
    "search_params": {                 # knobs used for retrieval
        "top_k": 10,
        "screening_round": 1,
        "strictness": "normal"
    },
    "candidate_results": [],           # raw retrieved results
    "ranked_candidates": [],           # normalized shortlist
    "comparison_result": None,         # result of compare_candidates
    "report": None,                    # generated recruiter-facing report
    "reasoning_cache": {},             # candidate_id -> reasoning
    "interview_questions": {},         # candidate_id -> questions
    "feedback_history": [],            # recruiter refinements / adjustments
    "rerank_explanation": None,        # why ranking changed after refinement
    "final_recommendation": None       # hire/no-hire summary if produced
}
```

---

# 13) Recommended Tools for Milestone 3

Milestone 3 should expose a tool layer that wraps reusable functionality.

## 13.1 Tools from Milestone 1
These can be wrapped for agent use:

- `read_files_in_folder(folder_path)`
- `search_files_for_keyword(folder_path, keyword)`
- `summarize_file(file_path)`

Potential new wrappers:
- `read_job_description_file(path)`
- `summarize_resume(candidate_id_or_path)`

---

## 13.2 Tools from Milestone 2
Likely wrappers around your existing modules:

- `index_directory(resume_folder)`
- `process_job_description(jd_text)`
- `search_candidates(requirements, top_k=10)`
- `rank_candidates(candidates, requirements)`
- `generate_match_reason(candidate_id, requirements)`

---

## 13.3 New Milestone 3 Tools
These are specifically required / strongly implied by the problem statement.

### `extract_requirements(jd: str)`
Purpose:
- split JD into structured must-have / nice-to-have requirements

Suggested output:
```python
{
    "must_have_skills": [],
    "nice_to_have_skills": [],
    "experience_years": None,
    "education": None,
    "certifications": [],
    "other_constraints": []
}
```

### `compare_candidates(candidate_ids: list)`
Purpose:
- compare candidates side by side

Suggested comparison dimensions:
- skills overlap
- experience relevance
- education fit
- matching projects
- certifications
- strengths
- gaps
- overall score / category

Suggested output:
```python
{
    "candidates": [...],
    "comparison_table": [...],
    "summary": "..."
}
```

### `generate_interview_questions(candidate_id: str)`
Purpose:
- generate targeted screening questions

Possible inputs:
- candidate profile
- matched skills
- missing requirements
- borderline areas

Suggested output:
```python
{
    "candidate_id": "...",
    "questions": [
        "...",
        "..."
    ],
    "focus_areas": ["React depth", "System design", "AWS deployment"]
}
```

---

# 14) Recommended Milestone 3 Intents / User Query Types

The agent should be able to detect and respond to several kinds of recruiter intents.

## 14.1 Search / shortlist intent
Examples:
- “Find candidates with React and 3+ years experience”
- “Show me top 10 backend engineers for this JD”

Action:
- parse / update requirements
- search and rank candidates
- generate shortlist

## 14.2 Explain ranking intent
Examples:
- “Why did John rank higher than Jane?”
- “Why is Candidate 2 only moderate?”

Action:
- retrieve stored reasoning
- compare scores and evidence
- explain differences

## 14.3 Compare candidates intent
Examples:
- “Compare the top 3 side by side”
- “Compare John, Priya, and Arun”

Action:
- call `compare_candidates`

## 14.4 Refinement intent
Examples:
- “Make AWS mandatory”
- “Actually reduce experience requirement to 2 years”
- “Ignore degree requirement”

Action:
- update requirements in state
- rerun ranking
- explain changes

## 14.5 Interview prep intent
Examples:
- “Generate interview questions for the top candidate”
- “What should I ask Candidate B about React?”

Action:
- call `generate_interview_questions`

## 14.6 File utility intent
Examples:
- “Summarize John’s resume”
- “Read the uploaded JD file”
- “Search all resumes for Kubernetes”

Action:
- route to Milestone 1 / file tools when appropriate

---

# 15) Recommended Multi-Round Screening Design

The problem statement explicitly asks for multi-round screening.  
A practical design is:

## Round 1 — Broad retrieval
Goal:
- narrow 100 resumes to top 10

Method:
- semantic + hybrid search
- moderate filtering
- fast shortlist generation

Output:
- top 10 candidates with quick reasons

## Round 2 — Deep analysis
Goal:
- evaluate the top 10 more carefully

Method:
- deeper reasoning over retrieved chunks
- compare must-have vs nice-to-have fit
- highlight strengths and gaps
- maybe inspect full resume sections more carefully

Output:
- richer profile summaries and refined ranking

## Round 3 — Final recommendation
Goal:
- produce final hiring guidance

Method:
- generate a recruiter-style report
- categorize candidates
- give hire / strong-consider / hold / reject style recommendations

Output:
- final recommendation package

---

# 16) Explainability Expectations in Milestone 3

The agent should not just rank candidates; it should justify the ranking.

## Each candidate report should ideally include:
- overall score
- category (Excellent / Strong / Moderate / Weak)
- matched must-have skills
- matched nice-to-have skills
- missing requirements / gaps
- experience relevance
- relevant excerpts from resume chunks
- short reasoning paragraph
- suggested interview focus areas

## For borderline candidates, also include:
- what is missing
- whether they are trainable / promising
- which questions should be asked before making a decision

---

# 17) Recommended Output Shapes in Milestone 3

## 17.1 Shortlist response
```python
{
    "job_summary": "...",
    "requirements": {...},
    "top_candidates": [
        {
            "candidate_id": "...",
            "name": "...",
            "score": 87,
            "category": "Strong",
            "matched_skills": [...],
            "gaps": [...],
            "reasoning": "..."
        }
    ]
}
```

## 17.2 Comparison response
```python
{
    "candidate_ids": ["c1", "c2", "c3"],
    "comparison": [
        {
            "candidate_id": "c1",
            "strengths": [...],
            "gaps": [...],
            "score": 90
        }
    ],
    "summary": "..."
}
```

## 17.3 Rerank explanation response
```python
{
    "old_requirements": {...},
    "new_requirements": {...},
    "ranking_changes": [...],
    "reason": "..."
}
```

## 17.4 Interview question response
```python
{
    "candidate_id": "...",
    "candidate_name": "...",
    "questions": [...],
    "focus_areas": [...]
}
```

---

# 18) Suggested Implementation Plan for Milestone 3

Below is a practical implementation order tailored to the current project.

## Phase 1 — Project foundation
### Goal
Set up the Milestone 3 project structure and define the agent state / tool boundaries.

### Tasks
- create `matching_agent.py`
- create `docs/context.md`, `docs/architecture.md`, `docs/implementation_plan.md`
- define state schema
- identify which Milestone 1 and Milestone 2 functions will be reused directly
- add a simple CLI shell for chat interaction

---

## Phase 2 — Tool wrappers over Milestones 1 and 2
### Goal
Create a clean interface layer for the agent.

### Tasks
- wrap Milestone 1 file tools for agent use
- wrap Milestone 2 matching functions for agent use
- standardize outputs returned to the agent
- ensure candidate IDs / metadata / reasoning formats are consistent

---

## Phase 3 — Requirement extraction and recruiter intent handling
### Goal
Make the system understand recruiter queries and JDs.

### Tasks
- implement `extract_requirements(jd)`
- define must-have vs nice-to-have structure
- support short query parsing in addition to full JD parsing
- detect recruiter intents: search / compare / explain / refine / interview / file utility

---

## Phase 4 — LangGraph workflow implementation
### Goal
Implement the required graph.

### Tasks
- create LangGraph nodes:
  - parse_jd_node
  - extract_requirements_node
  - search_resumes_node
  - rank_candidates_node
  - generate_report_node
  - feedback_router_node
- connect graph edges
- add state updates between nodes

---

## Phase 5 — Human feedback loop and iterative reranking
### Goal
Support multi-turn refinement.

### Tasks
- store feedback in state
- detect changes to requirements
- rerun search/ranking when needed
- generate rerank explanation
- preserve previous shortlist context for comparison

---

## Phase 6 — Candidate comparison and interview questions
### Goal
Add interactive decision-support features.

### Tasks
- implement `compare_candidates(candidate_ids)`
- implement `generate_interview_questions(candidate_id)`
- support comparison queries like “compare top 3”
- support “why X over Y?” queries

---

## Phase 7 — Multi-round screening
### Goal
Support progressive evaluation of candidates.

### Tasks
- define round 1 / round 2 / round 3 modes
- deep-analyze top N candidates in later rounds
- generate final recommendation report

---

## Phase 8 — UI and presentation
### Goal
Deliver a usable interactive interface.

### Options
- CLI first (simpler)
- optionally Streamlit or Gradio

### Tasks
- build chat loop
- show ranked results clearly
- show candidate comparison tables
- show interview questions and reports

---

## Phase 9 — Testing and demo preparation
### Goal
Prepare assignment submission quality output.

### Tasks
- create at least 5 conversation scenarios
- test refinement flows
- test compare flow
- test explainability flow
- test interview-question flow
- record demo video
- create graph/state diagram

---

# 19) Recommended Test Scenarios for Milestone 3

The assignment asks for 5+ conversation flows.  
Good scenarios would be:

## Scenario 1 — Basic candidate search
User:
- “Find me Python backend developers with AWS and 3+ years experience”

Expected:
- extracted requirements
- top ranked shortlist
- reasoning for each candidate

## Scenario 2 — Compare top candidates
User:
- “Compare the top 3 matches side by side”

Expected:
- structured comparison
- strengths and gaps for each candidate
- recommendation summary

## Scenario 3 — Ranking explanation
User:
- “Why did John rank higher than Jane?”

Expected:
- explanation referencing skill match, experience, and evidence chunks

## Scenario 4 — Requirement refinement
User:
- “Make React mandatory and reduce experience requirement to 2 years”

Expected:
- updated requirements
- reranked shortlist
- explanation of ranking changes

## Scenario 5 — Interview preparation
User:
- “Generate interview questions for the second-ranked candidate”

Expected:
- candidate-specific interview questions
- focus areas based on gaps and strengths

## Scenario 6 — Multi-round screening
User:
- “Now take the top 10 and do a deeper analysis, then tell me the top 3 to interview”

Expected:
- second-round analysis
- narrowed shortlist
- final recommendation

---

# 20) Key Design Principle for Milestone 3

Milestone 3 should be treated as an **agentic orchestration project**, not just another matching script.

That means the most important architectural idea is:

## Milestone 1 = file intelligence layer  
## Milestone 2 = candidate retrieval / ranking layer  
## Milestone 3 = recruiter-facing agent layer that coordinates both

So the new implementation should focus on:
- **stateful conversation**
- **routing and orchestration**
- **iterative refinement**
- **decision support**
- **explainability**
- **multi-step hiring workflows**

rather than rebuilding low-level resume parsing or vector search from scratch.

---

# 21) Final Summary

This project is a **LangGraph-based interactive hiring agent** built on top of two completed milestones.

## Milestone 1 already provides:
- natural-language file operations
- file reading / search / summarization utilities

## Milestone 2 already provides:
- resume ingestion
- metadata extraction
- chunking
- embeddings
- ChromaDB indexing
- semantic + hybrid search
- ranking and explainable candidate matching

## Milestone 3 must add:
- a **LangGraph agent**
- recruiter conversation state
- requirement extraction and refinement
- candidate comparison
- interview question generation
- multi-round screening
- explainable reports and feedback loop

The end result should feel like a **recruiting copilot** that a human recruiter can talk to naturally in order to find, compare, evaluate, and shortlist candidates.
