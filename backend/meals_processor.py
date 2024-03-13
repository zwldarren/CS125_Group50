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

if __name__ == '__main__':
    firebase_service = FirebaseService("serviceAccountKey.json")
    user_id = "Rcg1ukJq5LP7cEqbyAy6bNqUB3T2"
    db = firebase_service.get_db()
    activity_processor = MealProcessor(db, user_id)
    day = activity_processor.get_latest_meal_time()
    print(day)
    infos = activity_processor.get_certain_date_info(day)
    print(infos)

