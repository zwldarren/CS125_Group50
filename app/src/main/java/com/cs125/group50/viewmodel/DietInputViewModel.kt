package com.cs125.group50.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.util.Log
import java.time.LocalDate
import java.time.LocalTime

class DietInputViewModel : ViewModel() {

    private val db = Firebase.firestore

    fun saveDietInfo(userId: String, mealType: String, foodName: String, caloriesPerHundredGrams: String, foodAmount: String, selectedDate: LocalDate,
                     selectedTime: LocalTime) {
        val dietInfo = hashMapOf(
            "mealType" to mealType,
            "foodName" to foodName,
            "caloriesPerHundredGrams" to caloriesPerHundredGrams,
            "foodAmount" to foodAmount,
            "date" to selectedDate.toString(), // Storing the date as a String
            "time" to selectedTime.toString()  // Storing the time as a String
        )

        viewModelScope.launch {
            // Assume you have a 'meals' subcollection inside each 'user' document.
            db.collection("users").document(userId).collection("meals").add(dietInfo)
                .addOnSuccessListener { documentReference ->
                    Log.d("Firestore", "DocumentSnapshot written with ID: ${documentReference.id}")
                    // 处理成功保存数据后的逻辑，如更新UI或返回上一页面
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error adding document", e)
                    // 处理错误，如显示错误信息
                }
        }
    }
}

