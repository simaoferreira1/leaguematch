package com.leaguematch.data.remote.model

data class ConfiguracaoNotificacoes(
    val utilizadorId: Int,

    val notificacoesJogos: Boolean = true,
    val notificacoesGolos: Boolean = true,
    val notificacoesCartoes: Boolean = false,
    val notificacoesFimPartida: Boolean = true,

    val somNotificacao: Boolean = true,

    val futebol: Boolean = true,
    val tenis: Boolean = false,
    val basquetebol: Boolean = true,
    val andebol: Boolean = false
)