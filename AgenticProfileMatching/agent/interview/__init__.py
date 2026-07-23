from .interview_plan import InterviewPlanner


class InterviewGenerationEngine:

    @staticmethod
    def generate(match_result):
        requirements = match_result.job_requirements
        for candidate in match_result.candidates:
            InterviewPlanner.build(candidate, requirements)
        return match_result
    