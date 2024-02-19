package com.cs125.group50.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DietInputViewModel : ViewModel() {
    fun showDietInfo(mealType: String, foodName: String, caloriesPerHundredGrams: String, foodAmount: String, onInfoShown: (String) -> Unit) {
        viewModelScope.launch {
            val info = "您选择的是：$mealType, 食物名称：$foodName, 每百克卡路里：$caloriesPerHundredGrams, 食物单位：$foodAmount 百克"
            onInfoShown(info)
        }
    }
}
