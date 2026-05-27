package com.leaguematch.data.repository

import com.leaguematch.data.remote.model.DetalheTorneio
import com.leaguematch.data.remote.model.EstatisticasAdmin
import com.leaguematch.data.remote.model.ResumoDashboard
import com.leaguematch.data.remote.model.ResumoModalidade
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.data.remote.model.Classificacao
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
    suspend fun obterMelhoresMarcadores(torneioId: Int): List<MelhorMarcadorItem>
    suspend fun obterJogosDoTorneio(torneioId: Int): List<JogoResumoItem>
    suspend fun criarTorneio(
        nome: String,
        modalidade: String,
        regras: String,
        formato: String,
        organizadorId: Int
    ): Torneio?
}
