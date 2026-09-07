from agent.agent_tools import AgentTools


def test_agent_tools_uses_mcp_filesystem(monkeypatch):
    class FakeMCPFileSystem:
        def read_file(self, path):
            return f"content:{path}"

        def search_files(self, directory, keyword):
            return [{"path": f"{directory}/resume.txt", "keyword": keyword}]

        def batch_process(self, file_paths, operation="read"):
            return [{"path": path, "operation": operation} for path in file_paths]

        def watch_directory(
            self,
            directory,
            duration_seconds=30,
            poll_interval_seconds=2,
        ):
            return [{"path": f"{directory}/new_resume.txt"}]

    tools = object.__new__(AgentTools)
    tools.filesystem = FakeMCPFileSystem()

    assert tools.read_file("resumes/a.txt") == "content:resumes/a.txt"
    assert tools.search_resume_files("resumes", "python")
    assert tools.batch_process(["resumes/a.txt"])
    assert tools.watch_directory("resumes")
