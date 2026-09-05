"""
Phase 3 End-to-End Test

Tests the complete Agentic Profile Matching pipeline:

1. MCP filesystem discovery
2. MCP resource reading
3. MCP file search
4. MCP batch processing
5. Resume indexing
6. Job requirement extraction
7. Candidate matching
8. Candidate intelligence
9. Candidate comparison
10. Interview plan generation
11. Explanation generation
12. Final report generation
13. Full LangGraph agent execution

This test intentionally reports failures instead of hiding them.
That makes it useful for validating the current Phase 3 integration.
"""

from pathlib import Path
import traceback

from agent.agent_tools import AgentTools
from matching_agent import HiringAgent


# ---------------------------------------------------------------------
# Configuration
# ---------------------------------------------------------------------

PROJECT_ROOT = Path(__file__).resolve().parent

RAG_PROJECT_ROOT = PROJECT_ROOT.parent / "RAGBasedProfileMatching"
RESUMES_DIR = RAG_PROJECT_ROOT / "resumes"

TEST_RESUMES = [
    RESUMES_DIR / "resume_john_doe.txt",
    RESUMES_DIR / "resume_alice_smith.txt",
]


JOB_DESCRIPTION = """
We are looking for a Software Engineer with strong experience in Python,
Java, Spring Boot, AWS, SQL, REST APIs, Docker and microservices.

Requirements:
- 3+ years of software development experience
- Strong Python or Java programming skills
- Experience with Spring Boot
- Experience with AWS
- Strong SQL knowledge
- Experience building REST APIs and microservices
- Docker knowledge
- Bachelor's degree in Computer Science, Engineering, or related field

The candidate should have good problem-solving skills and experience
working on backend systems.
"""


# ---------------------------------------------------------------------
# Test helpers
# ---------------------------------------------------------------------

passed = 0
failed = 0


def run_test(name, function):
    global passed, failed

    print("\n" + "=" * 70)
    print(f"TEST: {name}")
    print("=" * 70)

    try:
        result = function()

        print("PASS")
        if result is not None:
            print_result(result)

        passed += 1
        return result

    except Exception as exc:
        print("FAIL")
        print(f"{type(exc).__name__}: {exc}")
        traceback.print_exc()

        failed += 1
        return None


def print_result(result):
    """
    Print useful information without dumping huge embedding objects.
    """

    if isinstance(result, dict):
        print("Result:")
        for key, value in result.items():

            if isinstance(value, list) and len(value) > 5:
                print(f"  {key}: [{len(value)} items]")
            else:
                print(f"  {key}: {value}")

    elif isinstance(result, list):
        print(f"Result: list with {len(result)} items")

        for item in result[:5]:
            print(f"  {item}")

    else:
        print(f"Result: {result}")


# ---------------------------------------------------------------------
# MCP tests
# ---------------------------------------------------------------------

def test_mcp_discovery(tools):
    result = tools.discover_filesystem()

    assert result is not None
    assert "tools" in result
    assert "resources" in result
    assert "resource_templates" in result

    expected_tools = {
        "search_files",
        "write_file",
        "summarize_file",
        "generate_summary_file",
        "watch_directory",
        "batch_process",
    }

    discovered_tools = set(result["tools"])

    missing = expected_tools - discovered_tools

    assert not missing, (
        f"Missing MCP tools: {missing}"
    )

    return result


def test_mcp_read(tools):
    resume_path = TEST_RESUMES[0]

    assert resume_path.exists(), (
        f"Test resume does not exist: {resume_path}"
    )

    content = tools.read_file(str(resume_path))

    assert content
    assert len(content.strip()) > 0

    return {
        "file": resume_path.name,
        "characters": len(content),
        "preview": content[:200],
    }


def test_mcp_search(tools):
    result = tools.search_resume_files(
        str(RESUMES_DIR),
        "python",
    )

    assert result is not None
    assert isinstance(result, list)
    assert len(result) > 0

    return result


