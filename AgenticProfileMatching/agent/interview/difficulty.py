"""
Difficulty Grading
"""

from agent.candidate_models import Candidate


class DifficultyGrader:

    @staticmethod
    def grade(candidate: Candidate) -> str:
        if candidate.final_score >= 85:
            return "Advanced"
        if candidate.final_score >= 70:
            return "Intermediate"
        return "Beginner"
    