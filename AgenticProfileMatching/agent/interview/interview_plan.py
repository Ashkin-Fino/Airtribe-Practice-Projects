"""
Interview Planner
"""

from agent.candidate_models import Candidate, JobRequirements

from agent.interview.technical import TechnicalQuestionGenerator
from agent.interview.behavioral import BehavioralQuestionGenerator
from agent.interview.role_specific import RoleSpecificQuestionGenerator
from agent.interview.difficulty import DifficultyGrader


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
