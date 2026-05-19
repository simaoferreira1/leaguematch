package com.leaguematch.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun LeagueMatchTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Kept for backward compatibility but ignored for brand consistency
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = LMRed, onPrimary = LMWhite,
            background = LMInk, onBackground = LMWhite,
            surface = LMInk2, onSurface = LMWhite,
            surfaceVariant = LMInk3,
            outline = LMGray700,
        )
    } else {
        lightColorScheme(
            primary = LMRed, onPrimary = LMWhite,
            background = LMBg, onBackground = LMInk,
            surface = LMSurface, onSurface = LMInk,
            surfaceVariant = LMBgSoft,
            outline = LMBorder, outlineVariant = LMBorderStrong,
            error = LMRed700,
        )
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = LMTypography,
        shapes = LMShapes,
        content = content,
    )
}
