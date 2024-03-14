package com.cs125.group50.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs125.group50.data.SleepInfo
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SleepInformationViewModel : ViewModel() {
    private val db = Firebase.firestore // 假设你的数据库实例
    private val _sleepInfoList = MutableStateFlow<List<SleepInfo>>(emptyList())
    val sleepInfoList: StateFlow<List<SleepInfo>> = _sleepInfoList

    fun loadSleepInfo(userId: String) {
        viewModelScope.launch {
            try {
                val documents = db.collection("users").document(userId).collection("sleep")
                    .orderBy("date", Query.Direction.DESCENDING) // 以日期降序
                    .limit(5) // 只取最新的5条记录
                    .get()
                    .await()
                val sleepInfos = documents.map { document ->
                    SleepInfo(
                        duration = document.getString("duration") ?: "",
                        startTime = document.getString("startTime") ?: "",
                        endTime = document.getString("endTime") ?: "",
                        date = document.getString("date") ?: "",
                        stages = null
                    )
                }
                _sleepInfoList.value = sleepInfos
            } catch (exception: Exception) {
                // 处理错误
                Log.e("SleepInformationViewModel", "Error loading sleep info", exception)
            }
        }
    }
}