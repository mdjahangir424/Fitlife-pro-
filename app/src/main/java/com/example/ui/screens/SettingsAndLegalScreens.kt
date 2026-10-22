package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.config.AppConfig
import com.example.data.preferences.UserPreferences
import com.example.localization.AppLanguage
import com.example.receiver.NotificationScheduler
import com.example.receiver.WorkoutReminderReceiver
import com.example.ui.components.FitLifeBottomNavigation
import com.example.ui.components.FitLifeTopBar
import com.example.ui.theme.FitElectricCyan
import com.example.ui.theme.FitNeonLime

@Composable
fun ReminderSettingsScreen(
    preferences: UserPreferences,
    language: AppLanguage,
    onSaveReminder: (Boolean, Int, Int) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val isBn = language == AppLanguage.BENGALI

    var enabled by remember { mutableStateOf(preferences.isReminderEnabled) }
    var hour by remember { mutableIntStateOf(preferences.reminderHour) }
    var minute by remember { mutableIntStateOf(preferences.reminderMinute) }
    var testSentMessage by remember { mutableStateOf<String?>(null) }

    val notificationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onSaveReminder(true, hour, minute)
        }
    }

    Scaffold(
        topBar = {
            FitLifeTopBar(
                title = if (isBn) "রিমাইন্ডার সেটিংস" else "Workout Reminders",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Main Toggle Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isBn) "দৈনিক ব্যায়ামের রিমাইন্ডার" else "Daily Workout Reminder",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isBn) "নির্দিষ্ট সময়ে অনুপ্রেরণামূলক নোটিফিকেশন পান" else "Get a friendly alert at your preferred time",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Switch(
                        checked = enabled,
                        onCheckedChange = { isChecked ->
                            enabled = isChecked
                            if (isChecked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                onSaveReminder(isChecked, hour, minute)
                            }
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = FitElectricCyan)
                    )
                }
            }

            // Time Selector Card
            if (enabled) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = if (isBn) "রিমাইন্ডার পাঠানোর সময়" else "Select Reminder Time",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // Quick Hour Chips
                        val commonHours = listOf(6 to "6:00 AM", 7 to "7:00 AM", 8 to "8:00 AM", 18 to "6:00 PM", 19 to "7:00 PM", 20 to "8:00 PM")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            commonHours.take(3).forEach { (h, label) ->
                                val isSelected = hour == h && minute == 0
                                Surface(
                                    onClick = {
                                        hour = h
                                        minute = 0
                                        onSaveReminder(true, h, 0)
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) FitElectricCyan else MaterialTheme.colorScheme.surface,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 10.dp)
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            commonHours.drop(3).forEach { (h, label) ->
                                val isSelected = hour == h && minute == 0
                                Surface(
                                    onClick = {
                                        hour = h
                                        minute = 0
                                        onSaveReminder(true, h, 0)
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) FitElectricCyan else MaterialTheme.colorScheme.surface,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 10.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Test Notification Button
                OutlinedButton(
                    onClick = {
                        val testIntent = Intent(context, WorkoutReminderReceiver::class.java).apply {
                            action = NotificationScheduler.ACTION_REMINDER
                        }
                        context.sendBroadcast(testIntent)
                        testSentMessage = if (isBn) "টেস্ট নোটিফিকেশন পাঠানো হয়েছে!" else "Test notification sent!"
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isBn) "টেস্ট নোটিফিকেশন পরীক্ষা করুন" else "Test Reminder Notification")
                }

                if (testSentMessage != null) {
                    Text(
                        text = testSentMessage ?: "",
                        color = FitNeonLime,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileSettingsScreen(
    preferences: UserPreferences,
    language: AppLanguage,
    onUpdateLanguage: (AppLanguage) -> Unit,
    onResetData: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val isBn = language == AppLanguage.BENGALI
    var showResetDialog by remember { mutableStateOf(false) }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(if (isBn) "সকল তথ্য রিসেট করুন?" else "Reset All Data?") },
            text = { Text(if (isBn) "আপনি কি নিশ্চিত? এর ফলে আপনার সংরক্ষিত সমস্ত ওয়ার্কআউট রেকর্ড ও পছন্দ মুছে যাবে।" else "Are you sure? This will permanently delete your workout history and custom preferences.") },
            confirmButton = {
                TextButton(onClick = {
                    onResetData()
                    showResetDialog = false
                }) {
                    Text(if (isBn) "রিসেট করুন" else "Reset", color = Color(0xFFFF5252))
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(if (isBn) "বাতিল" else "Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            FitLifeTopBar(
                title = if (isBn) "প্রোফাইল ও সেটিংস" else "Profile & Settings"
            )
        },
        bottomBar = {
            FitLifeBottomNavigation(
                currentRoute = "profile_settings",
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Profile Info Header Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .background(FitElectricCyan.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = FitElectricCyan,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (preferences.displayName.isNotBlank()) preferences.displayName else "FitLife Pro User",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${preferences.targetMinutes} ${if (isBn) "মিনিট দৈনিক লক্ষ্য" else "mins daily target"}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        OutlinedButton(
                            onClick = { onNavigate("user_profile") },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(if (isBn) "সম্পাদনা" else "Edit", fontSize = 12.sp)
                        }
                    }
                }
            }

            // Settings Options Group
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        SettingsRow(
                            icon = Icons.Default.Language,
                            title = if (isBn) "ভাষা (Language)" else "Language (ভাষা)",
                            subtitle = if (isBn) "বাংলা (সক্রিয়)" else "English (Active)",
                            onClick = { onNavigate("language_selection") }
                        )
                        SettingsRow(
                            icon = Icons.Default.Alarm,
                            title = if (isBn) "ওয়ার্কআউট রিমাইন্ডার" else "Workout Reminders",
                            subtitle = if (preferences.isReminderEnabled) "${preferences.reminderHour}:00" else "Off",
                            onClick = { onNavigate("reminder_settings") }
                        )
                    }
                }
            }

            // Legal & About Owner Group
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        SettingsRow(
                            icon = Icons.Default.Info,
                            title = if (isBn) "অ্যাপের তথ্য ও স্বত্বাধিকারী" else "About & Owner Information",
                            subtitle = "Md Jahangir Alam • v${AppConfig.VERSION_NAME}",
                            onClick = { onNavigate("about_owner") }
                        )
                        SettingsRow(
                            icon = Icons.Default.PrivacyTip,
                            title = if (isBn) "গোপনীয়তা নীতি" else "Privacy Policy",
                            subtitle = if (isBn) "স্বচ্ছ ও নিরাপদ নীতিমালা" else "Zero-tracking transparency",
                            onClick = { onNavigate("privacy_policy") }
                        )
                        SettingsRow(
                            icon = Icons.Default.Help,
                            title = if (isBn) "সহায়তা ও যোগাযোগ" else "Help & Support",
                            subtitle = if (isBn) "প্রায়শ জিজ্ঞাসিত প্রশ্ন ও হেল্প" else "FAQ & Contact Support",
                            onClick = { onNavigate("help_support") }
                        )
                    }
                }
            }

            // Reset Data Button
            item {
                Button(
                    onClick = { showResetDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252).copy(alpha = 0.15f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isBn) "সমস্ত লোকাল ডেটা রিসেট করুন" else "Reset All Local Data",
                        color = Color(0xFFFF5252),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = FitElectricCyan,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun AboutOwnerScreen(
    language: AppLanguage,
    onBackClick: () -> Unit
) {
    val isBn = language == AppLanguage.BENGALI
    val context = LocalContext.current

    Scaffold(
        topBar = {
            FitLifeTopBar(
                title = if (isBn) "অ্যাপের তথ্য ও নির্মাতা" else "About & Owner Information",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // App Branding Banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = AppConfig.APP_NAME,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isBn) AppConfig.APP_TAGLINE_BN else AppConfig.APP_TAGLINE_EN,
                        fontSize = 13.sp,
                        color = FitElectricCyan,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Version ${AppConfig.VERSION_NAME} (Build ${AppConfig.VERSION_CODE})",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Dedicated Owner & Creator Attribution Section
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = if (isBn) "স্বত্বাধিকারী ও নির্মাতা পরিচিতি" else "Owner & Creator Attribution",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    InfoRow(
                        label = if (isBn) "অ্যাপের স্বত্বাধিকারী (Owner)" else "App Owner",
                        value = AppConfig.APP_OWNER
                    )
                    InfoRow(
                        label = if (isBn) "অ্যাপ নির্মাতা (Creator & Developer)" else "App Creator",
                        value = AppConfig.APP_CREATOR
                    )
                    InfoRow(
                        label = if (isBn) "ইমেইল যোগাযোগ" else "Contact Email",
                        value = AppConfig.OWNER_EMAIL,
                        isClickable = true,
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:${AppConfig.OWNER_EMAIL}")
                            }
                            context.startActivity(intent)
                        }
                    )
                    InfoRow(
                        label = if (isBn) "ফোন নম্বর" else "Contact Phone",
                        value = AppConfig.OWNER_PHONE,
                        isClickable = true,
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${AppConfig.OWNER_PHONE}")
                            }
                            context.startActivity(intent)
                        }
                    )
                    InfoRow(
                        label = if (isBn) "প্রাথমিক বাজার" else "Initial Target Market",
                        value = AppConfig.INITIAL_TARGET_MARKET
                    )
                    InfoRow(
                        label = if (isBn) "ভাষা সমর্থন" else "Supported Languages",
                        value = "Bengali (বাংলা) & English"
                    )
                }
            }

            // Copyright & Transparency Notice
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = AppConfig.COPYRIGHT_NOTICE,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isBn) "বিজ্ঞপ্তি: এই অ্যাপ্লিকেশনটির মূল বিষয়বস্তু এবং রূপরেখা স্বত্বাধিকারী কর্তৃক পরিচালিত। ফিটলাইফ প্রো স্বাধীনভাবে প্রস্তুতকৃত একটি অ্যাপ্লিকেশন এবং এতে কোনো তৃতীয় পক্ষের কপিরাইটযুক্ত ট্রেডমার্কের সংশ্লিষ্টতা নেই।"
                        else "Legal Disclosure: This application is developed and published under owner attribution. FitLife Pro is an independent fitness platform and does not claim unregistered trademark rights or unauthorized third-party brand affiliation.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    isClickable: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = if (isClickable && onClick != null) Modifier.clickable { onClick() } else Modifier
    ) {
        Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isClickable) FitElectricCyan else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun PrivacyPolicyScreen(
    language: AppLanguage,
    onBackClick: () -> Unit
) {
    val isBn = language == AppLanguage.BENGALI

    Scaffold(
        topBar = {
            FitLifeTopBar(
                title = if (isBn) "গোপনীয়তা নীতি" else "Privacy Policy",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = if (isBn) "ফিটলাইফ প্রো গোপনীয়তা ও ডেটা সুরক্ষা নীতি" else "FitLife Pro Privacy & Data Protection Policy",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Last updated: October 2026 • Effective for Bangladesh & International Users",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            PolicySection(
                title = if (isBn) "১. ডেটা সংগ্রহ ও লোকাল স্টোরেজ" else "1. Local Data Storage & Privacy",
                content = if (isBn) "ফিটলাইফ প্রো ব্যবহারকারীদের গোপনীয়তাকে সর্বাধিক গুরুত্ব দেয়। আপনার সম্পাদিত ওয়ার্কআউট ইতিহাস, পছন্দসমূহ এবং অগ্রগতি সম্পূর্ণরূপে আপনার ডিভাইসের লোকাল ডাটাবেজে (Room Database) সংরক্ষিত থাকে। আমরা কোনো গোপন ট্র্যাকিং বা অযাচিত ব্যক্তিগত ডেটা সংগ্রহ করি না।"
                else "FitLife Pro operates on an offline-first architecture. Your completed workout records, streaks, and preferences are saved strictly on your local device via Android Room database. No secret profiling, telemetry, or personal health records are uploaded without explicit user authorization."
            )

            PolicySection(
                title = if (isBn) "২. ডিভাইসের অনুমতির স্বচ্ছতা" else "2. Permission Transparency",
                content = if (isBn) "অ্যাপটি শুধুমাত্র প্রয়োজনীয় কার্যকারিতার জন্য ন্যূনতম অনুমতি গ্রহণ করে:\n• INTERNET: অনুমোদিত ভিডিও প্রদর্শন ও স্ট্রিমিং নিশ্চিত করার জন্য।\n• POST_NOTIFICATIONS: আপনার নির্বাচিত দৈনিক ব্যায়ামের অনুস্মারক পাঠানোর জন্য (সম্পূর্ণ ঐচ্ছিক)।\n• VIBRATE: ব্যায়ামের সময় গণনার ক্ষেত্রে সতর্কতা প্রদানের জন্য।"
                else "The application requests only least-privilege permissions:\n• INTERNET: To stream verified educational workout demonstration videos.\n• POST_NOTIFICATIONS: To deliver optional daily reminders chosen by you.\n• VIBRATE: For gentle haptic feedback during workout transition intervals."
            )

            PolicySection(
                title = if (isBn) "৩. বিজ্ঞাপন ও তৃতীয় পক্ষ" else "3. No Intrusive Advertising",
                content = if (isBn) "ফিটলাইফ প্রো-তে কোনো বিভ্রান্তিকর বা অযাচিত বিজ্ঞাপন নেই। আপনার ফিটনেস যাত্রাকে নিরাপদ ও নির্বিঘ্ন রাখতে এটি সম্পূর্ণ পরিচ্ছন্ন ও নিরাপদ।"
                else "The core app does not include intrusive third-party ad networks or hidden trackers. Your focus remains entirely on your health."
            )

            PolicySection(
                title = if (isBn) "৪. যোগাযোগ ও ডেটা মুছে ফেলা" else "4. Contact & Data Deletion",
                content = if (isBn) "যেকোনো সময় সেটিংস থেকে 'সকল লোকাল ডেটা রিসেট করুন' বাটনে ক্লিক করে সমস্ত সংরক্ষিত তথ্য মুছে ফেলতে পারবেন। কোনো প্রশ্ন থাকলে স্বত্বাধিকারী মোঃ জাহাঙ্গীর আলম-এর সাথে সরাসরি যোগাযোগ করুন:\nইমেইল: mdjahangiralam2528@gmail.com\nফোন: +8801778600028"
                else "You may erase all local data at any time via Settings -> Reset All Local Data. For any data inquiries, contact the app owner Md Jahangir Alam directly at mdjahangiralam2528@gmail.com or +8801778600028."
            )
        }
    }
}

