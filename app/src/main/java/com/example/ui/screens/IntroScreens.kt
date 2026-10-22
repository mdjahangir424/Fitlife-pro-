package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.config.AppConfig
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.FitLifeViewModel
import com.example.ui.components.FitLifeTopBar
import com.example.ui.theme.FitElectricCyan
import com.example.ui.theme.FitNeonLime
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    val scale = remember { Animatable(0.7f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(durationMillis = 900)
        )
        delay(1100)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF090D16),
                        Color(0xFF111827),
                        Color(0xFF090D16)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.scale(scale.value)
        ) {
            // App Logo
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFF0F172A))
                    .border(2.dp, FitElectricCyan.copy(alpha = 0.5f), RoundedCornerShape(28.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_fitlife_logo),
                    contentDescription = "FitLife Pro Logo",
                    modifier = Modifier.size(90.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = AppConfig.APP_NAME,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 1.sp
            )

            Text(
                text = AppConfig.APP_TAGLINE_BN,
                fontSize = 14.sp,
                color = FitElectricCyan,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White.copy(alpha = 0.08f)
            ) {
                Text(
                    text = "By ${AppConfig.APP_OWNER}",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun OnboardingScreen(
    language: AppLanguage,
    onContinue: () -> Unit
) {
    val isBn = language == AppLanguage.BENGALI

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Hero Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.hero_fitness_workout),
                contentDescription = "Fitness Hero",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background.copy(alpha = 0.4f),
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
            )
        }

        // Onboarding Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = if (isBn) "প্রফেশনাল ভিডিও ওয়ার্কআউট\nআপনার হাতের মুঠোয়"
                    else "Professional Video Workouts\nIn Your Pocket",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 32.sp
                )

                Text(
                    text = if (isBn) "ভিডিও নির্দেশিকা, ব্যক্তিগত ওয়ার্কআউট প্ল্যান এবং অফলাইন ফিটনেস ট্র্যাকিং — কোনো জিম সরঞ্জামের প্রয়োজন নেই।"
                    else "HD guided exercise videos, personalized daily schedules, and full offline tracking. No expensive gym equipment required.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )

                // Feature Highlights
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FeaturePill(
                        icon = Icons.Default.PlayCircleFilled,
                        title = if (isBn) "আসল ভিডিও" else "Real Video",
                        tint = FitElectricCyan
                    )
                    FeaturePill(
                        icon = Icons.Default.FitnessCenter,
                        title = if (isBn) "১৩ ক্যাটাগরি" else "13 Categories",
                        tint = FitNeonLime
                    )
                    FeaturePill(
                        icon = Icons.Default.Security,
                        title = if (isBn) "১০০% অফলাইন" else "Offline Ready",
                        tint = Color(0xFFFF9100)
                    )
                }
            }

            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("onboarding_get_started_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = if (isBn) "শুরু করুন" else "Get Started",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun FeaturePill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    tint: Color
) {
    Row(
        modifier = Modifier
            .background(tint.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun LanguageSelectionScreen(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onBackClick: (() -> Unit)? = null
) {
    val isBn = currentLanguage == AppLanguage.BENGALI

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        FitLifeTopBar(
            title = if (isBn) "ভাষা নির্বাচন" else "Language Selection",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (isBn) "আপনার সুবিধাজনক ভাষা বেছে নিন:"
                else "Choose your preferred language:",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Bengali Option Card
            Card(
                onClick = { onLanguageSelected(AppLanguage.BENGALI) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (currentLanguage == AppLanguage.BENGALI)
                        FitElectricCyan.copy(alpha = 0.15f)
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (currentLanguage == AppLanguage.BENGALI) 2.dp else 1.dp,
                        color = if (currentLanguage == AppLanguage.BENGALI) FitElectricCyan else Color.Transparent,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .testTag("lang_select_bengali")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "বাংলা (Bengali)",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "বাংলাদেশের ব্যবহারকারীদের জন্য প্রাথমিক ভাষা",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (currentLanguage == AppLanguage.BENGALI) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = FitElectricCyan,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // English Option Card
            Card(
                onClick = { onLanguageSelected(AppLanguage.ENGLISH) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (currentLanguage == AppLanguage.ENGLISH)
                        FitElectricCyan.copy(alpha = 0.15f)
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (currentLanguage == AppLanguage.ENGLISH) 2.dp else 1.dp,
                        color = if (currentLanguage == AppLanguage.ENGLISH) FitElectricCyan else Color.Transparent,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .testTag("lang_select_english")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "English",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "For international and multilingual users",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (currentLanguage == AppLanguage.ENGLISH) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = FitElectricCyan,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserProfileScreen(
    language: AppLanguage,
    initialName: String,
    initialAgeGroup: String,
    initialGoal: String,
    initialTargetMinutes: Int,
    onSaveProfile: (name: String, age: String, goal: String, minutes: Int) -> Unit,
    onBackClick: (() -> Unit)? = null
) {
    val isBn = language == AppLanguage.BENGALI

    var name by remember { mutableStateOf(initialName) }
    var selectedAgeGroup by remember { mutableStateOf(initialAgeGroup) }
    var selectedGoal by remember { mutableStateOf(initialGoal) }
    var targetMinutes by remember { mutableIntStateOf(initialTargetMinutes) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        FitLifeTopBar(
            title = if (isBn) "প্রোফাইল ও পছন্দসমূহ" else "Profile & Fitness Preferences",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Display Name
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = if (isBn) "আপনার নাম (ঐচ্ছিক)" else "Your Name (Optional)",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text(if (isBn) "যেমন: আকাশ আহমেদ" else "e.g. Alex") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_name_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            // Age Group Selection
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (isBn) "বয়সসীমা" else "Age Group",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (isBn) "*কিশোর-কিশোরীদের (১৩-১৭ বছর) জন্য নিরাপদ ও হালকা মুভমেন্ট প্রোগ্রাম নির্ধারিত হবে।"
                    else "*Teenagers (13-17) receive age-appropriate, non-extreme movement routines.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                val ageGroups = listOf(
                    "teen" to if (isBn) "কিশোর/কিশোরী (১৩–১৭ বছর)" else "Teenager (13–17 yrs)",
                    "adult_young" to if (isBn) "তরুণ প্রাপ্তবয়স্ক (১৮–৩৫ বছর)" else "Young Adult (18–35 yrs)",
                    "adult_mature" to if (isBn) "প্রাপ্তবয়স্ক (৩৬–৫০ বছর)" else "Adult (36–50 yrs)",
                    "senior" to if (isBn) "জ্যেষ্ঠ নাগরিক (৫০+ বছর)" else "Senior (50+ yrs)"
                )

                ageGroups.forEach { (key, label) ->
                    Card(
                        onClick = { selectedAgeGroup = key },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedAgeGroup == key)
                                FitElectricCyan.copy(alpha = 0.12f)
                            else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedAgeGroup == key,
                                onClick = { selectedAgeGroup = key },
                                colors = RadioButtonDefaults.colors(selectedColor = FitElectricCyan)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = label,
                                fontSize = 14.sp,
                                fontWeight = if (selectedAgeGroup == key) FontWeight.Bold else FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Fitness Goal Selection
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (isBn) "ফিটনেস লক্ষ্য" else "Fitness Goal",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                val goals = listOf(
                    "general_health" to if (isBn) "সাধারণ সুস্বাস্থ্য ও স্ট্যামিনা" else "General Health & Stamina",
                    "strength" to if (isBn) "শারীরিক শক্তি ও পেশি গঠন" else "Strength & Muscle Tone",
                    "weight_loss" to if (isBn) "স্বাস্থ্যকর ওজন নিয়ন্ত্রণ ও চর্বি হ্রাস" else "Healthy Weight Management",
                    "mobility" to if (isBn) "নমনীয়তা ও সঠিক দেহভঙ্গি" else "Flexibility & Posture"
                )

                goals.forEach { (key, label) ->
                    Card(
                        onClick = { selectedGoal = key },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedGoal == key)
                                FitNeonLime.copy(alpha = 0.12f)
                            else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedGoal == key,
                                onClick = { selectedGoal = key },
                                colors = RadioButtonDefaults.colors(selectedColor = FitNeonLime)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = label,
                                fontSize = 14.sp,
                                fontWeight = if (selectedGoal == key) FontWeight.Bold else FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Target Daily Duration Slider
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isBn) "দৈনিক ব্যায়ামের সময়কাল" else "Target Daily Duration",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "$targetMinutes ${if (isBn) "মিনিট" else "minutes"}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = FitElectricCyan
                    )
                }

                Slider(
                    value = targetMinutes.toFloat(),
                    onValueChange = { targetMinutes = it.toInt() },
                    valueRange = 10f..45f,
                    steps = 6,
                    colors = SliderDefaults.colors(
                        thumbColor = FitElectricCyan,
                        activeTrackColor = FitElectricCyan
                    )
                )
            }

            Button(
                onClick = {
                    onSaveProfile(name, selectedAgeGroup, selectedGoal, targetMinutes)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_profile_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = if (isBn) "সংরক্ষণ করুন" else "Save Preferences",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
