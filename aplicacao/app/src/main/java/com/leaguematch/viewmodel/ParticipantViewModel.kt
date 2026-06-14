/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: ParticipantViewModel.kt
 * Tipo: ViewModel (Lógica de Apresentação e Estado)
 *
 * Descrição:
 * Este ficheiro gere o estado da interface (UI State) e a lógica de apresentação para o seu ecrã respetivo.\n * Ele comunica assincronamente com o Repositório de dados e expõe fluxos de dados reativos (StateFlow).\n * Ao rodar o ecrã ou pausar a aplicação, o ViewModel preserva este estado de forma segura no Android.
 */
package com.leaguematch.viewmodel // Define o pacote deste ficheiro de código

import androidx.lifecycle.ViewModel // Importa dependência / biblioteca necessária
import androidx.lifecycle.viewModelScope // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.* // Importa dependência / biblioteca necessária
import com.leaguematch.data.repository.LeagueMatchRepository // Importa dependência / biblioteca necessária
import kotlinx.coroutines.flow.MutableStateFlow // Importa dependência / biblioteca necessária
import kotlinx.coroutines.flow.StateFlow // Importa dependência / biblioteca necessária
import kotlinx.coroutines.launch // Importa dependência / biblioteca necessária

data class ParticipantStatsData( // Declaração de classe para modelar objetos
    val jogos: Int = 0, // Declara constante local (leitura única)
    val golos: Int = 0, // Declara constante local (leitura única)
    val faltas: Int = 0, // Declara constante local (leitura única)
    val cartoes: Int = 0 // Declara constante local (leitura única)
)

