# Resume RAG System and Job Matching Engine

## Overview

This project implements a Resume Retrieval-Augmented Generation (RAG) pipeline and a Job Matching Engine.

It supports:

- Resume ingestion from TXT, PDF, and DOCX files
- Metadata extraction (name, skills, experience, education)
- Section-based chunking of resumes
- Embedding generation using Sentence Transformers
- ChromaDB vector storage
- Semantic job-to-resume retrieval
- Hybrid candidate scoring using semantic similarity + metadata overlap
- Explainable candidate ranking

---

## Project Structure

```text
project/
│
├── resumes/                  # Input resumes
├── data/
│   └── chroma_db/            # ChromaDB persistence
│
├── resume_rag.py            # Resume ingestion + indexing pipeline
├── job_matcher.py           # Job matching engine
├── main.py                  # CLI entrypoint
│
├── README.md
├── problem_statement.md
├── context.md
└── architecture.md
```

---

## Features

### Resume Ingestion
Supported file types:
- `.txt`
- `.pdf`
- `.docx`

### Metadata Extraction
Each resume is processed to extract:
- Candidate name
- Skills
- Experience years
- Education

### Chunking
Resumes are split into logical sections such as:
- Summary
- Skills
- Experience
- Education
- Projects
- Certifications

### Embeddings
Embeddings are generated using:

- `sentence-transformers/all-MiniLM-L6-v2`

### Vector Storage
Resume chunks and metadata are stored in ChromaDB.

### Job Matching
Given a job description, the system:
1. extracts job requirements
2. performs semantic retrieval over indexed resumes
3. computes hybrid scores using:
   - semantic similarity
   - skill overlap
   - experience match
   - education match
4. generates explainable top candidate matches

---

## Installation

Create / activate your virtual environment, then install dependencies:

```bash
pip install -r requirements.txt
```

If you are not using a `requirements.txt` yet, install at least:

```bash
pip install chromadb sentence-transformers pypdf python-docx
```

---

## Folder Setup

Place resumes inside the `resumes/` folder.

Example:

```text
resumes/
├── john_doe.pdf
├── sarah_johnson.docx
└── alex_smith.txt
```

---

## Usage

## 1. Index resumes

```bash
python main.py index --resume-dir resumes --reset
```

### Notes
- `--reset` clears the Chroma collection before indexing.
- Omit `--reset` if you want to keep existing indexed data.

Example output:

```text
Indexing Complete
Indexed Resumes: 3
Indexed Chunks: 15
Stored Chunks in ChromaDB: 15
```

---

## 2. Create a job description file

Create a file like `job_description.txt`:

```text
We are hiring a Python Backend Developer with 3+ years of experience.

Required skills:
Python, Django, AWS, Docker, REST API, SQL, Git, PostgreSQL

Bachelor's degree in Computer Science required.
```

---

## 3. Match candidates

```bash
python main.py match --job-file job_description.txt --top-k 5
```

Example output:

```json
{
  "job_description": "...",
  "top_matches": [
    {
      "candidate_name": "John Doe",
      "resume_name": "john_doe.pdf",
      "final_score": 84.27,
      "match_category": "Strong",
      "experience_years": 5,
      "education": "Bachelor",
      "matched_sections": ["Skills", "Experience"],
      "matched_skills": ["python", "django", "aws"],
      "missing_skills": ["kubernetes"],
      "relevant_excerpts": [
        "Built REST APIs using Django and PostgreSQL...",
        "Deployed backend services on AWS using Docker..."
      ],
      "reasoning": "Matched skills: python, django, aws. Meets the experience requirement..."
    }
  ]
}
```

---

## Scoring Logic

Candidates are ranked using a weighted hybrid score.

Current weights:

- **55%** Semantic similarity
- **25%** Skill match
- **15%** Experience match
- **5%** Education match

Final scores are normalized to a **0–100** range.

Score categories:
- **90–100** → Excellent
- **75–89** → Strong
- **60–74** → Moderate
- **Below 60** → Weak

---

## Hybrid Search Design

This project uses a lightweight hybrid approach:

1. **Semantic retrieval** is performed using ChromaDB embeddings.
2. **Keyword / metadata matching** is applied during ranking using:
   - skill overlap
   - experience comparison
   - education comparison

This means “hybrid search” is implemented as:

- semantic retrieval + metadata-based keyword scoring

rather than a separate BM25 / inverted-index keyword engine.

---

## Assumptions / Limitations

- Resumes are assumed to be in English.
- Metadata extraction is rule-based and not guaranteed to be perfect.
- Skill extraction uses a predefined skills database.
- Must-have requirement filtering is threshold-based rather than full natural-language requirement parsing.
- The project is designed for local development and assignment-scale datasets.

---

## Main Components

### `resume_rag.py`
Responsible for:
- resume loading
- metadata extraction
- chunking
- embedding generation
- ChromaDB storage

### `job_matcher.py`
Responsible for:
- job description processing
- semantic retrieval
- hybrid scoring
- candidate ranking
- reasoning generation

### `main.py`
Command-line entrypoint for:
- indexing resumes
- running candidate matching

---

## Future Improvements

Possible improvements:
- better education extraction (highest degree detection)
- regex-based skill extraction with word boundaries
- explicit must-have requirement parsing (e.g. “React mandatory”)
- resume path in final output
- web UI / Streamlit frontend
- better keyword retrieval layer
