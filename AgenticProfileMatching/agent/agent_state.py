from typing import TypedDict, List, Optional

from candidate_models import (
    Candidate,
    JobRequirements,
    MatchResult
)


class AgentState(TypedDict):
    job_description: str
    match_result: Optional[MatchResult]
    report: Optional[dict]
    reasoning: List[str]
