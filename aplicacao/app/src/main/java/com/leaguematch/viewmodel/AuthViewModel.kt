package com.leaguematch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.data.repository.LeagueMatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: LeagueMatchRepository) : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _usuarioLogado = MutableStateFlow<Utilizador?>(null)
    val usuarioLogado: StateFlow<Utilizador?> = _usuarioLogado

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess: StateFlow<Boolean> = _registerSuccess


    fun autenticar(email: String, password: String) {
        viewModelScope.launch {
            _loginError.value = null
            try {
                val utilizador = repository.autenticar(email, password)
                if (utilizador != null) {
                    _usuarioLogado.value = utilizador
                    _isLoggedIn.value = true
                } else {
                    _loginError.value = "Credenciais inválidas"
                }
            } catch (e: Exception) {
                _loginError.value = e.message ?: "Erro ao realizar login"
            }
        }
    }

    fun registar(nome: String, email: String, password: String, tipo: String) {
        viewModelScope.launch {
            _loginError.value = null
            _registerSuccess.value = false
            try {
                val utilizador = repository.registar(nome, email, password, tipo)
                if (utilizador != null) {
                    _registerSuccess.value = true
                } else {
                    _loginError.value = "Não foi possível criar a conta"
                }
            } catch (e: Exception) {
                _loginError.value = e.message ?: "Erro ao realizar registo"
            }
        }
    }

    fun atualizarUtilizador(nome: String, password: String?) {
        val currentUser = _usuarioLogado.value ?: return
        viewModelScope.launch {
            _loginError.value = null
            try {
                val updated = repository.atualizarUtilizador(currentUser.id, nome, password)
                if (updated != null) {
                    // Update user state reactively (preserving the email and type if not returned, though we map them in toUtilizador)
                    _usuarioLogado.value = currentUser.copy(
                        nome = updated.nome
                    )
                } else {
                    _loginError.value = "Não foi possível atualizar o perfil"
                }
            } catch (e: Exception) {
                _loginError.value = e.message ?: "Erro ao atualizar perfil"
            }
        }
    }

    fun resetRegisterState() {
        _registerSuccess.value = false
        _loginError.value = null
    }

    fun terminarSessao() {
        _isLoggedIn.value = false
        _usuarioLogado.value = null
        _loginError.value = null
        _registerSuccess.value = false
    }
}
