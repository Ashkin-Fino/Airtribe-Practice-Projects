import asyncio
import sys

from mcp import ClientSession, StdioServerParameters
from mcp.client.stdio import stdio_client


async def main():
    server_params = StdioServerParameters(
        command=sys.executable,
        args=["filesystem_mcp_server.py"],
    )

    async with stdio_client(server_params) as (read, write):
        async with ClientSession(read, write) as session:

            await session.initialize()

            # 1. list_tools()
            tools = await session.list_tools()

            print("\n=== TOOLS ===")
            for tool in tools.tools:
                print(f"- {tool.name}")

            # 2. list_resources()
            resources = await session.list_resources()

            print("\n=== RESOURCES ===")
            for resource in resources.resources:
                print(f"- {resource.uri}")

            # 3. call_tool()
            result = await session.call_tool(
                "batch_process",
                {
                    "file_paths": ["resumes/resume_alice_smith.txt"],
                    "operation": "metadata",
                },
            )

            print("\n=== BATCH PROCESS ===")
            print(result)


if __name__ == "__main__":
    asyncio.run(main())