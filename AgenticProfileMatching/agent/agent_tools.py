from pathlib import Path
from typing import Any

from agent.config import DEFAULT_TOP_K, configure_imports

configure_imports()

from RAGBasedProfileMatching.job_matcher import (
    JobDescriptionProcessor,
    JobMatcher,
)
from RAGBasedProfileMatching.resume_rag import ResumeRAGPipeline

from agent.candidate_models import (
    Candidate,
    JobRequirements,
    MatchResult,
)
from agent.mcp_filesystem_client import MCPFileSystemClient


class AgentTools:

    def __init__(self):
        self.filesystem = MCPFileSystemClient()

        self.pipeline = ResumeRAGPipeline()
        self.matcher = JobMatcher()
        self.job_processor = JobDescriptionProcessor()

    # ============================================================
    # MCP FILESYSTEM OPERATIONS
    # ============================================================

    def discover_filesystem(self) -> dict[str, Any]:
        return self.filesystem.discover()

    def read_file(self, file_path: str) -> str:
        return self.filesystem.read_file(file_path)

    def search_resume_files(
        self,
        directory: str,
        keyword: str,
    ) -> list[dict[str, Any]]:
        return self.filesystem.search_files(directory, keyword)

    def summarize_resume(self, resume_path: str) -> str:
        result = self.filesystem.summarize_file(resume_path)

        if isinstance(result, dict):
            return str(
                result.get("summary")
                or result.get("content")
                or result
            )

        return str(result)

    def generate_summary_file(
        self,
        file_path: str,
        output_path: str | None = None,
    ) -> dict[str, Any]:
        return self.filesystem.generate_summary_file(
            file_path,
            output_path,
        )

    def batch_process(
        self,
        file_paths: list[str],
        operation: str = "read",
    ) -> list[dict[str, Any]]:
        return self.filesystem.batch_process(
            file_paths,
            operation,
        )

    def watch_directory(
        self,
        directory: str,
        duration_seconds: int = 30,
        poll_interval_seconds: int = 2,
    ) -> list[dict[str, Any]]:
        return self.filesystem.watch_directory(
            directory,
            duration_seconds,
            poll_interval_seconds,
        )

    # ============================================================
    # RESUME INDEXING
    # ============================================================

    def index_resume(self, resume_path: str) -> Any:
        # Read through MCP first so the agent architecture
        # uses the filesystem server as the file-access layer.
        self.filesystem.read_file(resume_path)

        return self.pipeline.index_resume(resume_path)

    def index_resume_directory(
        self,
        directory: str,
    ) -> list[Any]:

        files = self.filesystem.list_files(directory)

        results = []

        for metadata in files:
            resume_path = metadata["path"]

            try:
                self.filesystem.read_file(resume_path)

                results.append(
                    self.pipeline.index_resume(resume_path)
                )

            except Exception as exc:
                results.append(
                    {
                        "path": resume_path,
                        "status": "error",
                        "error": str(exc),
                    }
                )

        return results

    # ============================================================
    # JOB REQUIREMENTS
    # ============================================================

    def extract_job_requirements(
        self,
        job_description: str,
    ) -> JobRequirements:

        # Milestone 2 exposes `process()`, not
        # `extract_requirements()`.
        raw_requirements = self.job_processor.process(
            job_description
        )

        return JobRequirements.from_dict(
            raw_requirements
        )

    # ============================================================
    # CANDIDATE MATCHING
    # ============================================================

    def match_candidates(
        self,
        job_description: str,
        top_k: int = DEFAULT_TOP_K,
        job_requirements: JobRequirements | None = None,
    ) -> MatchResult:

        # Milestone 2 exposes `match()`, not
        # `match_candidates()`.
        raw_result = self.matcher.match(
            job_description,
            top_k=top_k,
        )

        if job_requirements is None:
            job_requirements = self.extract_job_requirements(
                job_description
            )

        return MatchResult.from_job_matcher(
            raw_result,
            job_requirements,
        )

    # ============================================================
    # CANDIDATE INTELLIGENCE
    # ============================================================

    def enrich_candidates(
        self,
        match_result: MatchResult,
    ) -> MatchResult:

        requirements = match_result.job_requirements

        for candidate in match_result.candidates:

            strengths = []
            weaknesses = []

            # ----------------------------------------------------
            # Experience analysis
            # ----------------------------------------------------

            required_experience = requirements.experience_years

            if required_experience > 0:

                if candidate.experience_years >= required_experience:
                    strengths.append(
                        f"Meets or exceeds the required "
                        f"{required_experience}+ years of experience."
                    )
                else:
                    gap = (
                        required_experience
                        - candidate.experience_years
                    )

                    weaknesses.append(
                        f"Has approximately {gap} fewer years "
                        f"of experience than required."
                    )

            # ----------------------------------------------------
            # Match score analysis
            # ----------------------------------------------------

            if candidate.final_score >= 80:
                strengths.append(
                    "Strong overall match based on the ranking score."
                )

            elif candidate.final_score >= 60:
                strengths.append(
                    "Moderate overall match based on the ranking score."
                )

            else:
                weaknesses.append(
                    "Overall match score is relatively low."
                )

            # ----------------------------------------------------
            # Existing Milestone 2 reasoning
            # ----------------------------------------------------

            if candidate.reasoning:
                strengths.append(
                    "Detailed matching reasoning is available "
                    "from the semantic and rule-based matcher."
                )

            # ----------------------------------------------------
            # Match category
            # ----------------------------------------------------

            if candidate.match_category:
                strengths.append(
                    f"Classified as '{candidate.match_category}' "
                    "by the matching engine."
                )

            # ----------------------------------------------------
            # Education
            # ----------------------------------------------------

            if requirements.education:

                candidate_education = (
                    candidate.education.lower()
                    if candidate.education
                    else ""
                )

                required_education = (
                    requirements.education.lower()
                )

                if (
                    candidate_education
                    and (
                        candidate_education in required_education
                        or required_education in candidate_education
                    )
                ):
                    strengths.append(
                        "Education appears aligned with "
                        "the job requirement."
                    )

            candidate.strengths = strengths
            candidate.weaknesses = weaknesses

            # ----------------------------------------------------
            # Risk classification
            # ----------------------------------------------------

            if candidate.final_score >= 80 and not weaknesses:
                candidate.risk_level = "LOW"

            elif candidate.final_score >= 60:
                candidate.risk_level = "MEDIUM"

            else:
                candidate.risk_level = "HIGH"

            # ----------------------------------------------------
            # Human-readable summary
            # ----------------------------------------------------

            candidate.summary = (
                f"{candidate.candidate_name} is a "
                f"{candidate.match_category or 'candidate'} "
                f"with a final match score of "
                f"{candidate.final_score:.2f}. "
                f"Risk level: {candidate.risk_level}."
            )

        return match_result

    # ============================================================
    # CANDIDATE COMPARISON
    # ============================================================

    def compare_candidates(
        self,
        match_result: MatchResult,
    ) -> MatchResult:

        candidates = sorted(
            match_result.candidates,
            key=lambda candidate: candidate.final_score,
            reverse=True,
        )

        ranking = []

        for rank, candidate in enumerate(
            candidates,
            start=1,
        ):
            ranking.append(
                {
                    "rank": rank,
                    "candidate_name": candidate.candidate_name,
                    "resume_name": candidate.resume_name,
                    "final_score": candidate.final_score,
                    "match_category": candidate.match_category,
                    "experience_years": candidate.experience_years,
                    "risk_level": candidate.risk_level,
                    "strengths": candidate.strengths,
                    "weaknesses": candidate.weaknesses,
                }
            )

        best_candidate = (
            candidates[0]
            if candidates
            else None
        )

        if best_candidate:

            recommendation = (
                f"Recommend {best_candidate.candidate_name} "
                f"as the strongest candidate based on the "
                f"current matching score of "
                f"{best_candidate.final_score:.2f}."
            )

            best_candidate_data = {
                "candidate_name": best_candidate.candidate_name,
                "resume_name": best_candidate.resume_name,
                "final_score": best_candidate.final_score,
                "match_category": best_candidate.match_category,
                "risk_level": best_candidate.risk_level,
            }

        else:

            recommendation = (
                "No candidates were found for the supplied "
                "job description."
            )

            best_candidate_data = None

        match_result.comparison = {
            "ranking": ranking,
            "best_candidate": best_candidate_data,
        }

        match_result.final_recommendation = recommendation

        return match_result

    # ============================================================
    # INTERVIEW PLAN
    # ============================================================

    def generate_interview_plan(
        self,
        match_result: MatchResult,
    ) -> MatchResult:

        requirements = match_result.job_requirements

        candidate_plans = {}

        for candidate in match_result.candidates:

            questions = [
                (
                    "Walk me through a project that is most "
                    "relevant to this role."
                ),
                (
                    "Describe a difficult backend or software "
                    "engineering problem you solved."
                ),
            ]

            if requirements.skills:

                for skill in requirements.skills[:3]:
                    questions.append(
                        f"Explain your hands-on experience with {skill}."
                    )

            if candidate.weaknesses:

                questions.append(
                    "Can you explain your experience in the areas "
                    "where your profile may have gaps for this role?"
                )

            candidate_plans[
                candidate.candidate_name
            ] = {
                "focus_areas": requirements.skills,
                "questions": questions,
                "risk_level": candidate.risk_level,
                "reasoning": candidate.reasoning,
            }

        match_result.interview_plan = {
            "candidates": candidate_plans,
            "general_focus_areas": requirements.skills,
        }

        return match_result

    # ============================================================
    # FINAL REPORT
    # ============================================================

    def generate_report(
        self,
        job_description: str,
        candidates: MatchResult,
        comparison: Any = None,
        interview_plan: Any = None,
    ) -> dict[str, Any]:

        if comparison is None:
            comparison = candidates.comparison

        if interview_plan is None:
            interview_plan = candidates.interview_plan

        return {
            "job_description": job_description,

            "job_requirements": (
                candidates.job_requirements.to_dict()
            ),

            "total_candidates": candidates.total_candidates,

            "candidates": [
                candidate.to_dict()
                for candidate in candidates.candidates
            ],

            "comparison": comparison,

            "final_recommendation": (
                candidates.final_recommendation
            ),

            "interview_plan": interview_plan,
        }
    