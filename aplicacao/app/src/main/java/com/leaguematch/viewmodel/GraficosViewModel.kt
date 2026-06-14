/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: GraficosViewModel.kt
 * Tipo: ViewModel (Lógica de Apresentação e Estado)
 *
 * Descrição:
 * Este ficheiro gere o estado da interface (UI State) e a lógica de apresentação para o seu ecrã respetivo.\n * Ele comunica assincronamente com o Repositório de dados e expõe fluxos de dados reativos (StateFlow).\n * Ao rodar o ecrã ou pausar a aplicação, o ViewModel preserva este estado de forma segura no Android.
 */
package com.leaguematch.viewmodel // Define o pacote deste ficheiro de código

import androidx.lifecycle.ViewModel // Importa dependência / biblioteca necessária
import androidx.lifecycle.viewModelScope // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.EstatisticasAdmin // Importa dependência / biblioteca necessária
import com.leaguematch.data.repository.LeagueMatchRepository // Importa dependência / biblioteca necessária
import kotlinx.coroutines.flow.MutableStateFlow // Importa dependência / biblioteca necessária
import kotlinx.coroutines.flow.StateFlow // Importa dependência / biblioteca necessária
import kotlinx.coroutines.launch // Importa dependência / biblioteca necessária

class GraficosViewModel(private val repository: LeagueMatchRepository) : ViewModel() { // Declaração de classe para modelar objetos
    private val _estatisticasState = MutableStateFlow<Result<EstatisticasAdmin>?>(null) // Declara fluxo de estado mutável para controlo no ViewModel
    val estatisticasState: StateFlow<Result<EstatisticasAdmin>?> = _estatisticasState // Declara fluxo de estado de leitura para observação na UI

    fun carregarEstatisticas(periodo: String = "30d") { // Declaração de função / método de lógica
        viewModelScope.launch { // Cria coroutine assíncrona no escopo do ViewModel
            _estatisticasState.value = null
            try { // Tenta executar bloco que pode lançar exceções
                val data = repository.obterEstatisticasAdmin(periodo) // Efetua chamada remota ou local ao repositório de dados
                _estatisticasState.value = Result.success(data)
            } catch (e: Exception) { // Captura e trata eventuais exceções ocorridas no bloco try
                _estatisticasState.value = Result.failure(e)
            }
        }
    }
}
