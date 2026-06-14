/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: Color.kt
 * Tipo: Lógica Utilitária / Auxiliar
 *
 * Descrição:
 * Contém funções utilitárias ou auxiliares transversais à aplicação.
 */
package com.leaguematch.ui.theme // Define o pacote deste ficheiro de código

import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Brush // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária

object BrandTheme { // Declaração de objeto estático / Singleton
    var primaryColor by mutableStateOf(Color(0xFFE31734)) // Declara estado mutável local do Compose
}

private fun Color.darken(factor: Float): Color { // Declaração de função / método de lógica
    return Color( // Retorna o resultado da execução da função
        red = (red * (1f - factor)).coerceIn(0f, 1f),
        green = (green * (1f - factor)).coerceIn(0f, 1f),
        blue = (blue * (1f - factor)).coerceIn(0f, 1f),
        alpha = alpha
    )
}

// Old variables for backward compatibility during migration
val RedPrimary: Color get() = BrandTheme.primaryColor // Declara constante local (leitura única)
val RedDark = Color(0xFF18181B) // Declara constante local (leitura única)
val DarkBackground = Color(0xFF0A0A0B) // Declara constante local (leitura única)
val SurfaceDark = Color(0xFF18181B) // Declara constante local (leitura única)
val CardGradient = Brush.verticalGradient( // Declara constante local (leitura única)
    colors = listOf(
        RedPrimary,
        RedPrimary,
        RedDark
    )
)

// Brand
val LMRed: Color get() = BrandTheme.primaryColor // Declara constante local (leitura única)
val LMRed600: Color get() = BrandTheme.primaryColor.darken(0.15f) // Declara constante local (leitura única)
val LMRed700: Color get() = BrandTheme.primaryColor.darken(0.30f) // Declara constante local (leitura única)
val LMRed50: Color get() = BrandTheme.primaryColor.copy(alpha = 0.12f) // Declara constante local (leitura única)
val LMRed100: Color get() = BrandTheme.primaryColor.copy(alpha = 0.25f) // Declara constante local (leitura única)

// Neutrals
val LMInk     = Color(0xFF0A0A0B) // Declara constante local (leitura única)
val LMInk2    = Color(0xFF18181B) // Declara constante local (leitura única)
val LMInk3    = Color(0xFF27272A) // Declara constante local (leitura única)
val LMGray900 = Color(0xFF1F1F22) // Declara constante local (leitura única)
val LMGray700 = Color(0xFF3F3F46) // Declara constante local (leitura única)
val LMGray600 = Color(0xFF52525B) // Declara constante local (leitura única)
val LMGray500 = Color(0xFF71717A) // Declara constante local (leitura única)
val LMGray400 = Color(0xFFA1A1AA) // Declara constante local (leitura única)
val LMGray300 = Color(0xFFD4D4D8) // Declara constante local (leitura única)
val LMGray200 = Color(0xFFE4E4E7) // Declara constante local (leitura única)
val LMGray100 = Color(0xFFF4F4F5) // Declara constante local (leitura única)
val LMGray50  = Color(0xFFFAFAFA) // Declara constante local (leitura única)
val LMWhite   = Color(0xFFFFFFFF) // Declara constante local (leitura única)
val LMCream   = Color(0xFFFBFAF7) // Declara constante local (leitura única)

// Semantic
val LMLive    = Color(0xFF16A34A) // Declara constante local (leitura única)
val LMLiveBg  = Color(0xFFDCFCE7) // Declara constante local (leitura única)
val LMWarn    = Color(0xFFF59E0B) // Declara constante local (leitura única)
val LMAmberBg = Color(0xFFFEF3C7) // Declara constante local (leitura única)
val LMInfo    = Color(0xFF2563EB) // Declara constante local (leitura única)

// Surface
val LMBg          = LMGray50 // Declara constante local (leitura única)
val LMBgSoft      = LMGray100 // Declara constante local (leitura única)
val LMSurface     = LMWhite // Declara constante local (leitura única)
val LMBorder      = LMGray200 // Declara constante local (leitura única)
val LMBorderStrong = LMGray300 // Declara constante local (leitura única)
