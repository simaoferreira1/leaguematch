package com.leaguematch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.data.repository.LeagueMatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * O AuthViewModel gere o estado de autenticação e sessão do utilizador ativo na app.
 *
 * Conceitos importantes:
 * 1. **Encapsulamento de Estado**: Usamos `MutableStateFlow` (privado, mutável, prefixado com sublinhado) 
 *    e expomos apenas um `StateFlow` (público, imutável) para a UI. Isto impede que a UI altere o estado diretamente.
 * 2. **viewModelScope.launch**: Cria e executa uma Coroutine associada ao ciclo de vida do ViewModel. 
 *    Se o utilizador rodar o ecrã ou sair da app, as coroutines em execução são canceladas automaticamente, evitando fugas de memória.
 * 3. **Gestão de Sessão**: Guarda o objeto `Utilizador` logado para que toda a aplicação saiba quem é o utilizador ativo.
 */
class AuthViewModel(private val repository: LeagueMatchRepository) : ViewModel() { // ViewModel de autenticação recebendo a interface de repositório

    // Fluxo de estado mutável que indica se o utilizador está logado
    private val _isLoggedIn = MutableStateFlow(false) 
    // Fluxo exposto publicamente como apenas de leitura para a UI observar o estado de login
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn 

    // Fluxo de estado mutável que armazena os dados do utilizador autenticado
    private val _usuarioLogado = MutableStateFlow<Utilizador?>(null) 
    // Fluxo público apenas de leitura com os dados do utilizador logado ativo
    val usuarioLogado: StateFlow<Utilizador?> = _usuarioLogado 

    // Fluxo mutável para guardar mensagens de erro de login
    private val _loginError = MutableStateFlow<String?>(null) 
    // Fluxo público correspondente para ler mensagens de erro de login na UI
    val loginError: StateFlow<String?> = _loginError 

    // Fluxo mutável para sinalizar o sucesso no registo de uma nova conta
    private val _registerSuccess = MutableStateFlow(false) 
    // Fluxo público associado para o ecrã de registo reagir ao sucesso
    val registerSuccess: StateFlow<Boolean> = _registerSuccess 


    fun autenticar(email: String, password: String) { // Função para iniciar sessão
        viewModelScope.launch { // Inicia uma Coroutine associada ao ciclo de vida deste ViewModel
            _loginError.value = null // Reseta erros de autenticação anteriores
            try { // Bloco de captura de erros para operações de rede asíncronas
                val utilizador = repository.autenticar(email, password) // Efetua chamada assíncrona ao repositório
                if (utilizador != null) { // Se o utilizador foi autenticado com sucesso
                    _usuarioLogado.value = utilizador // Grava os dados do utilizador no fluxo de sessão
                    _isLoggedIn.value = true // Define a flag de login ativo para verdadeiro
                } else { // Caso contrário (credenciais inválidas ou nulas)
                    _loginError.value = "Credenciais inválidas" // Define mensagem de erro na UI
                } // Fim do if
            } catch (e: Exception) { // Apanha exceções físicas (ex: sem ligação ao servidor)
                _loginError.value = e.message ?: "Erro ao realizar login" // Expõe o erro do sistema
            } // Fim do try-catch
        } // Fim da coroutine
    } // Fim de autenticar

    fun registar(nome: String, email: String, password: String, tipo: String) { // Cria nova conta de utilizador
        viewModelScope.launch { // Executa a chamada em background
            _loginError.value = null // Reseta erros do ecrã
            _registerSuccess.value = false // Define o sucesso inicial a falso
            try { // Bloco de segurança para chamadas remotas de escrita
                val utilizador = repository.registar(nome, email, password, tipo) // Executa pedido de criação de conta
                if (utilizador != null) { // Se o utilizador foi criado e devolvido com sucesso
                    _registerSuccess.value = true // Sinaliza sucesso à UI Compose
                } else { // Caso contrário
                    _loginError.value = "Não foi possível criar a conta" // Define a mensagem de erro correspondente
                } // Fim do if
            } catch (e: Exception) { // Apanha erros na inserção de dados
                _loginError.value = e.message ?: "Erro ao realizar registo" // Define mensagem de erro
            } // Fim do try-catch
        } // Fim da coroutine
    } // Fim de registar

    fun atualizarUtilizador(nome: String, password: String?) { // Atualiza dados cadastrais
        val currentUser = _usuarioLogado.value ?: return // Verifica se há uma sessão ativa; se não, aborta
        viewModelScope.launch { // Corre a alteração assincronamente
            _loginError.value = null // Limpa erros
            try { // Bloco protetivo de escrita na BD remota
                val updated = repository.atualizarUtilizador(currentUser.id, nome, password) // Efetua o patch
                if (updated != null) { // Se o servidor confirmou e devolveu a linha atualizada
                    _usuarioLogado.value = currentUser.copy( // Atualiza o estado da sessão local mantendo a consistência dos dados
                        nome = updated.nome // Altera o nome reativo na UI
                    ) // Fim do copy
                } else { // Caso ocorra erro de base de dados
                    _loginError.value = "Não foi possível atualizar o perfil" // Avisa a UI
                } // Fim do if
            } catch (e: Exception) { // Apanha erros de ligação
                _loginError.value = e.message ?: "Erro ao atualizar perfil" // Propaga erro
            } // Fim do try-catch
        } // Fim da coroutine
    } // Fim de atualizarUtilizador

    fun resetRegisterState() { // Método para resetar estados visuais nos formulários
        _registerSuccess.value = false // Reseta flag de sucesso
        _loginError.value = null // Reseta mensagens de erro acumuladas
    } // Fim de resetRegisterState

    fun resetPasswordPorAdmin(email: String, novaPassword: String, onResult: (Boolean) -> Unit) { // Permite redefinir password (usado por Admin)
        viewModelScope.launch { // Executa redefinição remota em coroutine
            val ok = try { // Tenta executar o reset na API
                repository.redefinirPasswordPorEmail(email, novaPassword) // Efetua o patch na tabela do utilizador
            } catch (e: Exception) { // Em caso de falha de rede
                false // Retorna falso no try
            } // Fim do try-catch
            onResult(ok) // Notifica o resultado à UI através da função lambda de callback
        } // Fim da coroutine
    } // Fim de resetPasswordPorAdmin

    fun terminarSessao() { // Método de Log out
        _isLoggedIn.value = false // Define login ativo como falso (UI redireciona para Login)
        _usuarioLogado.value = null // Remove os dados da sessão local do utilizador
        _loginError.value = null // Limpa eventuais erros
        _registerSuccess.value = false // Limpa sucesso de registo
    } // Fim de terminarSessao
}
