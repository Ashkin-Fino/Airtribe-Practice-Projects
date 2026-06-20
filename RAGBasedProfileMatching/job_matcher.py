import re
from typing import List, Dict, Tuple

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

    def extract_skills(self, text: str) -> List[str]:
        text_lower = text.lower()
        matched_skills = []

        for skill in self.SKILLS_DATABASE:
            if skill in text_lower:
                matched_skills.append(skill)

        return sorted(matched_skills)

    def extract_experience_years(self, text: str) -> int:
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
        text_lower = text.lower()

        for keyword in [
            "phd", "doctorate", "master", "m.tech", "m.e", 
            "msc", "bachelor", "b.tech", "b.e", "bsc"
        ]:
            if keyword in text_lower:
                return keyword.title()

        return ""

    def process(self, job_description: str) -> Dict:
        return {
            "raw_text": job_description,
            "skills": self.extract_skills(job_description),
            "experience_years": self.extract_experience_years(job_description),
            "education": self.extract_education(job_description)
        }


class SemanticSearcher:
    """
    Performs semantic retrieval from ChromaDB.
    """

    def __init__(self):
        self.embedding_service = EmbeddingService()
        self.vector_store = VectorStore()

    def search(self, query_text: str, top_k: int = 10) -> Dict:
        query_embedding = self.embedding_service.generate_embedding(query_text)
        return self.vector_store.search(query_embedding, top_k)


class HybridSearcher:
    """
    Converts raw Chroma chunk-level results into candidate-level hybrid candidates.
    Also computes semantic + skill + experience + education signals.
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

    def _normalize_candidate_skills(self, candidate_skills) -> set:
        if isinstance(candidate_skills, list):
            return {
                skill.strip().lower()
                for skill in candidate_skills
                if skill.strip()
            }

        if isinstance(candidate_skills, str):
            if not candidate_skills.strip():
                return set()
            return {
                skill.strip().lower()
                for skill in candidate_skills.split(",")
                if skill.strip()
            }

        return set()

    def _distance_to_similarity_score(self, distance: float) -> float:
        """
        Convert Chroma distance into similarity-like score in [0, 1].
        """
        score = 1 - distance
        return max(0.0, min(1.0, score))

    def _calculate_skill_match(
        self, jd_skills: List[str], candidate_skills
    ) -> Tuple[float, List[str], List[str]]:

        jd_skill_set = {skill.strip().lower() for skill in jd_skills if skill.strip()}

        candidate_skill_set = self._normalize_candidate_skills(candidate_skills)

        if not jd_skill_set:
            return 1.0, [], []

        matched = jd_skill_set.intersection(candidate_skill_set)
        missing = jd_skill_set.difference(candidate_skill_set)

        score = len(matched) / len(jd_skill_set)

        return score, sorted(matched), sorted(missing)

    def _calculate_experience_match(
        self, required_experience: int, candidate_experience: int
    ) -> float:

        if required_experience <= 0:
            return 1.0

        if candidate_experience >= required_experience:
            return 1.0

        if candidate_experience <= 0:
            return 0.0

        return candidate_experience / required_experience

    def _calculate_education_match(
        self, required_education: str, candidate_education: str
    ) -> float:

        if not required_education:
            return 1.0

        required_rank = self.EDUCATION_RANK.get(
            required_education, 0
        )
        candidate_rank = self.EDUCATION_RANK.get(
            candidate_education, 0
        )

        return 1.0 if candidate_rank >= required_rank else 0.0

    def aggregate_candidates(self, chroma_results: Dict) -> List[Dict]:
        """
        Convert chunk-level Chroma results into candidate-level objects.
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
                if (
                    section
                    and section not in candidate_map[resume_name]["matched_sections"]
                ):
                    candidate_map[resume_name]["matched_sections"].append(section)

                candidate_map[resume_name]["matched_chunks"].append(document)

        return list(candidate_map.values())

    def enrich_candidates(self, candidates: List[Dict], jd_metadata: Dict) -> List[Dict]:
        """
        Compute hybrid search signals for each candidate.
        """

        enriched = []

        for candidate in candidates:
            semantic_score = self._distance_to_similarity_score(
                candidate["best_distance"]
            )

            skill_match_score, matched_skills, missing_skills = (
                self._calculate_skill_match(jd_metadata["skills"], candidate["skills"])
            )

            experience_match_score = self._calculate_experience_match(
                required_experience=jd_metadata["experience_years"],
                candidate_experience=candidate["experience_years"]
            )

            education_match_score = self._calculate_education_match(
                required_education=jd_metadata["education"],
                candidate_education=candidate["education"]
            )

            enriched_candidate = {
                **candidate,
                "semantic_score": semantic_score,
                "skill_match_score": skill_match_score,
                "experience_match_score": experience_match_score,
                "education_match_score": education_match_score,
                "matched_skills": matched_skills,
                "missing_skills": missing_skills
            }

            enriched.append(enriched_candidate)

        return enriched


