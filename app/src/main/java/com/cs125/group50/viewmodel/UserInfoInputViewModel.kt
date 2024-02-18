package com.cs125.group50.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import android.util.Log

class UserInfoInputViewModel : ViewModel() {
    private val db = Firebase.firestore

    fun saveUserInfo(userId: String, gender: String, height: Int, weight: Int, age: Int) {
        val userInfo = hashMapOf(
            "gender" to gender,
            "height" to height,
            "weight" to weight,
            "age" to age
        )

        viewModelScope.launch {
            db.collection("users").document(userId).set(userInfo)
                .addOnSuccessListener {
                    //debug
                    Log.d("Firestore", "DocumentSnapshot successfully written!")
                    // 处理成功保存数据后的逻辑
                }
                .addOnFailureListener {e ->
                    //debug
                    Log.w("Firestore", "Error writing document", e)
                    // 处理错误
                }
        }
    }
}