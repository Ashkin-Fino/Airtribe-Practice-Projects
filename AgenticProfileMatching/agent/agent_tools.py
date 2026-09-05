from pathlib import Path
from typing import Any

from agent.config import DEFAULT_TOP_K, configure_imports

# The milestone projects live beside AgenticProfileMatching.
# Configure their import paths before importing Milestone 2 modules.
configure_imports()

from RAGBasedProfileMatching.resume_rag import ResumeRAGPipeline
from RAGBasedProfileMatching.job_matcher import (
    JobMatcher,
    JobDescriptionProcessor,
)

from agent.mcp_filesystem_client import MCPFileSystemClient


class AgentTools:
    """Integration layer between the LangGraph agent and project services."""

    def __init__(self):
        self.filesystem = MCPFileSystemClient()
        self.pipeline = ResumeRAGPipeline()
        self.matcher = JobMatcher()
        self.job_processor = JobDescriptionProcessor()

    # ------------------------------------------------------------------
    # MCP filesystem operations
    # ------------------------------------------------------------------

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
        return self.filesystem.batch_process(file_paths, operation)

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

    # ------------------------------------------------------------------
    # Resume indexing
    # ------------------------------------------------------------------

    def index_resume(self, resume_path: str) -> Any:
        # Validate/read through MCP first. The RAG pipeline remains the
        # Milestone 2 component responsible for embedding/indexing.
        self.filesystem.read_file(resume_path)
        return self.pipeline.index_resume(resume_path)

    def index_resume_directory(self, directory: str) -> list[Any]:
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

    # ------------------------------------------------------------------
    # Agent matching operations
    # ------------------------------------------------------------------

    def extract_job_requirements(self, job_description: str) -> Any:
        return self.job_processor.extract_requirements(job_description)

    def match_candidates(
        self,
        job_description: str,
        top_k: int = DEFAULT_TOP_K,
    ) -> Any:
        return self.matcher.match_candidates(
            job_description,
            top_k=top_k,
        )

    def enrich_candidates(self, candidates: Any) -> Any:
        return self.matcher.enrich_candidates(candidates)

    def compare_candidates(self, candidates: Any) -> Any:
        return self.matcher.compare_candidates(candidates)

    def generate_interview_plan(self, candidates: Any) -> Any:
        return self.matcher.generate_interview_plan(candidates)

    def generate_report(
        self,
        job_description: str,
        candidates: Any,
        comparison: Any = None,
        interview_plan: Any = None,
    ) -> Any:
        return self.matcher.generate_report(
            job_description=job_description,
            candidates=candidates,
            comparison=comparison,
            interview_plan=interview_plan,
        )
