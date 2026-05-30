"""
    Main entry point for the LLM-powered file assistant.
"""
import json

from modules.exceptions import FileAssistantError
from modules.query_processor import process_query


ui_string = """
=====================================
LLM File Assistant
Powered by Groq
=====================================

Supported Operations:
1. Read Files
2. Search Files
3. Summarize Files
4. Generate Summary Files

Enter your query:
"""

def main():

    print("=" * 60)
    print("LLM Powered File Assistant")
    print("Powered by Groq + Llama3")
    print("Type 'exit' to quit")
    print("=" * 60)

    while True:

        query = input(ui_string).strip()

        if query.lower() in ["exit", "quit"]:
            print("Exiting assistant...")
            break

        if not query:
            print("Please enter a valid query")
            continue

        try:
            result = process_query(query)
            print(f"Result:")
            print(json.dumps(result, indent=4))
            print("✓ Operation completed successfully")
        except FileAssistantError as error:
            print(f"Error: {error}")
            print("✗ Operation failed")
        except Exception as error:
            print(f"Error: {error}")
            print("✗ Operation failed")


if __name__ == "__main__":
    main()
