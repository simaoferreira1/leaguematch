package com.leaguematch

import android.os.Bundle
import android.widget.Toast
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
import androidx.lifecycle.ViewModelProvider
import com.leaguematch.data.remote.model.EstatisticasAdmin
import com.leaguematch.data.remote.model.ResumoDashboard
import com.leaguematch.data.remote.model.TipoUtilizador
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.data.repository.SupabaseLeagueMatchRepository
import com.leaguematch.ui.admin.DefinicoesScreen
import com.leaguematch.ui.admin.DetalheTorneioScreen
import com.leaguematch.ui.admin.DetalheUtilizadorScreen
import com.leaguematch.ui.admin.GestaoNotificacoesScreen
import com.leaguematch.ui.admin.GraficosScreen
import com.leaguematch.ui.admin.HomeScreen
import com.leaguematch.ui.admin.ListaTorneiosModalidadeScreen
import com.leaguematch.ui.admin.TorneiosScreen
import com.leaguematch.ui.admin.UtilizadoresScreen
import com.leaguematch.ui.auth.LoginScreen
import com.leaguematch.ui.auth.RegisterScreen
import com.leaguematch.ui.components.OrganizerBottomBar
import com.leaguematch.ui.organizer.OrgTournamentsScreen
import com.leaguematch.ui.theme.LeagueMatchTheme
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

sealed interface OrganizerRoute {
    data object MeusTorneios : OrganizerRoute
    data object Perfil : OrganizerRoute
}

sealed interface SpectatorRoute {
    data object EscolherTorneio : SpectatorRoute
    data object Classificacao : SpectatorRoute
    data object Jogos : SpectatorRoute
    data object Equipas : SpectatorRoute
    data object Perfil : SpectatorRoute
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
                val registerSuccess by authViewModel.registerSuccess.collectAsState()
                var showRegisterScreen by remember { mutableStateOf(false) }
                var currentRoute by remember { mutableStateOf<AdminRoute>(AdminRoute.Home) }
                var currentOrgRoute by remember { mutableStateOf<OrganizerRoute>(OrganizerRoute.MeusTorneios) }
                var currentSpectatorRoute by remember { mutableStateOf<SpectatorRoute>(SpectatorRoute.EscolherTorneio) }
                var torneioSelecionado by remember { mutableStateOf<Torneio?>(null) }
                fun navigate(route: AdminRoute) {
                    currentRoute = route
                }

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
                            val goMeusTorneios = { currentOrgRoute = OrganizerRoute.MeusTorneios }
                            val goPerfil = { currentOrgRoute = OrganizerRoute.Perfil }

                            val context = androidx.compose.ui.platform.LocalContext.current

                            when (currentOrgRoute) {
                                OrganizerRoute.MeusTorneios -> {
                                    OrgTournamentsScreen(
                                        onNavigateToCreate = {
                                            Toast.makeText(
                                                context,
                                                "Criação de torneios brevemente disponível!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        onNavigateToActions = {
                                            // Ação de clique nos cartões temporariamente inativa
                                        },
                                        onPerfilClick = goPerfil
                                    )
                                }

                                OrganizerRoute.Perfil -> {
                                    DefinicoesScreen(
                                        utilizadorLogado = usuarioLogado,
                                        onTerminarSessaoClick = {
                                            authViewModel.terminarSessao()
                                            currentOrgRoute = OrganizerRoute.MeusTorneios
                                        },
                                        onEditarPerfilClick = { nome, password ->
                                            authViewModel.atualizarUtilizador(nome, password)
                                        },
                                        bottomBar = {
                                            OrganizerBottomBar(
                                                selectedItem = "perfil",
                                                onTorneiosClick = goMeusTorneios,
                                                onEquipasClick = {},
                                                onJogosClick = {},
                                                onPerfilClick = goPerfil
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        TipoUtilizador.ESPECTADOR -> {
                            when (currentSpectatorRoute) {

                                SpectatorRoute.EscolherTorneio -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Escolher torneio")
                                    }
                                }

                                SpectatorRoute.Classificacao -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Classificação")
                                    }
                                }

                                SpectatorRoute.Jogos -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Jogos")
                                    }
                                }

                                SpectatorRoute.Equipas -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Equipas")
                                    }
                                }

                                SpectatorRoute.Perfil -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Perfil")
                                    }
                                }
                            }
                        }

                        else -> {
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

                                    val dadosModalidades by torneiosViewModel.modalidadesState.collectAsState()
                                    val dadosTorneios by torneiosViewModel.todosTorneiosState.collectAsState()

                                    LaunchedEffect(Unit) {
                                        torneiosViewModel.carregarTorneios()
                                        torneiosViewModel.carregarTodosTorneios()
                                    }

                                    when {
                                        dadosModalidades == null || dadosTorneios == null -> {
                                            LoadingScreen()
                                        }

                                        dadosModalidades!!.isFailure -> {
                                            ErrorScreen(
                                                dadosModalidades!!.exceptionOrNull()?.message
                                                    ?: "Erro ao carregar modalidades."
                                            )
                                        }

                                        dadosTorneios!!.isFailure -> {
                                            ErrorScreen(
                                                dadosTorneios!!.exceptionOrNull()?.message
                                                    ?: "Erro ao carregar torneios."
                                            )
                                        }

                                        else -> {

                                            val (modalidades, totalTorneios) =
                                                dadosModalidades!!.getOrThrow()

                                            val torneios =
                                                dadosTorneios!!.getOrThrow()

                                            TorneiosScreen(
                                                modalidades = modalidades,
                                                torneios = torneios,
                                                totalTorneios = totalTorneios,
                                                onHomeClick = goHome,
                                                onUtilizadoresClick = goUsers,
                                                onGraficosClick = goCharts,
                                                onDefinicoesClick = goSettings,
                                                onTorneioClick = { id ->
                                                    val torneio =
                                                        torneios.firstOrNull { it.id == id }

                                                    navigate(
                                                        AdminRoute.DetalheTorneio(
                                                            id = id,
                                                            modalidade = torneio?.modalidade ?: "Todos"
                                                        )
                                                    )
                                                },
                                                onRemoverTorneioClick = { id ->
                                                    torneiosViewModel.removerTorneio(id)
                                                }
                                            )
                                        }
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
                                            onBackClick = goTournaments,
                                            onHomeClick = goHome,
                                            onUtilizadoresClick = goUsers,
                                            onTorneiosClick = goTournaments,
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

                                AdminRoute.Definicoes -> {
                                    val usuarioLogado by authViewModel.usuarioLogado.collectAsState()
                                    DefinicoesScreen(
                                        utilizadorLogado = usuarioLogado,
                                        onTerminarSessaoClick = {
                                            authViewModel.terminarSessao()
                                            currentRoute = AdminRoute.Home
                                        },
                                        onEditarPerfilClick = { nome, password ->
                                            authViewModel.atualizarUtilizador(nome, password)
                                        },
                                        onGerirNotificacoesClick = {
                                            navigate(AdminRoute.Notificacoes)
                                        },
                                        onHomeClick = goHome,
                                        onUtilizadoresClick = goUsers,
                                        onTorneiosClick = goTournaments,
                                        onGraficosClick = goCharts
                                    )
                                }

                                AdminRoute.Notificacoes -> {
                                    GestaoNotificacoesScreen(
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