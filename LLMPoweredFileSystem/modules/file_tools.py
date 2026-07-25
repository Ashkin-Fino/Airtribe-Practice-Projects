import os
from typing import List, Dict
from PyPDF2 import PdfReader
from pathlib import Path
from docx import Document

from modules.llm_integration import LLMClient


SUPPORTED_EXTENSIONS = [".txt", ".pdf", ".docx"]

BASE_DIR = Path.cwd()

llm_client = LLMClient()


def resolve_path(path: str) -> str:
    """
        Resolves relative paths to absolute paths.
    """

    input_path = Path(path)

    if input_path.is_absolute():
        return str(input_path)

    resolved_path = BASE_DIR / input_path

    return str(resolved_path.resolve())


def list_supported_files(folder_path: str, ext: str = None) -> List[str]:
    """
    Returns all supported files from folder.
    """

    folder_path = resolve_path(folder_path)

    if not os.path.exists(folder_path):
        raise FileNotFoundError(
            f"Folder does not exist: {folder_path}"
        )

    files = []

    for file_name in os.listdir(folder_path):

        full_path = os.path.join(folder_path, file_name)

        if os.path.isfile(full_path):

            extension = os.path.splitext(file_name)[1].lower()

            if extension in SUPPORTED_EXTENSIONS:
                if ext and extension != ext:
                    continue
                files.append(full_path)

    return files


## File content extraction methods
def extract_text_from_txt(file_path: str) -> str:
    """
        Extracts text from txt file.
    """

    with open(file_path, "r", encoding="utf-8") as file:
        return file.read()


def extract_text_from_pdf(file_path: str) -> str:
    """
        Extracts text from PDF file.
    """

    reader = PdfReader(file_path)

    extracted_text = []

    for page in reader.pages:

        text = page.extract_text()

        if text:
            extracted_text.append(text)

    return "\n".join(extracted_text)


def extract_text_from_docx(file_path: str) -> str:
    """
        Extracts text from DOCX file.
    """

    document = Document(file_path)

    paragraphs = []

    for paragraph in document.paragraphs:

        text = paragraph.text.strip()

        if text:
            paragraphs.append(text)

    return "\n".join(paragraphs)


def extract_text(file_path: str) -> str:
    """
        Extracts text based on file extension.
    """

    extension = os.path.splitext(file_path)[1].lower()

    if extension == ".txt":
        return extract_text_from_txt(file_path)

    elif extension == ".pdf":
        return extract_text_from_pdf(file_path)
    
    elif extension == ".docx":
        return extract_text_from_docx(file_path)

    else:
        raise ValueError(f"Unsupported file type: {extension}")


def read_files_in_folder(folder_path: str) -> Dict[str, str]:
    """
        Reads all supported files in folder.
    """

    files = list_supported_files(folder_path)

    results = {}

    for file_path in files:
        try:
            content = extract_text(file_path)
            results[file_path] = content
        except Exception as error:
            results[file_path] = (f"Error reading file: {error}")

    return results


def search_files_for_keyword(folder_path: str, keyword: str) -> List[str]:
    """
        Searches files containing keyword.
    """
    files = list_supported_files(folder_path)
    matching_files = []
    keyword = keyword.lower()
    for file_path in files:
        try:
            content = extract_text(file_path)
            if keyword in content.lower():
                matching_files.append(file_path)
        except Exception:
            continue

    return matching_files


def summarize_file(file_path: str) -> str:
    """
        Summarizes file content using Groq LLM.
    """
    file_path = resolve_path(file_path)

    if not os.path.exists(file_path):
        raise FileNotFoundError(f"File does not exist: {file_path}")

    content = extract_text(file_path)

    if not content.strip():
        return "File is empty"

    truncated_content = content[:12000]

    prompt = f'''
        Summarize the following document.

        Document:
        {truncated_content}
    '''

    response = llm_client.client.chat.completions.create(
        model=llm_client.model_name,
        messages=[
            {
                "role": "system",
                "content": "You are a document summarizer."
            },
            {
                "role": "user",
                "content": prompt
            }
        ],
        temperature=0.2
    )

    return response.choices[0].message.content


def write_file(file_path: str, content: str) -> str:
    """
        Writes content to a file.

        Args:
            file_path (str): Output file path.
            content (str): Content to write.

        Returns:
            str: Success message.
    """

    file_path = resolve_path(file_path)

    parent_directory = os.path.dirname(file_path)

    if parent_directory:
        os.makedirs(parent_directory, exist_ok=True)

    with open(file_path, "w", encoding="utf-8") as file:
        file.write(content)

    return f"Content written successfully to: {file_path}"


def write_content_to_file(
    source_file_path: str,
    output_file_path: str
) -> str:
    """
        Generates summary from source file
        and writes it to output file.

        Args:
            source_file_path (str): Input file path.

        Returns:
            str: Success message.
    """

    source_path = Path(source_file_path)
    file_name = source_path.stem
    if (output_file_path is not None):
        output_file_path = f"summaries/{file_name}_summary.txt"

    summary = summarize_file(source_file_path)

    return write_file(
        output_file_path,
        summary
    )
