package com.cs125.group50.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class SleepInputViewModel : ViewModel() {


    private val db = Firebase.firestore

    fun saveActivityInfo(userId: String, startTime: LocalTime, endTime: LocalTime, selectedDate: LocalDate) {
        val sleepInfo = hashMapOf(
            "date" to selectedDate.toString(),
            "startTime" to startTime.toString(),
            "endTime" to endTime.toString()
        )

        viewModelScope.launch {
            db.collection("users").document(userId).collection("sleep").add(sleepInfo)
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
