package com.cs125.group50.data

data class HealthDataInfo(
    val sleepInfos: List<SleepInfo>,
    val dietInfos: List<DietInfo>,
    val activityInfos: List<ActivityInfo>,
    val heartRateInfos: List<HeartRateInfo>
)
