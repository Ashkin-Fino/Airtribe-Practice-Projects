"""
Final Hiring Report Generator
"""
from agent.candidate_models import MatchResult


class HiringReportGenerator:

    @staticmethod
    def build(match_result: MatchResult) -> dict:
        """
        Build the final structured report.
        """
        return {
            "job_description": match_result.job_requirements.raw_text,
            "top_matches": [
                {
                    "candidate_name": c.candidate_name,
                    "final_score": c.final_score,
                    "summary": c.summary,
                    "strengths": c.strengths,
                    "weaknesses": c.weaknesses,
                    "risk_level": c.risk_level,
                }
                for c in sorted(match_result.candidates, key=lambda x: x.final_score, reverse=True)
            ],
            "comparison": match_result.comparison,
            "interview_plan": {c.candidate_name: c.interview_plan for c in match_result.candidates}
        }
