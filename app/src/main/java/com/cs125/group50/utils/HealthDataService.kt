package com.cs125.group50.utils
import com.cs125.group50.data.HealthData
import com.cs125.group50.data.RecommendationResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HealthDataService {
    @POST("healthData/synchronize")
    fun synchronizeHealthData(@Body healthData: HealthData): Call<Void>

    @GET("/update/recommendation/{USER_ID}")
    suspend fun getRecommendation(@Path("USER_ID") userId: String): Response<RecommendationResponse>
}