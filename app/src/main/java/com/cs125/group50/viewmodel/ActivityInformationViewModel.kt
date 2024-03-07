package com.cs125.group50.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs125.group50.data.ActivityInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ActivityInformationViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _activityInfoList = MutableStateFlow<List<ActivityInfo>>(emptyList())
    val activityInfoList: StateFlow<List<ActivityInfo>> = _activityInfoList

    fun loadActivityInfo(userId: String) {
        viewModelScope.launch {
            try {
                val documents = db.collection("users").document(userId).collection("activity")
                    .limit(5)
                    .get()
                    .await() // 使用 Kotlin 协程的 await，确保已添加相应依赖

                val activities = documents.map { document ->
                    ActivityInfo(
                        activityType = document.getString("activityType") ?: "",
                        duration = document.getString("duration") ?: "",
                        caloriesBurned = document.getString("caloriesBurned") ?: "",
                        date = document.getString("date") ?: ""
                    )
                }
                _activityInfoList.value = activities
            } catch (e: Exception) {
                Log.e("ActivityInfoVM", "Error loading activity info", e)
                // Handle exception
            }
        }
    }
}
