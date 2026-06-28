from typing import TypedDict, List, Optional

from candidate_models import (
    Candidate,
    JobRequirements,
    MatchResult
)


class AgentState(TypedDict):
    job_description: str
    job_requirements: Optional[JobRequirements]
    candidates: List[Candidate]
    match_result: Optional[MatchResult]
    reasoning: List[str]