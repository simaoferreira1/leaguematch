package com.leaguematch.ui.organizer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class MatchEventType(
    val label: String,
    val icon: ImageVector
) {
    GOLO("Golo", Icons.Default.SportsSoccer),
    FALTA("Falta", Icons.Default.Warning),
    AMARELO("Cartão amarelo", Icons.Default.Warning),
    VERMELHO("Cartão vermelho", Icons.Default.Report),
    CANTO("Canto", Icons.Default.Flag),
    SUBSTITUICAO("Substituição", Icons.Default.SwapHoriz),

    DOIS_PONTOS("2 pontos", Icons.Default.SportsBasketball),
    TRES_PONTOS("3 pontos", Icons.Default.SportsBasketball),
    LANCE_LIVRE("Lance livre", Icons.Default.SportsBasketball),

    ACE("Ace", Icons.Default.SportsTennis),
    BREAK_POINT("Break point", Icons.Default.Bolt)
}

fun eventosPorModalidade(modalidade: String): List<MatchEventType> {
    return when (modalidade.trim().lowercase()) {
        "futebol" -> listOf(
            MatchEventType.GOLO,
            MatchEventType.FALTA,
            MatchEventType.AMARELO,
            MatchEventType.VERMELHO,
            MatchEventType.CANTO,
            MatchEventType.SUBSTITUICAO
        )

        "andebol" -> listOf(
            MatchEventType.GOLO,
            MatchEventType.FALTA,
            MatchEventType.AMARELO,
            MatchEventType.VERMELHO
        )

        "basquetebol" -> listOf(
            MatchEventType.DOIS_PONTOS,
            MatchEventType.TRES_PONTOS,
            MatchEventType.LANCE_LIVRE,
            MatchEventType.FALTA
        )

        "padel" -> listOf(
            MatchEventType.ACE,
            MatchEventType.BREAK_POINT,
            MatchEventType.FALTA
        )

        else -> listOf(
            MatchEventType.GOLO,
            MatchEventType.FALTA
        )
    }
}

fun modalidadeUsaPosseBola(modalidade: String): Boolean {
    return modalidade.trim().lowercase() in listOf(
        "futebol",
        "andebol",
        "basquetebol"
    )
}