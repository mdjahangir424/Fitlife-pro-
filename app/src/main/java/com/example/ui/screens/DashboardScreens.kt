package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DailyPlanStatusEntity
import com.example.data.local.DownloadedVideoEntity
import com.example.data.local.WorkoutRecordEntity
import com.example.data.repository.ExerciseCatalogRepository
import com.example.localization.AppLanguage
import com.example.model.Exercise
import com.example.ui.components.ExerciseItemCard
import com.example.ui.components.FitLifeBottomNavigation
import com.example.ui.components.FitLifeTopBar
import com.example.ui.components.StatCard
import com.example.ui.theme.FitElectricCyan
import com.example.ui.theme.FitNeonLime
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DailyCalendarScreen(
    dailyPlans: List<DailyPlanStatusEntity>,
    records: List<WorkoutRecordEntity>,
    streakDays: Int,
    language: AppLanguage,
    onNavigate: (String) -> Unit
) {
    val isBn = language == AppLanguage.BENGALI
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val completedDateSet = remember(records, dailyPlans) {
        val fromRecords = records.map { sdf.format(Date(it.completedAt)) }
        val fromPlans = dailyPlans.filter { it.isCompleted }.map { it.dateString }
        (fromRecords + fromPlans).toSet()
    }

    Scaffold(
        topBar = {
            FitLifeTopBar(
                title = if (isBn) "দৈনিক ওয়ার্কআউট ক্যালেন্ডার" else "Daily Workout Calendar"
            )
        },
        bottomBar = {
            FitLifeBottomNavigation(
                currentRoute = "daily_calendar",
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Streak Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = if (isBn) "বর্তমান ধারাবাহিকতা (স্ট্রিক)" else "Current Workout Streak",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$streakDays ${if (isBn) "দিন সক্রিয়" else "Days Active"}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = FitNeonLime
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFFFF5252).copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = Color(0xFFFF5252),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            // Current Month Calendar Grid
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isBn) "সাম্প্রতিক ১৪ দিনের ট্র্যাকিং" else "Past 14 Days Tracking",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        // Render past 14 days
                        val calendar = Calendar.getInstance()
                        val daysList = (0..13).map { offset ->
                            val cal = Calendar.getInstance()
                            cal.add(Calendar.DAY_OF_YEAR, -offset)
                            val dateStr = sdf.format(cal.time)
                            val dayNum = cal.get(Calendar.DAY_OF_MONTH)
                            val dayName = SimpleDateFormat("EEE", Locale.US).format(cal.time)
                            Triple(dateStr, dayNum, dayName)
                        }.reversed()

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            daysList.take(7).forEach { (dateStr, dayNum, dayName) ->
                                val isDone = completedDateSet.contains(dateStr)
                                DayPill(dayName = dayName, dayNum = dayNum, isDone = isDone)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            daysList.drop(7).forEach { (dateStr, dayNum, dayName) ->
                                val isDone = completedDateSet.contains(dateStr)
                                DayPill(dayName = dayName, dayNum = dayNum, isDone = isDone)
                            }
                        }
                    }
                }
            }

            // Completed Logs Title
            item {
                Text(
                    text = if (isBn) "সম্পন্ন সেশনের তালিকা (${records.size})" else "Completed Sessions (${records.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (records.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isBn) "এখনো কোনো ওয়ার্কআউট সম্পন্ন করা হয়নি। আজই আপনার প্রথম সেশন শুরু করুন!"
                            else "No workouts recorded yet. Complete your first workout today to start your streak!",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(records.take(10)) { rec ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = FitNeonLime,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = rec.programTitle,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = SimpleDateFormat("dd MMM, hh:mm a", Locale.US).format(Date(rec.completedAt)),
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Text(
                                text = "${rec.durationSeconds / 60} ${if (isBn) "মি." else "mins"}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = FitElectricCyan
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DayPill(dayName: String, dayNum: Int, isDone: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = dayName, fontSize = 10.sp, color = Color.Gray)
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(
                    if (isDone) FitNeonLime else Color.DarkGray.copy(alpha = 0.3f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$dayNum",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDone) Color.Black else Color.White
            )
        }
    }
}

@Composable
fun ProgressDashboardScreen(
    records: List<WorkoutRecordEntity>,
    streakDays: Int,
    targetDailyMinutes: Int,
    language: AppLanguage,
    onNavigate: (String) -> Unit
) {
    val isBn = language == AppLanguage.BENGALI

    val totalWorkouts = records.size
    val totalMinutes = (records.sumOf { it.durationSeconds.toLong() } / 60).toInt()
    val totalCalories = records.sumOf { it.caloriesEst }

    Scaffold(
        topBar = {
            FitLifeTopBar(
                title = if (isBn) "ফিটনেস অগ্রগতি" else "Progress & Statistics"
            )
        },
        bottomBar = {
            FitLifeBottomNavigation(
                currentRoute = "progress",
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stats Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = if (isBn) "মোট ওয়ার্কআউট" else "Workouts",
                        value = totalWorkouts.toString(),
                        subtitle = if (isBn) "সম্পন্ন সেশন" else "completed",
                        icon = Icons.Default.FitnessCenter,
                        iconTint = FitNeonLime,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = if (isBn) "মোট সময়" else "Active Time",
                        value = totalMinutes.toString(),
                        subtitle = if (isBn) "মিনিট" else "minutes",
                        icon = Icons.Default.Timer,
                        iconTint = FitElectricCyan,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = if (isBn) "আনুমানিক খরচ" else "Calories Burned",
                        value = "$totalCalories",
                        subtitle = "kcal (approx)",
                        icon = Icons.Default.LocalFireDepartment,
                        iconTint = Color(0xFFFF5252),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = if (isBn) "ধারাবাহিকতা" else "Current Streak",
                        value = "$streakDays",
                        subtitle = if (isBn) "দিন" else "days",
                        icon = Icons.Default.CheckCircle,
                        iconTint = Color(0xFFFFD600),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Weekly Minutes Bar Chart (Generated from genuine database records)
            item {
                val calendar = Calendar.getInstance()
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val dayFormat = SimpleDateFormat("EEE", Locale.US)

                val last7Days = (0..6).map { offset ->
                    val cal = Calendar.getInstance()
                    cal.add(Calendar.DAY_OF_YEAR, -offset)
                    val dateStr = sdf.format(cal.time)
                    val label = dayFormat.format(cal.time)
                    val minutesForDay = (records
                        .filter { sdf.format(Date(it.completedAt)) == dateStr }
                        .sumOf { it.durationSeconds } / 60)
                    label to minutesForDay
                }.reversed()

                val maxMinutes = (last7Days.maxOfOrNull { it.second } ?: targetDailyMinutes).coerceAtLeast(targetDailyMinutes)

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isBn) "গত ৭ দিনের ব্যায়ামের চার্ট (মিনিট)" else "Past 7 Days Activity (Minutes)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            last7Days.forEach { (dayLabel, mins) ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Bottom,
                                    modifier = Modifier.fillMaxHeight()
                                ) {
                                    val heightFraction = if (maxMinutes > 0) (mins.toFloat() / maxMinutes.toFloat()).coerceIn(0.08f, 1f) else 0.08f
                                    Box(
                                        modifier = Modifier
                                            .width(22.dp)
                                            .fillMaxHeight(heightFraction)
                                            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                            .background(if (mins > 0) FitElectricCyan else Color.DarkGray.copy(alpha = 0.4f))
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = dayLabel,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Disclaimer on calorie burn
            item {
                Text(
                    text = if (isBn) "*ফিটলাইফ প্রো-তে দেখানো ক্যালোরি হিসেব একটি আনুমানিক ধারণা। এটি কোনো চিকিৎসা বা ক্লিনিক্যাল ডেটা নয়।"
                    else "*All calorie expenditure and activity measurements on FitLife Pro are approximations for general wellness purposes and do not represent medical assessments.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
fun WorkoutHistoryScreen(
    records: List<WorkoutRecordEntity>,
    language: AppLanguage,
    onDeleteRecord: (Long) -> Unit,
    onClearAll: () -> Unit,
    onBackClick: () -> Unit
) {
    val isBn = language == AppLanguage.BENGALI
    var showClearDialog by remember { mutableStateOf(false) }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text(if (isBn) "ইতিহাস মুছে ফেলুন?" else "Clear All History?") },
            text = { Text(if (isBn) "আপনি কি নিশ্চিত যে সকল সংরক্ষিত ওয়ার্কআউট ইতিহাস মুছে ফেলতে চান?" else "Are you sure you want to delete all saved workout records?") },
            confirmButton = {
                TextButton(onClick = {
                    onClearAll()
                    showClearDialog = false
                }) {
                    Text(if (isBn) "মুছে ফেলুন" else "Clear", color = Color(0xFFFF5252))
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text(if (isBn) "বাতিল" else "Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            FitLifeTopBar(
                title = if (isBn) "ওয়ার্কআউট ইতিহাস" else "Workout History",
                onBackClick = onBackClick,
                actions = {
                    if (records.isNotEmpty()) {
                        IconButton(onClick = { showClearDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear History", tint = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (records.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isBn) "এখনো কোনো ওয়ার্কআউট ইতিহাস নেই" else "No workout history recorded yet",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(records) { rec ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = rec.programTitle,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.US).format(Date(rec.completedAt)),
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${rec.exercisesCompleted}/${rec.totalExercises} ${if (isBn) "ব্যায়াম সম্পন্ন" else "exercises"} • ${rec.caloriesEst} kcal",
                                    fontSize = 11.sp,
                                    color = FitElectricCyan
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${rec.durationSeconds / 60} ${if (isBn) "মিনিট" else "mins"}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                IconButton(onClick = { onDeleteRecord(rec.id) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color(0xFFFF5252).copy(alpha = 0.7f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DownloadedVideosScreen(
    downloadedVideos: List<DownloadedVideoEntity>,
    language: AppLanguage,
    onPlayExercise: (Exercise) -> Unit,
    onDeleteDownload: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val isBn = language == AppLanguage.BENGALI
    val totalSizeMb = downloadedVideos.sumOf { it.fileSizeMb }

    Scaffold(
        topBar = {
            FitLifeTopBar(
                title = if (isBn) "ডাউনলোড করা ভিডিও" else "Downloaded Videos",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            // Storage Summary Banner
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (isBn) "অফলাইন স্টোরেজ দখল" else "Offline Storage Used",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = String.format(Locale.US, "%.1f MB", totalSizeMb),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = FitElectricCyan
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.DownloadDone,
                        contentDescription = null,
                        tint = FitElectricCyan,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            if (downloadedVideos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isBn) "এখনো কোনো ভিডিও ডাউনলোড করা হয়নি। অফলাইনে দেখতে ব্যায়াম স্ক্রিন থেকে সেভ করুন।"
                        else "No downloaded videos yet. Save exercises from details screen for offline playback.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(32.dp),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(downloadedVideos) { download ->
                        val ex = ExerciseCatalogRepository.getExerciseById(download.exerciseId)
                        if (ex != null) {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (isBn) ex.nameBn else ex.nameEn,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "${download.fileSizeMb} MB • ${if (isBn) "অফলাইন প্রস্তুত" else "Ready offline"}",
                                            fontSize = 11.sp,
                                            color = FitNeonLime
                                        )
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = { onPlayExercise(ex) }) {
                                            Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = FitElectricCyan)
                                        }
                                        IconButton(onClick = { onDeleteDownload(download.exerciseId) }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFFF5252))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FavoritesScreen(
    favoriteIds: Set<String>,
    language: AppLanguage,
    onToggleFavorite: (String) -> Unit,
    onPlayExercise: (Exercise) -> Unit,
    onBackClick: () -> Unit
) {
    val isBn = language == AppLanguage.BENGALI
    val favoriteExercises = remember(favoriteIds) {
        favoriteIds.mapNotNull { ExerciseCatalogRepository.getExerciseById(it) }
    }

    Scaffold(
        topBar = {
            FitLifeTopBar(
                title = if (isBn) "প্রিয় ব্যায়ামসমূহ" else "Favorite Exercises",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        if (favoriteExercises.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isBn) "এখনো কোনো ব্যায়াম প্রিয় তালিকায় যোগ করা হয়নি" else "No favorite exercises added yet",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(favoriteExercises) { ex ->
                    ExerciseItemCard(
                        exercise = ex,
                        language = language,
                        isFavorite = true,
                        onToggleFavorite = { onToggleFavorite(ex.id) },
                        onPlayClick = { onPlayExercise(ex) }
                    )
                }
            }
        }
    }
}
