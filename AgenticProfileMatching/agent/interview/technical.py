"""
Technical Interview Question Generator
"""

from candidate_models import Candidate


class TechnicalQuestionGenerator:

    @staticmethod
    def generate(candidate: Candidate) -> list[dict]:

        questions = []

        # Questions for required skills
        for skill in candidate.matched_skills:
            questions.append({
                "skill": skill,
                "question": f"Explain your experience with {skill}.",
                "type": "Technical",
            })

        # Questions for missing skills
        for skill in candidate.missing_skills:
            questions.append({
                "skill": skill,
                "question": f"You have limited experience with {skill}. How would you approach learning and applying it in a production environment?",
                "type": "Technical",
            })

        return questions
    