"""
    Query processor module.
    Responsible for:
    - LLM-powered query classification
    - Tool routing
"""

from modules.llm_integration import LLMClient
from modules.file_tools import (
    read_files_in_folder,
    search_files_for_keyword,
    summarize_file,
    write_content_to_file
)


llm_client = LLMClient()



def process_query(query: str):
    """
        Processes user query using the LLM.

        Args:
            query (str): User query.

        Returns:
            Any: Tool execution result.
    """

    result = llm_client.extract_intent(query)
    print(result)

    intent = result.get("intent")

    if intent == "read_files":
        folder_path = result.get("folder_path")
        if not folder_path:
            return "Folder path missing in query"
        return read_files_in_folder(folder_path)
    elif intent == "search_files":
        folder_path = result.get("folder_path")
        keyword = result.get("keyword")
        if not folder_path or not keyword:
            return "Folder path or keyword missing in query"
        try:
            return search_files_for_keyword(folder_path, keyword)
        except FileNotFoundError as error:
            return str(error)
        except Exception as error:
            return f"Unexpected error: {error}"
    elif intent == "summarize_file":
        file_path = result.get("file_path")
        if not file_path:
            return "File path missing in query"
        return summarize_file(file_path)
    elif intent == "generate_summary_file":
        file_path = result.get("file_path")
        output_file_path = result.get(
            "output_file_path"
        )
        if not file_path:
            return "File path missing in query"
        return write_content_to_file(
            file_path,
            output_file_path
        )

    return {
        "status": "unsupported_query",
        "message": "Could not understand the query"
    }
