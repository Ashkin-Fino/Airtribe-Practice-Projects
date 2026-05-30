# LLM Powered File System

## Overview
This project is an LLM-powered file assistant capable of understanding natural language queries and executing file-related tasks.

Current implementation includes:
- Project structure setup
- Basic CLI
- Query classification
- Placeholder modules for future development

---

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository_url>
cd LLMPoweredFileSystem
```

### 2. Set Up a Virtual Environment
Create and activate a Python virtual environment to isolate dependencies.

On Windows:
```bash
python -m venv venv
venv\Scripts\activate
```

On macOS/Linux:
```bash
python3 -m venv venv
source venv/bin/activate
```

### 3. Install Dependencies
Install the required Python libraries using pip

```bash
pip install -r requirements.txt
```

### 4. Set Up LLM API Key
This project uses an LLM (e.g., OpenAI API). Set up your API key as an environment variable:

On Windows:
```bash
set OPENAI_API_KEY=<your_api_key>
```

On macOS/Linux:
```bash
export OPENAI_API_KEY=<your_api_key>
```

Alternatively, you can store the API key in a .env file and use a library like python-dotenv to load it.

### 5. Run the Application
Run the main script to start the file assistant:

```bash
python llm_file_assistant.py
```
