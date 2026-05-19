package com.leaguematch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leaguematch.data.repository.LeagueMatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: LeagueMatchRepository) : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    fun autenticar(email: String, password: String) {
        viewModelScope.launch {
            _loginError.value = null
            try {
                val utilizador = repository.autenticar(email, password)
                if (utilizador != null) {
                    _isLoggedIn.value = true
                } else {
                    _loginError.value = "Credenciais inválidas"
                }
            } catch (e: Exception) {
                _loginError.value = e.message ?: "Erro ao realizar login"
            }
        }
    }

    fun terminarSessao() {
        _isLoggedIn.value = false
        _loginError.value = null
    }
}
