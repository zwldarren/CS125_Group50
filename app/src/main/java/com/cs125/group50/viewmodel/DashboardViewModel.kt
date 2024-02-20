package com.cs125.group50.viewmodel

import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeartRateRecord
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cs125.group50.data.HealthConnectManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class DashboardViewModel(context: Context) : ViewModel() {
    private val healthConnectManager: HealthConnectManager = HealthConnectManager(context)
    private val requiredPermissions  = setOf(
        HealthPermission.getReadPermission(HeartRateRecord::class),
    )
    val healthConnectAvailability = healthConnectManager.availability
    var hasAllPermissions: MutableStateFlow<Boolean> = MutableStateFlow(false)

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
            healthConnectManager.checkPermissionsAndRun(requiredPermissions, permissionLauncher)
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