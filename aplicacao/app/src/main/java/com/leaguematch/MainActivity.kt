package com.leaguematch

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.leaguematch.data.remote.model.EstatisticasAdmin
import com.leaguematch.data.remote.model.ResumoDashboard
import com.leaguematch.data.remote.model.TipoUtilizador
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.data.repository.SupabaseLeagueMatchRepository
import com.leaguematch.ui.admin.DefinicoesScreen
import com.leaguematch.ui.admin.DetalheTorneioScreen
import com.leaguematch.ui.admin.DetalheUtilizadorScreen
import com.leaguematch.ui.admin.GestaoNotificacoesScreen
import com.leaguematch.ui.admin.GraficosScreen
import com.leaguematch.ui.admin.HomeScreen
import com.leaguematch.ui.admin.ListaTorneiosModalidadeScreen
import com.leaguematch.ui.admin.TorneiosScreen
import com.leaguematch.ui.admin.UtilizadoresScreen
import com.leaguematch.ui.auth.LoginScreen
import com.leaguematch.ui.auth.RegisterScreen
import com.leaguematch.ui.components.AdminBottomBar
import com.leaguematch.ui.components.OrganizerBottomBar
import com.leaguematch.ui.components.SpectatorBottomBar
import com.leaguematch.ui.spectator.ExplorarScreen
import com.leaguematch.ui.organizer.OrgCriarEquipaScreen
import com.leaguematch.ui.organizer.OrgCriarJogoScreen
import com.leaguematch.ui.organizer.OrgGerirEquipasScreen
import com.leaguematch.ui.organizer.OrgVerJogosScreen
import com.leaguematch.ui.organizer.OrgTorneioActionsScreen
import com.leaguematch.ui.organizer.OrgTournamentsScreen
import com.leaguematch.ui.theme.LeagueMatchTheme
import com.leaguematch.viewmodel.*
import com.leaguematch.ui.organizer.CreateTournamentScreen
import com.leaguematch.ui.organizer.CreateTournamentStep2Screen
import com.leaguematch.ui.spectator.ClassificacaoItem
import com.leaguematch.ui.spectator.ClassificacaoScreen
import com.leaguematch.data.remote.model.Classificacao
import com.leaguematch.ui.spectator.JogoResumoItem
import com.leaguematch.ui.spectator.MelhorMarcadorItem
import com.leaguematch.ui.spectator.EquipasScreen
import com.leaguematch.ui.spectator.JogosScreen
import com.leaguematch.ui.spectator.TorneioDetalheScreen
import com.leaguematch.ui.organizer.OrgEditarEstatisticasJogoScreen
import com.leaguematch.ui.spectator.NotificacoesScreen

sealed interface AdminRoute {
    data object Home : AdminRoute
    data object Utilizadores : AdminRoute
    data class DetalheUtilizador(val id: Int) : AdminRoute
    data object Torneios : AdminRoute
    data class TorneiosModalidade(val modalidade: String) : AdminRoute
    data class DetalheTorneio(val id: Int, val modalidade: String) : AdminRoute
    data object Graficos : AdminRoute
    data object Definicoes : AdminRoute
    data object Notificacoes : AdminRoute
}

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
}

sealed interface SpectatorRoute {
    data object Explorar : SpectatorRoute
    data object EscolherTorneio : SpectatorRoute
    data object TorneioDetalhe : SpectatorRoute
    data object Classificacao : SpectatorRoute
    data object Jogos : SpectatorRoute
    data object Equipas : SpectatorRoute
    data object Perfil : SpectatorRoute
    data object Notificacoes : SpectatorRoute
}

class MainActivity : ComponentActivity() {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var utilizadoresViewModel: UtilizadoresViewModel
    private lateinit var torneiosViewModel: TorneiosViewModel
    private lateinit var graficosViewModel: GraficosViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = SupabaseLeagueMatchRepository(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            anonKey = BuildConfig.SUPABASE_ANON_KEY,
        )
        val factory = ViewModelFactory(repository)

        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        utilizadoresViewModel = ViewModelProvider(this, factory)[UtilizadoresViewModel::class.java]
        torneiosViewModel = ViewModelProvider(this, factory)[TorneiosViewModel::class.java]
        graficosViewModel = ViewModelProvider(this, factory)[GraficosViewModel::class.java]

