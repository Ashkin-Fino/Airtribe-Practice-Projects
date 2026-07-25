"""
Candidate Summary Generator
"""

from agent.candidate_models import Candidate


class CandidateSummaryGenerator:

    @staticmethod
    def generate(candidate: Candidate) -> Candidate:
        """
        Generates a concise summary.
        """

        top_skills = ", ".join(candidate.skills[:5])

        summary = (
            f"{candidate.candidate_name} has "
            f"{candidate.experience_years} years of experience. "
            f"The candidate possesses skills in {top_skills}. "
            f"The overall match score is "
            f"{candidate.final_score:.1f}%."
        )

        candidate.summary = summary

        return candidate
    