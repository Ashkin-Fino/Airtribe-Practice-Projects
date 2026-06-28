from langgraph.graph import StateGraph, END

from .agent_state import AgentState
from .graph_nodes import (
    load_job,
    extract_requirements,
    match_candidates,
    candidate_intelligence,
    compare_candidates,
    generate_explanations,
)


def build_graph():

    workflow = StateGraph(AgentState)

    workflow.add_node("load_job", load_job)

    workflow.add_node(
        "extract_requirements",
        extract_requirements
    )

    workflow.add_node(
        "match_candidates",
        match_candidates
    )

    workflow.add_node(
        "candidate_intelligence",
        candidate_intelligence
    )

    workflow.add_node(
        "compare_candidates",
        compare_candidates
    )

    workflow.add_node(
        "generate_explanations",
        generate_explanations
    )

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
        "generate_explanations"
    )

    workflow.add_edge(
        "generate_explanations",
        END
    )

    return workflow.compile()