def test_mcp_batch_process(tools):
    paths = [
        str(path)
        for path in TEST_RESUMES
        if path.exists()
    ]

    assert paths, "No test resumes were found."

    result = tools.batch_process(
        paths,
        operation="metadata",
    )

    assert result is not None
    assert isinstance(result, list)
    assert len(result) == len(paths)

    for item in result:
        assert isinstance(item, dict)

        assert item.get("status") == "success", (
            f"Batch item failed: {item}"
        )

    return result


# ---------------------------------------------------------------------
# Resume indexing
# ---------------------------------------------------------------------

def test_resume_indexing(tools):
    """
    Index the test resumes individually through AgentTools.

    This verifies the MCP -> ResumeRAGPipeline integration.
    """

    results = []

    for resume_path in TEST_RESUMES:

        if not resume_path.exists():
            raise FileNotFoundError(
                f"Resume not found: {resume_path}"
            )

        result = tools.index_resume(str(resume_path))

        assert isinstance(result, dict)

        assert result.get("indexed_resumes") == 1
        assert result.get("indexed_chunks", 0) > 0

        results.append(result)

    return results


# ---------------------------------------------------------------------
# Job requirement extraction
# ---------------------------------------------------------------------

def test_requirement_extraction(tools):
    result = tools.extract_job_requirements(
        JOB_DESCRIPTION
    )

    assert result is not None

    return result


# ---------------------------------------------------------------------
# Candidate matching
# ---------------------------------------------------------------------

def test_candidate_matching(tools):
    result = tools.match_candidates(
        JOB_DESCRIPTION,
        top_k=5,
    )

    assert result is not None

    return result


# ---------------------------------------------------------------------
# Candidate intelligence
# ---------------------------------------------------------------------

def test_candidate_intelligence(tools, match_result):
    assert match_result is not None

    result = tools.enrich_candidates(
        match_result
    )

    assert result is not None

    return result


# ---------------------------------------------------------------------
# Candidate comparison
# ---------------------------------------------------------------------

def test_candidate_comparison(tools, candidates):
    assert candidates is not None

    result = tools.compare_candidates(
        candidates
    )

    assert result is not None

    return result


# ---------------------------------------------------------------------
# Interview plan
# ---------------------------------------------------------------------

def test_interview_plan(tools, candidates):
    assert candidates is not None

    result = tools.generate_interview_plan(
        candidates
    )

    assert result is not None

    return result


# ---------------------------------------------------------------------
# Final report
# ---------------------------------------------------------------------

def test_final_report(tools, candidates):
    assert candidates is not None

    result = tools.generate_report(
        job_description=JOB_DESCRIPTION,
        candidates=candidates,
    )

    assert result is not None

    return result


# ---------------------------------------------------------------------
# Full LangGraph test
# ---------------------------------------------------------------------

def test_full_agent():
    """
    Most important Phase 3 test.

    This executes the actual graph:

        load_job
            ↓
        extract_requirements
            ↓
        match_candidates
            ↓
        candidate_intelligence
            ↓
        compare_candidates
            ↓
        generate_interview_plan
            ↓
        generate_explanations
            ↓
        build_report
    """

    agent = HiringAgent()

    result = agent.run_agent(
        JOB_DESCRIPTION
    )

    assert result is not None
    assert isinstance(result, dict)

    assert "job_description" in result
    assert "match_result" in result
    assert "report" in result
    assert "reasoning" in result

    assert result["job_description"] == JOB_DESCRIPTION

    assert result["match_result"] is not None

    assert result["report"] is not None

    assert result["reasoning"]

    return {
        "state_keys": list(result.keys()),
        "reasoning": result["reasoning"],
        "match_result_type": type(
            result["match_result"]
        ).__name__,
        "report_type": type(
            result["report"]
        ).__name__,
    }


# ---------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------

