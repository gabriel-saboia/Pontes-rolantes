package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val IndustrialDarkColorScheme = darkColorScheme(
    primary = CatYellow,
    onPrimary = CatOnYellow,
    primaryContainer = CatYellow,
    onPrimaryContainer = CatOnYellow,
    inversePrimary = CatYellowDim,
    secondary = CatYellowLight,
    onSecondary = CatOnYellow,
    background = CatDarkBackground,
    onBackground = CatTextPrimary,
    surface = CatDarkSurface,
    onSurface = CatTextPrimary,
    surfaceVariant = CatSurfaceHighest,
    onSurfaceVariant = CatTextSecondary,
    surfaceContainerLowest = CatSurfaceLowest,
    surfaceContainerLow = CatSurfaceLow,
    surfaceContainer = CatSurfaceContainer,
    surfaceContainerHigh = CatSurfaceHigh,
    surfaceContainerHighest = CatSurfaceHighest,
    surfaceBright = CatSurfaceBright,
    outline = CatOutline,
    outlineVariant = CatOutline,
    error = CatCriticalRed,
    onError = CatOnCriticalRed,
    errorContainer = CatCriticalRedContainer,
    onErrorContainer = CatOnCriticalRed
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = IndustrialDarkColorScheme,
        typography = Typography,
        content = content
    )
}
