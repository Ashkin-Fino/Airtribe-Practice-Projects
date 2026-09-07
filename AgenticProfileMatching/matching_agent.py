from agent.graph_builder import build_graph


class HiringAgent:

    def __init__(self):
        self.graph = build_graph()

    def run_agent(self, job_description: str) -> dict:

        state = {
            "job_description": job_description,
            "job_requirements": None,
            "match_result": None,
            "report": None,
            "reasoning": [],
        }

        return self.graph.invoke(state)


if __name__ == "__main__":

    job_description = input(
        "Enter job description:\n"
    )

    result = HiringAgent().run_agent(
        job_description
    )

    print(result["report"])
    