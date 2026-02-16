package com.librarix.presentation.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Dark mode preference: null = follow system, true = force dark, false = force light
class DarkModePreference {
    var override = mutableStateOf<Boolean?>(null)
}

val LocalDarkModePreference = compositionLocalOf { DarkModePreference() }

// Provides the resolved isDark boolean to all screens - use this instead of isSystemInDarkTheme()
val LocalIsDarkTheme = compositionLocalOf { false }

private val DarkColorScheme = darkColorScheme(
    primary = LxPrimary,
    onPrimary = White,
    primaryContainer = LxPrimaryLight,
    onPrimaryContainer = White,
    secondary = LxAccentGold,
    onSecondary = Black,
    background = LxBackgroundDark,
    onBackground = White,
    surface = LxSurfaceDark,
    onSurface = White,
    surfaceVariant = LxAuthorCardDark,
    onSurfaceVariant = LxTextSecondary,
    outline = LxBorderDark,
    outlineVariant = LxBorderDark.copy(alpha = 0.5f),
    error = Color(0xFFCF6679),
    onError = Black
)

private val LightColorScheme = lightColorScheme(
    primary = LxPrimary,
    onPrimary = White,
    primaryContainer = LxPrimaryLight,
    onPrimaryContainer = White,
    secondary = LxAccentGold,
    onSecondary = Black,
    background = LxBackgroundLight,
    onBackground = Color(0xFF1C1B1F),
    surface = LxSurfaceLight,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFF3F4F6),
    onSurfaceVariant = LxTextSecondary,
    outline = LxBorderLight,
    outlineVariant = LxBorderLight.copy(alpha = 0.5f),
    error = Color(0xFFB00020),
    onError = White
)

@Composable
fun LibrarixTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    darkModeOverride: Boolean? = null,
    content: @Composable () -> Unit
) {
    val isDark = darkModeOverride ?: darkTheme
    val colorScheme = if (isDark) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !isDark
        }
    }

    CompositionLocalProvider(LocalIsDarkTheme provides isDark) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
