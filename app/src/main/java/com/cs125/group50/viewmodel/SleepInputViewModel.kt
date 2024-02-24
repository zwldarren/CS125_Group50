package com.cs125.group50.viewmodel

import androidx.lifecycle.ViewModel
import java.time.LocalTime

class SleepInputViewModel : ViewModel() {

    fun showSleepData(startTime: LocalTime, endTime: LocalTime) {
        // 将来添加将数据保存到数据库的逻辑
        println("Start: $startTime, End: $endTime")
    }
}
