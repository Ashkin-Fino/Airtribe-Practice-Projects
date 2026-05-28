"""
    Groq LLM integration module.
    Responsible for:
    - Sending prompts to Groq
    - Receiving structured intent responses
"""
import json
import os
from groq import Groq
from dotenv import load_dotenv


load_dotenv()


class LLMClient:

    def __init__(self):
        self.api_key = os.getenv("GROQ_API_KEY")
        self.model_name = os.getenv(
            "MODEL_NAME",
            "llama-3.1-8b-instant"
        )

        if not self.api_key:
            raise ValueError(
                "GROQ_API_KEY is missing in environment variables"
            )

        self.client = Groq(api_key=self.api_key)

    def extract_intent(self, query: str) -> dict:
        """
            Extracts intent and parameters from user query.

            Args:
                query (str): User query.

            Returns:
                dict: Parsed JSON response.
        """

        system_prompt = """
            You are an intent classification engine.

            Return ONLY valid JSON.

            Supported intents:
            - read_files
            - search_files
            - summarize_file
            - unknown

            Response format:
            {
                "intent": "intent_name",
                "folder_path": "optional_folder_path",
                "file_path": "optional_file_path",
                "keyword": "optional_keyword"
            }
        """

        response = self.client.chat.completions.create(
            model=self.model_name,
            temperature=0,
            messages=[
                {
                    "role": "system",
                    "content": system_prompt
                },
                {
                    "role": "user",
                    "content": query
                }
            ]
        )

        content = response.choices[0].message.content

        try:
            return json.loads(content)
        except json.JSONDecodeError:
            return {
                "intent": "unknown",
                "raw_response": content
            }
