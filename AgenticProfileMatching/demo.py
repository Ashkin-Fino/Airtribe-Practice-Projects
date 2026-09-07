import time
from pathlib import Path

from agent.agent_tools import AgentTools
from matching_agent import HiringAgent


# ============================================================
# CONFIGURATION
# ============================================================

PROJECT_ROOT = Path(__file__).resolve().parent

RAG_PROJECT_ROOT = PROJECT_ROOT.parent / "RAGBasedProfileMatching"
RESUMES_DIR = RAG_PROJECT_ROOT / "resumes"

RESUME_FILES = [
    RESUMES_DIR / "resume_john_doe.txt",
    RESUMES_DIR / "resume_alice_smith.txt",
]

JOB_DESCRIPTION = """
We are looking for a Software Engineer with strong experience in
Python, Java, Spring Boot, AWS, SQL, REST APIs, Docker and microservices.

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


# ============================================================
# DEMO HELPERS
# ============================================================

def sleep(seconds=2):
    time.sleep(seconds)


def header(title):
    print()
    print("=" * 72)
    print(f"  {title}")
    print("=" * 72)
    sleep(2)


def step(number, title):
    print()
    print(f"[STEP {number}] {title}")
    print("-" * 72)
    sleep(2)


def log(message):
    print(f"[DEMO] {message}")
    sleep(1)


def success(message):
    print(f"[SUCCESS] {message}")
    sleep(2)


# ============================================================
# MAIN DEMO
# ============================================================

def main():

    header("AGENTIC PROFILE MATCHING - END-TO-END DEMO")

    print("This demonstration shows:")
    print()
    print("  MCP Filesystem Server")
    print("        ↓")
    print("  MCP Client")
    print("        ↓")
    print("  AgentTools")
    print("        ↓")
    print("  RAG Candidate Matching")
    print("        ↓")
    print("  LangGraph Hiring Agent")
    print("        ↓")
    print("  Candidate Intelligence")
    print("        ↓")
    print("  Comparison + Interview Plan")
    print("        ↓")
    print("  Final Hiring Report")

    sleep(4)

    # ========================================================
    # INITIALIZE AGENT TOOLS
    # ========================================================

    step(1, "INITIALIZING MCP CLIENT AND AGENT TOOLS")

    log("Creating AgentTools...")
    tools = AgentTools()

    success("AgentTools initialized.")
    log("MCP client, RAG pipeline and matching engine are ready.")

    # ========================================================
    # MCP DISCOVERY
    # ========================================================

    step(2, "MCP SERVER DISCOVERY")

    log("Connecting to the MCP filesystem server...")
    log("Discovering available MCP tools and resources...")

    discovery = tools.discover_filesystem()

    tools_found = discovery.get("tools", [])
    resources_found = discovery.get("resources", [])
    templates_found = discovery.get("resource_templates", [])

    success(
        f"MCP discovery completed: "
        f"{len(tools_found)} tools, "
        f"{len(resources_found)} resources, "
        f"{len(templates_found)} resource templates."
    )

    print()
    print("Available MCP tools:")

    for tool in tools_found:
        if isinstance(tool, dict):
            print(f"  • {tool.get('name', 'unknown')}")
        else:
            print(f"  • {tool}")

    sleep(4)

    print()
    print("Available MCP resources:")

    for resource in resources_found:
        if isinstance(resource, dict):
            print(
                f"  • {resource.get('uri', 'unknown')}"
            )
        else:
            print(f"  • {resource}")

    sleep(4)

    # ========================================================
    # MCP SEARCH
    # ========================================================

    step(3, "SEARCHING RESUMES THROUGH MCP")

    log(
        f"Searching the resume directory for candidates "
        f"containing the keyword 'python'..."
    )

    search_results = tools.search_resume_files(
        str(RESUMES_DIR),
        "python",
    )

    success(
        f"MCP search returned {len(search_results)} matching files."
    )

    print()

    for result in search_results:
        if isinstance(result, dict):
            print(
                f"  • {result.get('name') or result.get('path')}"
            )
        else:
            print(f"  • {result}")

    sleep(4)

    # ========================================================
    # MCP BATCH PROCESSING
    # ========================================================

    step(4, "BATCH PROCESSING RESUMES THROUGH MCP")

    resume_paths = [
        str(path)
        for path in RESUME_FILES
    ]

    log("Sending multiple resume files to the MCP batch operation...")

    batch_results = tools.batch_process(
        resume_paths,
        operation="read",
    )

    success(
        f"MCP batch processing completed for "
        f"{len(batch_results)} files."
    )

    for result in batch_results:

        if isinstance(result, dict):

            path = result.get("path", "unknown")
            status = result.get("status", "completed")

            print(
                f"  • {Path(path).name} → {status}"
            )

        else:
            print(f"  • {result}")

    sleep(4)

    # ========================================================
    # RESUME INDEXING
    # ========================================================

    step(5, "INDEXING RESUMES INTO THE RAG VECTOR STORE")

    log("Preparing the vector database for the demonstration...")

    # Reset the demo collection so the script is repeatable.
    tools.pipeline.vector_store.reset_collection()

    success("Vector store reset.")

    sleep(2)

    for resume_path in RESUME_FILES:

        print()
        log(
            f"Indexing {resume_path.name}..."
        )

        tools.index_resume(
            str(resume_path)
        )

        success(
            f"{resume_path.name} indexed successfully."
        )

    try:
        vector_count = tools.pipeline.vector_store.count()

        success(
            f"Vector store now contains "
            f"{vector_count} indexed chunks."
        )

    except Exception:
        log("Vector store indexing completed.")

    sleep(4)

    # ========================================================
    # DISPLAY JOB DESCRIPTION
    # ========================================================

    step(6, "LOADING JOB DESCRIPTION")

    print(JOB_DESCRIPTION)

    sleep(5)

    # ========================================================
    # START LANGGRAPH AGENT
    # ========================================================

    step(7, "STARTING LANGGRAPH HIRING AGENT")

    log("Creating HiringAgent...")
    hiring_agent = HiringAgent()

    success("LangGraph workflow compiled successfully.")

    print()
    print("LangGraph workflow:")
    print()
    print(
        "  load_job"
        " → extract_requirements"
        " → match_candidates"
    )
    print(
        "  → candidate_intelligence"
        " → compare_candidates"
    )
    print(
        "  → generate_interview_plan"
        " → generate_explanations"
        " → build_report"
    )

    sleep(5)

    # ========================================================
    # RUN COMPLETE AGENT
    # ========================================================

    step(8, "RUNNING COMPLETE AGENT WORKFLOW")

    log("Submitting job description to the Hiring Agent...")
    log("The agent will now execute the complete LangGraph workflow.")

    sleep(3)

    result = hiring_agent.run_agent(
        JOB_DESCRIPTION
    )

    success("LangGraph workflow completed successfully.")

    # ========================================================
    # SHOW AGENT REASONING / STATE PROGRESSION
    # ========================================================

    step(9, "AGENT WORKFLOW EXECUTION TRACE")

    reasoning = result.get(
        "reasoning",
        []
    )

    for index, message in enumerate(
        reasoning,
        start=1,
    ):
        print(
            f"  {index}. {message}"
        )
        sleep(1.5)

    sleep(4)

    # ========================================================
    # SHOW REQUIREMENTS
    # ========================================================

    step(10, "EXTRACTED JOB REQUIREMENTS")

    requirements = result.get(
        "job_requirements"
    )

    if requirements:

        print(
            f"Experience : "
            f"{requirements.experience_years}+ years"
        )

        print(
            f"Education  : "
            f"{requirements.education}"
        )

        print()
        print("Skills:")

        for skill in requirements.skills:
            print(f"  • {skill}")
            sleep(0.5)

    else:
        log("Job requirements were not returned in the final state.")

    sleep(5)

    # ========================================================
    # CANDIDATE RESULTS
    # ========================================================

    step(11, "CANDIDATE MATCHING RESULTS")

    match_result = result.get(
        "match_result"
    )

    if match_result is None:

        print("No candidate results returned.")
        return

    print(
        f"Total candidates evaluated: "
        f"{match_result.total_candidates}"
    )

    sleep(2)

    candidates = match_result.candidates

    for rank, candidate in enumerate(
        candidates,
        start=1,
    ):

        print()
        print(
            f"{rank}. {candidate.candidate_name}"
        )

        print(
            f"   Resume       : "
            f"{candidate.resume_name}"
        )

        print(
            f"   Match Score  : "
            f"{candidate.final_score:.2f}"
        )

        print(
            f"   Category     : "
            f"{candidate.match_category}"
        )

        print(
            f"   Experience   : "
            f"{candidate.experience_years} years"
        )

        print(
            f"   Risk Level   : "
            f"{candidate.risk_level}"
        )

        sleep(2)

    sleep(4)

    # ========================================================
    # CANDIDATE INTELLIGENCE
    # ========================================================

    step(12, "CANDIDATE INTELLIGENCE")

    for candidate in candidates:

        print()
        print(
            f"Candidate: {candidate.candidate_name}"
        )

        print()
        print("  Strengths:")

        if candidate.strengths:

            for strength in candidate.strengths:
                print(f"    ✓ {strength}")
                sleep(0.7)

        else:
            print("    No strengths identified.")

        print()
        print("  Potential weaknesses:")

        if candidate.weaknesses:

            for weakness in candidate.weaknesses:
                print(f"    ! {weakness}")
                sleep(0.7)

        else:
            print("    No major weaknesses identified.")

        print()
        print(
            f"  Risk Level: {candidate.risk_level}"
        )

        sleep(3)

    # ========================================================
    # COMPARISON
    # ========================================================

    step(13, "CANDIDATE COMPARISON")

    comparison = match_result.comparison

    ranking = comparison.get(
        "ranking",
        []
    )

    for candidate in ranking:

        print(
            f"  #{candidate['rank']} "
            f"{candidate['candidate_name']}"
        )

        print(
            f"      Score: "
            f"{candidate['final_score']:.2f}"
        )

        print(
            f"      Risk : "
            f"{candidate['risk_level']}"
        )

        sleep(2)

    # ========================================================
    # FINAL RECOMMENDATION
    # ========================================================

    step(14, "FINAL HIRING RECOMMENDATION")

    recommendation = (
        match_result.final_recommendation
    )

    print()
    print(
        f"  >>> {recommendation}"
    )

    sleep(5)

    # ========================================================
    # INTERVIEW PLAN
    # ========================================================

    step(15, "GENERATED INTERVIEW PLAN")

    interview_plan = (
        match_result.interview_plan
    )

    candidate_plans = interview_plan.get(
        "candidates",
        {}
    )

    for candidate_name, plan in candidate_plans.items():

        print()
        print(
            f"Interview plan for: {candidate_name}"
        )

        print()
        print("  Focus areas:")

        for area in plan.get(
            "focus_areas",
            [],
        ):
            print(f"    • {area}")
            sleep(0.5)

        print()
        print("  Interview questions:")

        for question in plan.get(
            "questions",
            [],
        ):
            print(
                f"    ? {question}"
            )
            sleep(0.8)

        sleep(3)

    # ========================================================
    # FINAL REPORT
    # ========================================================

    step(16, "FINAL HIRING REPORT")

    report = result.get(
        "report"
    )

    if report:

        print()
        print("JOB REQUIREMENTS")
        print("-" * 40)

        report_requirements = report.get(
            "job_requirements",
            {}
        )

        print(
            f"Experience: "
            f"{report_requirements.get('experience_years')}+ years"
        )

        print(
            f"Education: "
            f"{report_requirements.get('education')}"
        )

        print()
        print("CANDIDATES")
        print("-" * 40)

        for candidate in report.get(
            "candidates",
            []
        ):

            print(
                f"{candidate.get('candidate_name')} "
                f"→ "
                f"{candidate.get('final_score'):.2f}"
            )

            sleep(1)

        print()
        print("RECOMMENDATION")
        print("-" * 40)

        print(
            report.get(
                "final_recommendation",
                "No recommendation available.",
            )
        )

    else:

        print("Final report was not generated.")

    sleep(6)

    # ========================================================
    # DEMO COMPLETE
    # ========================================================

    header("DEMO COMPLETED SUCCESSFULLY")

    print()
    print("The complete Agentic Profile Matching workflow has been demonstrated:")
    print()
    print("  ✓ MCP server discovery")
    print("  ✓ MCP resource/tool usage")
    print("  ✓ MCP resume search")
    print("  ✓ MCP batch processing")
    print("  ✓ Resume indexing")
    print("  ✓ Job requirement extraction")
    print("  ✓ RAG candidate matching")
    print("  ✓ Candidate intelligence")
    print("  ✓ Candidate comparison")
    print("  ✓ Interview plan generation")
    print("  ✓ Final hiring recommendation")
    print("  ✓ LangGraph end-to-end orchestration")
    print()
    print("=" * 72)


if __name__ == "__main__":
    main()