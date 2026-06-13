package com.leaguematch.ui.organizer

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.leaguematch.data.remote.model.ConfiguracaoNotificacoes
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.data.repository.LeagueMatchRepository
import com.leaguematch.ui.admin.DefinicoesScreen
import com.leaguematch.ui.components.ErrorScreen
import com.leaguematch.ui.components.LoadingScreen
import com.leaguematch.ui.components.OrganizerBottomBar
import com.leaguematch.ui.components.RemoteContent
import com.leaguematch.ui.spectator.ClassificacaoItem
import com.leaguematch.ui.spectator.ClassificacaoScreen
import com.leaguematch.viewmodel.AuthViewModel
import com.leaguematch.viewmodel.TorneiosViewModel
import kotlinx.coroutines.launch

sealed interface OrganizerRoute {
    data object MeusTorneios : OrganizerRoute
    data object CriarTorneio : OrganizerRoute
    data object ConfirmarTorneio : OrganizerRoute
    data object Perfil : OrganizerRoute
    data class DetalheTorneio(val id: Int) : OrganizerRoute
    data class CriarJogo(val torneioId: Int) : OrganizerRoute
    data class GerirEquipas(val torneioId: Int) : OrganizerRoute
    data class CriarEquipa(val torneioId: Int) : OrganizerRoute
    data class VerJogos(val torneioId: Int) : OrganizerRoute
    data class EditarEstatisticas(val torneioId: Int, val jogoId: Int) : OrganizerRoute
    data class VerEstatisticasJogo(val torneioId: Int, val jogoId: Int) : OrganizerRoute
    data class VerClassificacao(val torneioId: Int) : OrganizerRoute
    data class GerirJogadores(val torneioId: Int, val equipaId: Int) : OrganizerRoute
    data class Calendario(val torneioId: Int) : OrganizerRoute
    data class EstatisticasTorneio(val torneioId: Int) : OrganizerRoute
}

