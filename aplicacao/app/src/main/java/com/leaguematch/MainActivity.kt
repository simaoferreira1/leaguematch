package com.leaguematch

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import com.leaguematch.data.remote.model.TipoUtilizador
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.data.repository.SupabaseLeagueMatchRepository
import com.leaguematch.ui.admin.AdminFlowContainer
import com.leaguematch.ui.auth.LoginScreen
import com.leaguematch.ui.auth.RegisterScreen
import com.leaguematch.ui.spectator.SpectatorFlowContainer
import com.leaguematch.ui.organizer.OrganizerFlowContainer
import com.leaguematch.ui.theme.LeagueMatchTheme
import com.leaguematch.viewmodel.*







class MainActivity : ComponentActivity() {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var utilizadoresViewModel: UtilizadoresViewModel
    private lateinit var torneiosViewModel: TorneiosViewModel
    private lateinit var graficosViewModel: GraficosViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = SupabaseLeagueMatchRepository(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            anonKey = BuildConfig.SUPABASE_ANON_KEY,
        )
        val factory = ViewModelFactory(repository)

        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        utilizadoresViewModel = ViewModelProvider(this, factory)[UtilizadoresViewModel::class.java]
        torneiosViewModel = ViewModelProvider(this, factory)[TorneiosViewModel::class.java]
        graficosViewModel = ViewModelProvider(this, factory)[GraficosViewModel::class.java]

        setContent {
            LeagueMatchTheme {
                val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
                val loginError by authViewModel.loginError.collectAsState()
                val registerSuccess by authViewModel.registerSuccess.collectAsState()
                var showRegisterScreen by remember { mutableStateOf(false) }



                if (!isLoggedIn) {
                    if (showRegisterScreen) {
                        RegisterScreen(
                            erro = loginError,
                            sucesso = registerSuccess,
                            onBackClick = {
                                authViewModel.resetRegisterState()
                                showRegisterScreen = false
                            },
                            onRegisterClick = { nome, email, password, tipo ->
                                authViewModel.registar(nome, email, password, tipo)
                            },
                            onSuccessRedirect = {
                                authViewModel.resetRegisterState()
                                showRegisterScreen = false
                            }
                        )
                    } else {
                        LoginScreen(
                            erro = loginError,
                            onLoginClick = { email, password ->
                                authViewModel.autenticar(email, password)
                            },
                            onRegisterClick = {
                                authViewModel.resetRegisterState()
                                showRegisterScreen = true
                            }
                        )
                    }
                } else {
                    val usuarioLogado by authViewModel.usuarioLogado.collectAsState()

                    when (usuarioLogado?.tipo) {
                        TipoUtilizador.ORGANIZADOR -> {
                            OrganizerFlowContainer(
                                torneiosViewModel = torneiosViewModel,
                                authViewModel = authViewModel,
                                repository = repository,
                                usuarioLogado = usuarioLogado,
                                onTerminarSessao = {
                                    authViewModel.terminarSessao()
                                }
                            )
                        }

                        TipoUtilizador.ESPECTADOR -> {
                            SpectatorFlowContainer(
                                torneiosViewModel = torneiosViewModel,
                                authViewModel = authViewModel,
                                repository = repository,
                                usuarioLogado = usuarioLogado,
                                onTerminarSessao = {
                                    authViewModel.terminarSessao()
                                }
                            )
                        }
                        else -> {
                            AdminFlowContainer(
                                homeViewModel = homeViewModel,
                                utilizadoresViewModel = utilizadoresViewModel,
                                torneiosViewModel = torneiosViewModel,
                                graficosViewModel = graficosViewModel,
                                authViewModel = authViewModel,
                                repository = repository,
                                usuarioLogado = usuarioLogado,
                                onTerminarSessao = {
                                    authViewModel.terminarSessao()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}