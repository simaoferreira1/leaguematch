package com.leaguematch.ui.components

import androidx.compose.runtime.compositionLocalOf
import com.leaguematch.data.repository.TranslationRepository
import com.leaguematch.translations.Language

val LocalLanguage = compositionLocalOf { Language.PT }

val LocalTranslationRepository = compositionLocalOf<TranslationRepository?> { null }