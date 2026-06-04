package com.leaguematch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leaguematch.data.remote.model.*
import com.leaguematch.data.repository.LeagueMatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ParticipantStatsData(
    val jogos: Int = 0,
    val golos: Int = 0,
    val assistencias: Int = 0,
    val mvp: Int = 0
)

class ParticipantViewModel(
    private val repository: LeagueMatchRepository
) : ViewModel() {

    private val _torneios = MutableStateFlow<List<Torneio>>(emptyList())
    val torneios: StateFlow<List<Torneio>> = _torneios

    private val _jogos = MutableStateFlow<List<Jogo>>(emptyList())
    val jogos: StateFlow<List<Jogo>> = _jogos

    private val _equipa = MutableStateFlow<Equipa?>(null)
    val equipa: StateFlow<Equipa?> = _equipa

    private val _jogadoresEquipa = MutableStateFlow<List<Utilizador>>(emptyList())
    val jogadoresEquipa: StateFlow<List<Utilizador>> = _jogadoresEquipa

    private val _classificacaoEquipa = MutableStateFlow<Classificacao?>(null)
    val classificacaoEquipa: StateFlow<Classificacao?> = _classificacaoEquipa

    private val _jogosEquipa = MutableStateFlow<List<Jogo>>(emptyList())
    val jogosEquipa: StateFlow<List<Jogo>> = _jogosEquipa

    private val _detalheTorneio = MutableStateFlow<DetalheTorneio?>(null)
    val detalheTorneio: StateFlow<DetalheTorneio?> = _detalheTorneio

    private val _classificacaoTorneio = MutableStateFlow<List<Classificacao>>(emptyList())
    val classificacaoTorneio: StateFlow<List<Classificacao>> = _classificacaoTorneio

    private val _statsParticipante = MutableStateFlow(ParticipantStatsData())
    val statsParticipante: StateFlow<ParticipantStatsData> = _statsParticipante

    private val _configuracaoNotificacoes = MutableStateFlow<ConfiguracaoNotificacoes?>(null)
    val configuracaoNotificacoes: StateFlow<ConfiguracaoNotificacoes?> = _configuracaoNotificacoes

    private val _juntarEquipaLoading = MutableStateFlow(false)
    val juntarEquipaLoading: StateFlow<Boolean> = _juntarEquipaLoading

    private val _juntarEquipaErro = MutableStateFlow<String?>(null)
    val juntarEquipaErro: StateFlow<String?> = _juntarEquipaErro

    fun carregarDadosParticipante(utilizadorId: Int) {
        viewModelScope.launch {
            _torneios.value = emptyList()
            _jogos.value = emptyList()

            val equipaEncontrada = repository.obterEquipaDoParticipante(utilizadorId)
            _equipa.value = equipaEncontrada

            _configuracaoNotificacoes.value =
                repository.obterConfiguracaoNotificacoes(utilizadorId)

            if (equipaEncontrada != null) {
                _jogadoresEquipa.value =
                    repository.listarJogadoresEquipa(equipaEncontrada.id)

                _classificacaoEquipa.value =
                    repository.obterClassificacaoEquipa(
                        equipaId = equipaEncontrada.id,
                        torneioId = equipaEncontrada.torneioId
                    )

                val jogosDaEquipa = repository.listarJogosDaEquipa(equipaEncontrada.id)
                _jogosEquipa.value = jogosDaEquipa
                _jogos.value = jogosDaEquipa

                _torneios.value = repository.listarTorneios()
                    .filter { torneio ->
                        torneio.id == equipaEncontrada.torneioId
                    }

                _statsParticipante.value =
                    repository.obterEstatisticasParticipante(
                        utilizadorId = utilizadorId,
                        equipaId = equipaEncontrada.id
                    )
            } else {
                _jogadoresEquipa.value = emptyList()
                _classificacaoEquipa.value = null
                _jogosEquipa.value = emptyList()
                _statsParticipante.value = ParticipantStatsData()
            }
        }
    }

    fun carregarDetalheTorneio(torneioId: Int) {
        viewModelScope.launch {
            _detalheTorneio.value = null
            _classificacaoTorneio.value = emptyList()

            _detalheTorneio.value = repository.obterDetalheTorneio(torneioId)
            _classificacaoTorneio.value = repository.obterClassificacao(torneioId)
        }
    }

    fun juntarEquipa(
        utilizadorId: Int,
        codigo: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _juntarEquipaErro.value = null
            _juntarEquipaLoading.value = true
            val resultado = repository.juntarEquipaPorCodigo(utilizadorId, codigo)
            _juntarEquipaLoading.value = false
            resultado
                .onSuccess {
                    carregarDadosParticipante(utilizadorId)
                    onSuccess()
                }
                .onFailure {
                    _juntarEquipaErro.value = it.message ?: "Não foi possível entrar na equipa."
                }
        }
    }

    fun limparErroJuntarEquipa() {
        _juntarEquipaErro.value = null
    }

    fun atualizarConfiguracaoNotificacoes(configuracao: ConfiguracaoNotificacoes) {
        viewModelScope.launch {
            val novaConfiguracao =
                repository.atualizarConfiguracaoNotificacoes(configuracao)

            if (novaConfiguracao != null) {
                _configuracaoNotificacoes.value = novaConfiguracao
            }
        }
    }
}