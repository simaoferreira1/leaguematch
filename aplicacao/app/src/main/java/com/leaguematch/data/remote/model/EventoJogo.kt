package com.leaguematch.data.remote.model

data class EventoJogo(
    val id: Int,
    val matchId: Int,
    val tipo: String, // GOLO, FALTA, CARTAO, etc.
    val userId: Int,
    val userName: String,
    val tempo: Int, // Minuto
    val equipa: String // "casa", "fora" ou "center"
)
