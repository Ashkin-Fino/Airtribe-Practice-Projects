"""
Central configuration for Milestone 3.

This module is responsible for:
- locating sibling milestone projects
- configuring import paths
- defining common project directories

Expected directory structure:

Projects/
│
├── milestone1/
├── milestone2/
└── agentic-profile-matching/
"""

from pathlib import Path
import sys


# ---------------------------------------------------------------------
# Project Paths
# ---------------------------------------------------------------------

PROJECT_ROOT = Path(__file__).resolve().parent

WORKSPACE_ROOT = PROJECT_ROOT.parent

MILESTONE1_ROOT = WORKSPACE_ROOT / "LLMPoweredFileSystem"
MILESTONE2_ROOT = WORKSPACE_ROOT / "RAGBasedProfileMatching"


# ---------------------------------------------------------------------
# Milestone 3 directories
# ---------------------------------------------------------------------

ARTIFACTS_DIR = PROJECT_ROOT / "artifacts"

REPORTS_DIR = ARTIFACTS_DIR / "reports"
COMPARISONS_DIR = ARTIFACTS_DIR / "comparisons"
INTERVIEW_DIR = ARTIFACTS_DIR / "interview_questions"
FINAL_RECOMMENDATIONS_DIR = ARTIFACTS_DIR / "final_recommendations"

for directory in [
    REPORTS_DIR,
    COMPARISONS_DIR,
    INTERVIEW_DIR,
    FINAL_RECOMMENDATIONS_DIR,
]:
    directory.mkdir(parents=True, exist_ok=True)


# ---------------------------------------------------------------------
# Import Configuration
# ---------------------------------------------------------------------

def configure_imports():
    """
    Makes Milestone 1 and Milestone 2 importable.

    This function should be called once when the application starts.
    """

    for path in [MILESTONE1_ROOT, MILESTONE2_ROOT]:
        path_str = str(path.resolve())

        if path_str not in sys.path:
            sys.path.insert(0, path_str)


configure_imports()


# ---------------------------------------------------------------------
# Defaults
# ---------------------------------------------------------------------

DEFAULT_TOP_K = 5

DEFAULT_SCREENING_ROUND = 1

STRICT_FILTERING = False
