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

    private var equipaSelecionadaId: Int? = null

    private val _torneios = MutableStateFlow<List<Torneio>>(emptyList())
    val torneios: StateFlow<List<Torneio>> = _torneios

    private val _jogos = MutableStateFlow<List<Jogo>>(emptyList())
    val jogos: StateFlow<List<Jogo>> = _jogos

    private val _equipa = MutableStateFlow<Equipa?>(null)
    val equipa: StateFlow<Equipa?> = _equipa

    private val _equipas = MutableStateFlow<List<Equipa>>(emptyList())
    val equipas: StateFlow<List<Equipa>> = _equipas

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

            val equipasEncontradas = repository.listarEquipasDoParticipante(utilizadorId)
            _equipas.value = equipasEncontradas

            if (equipasEncontradas.none { it.id == equipaSelecionadaId }) {
                equipaSelecionadaId = equipasEncontradas.firstOrNull()?.id
            }

            val equipaPrincipal = equipasEncontradas.firstOrNull {
                it.id == equipaSelecionadaId
            }

            _equipa.value = equipaPrincipal

            _configuracaoNotificacoes.value =
                repository.obterConfiguracaoNotificacoes(utilizadorId)

            if (equipaPrincipal != null) {
                _jogadoresEquipa.value =
                    repository.listarJogadoresEquipa(equipaPrincipal.id)

                _classificacaoEquipa.value =
                    repository.obterClassificacaoEquipa(
                        equipaId = equipaPrincipal.id,
                        torneioId = equipaPrincipal.torneioId
                    )

                val jogosDeTodasEquipas = equipasEncontradas
                    .flatMap { equipa ->
                        repository.listarJogosDaEquipa(equipa.id)
                    }
                    .distinctBy { it.id }

                _jogosEquipa.value = jogosDeTodasEquipas
                _jogos.value = jogosDeTodasEquipas

                _torneios.value = repository.listarTorneios()

                _statsParticipante.value =
                    repository.obterEstatisticasParticipante(
                        utilizadorId = utilizadorId,
                        equipaId = equipaPrincipal.id
                    )
            } else {
                equipaSelecionadaId = null
                _jogadoresEquipa.value = emptyList()
                _classificacaoEquipa.value = null
                _jogosEquipa.value = emptyList()
                _jogos.value = emptyList()
                _torneios.value = repository.listarTorneios()
                _statsParticipante.value = ParticipantStatsData()
            }
        }
    }

    fun selecionarEquipa(
        utilizadorId: Int,
        equipaId: Int
    ) {
        equipaSelecionadaId = equipaId
        carregarDadosParticipante(utilizadorId)
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
                .onSuccess { equipa ->
                    equipaSelecionadaId = equipa.id
                    carregarDadosParticipante(utilizadorId)
                    onSuccess()
                }
                .onFailure {
                    _juntarEquipaErro.value =
                        it.message ?: "Não foi possível entrar na equipa."
                }
        }
    }

    fun sairDaEquipa(
        utilizadorId: Int,
        equipaId: Int
    ) {
        viewModelScope.launch {
            val removido = repository.removerJogadorEquipa(
                equipaId = equipaId,
                utilizadorId = utilizadorId
            )

            if (removido) {
                if (equipaSelecionadaId == equipaId) {
                    equipaSelecionadaId = null
                }

                carregarDadosParticipante(utilizadorId)
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