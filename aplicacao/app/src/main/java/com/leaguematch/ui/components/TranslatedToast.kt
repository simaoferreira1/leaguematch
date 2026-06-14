/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: TranslatedToast.kt
 * Tipo: Componente Visual Reutilizável
 *
 * Descrição:
 * Este ficheiro define um componente personalizado e reutilizável em Jetpack Compose.\n * É partilhado entre vários ecrãs para manter a consistência visual (botões, listas, caixas de diálogo, etc.).
 */
package com.leaguematch.ui.components // Define o pacote deste ficheiro de código

import android.content.Context // Importa dependência / biblioteca necessária
import android.widget.Toast // Importa dependência / biblioteca necessária
import com.leaguematch.data.repository.TranslationRepository // Importa dependência / biblioteca necessária
import com.leaguematch.translations.Language // Importa dependência / biblioteca necessária

suspend fun showTranslatedToast( // Declaração de função / método de lógica
    context: Context,
    text: String,
    language: Language,
    translationRepository: TranslationRepository?,
    duration: Int = Toast.LENGTH_SHORT
) {
    val message = // Declara constante local (leitura única)
        if (language == Language.EN && translationRepository != null) { // Estrutura de decisão condicional principal
            translationRepository.translateText(
                text = text,
                targetLang = "EN"
            )
        } else { // Fluxo condicional alternativo caso o 'if' seja falso
            text
        }

    Toast.makeText(
        context,
        message,
        duration
    ).show()
}