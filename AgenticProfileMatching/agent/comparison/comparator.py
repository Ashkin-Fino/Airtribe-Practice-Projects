"""
Pairwise Candidate Comparator
"""

from agent.candidate_models import Candidate


class CandidateComparator:

    @staticmethod
    def compare(candidate1: Candidate, candidate2: Candidate,) -> dict:
        return {
            candidate1.candidate_name: {
                "score": candidate1.final_score,
                "experience": candidate1.experience_years,
                "coverage": candidate1.skill_coverage,
                "risk": candidate1.risk_level,
            },
            candidate2.candidate_name: {
                "score": candidate2.final_score,
                "experience": candidate2.experience_years,
                "coverage": candidate2.skill_coverage,
                "risk": candidate2.risk_level,
            },
        }
    