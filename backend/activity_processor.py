from datetime import datetime, timedelta

from firebase_processor import FirebaseService

class ActivityProcessor:
    def __init__(self, db, user_id):
        self._db = db
        self._user_id = user_id

    def set_db(self, db):
        self._db = db

    def get_latest_meal_time(self):
        meals_ref = self._db.collection('users').document(self._user_id).collection('activity')
        try:
            latest_meal = meals_ref.order_by('date', direction='DESCENDING').limit(1).get()
            for meal in latest_meal:
                return meal.to_dict()["date"]  # Return the latest meal as a dictionary
        except Exception as e:
            print(f"An error occurred: {e}")
            return None

    def get_activities_last_7_days(self, input_date):
        # 将输入的日期字符串转换为 datetime 对象
        end_date = datetime.strptime(input_date, "%Y-%m-%d")
        # 计算开始日期（7天前）
        start_date = end_date - timedelta(days=7)

        activities_ref = self._db.collection('users').document(self._user_id).collection('activity')
        try:
            # 查询这个日期范围内的所有活动
            activities = activities_ref.where('date', '>=', start_date.strftime("%Y-%m-%d")) \
                .where('date', '<=', end_date.strftime("%Y-%m-%d")) \
                .order_by('date', direction='ASCENDING').get()
            activities_info = []
            for activity in activities:
                activities_info.append(activity.to_dict())
            return activities_info
        except Exception as e:
            print(f"An error occurred: {e}")
            return None

if __name__ == '__main__':
    firebase_service = FirebaseService("serviceAccountKey.json")
    user_id = "YpM0a1jDTrN2gRK96Worx89Ln0Q2"
    db = firebase_service.get_db()
    activity_processor = ActivityProcessor(db, user_id)
    day = activity_processor.get_latest_meal_time()
    print(day)
    infos = activity_processor.get_activities_last_7_days(day)
    print(infos)
