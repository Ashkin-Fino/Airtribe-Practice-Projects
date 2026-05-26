from modules.query_processor import classify_query



def test_read_query_classification():
    result = classify_query("Read all files in resumes folder")

    assert result["intent"] == "read_files"



def test_search_query_classification():
    result = classify_query("Find resumes mentioning Python")

    assert result["intent"] == "search_files"



def test_summary_query_classification():
    result = classify_query("Summarize the PDF file")

    assert result["intent"] == "summarize_file"



def test_unknown_query_classification():
    result = classify_query("Tell me a joke")

    assert result["intent"] == "unknown"
