package com.leaguematch.viewmodel

import com.leaguematch.MainDispatcherRule
import com.leaguematch.data.remote.model.TipoUtilizador
import com.leaguematch.data.remote.model.Utilizador
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
class UtilizadoresViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: LeagueMatchRepository = mockk(relaxed = true)
    private lateinit var viewModel: UtilizadoresViewModel

    private val mockUsersList = listOf(
        Utilizador(id = 1, nome = "Alex", email = "alex@test.com", tipo = TipoUtilizador.ADMIN),
        Utilizador(id = 2, nome = "Rita", email = "rita@test.com", tipo = TipoUtilizador.ORGANIZADOR)
    )

    @Before
    fun setUp() {
        viewModel = UtilizadoresViewModel(repository)
    }

    @Test
    fun `carregarUtilizadores success updates utilizadoresState`() = runTest {
        coEvery { repository.listarUtilizadores() } returns mockUsersList

        viewModel.carregarUtilizadores()

        val state = viewModel.utilizadoresState.value
        assertNotNull(state)
        assertTrue(state!!.isSuccess)
        assertEquals(mockUsersList, state.getOrNull())
    }

    @Test
    fun `carregarUtilizadores error updates utilizadoresState with Failure`() = runTest {
        val exception = RuntimeException("Server unavailable")
        coEvery { repository.listarUtilizadores() } throws exception

        viewModel.carregarUtilizadores()

        val state = viewModel.utilizadoresState.value
        assertNotNull(state)
        assertTrue(state!!.isFailure)
        assertEquals(exception, state.exceptionOrNull())
    }

    @Test
    fun `carregarDetalhes success updates detalheUtilizadorState`() = runTest {
        val singleUser = mockUsersList[1]
        coEvery { repository.obterUtilizador(2) } returns singleUser

        viewModel.carregarDetalhes(2)

        val state = viewModel.detalheUtilizadorState.value
        assertNotNull(state)
        assertTrue(state!!.isSuccess)
        assertEquals(singleUser, state.getOrNull())
    }

    @Test
    fun `carregarDetalhes error updates detalheUtilizadorState with Failure`() = runTest {
        val exception = RuntimeException("User not found")
        coEvery { repository.obterUtilizador(99) } throws exception

        viewModel.carregarDetalhes(99)

        val state = viewModel.detalheUtilizadorState.value
        assertNotNull(state)
        assertTrue(state!!.isFailure)
        assertEquals(exception, state.exceptionOrNull())
    }

    @Test
    fun `alterarEstadoUtilizador success calls repository and reloads details and list`() = runTest {
        val singleUser = mockUsersList[1]
        coEvery { repository.alterarEstadoUtilizador(2, false) } returns true
        coEvery { repository.obterUtilizador(2) } returns singleUser.copy(active = false)
        coEvery { repository.listarUtilizadores() } returns listOf(mockUsersList[0], singleUser.copy(active = false))

        viewModel.alterarEstadoUtilizador(2, false)

        val detailState = viewModel.detalheUtilizadorState.value
        assertNotNull(detailState)
        assertTrue(detailState!!.isSuccess)
        assertEquals(false, detailState.getOrNull()?.active)

        val listState = viewModel.utilizadoresState.value
        assertNotNull(listState)
        assertTrue(listState!!.isSuccess)
        assertEquals(false, listState.getOrNull()?.get(1)?.active)
    }

    @Test
    fun `alterarEstadoUtilizador error updates detailState with Failure`() = runTest {
        val exception = RuntimeException("Failed to update status")
        coEvery { repository.alterarEstadoUtilizador(2, false) } throws exception

        viewModel.alterarEstadoUtilizador(2, false)

        val state = viewModel.detalheUtilizadorState.value
        assertNotNull(state)
        assertTrue(state!!.isFailure)
        assertEquals(exception, state.exceptionOrNull())
    }
}
