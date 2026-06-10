package com.leaguematch.viewmodel

import com.leaguematch.MainDispatcherRule
import com.leaguematch.data.remote.model.EstatisticasAdmin
import com.leaguematch.data.repository.LeagueMatchRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GraficosViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: LeagueMatchRepository = mockk(relaxed = true)
    private lateinit var viewModel: GraficosViewModel

    private val mockStats = EstatisticasAdmin(
        totalUtilizadores = 10,
        totalTorneios = 6,
        totalJogos = 15,
        alertas = 1,
        jogosPorPeriodo = emptyList(),
        torneiosPorEstado = emptyList(),
        topTorneios = emptyList()
    )

    @Before
    fun setUp() {
        viewModel = GraficosViewModel(repository)
    }

    @Test
    fun `carregarEstatisticas success updates estatisticasState with Success`() = runTest {
        coEvery { repository.obterEstatisticasAdmin(any()) } returns mockStats

        viewModel.carregarEstatisticas()

        val state = viewModel.estatisticasState.value
        assertNotNull(state)
        assertTrue(state!!.isSuccess)
        assertEquals(mockStats, state.getOrNull())
    }

    @Test
    fun `carregarEstatisticas error updates estatisticasState with Failure`() = runTest {
        val exception = RuntimeException("Database error")
        coEvery { repository.obterEstatisticasAdmin(any()) } throws exception

        viewModel.carregarEstatisticas()

        val state = viewModel.estatisticasState.value
        assertNotNull(state)
        assertTrue(state!!.isFailure)
        assertEquals(exception, state.exceptionOrNull())
    }
}
