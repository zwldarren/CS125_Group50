from datetime import datetime, timedelta
from firebase_processor import FirebaseService


class SleepProcessor:
    def __init__(self, db, user_id):
        self._db = db
        self._user_id = user_id

    def get_latest_sleep_time(self):
        activity_ref = self._db.collection('users').document(self._user_id).collection('sleep')
        try:
            latest_meal = activity_ref.order_by('date', direction='DESCENDING').limit(1).get()
            for meal in latest_meal:
                return meal.to_dict()["date"]  # Return the latest meal as a dictionary
        except Exception as e:
            print(f"An error occurred: {e}")
            return None

    def get_sleep_last_7_days(self, input_date):
        # 将输入的日期字符串转换为 datetime 对象
        end_date = datetime.strptime(input_date, "%Y-%m-%d")
        # 计算开始日期（7天前）
        start_date = end_date - timedelta(days=7)

        sleep_ref = self._db.collection('users').document(self._user_id).collection('sleep')
        try:
            # 查询这个日期范围内的所有活动
            sleeps = sleep_ref.where('date', '>=', start_date.strftime("%Y-%m-%d")) \
                .where('date', '<=', end_date.strftime("%Y-%m-%d")) \
                .order_by('date', direction='ASCENDING').get()
            sleep_info = []
            for sleep in sleeps:
                sleep = sleep.to_dict()
                new_dict = {
                    'date': sleep['date'],
                    'duration': sleep['duration'],
                    'endTime': sleep['endTime'],
                    'startTime': sleep['startTime'],
                    'sleepScore': sleep['sleepScore']
                }
                sleep_info.append(new_dict)
            return sleep_info
        except Exception as e:
            print(f"An error occurred: {e}")
            return None


if __name__ == '__main__':
    firebase_service = FirebaseService("serviceAccountKey.json")
    user_id = "Rcg1ukJq5LP7cEqbyAy6bNqUB3T2"
    db = firebase_service.get_db()
    activity_processor = SleepProcessor(db, user_id)
    day = activity_processor.get_latest_sleep_time()
    print(day)
    infos = activity_processor.get_sleep_last_7_days(day)
    print(infos)
