
class JobDescriptionProcessor:

    def parse(self, job_description: str):
        raise NotImplementedError


class SemanticSearcher:

    def search(self, job_description: str, top_k: int = 10):
        raise NotImplementedError


class HybridSearcher:

    def score(self, candidate, job_requirements):
        raise NotImplementedError


class RequirementFilter:

    def filter(self, candidates, requirements):
        raise NotImplementedError


class RankingEngine:

    def rank(self, candidates):
        raise NotImplementedError


class MatchReasoner:

    def explain(self, candidate, job_description):
        raise NotImplementedError


class JobMatcher:

    def match(self, job_description: str):
        raise NotImplementedError
