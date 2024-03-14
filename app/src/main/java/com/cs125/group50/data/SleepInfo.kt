package com.cs125.group50.data

import kotlinx.serialization.Serializable


@Serializable
data class SleepStage(
    val endTime: String,
    val stage: Int,
    val startTime: String
)

@Serializable
data class SleepInfo(
    val duration: String,
    val startTime: String,
    val endTime: String,
    val date: String,
    val stages: List<SleepStage>?
)