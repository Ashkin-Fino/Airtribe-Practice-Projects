from pathlib import Path
import sys
import streamlit as st


CURRENT_DIR = Path(__file__).resolve().parent
PROJECT_ROOT = CURRENT_DIR.parent
sys.path.insert(0, str(PROJECT_ROOT))


from agent.graph_builder import build_graph

# ==========================================================
# Page Configuration
# ==========================================================
st.set_page_config(
    page_title="Agentic Hiring Assistant",
    page_icon="🤖",
    layout="wide"
)

# ==========================================================
# Session State
# ==========================================================
if "graph" not in st.session_state:
    st.session_state.graph = build_graph()

if "history" not in st.session_state:
    st.session_state.history = []

# ==========================================================
# Header
# ==========================================================
st.title("🤖 Agentic Hiring Assistant")
st.markdown(
    """
        This AI Hiring Assistant can

        - 📄 Understand Job Descriptions
        - 🔍 Match Candidates
        - 🧠 Analyze Skill Gaps
        - ⚖ Compare Candidates
        - 🎯 Generate Interview Plans
        - 📊 Produce Final Hiring Report
    """
)
st.divider()

# ==========================================================
# Sidebar
# ==========================================================
with st.sidebar:
    st.header("About")
    st.write(
        """
        This project demonstrates an Agentic AI workflow
        built using:

        - LangGraph
        - Vector Search
        - Resume RAG
        - Candidate Intelligence
        - Interview Planning
        """
    )

    st.divider()
    st.header("Agent Workflow")
    workflow = [
        "Receive Job Description",
        "Extract Requirements",
        "Match Candidates",
        "Generate Candidate Intelligence",
        "Compare Candidates",
        "Generate Interview Plan",
        "Build Final Report"
    ]

    for step in workflow:
        st.write("⬜", step)

# ==========================================================
# Chat History
# ==========================================================
for message in st.session_state.history:
    with st.chat_message(message["role"]):
        st.markdown(message["content"])

# ==========================================================
# Chat Input
# ==========================================================
prompt = st.chat_input("Paste a Job Description...")

if prompt:
    st.session_state.history.append({
        "role": "user",
        "content": prompt
    })

    with st.chat_message("user"):
        st.markdown(prompt)

    with st.chat_message("assistant"):
        progress_placeholder = st.empty()
        with progress_placeholder.container():
            st.info("Running Hiring Agent...")
            progress_bar = st.progress(0)
            status = st.empty()
            progress_bar.progress(10)
            status.write("Reading Job Description...")

            state = {
                "job_description": prompt,
                "match_result": None,
                "report": None,
                "reasoning": []
            }
            result = st.session_state.graph.invoke(state)
            report = result["report"]
            progress_bar.progress(100)
            status.success("Analysis Complete")

        st.success("Hiring Report Generated")

        # ==================================================
        # Top Matches
        # ==================================================
        st.header("🏆 Top Matching Candidates")
        for candidate in report["top_matches"]:
            with st.expander(candidate["candidate_name"]):
                col1, col2 = st.columns(2)
                with col1:
                    st.metric(
                        "Final Score",
                        f"{candidate['final_score']:.2f}"
                    )

                    st.metric("Risk Level", candidate["risk_level"])

                with col2:
                    st.write("### Summary")
                    st.write(candidate["summary"])

                st.write("---")
                left, right = st.columns(2)

                with left:
                    st.subheader("Strengths")
                    if candidate["strengths"]:
                        for item in candidate["strengths"]:
                            st.success(item)
                    else:
                        st.info("None")
                
                with right:
                    st.subheader("Weaknesses")
                    if candidate["weaknesses"]:
                        for item in candidate["weaknesses"]:
                            st.error(item)
                    else:
                        st.info("None")

        # ==================================================
        # Candidate Comparison
        # ==================================================
        st.header("⚖ Candidate Comparison")
        st.json(report["comparison"])

        # ==================================================
        # Interview Plans
        # ==================================================
        st.header("🎯 Interview Plans")

        for candidate, interview in report["interview_plan"].items():
            with st.expander(candidate):
                st.write(f"### Difficulty: {interview['difficulty']}")
                st.subheader("Technical Questions")
                for q in interview["technical"]:
                    if isinstance(q, dict):
                        st.write(f"- {q['question']}")
                    else:
                        st.write(f"- {q}")

                st.subheader("Behavioral Questions")
                for q in interview["behavioral"]:
                    st.write(f"- {q}")

                st.subheader("Role Specific Questions")
                for q in interview["role_specific"]:
                    st.write(f"- {q}")

        # ==================================================
        # Raw Report
        # ==================================================
        with st.expander("📄 Final Report (JSON)"):
            st.json(report)

        # ==================================================
        # Agent Reasoning
        # ==================================================
        st.header("🧠 Agent Reasoning")
        for i, step in enumerate(result["reasoning"], start=1):
            st.success(f"Step {i}: {step}")
        st.session_state.history.append({
            "role": "assistant",
            "content": "Hiring analysis completed successfully."
        })

# ==========================================================
# Footer
# ==========================================================

st.divider()
st.caption("Agentic Hiring Assistant • LangGraph + Resume RAG + Candidate Intelligence")