def main():

    print("\n")
    print("#" * 70)
    print("# PHASE 3 END-TO-END TEST")
    print("# Agentic Profile Matching")
    print("#" * 70)

    print("\nProject root:")
    print(PROJECT_ROOT)

    print("\nResumes directory:")
    print(RESUMES_DIR)

    print("\nTest resumes:")
    for resume in TEST_RESUMES:
        print(f"  - {resume}")

    print("\nJob description:")
    print(JOB_DESCRIPTION.strip())

    # -------------------------------------------------------------
    # Create ONE AgentTools instance.
    #
    # This avoids loading the SentenceTransformer model repeatedly.
    # -------------------------------------------------------------

    print("\nInitializing AgentTools...")

    try:
        tools = AgentTools()
    except Exception as exc:
        print("\nFAILED TO INITIALIZE AgentTools")
        print(f"{type(exc).__name__}: {exc}")
        traceback.print_exc()
        return

    # -------------------------------------------------------------
    # 1. MCP
    # -------------------------------------------------------------

    run_test(
        "1. MCP filesystem discovery",
        lambda: test_mcp_discovery(tools),
    )

    run_test(
        "2. MCP resource file reading",
        lambda: test_mcp_read(tools),
    )

    run_test(
        "3. MCP file search",
        lambda: test_mcp_search(tools),
    )

    run_test(
        "4. MCP batch processing",
        lambda: test_mcp_batch_process(tools),
    )

    # -------------------------------------------------------------
    # 2. Resume indexing
    # -------------------------------------------------------------

    run_test(
        "5. Resume indexing through AgentTools",
        lambda: test_resume_indexing(tools),
    )

    # -------------------------------------------------------------
    # 3. Job requirements
    # -------------------------------------------------------------

    run_test(
        "6. Job requirement extraction",
        lambda: test_requirement_extraction(tools),
    )

    # -------------------------------------------------------------
    # 4. Candidate matching
    # -------------------------------------------------------------

    match_result = run_test(
        "7. Candidate matching",
        lambda: test_candidate_matching(tools),
    )

    # -------------------------------------------------------------
    # 5. Candidate intelligence
    # -------------------------------------------------------------

    enriched_candidates = None

    if match_result is not None:

        enriched_candidates = run_test(
            "8. Candidate intelligence",
            lambda: test_candidate_intelligence(
                tools,
                match_result,
            ),
        )

    else:
        print("\nSKIP: Candidate intelligence")
        print("Reason: candidate matching failed.")

    # -------------------------------------------------------------
    # 6. Candidate comparison
    # -------------------------------------------------------------

    comparison_result = None

    if enriched_candidates is not None:

        comparison_result = run_test(
            "9. Candidate comparison",
            lambda: test_candidate_comparison(
                tools,
                enriched_candidates,
            ),
        )

    else:
        print("\nSKIP: Candidate comparison")
        print("Reason: candidate intelligence failed.")

    # -------------------------------------------------------------
    # 7. Interview plan
    # -------------------------------------------------------------

    interview_result = None

    if enriched_candidates is not None:

        interview_result = run_test(
            "10. Interview plan generation",
            lambda: test_interview_plan(
                tools,
                enriched_candidates,
            ),
        )

    else:
        print("\nSKIP: Interview plan")
        print("Reason: candidate intelligence failed.")

    # -------------------------------------------------------------
    # 8. Final report
    # -------------------------------------------------------------

    report_result = None

    if enriched_candidates is not None:

        report_result = run_test(
            "11. Final report generation",
            lambda: test_final_report(
                tools,
                enriched_candidates,
            ),
        )

    else:
        print("\nSKIP: Final report")
        print("Reason: candidate intelligence failed.")

    # -------------------------------------------------------------
    # 9. Full LangGraph
    # -------------------------------------------------------------

    run_test(
        "12. Full LangGraph agent execution",
        test_full_agent,
    )

    # -------------------------------------------------------------
    # Summary
    # -------------------------------------------------------------

    print("\n")
    print("#" * 70)
    print("# TEST SUMMARY")
    print("#" * 70)

    print(f"\nPassed: {passed}")
    print(f"Failed: {failed}")

    total = passed + failed

    print(f"Total executed: {total}")

    if failed == 0:
        print("\nALL TESTS PASSED")
    else:
        print(
            f"\n{failed} TEST(S) FAILED."
        )

        print(
            "\nThe failures above show exactly which Phase 3 "
            "integration points need to be fixed."
        )


if __name__ == "__main__":
    main()
