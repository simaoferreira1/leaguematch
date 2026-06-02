package com.leaguematch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.leaguematch.data.repository.LeagueMatchRepository

class ViewModelFactory(
    private val repository: LeagueMatchRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(repository) as T

            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(repository) as T

            modelClass.isAssignableFrom(UtilizadoresViewModel::class.java) ->
                UtilizadoresViewModel(repository) as T

            modelClass.isAssignableFrom(TorneiosViewModel::class.java) ->
                TorneiosViewModel(repository) as T

            modelClass.isAssignableFrom(GraficosViewModel::class.java) ->
                GraficosViewModel(repository) as T

            modelClass.isAssignableFrom(ParticipantViewModel::class.java) ->
                ParticipantViewModel(repository) as T

            else ->
                throw IllegalArgumentException(
                    "Unknown ViewModel class: ${modelClass.name}"
                )
        }
    }
}