/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: ParticipantFlowContainer.kt
 * Tipo: Interface (Compose View) do Participante
 *
 * Descrição:
 * Este ficheiro define um ecrã do fluxo do Jogador/Participante em Jetpack Compose.\n * Mostra ao participante o estado do seu torneio, código de equipas para inscrição, estatísticas e notificações.
 */
package com.leaguematch.ui.participant // Define o pacote deste ficheiro de código

import android.content.Context // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.LaunchedEffect // Importa dependência / biblioteca necessária
import androidx.compose.runtime.collectAsState // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableLongStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.remember // Importa dependência / biblioteca necessária
import androidx.compose.runtime.rememberCoroutineScope // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.platform.LocalContext // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.ConfiguracaoNotificacoes // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Jogo // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Utilizador // Importa dependência / biblioteca necessária
import com.leaguematch.data.repository.LeagueMatchRepository // Importa dependência / biblioteca necessária
import com.leaguematch.translations.Language // Importa dependência / biblioteca necessária
import com.leaguematch.translations.StringsEn // Importa dependência / biblioteca necessária
import com.leaguematch.translations.StringsPt // Importa dependência / biblioteca necessária
import com.leaguematch.ui.admin.DefinicoesScreen // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.ParticipantBottomBar // Importa dependência / biblioteca necessária
import com.leaguematch.viewmodel.AuthViewModel // Importa dependência / biblioteca necessária
import com.leaguematch.viewmodel.ParticipantViewModel // Importa dependência / biblioteca necessária
import com.leaguematch.viewmodel.TorneiosViewModel // Importa dependência / biblioteca necessária
import kotlinx.coroutines.launch // Importa dependência / biblioteca necessária

sealed interface ParticipantRoute { // Declaração de interface (contrato de métodos)
    data object Home : ParticipantRoute // Declaração de objeto estático / Singleton
    data object Torneios : ParticipantRoute // Declaração de objeto estático / Singleton
    data object Jogos : ParticipantRoute // Declaração de objeto estático / Singleton
    data object Equipa : ParticipantRoute // Declaração de objeto estático / Singleton
    data object Estatisticas : ParticipantRoute // Declaração de objeto estático / Singleton
    data object Perfil : ParticipantRoute // Declaração de objeto estático / Singleton
    data object Notificacoes : ParticipantRoute // Declaração de objeto estático / Singleton
    data object JoinTeam : ParticipantRoute // Declaração de objeto estático / Singleton
    data object InboxNotificacoes : ParticipantRoute // Declaração de objeto estático / Singleton

    data class TournamentDetail(val torneioId: Int) : ParticipantRoute // Declaração de classe para modelar objetos
    data class VerEstatisticasJogo(val jogo: Jogo) : ParticipantRoute // Declaração de classe para modelar objetos
}

