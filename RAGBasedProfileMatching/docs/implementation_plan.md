# Phase 1 - Project Foundation

## Goal

Create the project structure and basic interfaces.

## Deliverables

```text
project/
│
├── resumes/
├── data/
│   └── chroma_db/
│
├── resume_rag.py
├── job_matcher.py
├── docs/
│   ├── problem_statement.md
│   ├── context.md
│   └── architecture.md
|
└── README.md
```

## Responsibilities

### resume_rag.py

Responsible for:

- Resume ingestion
- Metadata extraction
- Chunking
- Embedding generation
- Vector database storage

### job_matcher.py

Responsible for:

- Job description processing
- Search
- Ranking
- Reasoning generation

---

# Phase 2 - Resume Ingestion Layer

## Goal

Read resumes from the filesystem.

## Components

### ResumeLoader

Supported formats:

- TXT
- PDF
- DOCX

### Methods

```python
load_txt()
load_pdf()
load_docx()
load_resume()
```

## Output

```python
{
    "file_name": "...",
    "file_path": "...",
    "content": "..."
}
```

---

# Phase 3 - Metadata Extraction Layer

## Goal

Extract searchable candidate information.

## Component

### MetadataExtractor

## Fields

```python
{
    "name": "",
    "skills": [],
    "experience_years": 0,
    "education": ""
}
```

## Suggested Techniques

- Regex
- Keyword matching
- Rule-based parsing

---

# Phase 4 - Intelligent Chunking Layer

## Goal

Convert resumes into retrieval-friendly chunks.

## Component

### ResumeChunker

## Strategy

Split by sections:

- Summary
- Skills
- Experience
- Education
- Certifications
- Projects

## Output

```python
{
    "chunk_id": "",
    "section": "Experience",
    "text": "..."
}
```

---

# Phase 5 - Embedding Pipeline

## Goal

Generate semantic representations.

## Component

### EmbeddingService

## Recommended Model

```text
      sentence-transformers/all-MiniLM-L6-v2
```

## Methods

```python
generate_embedding(text)
generate_embeddings(chunks)
```

## Output

Vector representation for each chunk.

---

# Phase 6 - Vector Database Layer

## Goal

Persist embeddings and metadata.

## Component

### VectorStore

## Recommended Database

ChromaDB

## Stored Data

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

---

# Phase 7 - Resume Indexing Pipeline

## Goal

Create a complete ingestion workflow.

## Flow

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

## Public Method

```python
index_resume(path)
index_directory(resume_folder)
```

---

# Phase 8 - Job Description Processing

## Goal

Prepare job descriptions for retrieval.

## Component

### JobDescriptionProcessor

## Responsibilities

- Parse job description
- Extract required skills
- Extract experience requirements
- Extract education requirements

## Example

```python
{
    "skills": ["Python", "AWS"],
    "experience": 5,
    "education": "Bachelor"
}
```

---

# Phase 9 - Semantic Search Engine

## Goal

Find similar resumes.

## Component

### SemanticSearcher

## Flow

```text
Job Description
       ↓
Generate Embedding
       ↓
Vector Similarity Search
       ↓
Top K Results
```

## Retrieval

```python
top_k = 10
```

---

# Phase 10 - Hybrid Search Layer

## Goal

Combine semantic and keyword matching.

## Component

### HybridSearcher

## Inputs

- Semantic similarity score
- Skill overlap score
- Experience match score

## Example Formula

```text
Final Score =
70% Semantic Similarity
20% Skill Match
10% Experience Match
```

---

# Phase 11 - Requirement Filtering

## Goal

Remove unsuitable candidates.

## Component

### RequirementFilter

## Examples

- Python >= 5 years
- AWS Certification
- React Experience

## Behaviour

Candidate:

- Excluded
or
- Heavy score penalty

---

# Phase 12 - Ranking Engine

## Goal

Convert retrieval results into business-friendly scores.

## Component

### RankingEngine

## Output Scale

```text
0 - 100
```

## Categories

```text
90-100  Excellent
75-89   Strong
60-74   Moderate
0-59    Weak
```

---

# Phase 13 - Reasoning Engine

## Goal

Explain why candidates were selected.

## Component

### MatchReasoner

## Output

```python
{
    "matched_skills": [],
    "relevant_excerpts": [],
    "reasoning": "..."
}
```

Reasoning should be generated from retrieved chunks.

---

# Phase 14 - Final Response Generator

## Goal

Produce assignment output format.

## Output

```json
{
  "job_description": "...",
  "top_matches": []
}
```

---

# Suggested Class Diagram

```text
ResumeLoader
      │
      ▼
MetadataExtractor
      │
      ▼
ResumeChunker
      │
      ▼
EmbeddingService
      │
      ▼
VectorStore
      │
      ▼
SemanticSearcher
      │
      ▼
HybridSearcher
      │
      ▼
RequirementFilter
      │
      ▼
RankingEngine
      │
      ▼
MatchReasoner
```

---

# Recommended Implementation Order

Phase 1  → Project Setup

Phase 2  → Resume Loaders

Phase 3  → Metadata Extraction

Phase 4  → Chunking

Phase 5  → Embeddings

Phase 6  → ChromaDB

Phase 7  → Resume Indexing

Phase 8  → Job Description Processing

Phase 9  → Semantic Search

Phase 10 → Hybrid Search

Phase 11 → Requirement Filtering

Phase 12 → Ranking

Phase 13 → Reasoning

Phase 14 → Final Output

This order minimizes rework and allows each phase to be independently tested before proceeding to the next phase.
