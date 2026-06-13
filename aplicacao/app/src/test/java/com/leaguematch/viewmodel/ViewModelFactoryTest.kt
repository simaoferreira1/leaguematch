package com.leaguematch.viewmodel

import androidx.lifecycle.ViewModel
import com.leaguematch.data.repository.LeagueMatchRepository
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ViewModelFactoryTest {

    private val repository: LeagueMatchRepository = mockk(relaxed = true)
    private val factory = ViewModelFactory(repository)

    @Test
    fun testCreateAuthViewModel() {
        val viewModel = factory.create(AuthViewModel::class.java)
        assertNotNull(viewModel)
        assertTrue(viewModel is AuthViewModel)
    }

    @Test
    fun testCreateHomeViewModel() {
        val viewModel = factory.create(HomeViewModel::class.java)
        assertNotNull(viewModel)
        assertTrue(viewModel is HomeViewModel)
    }

    @Test
    fun testCreateUtilizadoresViewModel() {
        val viewModel = factory.create(UtilizadoresViewModel::class.java)
        assertNotNull(viewModel)
        assertTrue(viewModel is UtilizadoresViewModel)
    }

    @Test
    fun testCreateTorneiosViewModel() {
        val viewModel = factory.create(TorneiosViewModel::class.java)
        assertNotNull(viewModel)
        assertTrue(viewModel is TorneiosViewModel)
    }

    @Test
    fun testCreateGraficosViewModel() {
        val viewModel = factory.create(GraficosViewModel::class.java)
        assertNotNull(viewModel)
        assertTrue(viewModel is GraficosViewModel)
    }

    @Test
    fun testCreateParticipantViewModel() {
        val viewModel = factory.create(ParticipantViewModel::class.java)
        assertNotNull(viewModel)
        assertTrue(viewModel is ParticipantViewModel)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testCreateUnknownViewModel() {
        class UnknownViewModel : ViewModel()
        factory.create(UnknownViewModel::class.java)
    }
}
