package com.leaguematch.ui.participant

import androidx.compose.runtime.*
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.ui.admin.DefinicoesScreen
import com.leaguematch.ui.components.ParticipantBottomBar
import com.leaguematch.viewmodel.AuthViewModel
import com.leaguematch.viewmodel.ParticipantViewModel
import com.leaguematch.ui.admin.DefinicoesScreen
import com.leaguematch.ui.components.ParticipantBottomBar

sealed interface ParticipantRoute {
    data object Home : ParticipantRoute
    data object Torneios : ParticipantRoute
    data object Jogos : ParticipantRoute
    data object Estatisticas : ParticipantRoute
    data object Perfil : ParticipantRoute
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

    val torneios by participantViewModel.torneios.collectAsState()
    val jogos by participantViewModel.jogos.collectAsState()

    when (currentRoute) {
        ParticipantRoute.Home -> {
            ParticipantHomeScreen(
                usuarioLogado = usuarioLogado,
                selectedItem = "home",
                onTorneiosClick = { currentRoute = ParticipantRoute.Torneios },
                onJogosClick = { currentRoute = ParticipantRoute.Jogos },
                onEstatisticasClick = { currentRoute = ParticipantRoute.Estatisticas },
                onPerfilClick = { currentRoute = ParticipantRoute.Perfil }
            )
        }

        ParticipantRoute.Torneios -> {
            ParticipantTournamentsScreen(
                torneios = torneios,
                onHomeClick = { currentRoute = ParticipantRoute.Home },
                onTorneiosClick = {},
                onJogosClick = { currentRoute = ParticipantRoute.Jogos },
                onEstatisticasClick = { currentRoute = ParticipantRoute.Estatisticas },
                onPerfilClick = { currentRoute = ParticipantRoute.Perfil }
            )
        }

        ParticipantRoute.Jogos -> {
            ParticipantGamesScreen(
                jogos = jogos,
                onHomeClick = { currentRoute = ParticipantRoute.Home },
                onTorneiosClick = { currentRoute = ParticipantRoute.Torneios },
                onJogosClick = {},
                onEstatisticasClick = { currentRoute = ParticipantRoute.Estatisticas },
                onPerfilClick = { currentRoute = ParticipantRoute.Perfil }
            )
        }

        ParticipantRoute.Estatisticas -> {
            ParticipantStatsScreen(
                stats = ParticipantStats(
                    jogos = jogos.size,
                    golos = jogos.sumOf { it.resultadoCasa + it.resultadoFora },
                    assistencias = 0,
                    mvp = 0
                ),
                onHomeClick = { currentRoute = ParticipantRoute.Home },
                onTorneiosClick = { currentRoute = ParticipantRoute.Torneios },
                onJogosClick = { currentRoute = ParticipantRoute.Jogos },
                onEstatisticasClick = {},
                onPerfilClick = { currentRoute = ParticipantRoute.Perfil }
            )
        }

        ParticipantRoute.Perfil -> {
            DefinicoesScreen(
                utilizadorLogado = usuarioLogado,
                onTerminarSessaoClick = onTerminarSessao,
                onEditarPerfilClick = { _, _ -> },
                onGerirNotificacoesClick = {},
                bottomBar = {
                    ParticipantBottomBar(
                        selectedItem = "perfil",
                        onHomeClick = { currentRoute = ParticipantRoute.Home },
                        onTorneiosClick = { currentRoute = ParticipantRoute.Torneios },
                        onJogosClick = { currentRoute = ParticipantRoute.Jogos },
                        onEstatisticasClick = { currentRoute = ParticipantRoute.Estatisticas },
                        onPerfilClick = {}
                    )
                }
            )
        }
    }
}