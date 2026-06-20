from abc import ABC, abstractmethod
from pathlib import Path
import re

from sentence_transformers import SentenceTransformer
from pypdf import PdfReader
from docx import Document
import chromadb
from chromadb.config import Settings


PROJECT_ROOT = Path(__file__).parent
RESUME_DIR = PROJECT_ROOT / "resumes"
CHROMA_DB_DIR = PROJECT_ROOT / "data" / "chroma_db"

class ResumeLoader:
    """
    Loads resume files from the local filesystem.

    Supported formats:
        - .txt
        - .pdf
        - .docx
    """

    SUPPORTED_EXTENSIONS = {".txt", ".pdf", ".docx"}

    def load_txt(self, file_path: str) -> dict:
        """
        Load text file.
        """

        with open(file_path, "r", encoding="utf-8") as file:
            content = file.read()

        return {
            "file_name": Path(file_path).name,
            "file_path": file_path,
            "content": content
        }

    def load_pdf(self, file_path: str) -> dict:
        """
        Extract text from PDF.
        """

        reader = PdfReader(file_path)

        pages = []

        for page in reader.pages:
            text = page.extract_text()

            if text:
                pages.append(text)

        content = "\n".join(pages)

        return {
            "file_name": Path(file_path).name,
            "file_path": file_path,
            "content": content
        }

    def load_docx(self, file_path: str) -> dict:
        """
        Extract text from DOCX.
        """

        document = Document(file_path)

        paragraphs = []

        for paragraph in document.paragraphs:
            text = paragraph.text.strip()

            if text:
                paragraphs.append(text)

        content = "\n".join(paragraphs)

        return {
            "file_name": Path(file_path).name,
            "file_path": file_path,
            "content": content
        }

    def load_resume(self, file_path: str) -> dict:
        """
        Auto-detect resume type and load.
        """
        file_path = Path(file_path)
        print(f"Loading resume: {file_path}")

        if not file_path.exists():
            raise FileNotFoundError(f"Resume file not found: {file_path}")
        
        extension = file_path.suffix.lower()
        if extension not in self.SUPPORTED_EXTENSIONS:
            raise ValueError(f"Unsupported file type: {extension}")

        if extension == ".txt":
            return self.load_txt(file_path)

        if extension == ".pdf":
            return self.load_pdf(file_path)

        if extension == ".docx":
            return self.load_docx(file_path)
    
    def load_resumes_from_directory(self, directory_path: str):
        resumes = []

        for file_path in Path(directory_path).iterdir():

            if file_path.suffix.lower() in self.SUPPORTED_EXTENSIONS:
                resumes.append(
                    self.load_resume(str(file_path))
                )

        return resumes


class MetadataExtractor:
    """
    Extract metadata from resume text.

    Extracts:
        - Name
        - Skills
        - Experience Years
        - Education
    """

    SKILLS_DATABASE = {
        "python", "java", "javascript", "typescript", "react", "angular",
        "vue", "django", "flask", "spring", "spring boot", "aws", "azure",
        "docker", "kubernetes", "sql", "mysql", "postgresql", "html", "css",
        "redis", "machine learning", "deep learning", "tensorflow", "gcp",
        "git", "github", "linux", "rest api", "microservices", "pytorch",
        "nodejs", "jenkins", "terraform", "cloudformation", "mongodb"
    }

    EDUCATION_KEYWORDS = [
        "bachelor", "master", "b.tech", "m.tech", "b.e",
        "m.e", "bsc", "msc", "phd", "doctorate"
    ]

    def extract_name(self, text: str) -> str:
        """
        Assumption:
        Resume starts with candidate name.
        """

        lines = [
            line.strip()
            for line in text.splitlines()
            if line.strip()
        ]

        if not lines:
            return ""

        first_line = lines[0]

        if len(first_line.split()) <= 5:
            return first_line

        return ""

    def extract_skills(self, text: str) -> list:
        """
        Match skills from predefined skill database.
        """

        text_lower = text.lower()

        matched_skills = []

        for skill in self.SKILLS_DATABASE:

            if skill in text_lower:
                matched_skills.append(skill)

        return sorted(matched_skills)

    def extract_experience_years(self, text: str) -> int:
        """
        Extract years of experience using regex.

        Examples:
            5 years experience
            7+ years
            3 yrs
        """

        patterns = [
            r'(\d+)\+?\s+years',
            r'(\d+)\+?\s+year',
            r'(\d+)\+?\s+yrs',
            r'(\d+)\+?\s+yr'
        ]

        max_years = 0

        text_lower = text.lower()

        for pattern in patterns:

            matches = re.findall(pattern, text_lower)

            for match in matches:
                max_years = max(
                    max_years,
                    int(match)
                )

        return max_years

    def extract_education(self, text: str) -> str:
        """
        Extract highest education found.
        """

        text_lower = text.lower()

        for keyword in self.EDUCATION_KEYWORDS:

            if keyword in text_lower:
                return keyword.title()

        return ""

    def extract(self, resume_text: str) -> dict:

        return {
            "name": self.extract_name(resume_text),
            "skills": self.extract_skills(resume_text),
            "experience_years": self.extract_experience_years(resume_text),
            "education": self.extract_education(resume_text)
        }


