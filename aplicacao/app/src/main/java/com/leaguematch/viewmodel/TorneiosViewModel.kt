package com.leaguematch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leaguematch.data.remote.model.DetalheTorneio
import com.leaguematch.data.remote.model.Equipa
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.data.remote.model.ResumoModalidade
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.data.remote.model.EstatisticaJogo
import com.leaguematch.data.remote.model.EventoJogo
import com.leaguematch.data.remote.model.Utilizador
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

    private val _equipasState = MutableStateFlow<Result<List<Equipa>>?>(null)
    val equipasState: StateFlow<Result<List<Equipa>>?> = _equipasState

    private val _criarJogoLoading = MutableStateFlow(false)
    val criarJogoLoading: StateFlow<Boolean> = _criarJogoLoading

    private val _jogosAoVivoState = MutableStateFlow<Result<List<Jogo>>?>(null)
    val jogosAoVivoState: StateFlow<Result<List<Jogo>>?> = _jogosAoVivoState

    private val _estatisticasJogoState = MutableStateFlow<Result<List<EstatisticaJogo>>?>(null)
    val estatisticasJogoState: StateFlow<Result<List<EstatisticaJogo>>?> = _estatisticasJogoState

    private val _eventosJogoState = MutableStateFlow<Result<List<EventoJogo>>?>(null)
    val eventosJogoState: StateFlow<Result<List<EventoJogo>>?> = _eventosJogoState

    private val _jogadoresEquipaState = MutableStateFlow<List<Utilizador>>(emptyList())
    val jogadoresEquipaState: StateFlow<List<Utilizador>> = _jogadoresEquipaState

    private val _jogadoresLoading = MutableStateFlow(false)
    val jogadoresLoading: StateFlow<Boolean> = _jogadoresLoading

    fun carregarJogadoresEquipa(equipaId: Int) {
        viewModelScope.launch {
            _jogadoresLoading.value = true
            _jogadoresEquipaState.value = repository.listarJogadoresEquipa(equipaId)
            _jogadoresLoading.value = false
        }
    }

    fun removerJogador(equipaId: Int, jogadorId: Int) {
        viewModelScope.launch {
            repository.removerJogadorEquipa(equipaId, jogadorId)
            carregarJogadoresEquipa(equipaId)
        }
    }

    fun carregarJogosAoVivo() {
        viewModelScope.launch {
            _jogosAoVivoState.value = null
            try {
                val data = repository.listarJogosAoVivo()
                _jogosAoVivoState.value = Result.success(data)
            } catch (e: Exception) {
                _jogosAoVivoState.value = Result.failure(e)
            }
        }
    }

    fun carregarEstatisticasJogo(partidaId: Int) {
        viewModelScope.launch {
            _estatisticasJogoState.value = null
            try {
                val data = repository.obterEstatisticasJogo(partidaId)
                _estatisticasJogoState.value = Result.success(data)
            } catch (e: Exception) {
                _estatisticasJogoState.value = Result.failure(e)
            }
        }
    }

    fun carregarEventosJogo(partidaId: Int) {
        viewModelScope.launch {
            _eventosJogoState.value = null
            try {
                val data = repository.obterEventosJogo(partidaId)
                _eventosJogoState.value = Result.success(data)
            } catch (e: Exception) {
                _eventosJogoState.value = Result.failure(e)
            }
        }
    }

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

    fun carregarTorneiosDoOrganizador(organizadorId: Int) {
        viewModelScope.launch {
            _todosTorneiosState.value = null
            try {
                val torneios = repository.listarTorneiosDoOrganizador(organizadorId)
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

    fun carregarEquipas(torneioId: Int) {
        viewModelScope.launch {
            _equipasState.value = null
            try {
                val data = repository.listarEquipasTorneio(torneioId)
                _equipasState.value = Result.success(data)
            } catch (e: Exception) {
                _equipasState.value = Result.failure(e)
            }
        }
    }

    fun criarEquipa(
        nome: String,
        torneioId: Int,
        onSuccess: (Equipa) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val equipa = repository.criarEquipa(nome, torneioId)
                if (equipa != null) {
                    carregarEquipas(torneioId)
                    onSuccess(equipa)
                } else {
                    onError("Não foi possível criar a equipa.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Erro ao criar equipa.")
            }
        }
    }

    fun removerEquipa(
        equipaId: Int,
        torneioId: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.removerEquipa(equipaId)
                carregarEquipas(torneioId)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Erro ao remover equipa.")
            }
        }
    }

    fun editarEquipa(
        equipaId: Int,
        nome: String,
        torneioId: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = repository.atualizarEquipa(equipaId, nome)
                if (result != null) {
                    carregarEquipas(torneioId)
                    onSuccess()
                } else {
                    onError("Não foi possível atualizar a equipa.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Erro ao atualizar equipa.")
            }
        }
    }

    fun removerJogo(
        jogoId: Int,
        torneioId: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.removerJogo(jogoId)
                carregarDetalheTorneio(torneioId)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Erro ao remover jogo.")
            }
        }
    }

    fun editarJogo(
        jogoId: Int,
        torneioId: Int,
        resultadoCasa: Int,
        resultadoFora: Int,
        estado: String,
        local: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = repository.atualizarJogo(jogoId, resultadoCasa, resultadoFora, estado, local)
                if (result != null) {
                    carregarDetalheTorneio(torneioId)
                    onSuccess()
                } else {
                    onError("Não foi possível atualizar o jogo.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Erro ao atualizar jogo.")
            }
        }
    }


    fun criarJogo(
        torneioId: Int,
        equipaCasaId: Int,
        equipaForaId: Int,
        data: String,
        hora: String,
        local: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _criarJogoLoading.value = true
            try {
                val jogo = repository.criarJogo(torneioId, equipaCasaId, equipaForaId, data, hora, local)
                if (jogo != null) {
                    carregarDetalheTorneio(torneioId)
                    onSuccess()
                } else {
                    onError("Não foi possível criar o jogo.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Erro ao criar jogo.")
            } finally {
                _criarJogoLoading.value = false
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
                    carregarTorneiosDoOrganizador(organizadorId)
                    onSuccess()
                } else {
                    onError("Não foi possível criar o torneio.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Erro ao criar torneio")
            }
        }
    }

    fun editarTorneio(
        id: Int,
        nome: String,
        regras: String,
        formato: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = repository.atualizarTorneio(id, nome, regras, formato)
                if (result != null) {
                    carregarDetalheTorneio(id)
                    carregarTorneios()
                    carregarTodosTorneios()
                    onSuccess()
                } else {
                    onError("Não foi possível editar o torneio.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Erro ao editar torneio.")
            }
        }
    }
}