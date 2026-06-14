/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: MatchEventConfig.kt
 * Tipo: Interface (Compose View) do Organizador
 *
 * Descrição:
 * Este ficheiro define um ecrã da área do Organizador em Jetpack Compose.\n * Fornece interface e lógica visual para criar torneios, gerir equipas, registar e editar jogos e estatísticas.
 */
package com.leaguematch.ui.organizer // Define o pacote deste ficheiro de código

import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.* // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.vector.ImageVector // Importa dependência / biblioteca necessária

enum class MatchEventType( // Declaração de classe para modelar objetos
    val label: String, // Declara constante local (leitura única)
    val icon: ImageVector, // Declara constante local (leitura única)
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

fun eventosPorModalidade(modalidade: String): List<MatchEventType> { // Declaração de função / método de lógica
    return when (modalidade.trim().lowercase()) { // Escolha múltipla condicional (semelhante a switch-case)
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

        else -> listOf( // Fluxo condicional alternativo caso o 'if' seja falso
            MatchEventType.GOLO,
            MatchEventType.FALTA
        )
    }
}

fun modalidadeUsaPosseBola(modalidade: String): Boolean { // Declaração de função / método de lógica
    return modalidade.trim().lowercase() in listOf( // Retorna o resultado da execução da função
        "futebol",
        "andebol",
        "basquetebol"
    )
}

data class EstatisticaInicial( // Declaração de classe para modelar objetos
    val titulo: String, // Declara constante local (leitura única)
    val casa: Int, // Declara constante local (leitura única)
    val fora: Int, // Declara constante local (leitura única)
    val isManual: Boolean = true // Se true: mostra + e -. Se false: conta eventos da BD.
)

fun estatisticasPorModalidade(modalidade: String): List<EstatisticaInicial> { // Declaração de função / método de lógica
    val lista = mutableListOf<EstatisticaInicial>() // Declara constante local (leitura única)

    when (modalidade.trim().lowercase()) { // Escolha múltipla condicional (semelhante a switch-case)
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

        else -> { // Fluxo condicional alternativo caso o 'if' seja falso
            lista.add(EstatisticaInicial("Faltas", 0, 0, isManual = false))
            lista.add(EstatisticaInicial("Cartões", 0, 0, isManual = false))
        }
    }

    return lista // Retorna o resultado da execução da função
}