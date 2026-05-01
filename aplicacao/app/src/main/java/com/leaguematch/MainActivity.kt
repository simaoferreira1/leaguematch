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
import com.leaguematch.dados.modelos.DetalheTorneio
import com.leaguematch.dados.modelos.EstatisticasAdmin
import com.leaguematch.dados.modelos.ResumoDashboard
import com.leaguematch.dados.modelos.ResumoModalidade
import com.leaguematch.dados.modelos.Torneio
import com.leaguematch.dados.modelos.Utilizador
import com.leaguematch.dados.repositorios.SupabaseLeagueMatchRepository
import com.leaguematch.funcionalidades.administrador.ecra.HomeScreen
import com.leaguematch.funcionalidades.administrador.ecra.DefinicoesScreen
import com.leaguematch.funcionalidades.administrador.ecra.DetalheTorneioScreen
import com.leaguematch.funcionalidades.administrador.ecra.DetalheUtilizadorScreen
import com.leaguematch.funcionalidades.administrador.ecra.GestaoNotificacoesScreen
import com.leaguematch.funcionalidades.administrador.ecra.GraficosScreen
import com.leaguematch.funcionalidades.administrador.ecra.ListaTorneiosModalidadeScreen
import com.leaguematch.funcionalidades.administrador.ecra.TorneiosScreen
import com.leaguematch.funcionalidades.administrador.ecra.UtilizadoresScreen
import com.leaguematch.funcionalidades.autenticacao.ecra.LoginScreen
import com.leaguematch.ui.theme.LeagueMatchTheme
import kotlinx.coroutines.launch

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LeagueMatchTheme {
                val repository = remember {
                    SupabaseLeagueMatchRepository(
                        supabaseUrl = BuildConfig.SUPABASE_URL,
                        anonKey = BuildConfig.SUPABASE_ANON_KEY
                    )
                }
                val scope = rememberCoroutineScope()
                var isLoggedIn by remember { mutableStateOf(false) }
                var loginError by remember { mutableStateOf<String?>(null) }
                var currentRoute by remember { mutableStateOf<AdminRoute>(AdminRoute.Home) }

                fun navigate(route: AdminRoute) {
                    currentRoute = route
                }

                if (!isLoggedIn) {
                    LoginScreen(
                        erro = loginError,
                        onLoginClick = { email, password ->
                            scope.launch {
                                runCatching { repository.autenticar(email, password) }
                                    .onSuccess { utilizador ->
                                        if (utilizador != null) {
                                            loginError = null
                                            isLoggedIn = true
                                            currentRoute = AdminRoute.Home
                                        } else {
                                            loginError = "Credenciais inválidas."
                                        }
                                    }
                                    .onFailure { error ->
                                        loginError = error.message ?: "Não foi possível ligar ao Supabase."
                                    }
                            }
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
                            val dashboard by produceState<Result<ResumoDashboard>?>(null) {
                                value = runCatching { repository.obterDashboard() }
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
                            val utilizadores by produceState<Result<List<Utilizador>>?>(null) {
                                value = runCatching { repository.listarUtilizadores() }
                            }
                            RemoteContent(utilizadores) {
                                UtilizadoresScreen(
                                    utilizadores = it,
                                    onUtilizadorClick = { id -> navigate(AdminRoute.DetalheUtilizador(id)) },
                                    onHomeClick = goHome,
                                    onTorneiosClick = goTournaments,
                                    onGraficosClick = goCharts,
                                    onDefinicoesClick = goSettings
                                )
                            }
                        }

                        is AdminRoute.DetalheUtilizador -> {
                            val utilizador by produceState<Result<Utilizador?>?>(null, route.id) {
                                value = runCatching { repository.obterUtilizador(route.id) }
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
                            val dados by produceState<Result<Pair<List<ResumoModalidade>, Int>>?>(null) {
                                value = runCatching {
                                    repository.listarModalidades() to repository.obterDashboard().totalTorneios
                                }
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
                            val torneios by produceState<Result<List<Torneio>>?>(null, route.modalidade) {
                                value = runCatching { repository.listarTorneiosPorModalidade(route.modalidade) }
                            }
                            RemoteContent(torneios) {
                                ListaTorneiosModalidadeScreen(
                                    modalidade = route.modalidade,
                                    torneios = it,
                                    onBackClick = goTournaments,
                                    onTorneioClick = { id -> navigate(AdminRoute.DetalheTorneio(id, route.modalidade)) },
                                    onHomeClick = goHome,
                                    onUtilizadoresClick = goUsers,
                                    onGraficosClick = goCharts,
                                    onDefinicoesClick = goSettings
                                )
                            }
                        }

                        is AdminRoute.DetalheTorneio -> {
                            val detalhe by produceState<Result<DetalheTorneio?>?>(null, route.id) {
                                value = runCatching { repository.obterDetalheTorneio(route.id) }
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
                            val estatisticas by produceState<Result<EstatisticasAdmin>?>(null) {
                                value = runCatching { repository.obterEstatisticasAdmin() }
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
                                isLoggedIn = false
                                loginError = null
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
