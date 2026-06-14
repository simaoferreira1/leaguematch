/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: Theme.kt
 * Tipo: Lógica Utilitária / Auxiliar
 *
 * Descrição:
 * Contém funções utilitárias ou auxiliares transversais à aplicação.
 */
package com.leaguematch.ui.theme // Define o pacote deste ficheiro de código

import androidx.compose.foundation.isSystemInDarkTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.* // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária

@Composable
fun LeagueMatchTheme( // Declaração de função / método de lógica
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Kept for backward compatibility but ignored for brand consistency
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) { // Estrutura de decisão condicional principal
        darkColorScheme(
            primary = LMRed, onPrimary = LMWhite,
            background = LMInk, onBackground = LMWhite,
            surface = LMInk2, onSurface = LMWhite,
            surfaceVariant = LMInk3,
            outline = LMGray700,
        )
    } else { // Fluxo condicional alternativo caso o 'if' seja falso
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