        setContent {
            LeagueMatchTheme {
                val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
                val loginError by authViewModel.loginError.collectAsState()
                val registerSuccess by authViewModel.registerSuccess.collectAsState()
                var showRegisterScreen by remember { mutableStateOf(false) }
                var currentRoute by remember { mutableStateOf<AdminRoute>(AdminRoute.Home) }
                var currentOrgRoute by remember { mutableStateOf<OrganizerRoute>(OrganizerRoute.MeusTorneios) }
                var currentOrgTorneioId by remember { mutableStateOf<Int?>(null) }
                var currentSpectatorRoute by remember { mutableStateOf<SpectatorRoute>(SpectatorRoute.Explorar) }
                var torneioSelecionado by remember { mutableStateOf<Torneio?>(null) }
                var dadosCriarTorneio by remember { mutableStateOf<List<String>?>(null) }
                fun navigate(route: AdminRoute) {
                    currentRoute = route
                }

                if (!isLoggedIn) {
                    if (showRegisterScreen) {
                        RegisterScreen(
                            erro = loginError,
                            sucesso = registerSuccess,
                            onBackClick = {
                                authViewModel.resetRegisterState()
                                showRegisterScreen = false
                            },
                            onRegisterClick = { nome, email, password, tipo ->
                                authViewModel.registar(nome, email, password, tipo)
                            },
                            onSuccessRedirect = {
                                authViewModel.resetRegisterState()
                                showRegisterScreen = false
                            }
                        )
                    } else {
                        LoginScreen(
                            erro = loginError,
                            onLoginClick = { email, password ->
                                authViewModel.autenticar(email, password)
                            },
                            onRegisterClick = {
                                authViewModel.resetRegisterState()
                                showRegisterScreen = true
                            }
                        )
                    }
                } else {
                    val usuarioLogado by authViewModel.usuarioLogado.collectAsState()

                    when (usuarioLogado?.tipo) {
                        TipoUtilizador.ORGANIZADOR -> {
                            val goMeusTorneios = { currentOrgRoute = OrganizerRoute.MeusTorneios }
                            val goPerfil = { currentOrgRoute = OrganizerRoute.Perfil }

                            val context = androidx.compose.ui.platform.LocalContext.current

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

                                            val (modalidades, totalTorneios) =
                                                dadosModalidades!!.getOrThrow()

                                            val torneios =
                                                dadosTorneios!!.getOrThrow()

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
                                            authViewModel.terminarSessao()
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
                                            onVerClassificacaoClick = {},
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
                                    val torneio = (torneiosViewModel.detalheTorneioState.value
                                        ?.getOrNull()?.torneio)
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
                                    val torneio = torneiosViewModel.detalheTorneioState.value
                                        ?.getOrNull()?.torneio
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
                                    val torneio = torneiosViewModel.detalheTorneioState.value
                                        ?.getOrNull()?.torneio
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
                                            torneio = torneio ?: com.leaguematch.data.remote.model.Torneio(
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
                            }
                        }

                        TipoUtilizador.ESPECTADOR -> {

                            when (currentSpectatorRoute) {

                                SpectatorRoute.Explorar -> {
                                    val dadosTorneios by torneiosViewModel.todosTorneiosState.collectAsState()

                                    LaunchedEffect(Unit) {
                                        torneiosViewModel.carregarTodosTorneios()
                                    }

                                    RemoteContent(dadosTorneios) { list: List<Torneio> ->
                                        ExplorarScreen(
                                            liveMatches = emptyList(),
                                            trendingTournaments = list,
                                            onTorneioClick = { torneio ->
                                                torneioSelecionado = torneio
                                                currentSpectatorRoute = SpectatorRoute.TorneioDetalhe
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
                                        val jogosResult by produceState<Result<List<com.leaguematch.data.remote.model.Jogo>>?>(null, torneio.id) {
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

                                SpectatorRoute.Perfil -> {
                                    DefinicoesScreen(
                                        utilizadorLogado = usuarioLogado,

                                        onTerminarSessaoClick = {
                                            authViewModel.terminarSessao()
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
                                    currentSpectatorRoute = SpectatorRoute.Explorar
                                }
                            }
                        }
                        else -> {
                            val goHome = { navigate(AdminRoute.Home) }
                            val goUsers = { navigate(AdminRoute.Utilizadores) }
                            val goTournaments = { navigate(AdminRoute.Torneios) }
                            val goCharts = { navigate(AdminRoute.Graficos) }
                            val goSettings = { navigate(AdminRoute.Definicoes) }

                            when (val route = currentRoute) {
                                AdminRoute.Home -> {
                                    val dashboard by homeViewModel.dashboardState.collectAsState()
                                    LaunchedEffect(Unit) {
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
                                    val utilizadores by utilizadoresViewModel.utilizadoresState.collectAsState()
                                    LaunchedEffect(Unit) {
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
                                    val utilizador by utilizadoresViewModel.detalheUtilizadorState.collectAsState()
                                    LaunchedEffect(route.id) {
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

                                            val (modalidades, totalTorneios) =
                                                dadosModalidades!!.getOrThrow()

                                            val torneios =
                                                dadosTorneios!!.getOrThrow()

                                            TorneiosScreen(
                                                modalidades = modalidades,
                                                torneios = torneios,
                                                totalTorneios = totalTorneios,
                                                onHomeClick = goHome,
                                                onUtilizadoresClick = goUsers,
                                                onGraficosClick = goCharts,
                                                onDefinicoesClick = goSettings,
                                                onTorneioClick = { id ->
                                                    val torneio =
                                                        torneios.firstOrNull { it.id == id }

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
                                    val torneios by torneiosViewModel.torneiosState.collectAsState()
                                    LaunchedEffect(route.modalidade) {
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
                                    val detalhe by torneiosViewModel.detalheTorneioState.collectAsState()
                                    LaunchedEffect(route.id) {
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
                                    val estatisticas by graficosViewModel.estatisticasState.collectAsState()
                                    LaunchedEffect(Unit) {
                                        graficosViewModel.carregarEstatisticas()
                                    }
                                    RemoteContent(estatisticas) {
                                        GraficosScreen(
                                            estatisticas = it,
                                            onHomeClick = goHome,
                                            onUtilizadoresClick = goUsers,
                                            onTorneiosClick = goTournaments,
                                            onDefinicoesClick = goSettings
                                        )
                                    }
                                }

                                AdminRoute.Definicoes -> {
                                    val usuarioLogado by authViewModel.usuarioLogado.collectAsState()
                                    DefinicoesScreen(
                                        utilizadorLogado = usuarioLogado,
                                        onTerminarSessaoClick = {
                                            authViewModel.terminarSessao()
                                            currentRoute = AdminRoute.Home
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
                                    GestaoNotificacoesScreen(
                                        onHomeClick = goHome,
                                        onUtilizadoresClick = goUsers,
                                        onTorneiosClick = goTournaments,
                                        onGraficosClick = goCharts,
                                        onDefinicoesClick = goSettings
                                    )
                                }
                            }
                        }
                    }
                }
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