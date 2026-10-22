package com.example.model

sealed interface SessionPhase {
    data class Preparing(val countdownSeconds: Int) : SessionPhase
    data class Exercising(val currentExercise: Exercise, val secondsRemaining: Int, val isPaused: Boolean) : SessionPhase
    data class Resting(val nextExercise: Exercise, val secondsRemaining: Int, val isPaused: Boolean) : SessionPhase
    data class Completed(val totalSeconds: Int, val exercisesDone: Int, val caloriesEst: Int) : SessionPhase
}

data class ActiveWorkoutSession(
    val programTitle: String,
    val categoryId: String,
    val exercises: List<Exercise>,
    val currentExerciseIndex: Int,
    val phase: SessionPhase,
    val totalDurationSeconds: Int = 0,
    val isMuted: Boolean = false,
    val playbackSpeed: Float = 1.0f
) {
    val progress: Float
        get() {
            if (exercises.isEmpty()) return 0f
            return (currentExerciseIndex.toFloat()) / exercises.size.toFloat()
        }

    val currentExercise: Exercise?
        get() = exercises.getOrNull(currentExerciseIndex)

    val nextExercise: Exercise?
        get() = exercises.getOrNull(currentExerciseIndex + 1)
}
