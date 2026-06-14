/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: TranslationProvider.kt
 * Tipo: Componente Visual Reutilizável
 *
 * Descrição:
 * Este ficheiro define um componente personalizado e reutilizável em Jetpack Compose.\n * É partilhado entre vários ecrãs para manter a consistência visual (botões, listas, caixas de diálogo, etc.).
 */
package com.leaguematch.ui.components // Define o pacote deste ficheiro de código

import androidx.compose.runtime.compositionLocalOf // Importa dependência / biblioteca necessária
import com.leaguematch.data.repository.TranslationRepository // Importa dependência / biblioteca necessária
import com.leaguematch.translations.Language // Importa dependência / biblioteca necessária

val LocalLanguage = compositionLocalOf { Language.PT } // Declara constante local (leitura única)

val LocalTranslationRepository = compositionLocalOf<TranslationRepository?> { null } // Declara constante local (leitura única)
