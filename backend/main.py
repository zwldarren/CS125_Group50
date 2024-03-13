from datetime import timedelta

from fastapi import FastAPI, Path
import data
import joblib
from activity_processor import ActivityProcessor
from meals_processor import MealProcessor
from sleep_processor import SleepProcessor
from diet_score import *
from diet_score import get_diet_score
from firebase_processor import FirebaseService
from exercise_score import *
from exercise_score import get_exercise_score
from sleep_recommendation import generate_sleep_advice
from diet_recommendation import generate_diet_advice
from exercise_recommendation import generate_exercise_advice

from overall_recommendation import calculate_overall_health_score
from overall_recommendation import provide_final_recommendation

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
        calories = record['totalCalories']
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


@app.get("/update/recommendation/{user_id}")
async def update_recommendation(user_id):
    # return {
    #     "overallResponse": "Recommendation updated successfully",
    #     "exerciseResponse": "Recommendation updated successfully",
    #     "sleepResponse": "Recommendation updated successfully",
    #     "dietResponse": "Recommendation updated successfully",
    # }
    try:
        firebase_processor = FirebaseService("serviceAccountKey.json")
        db = firebase_processor.get_db()
        activity_processor = ActivityProcessor(db, user_id)
        meals_processor = MealProcessor(db, user_id)
        sleep_processor = SleepProcessor(db, user_id)

        latest_meal_date = meals_processor.get_latest_meal_time()
        date_info = meals_processor.get_certain_date_info(latest_meal_date)
        extracted_times = [d['time'] for d in date_info]
        extracted_hours_minutes = [time.split(':')[0] + ':' + time.split(':')[1] for time in extracted_times]
        user_info = firebase_processor.get_user_info(user_id)
        gender = user_info['gender'].lower()
        weight = float(user_info['weight'])
        height = float(user_info['height'])
        age = int(user_info['age'])
        goal = user_info['goal']

        day_activity = activity_processor.get_latest_meal_time()
        activity_info = activity_processor.get_activities_last_7_days(day_activity)
        average_calories_burned_per_week = sum(
            float(dic.get('calories', 0)) + float(dic.get('caloriesBurned', 0)) for dic in activity_info)
        calorie_intake = sum(float(dic.get("totalCalories", 0)) for dic in date_info)
        dates_calories = [(dic['date'], float(dic.get('caloriesBurned', 0))) for dic in activity_info]
        # sort by date
        dates_calories.sort(key=lambda x: x[0])
        last_date = dates_calories[-1][0]
        calorie_burn = sum(cal for date, cal in dates_calories if date == last_date)

        ######diet score here
        diet_score = get_diet_score(extracted_hours_minutes, weight, height, age, gender,
                                    average_calories_burned_per_week, calorie_intake, calorie_burn)

        sleep_day = sleep_processor.get_latest_sleep_time()
        sleep_infos = sleep_processor.get_sleep_last_7_days(sleep_day)
        sleep_score = sum(item['sleepScore'] for item in sleep_infos) / len(sleep_infos)
        sleep_score = sleep_score / 10.0

        exercise_score, calorie_difference = get_exercise_score(weight, height, age, gender, average_calories_burned_per_week, calorie_intake, calorie_burn)

        overall_score = calculate_overall_health_score(sleep_score, diet_score, exercise_score, goal)
        overall_recommendation = provide_final_recommendation(overall_score, goal)

        start_times = [datetime.strptime(entry['startTime'], '%H:%M') for entry in sleep_infos]
        adjusted_time_differences = []
        for i in range(len(start_times)):
            for j in range(i + 1, len(start_times)):
                time_difference = calculate_adjusted_time_difference(start_times[i], start_times[j])
                adjusted_time_differences.append(time_difference)

        max_start_time_variance = max(adjusted_time_differences)

        total_sleep_duration = sum(int(entry['duration']) for entry in sleep_infos)
        unique_dates = len(set(entry['date'] for entry in sleep_infos))
        total_sleep_number = len(sleep_infos)
        sleep_times_per_day = total_sleep_number / unique_dates
        average_sleep_per_day = (total_sleep_duration / unique_dates) / 60

        sleep_recommendation = generate_sleep_advice(sleep_score, max_start_time_variance, sleep_times_per_day, average_sleep_per_day)

        diet_recommendation = generate_diet_advice(diet_score, calorie_difference, extracted_hours_minutes)

        weekly_exercise_sessions_number = len(activity_info)
        exercise_recommendation = generate_exercise_advice(exercise_score, weekly_exercise_sessions_number, calorie_difference)

        return {
            "overall_recommendation": overall_recommendation,
            "exercise_recommendation": exercise_recommendation,
            "sleep_recommendation": sleep_recommendation,
            "diet_recommendation": diet_recommendation,
            "overall_score": overall_score,
            "exercise_score": exercise_score,
            "sleep_score": sleep_score,
            "diet_score": diet_score
        }
    except Exception as e:
        print(f"ERROR: {e}")
        return {"message": "Failed to generate recommendation"}

def calculate_adjusted_time_difference(time1, time2):
    difference = time2 - time1
    if difference.days < 0:
        difference = timedelta(days=1) + difference

    difference_in_hours = difference.total_seconds() / 3600

    if difference_in_hours > 12:
        difference_in_hours = 24 - difference_in_hours
    return difference_in_hours