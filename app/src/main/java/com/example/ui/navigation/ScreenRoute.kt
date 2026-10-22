package com.example.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Onboarding : Screen("onboarding")
    data object LanguageSelection : Screen("language_selection")
    data object UserProfile : Screen("user_profile")
    data object Home : Screen("home")
    data object Categories : Screen("categories")
    data object ProgramDetails : Screen("program_details/{programId}") {
        fun createRoute(programId: String) = "program_details/$programId"
    }
    data object ExerciseVideoPlayer : Screen("exercise_player/{exerciseId}") {
        fun createRoute(exerciseId: String) = "exercise_player/$exerciseId"
    }
    data object ActiveWorkout : Screen("active_workout")
    data object RestTimer : Screen("rest_timer")
    data object DailyCalendar : Screen("daily_calendar")
    data object CompletionSummary : Screen("completion_summary")
    data object Progress : Screen("progress")
    data object WorkoutHistory : Screen("workout_history")
    data object DownloadedVideos : Screen("downloaded_videos")
    data object Favorites : Screen("favorites")
    data object ReminderSettings : Screen("reminder_settings")
    data object ProfileSettings : Screen("profile_settings")
    data object AboutOwner : Screen("about_owner")
    data object PrivacyPolicy : Screen("privacy_policy")
    data object HelpSupport : Screen("help_support")
}
