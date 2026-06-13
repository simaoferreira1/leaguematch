package com.leaguematch.ui.organizer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class MatchEventType(
    val label: String,
    val icon: ImageVector,
    val requerJogador: Boolean = true // Por defeito quase todos precisam de jogador
) {
    GOLO("Golo", Icons.Default.SportsSoccer),
    FALTA("Falta", Icons.Default.Warning),
    CARTAO_AMARELO("Cartão amarelo", Icons.Default.Warning),
    CARTAO_VERMELHO("Cartão vermelho", Icons.Default.Report),

    // requerJogador = false permite aplicar o canto apenas à equipa sem dar erro de validação
    CANTO("Canto", Icons.Default.Flag, requerJogador = false),

    // Para a substituição, a UI deve permitir escolher quem sai e quem entra
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
            MatchEventType.CARTAO_AMARELO,
            MatchEventType.CARTAO_VERMELHO,
            MatchEventType.CANTO,
            MatchEventType.SUBSTITUICAO
        )

        "andebol" -> listOf(
            MatchEventType.GOLO,
            MatchEventType.FALTA,
            MatchEventType.CARTAO_AMARELO,
            MatchEventType.CARTAO_VERMELHO
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

        "ténis", "tenis" -> listOf(
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
            MatchEventType.CARTAO_AMARELO,
            MatchEventType.CARTAO_VERMELHO,
            MatchEventType.SUBSTITUICAO
        )

        "rugby" -> listOf(
            MatchEventType.GOLO,
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

data class EstatisticaInicial(
    val titulo: String,
    val casa: Int,
    val fora: Int,
    val isManual: Boolean = true // Se true: mostra + e -. Se false: conta eventos da BD.
)

fun estatisticasPorModalidade(modalidade: String): List<EstatisticaInicial> {
    val lista = mutableListOf<EstatisticaInicial>()

    when (modalidade.trim().lowercase()) {
        "futebol" -> {
            lista.add(EstatisticaInicial("Remates", 0, 0, isManual = true))
            lista.add(EstatisticaInicial("Remates à baliza", 0, 0, isManual = true))
            lista.add(EstatisticaInicial("Cantos", 0, 0, isManual = false))
            lista.add(EstatisticaInicial("Faltas", 0, 0, isManual = false))
            lista.add(EstatisticaInicial("Cartões amarelos", 0, 0, isManual = false))
            lista.add(EstatisticaInicial("Cartões vermelhos", 0, 0, isManual = false))
        }

        "andebol" -> {
            lista.add(EstatisticaInicial("Remates", 0, 0, isManual = true))
            lista.add(EstatisticaInicial("Defesas", 0, 0, isManual = true))
            lista.add(EstatisticaInicial("Faltas", 0, 0, isManual = false))
            lista.add(EstatisticaInicial("Cartões amarelos", 0, 0, isManual = false))
            lista.add(EstatisticaInicial("Cartões vermelhos", 0, 0, isManual = false))
        }

        "basquetebol" -> {
            lista.add(EstatisticaInicial("Lançamentos 2 pts", 0, 0, isManual = true))
            lista.add(EstatisticaInicial("Lançamentos 3 pts", 0, 0, isManual = true))
            lista.add(EstatisticaInicial("Lances livres", 0, 0, isManual = true))
            lista.add(EstatisticaInicial("Faltas", 0, 0, isManual = false))
        }

        "padel", "ténis", "tenis" -> {
            lista.add(EstatisticaInicial("Aces", 0, 0, isManual = false))
            lista.add(EstatisticaInicial("Break points", 0, 0, isManual = false))
            lista.add(EstatisticaInicial("Erros", 0, 0, isManual = true))
        }

        "voleibol", "volei" -> {
            lista.add(EstatisticaInicial("Aces", 0, 0, isManual = false))
            lista.add(EstatisticaInicial("Blocos", 0, 0, isManual = true))
            lista.add(EstatisticaInicial("Ataques", 0, 0, isManual = true))
            lista.add(EstatisticaInicial("Erros", 0, 0, isManual = true))
        }

        "futsal" -> {
            lista.add(EstatisticaInicial("Remates", 0, 0, isManual = true))
            lista.add(EstatisticaInicial("Remates à baliza", 0, 0, isManual = true))
            lista.add(EstatisticaInicial("Faltas", 0, 0, isManual = false))
            lista.add(EstatisticaInicial("Cartões amarelos", 0, 0, isManual = false))
        }

        "rugby" -> {
            lista.add(EstatisticaInicial("Ensaios", 0, 0, isManual = true))
            lista.add(EstatisticaInicial("Penalidades", 0, 0, isManual = true))
            lista.add(EstatisticaInicial("Conversões", 0, 0, isManual = true))
            lista.add(EstatisticaInicial("Faltas", 0, 0, isManual = false))
        }

        else -> {
            lista.add(EstatisticaInicial("Faltas", 0, 0, isManual = false))
            lista.add(EstatisticaInicial("Cartões", 0, 0, isManual = false))
        }
    }

    return lista
}