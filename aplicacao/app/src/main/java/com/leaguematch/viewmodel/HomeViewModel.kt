package com.leaguematch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leaguematch.data.remote.model.ResumoDashboard
import com.leaguematch.data.repository.LeagueMatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: LeagueMatchRepository) : ViewModel() {
    private val _dashboardState = MutableStateFlow<Result<ResumoDashboard>?>(null)
    val dashboardState: StateFlow<Result<ResumoDashboard>?> = _dashboardState

    fun carregarDashboard() {
        viewModelScope.launch {
            _dashboardState.value = null
            try {
                val data = repository.obterDashboard()
                _dashboardState.value = Result.success(data)
            } catch (e: Exception) {
                _dashboardState.value = Result.failure(e)
            }
        }
    }
}
