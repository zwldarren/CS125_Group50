package com.cs125.group50.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs125.group50.data.DietInfo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.Query

class DietInformationViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _dietInfoList = MutableStateFlow<List<DietInfo>>(emptyList())
    val dietInfoList: StateFlow<List<DietInfo>> = _dietInfoList

    fun loadDietInfo(userId: String) {
        viewModelScope.launch {
            try {
                val documents = db.collection("users").document(userId).collection("meals")
                    .orderBy("date", Query.Direction.DESCENDING) // sorted by date
                    .limit(5) // limited to 5
                    .get()
                    .await()
                val dietInfos = documents.map { document ->
                    DietInfo(
                        mealType = document.getString("mealType") ?: "",
                        foodName = document.getString("foodName") ?: "",
                        totalFat = document.getString("totalFat") ?: "",
                        totalCalories = document.getString("totalCalories") ?: "",
                        foodAmount = document.getString("foodAmount") ?: "",
                        date = document.getString("date") ?: "",
                        time = document.getString("time") ?: ""
                    )
                }
                _dietInfoList.value = dietInfos
            } catch (exception: Exception) {
                // Handle any errors here
                Log.e("DietInformationViewModel", "Error loading diet info", exception)
            }
        }
    }
}
