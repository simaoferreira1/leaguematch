/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: TranslatedLeagueMatchTextField.kt
 * Tipo: Componente Visual Reutilizável
 *
 * Descrição:
 * Este ficheiro define um componente personalizado e reutilizável em Jetpack Compose.\n * É partilhado entre vários ecrãs para manter a consistência visual (botões, listas, caixas de diálogo, etc.).
 */
package com.leaguematch.ui.components // Define o pacote deste ficheiro de código

import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.LaunchedEffect // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.remember // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária
import com.leaguematch.translations.Language // Importa dependência / biblioteca necessária

@Composable
fun TranslatedLeagueMatchTextField( // Declaração de função / método de lógica
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    hint: String? = null,
    isPassword: Boolean = false,
    error: String? = null
) {
    val language = LocalLanguage.current // Declara constante local (leitura única)
    val translationRepository = LocalTranslationRepository.current // Declara constante local (leitura única)

    var translatedLabel by remember(label, language) { // Memoriza estado para evitar perda durante a recomposição
        mutableStateOf(label) // Declara estado mutável local do Compose
    }

    var translatedPlaceholder by remember(placeholder, language) { // Memoriza estado para evitar perda durante a recomposição
        mutableStateOf(placeholder) // Declara estado mutável local do Compose
    }

    var translatedHint by remember(hint, language) { // Memoriza estado para evitar perda durante a recomposição
        mutableStateOf(hint) // Declara estado mutável local do Compose
    }

    var translatedError by remember(error, language) { // Memoriza estado para evitar perda durante a recomposição
        mutableStateOf(error) // Declara estado mutável local do Compose
    }

    LaunchedEffect(label, placeholder, hint, error, language) { // Efeito colateral Compose: executa código assíncrono ao recompor
        if (language == Language.EN && translationRepository != null) { // Estrutura de decisão condicional principal

            translatedLabel =
                translationRepository.translateText(label, "EN")

            translatedPlaceholder =
                if (placeholder.isNotBlank()) // Estrutura de decisão condicional principal
                    translationRepository.translateText(placeholder, "EN")
                else // Fluxo condicional alternativo caso o 'if' seja falso
                    ""

            translatedHint =
                hint?.let {
                    translationRepository.translateText(it, "EN")
                }

            translatedError =
                error?.let {
                    translationRepository.translateText(it, "EN")
                }

        } else { // Fluxo condicional alternativo caso o 'if' seja falso
            translatedLabel = label
            translatedPlaceholder = placeholder
            translatedHint = hint
            translatedError = error
        }
    }

    LeagueMatchTextField(
        value = value,
        onValueChange = onValueChange,
        label = translatedLabel,
        placeholder = translatedPlaceholder,
        hint = translatedHint,
        isPassword = isPassword,
        error = translatedError
    )
}