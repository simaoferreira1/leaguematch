package com.leaguematch.ui.spectator

import androidx.compose.runtime.*
import com.leaguematch.data.remote.model.Classificacao
import com.leaguematch.data.remote.model.ConfiguracaoNotificacoes
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.data.repository.LeagueMatchRepository
import com.leaguematch.ui.admin.DefinicoesScreen
import com.leaguematch.ui.components.RemoteContent
import com.leaguematch.ui.components.SpectatorBottomBar
import com.leaguematch.viewmodel.AuthViewModel
import com.leaguematch.viewmodel.TorneiosViewModel
import kotlinx.coroutines.launch

sealed interface SpectatorRoute {
    data object Explorar : SpectatorRoute
    data object EscolherTorneio : SpectatorRoute
    data object TorneioDetalhe : SpectatorRoute
    data object Classificacao : SpectatorRoute
    data object Jogos : SpectatorRoute
    data object Equipas : SpectatorRoute
    data object Perfil : SpectatorRoute
    data object Notificacoes : SpectatorRoute
    data class JogoEmDireto(val jogo: Jogo) : SpectatorRoute
    data class EstatisticasJogo(val jogo: Jogo) : SpectatorRoute
}

@Composable
fun SpectatorFlowContainer(
    torneiosViewModel: TorneiosViewModel,
    authViewModel: AuthViewModel,
    repository: LeagueMatchRepository,
    usuarioLogado: Utilizador?,
    onTerminarSessao: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var currentSpectatorRoute by remember {
        mutableStateOf<SpectatorRoute>(SpectatorRoute.Explorar)
    }

    var torneioSelecionado by remember {
        mutableStateOf<Torneio?>(null)
    }

    val dadosTorneios by torneiosViewModel.todosTorneiosState.collectAsState()

    LaunchedEffect(Unit) {
        torneiosViewModel.carregarTodosTorneios()
    }

    when (currentSpectatorRoute) {

        SpectatorRoute.Explorar -> {
            val dadosJogosAoVivo by torneiosViewModel.jogosAoVivoState.collectAsState()

            LaunchedEffect(Unit) {
                torneiosViewModel.carregarJogosAoVivo()
            }

            RemoteContent(dadosTorneios) { list: List<Torneio> ->
                val liveMatches = dadosJogosAoVivo?.getOrNull() ?: emptyList()

                ExplorarScreen(
                    liveMatches = liveMatches,
                    trendingTournaments = list,
                    onTorneioClick = { torneio ->
                        torneioSelecionado = torneio
                        currentSpectatorRoute = SpectatorRoute.TorneioDetalhe
                    },
                    onJogoClick = { jogo ->
                        currentSpectatorRoute = SpectatorRoute.JogoEmDireto(jogo)
                    }
                )
            }
        }

        SpectatorRoute.TorneioDetalhe -> {
            val torneio = torneioSelecionado

            if (torneio == null) {
                currentSpectatorRoute = SpectatorRoute.Explorar
            } else {
                val marcadoresResult by produceState<Result<List<MelhorMarcadorItem>>?>(null, torneio.id) {
                    value = runCatching {
                        repository.obterMelhoresMarcadores(torneio.id)
                    }
                }

                val jogosResult by produceState<Result<List<JogoResumoItem>>?>(null, torneio.id) {
                    value = runCatching {
                        repository.obterJogosDoTorneio(torneio.id)
                    }
                }

                RemoteContent(marcadoresResult) { marcadores ->
                    RemoteContent(jogosResult) { jogos ->
                        TorneioDetalheScreen(
                            torneio = torneio,
                            melhoresMarcadores = marcadores,
                            jogos = jogos,
                            onBackClick = {
                                currentSpectatorRoute = SpectatorRoute.Explorar
                            },
                            onVerJogosClick = {
                                currentSpectatorRoute = SpectatorRoute.Jogos
                            },
                            onHomeClick = {
                                currentSpectatorRoute = SpectatorRoute.Explorar
                            },
                            onClassificacaoClick = {
                                currentSpectatorRoute = SpectatorRoute.Classificacao
                            },
                            onJogosClick = {
                                currentSpectatorRoute = SpectatorRoute.Jogos
                            },
                            onEquipasClick = {
                                currentSpectatorRoute = SpectatorRoute.Equipas
                            },
                            onPerfilClick = {
                                currentSpectatorRoute = SpectatorRoute.Perfil
                            }
                        )
                    }
                }
            }
        }

        SpectatorRoute.Classificacao -> {
            val torneio = torneioSelecionado

            if (torneio == null) {
                currentSpectatorRoute = SpectatorRoute.Explorar
            } else {
                val classificacaoResult by produceState<Result<List<Classificacao>>?>(null, torneio.id) {
                    value = runCatching {
                        repository.obterClassificacao(torneio.id)
                    }
                }

                RemoteContent(classificacaoResult) { classificacao ->
                    ClassificacaoScreen(
                        torneio = torneio,
                        classificacao = classificacao.map {
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
                        bottomBar = {
                            SpectatorBottomBar(
                                selectedItem = "classificacao",
                                onHomeClick = {
                                    currentSpectatorRoute = SpectatorRoute.Explorar
                                },
                                onClassificacaoClick = {},
                                onJogosClick = {
                                    currentSpectatorRoute = SpectatorRoute.Jogos
                                },
                                onEquipasClick = {
                                    currentSpectatorRoute = SpectatorRoute.Equipas
                                },
                                onPerfilClick = {
                                    currentSpectatorRoute = SpectatorRoute.Perfil
                                }
                            )
                        }
                    )
                }
            }
        }

        SpectatorRoute.Jogos -> {
            val torneio = torneioSelecionado

            if (torneio == null) {
                currentSpectatorRoute = SpectatorRoute.Explorar
            } else {
                val jogosResult by produceState<Result<List<Jogo>>?>(null, torneio.id) {
                    value = runCatching {
                        repository.obterDetalheTorneio(torneio.id)?.jogos ?: emptyList()
                    }
                }

                val jogos = jogosResult?.getOrNull() ?: emptyList()

                JogosScreen(
                    torneio = torneio,
                    jogos = jogos,
                    onHomeClick = {
                        currentSpectatorRoute = SpectatorRoute.Explorar
                    },
                    onClassificacaoClick = {
                        currentSpectatorRoute = SpectatorRoute.Classificacao
                    },
                    onJogosClick = {},
                    onEquipasClick = {
                        currentSpectatorRoute = SpectatorRoute.Equipas
                    },
                    onPerfilClick = {
                        currentSpectatorRoute = SpectatorRoute.Perfil
                    },
                    onJogoClick = { jogo ->
                        currentSpectatorRoute = SpectatorRoute.JogoEmDireto(jogo)
                    }
                )
            }
        }

        SpectatorRoute.Equipas -> {
            val torneio = torneioSelecionado

            if (torneio == null) {
                currentSpectatorRoute = SpectatorRoute.Explorar
            } else {
                val equipasResult by produceState<Result<List<com.leaguematch.data.remote.model.Equipa>>?>(null, torneio.id) {
                    value = runCatching {
                        repository.listarEquipasTorneio(torneio.id)
                    }
                }

                val equipas = equipasResult?.getOrNull() ?: emptyList()

                EquipasScreen(
                    torneio = torneio,
                    equipas = equipas,
                    onHomeClick = {
                        currentSpectatorRoute = SpectatorRoute.Explorar
                    },
                    onClassificacaoClick = {
                        currentSpectatorRoute = SpectatorRoute.Classificacao
                    },
                    onJogosClick = {
                        currentSpectatorRoute = SpectatorRoute.Jogos
                    },
                    onEquipasClick = {},
                    onPerfilClick = {
                        currentSpectatorRoute = SpectatorRoute.Perfil
                    }
                )
            }
        }

        SpectatorRoute.Notificacoes -> {
            val utilizador = usuarioLogado

            if (utilizador == null) {
                currentSpectatorRoute = SpectatorRoute.Perfil
            } else {
                val configuracaoResult by produceState<Result<ConfiguracaoNotificacoes>?>(null, utilizador.id) {
                    value = runCatching {
                        repository.obterConfiguracaoNotificacoes(utilizador.id)
                    }
                }

                RemoteContent(configuracaoResult) { configuracao ->
                    NotificacoesScreen(
                        configuracao = configuracao,
                        onGuardarConfiguracao = { novaConfiguracao ->
                            coroutineScope.launch {
                                repository.atualizarConfiguracaoNotificacoes(novaConfiguracao)
                            }
                        },
                        onHomeClick = {
                            currentSpectatorRoute = SpectatorRoute.Explorar
                        },
                        onClassificacaoClick = {
                            currentSpectatorRoute = SpectatorRoute.Classificacao
                        },
                        onJogosClick = {
                            currentSpectatorRoute = SpectatorRoute.Jogos
                        },
                        onEquipasClick = {
                            currentSpectatorRoute = SpectatorRoute.Equipas
                        },
                        onPerfilClick = {
                            currentSpectatorRoute = SpectatorRoute.Perfil
                        }
                    )
                }
            }
        }

        is SpectatorRoute.JogoEmDireto -> {
            val route = currentSpectatorRoute as SpectatorRoute.JogoEmDireto
            val estatisticasResult by torneiosViewModel.estatisticasJogoState.collectAsState()
            val eventosResult by torneiosViewModel.eventosJogoState.collectAsState()

            LaunchedEffect(route.jogo.id) {
                torneiosViewModel.carregarEstatisticasJogo(route.jogo.id)
                torneiosViewModel.carregarEventosJogo(route.jogo.id)
            }

            val estatisticas = estatisticasResult?.getOrNull() ?: emptyList()
            val eventos = eventosResult?.getOrNull() ?: emptyList()

            val modalidade = (dadosTorneios?.getOrNull() ?: emptyList())
                .firstOrNull { it.id == route.jogo.torneioId }?.modalidade
                ?: torneioSelecionado?.modalidade
                ?: "Futebol"

            JogoEmDiretoScreen(
                jogo = route.jogo,
                estatisticas = estatisticas,
                eventos = eventos,
                modalidade = modalidade,
                onBackClick = {
                    currentSpectatorRoute = SpectatorRoute.Explorar
                },
                onVerEstatisticasClick = {
                    currentSpectatorRoute =
                        SpectatorRoute.EstatisticasJogo(route.jogo)
                }
            )
        }

        is SpectatorRoute.EstatisticasJogo -> {

            val route = currentSpectatorRoute as SpectatorRoute.EstatisticasJogo

            val estatisticasResult by torneiosViewModel.estatisticasJogoState.collectAsState()

            LaunchedEffect(route.jogo.id) {
                torneiosViewModel.carregarEstatisticasJogo(route.jogo.id)
            }

            val estatisticas =
                estatisticasResult?.getOrNull() ?: emptyList()

            val modalidade = (dadosTorneios?.getOrNull() ?: emptyList())
                .firstOrNull { it.id == route.jogo.torneioId }?.modalidade
                ?: torneioSelecionado?.modalidade
                ?: "Futebol"

            EstatisticasJogoScreen(
                jogo = route.jogo,
                estatisticas = estatisticas,
                modalidade = modalidade,
                onBackClick = {
                    currentSpectatorRoute =
                        SpectatorRoute.JogoEmDireto(route.jogo)
                }
            )
        }

        SpectatorRoute.Perfil -> {
            DefinicoesScreen(
                utilizadorLogado = usuarioLogado,
                onTerminarSessaoClick = {
                    onTerminarSessao()
                    currentSpectatorRoute = SpectatorRoute.Explorar
                    torneioSelecionado = null
                },
                onEditarPerfilClick = { nome, password ->
                    authViewModel.atualizarUtilizador(nome, password)
                },
                onGerirNotificacoesClick = {
                    currentSpectatorRoute = SpectatorRoute.Notificacoes
                },
                bottomBar = {
                    SpectatorBottomBar(
                        selectedItem = "perfil",
                        onHomeClick = {
                            currentSpectatorRoute = SpectatorRoute.Explorar
                        },
                        onClassificacaoClick = {
                            currentSpectatorRoute = SpectatorRoute.Classificacao
                        },
                        onJogosClick = {
                            currentSpectatorRoute = SpectatorRoute.Jogos
                        },
                        onEquipasClick = {
                            currentSpectatorRoute = SpectatorRoute.Equipas
                        },
                        onPerfilClick = {}
                    )
                }
            )
        }

        else -> {
            LaunchedEffect(Unit) {
                currentSpectatorRoute = SpectatorRoute.Explorar
            }
        }
    }
}