@Composable
fun OrganizerFlowContainer(
    torneiosViewModel: TorneiosViewModel,
    authViewModel: AuthViewModel,
    repository: LeagueMatchRepository,
    usuarioLogado: Utilizador?,
    onTerminarSessao: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var currentOrgRoute by remember { mutableStateOf<OrganizerRoute>(OrganizerRoute.MeusTorneios) }
    var currentOrgTorneioId by remember { mutableStateOf<Int?>(null) }
    var dadosCriarTorneio by remember { mutableStateOf<List<String>?>(null) }
    var primaryColorArgb by rememberSaveable { mutableStateOf(0xFFE31734.toInt()) }
    val primaryColor = androidx.compose.ui.graphics.Color(primaryColorArgb)

    LaunchedEffect(primaryColor) {
        com.leaguematch.ui.theme.BrandTheme.primaryColor = primaryColor
    }

    val goMeusTorneios = { currentOrgRoute = OrganizerRoute.MeusTorneios }
    val goPerfil = { currentOrgRoute = OrganizerRoute.Perfil }

    when (currentOrgRoute) {
        OrganizerRoute.MeusTorneios -> {
            val dadosModalidades by torneiosViewModel.modalidadesState.collectAsState()
            val dadosTorneios by torneiosViewModel.todosTorneiosState.collectAsState()

            val orgId = usuarioLogado?.id
            LaunchedEffect(orgId) {
                torneiosViewModel.carregarTorneios()
                if (orgId != null) {
                    torneiosViewModel.carregarTorneiosDoOrganizador(orgId)
                }
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
                    val (modalidades, totalTorneios) = dadosModalidades!!.getOrThrow()
                    val torneios = dadosTorneios!!.getOrThrow()

                    OrgTournamentsScreen(
                        modalidades = modalidades,
                        torneios = torneios,
                        totalTorneios = totalTorneios,
                        onNavigateToCreate = {
                            currentOrgRoute = OrganizerRoute.CriarTorneio
                        },
                        onNavigateToActions = { torneioId: Int ->
                            currentOrgTorneioId = torneioId
                            currentOrgRoute = OrganizerRoute.DetalheTorneio(torneioId)
                        },
                        onEquipasClick = {
                            val tid = currentOrgTorneioId
                            if (tid != null) currentOrgRoute = OrganizerRoute.GerirEquipas(tid)
                        },
                        onJogosClick = {
                            val tid = currentOrgTorneioId
                            if (tid != null) currentOrgRoute = OrganizerRoute.VerJogos(tid)
                        },
                        onPerfilClick = goPerfil,
                        accentColor = primaryColor
                    )
                }
            }
        }

        OrganizerRoute.CriarTorneio -> {
            CreateTournamentScreen(
                onBackClick = {
                    currentOrgRoute = OrganizerRoute.MeusTorneios
                },
                onCancelClick = {
                    dadosCriarTorneio = null
                    currentOrgRoute = OrganizerRoute.MeusTorneios
                },
                onContinueClick = { nome, modalidade, formato, dataInicio, dataFim, maxEquipas, descricao, regras ->
                    dadosCriarTorneio = listOf(
                        nome,
                        modalidade,
                        formato,
                        dataInicio,
                        dataFim,
                        maxEquipas,
                        descricao,
                        regras
                    )
                    currentOrgRoute = OrganizerRoute.ConfirmarTorneio
                }
            )
        }

        OrganizerRoute.ConfirmarTorneio -> {
            val dados = dadosCriarTorneio

            if (dados == null) {
                currentOrgRoute = OrganizerRoute.CriarTorneio
            } else {
                CreateTournamentStep2Screen(
                    nome = dados[0],
                    modalidade = dados[1],
                    formato = dados[2],
                    dataInicio = dados[3],
                    dataFim = dados[4],
                    maxEquipas = dados[5],
                    descricao = dados[6],
                    regras = dados[7],
                    onBackClick = {
                        currentOrgRoute = OrganizerRoute.CriarTorneio
                    },
                    onCancelClick = {
                        dadosCriarTorneio = null
                        currentOrgRoute = OrganizerRoute.MeusTorneios
                    },
                    onCreateClick = { publico, jogosIdaVolta, pontosVitoria ->
                        val orgId = usuarioLogado?.id ?: 0
                        torneiosViewModel.criarTorneio(
                            nome = dados[0],
                            modalidade = dados[1],
                            regras = dados[7],
                            formato = dados[2],
                            organizadorId = orgId,
                            onSuccess = {
                                Toast.makeText(
                                    context,
                                    "Torneio criado com sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                dadosCriarTorneio = null
                                currentOrgRoute = OrganizerRoute.MeusTorneios
                            },
                            onError = { erro ->
                                Toast.makeText(
                                    context,
                                    erro,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        )
                    }
                )
            }
        }

        OrganizerRoute.Perfil -> {
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
                primaryColor = primaryColor,
                onPrimaryColorChange = { color ->
                    primaryColorArgb = color.toArgb()
                },
                onTerminarSessaoClick = {
                    onTerminarSessao()
                    currentOrgRoute = OrganizerRoute.MeusTorneios
                },
                onEditarPerfilClick = { nome, password ->
                    authViewModel.atualizarUtilizador(nome, password)
                },
                bottomBar = {
                    OrganizerBottomBar(
                        selectedItem = "perfil",
                        onTorneiosClick = goMeusTorneios,
                        onPerfilClick = goPerfil,
                        accentColor = primaryColor
                    )
                }
            )
        }

        is OrganizerRoute.DetalheTorneio -> {
            val route = currentOrgRoute as OrganizerRoute.DetalheTorneio
            val detalhe by torneiosViewModel.detalheTorneioState.collectAsState()

            LaunchedEffect(route.id) {
                torneiosViewModel.carregarDetalheTorneio(route.id)
                currentOrgTorneioId = route.id
            }

            RemoteContent(detalhe) {
                OrgTorneioActionsScreen(
                    detalhe = it,
                    onBackClick = goMeusTorneios,
                    onCriarJogoClick = {
                        currentOrgRoute = OrganizerRoute.CriarJogo(route.id)
                    },
                    onVerJogosClick = {
                        currentOrgRoute = OrganizerRoute.VerJogos(route.id)
                    },
                    onVerClassificacaoClick = {
                        currentOrgRoute = OrganizerRoute.VerClassificacao(route.id)
                    },
                    onEditarTorneio = { nome, regras, formato ->
                        torneiosViewModel.editarTorneio(
                            id = route.id,
                            nome = nome,
                            regras = regras,
                            formato = formato,
                            onSuccess = {
                                Toast.makeText(context, "Torneio atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                            },
                            onError = { erro ->
                                Toast.makeText(context, erro, Toast.LENGTH_LONG).show()
                            }
                        )
                    },
                    onRemoverTorneio = { id ->
                        torneiosViewModel.removerTorneio(id)
                        currentOrgRoute = OrganizerRoute.MeusTorneios
                        Toast.makeText(context, "Torneio removido com sucesso!", Toast.LENGTH_SHORT).show()
                    },
                    onGerirEquipasClick = {
                        currentOrgRoute = OrganizerRoute.GerirEquipas(route.id)
                    },
                    onCalendarioClick = {
                        currentOrgRoute = OrganizerRoute.Calendario(route.id)
                    },
                    onEstatisticasClick = {
                        currentOrgRoute = OrganizerRoute.EstatisticasTorneio(route.id)
                    },
                    bottomBar = {
                        OrganizerBottomBar(
                            selectedItem = "torneios",
                            onTorneiosClick = goMeusTorneios,
                            onPerfilClick = goPerfil,
                            accentColor = primaryColor
                        )
                    }
                )
            }
        }

        is OrganizerRoute.CriarJogo -> {
            val route = currentOrgRoute as OrganizerRoute.CriarJogo
            val equipasResult by torneiosViewModel.equipasState.collectAsState()
            val isLoading by torneiosViewModel.criarJogoLoading.collectAsState()

            LaunchedEffect(route.torneioId) {
                torneiosViewModel.carregarEquipas(route.torneioId)
            }

            val detalheAtualCriarJogo by torneiosViewModel.detalheTorneioState.collectAsState()
            val torneio = detalheAtualCriarJogo?.getOrNull()?.torneio
            val equipas = equipasResult?.getOrNull() ?: emptyList()

            if (torneio != null) {
                OrgCriarJogoScreen(
                    torneio = torneio,
                    equipas = equipas,
                    isLoading = isLoading,
                    onBackClick = {
                        currentOrgRoute = OrganizerRoute.DetalheTorneio(route.torneioId)
                    },
                    onCriarClick = { casaId, foraId, data, hora, local ->
                        torneiosViewModel.criarJogo(
                            torneioId = route.torneioId,
                            equipaCasaId = casaId,
                            equipaForaId = foraId,
                            data = data,
                            hora = hora,
                            local = local,
                            onSuccess = {
                                currentOrgRoute = OrganizerRoute.DetalheTorneio(route.torneioId)
                            },
                            onError = { erro ->
                                Toast.makeText(
                                    context,
                                    erro,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        )
                    }
                )
            } else {
                currentOrgRoute = OrganizerRoute.MeusTorneios
            }
        }

        is OrganizerRoute.GerirEquipas -> {
            val route = currentOrgRoute as OrganizerRoute.GerirEquipas
            val equipasResult by torneiosViewModel.equipasState.collectAsState()

            LaunchedEffect(route.torneioId) {
                torneiosViewModel.carregarEquipas(route.torneioId)
            }

            val detalheAtual by torneiosViewModel.detalheTorneioState.collectAsState()
            val torneio = detalheAtual?.getOrNull()?.torneio
            val equipas = equipasResult?.getOrNull() ?: emptyList()
            val isLoading = equipasResult == null

            if (torneio != null) {
                OrgGerirEquipasScreen(
                    torneio = torneio,
                    equipas = equipas,
                    isLoading = isLoading,
                    onBackClick = {
                        currentOrgRoute = OrganizerRoute.DetalheTorneio(route.torneioId)
                    },
                    onCriarEquipaClick = {
                        currentOrgRoute = OrganizerRoute.CriarEquipa(route.torneioId)
                    },
                    onRemoverEquipa = { equipa ->
                        torneiosViewModel.removerEquipa(
                            equipaId = equipa.id,
                            torneioId = route.torneioId,
                            onSuccess = {},
                            onError = { erro ->
                                Toast.makeText(context, erro, Toast.LENGTH_LONG).show()
                            }
                        )
                    },
                    onEditarEquipa = { equipa, novoNome ->
                        torneiosViewModel.editarEquipa(
                            equipaId = equipa.id,
                            nome = novoNome,
                            torneioId = route.torneioId,
                            onSuccess = {},
                            onError = { erro ->
                                Toast.makeText(context, erro, Toast.LENGTH_LONG).show()
                            }
                        )
                    },
                    onGerirJogadores = { equipa ->
                        currentOrgRoute = OrganizerRoute.GerirJogadores(
                            torneioId = route.torneioId,
                            equipaId = equipa.id
                        )
                    }
                )
            } else {
                currentOrgRoute = OrganizerRoute.DetalheTorneio(route.torneioId)
            }
        }

        is OrganizerRoute.GerirJogadores -> {
            val route = currentOrgRoute as OrganizerRoute.GerirJogadores
            val equipasResult by torneiosViewModel.equipasState.collectAsState()
            val jogadores by torneiosViewModel.jogadoresEquipaState.collectAsState()
            val isLoading by torneiosViewModel.jogadoresLoading.collectAsState()

            LaunchedEffect(route.equipaId) {
                torneiosViewModel.carregarEquipas(route.torneioId)
                torneiosViewModel.carregarJogadoresEquipa(route.equipaId)
            }

            val equipa = equipasResult?.getOrNull()?.firstOrNull { it.id == route.equipaId }

            if (equipa != null) {
                OrgGerirJogadoresScreen(
                    equipa = equipa,
                    jogadores = jogadores,
                    isLoading = isLoading,
                    onBackClick = {
                        currentOrgRoute = OrganizerRoute.GerirEquipas(route.torneioId)
                    },
                    onRemoverJogador = { jogador ->
                        torneiosViewModel.removerJogador(route.equipaId, jogador.id)
                    }
                )
            } else {
                LoadingScreen()
            }
        }

        is OrganizerRoute.CriarEquipa -> {
            val route = currentOrgRoute as OrganizerRoute.CriarEquipa
            val isLoading by torneiosViewModel.criarJogoLoading.collectAsState()
            val detalheAtual by torneiosViewModel.detalheTorneioState.collectAsState()
            val torneio = detalheAtual?.getOrNull()?.torneio
            if (torneio != null) {
                OrgCriarEquipaScreen(
                    torneio = torneio,
                    isLoading = isLoading,
                    onBackClick = {
                        currentOrgRoute = OrganizerRoute.GerirEquipas(route.torneioId)
                    },
                    onCriarClick = { nome ->
                        torneiosViewModel.criarEquipa(
                            nome = nome,
                            torneioId = route.torneioId,
                            onSuccess = {
                                currentOrgRoute = OrganizerRoute.GerirEquipas(route.torneioId)
                            },
                            onError = { erro ->
                                Toast.makeText(context, erro, Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                )
            } else {
                currentOrgRoute = OrganizerRoute.GerirEquipas(route.torneioId)
            }
        }

        is OrganizerRoute.VerJogos -> {
            val route = currentOrgRoute as OrganizerRoute.VerJogos
            val detalhe by torneiosViewModel.detalheTorneioState.collectAsState()

            LaunchedEffect(route.torneioId) {
                torneiosViewModel.carregarDetalheTorneio(route.torneioId)
            }

            val detalheAtual by torneiosViewModel.detalheTorneioState.collectAsState()
            val torneio = detalheAtual?.getOrNull()?.torneio
            val jogos = detalhe?.getOrNull()?.jogos ?: emptyList()
            val isLoading = detalhe == null

            if (torneio != null || isLoading) {
                OrgVerJogosScreen(
                    torneio = torneio ?: Torneio(
                        id = route.torneioId, nome = "Torneio",
                        modalidade = "", regras = "", formato = "", estado = ""
                    ),
                    jogos = jogos,
                    isLoading = isLoading,
                    onBackClick = {
                        currentOrgRoute = OrganizerRoute.DetalheTorneio(route.torneioId)
                    },
                    onCriarJogoClick = {
                        currentOrgRoute = OrganizerRoute.CriarJogo(route.torneioId)
                    },
                    onEditarJogo = { jogo ->
                        currentOrgRoute = OrganizerRoute.EditarEstatisticas(
                            torneioId = route.torneioId,
                            jogoId = jogo.id
                        )
                    },
                    onRemoverJogo = { jogo ->
                        torneiosViewModel.removerJogo(
                            jogoId = jogo.id,
                            torneioId = route.torneioId,
                            onSuccess = {},
                            onError = { erro ->
                                Toast.makeText(context, erro, Toast.LENGTH_LONG).show()
                            }
                        )
                    },
                    onVerEstatisticas = { jogo ->
                        currentOrgRoute = OrganizerRoute.VerEstatisticasJogo(
                            torneioId = route.torneioId,
                            jogoId = jogo.id
                        )
                    }
                )
            } else {
                currentOrgRoute = OrganizerRoute.DetalheTorneio(route.torneioId)
            }
        }

        is OrganizerRoute.EditarEstatisticas -> {
            val route = currentOrgRoute as OrganizerRoute.EditarEstatisticas
            val detalhe by torneiosViewModel.detalheTorneioState.collectAsState()

            LaunchedEffect(route.torneioId) {
                torneiosViewModel.carregarDetalheTorneio(route.torneioId)
            }

            val detalheAtual by torneiosViewModel.detalheTorneioState.collectAsState()
            val torneio = detalheAtual?.getOrNull()?.torneio
            val jogos = detalhe?.getOrNull()?.jogos ?: emptyList()
            val jogo = jogos.firstOrNull { it.id == route.jogoId }

            if (torneio != null && jogo != null) {
                OrgEditarEstatisticasJogoScreen(
                    jogo = jogo,
                    modalidade = torneio.modalidade,
                    repository = repository,
                    onBackClick = {
                        currentOrgRoute = OrganizerRoute.VerJogos(route.torneioId)
                    },
                    onGuardarClick = {
                        currentOrgRoute = OrganizerRoute.VerJogos(route.torneioId)
                    }
                )
            } else {
                LoadingScreen()
            }
        }

        is OrganizerRoute.VerEstatisticasJogo -> {
            val route = currentOrgRoute as OrganizerRoute.VerEstatisticasJogo
            val detalhe by torneiosViewModel.detalheTorneioState.collectAsState()
            val estatisticasResult by torneiosViewModel.estatisticasJogoState.collectAsState()

            LaunchedEffect(route.torneioId, route.jogoId) {
                torneiosViewModel.carregarDetalheTorneio(route.torneioId)
                torneiosViewModel.carregarEstatisticasJogo(route.jogoId)
            }

            val torneio = detalhe?.getOrNull()?.torneio
            val jogos = detalhe?.getOrNull()?.jogos ?: emptyList()
            val jogo = jogos.firstOrNull { it.id == route.jogoId }
            val estatisticas = estatisticasResult?.getOrNull() ?: emptyList()

            if (torneio != null && jogo != null) {
                com.leaguematch.ui.spectator.EstatisticasJogoScreen(
                    jogo = jogo,
                    estatisticas = estatisticas,
                    modalidade = torneio.modalidade,
                    onBackClick = {
                        currentOrgRoute = OrganizerRoute.VerJogos(route.torneioId)
                    }
                )
            } else {
                LoadingScreen()
            }
        }

        is OrganizerRoute.VerClassificacao -> {
            val route = currentOrgRoute as OrganizerRoute.VerClassificacao
            val classificationResult by produceState<Result<List<com.leaguematch.data.remote.model.Classificacao>>?>(null, route.torneioId) {
                value = runCatching {
                    repository.obterClassificacao(route.torneioId)
                }
            }

            val detailResult by torneiosViewModel.detalheTorneioState.collectAsState()

            LaunchedEffect(route.torneioId) {
                torneiosViewModel.carregarDetalheTorneio(route.torneioId)
            }

            val torneio = detailResult?.getOrNull()?.torneio

            if (torneio != null) {
                RemoteContent(classificationResult) { classification ->
                    ClassificacaoScreen(
                        torneio = torneio,
                        classificacao = classification.map {
                            ClassificacaoItem(
                                nomeEquipa = it.nomeEquipa,
                                pontos = it.pontos,
                                jogos = it.jogos,
                                vitorias = it.vitorias,
                                empates = it.empates,
                                derrotas = it.derrotas,
                                golosMarcados = it.golosMarcados,
                                golosSofridos = it.golosSofridos
                            )
                        },
                        onBackClick = {
                            currentOrgRoute = OrganizerRoute.DetalheTorneio(route.torneioId)
                        },
                        bottomBar = {
                            OrganizerBottomBar(
                                selectedItem = "torneios",
                                onTorneiosClick = goMeusTorneios,
                                onPerfilClick = goPerfil
                            )
                        }
                    )
                }
            } else {
                LoadingScreen()
            }
        }

        is OrganizerRoute.Calendario -> {
            val route = currentOrgRoute as OrganizerRoute.Calendario
            val detalhe by torneiosViewModel.detalheTorneioState.collectAsState()

            LaunchedEffect(route.torneioId) {
                torneiosViewModel.carregarDetalheTorneio(route.torneioId)
            }

            val data = detalhe?.getOrNull()
            if (data?.torneio != null) {
                OrgCalendarioScreen(
                    torneio = data.torneio,
                    jogos = data.jogos,
                    onBackClick = {
                        currentOrgRoute = OrganizerRoute.DetalheTorneio(route.torneioId)
                    },
                    onAtualizarDataHoraJogo = { jogo, dataNova, horaNova ->

                        torneiosViewModel.atualizarJogo(
                            id = jogo.id,
                            resultadoCasa = jogo.resultadoCasa,
                            resultadoFora = jogo.resultadoFora,
                            estado = jogo.estado,
                            local = jogo.local,
                            data = dataNova,
                            hora = horaNova,
                            onSuccess = {
                                torneiosViewModel.carregarDetalheTorneio(route.torneioId)

                                Toast.makeText(
                                    context,
                                    "Jogo atualizado com sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onError = { erro ->
                                Toast.makeText(
                                    context,
                                    erro,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        )
                    }
                )
            } else {
                LoadingScreen()
            }
        }

        is OrganizerRoute.EstatisticasTorneio -> {
            val route = currentOrgRoute as OrganizerRoute.EstatisticasTorneio
            val detalhe by torneiosViewModel.detalheTorneioState.collectAsState()

            LaunchedEffect(route.torneioId) {
                torneiosViewModel.carregarDetalheTorneio(route.torneioId)
            }

            val data = detalhe?.getOrNull()
            if (data != null) {
                OrgEstatisticasTorneioScreen(
                    detalhe = data,
                    onBackClick = {
                        currentOrgRoute = OrganizerRoute.DetalheTorneio(route.torneioId)
                    }
                )
            } else {
                LoadingScreen()
            }
        }
    }
}


