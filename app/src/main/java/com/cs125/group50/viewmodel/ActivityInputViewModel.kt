package com.cs125.group50.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ActivityInputViewModel : ViewModel() {

    private val db = Firebase.firestore

    fun saveActivityInfo(userId: String, activityType: String, startTime: LocalTime, endTime: LocalTime, selectedDate: LocalDate, caloriesBurned: Int) {
        var startDateTime = LocalDateTime.of(selectedDate, startTime)
        var endDateTime = LocalDateTime.of(selectedDate, endTime)

        // 检查如果 startTime 晚于 endTime，假定 endTime 为第二天
        if (startTime.isAfter(endTime)) {
            endDateTime = endDateTime.plusDays(1)
        }

        val duration = Duration.between(startDateTime, endDateTime).toMinutes()
        val activityInfo = hashMapOf(
            "activityType" to activityType,
            "date" to selectedDate.toString(),
            "startTime" to startTime.toString(),
            "endTime" to endTime.toString(),
            "duration" to duration.toString(),
            "caloriesBurned" to caloriesBurned.toString()
        )

        viewModelScope.launch {
            // Assume you have a 'meals' subcollection inside each 'user' document.
            db.collection("users").document(userId).collection("activity").add(activityInfo)
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
