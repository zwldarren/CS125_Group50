package com.cs125.group50.data

data class RecommendationResponse(
    val overallResponse: String,
    val exerciseResponse: String,
    val sleepResponse: String,
    val dietResponse: String,
    val overallScore: Float,
    val exerciseScore: Float,
    val sleepScore: Float,
    val dietScore: Float
)
