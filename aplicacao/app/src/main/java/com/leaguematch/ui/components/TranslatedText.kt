/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: TranslatedText.kt
 * Tipo: Componente Visual Reutilizável
 *
 * Descrição:
 * Este ficheiro define um componente personalizado e reutilizável em Jetpack Compose.\n * É partilhado entre vários ecrãs para manter a consistência visual (botões, listas, caixas de diálogo, etc.).
 */
package com.leaguematch.ui.components // Define o pacote deste ficheiro de código

import androidx.compose.material3.LocalTextStyle // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.runtime.* // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.TextStyle // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontFamily // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.TextUnit // Importa dependência / biblioteca necessária
import com.leaguematch.translations.Language // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária

@Composable
fun TranslatedText( // Declaração de função / método de lógica
    text: String,
    modifier: Modifier = Modifier, // Modificador Compose: Define tamanho, margem, padding ou clique
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    style: TextStyle = LocalTextStyle.current,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified
) {
    val language = LocalLanguage.current // Declara constante local (leitura única)
    val translationRepository = LocalTranslationRepository.current // Declara constante local (leitura única)

    var displayedText by remember(text, language) { // Memoriza estado para evitar perda durante a recomposição
        mutableStateOf(text) // Declara estado mutável local do Compose
    }

    LaunchedEffect(text, language) { // Efeito colateral Compose: executa código assíncrono ao recompor
        displayedText = if (language == Language.EN && translationRepository != null) { // Estrutura de decisão condicional principal
            translationRepository.translateText(
                text = text,
                targetLang = "EN"
            )
        } else { // Fluxo condicional alternativo caso o 'if' seja falso
            text
        }
    }

    Text( // Componente Compose: Desenha texto estruturado no ecrã
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