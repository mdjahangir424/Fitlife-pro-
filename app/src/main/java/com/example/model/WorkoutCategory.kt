package com.example.model

data class WorkoutCategory(
    val id: String,
    val titleEn: String,
    val titleBn: String,
    val descriptionEn: String,
    val descriptionBn: String,
    val iconName: String,
    val accentColorHex: Long,
    val exerciseCount: Int = 0
)

data class WorkoutProgram(
    val id: String,
    val titleEn: String,
    val titleBn: String,
    val descriptionEn: String,
    val descriptionBn: String,
    val categoryId: String,
    val difficulty: DifficultyLevel,
    val durationDays: Int, // e.g. 7, 14, 28 days
    val minutesPerDay: Int,
    val exerciseIds: List<String>,
    val isTeenSafe: Boolean = false
)
