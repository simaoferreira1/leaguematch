package com.leaguematch.viewmodel

import com.leaguematch.MainDispatcherRule
import com.leaguematch.data.remote.model.*
import com.leaguematch.data.repository.LeagueMatchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ParticipantViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: LeagueMatchRepository = mockk(relaxed = true)
    private lateinit var viewModel: ParticipantViewModel

    private val mockEquipa = Equipa(id = 10, nome = "FC Porto", torneioId = 1)
    private val mockUtilizador = Utilizador(id = 5, nome = "Carlos", email = "carlos@test.com", tipo = TipoUtilizador.PARTICIPANTE)
    private val mockClassificacao = Classificacao(equipaId = 10, nomeEquipa = "FC Porto", pontos = 10, jogos = 4, vitorias = 3, empates = 1, derrotas = 0, golosMarcados = 8, golosSofridos = 2)
    private val mockJogo = Jogo(id = 20, torneioId = 1, casa = "FC Porto", fora = "SL Benfica", resultadoCasa = 2, resultadoFora = 1, estado = "Finalizado")
    private val mockTorneio = Torneio(id = 1, nome = "Taça de Viana", modalidade = "Futebol", regras = "Eliminatórias", formato = "Liga", estado = "Em Curso")
    private val mockConfig = ConfiguracaoNotificacoes(utilizadorId = 5, notificacoesJogos = true, notificacoesGolos = true)
    private val mockStats = ParticipantStatsData(jogos = 4, golos = 3, faltas = 2, cartoes = 1)

    @Before
    fun setUp() {
        viewModel = ParticipantViewModel(repository)
    }

    @Test
    fun `carregarDadosParticipante with teams updates states correctly`() = runTest {
        coEvery { repository.listarEquipasDoParticipante(5) } returns listOf(mockEquipa)
        coEvery { repository.obterConfiguracaoNotificacoes(5) } returns mockConfig
        coEvery { repository.listarJogadoresEquipa(10) } returns listOf(mockUtilizador)
        coEvery { repository.obterClassificacaoEquipa(10, 1) } returns mockClassificacao
        coEvery { repository.listarJogosDaEquipa(10) } returns listOf(mockJogo)
        coEvery { repository.listarTorneios() } returns listOf(mockTorneio)
        coEvery { repository.obterEstatisticasParticipante(5, 10) } returns mockStats

        viewModel.carregarDadosParticipante(5)

        assertEquals(listOf(mockEquipa), viewModel.equipas.value)
        assertEquals(mockEquipa, viewModel.equipa.value)
        assertEquals(mockConfig, viewModel.configuracaoNotificacoes.value)
        assertEquals(listOf(mockUtilizador), viewModel.jogadoresEquipa.value)
        assertEquals(mockClassificacao, viewModel.classificacaoEquipa.value)
        assertEquals(listOf(mockJogo), viewModel.jogosEquipa.value)
        assertEquals(listOf(mockJogo), viewModel.jogos.value)
        assertEquals(listOf(mockTorneio), viewModel.torneios.value)
        assertEquals(mockStats, viewModel.statsParticipante.value)
    }

    @Test
    fun `carregarDadosParticipante without teams sets empty defaults`() = runTest {
        coEvery { repository.listarEquipasDoParticipante(5) } returns emptyList()
        coEvery { repository.obterConfiguracaoNotificacoes(5) } returns mockConfig
        coEvery { repository.listarTorneios() } returns listOf(mockTorneio)

        viewModel.carregarDadosParticipante(5)

        assertTrue(viewModel.equipas.value.isEmpty())
        assertNull(viewModel.equipa.value)
        assertEquals(mockConfig, viewModel.configuracaoNotificacoes.value)
        assertTrue(viewModel.jogadoresEquipa.value.isEmpty())
        assertNull(viewModel.classificacaoEquipa.value)
        assertTrue(viewModel.jogosEquipa.value.isEmpty())
        assertTrue(viewModel.jogos.value.isEmpty())
        assertEquals(listOf(mockTorneio), viewModel.torneios.value)
        assertEquals(ParticipantStatsData(), viewModel.statsParticipante.value)
    }

    @Test
    fun `selecionarEquipa updates team selection and reloads`() = runTest {
        val mockEquipa2 = Equipa(id = 11, nome = "SL Benfica", torneioId = 1)
        coEvery { repository.listarEquipasDoParticipante(5) } returns listOf(mockEquipa, mockEquipa2)

        viewModel.selecionarEquipa(5, 11)

        assertEquals(mockEquipa2, viewModel.equipa.value)
    }

    @Test
    fun `carregarDetalheTorneio updates detail states`() = runTest {
        val mockDetail = DetalheTorneio(torneio = mockTorneio, goleadores = emptyList(), jogos = listOf(mockJogo))
        coEvery { repository.obterDetalheTorneio(1) } returns mockDetail
        coEvery { repository.obterClassificacao(1) } returns listOf(mockClassificacao)

        viewModel.carregarDetalheTorneio(1)

        assertEquals(mockDetail, viewModel.detalheTorneio.value)
        assertEquals(listOf(mockClassificacao), viewModel.classificacaoTorneio.value)
    }

    @Test
    fun `juntarEquipa success calls repository and triggers onSuccess`() = runTest {
        coEvery { repository.juntarEquipaPorCodigo(5, "T0000A") } returns Result.success(mockEquipa)
        coEvery { repository.listarEquipasDoParticipante(5) } returns listOf(mockEquipa)

        var successTriggered = false
        assertFalse(viewModel.juntarEquipaLoading.value)

        viewModel.juntarEquipa(5, "T0000A") {
            successTriggered = true
        }

        assertTrue(successTriggered)
        assertFalse(viewModel.juntarEquipaLoading.value)
        assertNull(viewModel.juntarEquipaErro.value)
        assertEquals(mockEquipa, viewModel.equipa.value)
    }

    @Test
    fun `juntarEquipa failure updates error state`() = runTest {
        val exception = RuntimeException("Código de equipa inválido")
        coEvery { repository.juntarEquipaPorCodigo(5, "TXXXXX") } returns Result.failure(exception)

        var successTriggered = false
        viewModel.juntarEquipa(5, "TXXXXX") {
            successTriggered = true
        }

        assertFalse(successTriggered)
        assertFalse(viewModel.juntarEquipaLoading.value)
        assertEquals("Código de equipa inválido", viewModel.juntarEquipaErro.value)
    }

    @Test
    fun `sairDaEquipa calls repository and reloads if successful`() = runTest {
        coEvery { repository.removerJogadorEquipa(10, 5) } returns true
        coEvery { repository.listarEquipasDoParticipante(5) } returns emptyList()

        // Setup initially with a selected team
        viewModel.selecionarEquipa(5, 10)

        viewModel.sairDaEquipa(5, 10)

        coVerify { repository.removerJogadorEquipa(10, 5) }
        assertNull(viewModel.equipa.value)
    }

    @Test
    fun `limparErroJuntarEquipa resets error state`() = runTest {
        coEvery { repository.juntarEquipaPorCodigo(5, "TXXXXX") } returns Result.failure(RuntimeException("Error"))
        viewModel.juntarEquipa(5, "TXXXXX") {}
        assertNotNull(viewModel.juntarEquipaErro.value)

        viewModel.limparErroJuntarEquipa()
        assertNull(viewModel.juntarEquipaErro.value)
    }

    @Test
    fun `atualizarConfiguracaoNotificacoes updates state on success`() = runTest {
        coEvery { repository.atualizarConfiguracaoNotificacoes(mockConfig) } returns mockConfig

        viewModel.atualizarConfiguracaoNotificacoes(mockConfig)

        assertEquals(mockConfig, viewModel.configuracaoNotificacoes.value)
    }
}
