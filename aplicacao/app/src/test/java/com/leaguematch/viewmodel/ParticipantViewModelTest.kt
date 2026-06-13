package com.leaguematch.viewmodel

import com.leaguematch.MainDispatcherRule
import com.leaguematch.data.remote.model.Classificacao
import com.leaguematch.data.remote.model.ConfiguracaoNotificacoes
import com.leaguematch.data.remote.model.Equipa
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.data.remote.model.Torneio
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

    private val equipa = Equipa(id = 5, nome = "FC Águias", torneioId = 1)

    @Before
    fun setUp() {
        viewModel = ParticipantViewModel(repository)
    }

    @Test
    fun `initial state has empty collections and null entities`() {
        assertTrue(viewModel.torneios.value.isEmpty())
        assertTrue(viewModel.jogos.value.isEmpty())
        assertNull(viewModel.equipa.value)
        assertEquals(ParticipantStatsData(), viewModel.statsParticipante.value)
        assertNull(viewModel.juntarEquipaErro.value)
    }

    @Test
    fun `carregarDadosParticipante with no teams resets state`() = runTest {
        coEvery { repository.listarEquipasDoParticipante(99) } returns emptyList()
        coEvery { repository.obterConfiguracaoNotificacoes(99) } returns ConfiguracaoNotificacoes(utilizadorId = 99)
        coEvery { repository.listarTorneios() } returns emptyList()

        viewModel.carregarDadosParticipante(99)

        assertTrue(viewModel.equipas.value.isEmpty())
        assertNull(viewModel.equipa.value)
        assertEquals(ParticipantStatsData(), viewModel.statsParticipante.value)
    }

    @Test
    fun `carregarDadosParticipante with team loads stats and players`() = runTest {
        val torneio = Torneio(
            id = 1, nome = "Liga", modalidade = "Futebol",
            regras = "", formato = "LIGA",
            estado = "Em Curso", equipas = 2, active = true
        )
        coEvery { repository.listarEquipasDoParticipante(7) } returns listOf(equipa)
        coEvery { repository.obterConfiguracaoNotificacoes(7) } returns ConfiguracaoNotificacoes(utilizadorId = 7)
        coEvery { repository.listarJogadoresEquipa(5) } returns emptyList()
        coEvery {
            repository.obterClassificacaoEquipa(equipaId = 5, torneioId = 1)
        } returns Classificacao(
            equipaId = 5, nomeEquipa = "FC Águias",
            pontos = 0, jogos = 0, vitorias = 0, empates = 0, derrotas = 0,
            golosMarcados = 0, golosSofridos = 0
        )
        coEvery { repository.listarJogosDaEquipa(5) } returns listOf(
            Jogo(id = 1, torneioId = 1, casa = "A", fora = "B", estado = "Agendado")
        )
        coEvery { repository.listarTorneios() } returns listOf(torneio)
        coEvery {
            repository.obterEstatisticasParticipante(utilizadorId = 7, equipaId = 5)
        } returns ParticipantStatsData(jogos = 1, golos = 2, faltas = 1, cartoes = 0)

        viewModel.carregarDadosParticipante(7)

        assertEquals(listOf(equipa), viewModel.equipas.value)
        assertEquals(equipa, viewModel.equipa.value)
        assertEquals(1, viewModel.jogosEquipa.value.size)
        assertEquals(2, viewModel.statsParticipante.value.golos)
        assertEquals(1, viewModel.statsParticipante.value.faltas)
    }

    @Test
    fun `juntarEquipa success calls onSuccess and clears error`() = runTest {
        coEvery { repository.juntarEquipaPorCodigo(7, "CODE1") } returns Result.success(equipa)
        coEvery { repository.listarEquipasDoParticipante(7) } returns listOf(equipa)
        coEvery { repository.obterConfiguracaoNotificacoes(7) } returns ConfiguracaoNotificacoes(utilizadorId = 7)
        coEvery { repository.listarJogadoresEquipa(equipa.id) } returns emptyList()
        coEvery { repository.obterClassificacaoEquipa(any(), any()) } returns null
        coEvery { repository.listarJogosDaEquipa(any()) } returns emptyList()
        coEvery { repository.listarTorneios() } returns emptyList()
        coEvery { repository.obterEstatisticasParticipante(any(), any()) } returns ParticipantStatsData()

        var sucessoChamado = false
        viewModel.juntarEquipa(7, "CODE1") { sucessoChamado = true }

        assertTrue(sucessoChamado)
        assertNull(viewModel.juntarEquipaErro.value)
        assertFalse(viewModel.juntarEquipaLoading.value)
    }

    @Test
    fun `juntarEquipa failure sets error and keeps state`() = runTest {
        coEvery {
            repository.juntarEquipaPorCodigo(7, "BAD")
        } returns Result.failure(RuntimeException("Código inválido"))

        var sucessoChamado = false
        viewModel.juntarEquipa(7, "BAD") { sucessoChamado = true }

        assertFalse(sucessoChamado)
        assertEquals("Código inválido", viewModel.juntarEquipaErro.value)
        assertFalse(viewModel.juntarEquipaLoading.value)
    }

    @Test
    fun `limparErroJuntarEquipa resets the error state`() = runTest {
        coEvery { repository.juntarEquipaPorCodigo(any(), any()) } returns Result.failure(RuntimeException("x"))
        viewModel.juntarEquipa(7, "X") {}
        assertNotNull(viewModel.juntarEquipaErro.value)

        viewModel.limparErroJuntarEquipa()
        assertNull(viewModel.juntarEquipaErro.value)
    }

    @Test
    fun `atualizarConfiguracaoNotificacoes calls repository`() = runTest {
        val config = ConfiguracaoNotificacoes(utilizadorId = 7, notificacoesGolos = false)
        coEvery { repository.atualizarConfiguracaoNotificacoes(config) } returns config

        viewModel.atualizarConfiguracaoNotificacoes(config)

        coVerify(exactly = 1) { repository.atualizarConfiguracaoNotificacoes(config) }
    }
}
