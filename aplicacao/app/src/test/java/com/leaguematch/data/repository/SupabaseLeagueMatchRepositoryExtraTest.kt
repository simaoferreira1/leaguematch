package com.leaguematch.data.repository

import com.leaguematch.data.remote.model.TipoUtilizador
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SupabaseLeagueMatchRepositoryExtraTest {

    private lateinit var repository: SupabaseLeagueMatchRepository

    @Before
    fun setUp() {
        repository = SupabaseLeagueMatchRepository(
            supabaseUrl = "https://example.supabase.co",
            anonKey = "mock_anon_key"
        )
    }

    @Test
    fun `autenticar admin admin returns admin bypass user`() = runTest {
        val r = repository.autenticar("admin", "admin")
        assertNotNull(r)
        assertEquals(777, r?.id)
        assertEquals(TipoUtilizador.ADMIN, r?.tipo)
    }

    @Test
    fun `autenticar admin with wrong password does NOT short-circuit bypass`() = runTest {
        // Quando a password do bypass admin/admin não bate, o código não retorna
        // null directamente — cai para a query Supabase, que neste ambiente de
        // testes não existe. Assertamos a exceção esperada (rede inacessível) em
        // vez de null, para documentar o comportamento atual.
        try {
            repository.autenticar("admin", "errada")
        } catch (_: Throwable) {
            return@runTest
        }
        // Se chegou aqui sem exceção, falha (significa que mudou o comportamento)
        org.junit.Assert.fail("Esperava-se exceção de rede ao tentar autenticar admin com password errada")
    }

    @Test
    fun `autenticar admin is case insensitive for email`() = runTest {
        val r = repository.autenticar("ADMIN", "admin")
        assertNotNull(r)
        assertEquals(777, r?.id)
    }

    @Test
    fun `contarAlteracoesPendentes returns zero when sync queue is null`() = runTest {
        // Construtor sem syncQueue -> não há fila
        assertEquals(0, repository.contarAlteracoesPendentes())
    }

    @Test
    fun `sincronizarPendentes returns zero when sync queue is null`() = runTest {
        assertEquals(0, repository.sincronizarPendentes())
    }
}
