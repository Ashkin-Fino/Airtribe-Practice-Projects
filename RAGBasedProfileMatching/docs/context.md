# Additional Context for Resume RAG System

## Purpose

The goal of this project is to demonstrate a complete Retrieval-Augmented Generation (RAG) pipeline applied to resume matching. The implementation should prioritize clean architecture, maintainability, and explainability over production-scale optimization.

---

## Recommended Technology Stack

### Embeddings
Prefer one of the following:

- HuggingFace Sentence Transformers (recommended for local execution)
- OpenAI Embeddings
- Cohere Embeddings

Suggested local model:

- sentence-transformers/all-MiniLM-L6-v2

This model is lightweight, fast, and sufficient for semantic resume matching.

---

## Vector Database

For local development, ChromaDB is recommended because:

- No external infrastructure required
- Easy persistence to disk
- Simple API
- Suitable for assignment-scale datasets

Suggested persistence directory:

```text
data/chroma_db/
```

---

## Resume Directory Structure

Suggested project structure:

```text
project/
│
├── resumes/
│   ├── candidate1.pdf
│   ├── candidate2.docx
│   └── candidate3.txt
│
├── data/
│   └── chroma_db/
│
├── resume_rag.py
├── job_matcher.py
├── docs/
|   ├── problem_statement.md
|   ├── context.md
|   └── architecture.md
└── README.md
```

---

## Chunking Strategy

Resumes should not be split using fixed-size chunks alone.

Preferred approach:

1. Detect section headings.
2. Split by sections.
3. Store each section as an individual chunk.

Examples:

- Skills Section
- Experience Section
- Education Section
- Projects Section

Benefits:

- Better retrieval quality
- Better reasoning generation
- More accurate matching

---

## Metadata Extraction Guidance

The extracted metadata does not need to be perfect.

Acceptable approaches:

- Regular expressions
- Rule-based extraction
- Lightweight NLP

Required fields:

```json
{
  "name": "",
  "skills": [],
  "experience_years": 0,
  "education": ""
}
```

Missing fields may be stored as null or empty values.

---

## Hybrid Search Strategy

The retrieval score should combine:

1. Semantic Similarity
2. Skill Matching
3. Experience Matching

Example weighting:

```text
70% Semantic Similarity
20% Skill Overlap
10% Experience Match
```

Exact weights may be adjusted.

---

## Match Score Normalization

Convert the final ranking score into a 0-100 scale.

Example:

```text
Excellent Match : 90-100
Strong Match    : 75-89
Moderate Match  : 60-74
Weak Match      : Below 60
```

---

## Relevant Excerpts

The response should include resume snippets that influenced the ranking.

Example:

```text
Developed machine learning models using Python and Scikit-Learn.
```

Excerpts should come from retrieved chunks rather than the entire resume.

---

## Must-Have Requirement Handling

Examples:

- 5+ years Python experience
- AWS Certification
- React Experience

Candidates that fail mandatory requirements may:

- Be removed from results
- Receive a significant score penalty

The chosen behavior should be documented in code comments.

---

## Assumptions

- Resumes are written in English.
- Resumes are stored locally.
- The dataset size is small to medium (tens or hundreds of resumes).
- Real-time updates are not required.
- Authentication and user management are out of scope.

---

## Non-Goals

The following are not required:

- Resume generation
- LLM-based resume rewriting
- Fine-tuning embedding models
- Distributed vector databases
- Multi-language support
- Web UI or frontend development

---

## Expected Focus Areas

The implementation should demonstrate:

- Document ingestion
- Semantic search
- Vector databases
- Metadata filtering
- Hybrid retrieval
- Explainable ranking

Code clarity and maintainability are preferred over excessive optimization.
