from agent.agent_tools import AgentTools


tools = AgentTools()


def load_job(state):
    state["reasoning"].append(
        "Loaded job description."
    )

    return state


def extract_requirements(state):

    requirements = tools.extract_job_requirements(
        state["job_description"]
    )

    state["job_requirements"] = requirements

    state["reasoning"].append(
        "Extracted structured job requirements."
    )

    return state


def match_candidates(state):

    match_result = tools.match_candidates(
        job_description=state["job_description"],
        job_requirements=state.get("job_requirements"),
    )

    state["match_result"] = match_result

    state["reasoning"].append(
        f"Retrieved {len(match_result.candidates)} candidates."
    )

    return state


def candidate_intelligence(state):

    state["match_result"] = tools.enrich_candidates(
        state["match_result"]
    )

    state["reasoning"].append(
        "Generated candidate intelligence."
    )

    return state


def compare_candidates(state):

    state["match_result"] = tools.compare_candidates(
        state["match_result"]
    )

    state["reasoning"].append(
        "Compared top candidates."
    )

    return state


def generate_interview_plan(state):

    state["match_result"] = tools.generate_interview_plan(
        state["match_result"]
    )

    state["reasoning"].append(
        "Generated interview plans."
    )

    return state


def generate_explanations(state):

    state["reasoning"].append(
        "Candidate explanations ready."
    )

    return state


def build_report(state):

    state["report"] = tools.generate_report(
        job_description=state["job_description"],
        candidates=state["match_result"],
    )

    state["reasoning"].append(
        "Generated final hiring report."
    )

    return state
