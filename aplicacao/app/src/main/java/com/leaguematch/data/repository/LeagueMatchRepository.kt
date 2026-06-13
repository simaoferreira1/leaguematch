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
import com.leaguematch.data.remote.model.ConfiguracaoNotificacoes
import com.leaguematch.data.remote.model.NotificacaoItem
import com.leaguematch.viewmodel.ParticipantStatsData

interface LeagueMatchRepository {
    suspend fun autenticar(email: String, password: String): Utilizador?
    suspend fun registar(nome: String, email: String, password: String, tipo: String): Utilizador?
    suspend fun obterDashboard(): ResumoDashboard
    suspend fun listarUtilizadores(): List<Utilizador>
    suspend fun obterUtilizador(id: Int): Utilizador?
    suspend fun alterarEstadoUtilizador(
        id: Int,
        ativo: Boolean
    ): Boolean
    suspend fun listarModalidades(): List<ResumoModalidade>
    suspend fun listarTorneiosPorModalidade(modalidade: String): List<Torneio>
    suspend fun obterClassificacao(torneioId: Int): List<Classificacao>
    suspend fun obterDetalheTorneio(id: Int): DetalheTorneio?
    suspend fun obterEstatisticasAdmin(): EstatisticasAdmin
    suspend fun atualizarUtilizador(id: Int, nome: String, password: String?): Utilizador?
    suspend fun redefinirPasswordPorEmail(email: String, novaPassword: String): Boolean
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
        local: String? = null,
        data: String? = null,
        hora: String? = null,
        atualizarInicio: Boolean = false
    ): Jogo?
    suspend fun criarJogo(
        torneioId: Int,
        equipaCasaId: Int,
        equipaForaId: Int,
        data: String,
        hora: String,
        local: String
    ): Jogo?
    suspend fun guardarEstatisticasJogo(partidaId: Int, estatisticas: List<EstatisticaJogo>): Boolean
    suspend fun registarEventoJogo(
        partidaId: Int,
        tipo: String,
        equipa: String,
        tempo: Int,
        userId: Int?
    ): Boolean
    suspend fun obterConfiguracaoNotificacoes(utilizadorId: Int): ConfiguracaoNotificacoes
    suspend fun listarNotificacoes(utilizadorId: Int): List<NotificacaoItem>
    suspend fun marcarNotificacaoLida(notificacaoId: Int): Boolean
    suspend fun marcarTodasNotificacoesLidas(utilizadorId: Int): Boolean
    suspend fun criarNotificacaoParaTodos(mensagem: String): Boolean
    suspend fun contarAlteracoesPendentes(): Int
    suspend fun sincronizarPendentes(): Int

    suspend fun atualizarConfiguracaoNotificacoes(
        configuracao: ConfiguracaoNotificacoes
    ): ConfiguracaoNotificacoes?
    suspend fun listarTorneios(): List<Torneio>
    suspend fun listarTorneiosDoOrganizador(organizadorId: Int): List<Torneio>
    suspend fun obterEquipaDoParticipante(utilizadorId: Int): Equipa?
    suspend fun listarJogadoresEquipa(equipaId: Int): List<Utilizador>
    suspend fun obterClassificacaoEquipa(equipaId: Int, torneioId: Int): Classificacao?
    suspend fun listarJogosDaEquipa(equipaId: Int): List<Jogo>
    suspend fun obterEstatisticasParticipante(
        utilizadorId: Int,
        equipaId: Int
    ): ParticipantStatsData
    suspend fun juntarEquipaPorCodigo(utilizadorId: Int, codigo: String): Result<Equipa>
    suspend fun removerJogadorEquipa(equipaId: Int, utilizadorId: Int): Boolean
    suspend fun listarEquipasDoParticipante(utilizadorId: Int): List<Equipa>
    suspend fun obterEstatisticasAdmin(periodo: String): EstatisticasAdmin
    suspend fun desativarTorneio(id: Int): Boolean
}
