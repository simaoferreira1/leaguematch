package com.leaguematch.ui.spectator

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leaguematch.data.remote.model.Classificacao
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.data.repository.LeagueMatchRepository
import com.leaguematch.ui.admin.DefinicoesScreen
import com.leaguematch.ui.components.SpectatorBottomBar
import com.leaguematch.viewmodel.AuthViewModel
import com.leaguematch.viewmodel.TorneiosViewModel

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
}

@Composable
fun SpectatorFlowContainer(
    torneiosViewModel: TorneiosViewModel,
    authViewModel: AuthViewModel,
    repository: LeagueMatchRepository,
    usuarioLogado: Utilizador?,
    onTerminarSessao: () -> Unit
) {
    val context = LocalContext.current
    var currentSpectatorRoute by remember { mutableStateOf<SpectatorRoute>(SpectatorRoute.Explorar) }
    var torneioSelecionado by remember { mutableStateOf<Torneio?>(null) }

    when (currentSpectatorRoute) {
        SpectatorRoute.Explorar -> {
            val dadosTorneios by torneiosViewModel.todosTorneiosState.collectAsState()
            val dadosJogosAoVivo by torneiosViewModel.jogosAoVivoState.collectAsState()

            LaunchedEffect(Unit) {
                torneiosViewModel.carregarTodosTorneios()
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
            NotificacoesScreen(
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

            JogoEmDiretoScreen(
                jogo = route.jogo,
                estatisticas = estatisticas,
                eventos = eventos,
                onBackClick = {
                    currentSpectatorRoute = SpectatorRoute.Explorar
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
