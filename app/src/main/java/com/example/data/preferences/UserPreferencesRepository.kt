package com.example.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.localization.AppLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

data class UserPreferences(
    val language: AppLanguage = AppLanguage.BENGALI,
    val isOnboardingCompleted: Boolean = false,
    val displayName: String = "",
    val ageGroup: String = "adult_young", // teen, adult_young, adult_mature, senior
    val fitnessGoal: String = "general_health", // general_health, strength, weight_loss, mobility
    val targetMinutes: Int = 15,
    val isReminderEnabled: Boolean = true,
    val reminderHour: Int = 7,
    val reminderMinute: Int = 0,
    val isSoundVibrationEnabled: Boolean = true,
    val isDarkTheme: Boolean = true
)

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val LANGUAGE = stringPreferencesKey("app_language")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val DISPLAY_NAME = stringPreferencesKey("display_name")
        val AGE_GROUP = stringPreferencesKey("age_group")
        val FITNESS_GOAL = stringPreferencesKey("fitness_goal")
        val TARGET_MINUTES = intPreferencesKey("target_minutes")
        val REMINDER_ENABLED = booleanPreferencesKey("reminder_enabled")
        val REMINDER_HOUR = intPreferencesKey("reminder_hour")
        val REMINDER_MINUTE = intPreferencesKey("reminder_minute")
        val SOUND_VIBRATION = booleanPreferencesKey("sound_vibration")
        val DARK_THEME = booleanPreferencesKey("dark_theme")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { preferences ->
        val langCode = preferences[PreferencesKeys.LANGUAGE] ?: "bn"
        val language = if (langCode == "en") AppLanguage.ENGLISH else AppLanguage.BENGALI

        UserPreferences(
            language = language,
            isOnboardingCompleted = preferences[PreferencesKeys.ONBOARDING_COMPLETED] ?: false,
            displayName = preferences[PreferencesKeys.DISPLAY_NAME] ?: "",
            ageGroup = preferences[PreferencesKeys.AGE_GROUP] ?: "adult_young",
            fitnessGoal = preferences[PreferencesKeys.FITNESS_GOAL] ?: "general_health",
            targetMinutes = preferences[PreferencesKeys.TARGET_MINUTES] ?: 15,
            isReminderEnabled = preferences[PreferencesKeys.REMINDER_ENABLED] ?: true,
            reminderHour = preferences[PreferencesKeys.REMINDER_HOUR] ?: 7,
            reminderMinute = preferences[PreferencesKeys.REMINDER_MINUTE] ?: 0,
            isSoundVibrationEnabled = preferences[PreferencesKeys.SOUND_VIBRATION] ?: true,
            isDarkTheme = preferences[PreferencesKeys.DARK_THEME] ?: true
        )
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE] = language.code
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun updateProfile(
        displayName: String,
        ageGroup: String,
        fitnessGoal: String,
        targetMinutes: Int
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DISPLAY_NAME] = displayName
            preferences[PreferencesKeys.AGE_GROUP] = ageGroup
            preferences[PreferencesKeys.FITNESS_GOAL] = fitnessGoal
            preferences[PreferencesKeys.TARGET_MINUTES] = targetMinutes
        }
    }

    suspend fun updateReminderSettings(
        enabled: Boolean,
        hour: Int,
        minute: Int
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.REMINDER_ENABLED] = enabled
            preferences[PreferencesKeys.REMINDER_HOUR] = hour
            preferences[PreferencesKeys.REMINDER_MINUTE] = minute
        }
    }

    suspend fun setSoundVibrationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SOUND_VIBRATION] = enabled
        }
    }

    suspend fun setDarkTheme(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_THEME] = isDark
        }
    }

    suspend fun resetAllPreferences() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
