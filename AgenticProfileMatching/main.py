from .graph_builder import build_graph

graph = build_graph()

state = {
    "job_description": open("job.txt").read(),
    "job_requirements": None,
    "candidates": [],
    "match_result": None,
    "reasoning": [],
}

result = graph.invoke(state)

print(result["match_result"])