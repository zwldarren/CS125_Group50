package com.cs125.group50.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs125.group50.data.SleepInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SleepInformationViewModel : ViewModel() {
    private val _sleepInfoList = MutableStateFlow<List<SleepInfo>>(listOf(
        // Sample data, actual application should fetch from database
        SleepInfo("8 hours", "10 PM", "6 AM"),
        SleepInfo("6 hours", "11 PM", "5 AM")
    ))
    val sleepInfoList: StateFlow<List<SleepInfo>> = _sleepInfoList

    fun loadSleepInfo() {
        viewModelScope.launch {
            // Load sleep information logic from database
            // This is omitted here because the example already has hardcoded data
        }
    }
}