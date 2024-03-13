from fastapi import FastAPI
import data
from firebase_processor import FirebaseService
import joblib

from sleep_score import calculate_sleep_score

app = FastAPI()

sleep_model = joblib.load("train/model.joblib")

# Run the server with the following command
# uvicorn main:app --reload

@app.post("/healthData/synchronize")
async def synchronize_health_data(healthData: data.HealthData):
    # print(healthData)
    # healthData_json = healthData.model_dump_json()
    # with open("healthData_raw.txt", "w") as file:
    #     file.write(healthData_json)
    try:
        firebase_processor = FirebaseService("serviceAccountKey.json")
        user_id = healthData.userId
        print(user_id)
        sleep_records = healthData.sleepRecords
        exercise_records = healthData.exerciseRecord
        diet_records = healthData.dietRecords
        heart_rate_records = healthData.heartRateRecords

        process_sleep_records(user_id, firebase_processor, sleep_records)
        process_exercise_records(user_id, firebase_processor, exercise_records)
        process_diet_records(user_id, firebase_processor, diet_records)
        process_heart_rate_records(user_id, firebase_processor, heart_rate_records)
        return {"message": "success"}
    except Exception as e:
        print(f"ERROR: {e}")
        return {"message": "fail"}


def process_sleep_records(user_id, firebase_processor, records):
    for record in records:
        date = record["date"]
        duration = record['duration']
        end_time = record['endTime']
        stage = record['stages']
        start_time = record['startTime']
        
        sleep_structure = data.SleepStructure(date=date, duration=duration, end_time=end_time, stages=stage,
                                              start_time=start_time)
        sleep_structure_dict = sleep_structure.sleep_dict()
        sleep_score = calculate_sleep_score(sleep_structure_dict, sleep_model)
        sleep_structure_dict["sleepScore"] = sleep_score
        firebase_processor.save_data(user_id, "sleep", sleep_structure_dict)


def process_exercise_records(user_id, firebase_processor, records):
    for record in records:
        etype = record['activityType']
        calories = record['caloriesBurned']
        date = record['date']
        duration = record['duration']
        activity_structure = data.ActivityStructure(activity_type=etype, date=date, duration=duration,
                                                    calories=calories)
        firebase_processor.save_data(user_id, "activity", activity_structure.activity_dict())


def process_diet_records(user_id, firebase_processor, records):
    for record in records:
        calories = record['caloriesPerHundredGrams']
        date = record['date']
        amount = record['foodAmount']
        name = record['foodName']
        ftype = record['mealType']
        time = record['time']
        diet_structure = data.MealStructure(calories=calories, date=date, food_amount=amount, food_name=name,
                                            meal_type=ftype, time=time)
        firebase_processor.save_data(user_id, "meals", diet_structure.meal_dict())

def process_heart_rate_records(user_id, firebase_processor, records):
    for record in records:
        avg_heart_rate = record["averageHeartRate"]
        end_time = record['endTime']
        max_heart_rate = record['maximumHeartRate']
        min_heart_rate = record['minimumHeartRate']
        start_time = record['startTime']
        heart_rate_structure = data.HeartRateStructure(avg_heart_rate=avg_heart_rate, end_time=end_time,
                                                       maximum_heart_rate=max_heart_rate,
                                                       minimum_heart_rate=min_heart_rate, start_time=start_time)
        firebase_processor.save_data(user_id, "heartRate", heart_rate_structure.heart_rate_dict())
