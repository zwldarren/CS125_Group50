package com.cs125.group50.utils

import com.cs125.group50.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiService {
    private const val BASE_URL = BuildConfig.BASE_URL

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val healthDataService: HealthDataService = retrofit.create(HealthDataService::class.java)

    fun getHealthDataService(): HealthDataService = healthDataService
}