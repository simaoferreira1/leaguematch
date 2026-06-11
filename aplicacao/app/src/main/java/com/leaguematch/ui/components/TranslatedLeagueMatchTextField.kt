package com.leaguematch.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.leaguematch.translations.Language

@Composable
fun TranslatedLeagueMatchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    hint: String? = null,
    isPassword: Boolean = false,
    error: String? = null
) {
    val language = LocalLanguage.current
    val translationRepository = LocalTranslationRepository.current

    var translatedLabel by remember(label, language) {
        mutableStateOf(label)
    }

    var translatedPlaceholder by remember(placeholder, language) {
        mutableStateOf(placeholder)
    }

    var translatedHint by remember(hint, language) {
        mutableStateOf(hint)
    }

    var translatedError by remember(error, language) {
        mutableStateOf(error)
    }

    LaunchedEffect(label, placeholder, hint, error, language) {
        if (language == Language.EN && translationRepository != null) {

            translatedLabel =
                translationRepository.translateText(label, "EN")

            translatedPlaceholder =
                if (placeholder.isNotBlank())
                    translationRepository.translateText(placeholder, "EN")
                else
                    ""

            translatedHint =
                hint?.let {
                    translationRepository.translateText(it, "EN")
                }

            translatedError =
                error?.let {
                    translationRepository.translateText(it, "EN")
                }

        } else {
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