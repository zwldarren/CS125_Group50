package com.cs125.group50.viewmodel

import android.content.Context
import android.os.RemoteException
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cs125.group50.data.HealthConnectManager
import com.cs125.group50.data.UserInfo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit


class DashboardViewModel(context: Context) : ViewModel() {
    private val healthConnectManager: HealthConnectManager = HealthConnectManager(context)

    // define the permissions required to access the data, find the list of permissions here:
    // https://developer.android.com/reference/kotlin/androidx/health/connect/client/records/package-summary
    private val requiredPermissions = setOf(
        HealthPermission.getReadPermission(HeartRateRecord::class),
//        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(HeightRecord::class),
        HealthPermission.getReadPermission(WeightRecord::class),
        HealthPermission.getReadPermission(SleepSessionRecord::class),
        HealthPermission.getReadPermission(ExerciseSessionRecord::class),
        HealthPermission.getReadPermission(ActiveCaloriesBurnedRecord::class),
        HealthPermission.getReadPermission(BodyFatRecord::class)
    )
    val healthConnectAvailability = healthConnectManager.availability
    var hasAllPermissions: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)

    init {
        checkPermissions()
    }

    fun checkPermissions() {
        viewModelScope.launch {
            hasAllPermissions.value = healthConnectManager.hasAllPermissions(requiredPermissions)
        }
    }

    fun requestPermissions(permissionLauncher: ManagedActivityResultLauncher<Set<String>, Set<String>>) {
        // This will be called from UI to request permissions
        viewModelScope.launch {
            try {
                healthConnectManager.checkPermissionsAndRun(requiredPermissions, permissionLauncher)
            } catch (remoteException: RemoteException) {
                errorMessage.value = "Failed to request permissions due to a remote exception."
            } catch (e: Exception) {
                errorMessage.value = "An unexpected error occurred: ${e.message}"
            }
        }
    }

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> = _userInfo

    private val _healthAdvice = MutableStateFlow<String?>(null)
    val healthAdvice: StateFlow<String?> = _healthAdvice

    fun loadUserInfo(userId: String) {
        viewModelScope.launch {
            Firebase.firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val user = UserInfo(
                            gender = document.getString("gender") ?: "",
                            height = document.getLong("height")?.toInt() ?: 0,
                            weight = document.getLong("weight")?.toInt() ?: 0,
                            age = document.getLong("age")?.toInt() ?: 0
                        )
                        _userInfo.value = user
                        // 计算并更新健康建议
                        _healthAdvice.value = calculateHealthAdvice(user)
                    } else {
                        // 处理用户信息不存在的情况
                    }
                }
                .addOnFailureListener {
                    // 处理错误
                }
        }
    }

    private fun calculateHealthAdvice(user: UserInfo): String {
        // 假设的健康建议逻辑，需要根据实际情况调整
//        return "Based on your profile, here is your health advice..."
        return "Based on your personal information and lifestyle habits, we offer the following health advice:\n\n" +
                "Increase sleep duration: Your current average sleep time is slightly below the recommended 7-9 hours for adults.\n" +
                "Adjust dietary habits: Based on your height and weight, it is recommended to adopt a balanced diet, increase the intake of vegetables and fruits, and reduce the consumption of high-sugar and high-fat foods to achieve a healthier weight.\n" +
                "Here are some recommended ingredients and recipes:\n" +
                "Chicken Breast: Grilled chicken breast with vegetable salad, chicken breast salad wrap.\n" +
                "Avocado: Avocado salad, avocado yogurt salad.\n" +
//                "Oats: Oatmeal nut energy balls, oat cookies.\n" +
                "Eggs: Boiled eggs, vegetable omelette, egg muffin.\n" +
                "Lean Beef: Beef tomato stew, beef salad wrap, lean beef stir-fry with vegetables.\n"
    }

    fun synchronizeHealthData() {
        viewModelScope.launch {
            // Example of synchronizing weight records
            if (healthConnectManager.hasAllPermissions(requiredPermissions)) {
                val weightRecords = healthConnectManager.readWeightInputs(
                    start = Instant.now().minus(30, ChronoUnit.DAYS),
                    end = Instant.now()
                )
                // Handle the fetched records, update UI or local database as needed
            } else {
                // You might need to pass a permission launcher here as well, similar to how it's done for checking permissions
            }
        }
    }

}

class DashboardViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}