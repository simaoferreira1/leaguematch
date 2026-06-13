package com.leaguematch.ui.participant

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.leaguematch.data.remote.model.ConfiguracaoNotificacoes
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.data.repository.LeagueMatchRepository
import com.leaguematch.translations.Language
import com.leaguematch.translations.StringsEn
import com.leaguematch.translations.StringsPt
import com.leaguematch.ui.admin.DefinicoesScreen
import com.leaguematch.ui.admin.GestaoNotificacoesScreen
import com.leaguematch.ui.components.ParticipantBottomBar
import com.leaguematch.viewmodel.AuthViewModel
import com.leaguematch.viewmodel.ParticipantViewModel
import com.leaguematch.viewmodel.TorneiosViewModel
import kotlinx.coroutines.launch

sealed interface ParticipantRoute {
    data object Home : ParticipantRoute
    data object Torneios : ParticipantRoute
    data object Jogos : ParticipantRoute
    data object Equipa : ParticipantRoute
    data object Estatisticas : ParticipantRoute
    data object Perfil : ParticipantRoute
    data object Notificacoes : ParticipantRoute
    data object JoinTeam : ParticipantRoute

    data class TournamentDetail(val torneioId: Int) : ParticipantRoute
    data class VerEstatisticasJogo(val jogo: Jogo) : ParticipantRoute
}

@Composable
fun ParticipantFlowContainer(
    torneiosViewModel: TorneiosViewModel,
    authViewModel: AuthViewModel,
    participantViewModel: ParticipantViewModel,
    repository: LeagueMatchRepository,
    usuarioLogado: Utilizador?,
    onTerminarSessao: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val prefs = remember {
        context.getSharedPreferences("participant_preferences", Context.MODE_PRIVATE)
    }

    var currentRoute by remember {
        mutableStateOf<ParticipantRoute>(ParticipantRoute.Home)
    }

    var language by remember {
        mutableStateOf(
            when (prefs.getString("language", Language.PT.name)) {
                Language.EN.name -> Language.EN
                else -> Language.PT
            }
        )
    }

    var primaryColorLong by remember {
        mutableLongStateOf(
            prefs.getLong("primary_color", 0xFFE31734L)
        )
    }

    val primaryColor = Color(primaryColorLong)

    LaunchedEffect(primaryColor) {
        com.leaguematch.ui.theme.BrandTheme.primaryColor = primaryColor
    }

    val strings = when (language) {
        Language.PT -> StringsPt
        Language.EN -> StringsEn
    }

    LaunchedEffect(usuarioLogado?.id) {
        usuarioLogado?.id?.let { id ->
            participantViewModel.carregarDadosParticipante(id)
        }
    }

    val torneios by participantViewModel.torneios.collectAsState()
    val equipa by participantViewModel.equipa.collectAsState()
    val equipas by participantViewModel.equipas.collectAsState()
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
                strings = strings,
                primaryColor = primaryColor,
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
                strings = strings,
                primaryColor = primaryColor,
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
                strings = strings,
                primaryColor = primaryColor,
                onHomeClick = { currentRoute = ParticipantRoute.Home },
                onTorneiosClick = { currentRoute = ParticipantRoute.Torneios },
                onJogosClick = {},
                onEquipaClick = { currentRoute = ParticipantRoute.Equipa },
                onEstatisticasClick = { currentRoute = ParticipantRoute.Estatisticas },
                onPerfilClick = { currentRoute = ParticipantRoute.Perfil },
                onJogoClick = { jogo ->
                    currentRoute = ParticipantRoute.VerEstatisticasJogo(jogo)
                }
            )
        }

        ParticipantRoute.Equipa -> {
            ParticipantTeamScreen(
                equipa = equipa,
                equipas = equipas,
                jogadores = jogadoresEquipa,
                classificacao = classificacaoEquipa,
                jogos = jogosEquipa,
                strings = strings,
                primaryColor = primaryColor,
                onJoinTeamClick = {
                    participantViewModel.limparErroJuntarEquipa()
                    currentRoute = ParticipantRoute.JoinTeam
                },
                onSelecionarEquipaClick = { equipaId ->
                    val id = usuarioLogado?.id ?: return@ParticipantTeamScreen
                    participantViewModel.selecionarEquipa(
                        utilizadorId = id,
                        equipaId = equipaId
                    )
                },
                onSairEquipaClick = { equipaId ->
                    val id = usuarioLogado?.id ?: return@ParticipantTeamScreen
                    participantViewModel.sairDaEquipa(
                        utilizadorId = id,
                        equipaId = equipaId
                    )
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
                strings = strings,
                primaryColor = primaryColor,
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
                    faltas = statsParticipante.faltas,
                    cartoes = statsParticipante.cartoes
                ),
                strings = strings,
                primaryColor = primaryColor,
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
                configuracaoNotificacoes = ConfiguracaoNotificacoes(
                    utilizadorId = usuarioLogado?.id ?: 0
                ),
                onGuardarConfiguracaoNotificacoes = { config ->
                    coroutineScope.launch {
                        repository.atualizarConfiguracaoNotificacoes(config)
                    }
                },
                language = language,
                onLanguageChange = { newLanguage ->
                    language = newLanguage

                    prefs.edit()
                        .putString("language", newLanguage.name)
                        .apply()
                },
                primaryColor = primaryColor,
                onPrimaryColorChange = { color ->
                    val novaCor = when (color) {
                        Color(0xFFE31734) -> 0xFFE31734L
                        Color(0xFF2563EB) -> 0xFF2563EBL
                        Color(0xFF16A34A) -> 0xFF16A34AL
                        Color(0xFF9333EA) -> 0xFF9333EAL
                        else -> 0xFFE31734L
                    }

                    primaryColorLong = novaCor

                    prefs.edit()
                        .putLong("primary_color", novaCor)
                        .apply()
                },
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
                onBackClick = { currentRoute = ParticipantRoute.Perfil },
                onHomeClick = { currentRoute = ParticipantRoute.Home },
                onUtilizadoresClick = { currentRoute = ParticipantRoute.Perfil },
                onTorneiosClick = { currentRoute = ParticipantRoute.Torneios },
                onGraficosClick = { currentRoute = ParticipantRoute.Estatisticas },
                onDefinicoesClick = { currentRoute = ParticipantRoute.Perfil }
            )
        }

        is ParticipantRoute.TournamentDetail -> {
            ParticipantTournamentDetailScreen(
                detalhe = detalheTorneio,
                classificacao = classificacaoTorneio,
                strings = strings,
                primaryColor = primaryColor,
                onBackClick = {
                    currentRoute = ParticipantRoute.Torneios
                }
            )
        }

        is ParticipantRoute.VerEstatisticasJogo -> {
            val route = currentRoute as ParticipantRoute.VerEstatisticasJogo
            val estatisticasResult by torneiosViewModel.estatisticasJogoState.collectAsState()

            LaunchedEffect(route.jogo.id) {
                torneiosViewModel.carregarEstatisticasJogo(route.jogo.id)
            }

            val estatisticas = estatisticasResult?.getOrNull() ?: emptyList()

            val modalidade = participantViewModel.torneios.value
                .firstOrNull { it.id == route.jogo.torneioId }
                ?.modalidade ?: "Futebol"

            com.leaguematch.ui.spectator.EstatisticasJogoScreen(
                jogo = route.jogo,
                estatisticas = estatisticas,
                modalidade = modalidade,
                onBackClick = {
                    currentRoute = ParticipantRoute.Jogos
                }
            )
        }
    }
}