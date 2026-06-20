import re

from resume_rag import EmbeddingService, VectorStore


class JobDescriptionProcessor:
    """
    Extract structured requirements from a job description.
    """

    SKILLS_DATABASE = {
        "python",
        "java",
        "javascript",
        "typescript",
        "react",
        "angular",
        "vue",
        "django",
        "flask",
        "spring",
        "spring boot",
        "aws",
        "azure",
        "gcp",
        "docker",
        "kubernetes",
        "sql",
        "mysql",
        "postgresql",
        "mongodb",
        "redis",
        "machine learning",
        "deep learning",
        "tensorflow",
        "pytorch",
        "git",
        "github",
        "linux",
        "rest api",
        "microservices",
        "html",
        "css",
        "nodejs",
        "jenkins",
        "terraform",
        "cloudformation"
    }

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

        for keyword in [
            "phd",
            "doctorate",
            "master",
            "m.tech",
            "m.e",
            "msc",
            "bachelor",
            "b.tech",
            "b.e",
            "bsc"
        ]:
            if keyword in text_lower:
                return keyword.title()

        return ""

    def process(self, job_description: str) -> dict:
        """
        Convert JD into structured metadata.
        """

        return {
            "raw_text": job_description,
            "skills": self.extract_skills(job_description),
            "experience_years": self.extract_experience_years(job_description),
            "education": self.extract_education(job_description)
        }


class SemanticSearcher:
    """
    Semantic retrieval over resume embeddings.
    """

    def __init__(self):
        self.embedding_service = EmbeddingService()
        self.vector_store = VectorStore()

    def search(self, job_description: str, top_k: int = 10):
        """
        Search ChromaDB using JD embedding.
        """

        query_embedding = self.embedding_service.generate_embedding(
            job_description
        )

        return self.vector_store.search(
            query_embedding=query_embedding,
            top_k=top_k
        )


