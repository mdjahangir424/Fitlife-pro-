package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = FitElectricCyan,
    onPrimary = Color(0xFF00363D),
    primaryContainer = Color(0xFF004F58),
    onPrimaryContainer = Color(0xFF9CF0FF),
    secondary = FitNeonLime,
    onSecondary = Color(0xFF1B3700),
    secondaryContainer = Color(0xFF285000),
    onSecondaryContainer = Color(0xFFB4FF6F),
    tertiary = FitCoralFlame,
    onTertiary = Color.White,
    background = FitDarkBackground,
    onBackground = FitTextPrimaryDark,
    surface = FitDarkSurface,
    onSurface = FitTextPrimaryDark,
    surfaceVariant = FitDarkSurfaceVariant,
    onSurfaceVariant = FitTextSecondaryDark,
    outline = FitDarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006875),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF9CF0FF),
    onPrimaryContainer = Color(0xFF001F24),
    secondary = Color(0xFF386B00),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB4FF6F),
    onSecondaryContainer = Color(0xFF0E2000),
    tertiary = Color(0xFFB32626),
    onTertiary = Color.White,
    background = FitLightBackground,
    onBackground = FitTextPrimaryLight,
    surface = FitLightSurface,
    onSurface = FitTextPrimaryLight,
    surfaceVariant = FitLightSurfaceVariant,
    onSurfaceVariant = FitTextSecondaryLight,
    outline = FitLightBorder
)

@Composable
fun FitLifeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    FitLifeTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
