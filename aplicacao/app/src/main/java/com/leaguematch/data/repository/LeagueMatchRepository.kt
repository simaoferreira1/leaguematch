/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: LeagueMatchRepository.kt
 * Tipo: Camada de Acesso a Dados (Repositório)
 *
 * Descrição:
 * Este ficheiro define a interface ou implementação do repositório de dados.\n * Abstrai a origem dos dados da aplicação, servindo de ponte entre os ViewModels e as APIs externas.
 */
package com.leaguematch.data.repository // Define o pacote deste ficheiro de código

import com.leaguematch.data.remote.model.DetalheTorneio // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Equipa // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.EstatisticasAdmin // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Jogo // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.ResumoDashboard // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.ResumoModalidade // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Torneio // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Utilizador // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Classificacao // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.EstatisticaJogo // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.EventoJogo // Importa dependência / biblioteca necessária
import com.leaguematch.ui.spectator.JogoResumoItem // Importa dependência / biblioteca necessária
import com.leaguematch.ui.spectator.MelhorMarcadorItem // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.ConfiguracaoNotificacoes // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.NotificacaoItem // Importa dependência / biblioteca necessária
import com.leaguematch.viewmodel.ParticipantStatsData // Importa dependência / biblioteca necessária

interface LeagueMatchRepository { // Declaração de interface (contrato de métodos)
    suspend fun autenticar(email: String, password: String): Utilizador? // Declaração de função / método de lógica
    suspend fun registar(nome: String, email: String, password: String, tipo: String): Utilizador? // Declaração de função / método de lógica
    suspend fun obterDashboard(): ResumoDashboard // Declaração de função / método de lógica
    suspend fun listarUtilizadores(): List<Utilizador> // Declaração de função / método de lógica
    suspend fun obterUtilizador(id: Int): Utilizador? // Declaração de função / método de lógica
    suspend fun alterarEstadoUtilizador( // Declaração de função / método de lógica
        id: Int,
        ativo: Boolean
    ): Boolean
    suspend fun listarModalidades(): List<ResumoModalidade> // Declaração de função / método de lógica
    suspend fun listarTorneiosPorModalidade(modalidade: String): List<Torneio> // Declaração de função / método de lógica
    suspend fun obterClassificacao(torneioId: Int): List<Classificacao> // Declaração de função / método de lógica
    suspend fun obterDetalheTorneio(id: Int): DetalheTorneio? // Declaração de função / método de lógica
    suspend fun obterEstatisticasAdmin(): EstatisticasAdmin // Declaração de função / método de lógica
    suspend fun atualizarUtilizador(id: Int, nome: String, password: String?): Utilizador? // Declaração de função / método de lógica
    suspend fun redefinirPasswordPorEmail(email: String, novaPassword: String): Boolean // Declaração de função / método de lógica
    suspend fun removerTorneio(id: Int): Boolean // Declaração de função / método de lógica
    suspend fun atualizarTorneio( // Declaração de função / método de lógica
        id: Int,
        nome: String,
        regras: String,
        formato: String
    ): Torneio?
    suspend fun obterMelhoresMarcadores(torneioId: Int): List<MelhorMarcadorItem> // Declaração de função / método de lógica
    suspend fun obterJogosDoTorneio(torneioId: Int): List<JogoResumoItem> // Declaração de função / método de lógica
    suspend fun listarJogosAoVivo(): List<Jogo> // Declaração de função / método de lógica
    suspend fun listarTodosJogos(): List<Jogo> // Declaração de função / método de lógica
    suspend fun obterEstatisticasJogo(partidaId: Int): List<EstatisticaJogo> // Declaração de função / método de lógica
    suspend fun obterEventosJogo(partidaId: Int): List<EventoJogo> // Declaração de função / método de lógica
    suspend fun criarTorneio( // Declaração de função / método de lógica
        nome: String,
        modalidade: String,
        regras: String,
        formato: String,
        organizadorId: Int
    ): Torneio?
    suspend fun listarEquipasTorneio(torneioId: Int): List<Equipa> // Declaração de função / método de lógica
    suspend fun criarEquipa(nome: String, torneioId: Int): Equipa? // Declaração de função / método de lógica
    suspend fun removerEquipa(equipaId: Int): Boolean // Declaração de função / método de lógica
    suspend fun atualizarEquipa(id: Int, nome: String): Equipa? // Declaração de função / método de lógica
    suspend fun removerJogo(id: Int): Boolean // Declaração de função / método de lógica
    suspend fun atualizarJogo( // Declaração de função / método de lógica
        id: Int,
        resultadoCasa: Int,
        resultadoFora: Int,
        estado: String,
        local: String? = null,
        data: String? = null,
        hora: String? = null,
        atualizarInicio: Boolean = false
    ): Jogo?
    suspend fun criarJogo( // Declaração de função / método de lógica
        torneioId: Int,
        equipaCasaId: Int,
        equipaForaId: Int,
        data: String,
        hora: String,
        local: String
    ): Jogo?
    suspend fun guardarEstatisticasJogo(partidaId: Int, estatisticas: List<EstatisticaJogo>): Boolean // Declaração de função / método de lógica
    suspend fun registarEventoJogo( // Declaração de função / método de lógica
        partidaId: Int,
        tipo: String,
        equipa: String,
        tempo: Int,
        userId: Int?,
        jogadorSaiId: Int? = null,
        jogadorEntraId: Int? = null
    ): Boolean
    suspend fun obterConfiguracaoNotificacoes(utilizadorId: Int): ConfiguracaoNotificacoes // Declaração de função / método de lógica
    suspend fun listarNotificacoes(utilizadorId: Int): List<NotificacaoItem> // Declaração de função / método de lógica
    suspend fun marcarNotificacaoLida(notificacaoId: Int): Boolean // Declaração de função / método de lógica
    suspend fun marcarTodasNotificacoesLidas(utilizadorId: Int): Boolean // Declaração de função / método de lógica
    suspend fun criarNotificacaoParaTodos(mensagem: String): Boolean // Declaração de função / método de lógica
    suspend fun contarAlteracoesPendentes(): Int // Declaração de função / método de lógica
    suspend fun sincronizarPendentes(): Int // Declaração de função / método de lógica
    suspend fun listarNotificacoesAdmin(): List<NotificacaoItem> // Declaração de função / método de lógica
    suspend fun marcarTodasNotificacoesAdminLidas(): Boolean // Declaração de função / método de lógica
    suspend fun atualizarConfiguracaoNotificacoes( // Declaração de função / método de lógica
        configuracao: ConfiguracaoNotificacoes
    ): ConfiguracaoNotificacoes?
    suspend fun criarNotificacaoParaOrganizadorDoTorneio( // Declaração de função / método de lógica
        torneioId: Int,
        mensagem: String
    ): Boolean
    suspend fun listarTorneios(): List<Torneio> // Declaração de função / método de lógica
    suspend fun listarTorneiosDoOrganizador(organizadorId: Int): List<Torneio> // Declaração de função / método de lógica
    suspend fun obterEquipaDoParticipante(utilizadorId: Int): Equipa? // Declaração de função / método de lógica
    suspend fun listarJogadoresEquipa(equipaId: Int): List<Utilizador> // Declaração de função / método de lógica
    suspend fun obterClassificacaoEquipa(equipaId: Int, torneioId: Int): Classificacao? // Declaração de função / método de lógica
    suspend fun listarJogosDaEquipa(equipaId: Int): List<Jogo> // Declaração de função / método de lógica
    suspend fun obterEstatisticasParticipante( // Declaração de função / método de lógica
        utilizadorId: Int,
        equipaId: Int
    ): ParticipantStatsData
    suspend fun juntarEquipaPorCodigo(utilizadorId: Int, codigo: String): Result<Equipa> // Declaração de função / método de lógica
    suspend fun removerJogadorEquipa(equipaId: Int, utilizadorId: Int): Boolean // Declaração de função / método de lógica
    suspend fun listarEquipasDoParticipante(utilizadorId: Int): List<Equipa> // Declaração de função / método de lógica
    suspend fun obterEstatisticasAdmin(periodo: String): EstatisticasAdmin // Declaração de função / método de lógica
    suspend fun desativarTorneio(id: Int): Boolean // Declaração de função / método de lógica
}
