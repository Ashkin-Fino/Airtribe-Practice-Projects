"""
Candidate Reasoning Generator
"""

from agent.candidate_models import Candidate


class ReasoningGenerator:

    @staticmethod
    def generate(candidate: Candidate) -> Candidate:
        """
        Generate deterministic reasoning explaining
        why the candidate received the current score.
        """

        reasoning = []

        if candidate.skill_coverage >= 80:
            reasoning.append(
                "Excellent required skill coverage."
            )

        elif candidate.skill_coverage >= 60:
            reasoning.append(
                "Good skill coverage."
            )

        else:
            reasoning.append(
                "Limited required skill coverage."
            )

        if candidate.experience_years >= 8:
            reasoning.append(
                "Highly experienced professional."
            )

        elif candidate.experience_years >= 4:
            reasoning.append(
                "Relevant industry experience."
            )

        else:
            reasoning.append(
                "Limited professional experience."
            )

        if candidate.missing_skills:
            reasoning.append(
                f"Missing skills: {', '.join(candidate.missing_skills)}."
            )

        candidate.reasoning = " ".join(reasoning)

        return candidate
    