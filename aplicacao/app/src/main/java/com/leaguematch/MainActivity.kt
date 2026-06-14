package com.leaguematch

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import com.leaguematch.data.remote.model.TipoUtilizador
import com.leaguematch.data.repository.SupabaseLeagueMatchRepository
import com.leaguematch.data.repository.TranslationRepository
import com.leaguematch.translations.Language
import com.leaguematch.ui.admin.AdminFlowContainer
import com.leaguematch.ui.auth.IntroSliderScreen
import com.leaguematch.ui.auth.LoginScreen
import com.leaguematch.ui.auth.RegisterScreen
import com.leaguematch.ui.components.LoadingScreen
import com.leaguematch.ui.components.LocalLanguage
import com.leaguematch.ui.components.LocalTranslationRepository
import com.leaguematch.ui.organizer.OrganizerFlowContainer
import com.leaguematch.ui.participant.ParticipantFlowContainer
import com.leaguematch.ui.spectator.SpectatorFlowContainer
import com.leaguematch.ui.theme.LeagueMatchTheme
import com.leaguematch.viewmodel.AuthViewModel
import com.leaguematch.viewmodel.GraficosViewModel
import com.leaguematch.viewmodel.HomeViewModel
import com.leaguematch.viewmodel.ParticipantViewModel
import com.leaguematch.viewmodel.TorneiosViewModel
import com.leaguematch.viewmodel.UtilizadoresViewModel
import com.leaguematch.viewmodel.ViewModelFactory

/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * A MainActivity é o ponto de entrada principal da nossa aplicação Android.
 * Ela é responsável por:
 * 1. Inicializar os repositórios (Supabase, SharedPreferences/SyncQueue).
 * 2. Instanciar a fábrica de ViewModels (ViewModelFactory) e obter as instâncias partilhadas.
 * 3. Gerir as preferências locais de idioma (PT/EN) e a introdução da app (IntroSlider).
 * 4. Fazer o roteamento principal da interface dependendo do estado de autenticação (Login/Registo/App).
 * 5. Direcionar o utilizador autenticado para o fluxo específico de acordo com o seu perfil (Admin, Organizador, Participante, Espectador).
 */
class MainActivity : ComponentActivity() { // Define a nossa Activity principal que herda de ComponentActivity do Jetpack Compose

    // Declara o ViewModel responsável pela autenticação e controlo de sessão do utilizador
    private lateinit var authViewModel: AuthViewModel
    // Declara o ViewModel da Home (para o painel de administração e alertas)
    private lateinit var homeViewModel: HomeViewModel
    // Declara o ViewModel para gerir a lista de utilizadores (exclusivo do Admin)
    private lateinit var utilizadoresViewModel: UtilizadoresViewModel
    // Declara o ViewModel principal para gerir torneios, equipas e jogos
    private lateinit var torneiosViewModel: TorneiosViewModel
    // Declara o ViewModel que gere os dados estatísticos para renderização de gráficos
    private lateinit var graficosViewModel: GraficosViewModel
    // Declara o ViewModel associado à lógica e dados do perfil de Participante
    private lateinit var participantViewModel: ParticipantViewModel