@Composable
private fun PolicySection(title: String, content: String) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = FitElectricCyan
            )
            Text(
                text = content,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun HelpSupportScreen(
    language: AppLanguage,
    onBackClick: () -> Unit
) {
    val isBn = language == AppLanguage.BENGALI
    val context = LocalContext.current

    Scaffold(
        topBar = {
            FitLifeTopBar(
                title = if (isBn) "সহায়তা ও প্রায়শ জিজ্ঞাসিত প্রশ্ন" else "Help & Support",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (isBn) "প্রায়শ জিজ্ঞাসিত প্রশ্নাবলী (FAQ)" else "Frequently Asked Questions",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            FaqItem(
                question = if (isBn) "১. ভিডিও কি অফলাইনে কাজ করবে?" else "1. Does video work offline?",
                answer = if (isBn) "হ্যাঁ! আপনি যেকোনো ব্যায়ামের বিবরণ স্ক্রিনে গিয়ে 'অফলাইনের জন্য সেভ করুন' চাপলে ভিডিওটি ডাউনলোড ফোল্ডারে সংরক্ষিত হবে এবং ইন্টারনেট ছাড়াই চালানো যাবে।"
                else "Yes! Save any exercise from its detail view for offline access. Downloaded videos play without requiring active internet connectivity."
            )

            FaqItem(
                question = if (isBn) "২. ব্যায়াম করার সঠিক নিয়ম কী?" else "2. What is the safest workout progression?",
                answer = if (isBn) "সর্বদা প্রথমে ৩-৫ মিনিটের হালকা ওয়ার্ম-আপ বা স্ট্রেচিং করুন। ব্যায়ামের সময় শ্বাস স্বাভাবিক রাখুন এবং কোনো ব্যথা অনুভব করলে সাথে সাথে বিরতি নিন।"
                else "Always perform a light 3-5 minute warm-up first. Maintain steady breathing and stop immediately if you feel joint pain or dizziness."
            )

            FaqItem(
                question = if (isBn) "৩. কিশোর-কিশোরীদের জন্য কি এটি নিরাপদ?" else "3. Is FitLife Pro safe for teenagers?",
                answer = if (isBn) "হ্যাঁ! অ্যাপটিতে রয়েছে বিশেষ 'কিশোরদের নিরাপদ ফিটনেস রুটিন' যা ভারী ওজন বা চরম ব্যায়ামের পরিবর্তে শারীরিক ভঙ্গি, নমনীয়তা এবং স্বাভাবিক সুস্থতা নিশ্চিত করে।"
                else "Yes! FitLife Pro includes dedicated non-extreme Teen Healthy Movement programs tailored for posture and lifelong active habits without adult bodybuilding strain."
            )

            // Direct Support Contact Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = if (isBn) "সরাসরি সহায়তা ও যোগাযোগ" else "Direct Developer & Owner Support",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isBn) "অ্যাপ সম্পর্কিত যেকোনো প্রশ্ন বা মতামতের জন্য স্বত্বাধিকারী মোঃ জাহাঙ্গীর আলম-এর সাথে যোগাযোগ করতে পারেন:"
                        else "For assistance, suggestions, or licensing inquiries, contact Md Jahangir Alam directly:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:${AppConfig.OWNER_EMAIL}")
                                }
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = FitElectricCyan),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Email, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Email", color = Color.Black, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${AppConfig.OWNER_PHONE}")
                                }
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = FitNeonLime),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Call", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FaqItem(question: String, answer: String) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = question, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
            Text(text = answer, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 18.sp)
        }
    }
}
