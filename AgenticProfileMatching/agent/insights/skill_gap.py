"""
Skill Gap Analysis

Computes matched skills, missing skills, extra skills and
skill coverage for a candidate.
"""

from agent.candidate_models import Candidate, JobRequirements


class SkillGapAnalyzer:

    @staticmethod
    def analyze(candidate: Candidate, requirements: JobRequirements) -> Candidate:
        """
        Populate skill gap information for a candidate.
        """
        candidate_skills = {skill.lower().strip() for skill in candidate.skills}
        required_skills = {skill.lower().strip() for skill in requirements.skills}
        matched = sorted(candidate_skills & required_skills)
        missing = sorted(required_skills - candidate_skills)
        extra = sorted(candidate_skills - required_skills)

        candidate.matched_skills = matched
        candidate.missing_skills = missing
        candidate.extra_skills = extra

        if required_skills:
            candidate.skill_coverage = (len(matched) / len(required_skills)) * 100
        else:
            candidate.skill_coverage = 100.0

        return candidate
    