# Phase 1 - Foundation (Completed)

## Goal
Set up Milestone integration + data models.

## Deliverables
- config.py
- candidate_models.py
- AgentTools (base integration layer)

---

# Phase 2 - AgentTools Integration Layer (Completed)

## Goal
Unify Milestone 1 + Milestone 2 behind a single interface.

## Deliverables
- AgentTools class
- Job matching wrapper
- Resume indexing wrapper
- File system utilities

---

# Phase 3 - LangGraph Core Setup

## Goal
Introduce agent orchestration layer.

## Tasks

### 1. Create Agent State
- Store:
  - JobRequirements
  - Candidate list
  - MatchResult
  - intermediate reasoning

### 2. Define Graph Nodes
- load_job
- extract_requirements
- retrieve_candidates
- rank_candidates
- generate_explanations

### 3. Connect nodes using LangGraph edges

---

# Phase 4 - Candidate Intelligence Layer

## Goal
Enhance candidate understanding

## Tasks

- Improve reasoning output
- Add skill gap analysis
- Add candidate summaries
- Generate structured insights

---

# Phase 5 - Candidate Comparison Engine

## Goal
Compare multiple candidates intelligently

## Tasks

- pairwise comparison
- ranking justification
- winner selection logic
- structured comparison table

---

# Phase 6 - Interview Generation System

## Goal
Generate interview questions per candidate

## Tasks

- technical questions
- behavioral questions
- role-specific questions
- difficulty grading

---

# Phase 7 - Final Agent Orchestration

## Goal
End-to-end agentic workflow

## Flow

```text
  User Query
    ↓
  LangGraph Agent
    ↓
  AgentTools
    ↓
  Resume RAG + File System
    ↓
  Candidate Ranking
    ↓
  Comparison + Interview Generation
    ↓
  Final Report
```

---

# Output Format

  {
    "job_description": "...",
    "top_matches": [],
    "comparison": {},
    "interview_plan": {},
    "final_recommendation": {}
  }

# Timeline

  Phase	Focus
    3	LangGraph setup
    4	Candidate intelligence
    5	Comparison engine
    6	Interview system
    7	Final orchestration

# Summary

  This phase converts the system from:

  Resume Matching Engine
    → into
  Agentic Hiring System with reasoning + orchestration + decision support