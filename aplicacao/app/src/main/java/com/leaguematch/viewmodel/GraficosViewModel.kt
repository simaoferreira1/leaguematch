package com.leaguematch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leaguematch.data.remote.model.EstatisticasAdmin
import com.leaguematch.data.repository.LeagueMatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GraficosViewModel(private val repository: LeagueMatchRepository) : ViewModel() {
    private val _estatisticasState = MutableStateFlow<Result<EstatisticasAdmin>?>(null)
    val estatisticasState: StateFlow<Result<EstatisticasAdmin>?> = _estatisticasState

    fun carregarEstatisticas(periodo: String = "30d") {
        viewModelScope.launch {
            _estatisticasState.value = null
            try {
                val data = repository.obterEstatisticasAdmin(periodo)
                _estatisticasState.value = Result.success(data)
            } catch (e: Exception) {
                _estatisticasState.value = Result.failure(e)
            }
        }
    }
}
