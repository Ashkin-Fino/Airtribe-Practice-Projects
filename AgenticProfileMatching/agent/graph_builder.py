from langgraph.graph import StateGraph, END

from agent.agent_state import AgentState
from agent.graph_nodes import (
    load_job,
    extract_requirements,
    match_candidates,
    candidate_intelligence,
    compare_candidates,
    generate_explanations,
    generate_interview_plan,
    build_report,
)


def build_graph():

    workflow = StateGraph(AgentState)

    workflow.add_node("load_job", load_job)
    workflow.add_node("extract_requirements", extract_requirements)
    workflow.add_node("match_candidates", match_candidates)
    workflow.add_node("candidate_intelligence", candidate_intelligence)
    workflow.add_node("compare_candidates", compare_candidates)
    workflow.add_node("generate_interview_plan", generate_interview_plan)
    workflow.add_node("generate_explanations", generate_explanations)
    workflow.add_node("build_report", build_report)

    workflow.set_entry_point("load_job")

    workflow.add_edge(
        "load_job",
        "extract_requirements"
    )

    workflow.add_edge(
        "extract_requirements",
        "match_candidates"
    )

    workflow.add_edge(
        "match_candidates",
        "candidate_intelligence"
    )

    workflow.add_edge(
        "candidate_intelligence",
        "compare_candidates"
    )

    workflow.add_edge(
        "compare_candidates",
        "generate_interview_plan"
    )

    workflow.add_edge(
        "generate_interview_plan",
        "generate_explanations"
    )

    workflow.add_edge(
        "generate_explanations",
        "build_report"
    )

    workflow.add_edge(
        "build_report",
        END
    )

    return workflow.compile()
