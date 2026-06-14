/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: UtilizadoresViewModel.kt
 * Tipo: ViewModel (Lógica de Apresentação e Estado)
 *
 * Descrição:
 * Este ficheiro gere o estado da interface (UI State) e a lógica de apresentação para o seu ecrã respetivo.\n * Ele comunica assincronamente com o Repositório de dados e expõe fluxos de dados reativos (StateFlow).\n * Ao rodar o ecrã ou pausar a aplicação, o ViewModel preserva este estado de forma segura no Android.
 */
package com.leaguematch.viewmodel // Define o pacote deste ficheiro de código

import androidx.lifecycle.ViewModel // Importa dependência / biblioteca necessária
import androidx.lifecycle.viewModelScope // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Utilizador // Importa dependência / biblioteca necessária
import com.leaguematch.data.repository.LeagueMatchRepository // Importa dependência / biblioteca necessária
import kotlinx.coroutines.flow.MutableStateFlow // Importa dependência / biblioteca necessária
import kotlinx.coroutines.flow.StateFlow // Importa dependência / biblioteca necessária
import kotlinx.coroutines.launch // Importa dependência / biblioteca necessária

class UtilizadoresViewModel(private val repository: LeagueMatchRepository) : ViewModel() { // Declaração de classe para modelar objetos
    private val _utilizadoresState = MutableStateFlow<Result<List<Utilizador>>?>(null) // Declara fluxo de estado mutável para controlo no ViewModel
    val utilizadoresState: StateFlow<Result<List<Utilizador>>?> = _utilizadoresState // Declara fluxo de estado de leitura para observação na UI

    private val _detalheUtilizadorState = MutableStateFlow<Result<Utilizador?>?>(null) // Declara fluxo de estado mutável para controlo no ViewModel
    val detalheUtilizadorState: StateFlow<Result<Utilizador?>?> = _detalheUtilizadorState // Declara fluxo de estado de leitura para observação na UI

    fun carregarUtilizadores() { // Declaração de função / método de lógica
        viewModelScope.launch { // Cria coroutine assíncrona no escopo do ViewModel
            _utilizadoresState.value = null
            try { // Tenta executar bloco que pode lançar exceções
                val data = repository.listarUtilizadores() // Efetua chamada remota ou local ao repositório de dados
                _utilizadoresState.value = Result.success(data)
            } catch (e: Exception) { // Captura e trata eventuais exceções ocorridas no bloco try
                _utilizadoresState.value = Result.failure(e)
            }
        }
    }

    fun carregarDetalhes(id: Int) { // Declaração de função / método de lógica
        viewModelScope.launch { // Cria coroutine assíncrona no escopo do ViewModel
            _detalheUtilizadorState.value = null
            try { // Tenta executar bloco que pode lançar exceções
                val data = repository.obterUtilizador(id) // Efetua chamada remota ou local ao repositório de dados
                _detalheUtilizadorState.value = Result.success(data)
            } catch (e: Exception) { // Captura e trata eventuais exceções ocorridas no bloco try
                _detalheUtilizadorState.value = Result.failure(e)
            }
        }
    }

    fun alterarEstadoUtilizador( // Declaração de função / método de lógica
        id: Int,
        ativo: Boolean
    ) {
        viewModelScope.launch { // Cria coroutine assíncrona no escopo do ViewModel
            try { // Tenta executar bloco que pode lançar exceções
                repository.alterarEstadoUtilizador(id, ativo) // Efetua chamada remota ou local ao repositório de dados
                carregarDetalhes(id)
                carregarUtilizadores()
            } catch (e: Exception) { // Captura e trata eventuais exceções ocorridas no bloco try
                _detalheUtilizadorState.value = Result.failure(e)
            }
        }
    }
}
