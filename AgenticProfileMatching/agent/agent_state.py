from typing import List, Optional, TypedDict

from agent.candidate_models import JobRequirements, MatchResult


class AgentState(TypedDict):
    job_description: str
    job_requirements: Optional[JobRequirements]
    match_result: Optional[MatchResult]
    report: Optional[dict]
    reasoning: List[str]
    