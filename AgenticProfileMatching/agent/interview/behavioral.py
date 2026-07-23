"""
Behavioral Interview Question Generator
"""

from candidate_models import Candidate


class BehavioralQuestionGenerator:

    @staticmethod
    def generate(candidate: Candidate) -> list[str]:

        return ["Tell me about a challenging project you worked on.",
            "Describe a conflict within your team and how you resolved it.",
            "Tell me about a time when you had to learn a new technology quickly.",
            "Describe a production issue you solved.",
            "How do you prioritize multiple deadlines?"
        ]
