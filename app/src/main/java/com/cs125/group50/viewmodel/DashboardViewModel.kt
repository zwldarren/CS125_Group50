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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


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