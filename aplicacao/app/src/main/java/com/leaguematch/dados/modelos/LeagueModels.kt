package com.leaguematch.dados.modelos

enum class TipoUtilizador(val descricao: String) {
    ADMIN("Administrador"),
    ORGANIZADOR("Organizador"),
    PARTICIPANTE("Participante"),
    ESPECTADOR("Espectador")
}

data class Utilizador(
    val id: Int,
    val nome: String,
    val email: String,
    val tipo: TipoUtilizador,
    val ativo: Boolean,
    val equipas: Int,
    val torneios: Int,
    val jogos: Int
)

data class Torneio(
    val id: Int,
    val nome: String,
    val modalidade: String,
    val regras: String,
    val formato: String,
    val estado: String,
    val equipas: Int
)

data class Jogo(
    val id: Int,
    val torneioId: Int,
    val casa: String,
    val fora: String,
    val resultadoCasa: Int,
    val resultadoFora: Int,
    val estado: String
)

data class Goleador(
    val nome: String,
    val golos: Int
)

data class ResumoDashboard(
    val totalUtilizadores: Int,
    val totalTorneios: Int,
    val torneiosEmCurso: Int,
    val alertasSistema: Int
)

data class ResumoModalidade(
    val nome: String,
    val totalTorneios: Int
)

data class EstatisticasAdmin(
    val totalUtilizadores: Int,
    val totalTorneios: Int,
    val totalJogos: Int,
    val alertas: Int,
    val jogosPorPeriodo: List<Float>,
    val torneiosPorEstado: List<ParGrafico>,
    val topTorneios: List<ParGrafico>
)

data class ParGrafico(
    val legenda: String,
    val valorNormalizado: Float
)

data class DetalheTorneio(
    val torneio: Torneio,
    val goleadores: List<Goleador>,
    val jogos: List<Jogo>
) {
    val totalGolos: Int = jogos.sumOf { it.resultadoCasa + it.resultadoFora }
}
