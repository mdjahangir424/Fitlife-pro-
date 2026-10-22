package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.WorkoutRecordEntity
import com.example.data.repository.ExerciseCatalogRepository
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.model.ActiveWorkoutSession
import com.example.model.Exercise
import com.example.model.SessionPhase
import com.example.ui.components.FitLifeTopBar
import com.example.ui.components.FitLifeVideoPlayer
import com.example.ui.theme.FitElectricCyan
import com.example.ui.theme.FitNeonLime

@Composable
fun ActiveWorkoutScreen(
    session: ActiveWorkoutSession?,
    language: AppLanguage,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onSkip: () -> Unit,
    onPrevious: () -> Unit,
    onAddRestTime: (Int) -> Unit,
    onSkipRest: () -> Unit,
    onFinishEarly: () -> Unit,
    onBackClick: () -> Unit
) {
    val isBn = language == AppLanguage.BENGALI

    if (session == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isBn) "কোনো সক্রিয় ওয়ার্কআউট নেই" else "No active workout session",
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = onBackClick) {
                    Text(if (isBn) "ফিরে যান" else "Go Back")
                }
            }
        }
        return
    }

    when (val phase = session.phase) {
        is SessionPhase.Preparing -> {
            // Preparing Countdown Screen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF090D16)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = if (isBn) "প্রস্তুত হোন!" else "GET READY!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = FitNeonLime
                    )

                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .background(FitElectricCyan.copy(alpha = 0.15f), CircleShape)
                            .border(3.dp, FitElectricCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = phase.countdownSeconds.toString(),
                            fontSize = 60.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }

                    val firstEx = session.exercises.getOrNull(session.currentExerciseIndex)
                    if (firstEx != null) {
                        Text(
                            text = if (isBn) firstEx.nameBn else firstEx.nameEn,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        is SessionPhase.Resting -> {
            // Dedicated Rest Timer View
            RestTimerView(
                nextExercise = phase.nextExercise,
                secondsRemaining = phase.secondsRemaining,
                isPaused = phase.isPaused,
                language = language,
                onPauseToggle = { if (phase.isPaused) onResume() else onPause() },
                onAddTenSeconds = { onAddRestTime(10) },
                onSkipRest = onSkipRest
            )
        }

        is SessionPhase.Exercising -> {
            // Active Exercise Screen with Media3 Video Player
            val currentEx = phase.currentExercise
            val animatedProgress by animateFloatAsState(targetValue = session.progress, label = "progress")

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // Top Progress Bar
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp),
                    color = FitElectricCyan,
                    trackColor = Color.DarkGray
                )

                // Real Video Player
                FitLifeVideoPlayer(
                    videoUrl = currentEx.videoUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    autoPlay = !phase.isPaused,
                    exerciseTitle = if (isBn) currentEx.nameBn else currentEx.nameEn
                )

                // Exercise Info & Timer
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${if (isBn) "ব্যায়াম" else "Exercise"} ${session.currentExerciseIndex + 1}/${session.exercises.size}",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = currentEx.difficulty.name,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Text(
                            text = if (isBn) currentEx.nameBn else currentEx.nameEn,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Text(
                            text = if (isBn) currentEx.descriptionBn else currentEx.descriptionEn,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )

                        // Safety Notice
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = FitNeonLime,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isBn) currentEx.safetyInstructionsBn else currentEx.safetyInstructionsEn,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }

                    // Large Time Remaining Counter
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${phase.secondsRemaining}",
                            fontSize = 58.sp,
                            fontWeight = FontWeight.Black,
                            color = if (phase.secondsRemaining <= 5) Color(0xFFFF5252) else MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (isBn) "সেকেন্ড বাকি" else "seconds remaining",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Workout Controls (Previous, Pause/Play, Skip, Finish)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onPrevious,
                            enabled = session.currentExerciseIndex > 0,
                            modifier = Modifier
                                .size(48.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FastRewind,
                                contentDescription = "Previous",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        IconButton(
                            onClick = { if (phase.isPaused) onResume() else onPause() },
                            modifier = Modifier
                                .size(64.dp)
                                .background(FitElectricCyan, CircleShape)
                                .testTag("pause_resume_button")
                        ) {
                            Icon(
                                imageVector = if (phase.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                                contentDescription = if (phase.isPaused) "Resume" else "Pause",
                                tint = Color.Black,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        IconButton(
                            onClick = onSkip,
                            modifier = Modifier
                                .size(48.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                                .testTag("skip_exercise_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipNext,
                                contentDescription = "Skip",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        IconButton(
                            onClick = onFinishEarly,
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFFFF5252).copy(alpha = 0.15f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "Stop",
                                tint = Color(0xFFFF5252)
                            )
                        }
                    }
                }
            }
        }

        is SessionPhase.Completed -> {
            // Routed through completion summary
            Text("Completed")
        }
    }
}

