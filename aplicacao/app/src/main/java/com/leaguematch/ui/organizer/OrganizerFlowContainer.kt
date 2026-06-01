package com.leaguematch.ui.organizer

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
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.data.repository.LeagueMatchRepository
import com.leaguematch.ui.admin.DefinicoesScreen
import com.leaguematch.ui.components.OrganizerBottomBar
import com.leaguematch.ui.components.RemoteContent
import com.leaguematch.ui.components.LoadingScreen
import com.leaguematch.ui.components.ErrorScreen
import com.leaguematch.ui.spectator.ClassificacaoScreen
import com.leaguematch.ui.spectator.ClassificacaoItem
import com.leaguematch.viewmodel.AuthViewModel
import com.leaguematch.viewmodel.TorneiosViewModel

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
    data class VerClassificacao(val torneioId: Int) : OrganizerRoute
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
    var currentOrgRoute by remember { mutableStateOf<OrganizerRoute>(OrganizerRoute.MeusTorneios) }
    var currentOrgTorneioId by remember { mutableStateOf<Int?>(null) }
    var dadosCriarTorneio by remember { mutableStateOf<List<String>?>(null) }

    val goMeusTorneios = { currentOrgRoute = OrganizerRoute.MeusTorneios }
    val goPerfil = { currentOrgRoute = OrganizerRoute.Perfil }

    when (currentOrgRoute) {
        OrganizerRoute.MeusTorneios -> {
            val dadosModalidades by torneiosViewModel.modalidadesState.collectAsState()
            val dadosTorneios by torneiosViewModel.todosTorneiosState.collectAsState()

            LaunchedEffect(Unit) {
                torneiosViewModel.carregarTorneios()
                torneiosViewModel.carregarTodosTorneios()
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
                        onPerfilClick = goPerfil
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
                    onCreateClick = { publico, inscricoesAutomaticas, jogosIdaVolta, pontosVitoria ->
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
                        onPerfilClick = goPerfil
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
                    bottomBar = {
                        OrganizerBottomBar(
                            selectedItem = "torneios",
                            onTorneiosClick = goMeusTorneios,
                            onPerfilClick = goPerfil
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

            val torneio = (torneiosViewModel.detalheTorneioState.value?.getOrNull()?.torneio)
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

            val torneio = torneiosViewModel.detalheTorneioState.value?.getOrNull()?.torneio
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
                    }
                )
            } else {
                currentOrgRoute = OrganizerRoute.DetalheTorneio(route.torneioId)
            }
        }

        is OrganizerRoute.CriarEquipa -> {
            val route = currentOrgRoute as OrganizerRoute.CriarEquipa
            val isLoading by torneiosViewModel.criarJogoLoading.collectAsState()
            val torneio = torneiosViewModel.detalheTorneioState.value?.getOrNull()?.torneio

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

            val torneio = detalhe?.getOrNull()?.torneio
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

            val torneio = detalhe?.getOrNull()?.torneio
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
    }
}


