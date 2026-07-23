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

def compare_candidates(state):
    state["match_result"] = tools.compare_candidates(state["match_result"])
    state["reasoning"].append("Compared top candidates.")
    return state

def generate_interview_plan(state):
    state["match_result"] = (tools.generate_interview_plan(state["match_result"]))
    state["reasoning"].append("Generated interview plans.")
    return state

def build_report(state):
    """
    Generate the final hiring report.
    """
    state["report"] = tools.generate_report(state["match_result"])
    state["reasoning"].append("Generated final hiring report.")
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
