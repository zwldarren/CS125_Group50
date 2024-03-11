package com.cs125.group50.data

import kotlinx.serialization.Serializable

// 后续会更改

@Serializable
data class ActivityInfo(
    val activityType: String,
    val duration: String,
    val caloriesBurned: String,
    val date: String
)

@Serializable
data class HeartRateInfo(
    val startTime: String,
    val endTime: String,
    val minimumHeartRate: String,
    val maximumHeartRate: String,
    val averageHeartRate: String
)