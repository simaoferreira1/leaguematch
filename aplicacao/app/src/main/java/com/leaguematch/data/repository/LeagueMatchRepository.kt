package com.leaguematch.data.repository

import com.leaguematch.data.remote.model.DetalheTorneio
import com.leaguematch.data.remote.model.Equipa
import com.leaguematch.data.remote.model.EstatisticasAdmin
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.data.remote.model.ResumoDashboard
import com.leaguematch.data.remote.model.ResumoModalidade
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.data.remote.model.Classificacao
import com.leaguematch.data.remote.model.EstatisticaJogo
import com.leaguematch.data.remote.model.EventoJogo
import com.leaguematch.ui.spectator.JogoResumoItem
import com.leaguematch.ui.spectator.MelhorMarcadorItem

interface LeagueMatchRepository {
    suspend fun autenticar(email: String, password: String): Utilizador?
    suspend fun registar(nome: String, email: String, password: String, tipo: String): Utilizador?
    suspend fun obterDashboard(): ResumoDashboard
    suspend fun listarUtilizadores(): List<Utilizador>
    suspend fun obterUtilizador(id: Int): Utilizador?
    suspend fun listarModalidades(): List<ResumoModalidade>
    suspend fun listarTorneiosPorModalidade(modalidade: String): List<Torneio>
    suspend fun obterClassificacao(torneioId: Int): List<Classificacao>
    suspend fun obterDetalheTorneio(id: Int): DetalheTorneio?
    suspend fun obterEstatisticasAdmin(): EstatisticasAdmin
    suspend fun atualizarUtilizador(id: Int, nome: String, password: String?): Utilizador?
    suspend fun removerTorneio(id: Int): Boolean
    suspend fun atualizarTorneio(
        id: Int,
        nome: String,
        regras: String,
        formato: String
    ): Torneio?
    suspend fun obterMelhoresMarcadores(torneioId: Int): List<MelhorMarcadorItem>
    suspend fun obterJogosDoTorneio(torneioId: Int): List<JogoResumoItem>
    suspend fun listarJogosAoVivo(): List<Jogo>
    suspend fun listarTodosJogos(): List<Jogo>
    suspend fun obterEstatisticasJogo(partidaId: Int): List<EstatisticaJogo>
    suspend fun obterEventosJogo(partidaId: Int): List<EventoJogo>
    suspend fun criarTorneio(
        nome: String,
        modalidade: String,
        regras: String,
        formato: String,
        organizadorId: Int
    ): Torneio?
    suspend fun listarEquipasTorneio(torneioId: Int): List<Equipa>
    suspend fun criarEquipa(nome: String, torneioId: Int): Equipa?
    suspend fun removerEquipa(equipaId: Int): Boolean
    suspend fun atualizarEquipa(id: Int, nome: String): Equipa?
    suspend fun removerJogo(id: Int): Boolean
    suspend fun atualizarJogo(
        id: Int,
        resultadoCasa: Int,
        resultadoFora: Int,
        estado: String,
        local: String
    ): Jogo?
    suspend fun criarJogo(
        torneioId: Int,
        equipaCasaId: Int,
        equipaForaId: Int,
        data: String,
        hora: String,
        local: String
    ): Jogo?
}
