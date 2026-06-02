package com.leaguematch.viewmodel

import com.leaguematch.MainDispatcherRule
import com.leaguematch.data.remote.model.DetalheTorneio
import com.leaguematch.data.remote.model.Equipa
import com.leaguematch.data.remote.model.EstatisticaJogo
import com.leaguematch.data.remote.model.EventoJogo
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.data.remote.model.ResumoDashboard
import com.leaguematch.data.remote.model.ResumoModalidade
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TorneiosViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: LeagueMatchRepository = mockk(relaxed = true)
    private lateinit var viewModel: TorneiosViewModel

    private val mockTorneio = Torneio(
        id = 1,
        nome = "Taça de Viana",
        modalidade = "Futebol",
        regras = "Eliminatória",
        formato = "Liga",
        estado = "Em Curso",
        equipas = 8,
        active = true
    )
    
    private val mockEquipa = Equipa(id = 10, nome = "FC Porto", torneioId = 1)
    
    private val mockJogo = Jogo(
        id = 20,
        torneioId = 1,
        casa = "FC Porto",
        fora = "SL Benfica",
        resultadoCasa = 2,
        resultadoFora = 1,
        estado = "Decorrer",
        active = true,
        data = "2026-06-02",
        hora = "18:00"
    )

    @Before
    fun setUp() {
        viewModel = TorneiosViewModel(repository)
    }

    @Test
    fun `carregarJogosAoVivo success updates state`() = runTest {
        val list = listOf(mockJogo)
        coEvery { repository.listarJogosAoVivo() } returns list

        viewModel.carregarJogosAoVivo()

        val state = viewModel.jogosAoVivoState.value
        assertNotNull(state)
        assertTrue(state!!.isSuccess)
        assertEquals(list, state.getOrNull())
    }

    @Test
    fun `carregarEstatisticasJogo success updates state`() = runTest {
        val list = listOf(EstatisticaJogo(tipo = "Remates", equipa = "casa", valor = 10))
        coEvery { repository.obterEstatisticasJogo(20) } returns list

        viewModel.carregarEstatisticasJogo(20)

        val state = viewModel.estatisticasJogoState.value
        assertNotNull(state)
        assertTrue(state!!.isSuccess)
        assertEquals(list, state.getOrNull())
    }

    @Test
    fun `carregarEventosJogo success updates state`() = runTest {
        val list = listOf(EventoJogo(id = 1, matchId = 20, tipo = "GOLO", userId = 3, userName = "Carlos", tempo = 45, equipa = "casa"))
        coEvery { repository.obterEventosJogo(20) } returns list

        viewModel.carregarEventosJogo(20)

        val state = viewModel.eventosJogoState.value
        assertNotNull(state)
        assertTrue(state!!.isSuccess)
        assertEquals(list, state.getOrNull())
    }

    @Test
    fun `carregarTorneios success loads modalities and dashboard total`() = runTest {
        val modalities = listOf(ResumoModalidade("Futebol", 3))
        coEvery { repository.listarModalidades() } returns modalities
        coEvery { repository.obterDashboard() } returns ResumoDashboard(10, 3, 2, 0)

        viewModel.carregarTorneios()

        val state = viewModel.modalidadesState.value
        assertNotNull(state)
        assertTrue(state!!.isSuccess)
        val data = state.getOrNull()
        assertNotNull(data)
        assertEquals(modalities, data!!.first)
        assertEquals(3, data.second)
    }

    @Test
    fun `carregarTodosTorneios success aggregates across modalities`() = runTest {
        val modalities = listOf(ResumoModalidade("Futebol", 1), ResumoModalidade("Futsal", 1))
        val footballTournaments = listOf(mockTorneio)
        val futsalTournaments = listOf(mockTorneio.copy(id = 2, modalidade = "Futsal"))

        coEvery { repository.listarModalidades() } returns modalities
        coEvery { repository.listarTorneiosPorModalidade("Futebol") } returns footballTournaments
        coEvery { repository.listarTorneiosPorModalidade("Futsal") } returns futsalTournaments

        viewModel.carregarTodosTorneios()

        val state = viewModel.todosTorneiosState.value
        assertNotNull(state)
        assertTrue(state!!.isSuccess)
        assertEquals(2, state.getOrNull()?.size)
    }

    @Test
    fun `carregarTorneiosPorModalidade success updates state`() = runTest {
        val list = listOf(mockTorneio)
        coEvery { repository.listarTorneiosPorModalidade("Futebol") } returns list

        viewModel.carregarTorneiosPorModalidade("Futebol")

        val state = viewModel.torneiosState.value
        assertNotNull(state)
        assertTrue(state!!.isSuccess)
        assertEquals(list, state.getOrNull())
    }

    @Test
    fun `carregarDetalheTorneio success updates state`() = runTest {
        val detail = DetalheTorneio(torneio = mockTorneio, goleadores = emptyList(), jogos = emptyList())
        coEvery { repository.obterDetalheTorneio(1) } returns detail

        viewModel.carregarDetalheTorneio(1)

        val state = viewModel.detalheTorneioState.value
        assertNotNull(state)
        assertTrue(state!!.isSuccess)
        assertEquals(detail, state.getOrNull())
    }

    @Test
    fun `removerTorneio calls repository and reloads list`() = runTest {
        coEvery { repository.removerTorneio(1) } returns true
        coEvery { repository.listarModalidades() } returns emptyList()

        viewModel.removerTorneio(1)

        coVerify { repository.removerTorneio(1) }
    }

    @Test
    fun `carregarEquipas success updates state`() = runTest {
        val list = listOf(mockEquipa)
        coEvery { repository.listarEquipasTorneio(1) } returns list

        viewModel.carregarEquipas(1)

        val state = viewModel.equipasState.value
        assertNotNull(state)
        assertTrue(state!!.isSuccess)
        assertEquals(list, state.getOrNull())
    }

    @Test
    fun `criarEquipa triggers onSuccess callback on success`() = runTest {
        coEvery { repository.criarEquipa("Novo FC", 1) } returns mockEquipa
        var successTriggered = false
        var errorTriggered = false

        viewModel.criarEquipa("Novo FC", 1, { successTriggered = true }, { errorTriggered = true })

        assertTrue(successTriggered)
        assertFalse(errorTriggered)
    }

    @Test
    fun `criarEquipa triggers onError callback on failure`() = runTest {
        coEvery { repository.criarEquipa("Novo FC", 1) } returns null
        var successTriggered = false
        var errorTriggered = false

        viewModel.criarEquipa("Novo FC", 1, { successTriggered = true }, { errorTriggered = true })

        assertFalse(successTriggered)
        assertTrue(errorTriggered)
    }

    @Test
    fun `removerEquipa triggers onSuccess and reloads list`() = runTest {
        coEvery { repository.removerEquipa(10) } returns true
        var successTriggered = false
        var errorTriggered = false

        viewModel.removerEquipa(10, 1, { successTriggered = true }, { errorTriggered = true })

        assertTrue(successTriggered)
        assertFalse(errorTriggered)
        coVerify { repository.listarEquipasTorneio(1) }
    }

    @Test
    fun `editarEquipa triggers onSuccess and reloads list`() = runTest {
        coEvery { repository.atualizarEquipa(10, "Novo Nome") } returns mockEquipa
        var successTriggered = false
        var errorTriggered = false

        viewModel.editarEquipa(10, "Novo Nome", 1, { successTriggered = true }, { errorTriggered = true })

        assertTrue(successTriggered)
        assertFalse(errorTriggered)
        coVerify { repository.listarEquipasTorneio(1) }
    }

    @Test
    fun `removerJogo triggers onSuccess and reloads detail`() = runTest {
        coEvery { repository.removerJogo(20) } returns true
        var successTriggered = false
        var errorTriggered = false

        viewModel.removerJogo(20, 1, { successTriggered = true }, { errorTriggered = true })

        assertTrue(successTriggered)
        assertFalse(errorTriggered)
        coVerify { repository.obterDetalheTorneio(1) }
    }

    @Test
    fun `editarJogo triggers onSuccess and reloads detail`() = runTest {
        coEvery { repository.atualizarJogo(20, 3, 3, "Finalizado", "Estádio B") } returns mockJogo
        var successTriggered = false
        var errorTriggered = false

        viewModel.editarJogo(20, 1, 3, 3, "Finalizado", "Estádio B", { successTriggered = true }, { errorTriggered = true })

        assertTrue(successTriggered)
        assertFalse(errorTriggered)
        coVerify { repository.obterDetalheTorneio(1) }
    }

    @Test
    fun `criarJogo updates loading state and calls onSuccess`() = runTest {
        coEvery { repository.criarJogo(1, 10, 11, "2026-06-02", "18:00", "Estádio") } returns mockJogo
        var successTriggered = false
        var errorTriggered = false

        assertFalse(viewModel.criarJogoLoading.value)
        viewModel.criarJogo(1, 10, 11, "2026-06-02", "18:00", "Estádio", { successTriggered = true }, { errorTriggered = true })

        assertTrue(successTriggered)
        assertFalse(errorTriggered)
        assertFalse(viewModel.criarJogoLoading.value)
        coVerify { repository.obterDetalheTorneio(1) }
    }

    @Test
    fun `criarTorneio triggers onSuccess and reloads categories`() = runTest {
        coEvery { repository.criarTorneio("Nova Copa", "Futebol", "Regra", "Eliminatória", 3) } returns mockTorneio
        coEvery { repository.listarModalidades() } returns emptyList()
        var successTriggered = false
        var errorTriggered = false

        viewModel.criarTorneio("Nova Copa", "Futebol", "Regra", "Eliminatória", 3, { successTriggered = true }, { errorTriggered = true })

        assertTrue(successTriggered)
        assertFalse(errorTriggered)
        coVerify { repository.listarModalidades() }
    }

    @Test
    fun `editarTorneio triggers onSuccess and refreshes tournament info`() = runTest {
        coEvery { repository.atualizarTorneio(1, "Novo Nome", "Novas Regras", "Novo Formato") } returns mockTorneio
        coEvery { repository.listarModalidades() } returns emptyList()
        var successTriggered = false
        var errorTriggered = false

        viewModel.editarTorneio(1, "Novo Nome", "Novas Regras", "Novo Formato", { successTriggered = true }, { errorTriggered = true })

        assertTrue(successTriggered)
        assertFalse(errorTriggered)
        coVerify { repository.obterDetalheTorneio(1) }
    }
}
