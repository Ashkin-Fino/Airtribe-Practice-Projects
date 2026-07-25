from typing import TypedDict, List, Optional

from agent.candidate_models import MatchResult


class AgentState(TypedDict):
    job_description: str
    match_result: Optional[MatchResult]
    report: Optional[dict]
    reasoning: List[str]
