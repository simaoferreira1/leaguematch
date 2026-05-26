package com.leaguematch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leaguematch.data.remote.model.DetalheTorneio
import com.leaguematch.data.remote.model.ResumoModalidade
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.data.repository.LeagueMatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TorneiosViewModel(private val repository: LeagueMatchRepository) : ViewModel() {

    private val _modalidadesState = MutableStateFlow<Result<Pair<List<ResumoModalidade>, Int>>?>(null)
    val modalidadesState: StateFlow<Result<Pair<List<ResumoModalidade>, Int>>?> = _modalidadesState

    private val _todosTorneiosState = MutableStateFlow<Result<List<Torneio>>?>(null)
    val todosTorneiosState: StateFlow<Result<List<Torneio>>?> = _todosTorneiosState

    private val _torneiosState = MutableStateFlow<Result<List<Torneio>>?>(null)
    val torneiosState: StateFlow<Result<List<Torneio>>?> = _torneiosState

    private val _detalheTorneioState = MutableStateFlow<Result<DetalheTorneio?>?>(null)
    val detalheTorneioState: StateFlow<Result<DetalheTorneio?>?> = _detalheTorneioState

    fun carregarTorneios() {
        viewModelScope.launch {
            _modalidadesState.value = null
            try {
                val modalidades = repository.listarModalidades()
                val totalTorneios = repository.obterDashboard().totalTorneios
                _modalidadesState.value = Result.success(modalidades to totalTorneios)
            } catch (e: Exception) {
                _modalidadesState.value = Result.failure(e)
            }
        }
    }

    fun carregarTodosTorneios() {
        viewModelScope.launch {
            _todosTorneiosState.value = null
            try {
                val modalidades = repository.listarModalidades()
                val torneios = modalidades.flatMap { modalidade ->
                    repository.listarTorneiosPorModalidade(modalidade.nome)
                }
                _todosTorneiosState.value = Result.success(torneios)
            } catch (e: Exception) {
                _todosTorneiosState.value = Result.failure(e)
            }
        }
    }

    fun carregarTorneiosPorModalidade(modalidade: String) {
        viewModelScope.launch {
            _torneiosState.value = null
            try {
                val data = repository.listarTorneiosPorModalidade(modalidade)
                _torneiosState.value = Result.success(data)
            } catch (e: Exception) {
                _torneiosState.value = Result.failure(e)
            }
        }
    }

    fun carregarDetalheTorneio(id: Int) {
        viewModelScope.launch {
            _detalheTorneioState.value = null
            try {
                val data = repository.obterDetalheTorneio(id)
                _detalheTorneioState.value = Result.success(data)
            } catch (e: Exception) {
                _detalheTorneioState.value = Result.failure(e)
            }
        }
    }

    fun removerTorneio(id: Int) {
        viewModelScope.launch {
            try {
                repository.removerTorneio(id)
                carregarTorneios()
                carregarTodosTorneios()
            } catch (e: Exception) {
                _todosTorneiosState.value = Result.failure(e)
            }
        }
    }

    fun criarTorneio(
        nome: String,
        modalidade: String,
        regras: String,
        formato: String,
        organizadorId: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = repository.criarTorneio(nome, modalidade, regras, formato, organizadorId)
                if (result != null) {
                    carregarTorneios()
                    carregarTodosTorneios()
                    onSuccess()
                } else {
                    onError("Não foi possível criar o torneio.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Erro ao criar torneio")
            }
        }
    }
}