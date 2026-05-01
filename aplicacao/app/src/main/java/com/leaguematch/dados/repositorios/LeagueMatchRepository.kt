package com.leaguematch.dados.repositorios

import com.leaguematch.dados.modelos.DetalheTorneio
import com.leaguematch.dados.modelos.EstatisticasAdmin
import com.leaguematch.dados.modelos.ResumoDashboard
import com.leaguematch.dados.modelos.ResumoModalidade
import com.leaguematch.dados.modelos.Torneio
import com.leaguematch.dados.modelos.Utilizador

interface LeagueMatchRepository {
    suspend fun autenticar(email: String, password: String): Utilizador?
    suspend fun obterDashboard(): ResumoDashboard
    suspend fun listarUtilizadores(): List<Utilizador>
    suspend fun obterUtilizador(id: Int): Utilizador?
    suspend fun listarModalidades(): List<ResumoModalidade>
    suspend fun listarTorneiosPorModalidade(modalidade: String): List<Torneio>
    suspend fun obterDetalheTorneio(id: Int): DetalheTorneio?
    suspend fun obterEstatisticasAdmin(): EstatisticasAdmin
}
