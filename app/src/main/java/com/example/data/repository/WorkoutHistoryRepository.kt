package com.example.data.repository

import com.example.data.local.DailyPlanStatusEntity
import com.example.data.local.DownloadedVideoEntity
import com.example.data.local.FavoriteExerciseEntity
import com.example.data.local.FitLifeDatabase
import com.example.data.local.WorkoutRecordEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class WorkoutHistoryRepository(private val database: FitLifeDatabase) {

    val allWorkoutRecords: Flow<List<WorkoutRecordEntity>> =
        database.workoutDao().getAllRecords()

    val totalWorkoutsCount: Flow<Int> =
        database.workoutDao().getTotalWorkoutsCount()

    val totalDurationSeconds: Flow<Long> =
        database.workoutDao().getTotalDurationSeconds()

    val favoriteExerciseIds: Flow<Set<String>> =
        database.favoriteDao().getAllFavorites().map { list ->
            list.map { it.exerciseId }.toSet()
        }

    val allDownloadedVideos: Flow<List<DownloadedVideoEntity>> =
        database.downloadedVideoDao().getAllDownloadedVideos()

    val allDailyPlans: Flow<List<DailyPlanStatusEntity>> =
        database.dailyPlanDao().getAllDailyStatus()

    suspend fun saveCompletedWorkout(
        programTitle: String,
        categoryId: String,
        durationSeconds: Int,
        exercisesCompleted: Int,
        totalExercises: Int,
        caloriesEst: Int,
        notes: String = ""
    ): Long {
        val record = WorkoutRecordEntity(
            programTitle = programTitle,
            categoryId = categoryId,
            completedAt = System.currentTimeMillis(),
            durationSeconds = durationSeconds,
            exercisesCompleted = exercisesCompleted,
            totalExercises = totalExercises,
            caloriesEst = caloriesEst,
            notes = notes
        )
        val id = database.workoutDao().insertRecord(record)

        // Mark today in daily plan
        val todayStr = getTodayDateString()
        database.dailyPlanDao().insertOrUpdate(
            DailyPlanStatusEntity(
                dateString = todayStr,
                programId = programTitle,
                isCompleted = true,
                completedMinutes = (durationSeconds / 60).coerceAtLeast(1)
            )
        )
        return id
    }

    suspend fun deleteWorkoutRecord(id: Long) {
        database.workoutDao().deleteRecordById(id)
    }

    suspend fun toggleFavorite(exerciseId: String, isCurrentlyFav: Boolean) {
        if (isCurrentlyFav) {
            database.favoriteDao().removeFavorite(exerciseId)
        } else {
            database.favoriteDao().addFavorite(FavoriteExerciseEntity(exerciseId = exerciseId))
        }
    }

    suspend fun saveDownload(
        exerciseId: String,
        videoUrl: String,
        localPath: String,
        fileSizeMb: Double
    ) {
        database.downloadedVideoDao().insertDownload(
            DownloadedVideoEntity(
                exerciseId = exerciseId,
                videoUrl = videoUrl,
                localFilePath = localPath,
                fileSizeMb = fileSizeMb
            )
        )
    }

    suspend fun deleteDownload(exerciseId: String) {
        database.downloadedVideoDao().deleteDownload(exerciseId)
    }

    suspend fun clearAllData() {
        database.workoutDao().clearAllRecords()
        database.favoriteDao().clearFavorites()
        database.dailyPlanDao().clearDailyPlans()
        database.downloadedVideoDao().clearDownloads()
    }

    fun calculateStreak(records: List<WorkoutRecordEntity>): Int {
        if (records.isEmpty()) return 0
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val completedDates = records.map { sdf.format(Date(it.completedAt)) }.toSet()

        val calendar = Calendar.getInstance()
        var streak = 0

        val todayStr = sdf.format(calendar.time)
        // Check if completed today, or start from yesterday
        if (completedDates.contains(todayStr)) {
            streak++
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        } else {
            // Check yesterday
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val yesterdayStr = sdf.format(calendar.time)
            if (!completedDates.contains(yesterdayStr)) {
                return 0
            }
        }

        while (true) {
            val dateStr = sdf.format(calendar.time)
            if (completedDates.contains(dateStr)) {
                streak++
                calendar.add(Calendar.DAY_OF_YEAR, -1)
            } else {
                break
            }
        }
        return streak
    }

    companion object {
        fun getTodayDateString(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            return sdf.format(Date())
        }
    }
}
