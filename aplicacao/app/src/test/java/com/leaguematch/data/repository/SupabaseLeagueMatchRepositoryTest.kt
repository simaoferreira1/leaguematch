package com.leaguematch.data.repository

import com.leaguematch.data.remote.model.TipoUtilizador
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SupabaseLeagueMatchRepositoryTest {

    private lateinit var repository: SupabaseLeagueMatchRepository

    @Before
    fun setUp() {
        // Initialize repository with mock/placeholder settings
        repository = SupabaseLeagueMatchRepository(
            supabaseUrl = "https://example.supabase.co",
            anonKey = "mock_anon_key"
        )
    }

    @Test
    fun `autenticar with blank email returns null`() = runTest {
        val result = repository.autenticar("   ", "password123")
        assertNull(result)
    }

    @Test
    fun `autenticar with blank password returns null`() = runTest {
        val result = repository.autenticar("test@test.com", "   ")
        assertNull(result)
    }

    @Test
    fun `autenticar with developer admin bypass credentials returns admin user`() = runTest {
        val result = repository.autenticar("simao@leaguematch.com", "password")
        assertNotNull(result)
        assertEquals(999, result?.id)
        assertEquals("Simão Ferreira (Bypass Dev)", result?.nome)
        assertEquals(TipoUtilizador.ADMIN, result?.tipo)
    }

    @Test
    fun `autenticar with developer organizer bypass credentials returns organizer user`() = runTest {
        val result = repository.autenticar("organizador@leaguematch.com", "password")
        assertNotNull(result)
        assertEquals(888, result?.id)
        assertEquals("Organizador (Bypass Dev)", result?.nome)
        assertEquals(TipoUtilizador.ORGANIZADOR, result?.tipo)
    }

    @Test
    fun `obterDashboard throws exception when Supabase credentials are blank`() = runTest {
        val invalidRepository = SupabaseLeagueMatchRepository(
            supabaseUrl = "",
            anonKey = ""
        )
        try {
            invalidRepository.obterDashboard()
            fail("Should have thrown error due to missing Supabase configurations")
        } catch (e: Exception) {
            // Expected exception
            assertTrue(e.message?.contains("SUPABASE_URL") == true)
        }
    }

    private fun assertNotNull(actual: Any?) {
        org.junit.Assert.assertNotNull(actual)
    }

    private fun assertTrue(condition: Boolean) {
        org.junit.Assert.assertTrue(condition)
    }
}
