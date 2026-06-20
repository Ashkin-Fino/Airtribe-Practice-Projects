import uuid
from chromadb import PersistentClient
import pathlib

CHROMA_PATH = pathlib.Path(__file__).parent / "data" / "chroma_db"
print("Using path:", CHROMA_PATH.resolve())

client = PersistentClient(path=str(CHROMA_PATH))
collection = client.get_or_create_collection("test_collection")

print("Count before add:", collection.count())

new_id = str(uuid.uuid4())
collection.add(ids=[new_id], documents=["Hello World"])

print("Count after add:", collection.count())
