"""
Agent Tools

This module acts as the integration layer between Milestone 3 and the
previous milestones.

Responsibilities
----------------
- Reuse Milestone 1 (LLM File System)
- Reuse Milestone 2 (Resume RAG)
- Hide implementation details from the LangGraph agent
- Return canonical Candidate and MatchResult models
"""

from typing import Optional

from agent.config import DEFAULT_TOP_K
from agent.candidate_models import (
    Candidate,
    JobRequirements,
    MatchResult,
)
from agent.insights import CandidateIntelligence
from agent.comparison import CandidateComparisonEngine
from agent.interview import InterviewGenerationEngine
from agent.report import HiringReportGenerator

# Milestone 2
from RAGBasedProfileMatching.resume_rag import ResumeRAGPipeline
from RAGBasedProfileMatching.job_matcher import (
    JobMatcher,
    JobDescriptionProcessor,
)

# Milestone 1
from LLMPoweredFileSystem.modules.query_processor import QueryProcessor
from LLMPoweredFileSystem.modules.file_tools import FileTools

from exceptions import (
    AgentToolError,
    FileOperationError,
    CandidateMatchingError,
)


class AgentTools:
    """
    Facade over Milestone 1 and Milestone 2.

    LangGraph nodes should ONLY communicate with this class.

    They should never directly import classes from
    milestone1 or milestone2.
    """

    def __init__(self):
        self.pipeline = ResumeRAGPipeline()
        self.matcher = JobMatcher()
        self.jd_processor = JobDescriptionProcessor()
        self.query_processor = QueryProcessor()
        self.file_tools = FileTools()

    def extract_job_requirements(self, job_description: str) -> JobRequirements:
        """
        Convert a raw Job Description into structured requirements.
        """
        try:
            result = self.jd_processor.process(job_description)
            return JobRequirements.from_dict(result)
        except Exception as exc:
            raise AgentToolError(f"Unable to process job description: {exc}") from exc

    def match_candidates(
        self, job_description: str, top_k: int = DEFAULT_TOP_K
    ) -> MatchResult:
        """
            Search and rank candidates.
        """
        try:
            requirements = self.extract_job_requirements(job_description)
            raw_result = self.matcher.match(job_description, top_k)
            return MatchResult.from_job_matcher(raw_result, requirements)
        except Exception as exc:
            raise CandidateMatchingError(str(exc)) from exc
        
    def enrich_candidates(self, match_result: MatchResult) -> MatchResult:
        """
            Enrich every candidate with:
            - Skill gap analysis
            - Candidate summary
            - Match reasoning
            - Strengths / weaknesses
            - Risk assessment
        """

        requirements = match_result.job_requirements

        for candidate in match_result.candidates:
            CandidateIntelligence.enrich(candidate, requirements)

        return match_result
        
    def compare_candidates(self, match_result: MatchResult) -> MatchResult:
        """
        Compare the top-ranked candidates and generate
        a recommendation.
        """
        return CandidateComparisonEngine.compare(match_result)
    
    def generate_interview_plan(self, match_result: MatchResult) -> MatchResult:
        """
        Generate interview plans for all candidates.
        """
        return InterviewGenerationEngine.generate(match_result)
    
    def generate_report(self, match_result: MatchResult) -> dict:
        """
        Generate the final hiring report.
        """

        return HiringReportGenerator.build(match_result)

    # ==========================================================
    # Resume Indexing
    # ==========================================================

    def index_resume(self, resume_path: str) -> dict:
        """
        Index a single resume into the vector database.
        """
        return self.pipeline.index_resume(resume_path)

    def index_resume_directory(self, directory: str) -> dict:
        """
        Index every supported resume in a directory.
        """
        return self.pipeline.index_directory(directory)

    def reset_vector_database(self) -> None:
        """
        Delete all indexed resumes.
        """
        self.pipeline.vector_store.reset_collection()

    def process_query(self, query: str) -> str:
        """
        Process a natural language query using Milestone 1.
        """
        try:
            return self.query_processor.process(query)
        except Exception as exc:
            raise AgentToolError(f"Query processing failed: {exc}") from exc

    def read_file(self, file_path: str) -> str:
        """
        Generic file reader (JD, resume, notes, etc.)
        """
        try:
            return self.file_tools.read_file(file_path)
        except Exception as exc:
            raise FileOperationError(f"File read failed: {exc}") from exc

    def search_resume_files(self, keyword: str) -> list[str]:
        """
        Search resume files by keyword.
        """
        try:
            return self.file_tools.search_files(keyword)
        except Exception as exc:
            raise FileOperationError(f"Resume search failed: {exc}") from exc

    def summarize_resume(self, resume_text: str) -> str:
        """
        Summarize a resume using the Milestone 1 LLM pipeline.
        """
        try:
            return self.query_processor.summarize(resume_text)
        except Exception as exc:
            raise AgentToolError(f"Resume summarization failed: {exc}") from exc

    # ==========================================================
    # Candidate Utilities
    # ==========================================================

    def get_top_candidate(self, match_result: MatchResult) -> Optional[Candidate]:
        """
        Returns the highest ranked candidate.
        """
        if not match_result.candidates:
            return None

        return max(
            match_result.candidates,
            key=lambda c: c.final_score
        )

    def filter_candidates_by_score(
        self,
        match_result: MatchResult,
        min_score: float = 60.0
    ) -> list[Candidate]:
        """
        Return candidates above a minimum score.
        """
        return [
            candidate
            for candidate in match_result.candidates
            if candidate.final_score >= min_score
        ]

    def group_candidates_by_category(
        self,
        match_result: MatchResult
    ) -> dict[str, list[Candidate]]:
        """
        Group candidates by match category.
        """
        grouped: dict[str, list[Candidate]] = {}

        for candidate in match_result.candidates:
            grouped.setdefault(
                candidate.match_category,
                []
            ).append(candidate)

        return grouped

    # ==========================================================
    # Debug / Inspection Utilities
    # ==========================================================

    def debug_match_result(
        self,
        match_result: MatchResult
    ) -> dict:
        """
        Returns a debug-friendly structure.
        """
        return {
            "total_candidates": match_result.total_candidates,
            "top_scores": [
                {
                    "name": c.candidate_name,
                    "score": c.final_score,
                    "category": c.match_category,
                }
                for c in sorted(
                    match_result.candidates,
                    key=lambda x: x.final_score,
                    reverse=True
                )
            ],
        }

    def explain_candidate(
        self,
        candidate: Candidate
    ) -> str:
        """
        Human-readable explanation for CLI/UI.
        """
        return (
            f"{candidate.candidate_name} "
            f"({candidate.resume_name}) scored "
            f"{candidate.final_score:.1f} and is categorized as "
            f"{candidate.match_category}. "
            f"Experience: {candidate.experience_years} years. "
            f"Education: {candidate.education}."
        )
    