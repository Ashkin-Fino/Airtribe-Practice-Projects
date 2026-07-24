"""
Candidate Insight Generator

Creates structured strengths, weaknesses
and overall risk assessment.
"""

from agent.candidate_models import Candidate


class CandidateInsightGenerator:

    @staticmethod
    def generate(candidate: Candidate) -> Candidate:
        """
        Populate strengths, weaknesses and risk level.
        """

        strengths = []
        weaknesses = []

        if candidate.skill_coverage >= 80:
            strengths.append("Strong skill alignment")

        elif candidate.skill_coverage >= 60:
            strengths.append("Good skill alignment")

        else:
            weaknesses.append("Low skill alignment")

        if candidate.experience_years >= 5:
            strengths.append(
                "Experienced professional"
            )
        else:
            weaknesses.append(
                "Limited experience"
            )

        if candidate.final_score >= 85:
            risk = "Low"

        elif candidate.final_score >= 70:
            risk = "Medium"

        else:
            risk = "High"

        if candidate.missing_skills:
            weaknesses.append(
                "Missing required skills"
            )

        candidate.strengths = strengths
        candidate.weaknesses = weaknesses
        candidate.risk_level = risk

        return candidate
    