class RankingEngine:
    """
    Convert chunk-level retrieval results into candidate-level ranking.
    Then apply metadata-aware scoring.
    """

    EDUCATION_RANK = {
        "": 0,
        "Bsc": 1,
        "B.E": 1,
        "B.Tech": 1,
        "Bachelor": 1,
        "Msc": 2,
        "M.E": 2,
        "M.Tech": 2,
        "Master": 2,
        "Phd": 3,
        "Doctorate": 3
    }

    def rank_candidates(self, chroma_results: dict) -> list:
        """
        Convert raw Chroma chunk results into candidate-level results.
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

    def _normalize_candidate_skills(self, candidate_skills) -> set:
        """
        Convert candidate skills into a normalized set.
        Chroma metadata stores skills as comma-separated string.
        """

        if isinstance(candidate_skills, list):
            return {skill.strip().lower() for skill in candidate_skills if skill.strip()}

        if isinstance(candidate_skills, str):
            if not candidate_skills.strip():
                return set()
            return {
                skill.strip().lower()
                for skill in candidate_skills.split(",")
                if skill.strip()
            }

        return set()

    def calculate_skill_match_score(
        self,
        jd_skills: list,
        candidate_skills
    ) -> tuple:
        """
        Score skill overlap between JD and candidate.

        Returns:
            (score, matched_skills)
        """

        jd_skill_set = {
            skill.strip().lower()
            for skill in jd_skills
            if skill.strip()
        }

        candidate_skill_set = self._normalize_candidate_skills(
            candidate_skills
        )

        if not jd_skill_set:
            return 1.0, []

        matched = jd_skill_set.intersection(candidate_skill_set)
        score = len(matched) / len(jd_skill_set)

        return score, sorted(matched)

    def calculate_experience_score(
        self,
        required_experience: int,
        candidate_experience: int
    ) -> float:
        """
        Score experience match.

        Rules:
        - If JD doesn't specify experience -> full score
        - If candidate meets/exceeds requirement -> 1.0
        - Otherwise partial score proportional to gap
        """

        if required_experience <= 0:
            return 1.0

        if candidate_experience >= required_experience:
            return 1.0

        if candidate_experience <= 0:
            return 0.0

        return candidate_experience / required_experience

    def calculate_education_score(
        self,
        required_education: str,
        candidate_education: str
    ) -> float:
        """
        Score education match.

        If candidate education level >= JD education level -> 1.0
        Else 0.0
        """

        if not required_education:
            return 1.0

        required_rank = self.EDUCATION_RANK.get(
            required_education, 0
        )
        candidate_rank = self.EDUCATION_RANK.get(
            candidate_education, 0
        )

        return 1.0 if candidate_rank >= required_rank else 0.0

    def distance_to_similarity_score(
        self,
        distance: float
    ) -> float:
        """
        Convert Chroma distance into similarity-like score.

        Smaller distance = better.
        We clamp to keep score within [0, 1].
        """

        score = 1 - distance
        return max(0.0, min(1.0, score))

    def apply_hybrid_scoring(
        self,
        candidates: list,
        jd_metadata: dict
    ) -> list:
        """
        Phase 10 + 11:
        Combine semantic similarity with metadata-based scores.

        Final weighted score:
            40% semantic similarity
            35% skill match
            15% experience score
            10% education score
        """

        for candidate in candidates:

            semantic_score = self.distance_to_similarity_score(
                candidate["best_distance"]
            )

            skill_match_score, matched_skills = (
                self.calculate_skill_match_score(
                    jd_skills=jd_metadata["skills"],
                    candidate_skills=candidate["skills"]
                )
            )

            experience_score = self.calculate_experience_score(
                required_experience=jd_metadata["experience_years"],
                candidate_experience=candidate["experience_years"]
            )

            education_score = self.calculate_education_score(
                required_education=jd_metadata["education"],
                candidate_education=candidate["education"]
            )

            final_score = (
                0.40 * semantic_score +
                0.35 * skill_match_score +
                0.15 * experience_score +
                0.10 * education_score
            )

            candidate["semantic_score"] = round(
                semantic_score, 4
            )
            candidate["skill_match_score"] = round(
                skill_match_score, 4
            )
            candidate["experience_score"] = round(
                experience_score, 4
            )
            candidate["education_score"] = round(
                education_score, 4
            )
            candidate["final_score"] = round(
                final_score, 4
            )
            candidate["matched_skills"] = matched_skills

        candidates.sort(
            key=lambda x: x["final_score"],
            reverse=True
        )

        return candidates


class JobMatcher:
    """
    Main interface for job-to-candidate matching.

    Phase 8:
        JD processing

    Phase 9:
        Semantic retrieval

    Phase 10:
        Metadata-aware scoring

    Phase 11:
        Weighted final ranking
    """

    def __init__(self):
        self.jd_processor = JobDescriptionProcessor()
        self.semantic_searcher = SemanticSearcher()
        self.ranking_engine = RankingEngine()

    def search_candidates(
        self,
        job_description: str,
        top_k: int = 5
    ) -> list:
        """
        Search indexed resumes and return top ranked candidates.
        """

        jd_metadata = self.jd_processor.process(
            job_description
        )

        # Retrieve more chunk results than final candidate count
        # because multiple chunks can belong to the same resume.
        chroma_results = self.semantic_searcher.search(
            jd_metadata["raw_text"],
            top_k=top_k * 5
        )

        candidates = self.ranking_engine.rank_candidates(
            chroma_results
        )

        candidates = self.ranking_engine.apply_hybrid_scoring(
            candidates=candidates,
            jd_metadata=jd_metadata
        )

        return candidates[:top_k]


if __name__ == "__main__":

    job_description = """
    We are hiring a Python Backend Developer with 3+ years of experience.

    Required skills:
    Python, Django, AWS, Docker, REST API, SQL, Git, PostgreSQL

    Bachelor's degree in Computer Science required.
    """

    matcher = JobMatcher()

    results = matcher.search_candidates(
        job_description=job_description,
        top_k=5
    )

    print("\nTop Matching Candidates\n")

    for idx, candidate in enumerate(results, start=1):
        print("=" * 100)
        print(f"Rank #{idx}")
        print("Candidate Name:", candidate["candidate_name"])
        print("Resume Name:", candidate["resume_name"])
        print("Experience:", candidate["experience_years"])
        print("Education:", candidate["education"])
        print("Skills:", candidate["skills"])
        print("Matched Skills:", candidate["matched_skills"])
        print("Matched Sections:", candidate["matched_sections"])
        print("Best Distance:", candidate["best_distance"])
        print("Semantic Score:", candidate["semantic_score"])
        print("Skill Match Score:", candidate["skill_match_score"])
        print("Experience Score:", candidate["experience_score"])
        print("Education Score:", candidate["education_score"])
        print("Final Score:", candidate["final_score"])
