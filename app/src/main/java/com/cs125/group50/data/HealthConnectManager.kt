package com.cs125.group50.data

import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.mutableStateOf
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.google.gson.Gson
import java.time.Duration
import java.time.Instant
import java.time.ZoneId


class HealthConnectManager(private val context: Context) {
    private val healthConnectClient by lazy { HealthConnectClient.getOrCreate(context) }
    private val gson = Gson()

    var availability = mutableStateOf(HealthConnectAvailability.NOT_SUPPORTED)
        private set

    init {
        checkAvailability()
    }

    fun checkAvailability() {
        availability.value = when {
            HealthConnectClient.getSdkStatus(context) == HealthConnectClient.SDK_AVAILABLE -> HealthConnectAvailability.INSTALLED
            else -> HealthConnectAvailability.NOT_INSTALLED
        }
    }

    suspend fun getGrantedPermissions(): Set<String> {
        return healthConnectClient.permissionController.getGrantedPermissions()
    }

    suspend fun hasAllPermissions(permissions: Set<String>): Boolean {
        return healthConnectClient.permissionController.getGrantedPermissions()
            .containsAll(permissions)
    }

    suspend fun checkPermissionsAndRun(
        requiredPermissions: Set<String>,
        permissionLauncher: ManagedActivityResultLauncher<Set<String>, Set<String>>
    ) {
        if (availability.value == HealthConnectAvailability.INSTALLED) {
            if (!hasAllPermissions(requiredPermissions)) {
                permissionLauncher.launch(requiredPermissions.toTypedArray().toSet())
            }
        }
    }

    /**
     *  Below are the methods used to read the health data from the HealthConnect API.
     */
    suspend fun readExerciseRecords(
        startTime: Instant,
        endTime: Instant
    ): List<ExerciseSessionRecord> {
        val request = ReadRecordsRequest(
            recordType = ExerciseSessionRecord::class,
            timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
        )
        val response = healthConnectClient.readRecords(request)
        return response.records
    }

    suspend fun readDietRecords(
        startTime: Instant,
        endTime: Instant
    ): List<NutritionRecord> {
        val request = ReadRecordsRequest(
            recordType = NutritionRecord::class,
            timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
        )
        val response = healthConnectClient.readRecords(request)
        return response.records
    }

    suspend fun readSleepRecords(
        startTime: Instant,
        endTime: Instant
    ): List<SleepSessionRecord> {
        val request = ReadRecordsRequest(
            recordType = SleepSessionRecord::class,
            timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
        )
        val response = healthConnectClient.readRecords(request)
        return response.records
    }

    suspend fun readTotalCaloriesBurnedRecords(
        startTime: Instant,
        endTime: Instant
    ): List<TotalCaloriesBurnedRecord> {
        val request = ReadRecordsRequest(
            recordType = TotalCaloriesBurnedRecord::class,
            timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
        )
        val response = healthConnectClient.readRecords(request)
        return response.records
    }

    suspend fun readHeartRateRecords(
        startTime: Instant,
        endTime: Instant
    ): List<HeartRateRecord> {
        val request = ReadRecordsRequest(
            recordType = HeartRateRecord::class,
            timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
        )
        val response = healthConnectClient.readRecords(request)
        return response.records
    }

    suspend fun readActiveCaloriesBurnedRecords(startTime: Instant, endTime: Instant): List<ActiveCaloriesBurnedRecord> {
        val request = ReadRecordsRequest(
            recordType = ActiveCaloriesBurnedRecord::class,
            timeRangeFilter = TimeRangeFilter.between(startTime, endTime),
        )
        val response = healthConnectClient.readRecords(request)
        return response.records
    }

    suspend fun integrateExerciseData(
        exerciseRecords: List<ExerciseSessionRecord>,
        caloriesRecords: List<TotalCaloriesBurnedRecord>,
    ): List<Map<String, String?>> {
        return exerciseRecords.map { exerciseRecord ->
            val exerciseDuration = Duration.between(
                exerciseRecord.startTime,
                exerciseRecord.endTime
            )
            val caloriesBurnedDuringExercise = caloriesRecords.filter {
                it.startTime >= exerciseRecord.startTime && it.endTime <= exerciseRecord.endTime
            }.sumOf { it.energy.inCalories }

            mapOf(
                "activityType" to exerciseRecord.exerciseType.toString(),
                "duration" to exerciseDuration.toMinutes().toString(),
                "caloriesBurned" to caloriesBurnedDuringExercise.toString(),
                "date" to exerciseRecord.startTime.atZone(ZoneId.of("America/Los_Angeles")).toLocalDate().toString(),
            )
        }
    }

