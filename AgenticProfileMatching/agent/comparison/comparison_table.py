"""
Structured comparison table.
"""

from candidate_models import Candidate


class ComparisonTable:

    @staticmethod
    def build(candidates: list[Candidate],) -> list[dict]:

        table = []
        for candidate in candidates:
            table.append({
                "Candidate": candidate.candidate_name,
                "Score": candidate.final_score,
                "Coverage": candidate.skill_coverage,
                "Experience": candidate.experience_years,
                "Risk": candidate.risk_level,
            })
        return table
    