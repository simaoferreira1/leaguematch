/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: AdminFlowContainer.kt
 * Tipo: Interface (Compose View) do Administrador
 *
 * Descrição:
 * Este ficheiro define um ecrã da área do Administrador em Jetpack Compose.\n * Ele desenha componentes visuais reativos baseado no estado fornecido pelo respetivo ViewModel.\n * Permite ao Admin gerir utilizadores (ativar/desativar), visualizar alertas do sistema e gráficos.
 */
package com.leaguematch.ui.admin // Define o pacote deste ficheiro de código

import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.LaunchedEffect // Importa dependência / biblioteca necessária
import androidx.compose.runtime.collectAsState // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.remember // Importa dependência / biblioteca necessária
import androidx.compose.runtime.rememberCoroutineScope // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária
import androidx.compose.ui.platform.LocalContext // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.ConfiguracaoNotificacoes // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Utilizador // Importa dependência / biblioteca necessária
import com.leaguematch.data.repository.LeagueMatchRepository // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.AdminBottomBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.ErrorScreen // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.LoadingScreen // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.RemoteContent // Importa dependência / biblioteca necessária
import com.leaguematch.viewmodel.AuthViewModel // Importa dependência / biblioteca necessária
import com.leaguematch.viewmodel.GraficosViewModel // Importa dependência / biblioteca necessária
import com.leaguematch.viewmodel.HomeViewModel // Importa dependência / biblioteca necessária
import com.leaguematch.viewmodel.TorneiosViewModel // Importa dependência / biblioteca necessária
import com.leaguematch.viewmodel.UtilizadoresViewModel // Importa dependência / biblioteca necessária
import kotlinx.coroutines.launch // Importa dependência / biblioteca necessária

//Rotas disponveis no administrador
sealed interface AdminRoute { // Declaração de interface (contrato de métodos)
    data object Home : AdminRoute // Declaração de objeto estático / Singleton
    data object Utilizadores : AdminRoute // Declaração de objeto estático / Singleton
    data class DetalheUtilizador(val id: Int) : AdminRoute // Declaração de classe para modelar objetos
    data object Torneios : AdminRoute // Declaração de objeto estático / Singleton
    data class TorneiosModalidade(val modalidade: String) : AdminRoute // Declaração de classe para modelar objetos
    data class DetalheTorneio(val id: Int, val modalidade: String) : AdminRoute // Declaração de classe para modelar objetos
    data object Graficos : AdminRoute // Declaração de objeto estático / Singleton
    data object Definicoes : AdminRoute // Declaração de objeto estático / Singleton
    data object Notificacoes : AdminRoute // Declaração de objeto estático / Singleton
}

