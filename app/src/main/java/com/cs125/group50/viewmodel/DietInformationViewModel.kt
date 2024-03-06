package com.cs125.group50.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs125.group50.data.DietInfo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DietInformationViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _dietInfoList = MutableStateFlow<List<DietInfo>>(emptyList())
    val dietInfoList: StateFlow<List<DietInfo>> = _dietInfoList


    fun loadDietInfo(userId: String) {
        viewModelScope.launch {
            db.collection("users").document(userId).collection("meals")
                .get()
//                .addOnSuccessListener { documents ->
//                    val dietInfos = documents.map { document ->
//                        DietInfo(
//                            mealType = document.getString("mealType") ?: "",
//                            foodName = document.getString("foodName") ?: "",
//                            caloriesPerHundredGrams = document.getString("caloriesPerHundredGrams") ?: "",
//                            foodAmount = document.getString("foodAmount") ?: "",
//                            date = document.getString("date") ?: "",
//                            time = document.getString("time") ?: ""
//                        )
//                    }
//                    _dietInfoList.value = dietInfos
//                }
//                .addOnFailureListener { exception ->
//                    // Handle any errors here
//                }
        }
    }
}
