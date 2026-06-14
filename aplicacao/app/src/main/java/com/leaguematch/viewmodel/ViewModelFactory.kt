/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: ViewModelFactory.kt
 * Tipo: ViewModel (Lógica de Apresentação e Estado)
 *
 * Descrição:
 * Este ficheiro gere o estado da interface (UI State) e a lógica de apresentação para o seu ecrã respetivo.\n * Ele comunica assincronamente com o Repositório de dados e expõe fluxos de dados reativos (StateFlow).\n * Ao rodar o ecrã ou pausar a aplicação, o ViewModel preserva este estado de forma segura no Android.
 */
package com.leaguematch.viewmodel // Define o pacote deste ficheiro de código

import androidx.lifecycle.ViewModel // Importa dependência / biblioteca necessária
import androidx.lifecycle.ViewModelProvider // Importa dependência / biblioteca necessária
import com.leaguematch.data.repository.LeagueMatchRepository // Importa dependência / biblioteca necessária

class ViewModelFactory( // Declaração de classe para modelar objetos
    private val repository: LeagueMatchRepository // Declara constante local (leitura única)
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T { // Declaração de função / método de lógica
        return when { // Escolha múltipla condicional (semelhante a switch-case)
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> // Declaração de classe para modelar objetos
                AuthViewModel(repository) as T

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> // Declaração de classe para modelar objetos
                HomeViewModel(repository) as T

            modelClass.isAssignableFrom(UtilizadoresViewModel::class.java) -> // Declaração de classe para modelar objetos
                UtilizadoresViewModel(repository) as T

            modelClass.isAssignableFrom(TorneiosViewModel::class.java) -> // Declaração de classe para modelar objetos
                TorneiosViewModel(repository) as T

            modelClass.isAssignableFrom(GraficosViewModel::class.java) -> // Declaração de classe para modelar objetos
                GraficosViewModel(repository) as T

            modelClass.isAssignableFrom(ParticipantViewModel::class.java) -> // Declaração de classe para modelar objetos
                ParticipantViewModel(repository) as T

            else -> // Fluxo condicional alternativo caso o 'if' seja falso
                throw IllegalArgumentException(
                    "Unknown ViewModel class: ${modelClass.name}" // Declaração de classe para modelar objetos
                )
        }
    }
}