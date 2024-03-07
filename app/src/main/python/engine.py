import firebase_admin
import os
from firebase_admin import credentials
from firebase_admin import firestore

class Engine:
    def __init__(self, root_path = ".") -> None:
        path = os.path.join(root_path, "credentials.json")
        self._cred = credentials.Certificate(path)
        self._db = firestore.client()

    def process_query(self, query: str):
        self._query_pre_process(query)

    def get_total_table(self):
        users_ref = self._db.collection("users")
        return users_ref.stream()
    
    def _query_pre_process(query: str):
        pass
