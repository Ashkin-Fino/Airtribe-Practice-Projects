from state_models import create_initial_state


def test_initial_state_shape():
    state = create_initial_state()
    assert state["current_query"] == ""
    assert state["search_params"]["top_k"] == 10
    assert state["ranked_candidates"] == []
