package com.leaguematch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leaguematch.data.remote.model.DetalheTorneio
import com.leaguematch.data.remote.model.EstatisticasAdmin
import com.leaguematch.data.remote.model.ResumoDashboard
import com.leaguematch.data.remote.model.ResumoModalidade
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.data.repository.SupabaseLeagueMatchRepository
import com.leaguematch.ui.admin.HomeScreen
import com.leaguematch.ui.admin.DefinicoesScreen
import com.leaguematch.ui.admin.DetalheTorneioScreen
import com.leaguematch.ui.admin.DetalheUtilizadorScreen
import com.leaguematch.ui.admin.GestaoNotificacoesScreen
import com.leaguematch.ui.admin.GraficosScreen
import com.leaguematch.ui.admin.ListaTorneiosModalidadeScreen
import com.leaguematch.ui.admin.TorneiosScreen
import com.leaguematch.ui.admin.UtilizadoresScreen
import com.leaguematch.ui.auth.LoginScreen
import com.leaguematch.ui.theme.LeagueMatchTheme
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider
import com.leaguematch.viewmodel.*

sealed interface AdminRoute {
    data object Home : AdminRoute
    data object Utilizadores : AdminRoute
    data class DetalheUtilizador(val id: Int) : AdminRoute
    data object Torneios : AdminRoute
    data class TorneiosModalidade(val modalidade: String) : AdminRoute
    data class DetalheTorneio(val id: Int, val modalidade: String) : AdminRoute
    data object Graficos : AdminRoute
    data object Definicoes : AdminRoute
    data object Notificacoes : AdminRoute
}

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
                var currentRoute by remember { mutableStateOf<AdminRoute>(AdminRoute.Home) }

                fun navigate(route: AdminRoute) {
                    currentRoute = route
                }

                if (!isLoggedIn) {
                    LoginScreen(
                        erro = loginError,
                        onLoginClick = { email, password ->
                            authViewModel.autenticar(email, password)
                        }
                    )
                } else {
                    val goHome = { navigate(AdminRoute.Home) }
                    val goUsers = { navigate(AdminRoute.Utilizadores) }
                    val goTournaments = { navigate(AdminRoute.Torneios) }
                    val goCharts = { navigate(AdminRoute.Graficos) }
                    val goSettings = { navigate(AdminRoute.Definicoes) }

                    when (val route = currentRoute) {
                        AdminRoute.Home -> {
                            val dashboard by homeViewModel.dashboardState.collectAsState()
                            LaunchedEffect(Unit) {
                                homeViewModel.carregarDashboard()
                            }
                            RemoteContent(dashboard) {
                                HomeScreen(
                                    dashboard = it,
                                    onUtilizadoresClick = goUsers,
                                    onTorneiosClick = goTournaments,
                                    onGraficosClick = goCharts,
                                    onDefinicoesClick = goSettings
                                )
                            }
                        }

                        AdminRoute.Utilizadores -> {
                            val utilizadores by utilizadoresViewModel.utilizadoresState.collectAsState()
                            LaunchedEffect(Unit) {
                                utilizadoresViewModel.carregarUtilizadores()
                            }
                            RemoteContent(utilizadores) {
                                UtilizadoresScreen(
                                    utilizadores = it,
                                    onUtilizadorClick = { id -> 
                                        utilizadoresViewModel.carregarDetalhes(id)
                                        navigate(AdminRoute.DetalheUtilizador(id)) 
                                    },
                                    onHomeClick = goHome,
                                    onTorneiosClick = goTournaments,
                                    onGraficosClick = goCharts,
                                    onDefinicoesClick = goSettings
                                )
                            }
                        }

                        is AdminRoute.DetalheUtilizador -> {
                            val utilizador by utilizadoresViewModel.detalheUtilizadorState.collectAsState()
                            LaunchedEffect(route.id) {
                                utilizadoresViewModel.carregarDetalhes(route.id)
                            }
                            RemoteContent(utilizador) { user ->
                                DetalheUtilizadorScreen(
                                    nome = user?.nome.orEmpty(),
                                    email = user?.email.orEmpty(),
                                    tipo = user?.tipo?.descricao ?: "Participante",
                                    equipas = user?.equipas ?: 0,
                                    torneios = user?.torneios ?: 0,
                                    jogos = user?.jogos ?: 0,
                                    onBackClick = goUsers,
                                    onHomeClick = goHome,
                                    onUtilizadoresClick = goUsers,
                                    onTorneiosClick = goTournaments,
                                    onGraficosClick = goCharts,
                                    onDefinicoesClick = goSettings
                                )
                            }
                        }

                        AdminRoute.Torneios -> {
                            val dados by torneiosViewModel.modalidadesState.collectAsState()
                            LaunchedEffect(Unit) {
                                torneiosViewModel.carregarTorneios()
                            }
                            RemoteContent(dados) { (modalidades, totalTorneios) ->
                                TorneiosScreen(
                                    modalidades = modalidades,
                                    totalTorneios = totalTorneios,
                                    onHomeClick = goHome,
                                    onUtilizadoresClick = goUsers,
                                    onGraficosClick = goCharts,
                                    onDefinicoesClick = goSettings,
                                    onModalidadeClick = { navigate(AdminRoute.TorneiosModalidade(it)) }
                                )
                            }
                        }

                        is AdminRoute.TorneiosModalidade -> {
                            val torneios by torneiosViewModel.torneiosState.collectAsState()
                            LaunchedEffect(route.modalidade) {
                                torneiosViewModel.carregarTorneiosPorModalidade(route.modalidade)
                            }
                            RemoteContent(torneios) {
                                ListaTorneiosModalidadeScreen(
                                    modalidade = route.modalidade,
                                    torneios = it,
                                    onBackClick = goTournaments,
                                    onTorneioClick = { id -> 
                                        torneiosViewModel.carregarDetalheTorneio(id)
                                        navigate(AdminRoute.DetalheTorneio(id, route.modalidade)) 
                                    },
                                    onHomeClick = goHome,
                                    onUtilizadoresClick = goUsers,
                                    onGraficosClick = goCharts,
                                    onDefinicoesClick = goSettings
                                )
                            }
                        }

                        is AdminRoute.DetalheTorneio -> {
                            val detalhe by torneiosViewModel.detalheTorneioState.collectAsState()
                            LaunchedEffect(route.id) {
                                torneiosViewModel.carregarDetalheTorneio(route.id)
                            }
                            RemoteContent(detalhe) {
                                DetalheTorneioScreen(
                                    detalhe = it,
                                    onBackClick = { navigate(AdminRoute.TorneiosModalidade(route.modalidade)) },
                                    onHomeClick = goHome,
                                    onUtilizadoresClick = goUsers,
                                    onGraficosClick = goCharts,
                                    onDefinicoesClick = goSettings
                                )
                            }
                        }

                        AdminRoute.Graficos -> {
                            val estatisticas by graficosViewModel.estatisticasState.collectAsState()
                            LaunchedEffect(Unit) {
                                graficosViewModel.carregarEstatisticas()
                            }
                            RemoteContent(estatisticas) {
                                GraficosScreen(
                                    estatisticas = it,
                                    onHomeClick = goHome,
                                    onUtilizadoresClick = goUsers,
                                    onTorneiosClick = goTournaments,
                                    onDefinicoesClick = goSettings
                                )
                            }
                        }

                        AdminRoute.Definicoes -> DefinicoesScreen(
                            onTerminarSessaoClick = {
                                authViewModel.terminarSessao()
                                currentRoute = AdminRoute.Home
                            },
                            onGerirNotificacoesClick = { navigate(AdminRoute.Notificacoes) },
                            onHomeClick = goHome,
                            onUtilizadoresClick = goUsers,
                            onTorneiosClick = goTournaments,
                            onGraficosClick = goCharts
                        )

                        AdminRoute.Notificacoes -> GestaoNotificacoesScreen(
                            onHomeClick = goHome,
                            onUtilizadoresClick = goUsers,
                            onTorneiosClick = goTournaments,
                            onGraficosClick = goCharts,
                            onDefinicoesClick = goSettings
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> RemoteContent(
    result: Result<T>?,
    content: @Composable (T) -> Unit
) {
    when {
        result == null -> LoadingScreen()
        result.isSuccess -> content(result.getOrThrow())
        else -> ErrorScreen(result.exceptionOrNull()?.message ?: "Erro ao carregar dados.")
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorScreen(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Erro ao ligar ao Supabase",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
