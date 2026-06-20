import re
from collections import defaultdict

from resume_rag import EmbeddingService, VectorStore


class JobDescriptionProcessor:
    """
    Extract structured requirements from a job description.
    """

    SKILLS_DATABASE = {
        "python", "java", "javascript", "typescript", "react",
        "angular", "vue", "django", "flask", "spring", "aws",
        "spring boot", "azure", "gcp", "docker", "kubernetes",
        "sql", "mysql", "postgresql", "mongodb", "redis", "css",
        "machine learning", "deep learning", "tensorflow", "git",
        "pytorch", "github", "linux", "rest api", "microservices",
        "html", "nodejs", "jenkins", "terraform", "cloudformation"
    }

    EDUCATION_KEYWORDS = [
        "bachelor", "master", "b.tech", "m.tech",
        "b.e", "m.e", "bsc", "msc", "phd", "doctorate"
    ]

    def extract_skills(self, text: str) -> list:
        """
        Extract skills mentioned in the JD.
        """

        text_lower = text.lower()
        matched_skills = []

        for skill in self.SKILLS_DATABASE:
            if skill in text_lower:
                matched_skills.append(skill)

        return sorted(matched_skills)

    def extract_experience_years(self, text: str) -> int:
        """
        Extract required years of experience from the JD.
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
                max_years = max(max_years, int(match))

        return max_years

    def extract_education(self, text: str) -> str:
        """
        Extract education requirement from JD.
        """

        text_lower = text.lower()
        for keyword in self.EDUCATION_KEYWORDS:
            if keyword in text_lower:
                return keyword.title()

        return ""

    def process(self, job_description: str) -> dict:
        """
        Convert job description into structured metadata.
        """
        return {
            "raw_text": job_description,
            "skills": self.extract_skills(job_description),
            "experience_years": self.extract_experience_years(job_description),
            "education": self.extract_education(job_description)
        }


class SemanticSearcher:
    """
    Performs semantic search over indexed resume chunks.
    """

    def __init__(self):
        self.embedding_service = EmbeddingService()
        self.vector_store = VectorStore()

    def search(self, job_description: str, top_k: int = 10):
        """
        Search ChromaDB using the job description embedding.
        """
        query_embedding = self.embedding_service.generate_embedding(job_description)
        results = self.vector_store.search(query_embedding,top_k=top_k)
        return results


class HybridSearcher:

    def score(self, candidate, job_requirements):
        raise NotImplementedError


class RequirementFilter:

    def filter(self, candidates, requirements):
        raise NotImplementedError


class RankingEngine:
    """
    Aggregate chunk-level Chroma results into candidate-level ranking.
    """

    def rank_candidates(self, chroma_results: dict) -> list:
        """
        Convert Chroma chunk results into ranked candidate results.
        """

        ids = chroma_results.get("ids", [[]])[0]
        documents = chroma_results.get("documents", [[]])[0]
        metadatas = chroma_results.get("metadatas", [[]])[0]
        distances = chroma_results.get("distances", [[]])[0]

        candidate_map = {}

        for chunk_id, document, metadata, distance in zip(
            ids, documents, metadatas, distances
        ):
            resume_name = metadata["resume_name"]
            if resume_name not in candidate_map:
                candidate_map[resume_name] = {
                    "resume_name": resume_name,
                    "candidate_name": metadata.get("candidate_name", ""),
                    "experience_years": metadata.get("experience_years", 0),
                    "education": metadata.get("education", ""),
                    "skills": metadata.get("skills", ""),
                    "best_distance": distance,
                    "matched_sections": [metadata.get("section", "")],
                    "matched_chunks": [document]
                }
            else:
                # Keep the best (smallest) distance
                candidate_map[resume_name]["best_distance"] = min(
                    candidate_map[resume_name]["best_distance"],
                    distance
                )
                section = metadata.get("section", "")
                if section not in candidate_map[resume_name]["matched_sections"]:
                    candidate_map[resume_name]["matched_sections"].append(section)
                candidate_map[resume_name]["matched_chunks"].append(document)

        ranked_candidates = list(candidate_map.values())
        ranked_candidates.sort(key=lambda x: x["best_distance"])

        return ranked_candidates


class MatchReasoner:

    def explain(self, candidate, job_description):
        raise NotImplementedError


class JobMatcher:
    """
    End-to-end candidate search interface.

    Phase 8:
        - process job description

    Phase 9:
        - semantic search over resumes
        - aggregate chunk results into candidate ranking
    """

    def __init__(self):
        self.jd_processor = JobDescriptionProcessor()
        self.semantic_searcher = SemanticSearcher()
        self.ranking_engine = RankingEngine()

    def search_candidates(self, job_description: str, top_k: int = 5) -> list:
        """
        Search the indexed resume database for relevant candidates.
        """

        jd_metadata = self.jd_processor.process(job_description)

        # Use the raw JD text for semantic retrieval
        chroma_results = self.semantic_searcher.search(jd_metadata["raw_text"], top_k * 3)
        ranked_candidates = self.ranking_engine.rank_candidates(chroma_results)
        return ranked_candidates[:top_k]
    

if __name__ == "__main__":

    job_description = """
        We are hiring a Python Backend Developer with 3+ years of experience.
        Required skills:
        Python, Django, AWS, Docker, REST API, SQL

        Bachelor's degree in Computer Science preferred.
    """

    matcher = JobMatcher()

    results = matcher.search_candidates(job_description, top_k=3)

    for idx, candidate in enumerate(results, start=1):
        print("=" * 80)
        print(f"Rank #{idx}")
        print("Candidate Name:", candidate["candidate_name"])
        print("Resume Name:", candidate["resume_name"])
        print("Experience:", candidate["experience_years"])
        print("Education:", candidate["education"])
        print("Skills:", candidate["skills"])
        print("Best Distance:", candidate["best_distance"])
        print("Matched Sections:", candidate["matched_sections"])