@Composable
fun ParticipantFlowContainer( // Declaração de função / método de lógica
    torneiosViewModel: TorneiosViewModel,
    authViewModel: AuthViewModel,
    participantViewModel: ParticipantViewModel,
    repository: LeagueMatchRepository,
    usuarioLogado: Utilizador?,
    onTerminarSessao: () -> Unit
) {
    val context = LocalContext.current // Declara constante local (leitura única)
    val coroutineScope = rememberCoroutineScope() // Cria escopo local para lançar coroutines em cliques na UI

    val prefs = remember { // Memoriza estado para evitar perda durante a recomposição
        context.getSharedPreferences("participant_preferences", Context.MODE_PRIVATE)
    }

    var currentRoute by remember { // Memoriza estado para evitar perda durante a recomposição
        mutableStateOf<ParticipantRoute>(ParticipantRoute.Home) // Declara estado mutável local do Compose
    }

    var language by remember { // Memoriza estado para evitar perda durante a recomposição
        mutableStateOf( // Declara estado mutável local do Compose
            when (prefs.getString("language", Language.PT.name)) { // Escolha múltipla condicional (semelhante a switch-case)
                Language.EN.name -> Language.EN
                else -> Language.PT // Fluxo condicional alternativo caso o 'if' seja falso
            }
        )
    }

    var primaryColorLong by remember { // Memoriza estado para evitar perda durante a recomposição
        mutableLongStateOf(
            prefs.getLong("primary_color", 0xFFE31734L)
        )
    }

    val primaryColor = Color(primaryColorLong) // Declara constante local (leitura única)

    LaunchedEffect(primaryColor) { // Efeito colateral Compose: executa código assíncrono ao recompor
        com.leaguematch.ui.theme.BrandTheme.primaryColor = primaryColor
    }

    val strings = when (language) { // Escolha múltipla condicional (semelhante a switch-case)
        Language.PT -> StringsPt
        Language.EN -> StringsEn
    }

    LaunchedEffect(usuarioLogado?.id) { // Efeito colateral Compose: executa código assíncrono ao recompor
        usuarioLogado?.id?.let { id ->
            participantViewModel.carregarDadosParticipante(id)
        }
    }

    val torneios by participantViewModel.torneios.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)
    val equipa by participantViewModel.equipa.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)
    val equipas by participantViewModel.equipas.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)
    val jogadoresEquipa by participantViewModel.jogadoresEquipa.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)
    val classificacaoEquipa by participantViewModel.classificacaoEquipa.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)
    val jogosEquipa by participantViewModel.jogosEquipa.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)
    val statsParticipante by participantViewModel.statsParticipante.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)

    val detalheTorneio by participantViewModel.detalheTorneio.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)
    val classificacaoTorneio by participantViewModel.classificacaoTorneio.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)

    val juntarEquipaLoading by participantViewModel.juntarEquipaLoading.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)
    val juntarEquipaErro by participantViewModel.juntarEquipaErro.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)

    val notificacoesParticipante by participantViewModel.notificacoesParticipante.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)

    when (currentRoute) { // Escolha múltipla condicional (semelhante a switch-case)

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
                    val id = usuarioLogado?.id ?: return@ParticipantTeamScreen // Retorna o resultado da execução da função
                    participantViewModel.selecionarEquipa(
                        utilizadorId = id,
                        equipaId = equipaId
                    )
                },
                onSairEquipaClick = { equipaId ->
                    val id = usuarioLogado?.id ?: return@ParticipantTeamScreen // Retorna o resultado da execução da função
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
                    val id = usuarioLogado?.id ?: return@ParticipantJoinTeamScreen // Retorna o resultado da execução da função
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
                        repository.atualizarConfiguracaoNotificacoes(config) // Efetua chamada remota ou local ao repositório de dados
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
                    val novaCor = when (color) { // Escolha múltipla condicional (semelhante a switch-case)
                        Color(0xFFE31734) -> 0xFFE31734L
                        Color(0xFF2563EB) -> 0xFF2563EBL
                        Color(0xFF16A34A) -> 0xFF16A34AL
                        Color(0xFF9333EA) -> 0xFF9333EAL
                        else -> 0xFFE31734L // Fluxo condicional alternativo caso o 'if' seja falso
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
            ParticipantNotificationsScreen(
                configuracao = ConfiguracaoNotificacoes(
                    utilizadorId = usuarioLogado?.id ?: 0
                ),
                onGuardarConfiguracao = { config ->
                    coroutineScope.launch {
                        repository.atualizarConfiguracaoNotificacoes(config) // Efetua chamada remota ou local ao repositório de dados
                    }
                },
                onHomeClick = { currentRoute = ParticipantRoute.Home },
                onTorneiosClick = { currentRoute = ParticipantRoute.Torneios },
                onJogosClick = { currentRoute = ParticipantRoute.Jogos },
                onEquipaClick = { currentRoute = ParticipantRoute.Equipa },
                onEstatisticasClick = { currentRoute = ParticipantRoute.Estatisticas },
                onPerfilClick = { currentRoute = ParticipantRoute.Perfil },
                onAbrirInbox = {
                    currentRoute = ParticipantRoute.InboxNotificacoes
                }
            )
        }

        ParticipantRoute.InboxNotificacoes -> {
            com.leaguematch.ui.spectator.InboxNotificacoesScreen(
                notificacoes = notificacoesParticipante,
                onBackClick = {
                    currentRoute = ParticipantRoute.Notificacoes
                },
                onMarcarTodasLidas = {
                    usuarioLogado?.id?.let { id ->
                        participantViewModel.marcarTodasNotificacoesComoLidas(id)
                    }
                },
                onNotificacaoClick = { notificacao ->
                    usuarioLogado?.id?.let { id ->
                        participantViewModel.marcarNotificacaoComoLida(
                            utilizadorId = id,
                            notificacaoId = notificacao.id
                        )
                    }
                }
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
            val route = currentRoute as ParticipantRoute.VerEstatisticasJogo // Declara constante local (leitura única)
            val estatisticasResult by torneiosViewModel.estatisticasJogoState.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)

            LaunchedEffect(route.jogo.id) { // Efeito colateral Compose: executa código assíncrono ao recompor
                torneiosViewModel.carregarEstatisticasJogo(route.jogo.id)
            }

            val estatisticas = estatisticasResult?.getOrNull() ?: emptyList() // Declara constante local (leitura única)

            val modalidade = participantViewModel.torneios.value // Declara constante local (leitura única)
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