@Composable
fun RestTimerView(
    nextExercise: Exercise,
    secondsRemaining: Int,
    isPaused: Boolean,
    language: AppLanguage,
    onPauseToggle: () -> Unit,
    onAddTenSeconds: () -> Unit,
    onSkipRest: () -> Unit
) {
    val isBn = language == AppLanguage.BENGALI

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = if (isBn) "বিশ্রাম নিন" else "REST & BREATHE",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = FitNeonLime,
                letterSpacing = 1.sp
            )
            Text(
                text = if (isBn) "গভীরভাবে শ্বাস নিন এবং শরীরকে শিথিল রাখুন" else "Take deep breaths and stay hydrated",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }

        // Animated Rest Circular Countdown
        Box(
            modifier = Modifier.size(190.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { (secondsRemaining.toFloat() / 30f).coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxSize(),
                color = FitNeonLime,
                trackColor = Color.White.copy(alpha = 0.1f),
                strokeWidth = 10.dp,
                strokeCap = StrokeCap.Round
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$secondsRemaining",
                    fontSize = 54.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = if (isBn) "সেকেন্ড" else "seconds",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        // Up Next Preview Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2E)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = if (isBn) "পরবর্তী ব্যায়াম:" else "UP NEXT:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = FitElectricCyan
                )
                Text(
                    text = if (isBn) nextExercise.nameBn else nextExercise.nameEn,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${nextExercise.durationSeconds} ${if (isBn) "সেকেন্ড" else "sec"} • ${nextExercise.targetMuscles.joinToString()}",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        // Rest Controls (+10s, Pause, Skip)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onAddTenSeconds,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.12f)),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("+10s", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onSkipRest,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FitNeonLime),
                modifier = Modifier
                    .weight(1.2f)
                    .testTag("skip_rest_button")
            ) {
                Text(
                    text = if (isBn) "স্কিপ রেস্ট" else "Skip Rest",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun WorkoutCompletionSummaryScreen(
    summary: WorkoutRecordEntity?,
    language: AppLanguage,
    onSaveAndFinish: () -> Unit
) {
    val isBn = language == AppLanguage.BENGALI
    var notes by remember { mutableStateOf("") }

    val totalDuration = summary?.durationSeconds ?: 0
    val minutes = totalDuration / 60
    val seconds = totalDuration % 60
    val exercisesDone = summary?.exercisesCompleted ?: 0
    val totalExercises = summary?.totalExercises ?: 0
    val calories = summary?.caloriesEst ?: ((minutes.coerceAtLeast(1)) * 6)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Victory Badge
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(FitNeonLime.copy(alpha = 0.15f), CircleShape)
                .border(3.dp, FitNeonLime, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Workout Complete",
                tint = FitNeonLime,
                modifier = Modifier.size(60.dp)
            )
        }

        Text(
            text = if (isBn) "অভিনন্দন! চমৎকার কাজ করেছেন!" else "Awesome! Workout Completed!",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Text(
            text = summary?.programTitle ?: "FitLife Pro Workout",
            fontSize = 15.sp,
            color = FitElectricCyan,
            fontWeight = FontWeight.SemiBold
        )

        // Stats Summary Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatSummaryItem(
                title = if (isBn) "মোট সময়" else "Duration",
                value = String.format("%02d:%02d", minutes, seconds),
                icon = Icons.Default.Timer,
                tint = FitElectricCyan,
                modifier = Modifier.weight(1f)
            )
            StatSummaryItem(
                title = if (isBn) "ব্যায়াম সম্পন্ন" else "Exercises",
                value = "$exercisesDone/$totalExercises",
                icon = Icons.Default.CheckCircle,
                tint = FitNeonLime,
                modifier = Modifier.weight(1f)
            )
            StatSummaryItem(
                title = if (isBn) "আনুমানিক ক্যালোরি" else "Est. Burn",
                value = "$calories kcal",
                icon = Icons.Default.LocalFireDepartment,
                tint = Color(0xFFFF5252),
                modifier = Modifier.weight(1f)
            )
        }

        // Non-medical Disclaimer
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isBn) "*ক্যালোরি খরচ একটি আনুমানিক ধারণা এবং ব্যক্তির ওজন, তীব্রতা ও ফিটনেসের ওপর নির্ভর করে।"
                else "*Estimated calories burned are approximations only and depend on individual metabolism, body mass, and workout intensity.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(12.dp),
                lineHeight = 16.sp
            )
        }

        // Optional Workout Notes
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text(if (isBn) "আজকের ওয়ার্কআউট নোট (ঐচ্ছিক)" else "Workout notes (Optional)") },
            placeholder = { Text(if (isBn) "যেমন: আজ বেশ চাঙ্গা অনুভব করেছি!" else "e.g. Felt great energy today!") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onSaveAndFinish,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("summary_save_finish_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = if (isBn) "সংরক্ষণ করে হোম-এ ফিরুন" else "Save & Return Home",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun StatSummaryItem(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
            Text(text = title, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun ExerciseVideoPlayerScreen(
    exerciseId: String,
    language: AppLanguage,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onDownloadClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val isBn = language == AppLanguage.BENGALI
    val exercise = remember(exerciseId) {
        ExerciseCatalogRepository.getExerciseById(exerciseId)
            ?: ExerciseCatalogRepository.exercises.first()
    }

    Scaffold(
        topBar = {
            FitLifeTopBar(
                title = if (isBn) exercise.nameBn else exercise.nameEn,
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color(0xFFFF5252) else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Media3 Video Player
            FitLifeVideoPlayer(
                videoUrl = exercise.videoUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                exerciseTitle = if (isBn) exercise.nameBn else exercise.nameEn
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title & Duration Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isBn) exercise.nameBn else exercise.nameEn,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = FitElectricCyan.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "${exercise.durationSeconds} ${if (isBn) "সেকেন্ড" else "sec"}",
                            color = FitElectricCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Description
                Text(
                    text = if (isBn) exercise.descriptionBn else exercise.descriptionEn,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )

                // Muscle Targets & Equipment
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (isBn) "টার্গেট পেশি" else "Target Muscles",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = exercise.targetMuscles.joinToString(),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (isBn) "প্রয়োজনীয় সরঞ্জাম" else "Equipment",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = exercise.equipment,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Safety Instructions
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = FitNeonLime.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = FitNeonLime,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBn) "নিরাপত্তা ও সতর্কতা" else "Safety & Form Instructions",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = if (isBn) exercise.safetyInstructionsBn else exercise.safetyInstructionsEn,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    }
                }

                // Rights & Licensing Note
                Text(
                    text = exercise.videoRightsNotice,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )

                // Download Button
                Surface(
                    onClick = onDownloadClick,
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBn) "অফলাইনের জন্য সেভ করুন" else "Save for Offline Use",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
