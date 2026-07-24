"""
Final Recommendation Engine
"""

from agent.candidate_models import Candidate


class RecommendationEngine:

    @staticmethod
    def recommend(candidates: list[Candidate]) -> Candidate:

        return max(
            candidates,
            key=lambda c: (c.final_score, c.skill_coverage, c.experience_years)
        )
    