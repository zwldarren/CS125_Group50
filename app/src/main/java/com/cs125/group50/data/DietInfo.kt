package com.cs125.group50.data

import kotlinx.serialization.Serializable

@Serializable
data class DietInfo(
    val mealType: String,
    val foodName: String,
    val totalFat: String,
    val caloriesPerHundredGrams: String,
    val foodAmount: String,
    val date: String,
    val time: String
)
