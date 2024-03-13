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
        初始化Firebase应用并获取Firestore数据库的引用。
        """
        try:
            app = firebase_admin.get_app()
        except ValueError:
            cred = credentials.Certificate(self.service_account_path)
            app = firebase_admin.initialize_app(cred)
        return firestore.client(app)

    def get_user_info(self, user_id: str):
        doc_ref = self.db.collection('users').document(user_id)
        doc = doc_ref.get()
        if doc.exists:
            return doc.to_dict()
        else:
            return None

    def get_collections_for_user(self, user_ref):
        """
        获取指定用户文档下的所有子集合及其文档。

        :param user_ref: 指向用户文档的引用。
        :return: 一个字典，键为集合名称，值为该集合中所有文档的列表。
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
        获取特定用户的信息以及该用户下的所有子集合。

        :param user_id: 用户的ID。
        :return: 一个包含用户信息和子集合信息的字典。
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
        根据不同的集合名称和data中的可用数据，保存特定用户的数据到相应的子集合中。

        :param user_id: 用户的ID。
        :param collection_name: 子集合的名称。
        :param data: 要保存的数据。
        """
        user_ref = self.db.collection('users').document(user_id)
        collection_ref = user_ref.collection(collection_name)

        # 更新定义，包括一个新的集合，它不需要任何查询条件
        query_conditions_template = {
            'sleep': [('date', '=='), ('startTime', '==')],
            'meals': [('date', '=='), ('time', '==')],
            'activity': [('activityType', '=='), ('date', '=='), ('startTime', '=='), ('caloriesBurned', '==')],
            'heartRate': []  # 新集合不应用任何查询条件
        }

        conditions_template = query_conditions_template.get(collection_name, [])

        # 对于不需要查询条件的集合，直接添加数据
        if not conditions_template:  # 如果条件模板为空
            collection_ref.add(data)
            print("Data added to unrestricted collection.")
            return 1

        # 动态构建查询条件，确保data中存在相应的键
        conditions = [(field, op, data[field]) for field, op in conditions_template if field in data]

        # 如果data中缺少必要的字段，则不进行添加操作
        if len(conditions) != len(conditions_template):
            return 0

        # 构建并执行查询
        query = collection_ref
        for field, op, value in conditions:
            query = query.where(field, op, value)
        query = query.limit(1).get()

        # 检查查询结果，若不存在则添加新数据
        if not list(query):
            collection_ref.add(data)
            print("Data added")
            return 1
        else:
            print("Data already exists")
            return 0

