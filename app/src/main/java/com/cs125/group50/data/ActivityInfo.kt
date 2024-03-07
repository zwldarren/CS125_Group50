package com.cs125.group50.data

// 后续会更改

data class ActivityInfo(
    val activityType: String,
    val duration: String,
    val caloriesBurned: String,
    val date: String
)

data class HeartRateInfo(
    val startTime: String,
    val endTime: String,
    val minimumHeartRate: String,
    val maximumHeartRate: String,
    val averageHeartRate: String
)