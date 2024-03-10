from pydantic import BaseModel
from typing import List


class HealthData(BaseModel):
    sleepRecords: List
    exerciseRecord: List
    dietRecords: List
    heartRateRecords: List
    totalCaloriesBurnedRecords: List
    activeCaloriesBurnedRecord: List
