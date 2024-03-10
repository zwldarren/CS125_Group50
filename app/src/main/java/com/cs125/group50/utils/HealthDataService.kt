package com.cs125.group50.utils
import com.cs125.group50.data.HealthData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface HealthDataService {
    @POST("healthData/synchronize")
    fun synchronizeHealthData(@Body healthData: HealthData): Call<Void>
}