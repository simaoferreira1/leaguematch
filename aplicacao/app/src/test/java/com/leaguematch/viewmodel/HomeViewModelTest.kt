package com.leaguematch.viewmodel

import com.leaguematch.MainDispatcherRule
import com.leaguematch.data.remote.model.ResumoDashboard
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
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: LeagueMatchRepository = mockk(relaxed = true)
    private lateinit var viewModel: HomeViewModel

    private val mockDashboard = ResumoDashboard(
        totalUtilizadores = 10,
        totalTorneios = 5,
        torneiosEmCurso = 2,
        alertasSistema = 0
    )

    @Before
    fun setUp() {
        viewModel = HomeViewModel(repository)
    }

    @Test
    fun `carregarDashboard success updates dashboardState with Success Result`() = runTest {
        coEvery { repository.obterDashboard() } returns mockDashboard

        viewModel.carregarDashboard()

        val state = viewModel.dashboardState.value
        assertNotNull(state)
        assertTrue(state!!.isSuccess)
        assertEquals(mockDashboard, state.getOrNull())
    }

    @Test
    fun `carregarDashboard error updates dashboardState with Failure Result`() = runTest {
        val exception = RuntimeException("API Connection Error")
        coEvery { repository.obterDashboard() } throws exception

        viewModel.carregarDashboard()

        val state = viewModel.dashboardState.value
        assertNotNull(state)
        assertTrue(state!!.isFailure)
        assertEquals(exception, state.exceptionOrNull())
    }
}
