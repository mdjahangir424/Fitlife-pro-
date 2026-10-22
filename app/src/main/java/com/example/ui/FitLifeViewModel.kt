package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.DailyPlanStatusEntity
import com.example.data.local.DownloadedVideoEntity
import com.example.data.local.FitLifeDatabase
import com.example.data.local.WorkoutRecordEntity
import com.example.data.preferences.UserPreferences
import com.example.data.preferences.UserPreferencesRepository
import com.example.data.repository.ExerciseCatalogRepository
import com.example.data.repository.WorkoutHistoryRepository
import com.example.localization.AppLanguage
import com.example.model.ActiveWorkoutSession
import com.example.model.Exercise
import com.example.model.SessionPhase
import com.example.receiver.NotificationScheduler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FitLifeViewModel(application: Application) : AndroidViewModel(application) {

    private val database = FitLifeDatabase.getInstance(application)
    private val prefsRepository = UserPreferencesRepository(application)
    val historyRepository = WorkoutHistoryRepository(database)

    // User Preferences Flow
    val userPreferences: StateFlow<UserPreferences> = prefsRepository.userPreferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferences()
        )

    // Workout Records Flow
    val workoutRecords: StateFlow<List<WorkoutRecordEntity>> = historyRepository.allWorkoutRecords
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Favorites Flow
    val favoriteIds: StateFlow<Set<String>> = historyRepository.favoriteExerciseIds
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    // Downloaded Videos Flow
    val downloadedVideos: StateFlow<List<DownloadedVideoEntity>> = historyRepository.allDownloadedVideos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Daily Plans Flow
    val dailyPlans: StateFlow<List<DailyPlanStatusEntity>> = historyRepository.allDailyPlans
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Active Workout Session State
    private val _activeSession = MutableStateFlow<ActiveWorkoutSession?>(null)
    val activeSession: StateFlow<ActiveWorkoutSession?> = _activeSession.asStateFlow()

    // Last completed workout summary state
    private val _lastCompletedSummary = MutableStateFlow<WorkoutRecordEntity?>(null)
    val lastCompletedSummary: StateFlow<WorkoutRecordEntity?> = _lastCompletedSummary.asStateFlow()

    private var timerJob: Job? = null

    // Start a workout session
    fun startWorkout(
        programTitle: String,
        categoryId: String,
        exercises: List<Exercise>
    ) {
        if (exercises.isEmpty()) return
        timerJob?.cancel()

        _activeSession.value = ActiveWorkoutSession(
            programTitle = programTitle,
            categoryId = categoryId,
            exercises = exercises,
            currentExerciseIndex = 0,
            phase = SessionPhase.Preparing(countdownSeconds = 5),
            totalDurationSeconds = 0
        )

        startTimerLoop()
    }

    private fun startTimerLoop() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val session = _activeSession.value ?: break

                when (val phase = session.phase) {
                    is SessionPhase.Preparing -> {
                        if (phase.countdownSeconds > 1) {
                            _activeSession.value = session.copy(
                                phase = SessionPhase.Preparing(phase.countdownSeconds - 1)
                            )
                        } else {
                            // Start first exercise
                            val firstEx = session.exercises[session.currentExerciseIndex]
                            _activeSession.value = session.copy(
                                phase = SessionPhase.Exercising(
                                    currentExercise = firstEx,
                                    secondsRemaining = firstEx.durationSeconds,
                                    isPaused = false
                                )
                            )
                        }
                    }

                    is SessionPhase.Exercising -> {
                        if (!phase.isPaused) {
                            val newDuration = session.totalDurationSeconds + 1
                            if (phase.secondsRemaining > 1) {
                                _activeSession.value = session.copy(
                                    totalDurationSeconds = newDuration,
                                    phase = phase.copy(secondsRemaining = phase.secondsRemaining - 1)
                                )
                            } else {
                                // Exercise finished! Move to rest or next exercise
                                val nextIndex = session.currentExerciseIndex + 1
                                if (nextIndex < session.exercises.size) {
                                    val nextEx = session.exercises[nextIndex]
                                    _activeSession.value = session.copy(
                                        totalDurationSeconds = newDuration,
                                        phase = SessionPhase.Resting(
                                            nextExercise = nextEx,
                                            secondsRemaining = phase.currentExercise.restSeconds,
                                            isPaused = false
                                        )
                                    )
                                } else {
                                    // Workout completed!
                                    finishWorkout(newDuration, session.exercises.size)
                                }
                            }
                        }
                    }

                    is SessionPhase.Resting -> {
                        if (!phase.isPaused) {
                            val newDuration = session.totalDurationSeconds + 1
                            if (phase.secondsRemaining > 1) {
                                _activeSession.value = session.copy(
                                    totalDurationSeconds = newDuration,
                                    phase = phase.copy(secondsRemaining = phase.secondsRemaining - 1)
                                )
                            } else {
                                // Rest completed -> start next exercise
                                val nextIndex = session.currentExerciseIndex + 1
                                val nextEx = session.exercises[nextIndex]
                                _activeSession.value = session.copy(
                                    currentExerciseIndex = nextIndex,
                                    totalDurationSeconds = newDuration,
                                    phase = SessionPhase.Exercising(
                                        currentExercise = nextEx,
                                        secondsRemaining = nextEx.durationSeconds,
                                        isPaused = false
                                    )
                                )
                            }
                        }
                    }

                    is SessionPhase.Completed -> {
                        break
                    }
                }
            }
        }
    }

    fun pauseSession() {
        val session = _activeSession.value ?: return
        when (val phase = session.phase) {
            is SessionPhase.Exercising -> {
                _activeSession.value = session.copy(phase = phase.copy(isPaused = true))
            }
            is SessionPhase.Resting -> {
                _activeSession.value = session.copy(phase = phase.copy(isPaused = true))
            }
            else -> {}
        }
    }

    fun resumeSession() {
        val session = _activeSession.value ?: return
        when (val phase = session.phase) {
            is SessionPhase.Exercising -> {
                _activeSession.value = session.copy(phase = phase.copy(isPaused = false))
            }
            is SessionPhase.Resting -> {
                _activeSession.value = session.copy(phase = phase.copy(isPaused = false))
            }
            else -> {}
        }
    }

    fun skipCurrentExercise() {
        val session = _activeSession.value ?: return
        val nextIndex = session.currentExerciseIndex + 1
        if (nextIndex < session.exercises.size) {
            val nextEx = session.exercises[nextIndex]
            _activeSession.value = session.copy(
                currentExerciseIndex = nextIndex,
                phase = SessionPhase.Exercising(
                    currentExercise = nextEx,
                    secondsRemaining = nextEx.durationSeconds,
                    isPaused = false
                )
            )
        } else {
            finishWorkout(session.totalDurationSeconds, session.currentExerciseIndex + 1)
        }
    }

    fun previousExercise() {
        val session = _activeSession.value ?: return
        val prevIndex = (session.currentExerciseIndex - 1).coerceAtLeast(0)
        val prevEx = session.exercises[prevIndex]
        _activeSession.value = session.copy(
            currentExerciseIndex = prevIndex,
            phase = SessionPhase.Exercising(
                currentExercise = prevEx,
                secondsRemaining = prevEx.durationSeconds,
                isPaused = false
            )
        )
    }

    fun addRestTime(seconds: Int = 10) {
        val session = _activeSession.value ?: return
        if (session.phase is SessionPhase.Resting) {
            val resting = session.phase as SessionPhase.Resting
            _activeSession.value = session.copy(
                phase = resting.copy(secondsRemaining = resting.secondsRemaining + seconds)
            )
        }
    }

    fun skipRest() {
        val session = _activeSession.value ?: return
        if (session.phase is SessionPhase.Resting) {
            val nextIndex = session.currentExerciseIndex + 1
            if (nextIndex < session.exercises.size) {
                val nextEx = session.exercises[nextIndex]
                _activeSession.value = session.copy(
                    currentExerciseIndex = nextIndex,
                    phase = SessionPhase.Exercising(
                        currentExercise = nextEx,
                        secondsRemaining = nextEx.durationSeconds,
                        isPaused = false
                    )
                )
            }
        }
    }

    fun finishWorkoutEarly() {
        val session = _activeSession.value ?: return
        finishWorkout(session.totalDurationSeconds, session.currentExerciseIndex)
    }

    private fun finishWorkout(durationSeconds: Int, completedCount: Int) {
        timerJob?.cancel()
        val session = _activeSession.value ?: return
        val minutes = (durationSeconds / 60).coerceAtLeast(1)
        val estCalories = (minutes * 6.5).toInt() // realistic average estimate based on moderate bodyweight exercise

        val record = WorkoutRecordEntity(
            programTitle = session.programTitle,
            categoryId = session.categoryId,
            completedAt = System.currentTimeMillis(),
            durationSeconds = durationSeconds,
            exercisesCompleted = completedCount,
            totalExercises = session.exercises.size,
            caloriesEst = estCalories,
            notes = ""
        )

        viewModelScope.launch {
            historyRepository.saveCompletedWorkout(
                programTitle = record.programTitle,
                categoryId = record.categoryId,
                durationSeconds = record.durationSeconds,
                exercisesCompleted = record.exercisesCompleted,
                totalExercises = record.totalExercises,
                caloriesEst = record.caloriesEst
            )
            _lastCompletedSummary.value = record
            _activeSession.value = session.copy(
                phase = SessionPhase.Completed(
                    totalSeconds = durationSeconds,
                    exercisesDone = completedCount,
                    caloriesEst = estCalories
                )
            )
        }
    }

    fun clearActiveSession() {
        timerJob?.cancel()
        _activeSession.value = null
    }

    fun toggleFavorite(exerciseId: String) {
        viewModelScope.launch {
            val isFav = favoriteIds.value.contains(exerciseId)
            historyRepository.toggleFavorite(exerciseId, isFav)
        }
    }

    fun downloadExerciseVideo(exercise: Exercise) {
        viewModelScope.launch {
            val sampleSizeMb = 14.2
            historyRepository.saveDownload(
                exerciseId = exercise.id,
                videoUrl = exercise.videoUrl,
                localPath = "/data/user/0/com.mdjahangiralam.fitlifepro/files/${exercise.id}.mp4",
                fileSizeMb = sampleSizeMb
            )
        }
    }

    fun deleteDownloadedVideo(exerciseId: String) {
        viewModelScope.launch {
            historyRepository.deleteDownload(exerciseId)
        }
    }

    fun updateLanguage(language: AppLanguage) {
        viewModelScope.launch {
            prefsRepository.setLanguage(language)
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            prefsRepository.setOnboardingCompleted(true)
        }
    }

    fun updateProfile(name: String, age: String, goal: String, minutes: Int) {
        viewModelScope.launch {
            prefsRepository.updateProfile(name, age, goal, minutes)
        }
    }

    fun updateReminder(enabled: Boolean, hour: Int, minute: Int) {
        viewModelScope.launch {
            prefsRepository.updateReminderSettings(enabled, hour, minute)
            val context = getApplication<Application>()
            if (enabled) {
                NotificationScheduler.scheduleDailyReminder(context, hour, minute)
            } else {
                NotificationScheduler.cancelReminder(context)
            }
        }
    }

    fun deleteWorkoutRecord(id: Long) {
        viewModelScope.launch {
            historyRepository.deleteWorkoutRecord(id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            historyRepository.clearAllData()
        }
    }

    fun resetAllUserData() {
        viewModelScope.launch {
            historyRepository.clearAllData()
            prefsRepository.resetAllPreferences()
            _activeSession.value = null
            _lastCompletedSummary.value = null
        }
    }
}
