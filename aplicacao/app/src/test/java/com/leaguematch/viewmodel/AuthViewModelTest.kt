package com.leaguematch.viewmodel

import com.leaguematch.MainDispatcherRule
import com.leaguematch.data.remote.model.TipoUtilizador
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.data.repository.LeagueMatchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: LeagueMatchRepository = mockk(relaxed = true)
    private lateinit var viewModel: AuthViewModel

    private val testUser = Utilizador(
        id = 1,
        nome = "João Ferreira",
        email = "joao@leaguematch.com",
        tipo = TipoUtilizador.ADMIN
    )

    @Before
    fun setUp() {
        viewModel = AuthViewModel(repository)
    }

    @Test
    fun `autenticar success updates isLoggedIn and usuarioLogado`() = runTest {
        coEvery { repository.autenticar("joao@leaguematch.com", "senha123") } returns testUser

        viewModel.autenticar("joao@leaguematch.com", "senha123")

        assertTrue(viewModel.isLoggedIn.value)
        assertEquals(testUser, viewModel.usuarioLogado.value)
        assertNull(viewModel.loginError.value)
    }

    @Test
    fun `autenticar failure with invalid credentials sets loginError`() = runTest {
        coEvery { repository.autenticar("joao@leaguematch.com", "wrong_pass") } returns null

        viewModel.autenticar("joao@leaguematch.com", "wrong_pass")

        assertFalse(viewModel.isLoggedIn.value)
        assertNull(viewModel.usuarioLogado.value)
        assertEquals("Credenciais inválidas", viewModel.loginError.value)
    }

    @Test
    fun `autenticar error throws exception sets error message`() = runTest {
        coEvery { repository.autenticar(any(), any()) } throws RuntimeException("Network Exception")

        viewModel.autenticar("joao@leaguematch.com", "senha123")

        assertFalse(viewModel.isLoggedIn.value)
        assertNull(viewModel.usuarioLogado.value)
        assertEquals("Network Exception", viewModel.loginError.value)
    }

    @Test
    fun `registar success sets registerSuccess to true`() = runTest {
        coEvery { repository.registar("João", "joao@leaguematch.com", "senha123", "admin") } returns testUser

        viewModel.registar("João", "joao@leaguematch.com", "senha123", "admin")

        assertTrue(viewModel.registerSuccess.value)
        assertNull(viewModel.loginError.value)
    }

    @Test
    fun `registar failure sets loginError`() = runTest {
        coEvery { repository.registar(any(), any(), any(), any()) } returns null

        viewModel.registar("João", "joao@leaguematch.com", "senha123", "admin")

        assertFalse(viewModel.registerSuccess.value)
        assertEquals("Não foi possível criar a conta", viewModel.loginError.value)
    }

    @Test
    fun `atualizarUtilizador updates usuarioLogado successfully`() = runTest {
        // First log the user in to populate viewModel.usuarioLogado
        coEvery { repository.autenticar(any(), any()) } returns testUser
        viewModel.autenticar("joao@leaguematch.com", "senha123")

        val updatedUser = testUser.copy(nome = "João Updated")
        coEvery { repository.atualizarUtilizador(testUser.id, "João Updated", "novaSenha") } returns updatedUser

        viewModel.atualizarUtilizador("João Updated", "novaSenha")

        assertEquals("João Updated", viewModel.usuarioLogado.value?.nome)
        assertNull(viewModel.loginError.value)
    }

    @Test
    fun `resetRegisterState resets registerSuccess and loginError`() = runTest {
        coEvery { repository.registar(any(), any(), any(), any()) } returns null
        viewModel.registar("João", "joao@leaguematch.com", "senha123", "admin")

        // Error is set
        assertEquals("Não foi possível criar a conta", viewModel.loginError.value)

        viewModel.resetRegisterState()

        assertFalse(viewModel.registerSuccess.value)
        assertNull(viewModel.loginError.value)
    }

    @Test
    fun `terminarSessao clears all session states`() = runTest {
        coEvery { repository.autenticar(any(), any()) } returns testUser
        viewModel.autenticar("joao@leaguematch.com", "senha123")

        assertTrue(viewModel.isLoggedIn.value)
        assertEquals(testUser, viewModel.usuarioLogado.value)

        viewModel.terminarSessao()

        assertFalse(viewModel.isLoggedIn.value)
        assertNull(viewModel.usuarioLogado.value)
        assertNull(viewModel.loginError.value)
        assertFalse(viewModel.registerSuccess.value)
    }
}
