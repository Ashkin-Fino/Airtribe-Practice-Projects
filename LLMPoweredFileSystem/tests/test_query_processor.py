from modules.llm_integration import LLMClient



def test_llm_client_initialization():
    client = LLMClient()

    assert client is not None



def test_extract_intent_returns_dict():
    client = LLMClient()

    result = client.extract_intent(
        "Find resumes mentioning Python"
    )

    assert isinstance(result, dict)