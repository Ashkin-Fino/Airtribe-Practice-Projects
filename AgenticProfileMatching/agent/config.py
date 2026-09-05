from pathlib import Path
import sys


PROJECT_ROOT = Path(__file__).resolve().parents[1]
WORKSPACE_ROOT = PROJECT_ROOT.parent

MILESTONE1_ROOT = WORKSPACE_ROOT / "LLMPoweredFileSystem"
MILESTONE2_ROOT = WORKSPACE_ROOT / "RAGBasedProfileMatching"

DEFAULT_TOP_K = 5


def configure_imports() -> None:
    """Make sibling milestone projects importable."""
    for root in (WORKSPACE_ROOT, MILESTONE1_ROOT, MILESTONE2_ROOT):
        root_str = str(root)
        if root.exists() and root_str not in sys.path:
            sys.path.insert(0, root_str)
