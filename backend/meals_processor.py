import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from data import HealthData, MealStructure, ActivityStructure, SleepStructure
import json
from firebase_processor import FirebaseService


class MealProcessor:
    def __init__(self, db, user_id):
        self._db = db
        self._user_id = user_id

    def set_db(self, db):
        self._db = db

    def get_latest_meal_time(self):
        meals_ref = self._db.collection('users').document(self._user_id).collection('meals')
        try:
            latest_meal = meals_ref.order_by('date', direction='DESCENDING').limit(1).get()
            for meal in latest_meal:
                return meal.to_dict()["date"]  # Return the latest meal as a dictionary
        except Exception as e:
            print(f"An error occurred: {e}")
            return None

    def get_certain_date_info(self, date):
        meals_ref = self._db.collection('users').document(self._user_id).collection('meals')
        try:
            meals = meals_ref.where('date', '==', date).get()
            meals_info = []
            for meal in meals:
                meals_info.append(meal.to_dict())
            return meals_info
        except Exception as e:
            print(f"An error occurred: {e}")
            return None


# if __name__ == '__main__':
#     # with open("healthData_raw.txt", "r") as file:
#     #     content = file.read()
#     #
#     # data_dict = json.loads(content)
#     # healthData = HealthData(**data_dict)
#     # # user_id = healthData.userId
#     # user_sleep_data = healthData.sleepRecords
#     firebase_service = FirebaseService("serviceAccountKey.json")
#
#     # for key in user_sleep_data:
#     #     # etype = key["activityType"]
#     #     # cal = key["caloriesBurned"]
#     #     # date = key["date"]
#     #     # duration = key["duration"]
#     #     # exercise_data = ExerciseStructure(activity_type=etype, date=date, duration=duration)
#     #     print(key)
#     user_id = "YpM0a1jDTrN2gRK96Worx89Ln0Q2"
#     # if user_info:
#     #     print("User Info:", user_info)
#     # else:
#     #     print("User not found.")
#
#     db = firebase_service.get_db()
#     meals_processor = MealProcessor(db, user_id)
#     day = meals_processor.get_latest_meal_time()
#     print(day)
#     infos = meals_processor.get_certain_date_info(day)
#     print(infos)
#
#     # for record in user_sleep_data:
#     #     date = record["date"]
#     #     duration = record['duration']
#     #     end_time = record['endTime']
#     #     stage = record['stages']
#     #     start_time = record['startTime']
#     #     sleep_structure = SleepStructure(date=date, duration=duration, end_time=end_time, stages=stage, start_time=start_time)
#     #     firebase_service.save_data("YpM0a1jDTrN2gRK96Worx89Ln0Q2", "sleep", sleep_structure.sleep_dict())
#     #
#     # # user_infos = firebase_service.get_user_with_collections("YpM0a1jDTrN2gRK96Worx89Ln0Q2")
#     # # print(user_infos)
