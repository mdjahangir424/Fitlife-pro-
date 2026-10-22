package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_records")
data class WorkoutRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val programTitle: String,
    val categoryId: String,
    val completedAt: Long,
    val durationSeconds: Int,
    val exercisesCompleted: Int,
    val totalExercises: Int,
    val caloriesEst: Int,
    val notes: String = ""
)

@Entity(tableName = "favorite_exercises")
data class FavoriteExerciseEntity(
    @PrimaryKey
    val exerciseId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "daily_plan_status")
data class DailyPlanStatusEntity(
    @PrimaryKey
    val dateString: String, // Format: YYYY-MM-DD
    val programId: String,
    val isCompleted: Boolean,
    val completedMinutes: Int,
    val completedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "downloaded_videos")
data class DownloadedVideoEntity(
    @PrimaryKey
    val exerciseId: String,
    val videoUrl: String,
    val localFilePath: String,
    val fileSizeMb: Double,
    val downloadedAt: Long = System.currentTimeMillis(),
    val isComplete: Boolean = true
)
