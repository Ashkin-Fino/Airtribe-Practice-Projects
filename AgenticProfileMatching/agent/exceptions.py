class AgentToolError(Exception):
    """
    Custom exception for errors related to agent tools.
    """
    def __init__(self, message: str):
        super().__init__(message)

class FileOperationError(AgentToolError):
    """
    Custom exception for errors related to file operations.
    """
    def __init__(self, message: str):
        super().__init__(message)

class CandidateMatchingError(AgentToolError):
    """
    Custom exception for errors related to candidate matching.
    """
    def __init__(self, message: str):
        super().__init__(message)
