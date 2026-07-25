from .skill_gap import SkillGapAnalyzer
from .candidate_summary import CandidateSummaryGenerator
from .reasoning import ReasoningGenerator
from .insights import CandidateInsightGenerator


class CandidateIntelligence:
    @staticmethod
    def enrich(candidate, requirements):
        candidate = SkillGapAnalyzer.analyze(candidate, requirements,)
        candidate = CandidateSummaryGenerator.generate(candidate)
        candidate = ReasoningGenerator.generate(candidate)
        candidate = CandidateInsightGenerator.generate(candidate)
        return candidate