//contentor principal, responsável por gerir a navegação entre ecrãs e ligar
@Composable
fun AdminFlowContainer( // Declaração de função / método de lógica
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
    var currentRoute by remember { mutableStateOf<AdminRoute>(AdminRoute.Home) } // Declara estado mutável local do Compose
    val coroutineScope = rememberCoroutineScope() // Cria escopo local para lançar coroutines em cliques na UI
    val context = LocalContext.current // Declara constante local (leitura única)
    //função auxiliar para alterar a rota atual
    fun navigate(route: AdminRoute) { // Declaração de função / método de lógica
        currentRoute = route
    }

    //Atalhos de navegação utilizados pelos vários ecrãs
    val goHome = { navigate(AdminRoute.Home) } // Declara constante local (leitura única)
    val goUsers = { navigate(AdminRoute.Utilizadores) } // Declara constante local (leitura única)
    val goTournaments = { navigate(AdminRoute.Torneios) } // Declara constante local (leitura única)
    val goCharts = { navigate(AdminRoute.Graficos) } // Declara constante local (leitura única)
    val goSettings = { navigate(AdminRoute.Definicoes) } // Declara constante local (leitura única)

    var notificacoesAdmin by remember { // Memoriza estado para evitar perda durante a recomposição
        mutableStateOf<List<com.leaguematch.data.remote.model.NotificacaoItem>>(emptyList()) // Declara estado mutável local do Compose
    }

    // Seleciona qual o ecrã a apresentar com base na rota atual.
    when (val route = currentRoute) { // Escolha múltipla condicional (semelhante a switch-case)
        AdminRoute.Home -> {
            val dashboard by homeViewModel.dashboardState.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)
            LaunchedEffect(Unit) { // Efeito colateral Compose: executa código assíncrono ao recompor
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
            val utilizadores by utilizadoresViewModel.utilizadoresState.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)
            LaunchedEffect(Unit) { // Efeito colateral Compose: executa código assíncrono ao recompor
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
            val utilizador by utilizadoresViewModel.detalheUtilizadorState.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)

            LaunchedEffect(route.id) { // Efeito colateral Compose: executa código assíncrono ao recompor
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

                    onResetPasswordClick = { novaPass ->
                        val email = user?.email.orEmpty() // Declara constante local (leitura única)
                        if (email.isNotBlank() && novaPass.isNotBlank()) { // Estrutura de decisão condicional principal
                            authViewModel.resetPasswordPorAdmin(email, novaPass) { ok ->
                                android.widget.Toast.makeText(
                                    context,
                                    if (ok) "Palavra-passe reposta com sucesso." else "Erro ao repor palavra-passe.", // Estrutura de decisão condicional principal
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
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
            val dadosModalidades by torneiosViewModel.modalidadesState.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)
            val dadosTorneios by torneiosViewModel.todosTorneiosState.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)

            LaunchedEffect(Unit) { // Efeito colateral Compose: executa código assíncrono ao recompor
                torneiosViewModel.carregarTorneios()
                torneiosViewModel.carregarTodosTorneios()
            }

            when { // Escolha múltipla condicional (semelhante a switch-case)
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

                else -> { // Fluxo condicional alternativo caso o 'if' seja falso
                    val (modalidades, totalTorneios) = dadosModalidades!!.getOrThrow() // Declara constante local (leitura única)
                    val torneios = dadosTorneios!!.getOrThrow() // Declara constante local (leitura única)

                    TorneiosScreen(
                        modalidades = modalidades,
                        torneios = torneios,
                        totalTorneios = totalTorneios,
                        onHomeClick = goHome,
                        onUtilizadoresClick = goUsers,
                        onGraficosClick = goCharts,
                        onDefinicoesClick = goSettings,
                        onTorneioClick = { id ->
                            val torneio = torneios.firstOrNull { it.id == id } // Declara constante local (leitura única)
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
            val torneios by torneiosViewModel.torneiosState.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)

            LaunchedEffect(route.modalidade) { // Efeito colateral Compose: executa código assíncrono ao recompor
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
            val detalhe by torneiosViewModel.detalheTorneioState.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)

            LaunchedEffect(route.id) { // Efeito colateral Compose: executa código assíncrono ao recompor
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
            val estatisticas by graficosViewModel.estatisticasState.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)

            LaunchedEffect(Unit) { // Efeito colateral Compose: executa código assíncrono ao recompor
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
                        repository.atualizarConfiguracaoNotificacoes(config) // Efetua chamada remota ou local ao repositório de dados
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
            LaunchedEffect(Unit) { // Efeito colateral Compose: executa código assíncrono ao recompor
                notificacoesAdmin = repository.listarNotificacoesAdmin() // Efetua chamada remota ou local ao repositório de dados
            }

            AdminNotificacoesScreen(
                notificacoes = notificacoesAdmin,
                onBackClick = {
                    navigate(AdminRoute.Definicoes)
                },
                onMarcarTodasLidas = {
                    coroutineScope.launch {
                        repository.marcarTodasNotificacoesAdminLidas() // Efetua chamada remota ou local ao repositório de dados
                        notificacoesAdmin = repository.listarNotificacoesAdmin() // Efetua chamada remota ou local ao repositório de dados
                    }
                },
                onNotificacaoClick = { notificacao ->
                    coroutineScope.launch {
                        repository.marcarNotificacaoLida(notificacao.id) // Efetua chamada remota ou local ao repositório de dados
                        notificacoesAdmin = repository.listarNotificacoesAdmin() // Efetua chamada remota ou local ao repositório de dados
                    }
                }
            )
        }
    }
}