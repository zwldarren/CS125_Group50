import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore


class FirebaseService:
    def __init__(self, service_account_path: str):
        self.service_account_path = service_account_path
        self.db = self.init_firebase()

    def get_db(self):
        return self.db

    def init_firebase(self):
        """
        Initialize Firebase application and get a reference to the Firestore database.
        """
        try:
            app = firebase_admin.get_app()
        except ValueError:
            cred = credentials.Certificate(self.service_account_path)
            app = firebase_admin.initialize_app(cred)
        return firestore.client(app)

    # def get_user_info(self, user_id: str):
    #     doc_ref = self.db.collection('users').document(user_id)
    #     doc = doc_ref.get()
    #     if doc.exists:
    #         return doc.to_dict()
    #     else:
    #         return None

    def get_collections_for_user(self, user_ref):
        """
        Get all sub-collections and their documents for a given user document reference.

        :param user_ref: A reference to a user document.
        :return: A dictionary with collection names as keys and lists of all documents in that collection as values.
        """
        collections_result = {}
        collections = user_ref.collections()
        for collection in collections:
            collection_name = collection.id
            docs = collection.stream()  # 获取子集合中的所有文档
            collections_result[collection_name] = [doc.to_dict() for doc in docs]

        return collections_result

    def get_user_with_collections(self, user_id: str):
        """
        Get information for a specific user and all sub-collections under that user.

        :param user_id: The ID of the user.
        :return: A dictionary containing user information and sub-collection information.
        """
        user_ref = self.db.collection('users').document(user_id)
        user_doc = user_ref.get()
        if not user_doc.exists:
            return None  # 如果用户不存在，则返回None

        user_data = user_doc.to_dict()  # 获取用户文档的字段
        collections_data = self.get_collections_for_user(user_ref)  # 获取用户的所有子集合

        return {'userInfo': user_data, 'collections': collections_data}

    def save_data(self, user_id: str, collection_name: str, data: dict):
        """
        Save data for a specific user into a respective sub-collection based on collection name and data provided.

        :param user_id: The ID of the user.
        :param collection_name: The name of the sub-collection.
        :param data: The data to be saved.
        """
        user_ref = self.db.collection('users').document(user_id)
        collection_ref = user_ref.collection(collection_name)

        # Definitions for query conditions based on collection name
        query_conditions_template = {
            'sleep': [('date', '=='), ('startTime', '==')],
            'meals': [('date', '=='), ('time', '==')],
            'activity': [('activityType', '=='), ('date', '=='), ('startTime', '=='), ('caloriesBurned', '==')],
            'heartRate': []  # New collections should not apply any query condition
        }

        conditions_template = query_conditions_template.get(collection_name, [])

        # For collections without query conditions, directly add data
        if not conditions_template:  # If the conditions template is empty
            collection_ref.add(data)
            print("Data added to unrestricted collection.")
            return 1

        # Dynamically build query conditions, ensuring keys exist in data
        conditions = [(field, op, data[field]) for field, op in conditions_template if field in data]

        # If necessary fields are missing in data, do not proceed with add operation
        if len(conditions) != len(conditions_template):
            return 0

        # Build and execute the query
        query = collection_ref
        for field, op, value in conditions:
            query = query.where(field, op, value)
        query = query.limit(1).get()

        # Check query result, if no document exists then add new data
        if not list(query):
            collection_ref.add(data)
            print("Data added")
            return 1
        else:
            print("Data already exists")
            return 0

    def get_user_info(self, user_id):
        """
        Get specific user's age, gender, goal, height, and weight by user ID.

        :param user_id: The ID of the user.
        :return: A dictionary containing user information, or None if the user does not exist.
        """
        user_ref = self.db.collection('users').document(user_id)
        user_doc = user_ref.get()
        if not user_doc.exists:
            print(f"User {user_id} not found.")
            return None

        user_data = user_doc.to_dict()
        # Update info_keys list to include 'goal'
        info_keys = ['age', 'gender', 'goal', 'height', 'weight']
        user_info = {key: user_data.get(key, None) for key in info_keys}
        return user_info


if __name__ == '__main__':
    firebase_service = FirebaseService("serviceAccountKey.json")
    user_id = "YpM0a1jDTrN2gRK96Worx89Ln0Q2"
    info = firebase_service.get_user_info(user_id)
    print(info)
