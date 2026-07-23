"""
Interview Planner
"""

from candidate_models import Candidate, JobRequirements

from .technical import TechnicalQuestionGenerator
from .behavioral import BehavioralQuestionGenerator
from .role_specific import RoleSpecificQuestionGenerator
from .difficulty import DifficultyGrader


class InterviewPlanner:
    @staticmethod
    def build(candidate: Candidate, requirements: JobRequirements,) -> Candidate:
        candidate.interview_plan = {
            "difficulty": DifficultyGrader.grade(candidate),
            "technical": TechnicalQuestionGenerator.generate(candidate),
            "behavioral": BehavioralQuestionGenerator.generate(candidate),
            "role_specific": RoleSpecificQuestionGenerator.generate(candidate, requirements),
        }
        return candidate
