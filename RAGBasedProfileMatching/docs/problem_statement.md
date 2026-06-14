# Resume RAG System and Job Matching Engine

## Overview

Build a Resume Retrieval-Augmented Generation (RAG) system capable of processing resumes from multiple document formats, generating embeddings, storing them in a vector database, and performing intelligent job-to-candidate matching using semantic search techniques.

The system should support resume ingestion, metadata extraction, vector search, hybrid retrieval, ranking, filtering, and explainable candidate recommendations.

---

# Assignment Requirements

## Part A: RAG System Setup

Create a file named:

`resume_rag.py`

### 1. Document Processing Pipeline

Implement a pipeline capable of processing resumes from the local file system.

#### Supported Resume Formats

- TXT files
- PDF files
- DOCX files

#### Document Parsing

Extract textual content from each resume while preserving important structural information.

#### Intelligent Chunking

Split resumes into meaningful chunks while preserving resume sections whenever possible.

Examples of sections:

- Personal Information
- Summary
- Skills
- Experience
- Education
- Certifications
- Projects

Chunking should avoid breaking important sections into unrelated fragments.

#### Embedding Generation

Generate vector embeddings using:

- OpenAI Embeddings
- Cohere Embeddings
- HuggingFace Embeddings

#### Vector Database Storage

Store generated embeddings in:

- ChromaDB
- Pinecone
- Weaviate

Each stored document chunk should maintain a link back to its original resume.

### 2. Metadata Extraction

Extract:

- Name
- Skills
- Experience Years
- Education

Store metadata alongside embeddings for filtering and ranking.

---

## Part B: Job Matching Engine

Create a file named:

`job_matcher.py`

### 1. Semantic Search

- Accept a job description as input.
- Generate an embedding for the job description.
- Retrieve Top-K (K=10) most relevant resumes.

### 2. Hybrid Search

Combine:

- Semantic Search
- Keyword Search

Critical skills may include:

- Python
- AWS
- Machine Learning
- React
- Kubernetes
- SQL

### 3. Ranking and Scoring

Assign a match score between 0 and 100.

Possible scoring factors:

- Semantic similarity
- Skill overlap
- Years of experience
- Education relevance
- Keyword matches
- Technology stack coverage

### 4. Match Reasoning

Provide:

- Why the candidate was selected
- Matching skills
- Matching experience
- Relevant resume sections

### 5. Must-Have Requirement Filtering

Support mandatory requirements such as:

- 5+ years Python experience
- AWS certification required
- React experience mandatory
- Master's degree preferred

Candidates failing mandatory requirements should be excluded or heavily penalized.

---

# Expected Workflow

## Resume Ingestion Phase

1. Read resumes from filesystem.
2. Extract text content.
3. Extract metadata.
4. Chunk resume content.
5. Generate embeddings.
6. Store embeddings and metadata in vector database.

## Job Matching Phase

1. Accept job description.
2. Generate job description embedding.
3. Retrieve top-K candidate chunks.
4. Apply metadata filters.
5. Perform hybrid search.
6. Compute match scores.
7. Generate reasoning.
8. Return ranked candidates.

---

# Output Format

```json
{
  "job_description": "...",
  "top_matches": [
    {
      "candidate_name": "John Doe",
      "resume_path": "resumes/john_doe.pdf",
      "match_score": 92,
      "matched_skills": ["Python", "Machine Learning"],
      "relevant_excerpts": [
        "Developed machine learning models using Python..."
      ],
      "reasoning": "Strong match for ML experience and Python development."
    }
  ]
}
```

---

# Deliverables

## Required Files

### resume_rag.py

Responsible for:

- Resume loading
- Text extraction
- Chunking
- Metadata extraction
- Embedding generation
- Vector database storage

### job_matcher.py

Responsible for:

- Job description processing
- Embedding generation
- Resume retrieval
- Hybrid search
- Candidate ranking
- Match explanation generation
