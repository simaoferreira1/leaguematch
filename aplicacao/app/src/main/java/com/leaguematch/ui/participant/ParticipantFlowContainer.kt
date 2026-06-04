package com.leaguematch.ui.participant

import androidx.compose.runtime.*
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.ui.admin.DefinicoesScreen
import com.leaguematch.ui.admin.GestaoNotificacoesScreen
import com.leaguematch.ui.components.ParticipantBottomBar
import com.leaguematch.viewmodel.AuthViewModel
import com.leaguematch.viewmodel.ParticipantViewModel

sealed interface ParticipantRoute {
    data object Home : ParticipantRoute
    data object Torneios : ParticipantRoute
    data object Jogos : ParticipantRoute
    data object Equipa : ParticipantRoute
    data object Estatisticas : ParticipantRoute
    data object Perfil : ParticipantRoute
    data object Notificacoes : ParticipantRoute
    data object JoinTeam : ParticipantRoute

    data class TournamentDetail(
        val torneioId: Int
    ) : ParticipantRoute
}

@Composable
fun ParticipantFlowContainer(
    authViewModel: AuthViewModel,
    participantViewModel: ParticipantViewModel,
    usuarioLogado: Utilizador?,
    onTerminarSessao: () -> Unit
) {
    var currentRoute by remember {
        mutableStateOf<ParticipantRoute>(ParticipantRoute.Home)
    }

    LaunchedEffect(usuarioLogado?.id) {
        usuarioLogado?.id?.let { id ->
            participantViewModel.carregarDadosParticipante(id)
        }
    }

    val torneios by participantViewModel.torneios.collectAsState()
    val equipa by participantViewModel.equipa.collectAsState()
    val jogadoresEquipa by participantViewModel.jogadoresEquipa.collectAsState()
    val classificacaoEquipa by participantViewModel.classificacaoEquipa.collectAsState()
    val jogosEquipa by participantViewModel.jogosEquipa.collectAsState()
    val statsParticipante by participantViewModel.statsParticipante.collectAsState()

    val detalheTorneio by participantViewModel.detalheTorneio.collectAsState()
    val classificacaoTorneio by participantViewModel.classificacaoTorneio.collectAsState()

    val juntarEquipaLoading by participantViewModel.juntarEquipaLoading.collectAsState()
    val juntarEquipaErro by participantViewModel.juntarEquipaErro.collectAsState()

    when (currentRoute) {

        ParticipantRoute.Home -> {
            ParticipantHomeScreen(
                usuarioLogado = usuarioLogado,
                selectedItem = "home",
                onTorneiosClick = { currentRoute = ParticipantRoute.Torneios },
                onJogosClick = { currentRoute = ParticipantRoute.Jogos },
                onEquipaClick = { currentRoute = ParticipantRoute.Equipa },
                onEstatisticasClick = { currentRoute = ParticipantRoute.Estatisticas },
                onPerfilClick = { currentRoute = ParticipantRoute.Perfil }
            )
        }

        ParticipantRoute.Torneios -> {
            ParticipantTournamentsScreen(
                torneios = torneios,
                onTournamentClick = { torneioId ->
                    participantViewModel.carregarDetalheTorneio(torneioId)
                    currentRoute = ParticipantRoute.TournamentDetail(torneioId)
                },
                onHomeClick = { currentRoute = ParticipantRoute.Home },
                onTorneiosClick = {},
                onJogosClick = { currentRoute = ParticipantRoute.Jogos },
                onEquipaClick = { currentRoute = ParticipantRoute.Equipa },
                onEstatisticasClick = { currentRoute = ParticipantRoute.Estatisticas },
                onPerfilClick = { currentRoute = ParticipantRoute.Perfil }
            )
        }

        ParticipantRoute.Jogos -> {
            ParticipantGamesScreen(
                jogos = jogosEquipa,
                onHomeClick = { currentRoute = ParticipantRoute.Home },
                onTorneiosClick = { currentRoute = ParticipantRoute.Torneios },
                onJogosClick = {},
                onEquipaClick = { currentRoute = ParticipantRoute.Equipa },
                onEstatisticasClick = { currentRoute = ParticipantRoute.Estatisticas },
                onPerfilClick = { currentRoute = ParticipantRoute.Perfil }
            )
        }

        ParticipantRoute.Equipa -> {
            ParticipantTeamScreen(
                equipa = equipa,
                jogadores = jogadoresEquipa,
                classificacao = classificacaoEquipa,
                jogos = jogosEquipa,
                onJoinTeamClick = {
                    participantViewModel.limparErroJuntarEquipa()
                    currentRoute = ParticipantRoute.JoinTeam
                },
                onHomeClick = { currentRoute = ParticipantRoute.Home },
                onTorneiosClick = { currentRoute = ParticipantRoute.Torneios },
                onJogosClick = { currentRoute = ParticipantRoute.Jogos },
                onEquipaClick = {},
                onEstatisticasClick = { currentRoute = ParticipantRoute.Estatisticas },
                onPerfilClick = { currentRoute = ParticipantRoute.Perfil }
            )
        }

        ParticipantRoute.JoinTeam -> {
            ParticipantJoinTeamScreen(
                isLoading = juntarEquipaLoading,
                erro = juntarEquipaErro,
                onBackClick = {
                    participantViewModel.limparErroJuntarEquipa()
                    currentRoute = ParticipantRoute.Equipa
                },
                onConfirmClick = { codigo ->
                    val id = usuarioLogado?.id ?: return@ParticipantJoinTeamScreen
                    participantViewModel.juntarEquipa(
                        utilizadorId = id,
                        codigo = codigo,
                        onSuccess = {
                            currentRoute = ParticipantRoute.Equipa
                        }
                    )
                }
            )
        }

        ParticipantRoute.Estatisticas -> {
            ParticipantStatsScreen(
                stats = ParticipantStats(
                    jogos = statsParticipante.jogos,
                    golos = statsParticipante.golos,
                    assistencias = statsParticipante.assistencias,
                    mvp = statsParticipante.mvp
                ),
                onHomeClick = { currentRoute = ParticipantRoute.Home },
                onTorneiosClick = { currentRoute = ParticipantRoute.Torneios },
                onJogosClick = { currentRoute = ParticipantRoute.Jogos },
                onEquipaClick = { currentRoute = ParticipantRoute.Equipa },
                onEstatisticasClick = {},
                onPerfilClick = { currentRoute = ParticipantRoute.Perfil }
            )
        }

        ParticipantRoute.Perfil -> {
            DefinicoesScreen(
                utilizadorLogado = usuarioLogado,
                onTerminarSessaoClick = onTerminarSessao,
                onEditarPerfilClick = { nome, password ->
                    authViewModel.atualizarUtilizador(nome, password)
                },
                onGerirNotificacoesClick = {
                    currentRoute = ParticipantRoute.Notificacoes
                },
                bottomBar = {
                    ParticipantBottomBar(
                        selectedItem = "perfil",
                        onHomeClick = { currentRoute = ParticipantRoute.Home },
                        onTorneiosClick = { currentRoute = ParticipantRoute.Torneios },
                        onJogosClick = { currentRoute = ParticipantRoute.Jogos },
                        onEquipaClick = { currentRoute = ParticipantRoute.Equipa },
                        onEstatisticasClick = { currentRoute = ParticipantRoute.Estatisticas },
                        onPerfilClick = {}
                    )
                }
            )
        }

        ParticipantRoute.Notificacoes -> {
            GestaoNotificacoesScreen(
                onBackClick = {
                    currentRoute = ParticipantRoute.Perfil
                },
                onHomeClick = {
                    currentRoute = ParticipantRoute.Home
                },
                onUtilizadoresClick = {
                    currentRoute = ParticipantRoute.Perfil
                },
                onTorneiosClick = {
                    currentRoute = ParticipantRoute.Torneios
                },
                onGraficosClick = {
                    currentRoute = ParticipantRoute.Estatisticas
                },
                onDefinicoesClick = {
                    currentRoute = ParticipantRoute.Perfil
                }
            )
        }

        is ParticipantRoute.TournamentDetail -> {
            ParticipantTournamentDetailScreen(
                detalhe = detalheTorneio,
                classificacao = classificacaoTorneio,
                onBackClick = {
                    currentRoute = ParticipantRoute.Torneios
                }
            )
        }
    }
}