package com.cs125.group50.data

data class RecommendationResponse(
    val overallResponse: String,
    val exerciseResponse: String,
    val sleepResponse: String,
    val dietResponse: String,
    val overallScore: Int,
    val exerciseScore: Int,
    val sleepScore: Int,
    val dietScore: Int
)
