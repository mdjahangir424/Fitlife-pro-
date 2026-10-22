package com.example.model

enum class DifficultyLevel {
    BEGINNER, INTERMEDIATE, ADVANCED
}

data class Exercise(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val descriptionEn: String,
    val descriptionBn: String,
    val categoryId: String,
    val difficulty: DifficultyLevel,
    val durationSeconds: Int, // e.g. 30, 45, 60 seconds
    val repsTarget: Int? = null, // e.g. 15 reps if rep-based
    val restSeconds: Int = 20,
    val videoUrl: String,
    val videoRightsNotice: String = "Authorized Educational Fitness Content (CC-BY / Open Domain Media)",
    val targetMuscles: List<String>,
    val equipment: String = "None (Bodyweight)",
    val safetyInstructionsEn: String,
    val safetyInstructionsBn: String
)
