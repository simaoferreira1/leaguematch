package com.leaguematch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.data.repository.LeagueMatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UtilizadoresViewModel(private val repository: LeagueMatchRepository) : ViewModel() {
    private val _utilizadoresState = MutableStateFlow<Result<List<Utilizador>>?>(null)
    val utilizadoresState: StateFlow<Result<List<Utilizador>>?> = _utilizadoresState

    private val _detalheUtilizadorState = MutableStateFlow<Result<Utilizador?>?>(null)
    val detalheUtilizadorState: StateFlow<Result<Utilizador?>?> = _detalheUtilizadorState

    fun carregarUtilizadores() {
        viewModelScope.launch {
            _utilizadoresState.value = null
            try {
                val data = repository.listarUtilizadores()
                _utilizadoresState.value = Result.success(data)
            } catch (e: Exception) {
                _utilizadoresState.value = Result.failure(e)
            }
        }
    }

    fun carregarDetalhes(id: Int) {
        viewModelScope.launch {
            _detalheUtilizadorState.value = null
            try {
                val data = repository.obterUtilizador(id)
                _detalheUtilizadorState.value = Result.success(data)
            } catch (e: Exception) {
                _detalheUtilizadorState.value = Result.failure(e)
            }
        }
    }
}
