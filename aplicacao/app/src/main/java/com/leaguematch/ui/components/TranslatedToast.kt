package com.leaguematch.ui.components

import android.content.Context
import android.widget.Toast
import com.leaguematch.data.repository.TranslationRepository
import com.leaguematch.translations.Language

suspend fun showTranslatedToast(
    context: Context,
    text: String,
    language: Language,
    translationRepository: TranslationRepository?,
    duration: Int = Toast.LENGTH_SHORT
) {
    val message =
        if (language == Language.EN && translationRepository != null) {
            translationRepository.translateText(
                text = text,
                targetLang = "EN"
            )
        } else {
            text
        }

    Toast.makeText(
        context,
        message,
        duration
    ).show()
}