    override fun onCreate(savedInstanceState: Bundle?) { // Ponto de entrada do ciclo de vida da Activity
        super.onCreate(savedInstanceState) // Chama o método onCreate da superclasse
        enableEdgeToEdge() // Habilita o design imersivo de ecrã inteiro (edge-to-edge)

        // Inicializa a fila de sincronização offline que usa SharedPreferences para guardar atualizações falhadas
        val syncQueue = com.leaguematch.data.sync.SyncQueueStore(applicationContext)
        
        // Inicializa o repositório principal do Supabase, passando as URLs e as chaves de acesso
        val repository = SupabaseLeagueMatchRepository(
            supabaseUrl = BuildConfig.SUPABASE_URL, // Obtém a URL do Supabase a partir do build config do gradle
            anonKey = BuildConfig.SUPABASE_ANON_KEY, // Obtém a chave anónima de acesso público à API
            syncQueue = syncQueue // Injeta a fila de sync para lidar com o comportamento offline
        )

        // Inicializa o repositório encarregue de aceder às chaves de tradução/localização remota
        val translationRepository = TranslationRepository(
            supabaseUrl = BuildConfig.SUPABASE_URL, // Passa a URL do servidor
            anonKey = BuildConfig.SUPABASE_ANON_KEY // Passa a chave de acesso do Supabase
        )

        // Cria a factory para inicializar ViewModels que necessitam do repositório como dependência
        val factory = ViewModelFactory(repository)

        // Obtém a instância do AuthViewModel usando a factory criada
        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
        // Obtém a instância do HomeViewModel usando a factory
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        // Obtém a instância do UtilizadoresViewModel usando a factory
        utilizadoresViewModel = ViewModelProvider(this, factory)[UtilizadoresViewModel::class.java]
        // Obtém a instância do TorneiosViewModel usando a factory
        torneiosViewModel = ViewModelProvider(this, factory)[TorneiosViewModel::class.java]
        // Obtém a instância do GraficosViewModel usando a factory
        graficosViewModel = ViewModelProvider(this, factory)[GraficosViewModel::class.java]
        // Obtém a instância do ParticipantViewModel usando a factory
        participantViewModel = ViewModelProvider(this, factory)[ParticipantViewModel::class.java]

        setContent { // Define o layout da nossa activity utilizando Jetpack Compose
            LeagueMatchTheme { // Aplica o tema visual global definido para o nosso projeto
                // Recolhe o estado de login como uma variável de estado observável pelo Compose
                val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
                // Recolhe eventuais erros de autenticação (ex: password errada)
                val loginError by authViewModel.loginError.collectAsState()
                // Recolhe o sucesso no registo de uma nova conta de utilizador
                val registerSuccess by authViewModel.registerSuccess.collectAsState()
                // Define uma variável de estado local para controlar a transição para o ecrã de registo
                var showRegisterScreen by remember { mutableStateOf(false) }

                // Obtém o contexto Compose local do ecrã
                val context = LocalContext.current
                // Cria e armazena localmente o objeto SharedPreferences para ler/gravar configurações da app
                val prefs = remember {
                    context.getSharedPreferences("lm_prefs", Context.MODE_PRIVATE)
                }

                // Lê a preferência de idioma guardada (Português por padrão) e mantém-na como estado Compose
                var language by remember {
                    mutableStateOf(
                        Language.valueOf(
                            prefs.getString("language", Language.PT.name) ?: Language.PT.name
                        )
                    )
                }

                // Função utilitária local para alterar o idioma ativo e gravar a alteração
                fun changeLanguage(newLanguage: Language) {
                    language = newLanguage // Altera o estado na UI Compose (despoleta recomposição)
                    prefs.edit().putString("language", newLanguage.name).apply() // Grava a opção no dispositivo
                }

                // Verifica se o utilizador já visualizou a introdução (Slider) da aplicação
                var introVisto by remember {
                    mutableStateOf(prefs.getBoolean("intro_seen", false))
                }

                // Providencia as instâncias de idioma e tradução para os componentes filhos
                CompositionLocalProvider(
                    LocalLanguage provides language, // Injeta o idioma ativo
                    LocalTranslationRepository provides translationRepository // Injeta o repositório de textos
                ) {
                    // Condição 1: Utilizador não está logado e nunca viu o slider de introdução
                    if (!isLoggedIn && !introVisto) {
                        IntroSliderScreen( // Mostra o ecrã com a introdução em carrossel
                            onConcluir = { // Ação executada ao terminar a introdução
                                prefs.edit().putBoolean("intro_seen", true).apply() // Regista que já viu a intro
                                introVisto = true // Atualiza o estado para recompor o ecrã e avançar
                            }
                        )
                    } 
                    // Condição 2: Utilizador não autenticado (deve ver o login ou o registo)
                    else if (!isLoggedIn) {
                        if (showRegisterScreen) { // Se a flag for verdadeira, mostra o ecrã de Registo
                            RegisterScreen(
                                erro = loginError, // Passa erros de registo
                                sucesso = registerSuccess, // Passa confirmação de registo efetuado
                                onBackClick = { // Botão voltar
                                    authViewModel.resetRegisterState() // Limpa os erros de estado de registo
                                    showRegisterScreen = false // Volta para o login
                                },
                                onRegisterClick = { nome, email, password, tipo -> // Callback para criar utilizador
                                    authViewModel.registar(nome, email, password, tipo) // Dispara a coroutine de registo
                                },
                                onSuccessRedirect = { // Ação executada após registo com sucesso
                                    authViewModel.resetRegisterState() // Limpa o estado
                                    showRegisterScreen = false // Redireciona para o ecrã de Login
                                }
                            )
                        } else { // Caso contrário, mostra o ecrã de Login
                            LoginScreen(
                                erro = loginError, // Passa erros do ViewModel
                                language = language, // Passa idioma ativo
                                onLanguageChange = { newLanguage -> // Callback para alteração de idioma no ecrã
                                    changeLanguage(newLanguage) // Altera e guarda o idioma
                                },
                                translationRepository = translationRepository, // Repositório de traduções
                                onLoginClick = { email, password -> // Callback para tentar autenticar
                                    authViewModel.autenticar(email, password) // Efetua login
                                },
                                onRegisterClick = { // Botão para criar nova conta
                                    authViewModel.resetRegisterState() // Limpa estados anteriores
                                    showRegisterScreen = true // Mostra ecrã de registo
                                }
                            )
                        }
                    } 
                    // Condição 3: Utilizador autenticado (iniciou sessão)
                    else {
                        // Observa reativamente os dados detalhados do utilizador autenticado
                        val usuarioLogado by authViewModel.usuarioLogado.collectAsState()

                        if (usuarioLogado == null) { // Se os dados ainda não carregaram do servidor
                            LoadingScreen() // Exibe o ecrã de carregamento (Spinner)
                        } else {
                            // Direciona o fluxo da app dependendo do tipo do Utilizador (Perfil/Cargo)
                            when (usuarioLogado!!.tipo) {
                                TipoUtilizador.ORGANIZADOR -> { // Fluxo de Organizador de Torneios
                                    OrganizerFlowContainer(
                                        torneiosViewModel = torneiosViewModel, // Passa o viewModel de torneios
                                        authViewModel = authViewModel, // Passa o viewModel de autenticação
                                        repository = repository, // Passa o repositório
                                        usuarioLogado = usuarioLogado, // Passa os dados do utilizador
                                        onTerminarSessao = { // Callback para terminar sessão
                                            authViewModel.terminarSessao() // Limpa a sessão
                                        }
                                    )
                                }

                                TipoUtilizador.ESPECTADOR -> { // Fluxo de Espectador (Apenas visualização)
                                    SpectatorFlowContainer(
                                        torneiosViewModel = torneiosViewModel, // Passa Viewmodel
                                        authViewModel = authViewModel, // Passa Viewmodel
                                        repository = repository, // Passa repositório
                                        usuarioLogado = usuarioLogado, // Passa dados
                                        onTerminarSessao = { // Callback de log out
                                            authViewModel.terminarSessao() // Faz log out
                                        }
                                    )
                                }

                                TipoUtilizador.PARTICIPANTE -> { // Fluxo de Jogador / Participante
                                    ParticipantFlowContainer(
                                        torneiosViewModel = torneiosViewModel, // Passa Viewmodel de torneios
                                        authViewModel = authViewModel, // Passa Viewmodel de auth
                                        participantViewModel = participantViewModel, // Passa Viewmodel de participante
                                        repository = repository, // Passa repositório
                                        usuarioLogado = usuarioLogado, // Passa dados
                                        onTerminarSessao = { // Callback de log out
                                            authViewModel.terminarSessao() // Faz log out
                                        }
                                    )
                                }

                                else -> { // Fluxo por omissão / Administrador do Sistema
                                    AdminFlowContainer(
                                        homeViewModel = homeViewModel, // Passa Viewmodel da Dashboard
                                        utilizadoresViewModel = utilizadoresViewModel, // Passa Viewmodel de utilizadores
                                        torneiosViewModel = torneiosViewModel, // Passa Viewmodel de torneios
                                        graficosViewModel = graficosViewModel, // Passa Viewmodel de gráficos
                                        authViewModel = authViewModel, // Passa Viewmodel de auth
                                        repository = repository, // Passa repositório
                                        usuarioLogado = usuarioLogado, // Passa dados do utilizador
                                        onTerminarSessao = { // Callback de log out
                                            authViewModel.terminarSessao() // Faz log out
                                        }
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