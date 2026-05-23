package com.leaguematch.data.repository

import com.leaguematch.data.remote.model.DetalheTorneio
import com.leaguematch.data.remote.model.EstatisticasAdmin
import com.leaguematch.data.remote.model.ResumoDashboard
import com.leaguematch.data.remote.model.ResumoModalidade
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.data.remote.model.Utilizador

interface LeagueMatchRepository {
    suspend fun autenticar(email: String, password: String): Utilizador?
    suspend fun registar(nome: String, email: String, password: String, tipo: String): Utilizador?
    suspend fun obterDashboard(): ResumoDashboard
    suspend fun listarUtilizadores(): List<Utilizador>
    suspend fun obterUtilizador(id: Int): Utilizador?
    suspend fun listarModalidades(): List<ResumoModalidade>
    suspend fun listarTorneiosPorModalidade(modalidade: String): List<Torneio>
    suspend fun obterDetalheTorneio(id: Int): DetalheTorneio?
    suspend fun obterEstatisticasAdmin(): EstatisticasAdmin
    suspend fun atualizarUtilizador(id: Int, nome: String, password: String?): Utilizador?
    suspend fun removerTorneio(id: Int): Boolean
}