class RequirementFilter:
    """
    Remove or penalize unsuitable candidates based on job requirements.
    """

    def __init__(
        self,
        min_skill_match_threshold: float = 0.20,
        min_experience_match_threshold: float = 0.50,
        strict_filtering: bool = False
    ):
        """
        strict_filtering=False:
            keep candidates but apply penalties later if weak.

        strict_filtering=True:
            drop candidates that don't satisfy minimum thresholds.
        """
        self.min_skill_match_threshold = min_skill_match_threshold
        self.min_experience_match_threshold = min_experience_match_threshold
        self.strict_filtering = strict_filtering

    def filter_candidates(
        self,
        candidates: List[Dict],
        jd_metadata: Dict
    ) -> List[Dict]:
        filtered = []

        for candidate in candidates:
            skill_ok = (candidate["skill_match_score"] >= self.min_skill_match_threshold)

            exp_ok = (candidate["experience_match_score"] >= self.min_experience_match_threshold)

            # If strict mode, remove obviously unsuitable candidates
            if self.strict_filtering and not (skill_ok and exp_ok):
                continue

            # Otherwise keep them but annotate requirement status
            candidate["passes_skill_requirement"] = skill_ok
            candidate["passes_experience_requirement"] = exp_ok
            candidate["passes_education_requirement"] = (candidate["education_match_score"] >= 1.0)

            filtered.append(candidate)

        return filtered


class RankingEngine:
    """
    Convert candidate signals into business-friendly 0-100 scores.
    """

    def compute_final_scores(self, candidates: List[Dict]) -> List[Dict]:
        """
        Final weighted score (0-100).

        Weights:
            55% semantic similarity
            25% skill match
            15% experience match
            5% education match

        Then apply penalties if candidate fails key requirements.
        """

        ranked = []

        for candidate in candidates:
            semantic = candidate["semantic_score"]
            skill = candidate["skill_match_score"]
            experience = candidate["experience_match_score"]
            education = candidate["education_match_score"]

            base_score = (
                0.55 * semantic +
                0.25 * skill +
                0.15 * experience +
                0.05 * education
            )

            penalty_multiplier = 1.0

            if not candidate.get("passes_skill_requirement", True):
                penalty_multiplier *= 0.75

            if not candidate.get("passes_experience_requirement", True):
                penalty_multiplier *= 0.80

            if not candidate.get("passes_education_requirement", True):
                penalty_multiplier *= 0.95

            final_score = base_score * penalty_multiplier * 100

            candidate["final_score"] = round(final_score, 2)
            candidate["match_category"] = self._score_to_category(
                candidate["final_score"]
            )

            ranked.append(candidate)

        ranked.sort(
            key=lambda x: x["final_score"],
            reverse=True
        )

        return ranked

    def _score_to_category(self, score: float) -> str:
        if score >= 90:
            return "Excellent"
        elif score >= 75:
            return "Strong"
        elif score >= 60:
            return "Moderate"
        return "Weak"


