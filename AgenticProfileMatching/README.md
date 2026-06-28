# Milestone 3 — LangGraph Hiring Agent (Phase 1 Scaffold)

This folder contains the **Phase 1 implementation scaffold** for Milestone 3.

## What is included in Phase 1
- project structure for Milestone 3
- `state_models.py` with the canonical agent state
- `matching_agent.py` with a placeholder `HiringAgent`
- `agent_tools.py` with Phase 1 tool wrappers / integration boundary
- `reporting.py` with placeholder reporting helpers
- `ui_cli.py` with a minimal CLI loop
- `tests/` starter tests

## What is intentionally NOT implemented yet
These belong to later phases:
- real LangGraph node/edge workflow
- candidate retrieval from Milestone 2
- ranking and reasoning
- file summarization / resume search via Milestone 1
- candidate comparison / interview question generation / multi-round screening

## Run the CLI
```bash
python ui_cli.py
```

## Next implementation target
Phase 2: wire Milestone 1 + Milestone 2 into `agent_tools.py`.
