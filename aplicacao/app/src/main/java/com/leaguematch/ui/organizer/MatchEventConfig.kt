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

        "voleibol", "volei" -> listOf(
            MatchEventType.ACE,
            MatchEventType.FALTA
        )

        "futsal" -> listOf(
            MatchEventType.GOLO,
            MatchEventType.FALTA,
            MatchEventType.AMARELO
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

data class EstatisticaInicial(
    val titulo: String,
    val casa: Int,
    val fora: Int
)

fun estatisticasPorModalidade(modalidade: String): List<EstatisticaInicial> {
    val lista = mutableListOf<EstatisticaInicial>()

    if (modalidadeUsaPosseBola(modalidade)) {
        lista.add(EstatisticaInicial("Posse de Bola", 50, 50))
    }

    when (modalidade.trim().lowercase()) {
        "futebol" -> {
            lista.add(EstatisticaInicial("Remates", 0, 0))
            lista.add(EstatisticaInicial("Remates à baliza", 0, 0))
            lista.add(EstatisticaInicial("Cantos", 0, 0))
            lista.add(EstatisticaInicial("Faltas", 0, 0))
            lista.add(EstatisticaInicial("Cartões amarelos", 0, 0))
            lista.add(EstatisticaInicial("Cartões vermelhos", 0, 0))
        }

        "andebol" -> {
            lista.add(EstatisticaInicial("Remates", 0, 0))
            lista.add(EstatisticaInicial("Defesas", 0, 0))
            lista.add(EstatisticaInicial("Faltas", 0, 0))
            lista.add(EstatisticaInicial("Cartões amarelos", 0, 0))
            lista.add(EstatisticaInicial("Cartões vermelhos", 0, 0))
        }

        "basquetebol" -> {
            lista.add(EstatisticaInicial("Lançamentos 2 pts", 0, 0))
            lista.add(EstatisticaInicial("Lançamentos 3 pts", 0, 0))
            lista.add(EstatisticaInicial("Lances livres", 0, 0))
            lista.add(EstatisticaInicial("Faltas", 0, 0))
        }

        "padel" -> {
            lista.add(EstatisticaInicial("Aces", 0, 0))
            lista.add(EstatisticaInicial("Break points", 0, 0))
            lista.add(EstatisticaInicial("Erros", 0, 0))
        }

        "ténis", "tenis" -> {
            lista.add(EstatisticaInicial("Aces", 0, 0))
            lista.add(EstatisticaInicial("Break points", 0, 0))
            lista.add(EstatisticaInicial("Erros", 0, 0))
        }

        "voleibol", "volei" -> {
            lista.add(EstatisticaInicial("Aces", 0, 0))
            lista.add(EstatisticaInicial("Blocos", 0, 0))
            lista.add(EstatisticaInicial("Ataques", 0, 0))
            lista.add(EstatisticaInicial("Erros", 0, 0))
        }

        "futsal" -> {
            lista.add(EstatisticaInicial("Remates", 0, 0))
            lista.add(EstatisticaInicial("Remates à baliza", 0, 0))
            lista.add(EstatisticaInicial("Faltas", 0, 0))
            lista.add(EstatisticaInicial("Cartões amarelos", 0, 0))
        }

        "rugby" -> {
            lista.add(EstatisticaInicial("Ensaios", 0, 0))
            lista.add(EstatisticaInicial("Penalidades", 0, 0))
            lista.add(EstatisticaInicial("Conversões", 0, 0))
            lista.add(EstatisticaInicial("Faltas", 0, 0))
        }

        else -> {
            lista.add(EstatisticaInicial("Faltas", 0, 0))
            lista.add(EstatisticaInicial("Cartões", 0, 0))
        }
    }

    return lista
}