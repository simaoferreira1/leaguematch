package com.leaguematch.ui.components

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import com.leaguematch.translations.Language
import com.leaguematch.ui.components.TranslatedText

@Composable
fun TranslatedText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    style: TextStyle = LocalTextStyle.current,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified
) {
    val language = LocalLanguage.current
    val translationRepository = LocalTranslationRepository.current

    var displayedText by remember(text, language) {
        mutableStateOf(text)
    }

    LaunchedEffect(text, language) {
        displayedText = if (language == Language.EN && translationRepository != null) {
            translationRepository.translateText(
                text = text,
                targetLang = "EN"
            )
        } else {
            text
        }
    }

    Text(
        text = displayedText,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        style = style,
        letterSpacing = letterSpacing,
        lineHeight = lineHeight
    )
}