package com.leaguematch.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

object BrandTheme {
    var primaryColor by mutableStateOf(Color(0xFFE31734))
}

private fun Color.darken(factor: Float): Color {
    return Color(
        red = (red * (1f - factor)).coerceIn(0f, 1f),
        green = (green * (1f - factor)).coerceIn(0f, 1f),
        blue = (blue * (1f - factor)).coerceIn(0f, 1f),
        alpha = alpha
    )
}

// Old variables for backward compatibility during migration
val RedPrimary: Color get() = BrandTheme.primaryColor
val RedDark = Color(0xFF18181B)
val DarkBackground = Color(0xFF0A0A0B)
val SurfaceDark = Color(0xFF18181B)
val CardGradient = Brush.verticalGradient(
    colors = listOf(
        RedPrimary,
        RedPrimary,
        RedDark
    )
)

// Brand
val LMRed: Color get() = BrandTheme.primaryColor
val LMRed600: Color get() = BrandTheme.primaryColor.darken(0.15f)
val LMRed700: Color get() = BrandTheme.primaryColor.darken(0.30f)
val LMRed50: Color get() = BrandTheme.primaryColor.copy(alpha = 0.12f)
val LMRed100: Color get() = BrandTheme.primaryColor.copy(alpha = 0.25f)

// Neutrals
val LMInk     = Color(0xFF0A0A0B)
val LMInk2    = Color(0xFF18181B)
val LMInk3    = Color(0xFF27272A)
val LMGray900 = Color(0xFF1F1F22)
val LMGray700 = Color(0xFF3F3F46)
val LMGray600 = Color(0xFF52525B)
val LMGray500 = Color(0xFF71717A)
val LMGray400 = Color(0xFFA1A1AA)
val LMGray300 = Color(0xFFD4D4D8)
val LMGray200 = Color(0xFFE4E4E7)
val LMGray100 = Color(0xFFF4F4F5)
val LMGray50  = Color(0xFFFAFAFA)
val LMWhite   = Color(0xFFFFFFFF)
val LMCream   = Color(0xFFFBFAF7)

// Semantic
val LMLive    = Color(0xFF16A34A)
val LMLiveBg  = Color(0xFFDCFCE7)
val LMWarn    = Color(0xFFF59E0B)
val LMAmberBg = Color(0xFFFEF3C7)
val LMInfo    = Color(0xFF2563EB)

// Surface
val LMBg          = LMGray50
val LMBgSoft      = LMGray100
val LMSurface     = LMWhite
val LMBorder      = LMGray200
val LMBorderStrong = LMGray300
