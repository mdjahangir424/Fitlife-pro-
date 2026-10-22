package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.repository.ExerciseCatalogRepository
import com.example.model.SessionPhase
import com.example.ui.FitLifeViewModel
import com.example.ui.screens.AboutOwnerScreen
import com.example.ui.screens.ActiveWorkoutScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.DailyCalendarScreen
import com.example.ui.screens.DownloadedVideosScreen
import com.example.ui.screens.ExerciseVideoPlayerScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HelpSupportScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LanguageSelectionScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PrivacyPolicyScreen
import com.example.ui.screens.ProfileSettingsScreen
import com.example.ui.screens.ProgramDetailsScreen
import com.example.ui.screens.ProgressDashboardScreen
import com.example.ui.screens.ReminderSettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.UserProfileScreen
import com.example.ui.screens.WorkoutCompletionSummaryScreen
import com.example.ui.screens.WorkoutHistoryScreen

@Composable
fun AppNavHost(
    viewModel: FitLifeViewModel,
    navController: NavHostController = rememberNavController()
) {
    val preferences by viewModel.userPreferences.collectAsState()
    val records by viewModel.workoutRecords.collectAsState()
    val activeSession by viewModel.activeSession.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val downloadedVideos by viewModel.downloadedVideos.collectAsState()
    val dailyPlans by viewModel.dailyPlans.collectAsState()
    val lastSummary by viewModel.lastCompletedSummary.collectAsState()

    val streakDays = viewModel.historyRepository.calculateStreak(records)

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // 1. Splash Screen
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    if (preferences.isOnboardingCompleted) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Onboarding.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        // 2. Onboarding Screen
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                language = preferences.language,
                onContinue = {
                    navController.navigate(Screen.UserProfile.route)
                }
            )
        }

        // 3. Language Selection Screen
        composable(Screen.LanguageSelection.route) {
            LanguageSelectionScreen(
                currentLanguage = preferences.language,
                onLanguageSelected = { lang ->
                    viewModel.updateLanguage(lang)
                    navController.popBackStack()
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // 4. User Profile & Fitness Preferences
        composable(Screen.UserProfile.route) {
            UserProfileScreen(
                language = preferences.language,
                initialName = preferences.displayName,
                initialAgeGroup = preferences.ageGroup,
                initialGoal = preferences.fitnessGoal,
                initialTargetMinutes = preferences.targetMinutes,
                onSaveProfile = { name, age, goal, minutes ->
                    viewModel.updateProfile(name, age, goal, minutes)
                    viewModel.completeOnboarding()
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                        popUpTo(Screen.UserProfile.route) { inclusive = true }
                    }
                },
                onBackClick = if (preferences.isOnboardingCompleted) {
                    { navController.popBackStack() }
                } else null
            )
        }

        // 5. Home Dashboard
        composable(Screen.Home.route) {
            HomeScreen(
                preferences = preferences,
                records = records,
                activeSession = activeSession,
                favoriteIds = favoriteIds,
                streakDays = streakDays,
                onStartProgram = { program ->
                    val exercises = ExerciseCatalogRepository.getExercisesForProgram(program)
                    viewModel.startWorkout(
                        programTitle = if (preferences.language == com.example.localization.AppLanguage.BENGALI) program.titleBn else program.titleEn,
                        categoryId = program.categoryId,
                        exercises = exercises
                    )
                    navController.navigate(Screen.ActiveWorkout.route)
                },
                onContinueSession = {
                    navController.navigate(Screen.ActiveWorkout.route)
                },
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                },
                onPlayExercise = { ex ->
                    navController.navigate(Screen.ExerciseVideoPlayer.createRoute(ex.id))
                },
                onToggleFavorite = { exId ->
                    viewModel.toggleFavorite(exId)
                }
            )
        }

        // 6. Workout Categories
        composable(Screen.Categories.route) {
            CategoriesScreen(
                language = preferences.language,
                favoriteIds = favoriteIds,
                onToggleFavorite = { viewModel.toggleFavorite(it) },
                onPlayExercise = { ex ->
                    navController.navigate(Screen.ExerciseVideoPlayer.createRoute(ex.id))
                },
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // 7. Workout Program Details
        composable(
            route = Screen.ProgramDetails.route,
            arguments = listOf(navArgument("programId") { type = NavType.StringType })
        ) { backStackEntry ->
            val programId = backStackEntry.arguments?.getString("programId") ?: ""
            ProgramDetailsScreen(
                programId = programId,
                language = preferences.language,
                favoriteIds = favoriteIds,
                onStartProgram = { program ->
                    val exercises = ExerciseCatalogRepository.getExercisesForProgram(program)
                    viewModel.startWorkout(
                        programTitle = if (preferences.language == com.example.localization.AppLanguage.BENGALI) program.titleBn else program.titleEn,
                        categoryId = program.categoryId,
                        exercises = exercises
                    )
                    navController.navigate(Screen.ActiveWorkout.route)
                },
                onPlayExercise = { ex ->
                    navController.navigate(Screen.ExerciseVideoPlayer.createRoute(ex.id))
                },
                onToggleFavorite = { viewModel.toggleFavorite(it) },
                onBackClick = { navController.popBackStack() }
            )
        }

        // 8. Exercise Video Player Screen
        composable(
            route = Screen.ExerciseVideoPlayer.route,
            arguments = listOf(navArgument("exerciseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: ""
            ExerciseVideoPlayerScreen(
                exerciseId = exerciseId,
                language = preferences.language,
                isFavorite = favoriteIds.contains(exerciseId),
                onToggleFavorite = { viewModel.toggleFavorite(exerciseId) },
                onDownloadClick = {
                    val ex = ExerciseCatalogRepository.getExerciseById(exerciseId)
                    if (ex != null) viewModel.downloadExerciseVideo(ex)
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // 9 & 10. Active Workout Session (including Rest Timer)
        composable(Screen.ActiveWorkout.route) {
            // Check if completed
            if (activeSession?.phase is SessionPhase.Completed) {
                WorkoutCompletionSummaryScreen(
                    summary = lastSummary,
                    language = preferences.language,
                    onSaveAndFinish = {
                        viewModel.clearActiveSession()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            } else {
                ActiveWorkoutScreen(
                    session = activeSession,
                    language = preferences.language,
                    onPause = { viewModel.pauseSession() },
                    onResume = { viewModel.resumeSession() },
                    onSkip = { viewModel.skipCurrentExercise() },
                    onPrevious = { viewModel.previousExercise() },
                    onAddRestTime = { viewModel.addRestTime(it) },
                    onSkipRest = { viewModel.skipRest() },
                    onFinishEarly = {
                        viewModel.finishWorkoutEarly()
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        // 11. Daily Workout Calendar
        composable(Screen.DailyCalendar.route) {
            DailyCalendarScreen(
                dailyPlans = dailyPlans,
                records = records,
                streakDays = streakDays,
                language = preferences.language,
                onNavigate = { route ->
                    navController.navigate(route) { launchSingleTop = true }
                }
            )
        }

        // 12. Workout Completion Summary (standalone route)
        composable(Screen.CompletionSummary.route) {
            WorkoutCompletionSummaryScreen(
                summary = lastSummary,
                language = preferences.language,
                onSaveAndFinish = {
                    viewModel.clearActiveSession()
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        // 13. Progress Dashboard
        composable(Screen.Progress.route) {
            ProgressDashboardScreen(
                records = records,
                streakDays = streakDays,
                targetDailyMinutes = preferences.targetMinutes,
                language = preferences.language,
                onNavigate = { route ->
                    navController.navigate(route) { launchSingleTop = true }
                }
            )
        }

        // 14. Workout History
        composable(Screen.WorkoutHistory.route) {
            WorkoutHistoryScreen(
                records = records,
                language = preferences.language,
                onDeleteRecord = { id -> viewModel.deleteWorkoutRecord(id) },
                onClearAll = { viewModel.clearAllHistory() },
                onBackClick = { navController.popBackStack() }
            )
        }

        // 15. Downloaded Videos
        composable(Screen.DownloadedVideos.route) {
            DownloadedVideosScreen(
                downloadedVideos = downloadedVideos,
                language = preferences.language,
                onPlayExercise = { ex ->
                    navController.navigate(Screen.ExerciseVideoPlayer.createRoute(ex.id))
                },
                onDeleteDownload = { exId ->
                    viewModel.deleteDownloadedVideo(exId)
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // 16. Favorites
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                favoriteIds = favoriteIds,
                language = preferences.language,
                onToggleFavorite = { viewModel.toggleFavorite(it) },
                onPlayExercise = { ex ->
                    navController.navigate(Screen.ExerciseVideoPlayer.createRoute(ex.id))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // 17. Reminder Settings
        composable(Screen.ReminderSettings.route) {
            ReminderSettingsScreen(
                preferences = preferences,
                language = preferences.language,
                onSaveReminder = { enabled, hour, minute ->
                    viewModel.updateReminder(enabled, hour, minute)
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // 18. Profile & Settings
        composable(Screen.ProfileSettings.route) {
            ProfileSettingsScreen(
                preferences = preferences,
                language = preferences.language,
                onUpdateLanguage = { viewModel.updateLanguage(it) },
                onResetData = { viewModel.resetAllUserData() },
                onNavigate = { route ->
                    navController.navigate(route) { launchSingleTop = true }
                }
            )
        }

        // 19. About & Owner Information
        composable(Screen.AboutOwner.route) {
            AboutOwnerScreen(
                language = preferences.language,
                onBackClick = { navController.popBackStack() }
            )
        }

        // 20. Privacy Policy
        composable(Screen.PrivacyPolicy.route) {
            PrivacyPolicyScreen(
                language = preferences.language,
                onBackClick = { navController.popBackStack() }
            )
        }

        // 21. Help & Support
        composable(Screen.HelpSupport.route) {
            HelpSupportScreen(
                language = preferences.language,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
