"""
Canonical data models used throughout Milestone 3.

Milestone 2 returns dictionaries as output. This module converts those
dictionaries into strongly-typed objects so the rest of the application
doesn't depend on Milestone 2's implementation details.
"""

from __future__ import annotations

from dataclasses import dataclass, field, asdict
from typing import Any


# -------------------------------------------------------------------------
# Job Requirements
# -------------------------------------------------------------------------

@dataclass
class JobRequirements:
    """
    Structured representation of a job description.
    """

    raw_text: str

    skills: list[str] = field(default_factory=list)

    experience_years: int = 0

    education: str = ""

    metadata: dict[str, Any] = field(default_factory=dict)

    @classmethod
    def from_dict(cls, data: dict[str, Any]) -> "JobRequirements":
        return cls(
            raw_text=data.get("raw_text", ""),
            skills=data.get("skills", []),
            experience_years=data.get("experience_years", 0),
            education=data.get("education", ""),
            metadata=data,
        )

    def to_dict(self) -> dict[str, Any]:
        return asdict(self)


# -------------------------------------------------------------------------
# Candidate
# -------------------------------------------------------------------------

@dataclass
class Candidate:
    """
    Canonical candidate model used by Milestone 3.
    """

    # Identity
    candidate_name: str
    resume_name: str

    # Final ranking
    final_score: float = 0.0
    match_category: str = ""

    # Resume metadata
    experience_years: int = 0
    education: str = ""

    # Matching details
    matched_skills: list[str] = field(default_factory=list)
    missing_skills: list[str] = field(default_factory=list)

    # Useful for later phases
    reasoning: str = ""
    summary: str = ""
    strengths: list[str] = field(default_factory=list)
    weaknesses: list[str] = field(default_factory=list)
    risk_level: str = ""

    # Extra information from Milestone 2 or future phases
    metadata: dict[str, Any] = field(default_factory=dict)

    @classmethod
    def from_job_matcher(cls, data: dict[str, Any]) -> "Candidate":
        """
        Convert JobMatcher output into a Candidate object.
        """

        return cls(
            candidate_name=data.get("candidate_name", ""),
            resume_name=data.get("resume_name", ""),
            final_score=float(data.get("final_score", 0.0)),
            match_category=data.get("match_category", ""),
            experience_years=int(data.get("experience_years", 0)),
            education=data.get("education", ""),
            matched_skills=data.get("matched_skills", []),
            missing_skills=data.get("missing_skills", []),
            reasoning=data.get("reasoning", ""),
            summary=data.get("summary", ""),
            metadata=data,
        )

    def to_dict(self) -> dict[str, Any]:
        return asdict(self)


# -------------------------------------------------------------------------
# Match Result
# -------------------------------------------------------------------------

@dataclass
class MatchResult:
    """
    Standard response returned by AgentTools.
    """

    job_requirements: JobRequirements

    candidates: list[Candidate] = field(default_factory=list)

    total_candidates: int = 0

    @classmethod
    def from_job_matcher(
        cls,
        job_matcher_result: dict[str, Any],
        job_requirements: JobRequirements,
    ) -> "MatchResult":

        candidates = [
            Candidate.from_job_matcher(candidate)
            for candidate in job_matcher_result.get("top_matches", [])
        ]

        return cls(
            job_requirements=job_requirements,
            candidates=candidates,
            total_candidates=len(candidates),
        )

    def to_dict(self) -> dict[str, Any]:
        return {
            "job_requirements": self.job_requirements.to_dict(),
            "total_candidates": self.total_candidates,
            "candidates": [
                candidate.to_dict()
                for candidate in self.candidates
            ],
        }
    