class MatchReasoner:
    """
    Phase 13:
    Generate human-readable explanation for why a candidate matched.
    """

    def _pick_relevant_excerpts(
        self,
        matched_chunks: List[str],
        max_excerpts: int = 2,
        max_excerpt_length: int = 220
    ) -> List[str]:
        excerpts = []

        for chunk in matched_chunks[:max_excerpts]:
            chunk = chunk.strip()
            if len(chunk) > max_excerpt_length:
                chunk = chunk[:max_excerpt_length].rstrip() + "..."
            excerpts.append(chunk)

        return excerpts

    def generate_reasoning(self, candidate: Dict, jd_metadata: Dict) -> Dict:
        matched_skills = candidate.get("matched_skills", [])
        missing_skills = candidate.get("missing_skills", [])

        excerpts = self._pick_relevant_excerpts(candidate.get("matched_chunks", []))

        reasoning_parts = []

        # Skills
        if matched_skills:
            reasoning_parts.append(f"Matched skills: {', '.join(matched_skills)}.")

        if missing_skills:
            reasoning_parts.append(f"Missing skills: {', '.join(missing_skills)}.")

        # Experience
        required_exp = jd_metadata.get("experience_years", 0)
        candidate_exp = candidate.get("experience_years", 0)

        if required_exp > 0:
            if candidate_exp >= required_exp:
                reasoning_parts.append(
                    f"Meets the experience requirement with "
                    f"{candidate_exp} years vs required {required_exp} years."
                )
            else:
                reasoning_parts.append(
                    f"Has {candidate_exp} years of experience, below the "
                    f"required {required_exp} years."
                )

        # Education
        required_education = jd_metadata.get("education", "")
        candidate_education = candidate.get("education", "")

        if required_education:
            if candidate.get("education_match_score", 0) >= 1.0:
                reasoning_parts.append(
                    f"Meets the education requirement "
                    f"({candidate_education})."
                )
            else:
                reasoning_parts.append(
                    f"Does not fully meet the education requirement. "
                    f"Candidate education: {candidate_education or 'Not found'}."
                )

        # Semantic relevance
        reasoning_parts.append(f"Retrieved relevant resume sections: "
            f"{', '.join(candidate.get('matched_sections', []))}."
        )

        reasoning = " ".join(reasoning_parts).strip()

        return {
            "matched_skills": matched_skills,
            "missing_skills": missing_skills,
            "relevant_excerpts": excerpts,
            "reasoning": reasoning
        }


class JobMatcher:
    def __init__(self):
        self.jd_processor = JobDescriptionProcessor()
        self.semantic_searcher = SemanticSearcher()
        self.hybrid_searcher = HybridSearcher()
        self.requirement_filter = RequirementFilter(strict_filtering=False)
        self.ranking_engine = RankingEngine()
        self.reasoner = MatchReasoner()

    def match(self, job_description: str, top_k: int = 5) -> Dict:
        """
        Final assignment-style output.

        Returns:
        {
            "job_description": "...",
            "top_matches": [...]
        }
        """

        jd_metadata = self.jd_processor.process(job_description)
        chroma_results = self.semantic_searcher.search(jd_metadata["raw_text"], top_k * 5)

        candidates = self.hybrid_searcher.aggregate_candidates(chroma_results)
        candidates = self.hybrid_searcher.enrich_candidates(
            candidates=candidates,
            jd_metadata=jd_metadata
        )

        # Phase 11
        candidates = self.requirement_filter.filter_candidates(
            candidates=candidates,
            jd_metadata=jd_metadata
        )

        # Phase 12
        candidates = self.ranking_engine.compute_final_scores(
            candidates
        )

        # Phase 13 + 14
        top_matches = []
        for candidate in candidates[:top_k]:
            reasoning_payload = self.reasoner.generate_reasoning(
                candidate=candidate,
                jd_metadata=jd_metadata
            )

            top_matches.append({
                "candidate_name": candidate["candidate_name"],
                "resume_name": candidate["resume_name"],
                "final_score": candidate["final_score"],
                "match_category": candidate["match_category"],
                "experience_years": candidate["experience_years"],
                "education": candidate["education"],
                "matched_sections": candidate["matched_sections"],
                "matched_skills": reasoning_payload["matched_skills"],
                "missing_skills": reasoning_payload["missing_skills"],
                "relevant_excerpts": reasoning_payload["relevant_excerpts"],
                "reasoning": reasoning_payload["reasoning"]
            })

        return {
            "job_description": job_description,
            "top_matches": top_matches
        }
    