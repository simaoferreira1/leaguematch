package com.leaguematch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.data.repository.LeagueMatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ParticipantViewModel(
    private val repository: LeagueMatchRepository
) : ViewModel() {

    private val _torneios = MutableStateFlow<List<Torneio>>(emptyList())
    val torneios: StateFlow<List<Torneio>> = _torneios

    private val _jogos = MutableStateFlow<List<Jogo>>(emptyList())
    val jogos: StateFlow<List<Jogo>> = _jogos

    init {
        carregarDados()
    }

    fun carregarDados() {
        viewModelScope.launch {
            _torneios.value = repository.listarTorneios()
            _jogos.value = repository.listarTodosJogos()
        }
    }
}