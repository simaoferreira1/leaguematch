package com.leaguematch.viewmodel

import com.leaguematch.MainDispatcherRule
import com.leaguematch.data.repository.LeagueMatchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelResetPasswordTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: LeagueMatchRepository = mockk(relaxed = true)
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        viewModel = AuthViewModel(repository)
    }

    @Test
    fun `resetPasswordPorAdmin success invokes callback with true`() = runTest {
        coEvery { repository.redefinirPasswordPorEmail("user@x.com", "nova123") } returns true
        var resultado: Boolean? = null

        viewModel.resetPasswordPorAdmin("user@x.com", "nova123") { ok -> resultado = ok }

        assertTrue(resultado == true)
        coVerify(exactly = 1) { repository.redefinirPasswordPorEmail("user@x.com", "nova123") }
    }

    @Test
    fun `resetPasswordPorAdmin repository false invokes callback with false`() = runTest {
        coEvery { repository.redefinirPasswordPorEmail(any(), any()) } returns false
        var resultado: Boolean? = null

        viewModel.resetPasswordPorAdmin("user@x.com", "nova123") { ok -> resultado = ok }

        assertFalse(resultado == true)
    }

    @Test
    fun `resetPasswordPorAdmin exception invokes callback with false instead of crashing`() = runTest {
        coEvery { repository.redefinirPasswordPorEmail(any(), any()) } throws RuntimeException("rede down")
        var resultado: Boolean? = null

        viewModel.resetPasswordPorAdmin("user@x.com", "nova123") { ok -> resultado = ok }

        assertFalse(resultado == true)
    }
}
