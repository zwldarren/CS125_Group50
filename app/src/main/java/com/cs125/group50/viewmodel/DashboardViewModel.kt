package com.cs125.group50.viewmodel

import android.content.Context
import android.os.RemoteException
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cs125.group50.data.ActivityInfo
import com.cs125.group50.data.DietInfo
import com.cs125.group50.data.HealthConnectManager
import com.cs125.group50.data.HeartRateInfo
import com.cs125.group50.data.SleepInfo
import com.cs125.group50.data.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit


class DashboardViewModel(context: Context) : ViewModel() {
    private val healthConnectManager: HealthConnectManager = HealthConnectManager(context)
    val functions = Firebase.functions
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid.orEmpty()

    // define the permissions required to access the data, find the list of permissions here:
    // https://developer.android.com/reference/kotlin/androidx/health/connect/client/records/package-summary
    private val requiredPermissions = setOf(
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(SleepSessionRecord::class),
        HealthPermission.getReadPermission(ExerciseSessionRecord::class),
        HealthPermission.getReadPermission(ActiveCaloriesBurnedRecord::class),
        HealthPermission.getReadPermission(BodyFatRecord::class),
        HealthPermission.getReadPermission(NutritionRecord::class),
        HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class)
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

    suspend fun synchronizeHealthData() {
        viewModelScope.launch {
            if (healthConnectManager.hasAllPermissions(requiredPermissions)) {
                val endTime: Instant = Instant.now()
                val startTime: Instant = endTime.minus(30, ChronoUnit.DAYS)

                val sleepRecords = healthConnectManager.readSleepRecords(startTime, endTime)
                val exerciseRecord = healthConnectManager.readExerciseRecords(startTime, endTime)
                val dietRecords = healthConnectManager.readDietRecords(startTime, endTime)
                val heartRateRecords = healthConnectManager.readHeartRateRecords(startTime, endTime)
                val totalCaloriesBurnedRecords = healthConnectManager.readTotalCaloriesBurnedRecords(startTime, endTime)

                val dataToSend = mapOf(
                    "sleepRecords" to sleepRecords,
                    "exerciseRecord" to exerciseRecord,
                    "dietRecords" to dietRecords,
                    "heartRateRecords" to heartRateRecords,
                    "totalCaloriesBurnedRecords" to totalCaloriesBurnedRecords
                )
                functions
                    .getHttpsCallable("synchronizeHealthData")
                    .call(dataToSend)
                    .addOnSuccessListener { result ->
                        // success
                        Log.d("CloudFunctions", "Function result: ${result.data}")
                    }
                    .addOnFailureListener { e ->
                        // error
                        Log.w("CloudFunctions", "Function error: ", e)
                    }

            } else {
                // handle the case where the app does not have the required permissions
            }
        }
    }

    private val db = Firebase.firestore
    fun saveActivityInfo(userId: String, activityInfo: ActivityInfo) {
        val activityMap = hashMapOf(
            "activityType" to activityInfo.activityType,
            "duration" to activityInfo.duration,
            "caloriesBurned" to activityInfo.caloriesBurned,
            "date" to activityInfo.date
            // 其他字段...
        )
        db.collection("users").document(userId).collection("activity")
            .add(activityMap)
            .addOnSuccessListener { documentReference ->
                Log.d("DashboardViewModel", "ActivityInfo added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("DashboardViewModel", "Error adding ActivityInfo", e)
            }
    }

    fun saveDietInfo(userId: String, dietInfo: DietInfo) {
        val dietMap = hashMapOf(
            "mealType" to dietInfo.mealType,
            "foodName" to dietInfo.foodName,  // 假设这些信息已正确获取
            "totalFat" to dietInfo.totalFat,
            "caloriesPerHundredGrams" to dietInfo.caloriesPerHundredGrams,
            "foodAmount" to dietInfo.foodAmount,
            "date" to dietInfo.date,  // 你需要确保这个值在dietInfo对象创建时被正确设置
            "time" to dietInfo.time   // 同上
        )

        db.collection("users").document(userId).collection("meals")
            .add(dietMap)
            .addOnSuccessListener { documentReference ->
                Log.d("DashboardViewModel", "DietInfo added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("DashboardViewModel", "Error adding DietInfo", e)
            }
    }

    fun saveSleepInfo(userId: String, sleepInfo: SleepInfo) {
        val sleepMap = hashMapOf(
            "duration" to sleepInfo.duration,
            "startTime" to sleepInfo.startTime,
            "endTime" to sleepInfo.endTime,
            "date" to sleepInfo.date  // 确保这个值在SleepInfo对象创建时被正确设置
        )

        db.collection("users").document(userId).collection("sleep")
            .add(sleepMap)
            .addOnSuccessListener { documentReference ->
                Log.d("DashboardViewModel", "SleepInfo added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("DashboardViewModel", "Error adding SleepInfo", e)
            }
    }

    fun saveHeartRateInfo(userId: String, heartRateInfo: HeartRateInfo) {
        val heartRateMap = hashMapOf(
            "startTime" to heartRateInfo.startTime,
            "endTime" to heartRateInfo.endTime,
            "minimumHeartRate" to heartRateInfo.minimumHeartRate,
            "maximumHeartRate" to heartRateInfo.maximumHeartRate,
            "averageHeartRate" to heartRateInfo.averageHeartRate
        )

        db.collection("users").document(userId).collection("heartRate")
            .add(heartRateMap)
            .addOnSuccessListener { documentReference ->
                Log.d("DashboardViewModel", "HeartRateInfo added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("DashboardViewModel", "Error adding HeartRateInfo", e)
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