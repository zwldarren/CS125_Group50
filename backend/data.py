from pydantic import BaseModel
from typing import List


class HealthData(BaseModel):
    userId: str
    sleepRecords: List
    exerciseRecord: List
    dietRecords: List
    heartRateRecords: List
    totalCaloriesBurnedRecords: List
    activeCaloriesBurnedRecord: List
