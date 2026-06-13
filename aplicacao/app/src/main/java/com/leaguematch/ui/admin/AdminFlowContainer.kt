package com.leaguematch.ui.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.leaguematch.data.remote.model.ConfiguracaoNotificacoes
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.data.repository.LeagueMatchRepository
import com.leaguematch.ui.components.AdminBottomBar
import com.leaguematch.ui.components.ErrorScreen
import com.leaguematch.ui.components.LoadingScreen
import com.leaguematch.ui.components.RemoteContent
import com.leaguematch.viewmodel.AuthViewModel
import com.leaguematch.viewmodel.GraficosViewModel
import com.leaguematch.viewmodel.HomeViewModel
import com.leaguematch.viewmodel.TorneiosViewModel
import com.leaguematch.viewmodel.UtilizadoresViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

//Rotas disponveis no administrador
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

//contentor principal, responsável por gerir a navegação entre ecrãs e ligar
@Composable
fun AdminFlowContainer(
    homeViewModel: HomeViewModel,
    utilizadoresViewModel: UtilizadoresViewModel,
    torneiosViewModel: TorneiosViewModel,
    graficosViewModel: GraficosViewModel,
    authViewModel: AuthViewModel,
    repository: LeagueMatchRepository,
    usuarioLogado: Utilizador?,
    onTerminarSessao: () -> Unit
) {
    //Guarda o ecrã apresentado ao administrador
    var currentRoute by remember { mutableStateOf<AdminRoute>(AdminRoute.Home) }
    val coroutineScope = rememberCoroutineScope()
    //função auxiliar para alterar a rota atual
    fun navigate(route: AdminRoute) {
        currentRoute = route
    }

    //Atalhos de navegação utilizados pelos vários ecrãs
    val goHome = { navigate(AdminRoute.Home) }
    val goUsers = { navigate(AdminRoute.Utilizadores) }
    val goTournaments = { navigate(AdminRoute.Torneios) }
    val goCharts = { navigate(AdminRoute.Graficos) }
    val goSettings = { navigate(AdminRoute.Definicoes) }

    // Seleciona qual o ecrã a apresentar com base na rota atual.
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
                    golos = 0,

                    ativo = user?.active ?: true,

                    onAlterarEstadoClick = {
                        utilizadoresViewModel.alterarEstadoUtilizador(
                            route.id,
                            !(user?.active ?: true)
                        )
                        navigate(AdminRoute.Utilizadores)
                    },

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
                dadosModalidades == null || dadosTorneios == null -> LoadingScreen()

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
                    val (modalidades, totalTorneios) = dadosModalidades!!.getOrThrow()
                    val torneios = dadosTorneios!!.getOrThrow()

                    TorneiosScreen(
                        modalidades = modalidades,
                        torneios = torneios,
                        totalTorneios = totalTorneios,
                        onHomeClick = goHome,
                        onUtilizadoresClick = goUsers,
                        onGraficosClick = goCharts,
                        onDefinicoesClick = goSettings,
                        onTorneioClick = { id ->
                            val torneio = torneios.firstOrNull { it.id == id }
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
                    bottomBar = {
                        AdminBottomBar(
                            selectedItem = "torneios",
                            onHomeClick = goHome,
                            onUtilizadoresClick = goUsers,
                            onTorneiosClick = goTournaments,
                            onGraficosClick = goCharts,
                            onDefinicoesClick = goSettings
                        )
                    }
                )
            }
        }

        AdminRoute.Graficos -> {
            val estatisticas by graficosViewModel.estatisticasState.collectAsState()

            LaunchedEffect(Unit) {
                graficosViewModel.carregarEstatisticas("30d")
            }

            RemoteContent(estatisticas) { dados ->
                GraficosScreen(
                    estatisticas = dados,
                    onHomeClick = goHome,
                    onUtilizadoresClick = goUsers,
                    onTorneiosClick = goTournaments,
                    onDefinicoesClick = goSettings,
                    onPeriodChange = { periodo ->
                        graficosViewModel.carregarEstatisticas(periodo)
                    }
                )
            }
        }

        AdminRoute.Definicoes -> {
            DefinicoesScreen(
                utilizadorLogado = usuarioLogado,
                configuracaoNotificacoes = ConfiguracaoNotificacoes(
                    utilizadorId = usuarioLogado?.id ?: 0
                ),
                onGuardarConfiguracaoNotificacoes = { config ->
                    coroutineScope.launch {
                        repository.atualizarConfiguracaoNotificacoes(config)
                    }
                },
                onTerminarSessaoClick = {
                    onTerminarSessao()
                    navigate(AdminRoute.Home)
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