package com.cs125.group50.data

import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord

data class HealthData(
    val userId: String,
    val sleepRecords: List<SleepSessionRecord>,
    val exerciseRecord: List<ExerciseSessionRecord>,
    val dietRecords: List<NutritionRecord>,
    val heartRateRecords: List<HeartRateRecord>,
    val totalCaloriesBurnedRecords: List<TotalCaloriesBurnedRecord>,
    val activeCaloriesBurnedRecord: List<ActiveCaloriesBurnedRecord>
)
