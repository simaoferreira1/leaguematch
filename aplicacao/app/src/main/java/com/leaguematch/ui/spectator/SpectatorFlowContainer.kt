/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: SpectatorFlowContainer.kt
 * Tipo: Interface (Compose View) do Espectador
 *
 * Descrição:
 * Este ficheiro define um ecrã de visualização pública (Espectador) em Jetpack Compose.\n * Apenas exibe dados para leitura (como tabelas de classificação, jogos ao vivo e calendários) sem permitir alteração.
 */
    package com.leaguematch.ui.spectator // Define o pacote deste ficheiro de código

    import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.LaunchedEffect // Importa dependência / biblioteca necessária
import androidx.compose.runtime.collectAsState // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.produceState // Importa dependência / biblioteca necessária
import androidx.compose.runtime.remember // Importa dependência / biblioteca necessária
import androidx.compose.runtime.rememberCoroutineScope // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Classificacao // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.ConfiguracaoNotificacoes // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Equipa // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Jogo // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Torneio // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Utilizador // Importa dependência / biblioteca necessária
import com.leaguematch.data.repository.LeagueMatchRepository // Importa dependência / biblioteca necessária
import com.leaguematch.ui.admin.DefinicoesScreen // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.RemoteContent // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.SpectatorBottomBar // Importa dependência / biblioteca necessária
import com.leaguematch.viewmodel.AuthViewModel // Importa dependência / biblioteca necessária
import com.leaguematch.viewmodel.TorneiosViewModel // Importa dependência / biblioteca necessária
import kotlinx.coroutines.launch // Importa dependência / biblioteca necessária

    sealed interface SpectatorRoute { // Declaração de interface (contrato de métodos)
        data object Explorar : SpectatorRoute // Declaração de objeto estático / Singleton
        data object EscolherTorneio : SpectatorRoute // Declaração de objeto estático / Singleton
        data object TorneioDetalhe : SpectatorRoute // Declaração de objeto estático / Singleton
        data object Classificacao : SpectatorRoute // Declaração de objeto estático / Singleton
        data object Jogos : SpectatorRoute // Declaração de objeto estático / Singleton
        data object Equipas : SpectatorRoute // Declaração de objeto estático / Singleton
        data object Perfil : SpectatorRoute // Declaração de objeto estático / Singleton
        data object Notificacoes : SpectatorRoute // Declaração de objeto estático / Singleton
        data object InboxNotificacoes : SpectatorRoute // Declaração de objeto estático / Singleton

        data class JogoEmDireto(val jogo: Jogo, val voltarPara: SpectatorRoute) : SpectatorRoute // Declaração de classe para modelar objetos
        data class EstatisticasJogo(val jogo: Jogo) : SpectatorRoute // Declaração de classe para modelar objetos
        data class EquipaDetalhe(val equipa: Equipa) : SpectatorRoute // Declaração de classe para modelar objetos
        data class CalendarioEquipa(val equipa: Equipa) : SpectatorRoute // Declaração de classe para modelar objetos
    }

    @Composable
    fun SpectatorFlowContainer( // Declaração de função / método de lógica
        torneiosViewModel: TorneiosViewModel,
        authViewModel: AuthViewModel,
        repository: LeagueMatchRepository,
        usuarioLogado: Utilizador?,
        onTerminarSessao: () -> Unit
    ) {
        val coroutineScope = rememberCoroutineScope() // Cria escopo local para lançar coroutines em cliques na UI

        var currentSpectatorRoute by remember { // Memoriza estado para evitar perda durante a recomposição
            mutableStateOf<SpectatorRoute>(SpectatorRoute.Explorar) // Declara estado mutável local do Compose
        }

        var torneioSelecionado by remember { // Memoriza estado para evitar perda durante a recomposição
            mutableStateOf<Torneio?>(null) // Declara estado mutável local do Compose
        }

        val dadosTorneios by torneiosViewModel.todosTorneiosState.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)

        LaunchedEffect(Unit) { // Efeito colateral Compose: executa código assíncrono ao recompor
            torneiosViewModel.carregarTodosTorneios()
        }

        when (currentSpectatorRoute) { // Escolha múltipla condicional (semelhante a switch-case)

            SpectatorRoute.Explorar -> {
                val dadosJogosAoVivo by torneiosViewModel.jogosAoVivoState.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)

                LaunchedEffect(Unit) { // Efeito colateral Compose: executa código assíncrono ao recompor
                    torneiosViewModel.carregarJogosAoVivo()
                }

                RemoteContent(dadosTorneios) { list: List<Torneio> ->
                    val liveMatches = dadosJogosAoVivo?.getOrNull() ?: emptyList() // Declara constante local (leitura única)

                    ExplorarScreen(
                        liveMatches = liveMatches,
                        trendingTournaments = list,
                        onTorneioClick = { torneio ->
                            torneioSelecionado = torneio
                            currentSpectatorRoute = SpectatorRoute.TorneioDetalhe
                        },
                        onJogoClick = { jogo ->
                            currentSpectatorRoute = SpectatorRoute.JogoEmDireto(
                                jogo = jogo,
                                voltarPara = SpectatorRoute.Explorar
                            )
                        },
                    )
                }
            }

            SpectatorRoute.TorneioDetalhe -> {
                val torneio = torneioSelecionado // Declara constante local (leitura única)

                if (torneio == null) { // Estrutura de decisão condicional principal
                    currentSpectatorRoute = SpectatorRoute.Explorar
                } else { // Fluxo condicional alternativo caso o 'if' seja falso
                    val marcadoresResult by produceState<Result<List<MelhorMarcadorItem>>?>(null, torneio.id) { // Declara constante local (leitura única)
                        value = runCatching {
                            repository.obterMelhoresMarcadores(torneio.id) // Efetua chamada remota ou local ao repositório de dados
                        }
                    }

                    val jogosResult by produceState<Result<List<JogoResumoItem>>?>(null, torneio.id) { // Declara constante local (leitura única)
                        value = runCatching {
                            repository.obterJogosDoTorneio(torneio.id) // Efetua chamada remota ou local ao repositório de dados
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
                val torneio = torneioSelecionado // Declara constante local (leitura única)

                if (torneio == null) { // Estrutura de decisão condicional principal
                    currentSpectatorRoute = SpectatorRoute.Explorar
                } else { // Fluxo condicional alternativo caso o 'if' seja falso
                    val classificacaoResult by produceState<Result<List<Classificacao>>?>(null, torneio.id) { // Declara constante local (leitura única)
                        value = runCatching {
                            repository.obterClassificacao(torneio.id) // Efetua chamada remota ou local ao repositório de dados
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
                val torneio = torneioSelecionado // Declara constante local (leitura única)

                if (torneio == null) { // Estrutura de decisão condicional principal
                    currentSpectatorRoute = SpectatorRoute.Explorar
                } else { // Fluxo condicional alternativo caso o 'if' seja falso
                    val jogosResult by produceState<Result<List<Jogo>>?>(null, torneio.id) { // Declara constante local (leitura única)
                        value = runCatching {
                            repository.obterDetalheTorneio(torneio.id)?.jogos ?: emptyList() // Efetua chamada remota ou local ao repositório de dados
                        }
                    }

                    val jogos = jogosResult?.getOrNull() ?: emptyList() // Declara constante local (leitura única)

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
                            currentSpectatorRoute = SpectatorRoute.JogoEmDireto(
                                jogo = jogo,
                                voltarPara = SpectatorRoute.Jogos
                            )
                        }
                    )
                }
            }

            SpectatorRoute.Equipas -> {
                val torneio = torneioSelecionado // Declara constante local (leitura única)

                if (torneio == null) { // Estrutura de decisão condicional principal
                    currentSpectatorRoute = SpectatorRoute.Explorar
                } else { // Fluxo condicional alternativo caso o 'if' seja falso
                    val equipasResult by produceState<Result<List<Equipa>>?>(null, torneio.id) { // Declara constante local (leitura única)
                        value = runCatching {
                            repository.listarEquipasTorneio(torneio.id) // Efetua chamada remota ou local ao repositório de dados
                        }
                    }

                    val equipas = equipasResult?.getOrNull() ?: emptyList() // Declara constante local (leitura única)

                    EquipasScreen(
                        torneio = torneio,
                        equipas = equipas,
                        onBackClick = {
                            currentSpectatorRoute = SpectatorRoute.TorneioDetalhe
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
                        onEquipasClick = {},
                        onPerfilClick = {
                            currentSpectatorRoute = SpectatorRoute.Perfil
                        },
                        onEquipaClick = { equipa ->
                            currentSpectatorRoute = SpectatorRoute.EquipaDetalhe(equipa)
                        }
                    )
                }
            }

            is SpectatorRoute.EquipaDetalhe -> {
                val route = currentSpectatorRoute as SpectatorRoute.EquipaDetalhe // Declara constante local (leitura única)
                val torneio = torneioSelecionado // Declara constante local (leitura única)

                if (torneio == null) { // Estrutura de decisão condicional principal
                    currentSpectatorRoute = SpectatorRoute.Explorar
                } else { // Fluxo condicional alternativo caso o 'if' seja falso
                    val jogosResult by produceState<Result<List<Jogo>>?>(null, torneio.id) { // Declara constante local (leitura única)
                        value = runCatching {
                            repository.obterDetalheTorneio(torneio.id)?.jogos ?: emptyList() // Efetua chamada remota ou local ao repositório de dados
                        }
                    }

                    val jogos = jogosResult?.getOrNull() ?: emptyList() // Declara constante local (leitura única)

                    EquipaDetalheScreen(
                        torneio = torneio,
                        equipa = route.equipa,
                        jogos = jogos,
                        onBackClick = {
                            currentSpectatorRoute = SpectatorRoute.Equipas
                        },
                        onCalendarioClick = {
                            currentSpectatorRoute = SpectatorRoute.CalendarioEquipa(route.equipa)
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

            is SpectatorRoute.CalendarioEquipa -> {
                val route = currentSpectatorRoute as SpectatorRoute.CalendarioEquipa // Declara constante local (leitura única)
                val torneio = torneioSelecionado // Declara constante local (leitura única)

                if (torneio == null) { // Estrutura de decisão condicional principal
                    currentSpectatorRoute = SpectatorRoute.Explorar
                } else { // Fluxo condicional alternativo caso o 'if' seja falso
                    val jogosResult by produceState<Result<List<Jogo>>?>(null, torneio.id) { // Declara constante local (leitura única)
                        value = runCatching {
                            repository.obterDetalheTorneio(torneio.id)?.jogos ?: emptyList() // Efetua chamada remota ou local ao repositório de dados
                        }
                    }

                    val jogos = jogosResult?.getOrNull() ?: emptyList() // Declara constante local (leitura única)

                    CalendarioScreen(

                        torneio = torneio,
                        jogos = jogos.filter { jogo ->
                            jogo.casa == route.equipa.nome || jogo.fora == route.equipa.nome
                        },
                        onJogoClick = { jogo ->
                            currentSpectatorRoute = SpectatorRoute.JogoEmDireto(
                                jogo = jogo,
                                voltarPara = SpectatorRoute.CalendarioEquipa(route.equipa)
                            )
                        },
                        onBackClick = {
                            currentSpectatorRoute = SpectatorRoute.EquipaDetalhe(route.equipa)
                        },
                        onNavigateExplorar = {
                            currentSpectatorRoute = SpectatorRoute.Explorar
                        },
                        onNavigateClassificacao = {
                            currentSpectatorRoute = SpectatorRoute.Classificacao
                        },
                        onNavigateJogos = {
                            currentSpectatorRoute = SpectatorRoute.Jogos
                        },
                        onNavigateEquipas = {
                            currentSpectatorRoute = SpectatorRoute.Equipas
                        },
                        onNavigatePerfil = {
                            currentSpectatorRoute = SpectatorRoute.Perfil
                        }
                    )
                }
            }

            SpectatorRoute.InboxNotificacoes -> {
                val utilizador = usuarioLogado // Declara constante local (leitura única)
                if (utilizador == null) { // Estrutura de decisão condicional principal
                    currentSpectatorRoute = SpectatorRoute.Perfil
                } else { // Fluxo condicional alternativo caso o 'if' seja falso
                    var notificacoes by remember { mutableStateOf<List<com.leaguematch.data.remote.model.NotificacaoItem>>(emptyList()) } // Declara estado mutável local do Compose
                    LaunchedEffect(utilizador.id) { // Efeito colateral Compose: executa código assíncrono ao recompor
                        notificacoes = repository.listarNotificacoes(utilizador.id) // Efetua chamada remota ou local ao repositório de dados
                    }
                    InboxNotificacoesScreen(
                        notificacoes = notificacoes,
                        onBackClick = { currentSpectatorRoute = SpectatorRoute.Perfil },
                        onMarcarTodasLidas = {
                            coroutineScope.launch {
                                repository.marcarTodasNotificacoesLidas(utilizador.id) // Efetua chamada remota ou local ao repositório de dados
                                notificacoes = repository.listarNotificacoes(utilizador.id) // Efetua chamada remota ou local ao repositório de dados
                            }
                        },
                        onNotificacaoClick = { item ->
                            coroutineScope.launch {
                                repository.marcarNotificacaoLida(item.id) // Efetua chamada remota ou local ao repositório de dados
                                notificacoes = repository.listarNotificacoes(utilizador.id) // Efetua chamada remota ou local ao repositório de dados
                            }
                        }
                    )
                }
            }

            SpectatorRoute.Notificacoes -> {
                val utilizador = usuarioLogado // Declara constante local (leitura única)

                if (utilizador == null) { // Estrutura de decisão condicional principal
                    currentSpectatorRoute = SpectatorRoute.Perfil
                } else { // Fluxo condicional alternativo caso o 'if' seja falso
                    val configuracaoResult by produceState<Result<ConfiguracaoNotificacoes>?>(null, utilizador.id) { // Declara constante local (leitura única)
                        value = runCatching {
                            repository.obterConfiguracaoNotificacoes(utilizador.id) // Efetua chamada remota ou local ao repositório de dados
                        }
                    }

                    RemoteContent(configuracaoResult) { configuracao ->
                        NotificacoesScreen(
                            configuracao = configuracao,
                            onGuardarConfiguracao = { novaConfiguracao ->
                                coroutineScope.launch {
                                    repository.atualizarConfiguracaoNotificacoes(novaConfiguracao) // Efetua chamada remota ou local ao repositório de dados
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
                            },
                            onAbrirInbox = {
                                currentSpectatorRoute = SpectatorRoute.InboxNotificacoes
                            }
                        )
                    }
                }
            }

            is SpectatorRoute.JogoEmDireto -> {
                val route = currentSpectatorRoute as SpectatorRoute.JogoEmDireto // Declara constante local (leitura única)
                val estatisticasResult by torneiosViewModel.estatisticasJogoState.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)
                val eventosResult by torneiosViewModel.eventosJogoState.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)

                LaunchedEffect(route.jogo.id) { // Efeito colateral Compose: executa código assíncrono ao recompor
                    torneiosViewModel.carregarEstatisticasJogo(route.jogo.id)
                    torneiosViewModel.carregarEventosJogo(route.jogo.id)
                }

                val estatisticas = estatisticasResult?.getOrNull() ?: emptyList() // Declara constante local (leitura única)
                val eventos = eventosResult?.getOrNull() ?: emptyList() // Declara constante local (leitura única)

                val modalidade = (dadosTorneios?.getOrNull() ?: emptyList()) // Declara constante local (leitura única)
                    .firstOrNull { it.id == route.jogo.torneioId }?.modalidade
                    ?: torneioSelecionado?.modalidade
                    ?: "Futebol"

                JogoEmDiretoScreen(
                    jogo = route.jogo,
                    estatisticas = estatisticas,
                    eventos = eventos,
                    modalidade = modalidade,
                    onBackClick = {
                        currentSpectatorRoute = route.voltarPara
                    },
                    onVerEstatisticasClick = {
                        currentSpectatorRoute = SpectatorRoute.EstatisticasJogo(route.jogo)
                    }
                )
            }

            is SpectatorRoute.EstatisticasJogo -> {
                val route = currentSpectatorRoute as SpectatorRoute.EstatisticasJogo // Declara constante local (leitura única)
                val estatisticasResult by torneiosViewModel.estatisticasJogoState.collectAsState() // Subscreve ao fluxo de estado reativo (StateFlow)

                LaunchedEffect(route.jogo.id) { // Efeito colateral Compose: executa código assíncrono ao recompor
                    torneiosViewModel.carregarEstatisticasJogo(route.jogo.id)
                }

                val estatisticas = estatisticasResult?.getOrNull() ?: emptyList() // Declara constante local (leitura única)

                val modalidade = (dadosTorneios?.getOrNull() ?: emptyList()) // Declara constante local (leitura única)
                    .firstOrNull { it.id == route.jogo.torneioId }?.modalidade
                    ?: torneioSelecionado?.modalidade
                    ?: "Futebol"

                EstatisticasJogoScreen(
                    jogo = route.jogo,
                    estatisticas = estatisticas,
                    modalidade = modalidade,
                    onBackClick = {
                        currentSpectatorRoute = SpectatorRoute.JogoEmDireto(
                            jogo = route.jogo,
                            voltarPara = SpectatorRoute.Jogos
                        )
                    }
                )
            }

            SpectatorRoute.Perfil -> {
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

            else -> { // Fluxo condicional alternativo caso o 'if' seja falso
                LaunchedEffect(Unit) { // Efeito colateral Compose: executa código assíncrono ao recompor
                    currentSpectatorRoute = SpectatorRoute.Explorar
                }
            }
        }
    }