/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: HomeViewModel.kt
 * Tipo: ViewModel (Lógica de Apresentação e Estado)
 *
 * Descrição:
 * Este ficheiro gere o estado da interface (UI State) e a lógica de apresentação para o seu ecrã respetivo.\n * Ele comunica assincronamente com o Repositório de dados e expõe fluxos de dados reativos (StateFlow).\n * Ao rodar o ecrã ou pausar a aplicação, o ViewModel preserva este estado de forma segura no Android.
 */
package com.leaguematch.viewmodel // Define o pacote deste ficheiro de código

import androidx.lifecycle.ViewModel // Importa dependência / biblioteca necessária
import androidx.lifecycle.viewModelScope // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.ResumoDashboard // Importa dependência / biblioteca necessária
import com.leaguematch.data.repository.LeagueMatchRepository // Importa dependência / biblioteca necessária
import kotlinx.coroutines.flow.MutableStateFlow // Importa dependência / biblioteca necessária
import kotlinx.coroutines.flow.StateFlow // Importa dependência / biblioteca necessária
import kotlinx.coroutines.launch // Importa dependência / biblioteca necessária

class HomeViewModel(private val repository: LeagueMatchRepository) : ViewModel() { // Declaração de classe para modelar objetos
    private val _dashboardState = MutableStateFlow<Result<ResumoDashboard>?>(null) // Declara fluxo de estado mutável para controlo no ViewModel
    val dashboardState: StateFlow<Result<ResumoDashboard>?> = _dashboardState // Declara fluxo de estado de leitura para observação na UI

    fun carregarDashboard() { // Declaração de função / método de lógica
        viewModelScope.launch { // Cria coroutine assíncrona no escopo do ViewModel
            _dashboardState.value = null
            try { // Tenta executar bloco que pode lançar exceções
                val data = repository.obterDashboard() // Efetua chamada remota ou local ao repositório de dados
                _dashboardState.value = Result.success(data)
            } catch (e: Exception) { // Captura e trata eventuais exceções ocorridas no bloco try
                _dashboardState.value = Result.failure(e)
            }
        }
    }
}
