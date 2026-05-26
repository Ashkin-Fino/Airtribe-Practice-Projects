"""
Query processor module.
Responsible for:
- Basic query classification
- Routing queries to future tools
"""

from typing import Dict


INTENT_KEYWORDS = {
    "read_files": ["read", "open", "show"],
    "search_files": ["search", "find", "lookup"],
    "summarize_file": ["summarize", "summary"]
}



def classify_query(query: str) -> Dict[str, str]:
    """
    Classifies a user query into a basic intent.

    Args:
        query (str): User input query.

    Returns:
        Dict[str, str]: Intent classification result.
    """

    normalized_query = query.lower()

    for intent, keywords in INTENT_KEYWORDS.items():
        if any(keyword in normalized_query for keyword in keywords):
            return {
                "intent": intent,
                "query": query,
                "status": "classified"
            }

    return {
        "intent": "unknown",
        "query": query,
        "status": "unclassified"
    }
