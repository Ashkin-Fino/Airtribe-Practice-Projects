"""
    Main entry point for the LLM-powered file assistant.
"""

from modules.query_processor import process_query



def main():

    print("=" * 60)
    print("LLM Powered File Assistant")
    print("Powered by Groq + Llama3")
    print("Type 'exit' to quit")
    print("=" * 60)

    while True:

        query = input("Enter your query: ").strip()

        if query.lower() in ["exit", "quit"]:
            print("Exiting assistant...")
            break

        if not query:
            print("Please enter a valid query")
            continue

        try:
            result = process_query(query)

            print("Result:")
            print(result)

        except Exception as error:
            print(f"Error: {error}")


if __name__ == "__main__":
    main()
