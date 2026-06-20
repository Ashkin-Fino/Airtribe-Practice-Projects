import argparse
import json
from pathlib import Path

from resume_rag import ResumeRAGPipeline
from job_matcher import JobMatcher

PROJECT_ROOT = Path(__file__).resolve().parent
DEFAULT_RESUMES_DIR = PROJECT_ROOT / "resumes"


def run_index(resume_dir: str, reset: bool):
    pipeline = ResumeRAGPipeline()

    if reset:
        pipeline.vector_store.reset_collection()

    result = pipeline.index_directory(resume_dir)

    print("\nIndexing Complete")
    print(f"Indexed Resumes: {result['indexed_resumes']}")
    print(f"Indexed Chunks: {result['indexed_chunks']}")
    print(f"Stored Chunks in ChromaDB: {pipeline.vector_store.count()}")


def run_match(job_description: str, top_k: int):
    matcher = JobMatcher()
    result = matcher.match(job_description=job_description, top_k=top_k)
    print(json.dumps(result, indent=2))


def main():
    parser = argparse.ArgumentParser(description="Resume RAG System and Job Matching Engine")
    subparsers = parser.add_subparsers(dest="command", required=True)

    # Index command
    index_parser = subparsers.add_parser("index", help="Index resumes into ChromaDB")
    index_parser.add_argument(
        "--resume-dir",
        type=str,
        default=str(DEFAULT_RESUMES_DIR),
        help="Path to directory containing resumes"
    )
    index_parser.add_argument(
        "--reset",
        action="store_true",
        help="Reset the Chroma collection before indexing"
    )

    # Match command
    match_parser = subparsers.add_parser(
        "match",
        help="Match candidates against a job description"
    )
    match_parser.add_argument(
        "--job-file",
        type=str,
        required=True,
        help="Path to a text file containing the job description"
    )
    match_parser.add_argument(
        "--top-k",
        type=int, 
        default=5, 
        help="Number of top matches to return"
    )

    args = parser.parse_args()

    if args.command == "index":
        run_index(args.resume_dir, args.reset)
    elif args.command == "match":
        job_file = Path(args.job_file)
        if not job_file.exists():
            job_file = Path(PROJECT_ROOT/job_file)
            if not job_file.exists():
                raise FileNotFoundError(f"Job description file not found: {job_file}")
        
        job_description = job_file.read_text(encoding="utf-8")
        run_match(job_description, args.top_k)


if __name__ == "__main__":
    main()
