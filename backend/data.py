from typing import List
from pydantic import BaseModel
from datetime import datetime

from sleep_score import calculate_sleep_score

class HealthData(BaseModel):
    userId: str
    sleepRecords: List
    exerciseRecord: List
    dietRecords: List
    heartRateRecords: List


class SleepStructure:
    def __init__(self, date=None, duration=None, start_time=None, end_time=None, stages=None, sleep_model = None):
        self._date = date
        self._duration = duration
        self._start_time = start_time
        self._end_time = end_time
        self._stages = stages
        self._stage_percentages = self._preprocess_stage(stages)
        

    def _preprocess_stage(self, sleep_data) -> dict | None:
        if not sleep_data:
            return None
        for entry in sleep_data:
            entry['duration'] = (datetime.fromisoformat(entry['endTime']) - datetime.fromisoformat(
                entry['startTime'])).total_seconds()

        total_sleep_time = sum(entry['duration'] for entry in sleep_data)

        stage_durations = {}
        for entry in sleep_data:
            stage_key = str(entry['stage'])  # 转换键为字符串
            if stage_key not in stage_durations:
                stage_durations[stage_key] = entry['duration']
            else:
                stage_durations[stage_key] += entry['duration']

        stage_percentages = {stage: (duration / total_sleep_time) for stage, duration in stage_durations.items()}
        return stage_percentages

    def sleep_dict(self):
        db_dict = {
            "date": self._date,
            "duration": self._duration,
            "endTime": self._end_time,
            "stages": self._stages,
            "startTime": self._start_time,
            "stagePercentages": self._stage_percentages
        }
        return db_dict


class MealStructure:
    def __init__(self, calories=None, date=None, food_amount=None, food_name=None, meal_type=None, time=None):
        self._calories = calories
        self._date = date
        self._food_amount = food_amount
        self._food_name = food_name
        self._meal_type = meal_type
        self._time = time

    def meal_dict(self):
        db_dict = {
            "totalCalories": self._calories,
            "date": self._date,
            "foodAmount": self._food_amount,
            "foodName": self._food_name,
            "mealType": self._meal_type,
            "time": self._time
        }

        return db_dict


class ActivityStructure:
    def __init__(self, activity_type=None, date=None, duration=None, end_time=None, start_time=None, calories=None):
        self._activity_type = activity_type
        self._date = date
        self._duration = duration
        self._end_time = end_time
        self._start_time = start_time
        self._calories = calories

    def activity_dict(self):
        db_dict = {
            "activityType": self._activity_type,
            "date": self._date,
            "duration": self._duration,
            "endTime": self._end_time,
            "startTime": self._start_time,
            "calories": self._calories
        }
        return db_dict


class HeartRateStructure:
    def __init__(self, avg_heart_rate=None, end_time=None, maximum_heart_rate=None, minimum_heart_rate=None,
                 start_time=None):
        self._avg_heart_rate = avg_heart_rate
        self._end_time = end_time
        self._maximum_heart_rate = maximum_heart_rate
        self._minimum_heart_rate = minimum_heart_rate
        self._start_time = start_time

    def heart_rate_dict(self):
        db_dict = {
            "averageHeartRate": self._avg_heart_rate,
            "endTime": self._end_time,
            "maximumHeartRate": self._maximum_heart_rate,
            "minimumHeartRate": self._minimum_heart_rate,
            "startTime": self._start_time
        }
        return db_dict
