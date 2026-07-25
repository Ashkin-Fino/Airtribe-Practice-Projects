from .comparator import CandidateComparator
from .ranking import RankingJustifier
from .recommendation import RecommendationEngine
from .comparison_table import ComparisonTable


class CandidateComparisonEngine:

    @staticmethod
    def compare(match_result):

        candidates = sorted(match_result.candidates, key=lambda c: c.final_score, reverse=True,)
        if len(candidates) < 2:
            return match_result

        top = candidates[:2]
        comparison = CandidateComparator.compare(top[0], top[1])
        table = ComparisonTable.build(top)
        winner = RecommendationEngine.recommend(top)

        match_result.comparison = {
            "table": table,
            "pairwise": comparison,
            "winner": winner.candidate_name,
        }

        match_result.final_recommendation = (RankingJustifier.justify(winner))

        return match_result
