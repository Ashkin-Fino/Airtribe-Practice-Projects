from matching_agent import HiringAgent


def test_hiring_agent_smoke():
    agent = HiringAgent()
    report = agent.run_agent("Find me Python backend engineers with AWS")
    assert report["status"] == "phase1_placeholder"
    assert "requirements" in report
