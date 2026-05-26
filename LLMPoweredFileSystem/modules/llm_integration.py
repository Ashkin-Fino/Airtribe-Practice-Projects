"""
LLM integration module.
Phase 1 contains placeholder functions for future API integration.
"""

import os
from typing import Optional


class LLMClient:
    """
    Placeholder LLM client.
    Future implementation will integrate with Groq/OpenAI APIs.
    """

    def __init__(self, api_key: Optional[str] = None):
        self.api_key = api_key or os.getenv("GROQ_API_KEY")

    def is_configured(self) -> bool:
        """
        Checks whether API credentials are available.

        Returns:
            bool: True if configured.
        """

        return bool(self.api_key)

    def send_query(self, query: str) -> str:
        """
        Placeholder query method.

        Args:
            query (str): User query.

        Returns:
            str: Placeholder response.
        """

        return (
            "LLM integration is not implemented yet. "
            "This functionality will be added in Phase 2."
        )
