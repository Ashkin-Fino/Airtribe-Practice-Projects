"""
    Groq LLM integration module.
    Responsible for:
    - Sending prompts to Groq
    - Receiving structured intent responses
"""
import json
import os
import re
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

        system_prompt =  """
            You are a JSON API and an intent extraction engine.

            Extract:
            - intent
            - folder_path
            - file_path
            - keyword

            IMPORTANT:
            - If user says 'resumes folder',
            folder_path should be 'resumes'
            - If user says 'C:Users/name/resumes folder',
            folder_path should be 'C:Users/name/resumes'
            - If user says 'python experience', 
            keyword should be 'python', and not 
            'python experience'

            Return ONLY valid JSON.

            DO NOT:
            - add explanations
            - add markdown
            - add code fences
            - add extra text

            Supported intents:
            - read_files
            - search_files
            - summarize_file
            - unknown

            Response schema:
            {
                "intent": "string",
                "folder_path": "string or null",
                "file_path": "string or null",
                "keyword": "string or null"
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

        content = response.choices[0].message.content.strip()

        # Remove markdown code fences
        content = re.sub(r"^```json", "", content)
        content = re.sub(r"^```", "", content)
        content = re.sub(r"```$", "", content)

        content = content.strip()

        # Extract first JSON object
        match = re.search(r"\{.*\}", content, re.DOTALL)

        if not match:
            return {
                "intent": "unknown",
                "raw_response": content
            }

        json_string = match.group(0)

        try:
            return json.loads(json_string)

        except json.JSONDecodeError:

            return {
                "intent": "unknown",
                "raw_response": content
            }
