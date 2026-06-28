# Agentic Profile Matching System - Architecture

## Overview

This system builds an **agentic layer on top of:**

- Milestone 1 → LLM File System (file tools + query processor)
- Milestone 2 → Resume RAG System (vector search + ranking engine)

Milestone 3 introduces an **Agentic orchestration layer** that unifies both systems and enables intelligent multi-step workflows using structured tools.

---

# High-Level Architecture

```text
                    ┌────────────────────────────┐
                    │     User / CLI / API       │
                    └────────────┬───────────────┘
                                 │
                                 ▼
                    ┌────────────────────────────┐
                    │     Agent (Future LangGraph)│
                    └────────────┬───────────────┘
                                 │
                                 ▼
                    ┌────────────────────────────┐
                    │       AgentTools Layer      │
                    │  (Unified Orchestration)    │
                    └────────────┬───────────────┘
                                 │
        ┌────────────────────────┼────────────────────────┐
        ▼                        ▼                        ▼
┌────────────────┐   ┌────────────────────┐   ┌────────────────────┐
│  Milestone 1   │   │    Milestone 2     │   │   Data Models      │
│ File Assistant  │   │ Resume RAG System  │   │ Candidate Models   │
└────────────────┘   └────────────────────┘   └────────────────────┘
        │                        │
        ▼                        ▼
┌────────────────┐   ┌────────────────────┐
│ File Tools     │   │ Resume Index +     │
│ Query Processor │   │ Job Matcher        │
└────────────────┘   └────────────────────┘
                                 │
                                 ▼
                    ┌────────────────────────────┐
                    │   ChromaDB Vector Store     │
                    └────────────────────────────┘
                                 │
                                 ▼
                    ┌────────────────────────────┐
                    │ Ranking + Hybrid Search     │
                    │ (Semantic + Skills + Exp)   │
                    └────────────────────────────┘
                                 │
                                 ▼
                    ┌────────────────────────────┐
                    │   MatchResult (Canonical)   │
                    └────────────────────────────┘
```

# Core Design Principles

1. Abstraction First
      LangGraph NEVER touches Milestone 1 or 2 directly
      Only interacts with AgentTools

2. Model Standardization
      All outputs are converted into:
            Candidate
            JobRequirements
            MatchResult

3. Separation of Concerns
      Layer	Responsibility
      Milestone 1	File operations + LLM query processing
      Milestone 2	Resume indexing + semantic search
      Milestone 3	Agent orchestration + reasoning

# Components

1. AgentTools (Core Layer)

      Acts as a unified facade over:

      ResumeRAGPipeline (indexing)
      JobMatcher (ranking + retrieval)
      FileTools (Milestone 1)
      QueryProcessor (LLM-based intent handling)
      Responsibilities:
      Candidate matching
      Job requirement extraction
      Resume indexing
      File operations
      Utility helpers for agent

2. Candidate Model System
      Candidate
            Represents a fully enriched resume profile:
                  Identity
                  Scores (semantic, skill, experience)
                  Matched skills
                  Missing skills
                  Reasoning
                  Metadata
      JobRequirements
            Structured job description:
                  Skills
                  Experience requirement
                  Education requirement
                  Raw text
                  
      MatchResult
            Final output structure:
                  Job requirements
                  Ranked candidates
                  Metadata
3. Milestone 1 Integration Layer
      Provides:
            File reading
            File search
            Query processing
            Resume summarization (LLM-based)

4. Milestone 2 Integration Layer
      Provides:
            Resume ingestion pipeline
            Metadata extraction
            Chunking
            Embedding generation
            ChromaDB vector search
            Hybrid ranking system

5. Matching Pipeline
      Job Description
      ↓
      JobRequirements Extraction
      ↓
      Vector Search (ChromaDB)
      ↓
      Hybrid Scoring
      ↓
      Requirement Filtering
      ↓
      Ranking Engine
      ↓
      MatchResult

6. Scoring System
      Final score combines:
            Semantic similarity (primary signal)
            Skill match
            Experience match
            Education match

      Output:
            0–100 score
            Category:
                  Excellent
                  Strong
                  Moderate
                  Weak

# Data Flow

      User Input
      ↓
      AgentTools
      ↓
      JobRequirements Extraction
      ↓
      Resume RAG Search (Milestone 2)
      ↓
      Hybrid Scoring
      ↓
      Ranking Engine
      ↓
      Candidate Model Conversion
      ↓
      MatchResult
      ↓
      Agent Output

# Key Design Decisions

1. No direct Milestone coupling
      All interactions go through AgentTools.

2. Dictionary → Model conversion
      Milestone 2 outputs are normalized into dataclasses.

3. Extensible reasoning layer
      Reasoning is separated from ranking for future LLM enhancement.

# Extensibility Plan

      Future phases will plug into this architecture:
            LangGraph orchestration
            Interview generation
            Candidate comparison engine
            Feedback-based reranking
            Multi-agent workflows

# Summary

      This architecture transforms:
            "Resume Matching System"
            → into
            "Agentic Hiring Intelligence System"

      It enables:
            modular design
            reusable pipelines
            clean separation between retrieval, reasoning, and orchestration