class ResumeChunker:
    """
    Splits resumes into logical sections.

    Preferred sections:

    - Summary
    - Skills
    - Experience
    - Education
    - Projects
    - Certifications
    """

    SECTION_PATTERNS = {
        "summary": [
            "summary",
            "professional summary",
            "profile"
        ],
        "skills": [
            "skills",
            "technical skills",
            "core skills"
        ],
        "experience": [
            "experience",
            "work experience",
            "employment history",
            "professional experience"
        ],
        "education": [
            "education",
            "academic background"
        ],
        "projects": [
            "projects",
            "project experience"
        ],
        "certifications": [
            "certifications",
            "certificates"
        ]
    }

    def normalize_heading(self, line: str):

        line = line.strip().lower()
        line = line.rstrip(":")

        for section, aliases in self.SECTION_PATTERNS.items():
            if line in aliases:
                return section

        return None

    def chunk(self, resume_text: str , resume_name: str):

        lines = resume_text.splitlines()
        chunks = []
        current_section = "general"
        current_content = []

        chunk_counter = 1
        for line in lines:
            stripped_line = line.strip()

            if not stripped_line:
                continue

            detected_section = self.normalize_heading(stripped_line)

            if detected_section:
                if current_content:
                    chunks.append({
                        "chunk_id": f"{current_section}_{chunk_counter}",
                        "resume_name": resume_name,
                        "section": current_section.title(),
                        "text": "\n".join(current_content)
                    })
                    chunk_counter += 1
                current_section = detected_section
                current_content = []
            else:
                current_content.append(stripped_line)

        if current_content:
            chunks.append({
                "chunk_id": f"{current_section}_{chunk_counter}",
                "resume_name": resume_name,
                "section": current_section.title(),
                "text": "\n".join(current_content)
            })

        return chunks


class EmbeddingService:
    """
    Generates embeddings using Sentence Transformers.
    """

    MODEL_NAME = "sentence-transformers/all-MiniLM-L6-v2"

    def __init__(self):
        self.model = SentenceTransformer(self.MODEL_NAME)

    def generate_embedding(self, text: str):
        """
        Generate embedding for a single text.
        """
        if not text:
            return []
        embedding = self.model.encode(text, convert_to_numpy=True)
        return embedding.tolist()

    def generate_embeddings(self, chunks):
        """
            Generate embeddings for all chunks.
            Input:
                [{
                    "chunk_id": "...",
                    "resume_name": "...",
                    "section": "...",
                    "text": "..."
                }]
            Output:
                [{
                    "chunk_id": "...",
                    "resume_name": resume_name,
                    "section": "...",
                    "text": "...",
                    "embedding": [...]
                }]
        """

        if not chunks:
            return []

        texts = [chunk["text"] for chunk in chunks]
        embeddings = self.model.encode(texts, convert_to_numpy=True)

        results = []
        for chunk, embedding in zip(chunks, embeddings):
            results.append({
                **chunk,
                "embedding": embedding.tolist()
            })

        return results


class VectorStore:

    COLLECTION_NAME = "resume_chunks"

    def __init__(self, persist_directory=None):
        if persist_directory is None:
            persist_directory = CHROMA_DB_DIR

        persist_directory = Path(persist_directory).resolve()
        persist_directory.mkdir(parents=True, exist_ok=True)

        self.client = chromadb.PersistentClient(
            path=str(persist_directory)
        )

        self.collection = self.client.get_or_create_collection(
            name=self.COLLECTION_NAME
        )

    def add_documents(self, embedded_chunks, metadata):
        """
        Store chunks in ChromaDB.
        """

        ids = []
        documents = []
        embeddings = []
        metadatas = []

        for chunk in embedded_chunks:
            ids.append(f"{chunk['resume_name']}_{chunk['chunk_id']}")
            documents.append(chunk["text"])
            embeddings.append(chunk["embedding"])
            metadatas.append({
                "resume_name": chunk["resume_name"],
                "section": chunk["section"],
                "candidate_name": metadata["name"],
                "experience_years": metadata["experience_years"],
                "education": metadata["education"],
                "skills": ",".join(metadata["skills"])
            })

        self.collection.add(
            ids=ids,
            documents=documents,
            embeddings=embeddings,
            metadatas=metadatas
        )

    def count(self):
        return self.collection.count()
    
    def search(self, query_embedding, top_k=10):

        results = self.collection.query(
            query_embeddings=[query_embedding],
            n_results=top_k
        )

        return results


class ResumeRAGPipeline:
    """Main ingestion pipeline."""

    def index_resume(self, path: str):
        raise NotImplementedError

    def index_directory(self, resume_folder: str):
        raise NotImplementedError


if __name__ == "__main__":

    loader = ResumeLoader()
    extractor = MetadataExtractor()
    chunker = ResumeChunker()
    embedding_service = EmbeddingService()
    vector_store = VectorStore()

    resume = loader.load_resumes_from_directory(str(RESUME_DIR))
    metadata = extractor.extract(resume[0]["content"])
    chunks = chunker.chunk(resume[0]["content"], resume_name=resume[0]["file_name"])
    embedded_chunks = (embedding_service.generate_embeddings(chunks))
    vector_store.add_documents(embedded_chunks, metadata)

    print()
    print("Chunk Count:")
    print(len(embedded_chunks))
    print()
    print(embedded_chunks[0]["embedding"][:10])
    print()
    print("Embedding Dimensions:", len(embedded_chunks[0]["embedding"]))
    print()
    print("Stored Chunks:", vector_store.count())
