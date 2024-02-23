package com.cs125.group50.viewmodel

import androidx.lifecycle.ViewModel
import java.time.LocalTime

class ActivityInputViewModel : ViewModel() {

    fun showActivityData(activityType: String, startTime: LocalTime, endTime: LocalTime) {
        // 将来添加将数据保存到数据库的逻辑
        println("Activity: $activityType, Start: $startTime, End: $endTime")
    }
}
