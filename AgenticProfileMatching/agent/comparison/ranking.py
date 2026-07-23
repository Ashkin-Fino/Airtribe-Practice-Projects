"""
Ranking Justification
"""

from candidate_models import Candidate


class RankingJustifier:

    @staticmethod
    def justify(candidate: Candidate) -> str:

        reasons = []

        if candidate.final_score >= 85:
            reasons.append("Excellent overall match")
        elif candidate.final_score >= 70:
            reasons.append("Strong overall match")
        else:
            reasons.append("Moderate overall match")

        if candidate.skill_coverage >= 80:
            reasons.append("High skill coverage")
        if candidate.experience_years >= 5:
            reasons.append("Experienced professional")
        if candidate.missing_skills:
            reasons.append(f"Missing: {', '.join(candidate.missing_skills)}")

        return ". ".join(reasons) + "."
    