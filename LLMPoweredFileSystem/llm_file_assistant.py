"""
Entry point for the LLM-powered file assistant.
Phase 1 implementation provides:
- Basic CLI
- Query input handling
- Placeholder query processing flow
"""

from modules.query_processor import classify_query


def main():
    print("=" * 50)
    print("LLM Powered File Assistant")
    print("Type 'exit' to quit")
    print("=" * 50)

    while True:
        user_query = input("\nEnter your query: ").strip()

        if user_query.lower() in ["exit", "quit"]:
            print("Exiting assistant...")
            break

        if not user_query:
            print("Please enter a valid query.")
            continue

        result = classify_query(user_query)

        print("\nProcessed Query:")
        print(result)


if __name__ == "__main__":
    main()