from __future__ import annotations

from dataclasses import asdict, dataclass, field
from typing import Any


@dataclass
class JobRequirements:
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


@dataclass
class Candidate:
    candidate_name: str
    resume_name: str

    final_score: float = 0.0
    match_category: str = ""

    experience_years: int = 0
    education: str = ""
    skills: list[str] = field(default_factory=list)

    matched_skills: list[str] = field(default_factory=list)
    missing_skills: list[str] = field(default_factory=list)
    extra_skills: list[str] = field(default_factory=list)

    skill_coverage: float = 0.0

    reasoning: str = ""
    summary: str = ""

    strengths: list[str] = field(default_factory=list)
    weaknesses: list[str] = field(default_factory=list)

    risk_level: str = ""

    metadata: dict[str, Any] = field(default_factory=dict)

    @classmethod
    def from_job_matcher(cls, data: dict[str, Any]) -> "Candidate":
        return cls(
            candidate_name=data.get("candidate_name", ""),
            resume_name=data.get("resume_name", ""),
            final_score=float(data.get("final_score", 0.0)),
            match_category=data.get("match_category", ""),
            experience_years=int(data.get("experience_years", 0)),
            education=data.get("education", ""),
            skills=data.get("skills", []),
            matched_skills=data.get("matched_skills", []),
            missing_skills=data.get("missing_skills", []),
            extra_skills=data.get("extra_skills", []),
            skill_coverage=float(data.get("skill_coverage", 0.0)),
            reasoning=data.get("reasoning", ""),
            summary=data.get("summary", ""),
            strengths=data.get("strengths", []),
            weaknesses=data.get("weaknesses", []),
            risk_level=data.get("risk_level", ""),
            metadata=data,
        )

    def to_dict(self) -> dict[str, Any]:
        return asdict(self)


@dataclass
class MatchResult:
    job_requirements: JobRequirements

    candidates: list[Candidate] = field(default_factory=list)
    total_candidates: int = 0

    comparison: dict[str, Any] = field(default_factory=dict)
    final_recommendation: str = ""

    interview_plan: dict[str, Any] = field(default_factory=dict)

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
            "comparison": self.comparison,
            "final_recommendation": self.final_recommendation,
            "interview_plan": self.interview_plan,
        }
    