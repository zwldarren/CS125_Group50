package com.cs125.group50.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs125.group50.data.ActivityInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ActivityInformationViewModel : ViewModel() {
    private val _activityInfoList = MutableStateFlow<List<ActivityInfo>>(listOf(
        // 示例数据，实际应用中应从数据库获取
        ActivityInfo("Running", "30 minutes", "5 km"),
        ActivityInfo("Swimming", "45 minutes", "1 km")
    ))
    val activityInfoList: StateFlow<List<ActivityInfo>> = _activityInfoList

    // 实际应用中，加载数据的逻辑会从数据库获取
    fun loadActivityInfo() {
        viewModelScope.launch {
            // 从数据库加载活动信息的逻辑
            // 此处省略，因为示例中已经硬编码数据
        }
    }
}
