package com.skeletonviewer.presentation.theme

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
    primary = Color(0xFF00FF41),
    onPrimary = Color(0xFF003910),
    primaryContainer = Color(0xFF00531B),
    onPrimaryContainer = Color(0xFF72FF82),
    secondary = Color(0xFF52DA7A),
    onSecondary = Color(0xFF003917),
    secondaryContainer = Color(0xFF005227),
    onSecondaryContainer = Color(0xFF6FF794),
    background = Color(0xFF0A0A0A),
    onBackground = Color(0xFFE2E2E2),
    surface = Color(0xFF121212),
    onSurface = Color(0xFFE2E2E2),
    error = Color(0xFFFF4444),
    outline = Color(0xFF8A9A88)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006B27),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF72FF82),
    onPrimaryContainer = Color(0xFF003910),
    secondary = Color(0xFF4B6351),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCEE9D2),
    onSecondaryContainer = Color(0xFF082011),
    background = Color(0xFFF8FBF4),
    onBackground = Color(0xFF191C19),
    surface = Color(0xFFF8FBF4),
    onSurface = Color(0xFF191C19)
)

@Composable
fun SkeletonViewerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
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
