"""
Role Specific Questions
"""

from candidate_models import Candidate, JobRequirements


class RoleSpecificQuestionGenerator:

    @staticmethod
    def generate(candidate: Candidate, requirements: JobRequirements,) -> list[str]:

        questions = []

        if "backend" in requirements.raw_text.lower():
            questions.append("How would you design a scalable REST API?")
            questions.append("Explain horizontal vs vertical scaling.")

        if "aws" in requirements.raw_text.lower():
            questions.append("Explain an AWS architecture you have built.")

        if "database" in requirements.raw_text.lower():
            questions.append("How do you optimize slow SQL queries?")

        return questions
