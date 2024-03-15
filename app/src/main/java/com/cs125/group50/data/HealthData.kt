package com.cs125.group50.data

import kotlinx.serialization.Serializable

@Serializable
data class HealthData(
    val userId: String,
    val sleepRecords: List<SleepInfo>,
    val exerciseRecord: List<ActivityInfo>,
    val dietRecords: List<DietInfo>,
    val heartRateRecords: List<HeartRateInfo>,
    val location: String
)