class ParticipantViewModel( // Declaração de classe para modelar objetos
    private val repository: LeagueMatchRepository // Declara constante local (leitura única)
) : ViewModel() {

    private var equipaSelecionadaId: Int? = null // Declara variável local (leitura e escrita)

    private val _torneios = MutableStateFlow<List<Torneio>>(emptyList()) // Declara fluxo de estado mutável para controlo no ViewModel
    val torneios: StateFlow<List<Torneio>> = _torneios // Declara fluxo de estado de leitura para observação na UI

    private val _jogos = MutableStateFlow<List<Jogo>>(emptyList()) // Declara fluxo de estado mutável para controlo no ViewModel
    val jogos: StateFlow<List<Jogo>> = _jogos // Declara fluxo de estado de leitura para observação na UI

    private val _equipa = MutableStateFlow<Equipa?>(null) // Declara fluxo de estado mutável para controlo no ViewModel
    val equipa: StateFlow<Equipa?> = _equipa // Declara fluxo de estado de leitura para observação na UI

    private val _equipas = MutableStateFlow<List<Equipa>>(emptyList()) // Declara fluxo de estado mutável para controlo no ViewModel
    val equipas: StateFlow<List<Equipa>> = _equipas // Declara fluxo de estado de leitura para observação na UI

    private val _jogadoresEquipa = MutableStateFlow<List<Utilizador>>(emptyList()) // Declara fluxo de estado mutável para controlo no ViewModel
    val jogadoresEquipa: StateFlow<List<Utilizador>> = _jogadoresEquipa // Declara fluxo de estado de leitura para observação na UI

    private val _classificacaoEquipa = MutableStateFlow<Classificacao?>(null) // Declara fluxo de estado mutável para controlo no ViewModel
    val classificacaoEquipa: StateFlow<Classificacao?> = _classificacaoEquipa // Declara fluxo de estado de leitura para observação na UI

    private val _jogosEquipa = MutableStateFlow<List<Jogo>>(emptyList()) // Declara fluxo de estado mutável para controlo no ViewModel
    val jogosEquipa: StateFlow<List<Jogo>> = _jogosEquipa // Declara fluxo de estado de leitura para observação na UI

    private val _detalheTorneio = MutableStateFlow<DetalheTorneio?>(null) // Declara fluxo de estado mutável para controlo no ViewModel
    val detalheTorneio: StateFlow<DetalheTorneio?> = _detalheTorneio // Declara fluxo de estado de leitura para observação na UI

    private val _classificacaoTorneio = MutableStateFlow<List<Classificacao>>(emptyList()) // Declara fluxo de estado mutável para controlo no ViewModel
    val classificacaoTorneio: StateFlow<List<Classificacao>> = _classificacaoTorneio // Declara fluxo de estado de leitura para observação na UI

    private val _statsParticipante = MutableStateFlow(ParticipantStatsData()) // Declara fluxo de estado mutável para controlo no ViewModel
    val statsParticipante: StateFlow<ParticipantStatsData> = _statsParticipante // Declara fluxo de estado de leitura para observação na UI

    private val _configuracaoNotificacoes = MutableStateFlow<ConfiguracaoNotificacoes?>(null) // Declara fluxo de estado mutável para controlo no ViewModel
    val configuracaoNotificacoes: StateFlow<ConfiguracaoNotificacoes?> = _configuracaoNotificacoes // Declara fluxo de estado de leitura para observação na UI

    private val _notificacoesParticipante = MutableStateFlow<List<NotificacaoItem>>(emptyList()) // Declara fluxo de estado mutável para controlo no ViewModel
    val notificacoesParticipante: StateFlow<List<NotificacaoItem>> = _notificacoesParticipante // Declara fluxo de estado de leitura para observação na UI

    private val _juntarEquipaLoading = MutableStateFlow(false) // Declara fluxo de estado mutável para controlo no ViewModel
    val juntarEquipaLoading: StateFlow<Boolean> = _juntarEquipaLoading // Declara fluxo de estado de leitura para observação na UI

    private val _juntarEquipaErro = MutableStateFlow<String?>(null) // Declara fluxo de estado mutável para controlo no ViewModel
    val juntarEquipaErro: StateFlow<String?> = _juntarEquipaErro // Declara fluxo de estado de leitura para observação na UI

    fun carregarDadosParticipante(utilizadorId: Int) { // Declaração de função / método de lógica
        viewModelScope.launch { // Cria coroutine assíncrona no escopo do ViewModel
            _torneios.value = emptyList()
            _jogos.value = emptyList()

            val equipasEncontradas = repository.listarEquipasDoParticipante(utilizadorId) // Efetua chamada remota ou local ao repositório de dados
            _equipas.value = equipasEncontradas

            if (equipasEncontradas.none { it.id == equipaSelecionadaId }) { // Estrutura de decisão condicional principal
                equipaSelecionadaId = equipasEncontradas.firstOrNull()?.id
            }

            val equipaPrincipal = equipasEncontradas.firstOrNull { // Declara constante local (leitura única)
                it.id == equipaSelecionadaId
            }

            _equipa.value = equipaPrincipal

            _configuracaoNotificacoes.value =
                repository.obterConfiguracaoNotificacoes(utilizadorId) // Efetua chamada remota ou local ao repositório de dados
            carregarNotificacoesParticipante(utilizadorId)

            if (equipaPrincipal != null) { // Estrutura de decisão condicional principal
                _jogadoresEquipa.value =
                    repository.listarJogadoresEquipa(equipaPrincipal.id) // Efetua chamada remota ou local ao repositório de dados

                _classificacaoEquipa.value =
                    repository.obterClassificacaoEquipa( // Efetua chamada remota ou local ao repositório de dados
                        equipaId = equipaPrincipal.id,
                        torneioId = equipaPrincipal.torneioId
                    )

                val jogosDeTodasEquipas = equipasEncontradas // Declara constante local (leitura única)
                    .flatMap { equipa ->
                        repository.listarJogosDaEquipa(equipa.id) // Efetua chamada remota ou local ao repositório de dados
                    }
                    .distinctBy { it.id }

                _jogosEquipa.value = jogosDeTodasEquipas
                _jogos.value = jogosDeTodasEquipas

                _torneios.value = repository.listarTorneios() // Efetua chamada remota ou local ao repositório de dados

                _statsParticipante.value =
                    repository.obterEstatisticasParticipante( // Efetua chamada remota ou local ao repositório de dados
                        utilizadorId = utilizadorId,
                        equipaId = equipaPrincipal.id
                    )
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                equipaSelecionadaId = null
                _jogadoresEquipa.value = emptyList()
                _classificacaoEquipa.value = null
                _jogosEquipa.value = emptyList()
                _jogos.value = emptyList()
                _torneios.value = repository.listarTorneios() // Efetua chamada remota ou local ao repositório de dados
                _statsParticipante.value = ParticipantStatsData()
            }
        }
    }

    fun selecionarEquipa( // Declaração de função / método de lógica
        utilizadorId: Int,
        equipaId: Int
    ) {
        equipaSelecionadaId = equipaId
        carregarDadosParticipante(utilizadorId)
    }

    fun carregarDetalheTorneio(torneioId: Int) { // Declaração de função / método de lógica
        viewModelScope.launch { // Cria coroutine assíncrona no escopo do ViewModel
            _detalheTorneio.value = null
            _classificacaoTorneio.value = emptyList()

            _detalheTorneio.value = repository.obterDetalheTorneio(torneioId) // Efetua chamada remota ou local ao repositório de dados
            _classificacaoTorneio.value = repository.obterClassificacao(torneioId) // Efetua chamada remota ou local ao repositório de dados
        }
    }

    fun juntarEquipa( // Declaração de função / método de lógica
        utilizadorId: Int,
        codigo: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch { // Cria coroutine assíncrona no escopo do ViewModel
            _juntarEquipaErro.value = null
            _juntarEquipaLoading.value = true

            val resultado = repository.juntarEquipaPorCodigo(utilizadorId, codigo) // Efetua chamada remota ou local ao repositório de dados

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

    fun sairDaEquipa( // Declaração de função / método de lógica
        utilizadorId: Int,
        equipaId: Int
    ) {
        viewModelScope.launch { // Cria coroutine assíncrona no escopo do ViewModel
            val removido = repository.removerJogadorEquipa( // Efetua chamada remota ou local ao repositório de dados
                equipaId = equipaId,
                utilizadorId = utilizadorId
            )

            if (removido) { // Estrutura de decisão condicional principal
                if (equipaSelecionadaId == equipaId) { // Estrutura de decisão condicional principal
                    equipaSelecionadaId = null
                }

                carregarDadosParticipante(utilizadorId)
            }
        }
    }

    fun limparErroJuntarEquipa() { // Declaração de função / método de lógica
        _juntarEquipaErro.value = null
    }

    fun atualizarConfiguracaoNotificacoes(configuracao: ConfiguracaoNotificacoes) { // Declaração de função / método de lógica
        viewModelScope.launch { // Cria coroutine assíncrona no escopo do ViewModel
            val novaConfiguracao = // Declara constante local (leitura única)
                repository.atualizarConfiguracaoNotificacoes(configuracao) // Efetua chamada remota ou local ao repositório de dados

            if (novaConfiguracao != null) { // Estrutura de decisão condicional principal
                _configuracaoNotificacoes.value = novaConfiguracao
            }
        }
    }
    fun carregarNotificacoesParticipante(utilizadorId: Int) { // Declaração de função / método de lógica
        viewModelScope.launch { // Cria coroutine assíncrona no escopo do ViewModel
            _notificacoesParticipante.value =
                repository.listarNotificacoes(utilizadorId) // Efetua chamada remota ou local ao repositório de dados
        }
    }

    fun marcarNotificacaoComoLida( // Declaração de função / método de lógica
        utilizadorId: Int,
        notificacaoId: Int
    ) {
        viewModelScope.launch { // Cria coroutine assíncrona no escopo do ViewModel
            repository.marcarNotificacaoLida(notificacaoId) // Efetua chamada remota ou local ao repositório de dados

            _notificacoesParticipante.value =
                repository.listarNotificacoes(utilizadorId) // Efetua chamada remota ou local ao repositório de dados
        }
    }

    fun marcarTodasNotificacoesComoLidas(utilizadorId: Int) { // Declaração de função / método de lógica
        viewModelScope.launch { // Cria coroutine assíncrona no escopo do ViewModel
            repository.marcarTodasNotificacoesLidas(utilizadorId) // Efetua chamada remota ou local ao repositório de dados

            _notificacoesParticipante.value =
                repository.listarNotificacoes(utilizadorId) // Efetua chamada remota ou local ao repositório de dados
        }
    }
}