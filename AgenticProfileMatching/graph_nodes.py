from agent_tools import AgentTools


tools = AgentTools()


def load_job(state):
    return state

def extract_requirements(state):
    requirements = tools.extract_job_requirements(state["job_description"])
    state["job_requirements"] = requirements
    state["reasoning"].append("Extracted structured job requirements.")
    return state

def match_candidates(state):
    candidates = tools.match_candidates(state["job_description"])
    state["candidates"] = candidates
    state["reasoning"].append(f"Retrieved {len(candidates)} candidates.")
    return state

def candidate_intelligence(state):
    """
    Enrich every candidate with structured insights.
    """
    state["match_result"] = tools.enrich_candidates(state["match_result"])
    state["reasoning"].append("Generated candidate intelligence.")
    return state

def generate_explanations(state):
    """
    Final graph node.

    Currently explanations are generated during the
    candidate intelligence stage.

    This node exists as an extension point for future
    LLM-generated reasoning.
    """
    state["reasoning"].append("Candidate explanations ready.")
    return state
