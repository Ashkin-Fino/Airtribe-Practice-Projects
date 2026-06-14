# Resume RAG System - Detailed Architecture Plan

## System Overview

The system consists of two major components:

1. Resume Ingestion & Indexing Pipeline (`resume_rag.py`)
2. Job Matching & Retrieval Engine (`job_matcher.py`)

The architecture follows a modular design to ensure maintainability, scalability, and explainability.

---

# High-Level Architecture

```text
                    ┌─────────────────────┐
                    │    Resume Files     │
                    │  PDF / DOCX / TXT   │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │   Resume Loader     │
                    │ (load_txt, load_pdf,│
                    │  load_docx)         │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │ Metadata Extraction │
                    │ (Regex, NLP, Rules) │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │ Section Chunking    │
                    │ (Skills, Experience,│
                    │ Education, etc.)    │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │ Embedding Generator │
                    │(SentenceTransformer)│
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │      ChromaDB       │
                    │   (Vector Storage)  │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │  Job Matcher Engine │
                    │ (Hybrid Search)     │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │ Ranking & Filtering │
                    │ (Explainable Scores)│
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │Final Recommendations│
                    │  (Top Candidates)   │
                    └─────────────────────┘
```

---

# Detailed Component Breakdown

## Resume Ingestion & Indexing Pipeline

### ResumeLoader
      Purpose: Load resumes from the filesystem.
      Supported Formats: TXT, PDF, DOCX.
      Methods:
            load_txt()
            load_pdf()
            load_docx()
            load_resume()

### MetadataExtractor
      Purpose: Extract metadata such as name, skills, experience, and education.
      Techniques:
            Regular expressions
            Rule-based parsing
            Lightweight NLP

### ResumeChunker
      Purpose: Split resumes into logical sections for better retrieval.
      Sections:
            Skills
            Experience
            Education
            Projects
            Certifications

### EmbeddingService
      Purpose: Generate semantic embeddings for resume chunks.
      Model: sentence-transformers/all-MiniLM-L6-v2
### VectorStore
      Purpose: Store embeddings and metadata in a vector database.
      Database: ChromaDB

## Job Matching & Retrieval Engine

### JobDescriptionProcessor
      Purpose: Parse job descriptions to extract requirements.
      Extracted Fields:
            Skills
            Experience
            Education

### SemanticSearcher
      Purpose: Perform semantic similarity search using embeddings.

### HybridSearcher
      Purpose: Combine semantic similarity with keyword matching.

### Scoring Formula:
      70% Semantic Similarity
      20% Skill Match
      10% Experience Match

### RequirementFilter
      Purpose: Filter out candidates who do not meet mandatory requirements.

### RankingEngine
      Purpose: Normalize scores and rank candidates.
      Score Categories:
            90-100: Excellent
            75-89: Strong
            60-74: Moderate
            Below 60: Weak

### MatchReasoner
      Purpose: Provide explainable reasoning for candidate selection.
      Output:
            Matched skills
            Relevant excerpts
            Reasoning text
            Data Flow
            Persistence
### Resumes: 
      Stored in the resumes/ directory.

### Vector Database:
      Stored in data/chroma_db/.

### Assumptions
      Resumes are in English.
      The dataset size is small to medium.
      Real-time updates are not required.

---
