package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.WorkoutRecordEntity
import com.example.data.preferences.UserPreferences
import com.example.data.repository.ExerciseCatalogRepository
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.model.ActiveWorkoutSession
import com.example.model.Exercise
import com.example.model.WorkoutCategory
import com.example.model.WorkoutProgram
import com.example.ui.components.CategoryCard
import com.example.ui.components.ExerciseItemCard
import com.example.ui.components.FitLifeBottomNavigation
import com.example.ui.components.FitLifeTopBar
import com.example.ui.components.ProgramCard
import com.example.ui.components.StatCard
import com.example.ui.theme.FitElectricCyan
import com.example.ui.theme.FitNeonLime
import java.util.Calendar

@Composable
fun HomeScreen(
    preferences: UserPreferences,
    records: List<WorkoutRecordEntity>,
    activeSession: ActiveWorkoutSession?,
    favoriteIds: Set<String>,
    streakDays: Int,
    onStartProgram: (WorkoutProgram) -> Unit,
    onContinueSession: () -> Unit,
    onNavigate: (String) -> Unit,
    onPlayExercise: (Exercise) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    val language = preferences.language
    val strings = AppStrings.get(language)
    val isBn = language == AppLanguage.BENGALI

    val greeting = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when {
            hour < 12 -> strings.greetingsMorning
            hour < 17 -> strings.greetingsAfternoon
            else -> strings.greetingsEvening
        }
    }

    val totalWorkouts = records.size
    val totalMinutes = (records.sumOf { it.durationSeconds.toLong() } / 60).toInt()

    Scaffold(
        bottomBar = {
            FitLifeBottomNavigation(
                currentRoute = "home",
                language = language,
                onNavigate = onNavigate
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Header: Greeting + Profile icon
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (preferences.displayName.isNotBlank())
                                "${if (isBn) "সালাম," else "Hello,"} ${preferences.displayName} 👋"
                            else greeting,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = strings.motivationalQuote,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    IconButton(
                        onClick = { onNavigate("reminder_settings") },
                        modifier = Modifier
                            .size(40.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Reminders",
                            tint = FitElectricCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Unfinished Session Banner (if exists)
            if (activeSession != null) {
                item {
                    Card(
                        onClick = onContinueSession,
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = FitElectricCyan.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("continue_workout_card")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = if (isBn) "অসমাপ্ত ওয়ার্কআউট চালু আছে" else "Workout In Progress",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = FitElectricCyan
                                )
                                Text(
                                    text = activeSession.programTitle,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = FitElectricCyan
                            ) {
                                Text(
                                    text = strings.continueWorkout,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Today's Main Program Card
            item {
                val todayProgram = remember(preferences.ageGroup) {
                    if (preferences.ageGroup == "teen") {
                        ExerciseCatalogRepository.programs.first { it.isTeenSafe }
                    } else {
                        ExerciseCatalogRepository.programs.first()
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = strings.todayPlan,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    ProgramCard(
                        program = todayProgram,
                        language = language,
                        onStartClick = { onStartProgram(todayProgram) }
                    )
                }
            }

            // Real Data Activity Summary (Workouts, Minutes, Streak)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = strings.weeklyOverview,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            title = strings.workoutsCompleted,
                            value = totalWorkouts.toString(),
                            subtitle = if (isBn) "টি সেশন" else "sessions",
                            icon = Icons.Default.CheckCircle,
                            iconTint = FitNeonLime,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = strings.totalMinutes,
                            value = totalMinutes.toString(),
                            subtitle = strings.minutesShort,
                            icon = Icons.Default.Timer,
                            iconTint = FitElectricCyan,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = strings.currentStreak,
                            value = streakDays.toString(),
                            subtitle = strings.daysStreak,
                            icon = Icons.Default.LocalFireDepartment,
                            iconTint = Color(0xFFFF5252),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Quick Shortcut Row (Favorites, Downloads, History)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ShortcutCard(
                        title = strings.favorites,
                        icon = Icons.Default.Favorite,
                        tint = Color(0xFFFF5252),
                        onClick = { onNavigate("favorites") },
                        modifier = Modifier.weight(1f)
                    )
                    ShortcutCard(
                        title = strings.downloads,
                        icon = Icons.Default.Download,
                        tint = FitElectricCyan,
                        onClick = { onNavigate("downloaded_videos") },
                        modifier = Modifier.weight(1f)
                    )
                    ShortcutCard(
                        title = strings.workoutHistory,
                        icon = Icons.Default.History,
                        tint = FitNeonLime,
                        onClick = { onNavigate("workout_history") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Popular Categories Carousel
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = strings.categories,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = strings.viewAll,
                            fontSize = 13.sp,
                            color = FitElectricCyan,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onNavigate("categories") }
                        )
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(ExerciseCatalogRepository.categories) { cat ->
                            CategoryCard(
                                category = cat,
                                language = language,
                                onClick = { onNavigate("categories") },
                                modifier = Modifier.width(150.dp)
                            )
                        }
                    }
                }
            }

            // Recommended Programs List
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = if (isBn) "প্রস্তাবিত ওয়ার্কআউট প্রোগ্রাম" else "Recommended Workout Programs",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    ExerciseCatalogRepository.programs.drop(1).forEach { prog ->
                        ProgramCard(
                            program = prog,
                            language = language,
                            onStartClick = { onStartProgram(prog) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShortcutCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun CategoriesScreen(
    language: AppLanguage,
    favoriteIds: Set<String>,
    onToggleFavorite: (String) -> Unit,
    onPlayExercise: (Exercise) -> Unit,
    onNavigate: (String) -> Unit
) {
    val isBn = language == AppLanguage.BENGALI
    var selectedCategoryId by remember { mutableStateOf<String?>("cat_full_body") }

    Scaffold(
        topBar = {
            FitLifeTopBar(
                title = if (isBn) "ওয়ার্কআউট ক্যাটাগরি" else "Workout Categories"
            )
        },
        bottomBar = {
            FitLifeBottomNavigation(
                currentRoute = "categories",
                language = language,
                onNavigate = onNavigate
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            // Horizontal scrollable categories chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(ExerciseCatalogRepository.categories) { cat ->
                    val isSelected = selectedCategoryId == cat.id
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategoryId = cat.id },
                        label = {
                            Text(
                                text = if (isBn) cat.titleBn else cat.titleEn,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = FitElectricCyan,
                            selectedLabelColor = Color.Black
                        )
                    )
                }
            }

            // Exercise List for selected category
            val category = remember(selectedCategoryId) {
                ExerciseCatalogRepository.categories.firstOrNull { it.id == selectedCategoryId }
                    ?: ExerciseCatalogRepository.categories.first()
            }
            val exercises = remember(selectedCategoryId) {
                ExerciseCatalogRepository.getExercisesForCategory(category.id)
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    // Category Info Banner
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(category.accentColorHex).copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = if (isBn) category.titleBn else category.titleEn,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isBn) category.descriptionBn else category.descriptionEn,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                items(exercises) { ex ->
                    ExerciseItemCard(
                        exercise = ex,
                        language = language,
                        isFavorite = favoriteIds.contains(ex.id),
                        onToggleFavorite = { onToggleFavorite(ex.id) },
                        onPlayClick = { onPlayExercise(ex) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProgramDetailsScreen(
    programId: String,
    language: AppLanguage,
    favoriteIds: Set<String>,
    onStartProgram: (WorkoutProgram) -> Unit,
    onPlayExercise: (Exercise) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val isBn = language == AppLanguage.BENGALI
    val program = remember(programId) {
        ExerciseCatalogRepository.getProgramById(programId)
            ?: ExerciseCatalogRepository.programs.first()
    }
    val exercises = remember(program) {
        ExerciseCatalogRepository.getExercisesForProgram(program)
    }

    Scaffold(
        topBar = {
            FitLifeTopBar(
                title = if (isBn) program.titleBn else program.titleEn,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Surface(
                        onClick = { onStartProgram(program) },
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("program_details_start_button")
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBn) "ওয়ার্কআউট শুরু করুন" else "Start Workout Session",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = FitElectricCyan.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "${program.durationDays} ${if (isBn) "দিনের প্ল্যান" else "Days"}",
                                color = FitElectricCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = FitNeonLime.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "${program.minutesPerDay} ${if (isBn) "মিনিট/দিন" else "mins/day"}",
                                color = FitNeonLime,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Text(
                        text = if (isBn) program.descriptionBn else program.descriptionEn,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )
                }
            }

            item {
                Text(
                    text = "${if (isBn) "ব্যায়ামের তালিকা" else "Exercises"} (${exercises.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(exercises) { ex ->
                ExerciseItemCard(
                    exercise = ex,
                    language = language,
                    isFavorite = favoriteIds.contains(ex.id),
                    onToggleFavorite = { onToggleFavorite(ex.id) },
                    onPlayClick = { onPlayExercise(ex) }
                )
            }
        }
    }
}
