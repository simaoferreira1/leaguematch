package com.leaguematch

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import com.leaguematch.data.remote.model.TipoUtilizador
import com.leaguematch.data.repository.SupabaseLeagueMatchRepository
import com.leaguematch.data.repository.TranslationRepository
import com.leaguematch.translations.Language
import com.leaguematch.ui.admin.AdminFlowContainer
import com.leaguematch.ui.auth.IntroSliderScreen
import com.leaguematch.ui.auth.LoginScreen
import com.leaguematch.ui.auth.RegisterScreen
import com.leaguematch.ui.components.LoadingScreen
import com.leaguematch.ui.components.LocalLanguage
import com.leaguematch.ui.components.LocalTranslationRepository
import com.leaguematch.ui.organizer.OrganizerFlowContainer
import com.leaguematch.ui.participant.ParticipantFlowContainer
import com.leaguematch.ui.spectator.SpectatorFlowContainer
import com.leaguematch.ui.theme.LeagueMatchTheme
import com.leaguematch.viewmodel.AuthViewModel
import com.leaguematch.viewmodel.GraficosViewModel
import com.leaguematch.viewmodel.HomeViewModel
import com.leaguematch.viewmodel.ParticipantViewModel
import com.leaguematch.viewmodel.TorneiosViewModel
import com.leaguematch.viewmodel.UtilizadoresViewModel
import com.leaguematch.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var utilizadoresViewModel: UtilizadoresViewModel
    private lateinit var torneiosViewModel: TorneiosViewModel
    private lateinit var graficosViewModel: GraficosViewModel
    private lateinit var participantViewModel: ParticipantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = SupabaseLeagueMatchRepository(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            anonKey = BuildConfig.SUPABASE_ANON_KEY
        )

        val translationRepository = TranslationRepository(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            anonKey = BuildConfig.SUPABASE_ANON_KEY
        )

        val factory = ViewModelFactory(repository)

        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        utilizadoresViewModel = ViewModelProvider(this, factory)[UtilizadoresViewModel::class.java]
        torneiosViewModel = ViewModelProvider(this, factory)[TorneiosViewModel::class.java]
        graficosViewModel = ViewModelProvider(this, factory)[GraficosViewModel::class.java]
        participantViewModel = ViewModelProvider(this, factory)[ParticipantViewModel::class.java]

        setContent {
            LeagueMatchTheme {
                val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
                val loginError by authViewModel.loginError.collectAsState()
                val registerSuccess by authViewModel.registerSuccess.collectAsState()
                var showRegisterScreen by remember { mutableStateOf(false) }

                val context = LocalContext.current
                val prefs = remember {
                    context.getSharedPreferences("lm_prefs", Context.MODE_PRIVATE)
                }

                var language by remember {
                    mutableStateOf(
                        Language.valueOf(
                            prefs.getString("language", Language.PT.name) ?: Language.PT.name
                        )
                    )
                }

                fun changeLanguage(newLanguage: Language) {
                    language = newLanguage
                    prefs.edit().putString("language", newLanguage.name).apply()
                }

                var introVisto by remember {
                    mutableStateOf(prefs.getBoolean("intro_seen", false))
                }

                CompositionLocalProvider(
                    LocalLanguage provides language,
                    LocalTranslationRepository provides translationRepository
                ) {
                    if (!isLoggedIn && !introVisto) {
                        IntroSliderScreen(
                            onConcluir = {
                                prefs.edit().putBoolean("intro_seen", true).apply()
                                introVisto = true
                            }
                        )
                    } else if (!isLoggedIn) {
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
                                language = language,
                                onLanguageChange = { newLanguage ->
                                    changeLanguage(newLanguage)
                                },
                                translationRepository = translationRepository,
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

                        if (usuarioLogado == null) {
                            LoadingScreen()
                        } else {
                            when (usuarioLogado!!.tipo) {
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

                                TipoUtilizador.PARTICIPANTE -> {
                                    ParticipantFlowContainer(
                                        torneiosViewModel = torneiosViewModel,
                                        authViewModel = authViewModel,
                                        participantViewModel = participantViewModel,
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
    }
}