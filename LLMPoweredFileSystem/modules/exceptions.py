# modules/exceptions.py
class FileAssistantError(Exception):
    pass


class InvalidQueryError(FileAssistantError):
    pass


class UnsupportedIntentError(FileAssistantError):
    pass