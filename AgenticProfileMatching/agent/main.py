from agent.graph_builder import build_graph

graph = build_graph()
state = {
    "job_description": open("job_description.txt").read(),
    "match_result": None,
    "report": None,
    "reasoning": [],
}
result = graph.invoke(state)
report = result["report"]
print(report)