    suspend fun filterSleepRecords(
        records: List<SleepSessionRecord>
    ): List<Map<String, String?>> {
        return records.map { record ->
            mapOf(
                "startTime" to record.startTime.toString(),
                "endTime" to record.endTime.toString(),
                "duration" to Duration
                    .between(record.startTime, record.endTime)
                    .toMinutes().toString()
            )
        }
    }

    suspend fun aggregateTotalCaloriesBurned(
        startTime: Instant,
        endTime: Instant
    ): Double {
        return try {
            val response = healthConnectClient.aggregate(
                AggregateRequest(
                    metrics = setOf(TotalCaloriesBurnedRecord.ENERGY_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            // Convert total energy to kilocalories, may be null if no data is available
            response[TotalCaloriesBurnedRecord.ENERGY_TOTAL]?.inCalories ?: 0.0
        } catch (e: Exception) {
            0.0 // Or handle error more appropriately
        }
    }

    suspend fun aggregateHeartRate(
        startTime: Instant,
        endTime: Instant
    ): Map<String, String?> {
        return try {
            val response = healthConnectClient.aggregate(
                AggregateRequest(
                    metrics = setOf(
                        HeartRateRecord.BPM_MAX,
                        HeartRateRecord.BPM_MIN,
                        HeartRateRecord.BPM_AVG
                    ),
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            mapOf(
                "startTime" to "$startTime",
                "endTime" to "$endTime",
                "minimumHeartRate" to response[HeartRateRecord.BPM_MIN]?.toDouble().toString(),
                "maximumHeartRate" to response[HeartRateRecord.BPM_MAX]?.toDouble().toString(),
                "averageHeartRate" to response[HeartRateRecord.BPM_AVG]?.toDouble().toString()
            )
        } catch (e: Exception) {
            mapOf(
                "minimumHeartRate" to null,
                "maximumHeartRate" to null,
                "averageHeartRate" to null
            )
        }
    }


//    suspend fun getChangesToken(): String {
//        return healthConnectClient.getChangesToken(
//            ChangesTokenRequest(
//                setOf(
//                    ExerciseSessionRecord::class,
//                    StepsRecord::class,
//                    TotalCaloriesBurnedRecord::class,
//                    HeartRateRecord::class,
//                    WeightRecord::class
//                )
//            )
//        )
//    }
//
//    suspend fun getChanges(token: String): Flow<ChangesMessage> = flow {
//        var nextChangesToken = token
//        do {
//            val response = healthConnectClient.getChanges(nextChangesToken)
//            if (response.changesTokenExpired) {
//                // As described here: https://developer.android.com/guide/health-and-fitness/health-connect/data-and-data-types/differential-changes-api
//                // tokens are only valid for 30 days. It is important to check whether the token has
//                // expired. As well as ensuring there is a fallback to using the token (for example
//                // importing data since a certain date), more importantly, the app should ensure
//                // that the changes API is used sufficiently regularly that tokens do not expire.
//                throw IOException("Changes token has expired")
//            }
//            emit(ChangesMessage.ChangeList(response.changes))
//            nextChangesToken = response.nextChangesToken
//        } while (response.hasMore)
//        emit(ChangesMessage.NoMoreChanges(nextChangesToken))
//    }
//
//    private suspend inline fun <reified T : Record> readData(
//        timeRangeFilter: TimeRangeFilter,
//        dataOriginFilter: Set<DataOrigin> = setOf(),
//    ): List<T> {
//        val request = ReadRecordsRequest(
//            recordType = T::class,
//            dataOriginFilter = dataOriginFilter,
//            timeRangeFilter = timeRangeFilter
//        )
//        return healthConnectClient.readRecords(request).records
//    }
//
//    sealed class ChangesMessage {
//        data class NoMoreChanges(val nextChangesToken: String) : ChangesMessage()
//        data class ChangeList(val changes: List<Change>) : ChangesMessage()
//    }
}

enum class HealthConnectAvailability {
    INSTALLED,
    NOT_INSTALLED,
    NOT_SUPPORTED
}