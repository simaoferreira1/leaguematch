/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: OrgEditarEstatisticasJogosScreen.kt
 * Tipo: Interface (Compose View) do Organizador
 *
 * Descrição:
 * Este ficheiro define um ecrã da área do Organizador em Jetpack Compose.\n * Fornece interface e lógica visual para criar torneios, gerir equipas, registar e editar jogos e estatísticas.
 */
package com.leaguematch.ui.organizer // Define o pacote deste ficheiro de código

import android.widget.Toast // Importa dependência / biblioteca necessária
import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.clickable // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Arrangement // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Box // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Column // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.PaddingValues // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Row // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Spacer // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.fillMaxHeight // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.fillMaxSize // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.fillMaxWidth // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.height // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.padding // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.size // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.width // Importa dependência / biblioteca necessária
import androidx.compose.foundation.rememberScrollState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Groups // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Person // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Schedule // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.rounded.ArrowBack // Importa dependência / biblioteca necessária
import androidx.compose.material3.Button // Importa dependência / biblioteca necessária
import androidx.compose.material3.ButtonDefaults // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.IconButton // Importa dependência / biblioteca necessária
import androidx.compose.material3.OutlinedTextField // Importa dependência / biblioteca necessária
import androidx.compose.material3.OutlinedTextFieldDefaults // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Surface // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.LaunchedEffect // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateListOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.remember // Importa dependência / biblioteca necessária
import androidx.compose.runtime.rememberCoroutineScope // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.snapshots.SnapshotStateList // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.vector.ImageVector // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.EstatisticaJogo // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Jogo // Importa dependência / biblioteca necessária
import com.leaguematch.data.repository.LeagueMatchRepository // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Bricolage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import kotlinx.coroutines.launch // Importa dependência / biblioteca necessária
import androidx.compose.material3.DropdownMenu // Importa dependência / biblioteca necessária
import androidx.compose.material3.DropdownMenuItem // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Utilizador // Importa dependência / biblioteca necessária

@Composable
fun OrgEditarEstatisticasJogoScreen( // Declaração de função / método de lógica
    jogo: Jogo,
    modalidade: String,
    repository: LeagueMatchRepository,
    onBackClick: () -> Unit,
    onGuardarClick: () -> Unit
) {
    var tabSelecionada by remember { mutableStateOf("Eventos") } // Declara estado mutável local do Compose

    var scoreCasa by remember { mutableStateOf(jogo.resultadoCasa) } // Declara estado mutável local do Compose
    var scoreFora by remember { mutableStateOf(jogo.resultadoFora) } // Declara estado mutável local do Compose
    var estadoJogo by remember { mutableStateOf(jogo.estado) } // Declara estado mutável local do Compose
    var iniciadoEmJogo by remember { mutableStateOf(jogo.iniciado_em) } // Declara estado mutável local do Compose
    var dataText by remember { mutableStateOf(jogo.data) } // Declara estado mutável local do Compose
    var horaText by remember { mutableStateOf(jogo.hora) } // Declara estado mutável local do Compose
    var isSavingScore by remember { mutableStateOf(false) } // Declara estado mutável local do Compose

    val scope = rememberCoroutineScope() // Cria escopo local para lançar coroutines em cliques na UI
    val context = androidx.compose.ui.platform.LocalContext.current // Declara constante local (leitura única)

    Scaffold(containerColor = Color(0xFFF7F7F7)) { innerPadding ->
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF7F7F7))
        ) {
            TopBarEditarEstatisticas(onBackClick)
            JogoHeaderCard(
                jogo = jogo,
                modalidade = modalidade,
                scoreCasa = scoreCasa,
                scoreFora = scoreFora,
                estadoJogo = estadoJogo,
                onCasaChange = { scoreCasa = it },
                onForaChange = { scoreFora = it },
                dataText = dataText,
                onDataChange = { dataText = it },
                horaText = horaText,
                onHoraChange = { horaText = it },
                iniciadoEm = iniciadoEmJogo,
                onSaveScoreClick = {
                    scope.launch {
                        isSavingScore = true
                        val result = repository.atualizarJogo( // Efetua chamada remota ou local ao repositório de dados
                            id = jogo.id,
                            resultadoCasa = scoreCasa,
                            resultadoFora = scoreFora,
                            estado = estadoJogo,
                            data = dataText,
                            hora = horaText,
                            atualizarInicio = false
                        )
                        isSavingScore = false
                        if (result != null) { // Estrutura de decisão condicional principal
                            Toast.makeText(context, "Jogo guardado com sucesso!", Toast.LENGTH_SHORT).show()
                        } else { // Fluxo condicional alternativo caso o 'if' seja falso
                            Toast.makeText(context, "Erro ao guardar alterações.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onStartGameClick = {
                    scope.launch {
                        isSavingScore = true
                        val result = repository.atualizarJogo( // Efetua chamada remota ou local ao repositório de dados
                            id = jogo.id,
                            resultadoCasa = 0,
                            resultadoFora = 0,
                            estado = "A Decorrer",
                            data = dataText,
                            hora = horaText,
                            atualizarInicio = true
                        )
                        isSavingScore = false
                        if (result != null) { // Estrutura de decisão condicional principal
                            scoreCasa = 0
                            scoreFora = 0
                            estadoJogo = "A Decorrer"
                            iniciadoEmJogo = result.iniciado_em
                            Toast.makeText(context, "O jogo começou a zeros!", Toast.LENGTH_SHORT).show()
                        } else { // Fluxo condicional alternativo caso o 'if' seja falso
                            Toast.makeText(context, "Erro ao começar o jogo.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onFinishGameClick = {
                    scope.launch {
                        isSavingScore = true
                        val result = repository.atualizarJogo( // Efetua chamada remota ou local ao repositório de dados
                            id = jogo.id,
                            resultadoCasa = scoreCasa,
                            resultadoFora = scoreFora,
                            estado = "Finalizado",
                            data = dataText,
                            hora = horaText,
                            atualizarInicio = false
                        )
                        isSavingScore = false
                        if (result != null) { // Estrutura de decisão condicional principal
                            estadoJogo = "Finalizado"
                            Toast.makeText(context, "Jogo finalizado com sucesso!", Toast.LENGTH_SHORT).show()
                        } else { // Fluxo condicional alternativo caso o 'if' seja falso
                            Toast.makeText(context, "Erro ao finalizar o jogo.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                isSavingScore = isSavingScore
            )

            TabEventosEstatisticas(
                selected = tabSelecionada,
                onSelected = { tabSelecionada = it }
            )

            if (tabSelecionada == "Eventos") { // Estrutura de decisão condicional principal
                EventosJogoContent(
                    jogo = jogo,
                    modalidade = modalidade,
                    repository = repository,
                    bloqueado = estadoJogo.equals("Finalizado", ignoreCase = true),
                    onEventRegistered = { tipo, equipa ->
                        val tipoUpper = tipo.uppercase() // Declara constante local (leitura única)
                        val incremento = when (tipoUpper) { // Escolha múltipla condicional (semelhante a switch-case)
                            "GOLO", "ACE", "LANCE_LIVRE" -> 1
                            "DOIS_PONTOS" -> 2
                            "TRES_PONTOS" -> 3
                            else -> 0 // Fluxo condicional alternativo caso o 'if' seja falso
                        }
                        if (incremento > 0) { // Estrutura de decisão condicional principal
                            if (equipa == "casa") { // Estrutura de decisão condicional principal
                                scoreCasa += incremento
                            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                                scoreFora += incremento
                            }
                        }
                    }
                )
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                EstatisticasJogoContent(
                    jogo = jogo,
                    modalidade = modalidade,
                    repository = repository,
                    onGuardarClick = onGuardarClick
                )
            }
        }
    }
}

@Composable
private fun TopBarEditarEstatisticas(onBackClick: () -> Unit) { // Declaração de função / método de lógica
    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) { // Componente Compose: Desenha um botão com ícone
            Icon( // Componente Compose: Desenha um ícone vetorial
                Icons.Rounded.ArrowBack,
                contentDescription = null,
                tint = Color.Black
            )
        }

        TranslatedText(
            text = "Editar estatísticas",
            fontFamily = Geist,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
        )

        Surface(
            shape = RoundedCornerShape(50),
            color = Color(0xFFDCFCE7)
        ) {
            TranslatedText(
                text = "Em Direto",
                color = Color(0xFF16A34A),
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )
        }
    }
}

@Composable
private fun JogoHeaderCard( // Declaração de função / método de lógica
    jogo: Jogo,
    modalidade: String,
    scoreCasa: Int,
    scoreFora: Int,
    estadoJogo: String,
    onCasaChange: (Int) -> Unit,
    onForaChange: (Int) -> Unit,
    dataText: String,
    onDataChange: (String) -> Unit,
    horaText: String,
    onHoraChange: (String) -> Unit,
    onSaveScoreClick: () -> Unit,
    onStartGameClick: () -> Unit,
    onFinishGameClick: () -> Unit,
    isSavingScore: Boolean,
    iniciadoEm: String?,
) {
    val isAgendado = estadoJogo.uppercase() == "AGENDADO" || estadoJogo.uppercase() == "AGENDADO" // Declara constante local (leitura única)
    val isLive = estadoJogo.uppercase() == "EM_CURSO" || estadoJogo.uppercase() == "A DECORRER" || estadoJogo.uppercase() == "EM DIRETO" // Declara constante local (leitura única)
    val isFinalizado = estadoJogo.uppercase() == "FINALIZADO" // Declara constante local (leitura única)

    Box( // Contentor Compose: Sobrepõe os elementos filhos
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .background(
                color = Color(0xFF111111),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) { // Contentor Compose: Alinha os filhos numa coluna vertical
            // Live / Estado header
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .size(8.dp)
                        .background(
                            if (isLive) Color(0xFF22C55E) else if (isFinalizado) Color(0xFF9CA3AF) else Color(0xFFEF4444), // Estrutura de decisão condicional principal
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = when { // Escolha múltipla condicional (semelhante a switch-case)
                        isLive -> "A DECORRER"
                        isFinalizado -> "FINALIZADO"
                        else -> "AGENDADO" // Fluxo condicional alternativo caso o 'if' seja falso
                    },
                    color = Color.White.copy(alpha = 0.8f),
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }

            // Dynamic ticking clock for active match (Football / Handball / Basketball)
            if (isLive) { // Estrutura de decisão condicional principal
                val isBasquetebol = modalidade.lowercase() == "basquetebol" // Declara constante local (leitura única)

                var elapsedSeconds by remember(iniciadoEm) { // Memoriza estado para evitar perda durante a recomposição
                    mutableStateOf(0) // Declara estado mutável local do Compose
                }

                LaunchedEffect(iniciadoEm, estadoJogo) { // Efeito colateral Compose: executa código assíncrono ao recompor
                    while (true) {
                        val inicioMillis = com.leaguematch.util.parseIniciadoEmEpochMillis(iniciadoEm) // Declara constante local (leitura única)

                        elapsedSeconds = if (inicioMillis != null) { // Estrutura de decisão condicional principal
                            val agoraMillis = java.time.Instant.now().toEpochMilli() // Declara constante local (leitura única)
                            ((agoraMillis - inicioMillis) / 1000).toInt().coerceAtLeast(0)
                        } else { // Fluxo condicional alternativo caso o 'if' seja falso
                            0
                        }

                        kotlinx.coroutines.delay(1000)
                    }
                }

                val secondsToShow = // Declara constante local (leitura única)
                    if (isBasquetebol) { // Estrutura de decisão condicional principal
                        (2400 - elapsedSeconds).coerceAtLeast(0)
                    } else { // Fluxo condicional alternativo caso o 'if' seja falso
                        elapsedSeconds
                    }

                val min = secondsToShow / 60 // Declara constante local (leitura única)
                val sec = secondsToShow % 60 // Declara constante local (leitura única)
                val clockText = String.format("%02d:%02d", min, sec) // Declara constante local (leitura única)

                Spacer(modifier = Modifier.height(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color.White.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
                ) {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = clockText,
                        color = Color.White,
                        fontFamily = Geist,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 3.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                }
            }

            // Realtime Countdown (if state is AGENDADO)
            if (isAgendado) { // Estrutura de decisão condicional principal
                var countdownText by remember { mutableStateOf("") } // Declara estado mutável local do Compose
                
                LaunchedEffect(jogo.data, jogo.hora, dataText, horaText) { // Efeito colateral Compose: executa código assíncrono ao recompor
                    while (true) {
                        val now = java.util.Calendar.getInstance() // Declara constante local (leitura única)
                        val matchCal = java.util.Calendar.getInstance() // Declara constante local (leitura única)
                        
                        try { // Tenta executar bloco que pode lançar exceções
                            val activeData = dataText.ifBlank { jogo.data } // Declara constante local (leitura única)
                            val activeHora = horaText.ifBlank { jogo.hora } // Declara constante local (leitura única)
                            val dateSplit = activeData.split("/") // Declara constante local (leitura única)
                            val timeSplit = activeHora.split(":") // Declara constante local (leitura única)
                            if (dateSplit.size == 3 && timeSplit.size == 2) { // Estrutura de decisão condicional principal
                                matchCal.set(
                                    dateSplit[2].toInt(),
                                    dateSplit[1].toInt() - 1,
                                    dateSplit[0].toInt(),
                                    timeSplit[0].toInt(),
                                    timeSplit[1].toInt(),
                                    0
                                )
                                
                                val diff = matchCal.timeInMillis - now.timeInMillis // Declara constante local (leitura única)
                                if (diff > 0) { // Estrutura de decisão condicional principal
                                    val days = diff / (24 * 60 * 60 * 1000) // Declara constante local (leitura única)
                                    val hours = (diff / (60 * 60 * 1000)) % 24 // Declara constante local (leitura única)
                                    val minutes = (diff / (60 * 1000)) % 60 // Declara constante local (leitura única)
                                    val seconds = (diff / 1000) % 60 // Declara constante local (leitura única)
                                    
                                    countdownText = if (days > 0) { // Estrutura de decisão condicional principal
                                        "Começa em: ${days}d ${hours}h ${minutes}m"
                                    } else if (hours > 0) { // Estrutura de decisão condicional principal
                                        "Começa em: ${hours}h ${minutes}m ${seconds}s"
                                    } else { // Fluxo condicional alternativo caso o 'if' seja falso
                                        "Começa em: ${minutes}m ${seconds}s"
                                    }
                                } else { // Fluxo condicional alternativo caso o 'if' seja falso
                                    countdownText = "Hora do jogo atingida"
                                }
                            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                                countdownText = "Aguardando data/hora"
                            }
                        } catch (e: Exception) { // Captura e trata eventuais exceções ocorridas no bloco try
                            countdownText = ""
                        }
                        
                        kotlinx.coroutines.delay(1000)
                    }
                }
                
                if (countdownText.isNotBlank()) { // Estrutura de decisão condicional principal
                    Spacer(modifier = Modifier.height(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = countdownText,
                        color = Color(0xFFEF4444),
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            // Score and team selector area
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Team Casa
                Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                    modifier = Modifier.weight(1f), // Modificador Compose: Define tamanho, margem, padding ou clique
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = jogo.casa,
                        color = Color.White,
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                    Row(verticalAlignment = Alignment.CenterVertically) { // Contentor Compose: Alinha os filhos numa linha horizontal
                        if (isLive) { // Estrutura de decisão condicional principal
                            IconButton( // Componente Compose: Desenha um botão com ícone
                                onClick = { onCasaChange((scoreCasa - 1).coerceAtLeast(0)) }, // Callback: Define a ação executada ao clicar no componente
                                modifier = Modifier.size(28.dp).background(Color.White.copy(alpha = 0.1f), androidx.compose.foundation.shape.CircleShape) // Modificador Compose: Define tamanho, margem, padding ou clique
                            ) {
                                Text("-", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp) // Componente Compose: Desenha texto estruturado no ecrã
                            }
                            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                        }
                        
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = if (isAgendado) "0" else scoreCasa.toString(), // Estrutura de decisão condicional principal
                            color = Color.White,
                            fontFamily = Bricolage,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 32.sp
                        )
                        
                        if (isLive) { // Estrutura de decisão condicional principal
                            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                            IconButton( // Componente Compose: Desenha um botão com ícone
                                onClick = { onCasaChange(scoreCasa + 1) }, // Callback: Define a ação executada ao clicar no componente
                                modifier = Modifier.size(28.dp).background(Color.White.copy(alpha = 0.1f), androidx.compose.foundation.shape.CircleShape) // Modificador Compose: Define tamanho, margem, padding ou clique
                            ) {
                                Text("+", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp) // Componente Compose: Desenha texto estruturado no ecrã
                            }
                        }
                    }
                }

                // Separator
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "-",
                    color = Color.White.copy(alpha = 0.4f),
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(horizontal = 8.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                // Team Fora
                Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                    modifier = Modifier.weight(1f), // Modificador Compose: Define tamanho, margem, padding ou clique
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = jogo.fora,
                        color = Color.White,
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                    Row(verticalAlignment = Alignment.CenterVertically) { // Contentor Compose: Alinha os filhos numa linha horizontal
                        if (isLive) { // Estrutura de decisão condicional principal
                            IconButton( // Componente Compose: Desenha um botão com ícone
                                onClick = { onForaChange((scoreFora - 1).coerceAtLeast(0)) }, // Callback: Define a ação executada ao clicar no componente
                                modifier = Modifier.size(28.dp).background(Color.White.copy(alpha = 0.1f), androidx.compose.foundation.shape.CircleShape) // Modificador Compose: Define tamanho, margem, padding ou clique
                            ) {
                                Text("-", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp) // Componente Compose: Desenha texto estruturado no ecrã
                            }
                            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                        }
                        
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = if (isAgendado) "0" else scoreFora.toString(), // Estrutura de decisão condicional principal
                            color = Color.White,
                            fontFamily = Bricolage,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 32.sp
                        )
                        
                        if (isLive) { // Estrutura de decisão condicional principal
                            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                            IconButton( // Componente Compose: Desenha um botão com ícone
                                onClick = { onForaChange(scoreFora + 1) }, // Callback: Define a ação executada ao clicar no componente
                                modifier = Modifier.size(28.dp).background(Color.White.copy(alpha = 0.1f), androidx.compose.foundation.shape.CircleShape) // Modificador Compose: Define tamanho, margem, padding ou clique
                            ) {
                                Text("+", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp) // Componente Compose: Desenha texto estruturado no ecrã
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            // Schedule input fields (visible in all states, editable)
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField( // Campo Compose: Entrada de texto com contorno visual
                    value = dataText,
                    onValueChange = onDataChange,
                    label = { Text("Data (DD/MM/AAAA)", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp) }, // Componente Compose: Desenha texto estruturado no ecrã
                    placeholder = { Text("01/01/2026", color = Color.White.copy(alpha = 0.3f)) }, // Componente Compose: Desenha texto estruturado no ecrã
                    modifier = Modifier.weight(1f).height(50.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LMRed,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                        focusedLabelColor = LMRed,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.White.copy(alpha = 0.05f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                )

                OutlinedTextField( // Campo Compose: Entrada de texto com contorno visual
                    value = horaText,
                    onValueChange = onHoraChange,
                    label = { Text("Hora (HH:MM)", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp) }, // Componente Compose: Desenha texto estruturado no ecrã
                    placeholder = { Text("12:00", color = Color.White.copy(alpha = 0.3f)) }, // Componente Compose: Desenha texto estruturado no ecrã
                    modifier = Modifier.weight(1f).height(50.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LMRed,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                        focusedLabelColor = LMRed,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.White.copy(alpha = 0.05f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            // Action Buttons based on status
            when { // Escolha múltipla condicional (semelhante a switch-case)
                isAgendado -> {
                    Button( // Componente Compose: Desenha um botão interativo
                        onClick = onStartGameClick, // Callback: Define a ação executada ao clicar no componente
                        enabled = !isSavingScore,
                        modifier = Modifier.fillMaxWidth().height(40.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E)), // Green premium start button
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                    ) {
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = if (isSavingScore) "A começar jogo..." else "Começar Jogo (A Zeros)", // Estrutura de decisão condicional principal
                            color = Color.White,
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
                
                isLive -> {
                    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button( // Componente Compose: Desenha um botão interativo
                            onClick = onSaveScoreClick, // Callback: Define a ação executada ao clicar no componente
                            enabled = !isSavingScore,
                            modifier = Modifier.weight(1f).height(40.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LMRed),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                        ) {
                            Text( // Componente Compose: Desenha texto estruturado no ecrã
                                text = if (isSavingScore) "A guardar..." else "Guardar Placar", // Estrutura de decisão condicional principal
                                color = Color.White,
                                fontFamily = Geist,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        Button( // Componente Compose: Desenha um botão interativo
                            onClick = onFinishGameClick, // Callback: Define a ação executada ao clicar no componente
                            enabled = !isSavingScore,
                            modifier = Modifier.weight(1f).height(40.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black), // Premium black end match button
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                        ) {
                            Text( // Componente Compose: Desenha texto estruturado no ecrã
                                text = if (isSavingScore) "A finalizar..." else "Finalizar Jogo", // Estrutura de decisão condicional principal
                                color = Color.White,
                                fontFamily = Geist,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
                
                isFinalizado -> {
                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = "Jogo Terminado - Resultados Bloqueados",
                            color = Color.White.copy(alpha = 0.7f),
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TabEventosEstatisticas( // Declaração de função / método de lógica
    selected: String,
    onSelected: (String) -> Unit
) {
    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .padding(horizontal = 26.dp, vertical = 6.dp)
            .background(Color(0xFFE5E5E5), RoundedCornerShape(50))
            .padding(3.dp)
    ) {
        listOf("Eventos", "Estatísticas").forEach { tab ->
            val ativo = selected == tab // Declara constante local (leitura única)

            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .weight(1f)
                    .background(
                        if (ativo) LMRed else Color.Transparent, // Estrutura de decisão condicional principal
                        RoundedCornerShape(50)
                    )
                    .clickable { onSelected(tab) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = tab,
                    color = if (ativo) Color.White else Color(0xFF6B7280), // Estrutura de decisão condicional principal
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun EventosJogoContent( // Declaração de função / método de lógica
    jogo: Jogo,
    modalidade: String,
    repository: LeagueMatchRepository,
    bloqueado: Boolean,
    onEventRegistered: (String, String) -> Unit
) {
    var eventoSelecionado by remember { mutableStateOf<MatchEventType?>(null) } // Declara estado mutável local do Compose
    var equipaSelecionada by remember { mutableStateOf("casa") } // Declara estado mutável local do Compose
    var tipoAlvo by remember { mutableStateOf("equipa") } // Declara estado mutável local do Compose
    var jogadorSelecionado by remember { mutableStateOf<Utilizador?>(null) } // Declara estado mutável local do Compose
    var jogadorSai by remember { mutableStateOf<Utilizador?>(null) } // Declara estado mutável local do Compose
    var jogadorEntra by remember { mutableStateOf<Utilizador?>(null) } // Declara estado mutável local do Compose
    var jogadoresEquipa by remember { mutableStateOf<List<Utilizador>>(emptyList()) } // Declara estado mutável local do Compose
    var jogadoresExpanded by remember { mutableStateOf(false) } // Declara estado mutável local do Compose
    var minutoAtual by remember { mutableStateOf(0) } // Declara estado mutável local do Compose

    LaunchedEffect(jogo.iniciado_em) { // Efeito colateral Compose: executa código assíncrono ao recompor
        while (true) {
            val inicioMillis = com.leaguematch.util.parseIniciadoEmEpochMillis(jogo.iniciado_em) // Declara constante local (leitura única)

            minutoAtual = if (inicioMillis != null) { // Estrutura de decisão condicional principal
                (((java.time.Instant.now().toEpochMilli() - inicioMillis) / 1000) / 60)
                    .toInt()
                    .coerceAtLeast(0)
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                0
            }

            kotlinx.coroutines.delay(1000)
        }
    }

    var isSaving by remember { mutableStateOf(false) } // Declara estado mutável local do Compose
    val scope = rememberCoroutineScope() // Cria escopo local para lançar coroutines em cliques na UI
    val context = androidx.compose.ui.platform.LocalContext.current // Declara constante local (leitura única)

    val eventos = eventosPorModalidade(modalidade) // Declara constante local (leitura única)

    LaunchedEffect(equipaSelecionada, jogo.equipaCasaId, jogo.equipaForaId) { // Efeito colateral Compose: executa código assíncrono ao recompor
        jogadorSelecionado = null

        val equipaId = if (equipaSelecionada == "casa") { // Estrutura de decisão condicional principal
            jogo.equipaCasaId
        } else { // Fluxo condicional alternativo caso o 'if' seja falso
            jogo.equipaForaId
        }

        jogadoresEquipa = if (equipaId != null) { // Estrutura de decisão condicional principal
            repository.listarJogadoresEquipa(equipaId) // Efetua chamada remota ou local ao repositório de dados
        } else { // Fluxo condicional alternativo caso o 'if' seja falso
            emptyList()
        }
    }

    Column( // Contentor Compose: Alinha os filhos numa coluna vertical
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = "Tipo de evento",
            color = Color.Black,
            fontFamily = Geist,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        eventos.chunked(3).forEach { linha ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { // Contentor Compose: Alinha os filhos numa linha horizontal
                linha.forEach { evento ->
                    EventoButton(
                        evento = evento,
                        selected = eventoSelecionado == evento,
                        onClick = { eventoSelecionado = evento }, // Callback: Define a ação executada ao clicar no componente
                        modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                }

                repeat(3 - linha.size) {
                    Spacer(modifier = Modifier.weight(1f)) // Espaçador Compose: Cria distanciamento visual entre componentes
                }
            }
        }

        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = "Equipa",
            color = Color.Black,
            fontFamily = Geist,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { // Contentor Compose: Alinha os filhos numa linha horizontal
            TeamSelectButton(
                text = jogo.casa,
                selected = equipaSelecionada == "casa",
                onClick = { equipaSelecionada = "casa" }, // Callback: Define a ação executada ao clicar no componente
                modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
            )

            TeamSelectButton(
                text = jogo.fora,
                selected = equipaSelecionada == "fora",
                onClick = { equipaSelecionada = "fora" }, // Callback: Define a ação executada ao clicar no componente
                modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
            )
        }

        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = "Aplicar a",
            color = Color.Black,
            fontFamily = Geist,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { // Contentor Compose: Alinha os filhos numa linha horizontal
            TargetButton(
                text = "Equipa",
                icon = Icons.Default.Groups,
                selected = tipoAlvo == "equipa",
                onClick = { tipoAlvo = "equipa" }, // Callback: Define a ação executada ao clicar no componente
                modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
            )

            TargetButton(
                text = "Jogador",
                icon = Icons.Default.Person,
                selected = tipoAlvo == "jogador",
                onClick = { tipoAlvo = "jogador" }, // Callback: Define a ação executada ao clicar no componente
                modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
            )
        }

        if (tipoAlvo == "jogador") { // Estrutura de decisão condicional principal
            Box(modifier = Modifier.fillMaxWidth()) { // Contentor Compose: Sobrepõe os elementos filhos
                OutlinedTextField( // Campo Compose: Entrada de texto com contorno visual
                    value = jogadorSelecionado?.nome ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Jogador") }, // Componente Compose: Desenha texto estruturado no ecrã
                    placeholder = { Text("Selecionar jogador") }, // Componente Compose: Desenha texto estruturado no ecrã
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .fillMaxWidth()
                        .clickable { jogadoresExpanded = true },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = Color(0xFFD1D5DB),
                        disabledLabelColor = Color(0xFF6B7280),
                        disabledTextColor = Color.Black,
                        disabledContainerColor = Color.White
                    )
                )

                DropdownMenu(
                    expanded = jogadoresExpanded,
                    onDismissRequest = { jogadoresExpanded = false },
                    modifier = Modifier.fillMaxWidth() // Modificador Compose: Define tamanho, margem, padding ou clique
                ) {
                    if (jogadoresEquipa.isEmpty()) { // Estrutura de decisão condicional principal
                        DropdownMenuItem(
                            text = { Text("Sem jogadores nesta equipa") }, // Componente Compose: Desenha texto estruturado no ecrã
                            onClick = { jogadoresExpanded = false } // Callback: Define a ação executada ao clicar no componente
                        )
                    } else { // Fluxo condicional alternativo caso o 'if' seja falso
                        jogadoresEquipa.forEach { jogador ->
                            DropdownMenuItem(
                                text = { Text(jogador.nome) }, // Componente Compose: Desenha texto estruturado no ecrã
                                onClick = { // Callback: Define a ação executada ao clicar no componente
                                    jogadorSelecionado = jogador
                                    jogadoresExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        if (eventoSelecionado?.name == "SUBSTITUICAO") { // Estrutura de decisão condicional principal
            var jogadorSaiExpanded by remember { mutableStateOf(false) } // Declara estado mutável local do Compose
            var jogadorEntraExpanded by remember { mutableStateOf(false) } // Declara estado mutável local do Compose

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = "Jogador que sai",
                color = Color.Black,
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Box(modifier = Modifier.fillMaxWidth()) { // Contentor Compose: Sobrepõe os elementos filhos
                OutlinedTextField( // Campo Compose: Entrada de texto com contorno visual
                    value = jogadorSai?.nome ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Jogador que sai") }, // Componente Compose: Desenha texto estruturado no ecrã
                    placeholder = { Text("Selecionar jogador") }, // Componente Compose: Desenha texto estruturado no ecrã
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .fillMaxWidth()
                        .clickable { jogadorSaiExpanded = true },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = Color(0xFFD1D5DB),
                        disabledLabelColor = Color(0xFF6B7280),
                        disabledTextColor = Color.Black,
                        disabledContainerColor = Color.White
                    )
                )

                DropdownMenu(
                    expanded = jogadorSaiExpanded,
                    onDismissRequest = { jogadorSaiExpanded = false },
                    modifier = Modifier.fillMaxWidth() // Modificador Compose: Define tamanho, margem, padding ou clique
                ) {
                    jogadoresEquipa.forEach { jogador ->
                        DropdownMenuItem(
                            text = { Text(jogador.nome) }, // Componente Compose: Desenha texto estruturado no ecrã
                            onClick = { // Callback: Define a ação executada ao clicar no componente
                                jogadorSai = jogador
                                jogadorSaiExpanded = false
                            }
                        )
                    }
                }
            }

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = "Jogador que entra",
                color = Color.Black,
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Box(modifier = Modifier.fillMaxWidth()) { // Contentor Compose: Sobrepõe os elementos filhos
                OutlinedTextField( // Campo Compose: Entrada de texto com contorno visual
                    value = jogadorEntra?.nome ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Jogador que entra") }, // Componente Compose: Desenha texto estruturado no ecrã
                    placeholder = { Text("Selecionar jogador") }, // Componente Compose: Desenha texto estruturado no ecrã
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .fillMaxWidth()
                        .clickable { jogadorEntraExpanded = true },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = Color(0xFFD1D5DB),
                        disabledLabelColor = Color(0xFF6B7280),
                        disabledTextColor = Color.Black,
                        disabledContainerColor = Color.White
                    )
                )

                DropdownMenu(
                    expanded = jogadorEntraExpanded,
                    onDismissRequest = { jogadorEntraExpanded = false },
                    modifier = Modifier.fillMaxWidth() // Modificador Compose: Define tamanho, margem, padding ou clique
                ) {
                    jogadoresEquipa.forEach { jogador ->
                        DropdownMenuItem(
                            text = { Text(jogador.nome) }, // Componente Compose: Desenha texto estruturado no ecrã
                            onClick = { // Callback: Define a ação executada ao clicar no componente
                                jogadorEntra = jogador
                                jogadorEntraExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Button( // Componente Compose: Desenha um botão interativo
            onClick = { // Callback: Define a ação executada ao clicar no componente
                val ev = eventoSelecionado // Declara constante local (leitura única)
                if (ev == null) { // Estrutura de decisão condicional principal
                    Toast.makeText(context, "Por favor, selecione um tipo de evento.", Toast.LENGTH_SHORT).show()
                    return@Button // Componente Compose: Desenha um botão interativo
                }

                if (ev.name == "CANTO" && tipoAlvo == "jogador") { // Estrutura de decisão condicional principal
                    Toast.makeText(
                        context,
                        "O canto só pode ser aplicado à equipa.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button // Componente Compose: Desenha um botão interativo
                }

                if (tipoAlvo == "jogador" && jogadorSelecionado == null) { // Estrutura de decisão condicional principal
                    Toast.makeText(
                        context,
                        "Selecione um jogador.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button // Componente Compose: Desenha um botão interativo
                }

                scope.launch {
                    isSaving = true
                    val success = repository.registarEventoJogo( // Efetua chamada remota ou local ao repositório de dados
                        partidaId = jogo.id,
                        tipo = ev.name,
                        equipa = equipaSelecionada,
                        tempo = minutoAtual,
                        userId = if (tipoAlvo == "jogador") jogadorSelecionado?.id else null, // Estrutura de decisão condicional principal
                        jogadorSaiId = if (ev.name == "SUBSTITUICAO") jogadorSai?.id else null, // Estrutura de decisão condicional principal
                        jogadorEntraId = if (ev.name == "SUBSTITUICAO") jogadorEntra?.id else null // Estrutura de decisão condicional principal
                    )
                    isSaving = false

                    if (success) { // Estrutura de decisão condicional principal
                        Toast.makeText(context, "Evento registado com sucesso!", Toast.LENGTH_SHORT).show()
                        onEventRegistered(ev.name, equipaSelecionada)
                        jogadorSelecionado = null
                    } else { // Fluxo condicional alternativo caso o 'if' seja falso
                        Toast.makeText(context, "Erro ao registar o evento.", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            enabled = !isSaving && !bloqueado,
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LMRed)
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = if (isSaving) "A registar..." else "Registar evento", // Estrutura de decisão condicional principal
                color = Color.White,
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
private fun EventoButton( // Declaração de função / método de lógica
    evento: MatchEventType,
    selected: Boolean,
    onClick: () -> Unit, // Callback: Define a ação executada ao clicar no componente
    modifier: Modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
) {
    Surface(
        onClick = onClick, // Callback: Define a ação executada ao clicar no componente
        modifier = modifier.height(78.dp),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) LMRed else Color.White, // Estrutura de decisão condicional principal
        border = BorderStroke(
            1.dp,
            if (selected) LMRed else Color(0xFFE5E7EB) // Estrutura de decisão condicional principal
        ),
        shadowElevation = 2.dp
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier.padding(8.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                evento.icon,
                contentDescription = null,
                tint = if (selected) Color.White else LMRed, // Estrutura de decisão condicional principal
                modifier = Modifier.size(22.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )

            Spacer(modifier = Modifier.height(5.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = evento.label,
                color = if (selected) Color.White else Color.Black, // Estrutura de decisão condicional principal
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun EstatisticasJogoContent( // Declaração de função / método de lógica
    jogo: Jogo,
    modalidade: String,
    repository: LeagueMatchRepository,
    onGuardarClick: () -> Unit
) {
    val scope = rememberCoroutineScope() // Cria escopo local para lançar coroutines em cliques na UI

    var isSaving by remember { mutableStateOf(false) } // Declara estado mutável local do Compose
    var mensagem by remember { mutableStateOf<String?>(null) } // Declara estado mutável local do Compose

    val jogoFinalizado = jogo.estado.equals("Finalizado", ignoreCase = true) || // Declara constante local (leitura única)
            jogo.estado.equals("FINALIZADO", ignoreCase = true)

    val estatisticas = remember(modalidade) { // Memoriza estado para evitar perda durante a recomposição
        mutableStateListOf<EstatisticaEditavel>()
    }

    fun estatisticaManual(titulo: String): Boolean { // Declaração de função / método de lógica
        return titulo.equals("Remates", ignoreCase = true) || // Retorna o resultado da execução da função
                titulo.equals("Remates à baliza", ignoreCase = true)
    }

    LaunchedEffect(jogo.id) { // Efeito colateral Compose: executa código assíncrono ao recompor
        estatisticas.clear()

        val base = estatisticasPorModalidade(modalidade).map { // Declara constante local (leitura única)
            EstatisticaEditavel(
                titulo = it.titulo,
                casa = it.casa,
                fora = it.fora
            )
        }.toMutableList()

        val salvas = repository.obterEstatisticasJogo(jogo.id) // Efetua chamada remota ou local ao repositório de dados

        salvas.forEach { salva ->
            val index = base.indexOfFirst { // Declara constante local (leitura única)
                it.titulo.equals(salva.tipo, ignoreCase = true)
            }

            if (index >= 0 && estatisticaManual(base[index].titulo)) { // Estrutura de decisão condicional principal
                val atual = base[index] // Declara constante local (leitura única)
                base[index] = if (salva.equipa.equals("casa", ignoreCase = true)) { // Estrutura de decisão condicional principal
                    atual.copy(casa = salva.valor)
                } else { // Fluxo condicional alternativo caso o 'if' seja falso
                    atual.copy(fora = salva.valor)
                }
            }
        }

        val eventos = repository.obterEventosJogo(jogo.id) // Efetua chamada remota ou local ao repositório de dados

        fun contar(tipo: String, equipa: String): Int { // Declaração de função / método de lógica
            return eventos.count { // Retorna o resultado da execução da função
                it.tipo.equals(tipo, ignoreCase = true) &&
                        it.equipa.equals(equipa, ignoreCase = true)
            }
        }

        fun aplicarAutomatica(titulo: String, tipoEvento: String) { // Declaração de função / método de lógica
            val index = base.indexOfFirst { // Declara constante local (leitura única)
                it.titulo.equals(titulo, ignoreCase = true)
            }

            if (index >= 0) { // Estrutura de decisão condicional principal
                base[index] = base[index].copy(
                    casa = contar(tipoEvento, "casa"),
                    fora = contar(tipoEvento, "fora")
                )
            }
        }

        aplicarAutomatica("Cantos", "CANTO")
        aplicarAutomatica("Faltas", "FALTA")
        aplicarAutomatica("Cartões amarelos", "CARTAO_AMARELO")
        aplicarAutomatica("Cartões vermelhos", "CARTAO_VERMELHO")

        // Compatibilidade com eventos antigos que ficaram guardados como AMARELO/VERMELHO
        val amarelosIndex = base.indexOfFirst { it.titulo.equals("Cartões amarelos", ignoreCase = true) } // Declara constante local (leitura única)
        if (amarelosIndex >= 0) { // Estrutura de decisão condicional principal
            base[amarelosIndex] = base[amarelosIndex].copy(
                casa = base[amarelosIndex].casa + contar("AMARELO", "casa"),
                fora = base[amarelosIndex].fora + contar("AMARELO", "fora")
            )
        }

        val vermelhosIndex = base.indexOfFirst { it.titulo.equals("Cartões vermelhos", ignoreCase = true) } // Declara constante local (leitura única)
        if (vermelhosIndex >= 0) { // Estrutura de decisão condicional principal
            base[vermelhosIndex] = base[vermelhosIndex].copy(
                casa = base[vermelhosIndex].casa + contar("VERMELHO", "casa"),
                fora = base[vermelhosIndex].fora + contar("VERMELHO", "fora")
            )
        }

        estatisticas.addAll(base)
    }

    Column( // Contentor Compose: Alinha os filhos numa coluna vertical
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = "Estatísticas do jogo",
            fontFamily = Geist,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = "Remates são editáveis. Cantos, faltas e cartões vêm dos eventos registados.",
            fontFamily = Geist,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = Color(0xFF6B7280)
        )

        Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

        estatisticas.forEachIndexed { index, stat ->
            val manual = estatisticaManual(stat.titulo) // Declara constante local (leitura única)

            StatBarEditable(
                titulo = stat.titulo,
                casa = stat.casa,
                fora = stat.fora,
                bloqueado = jogoFinalizado || !manual,
                onCasaChange = { novoValor ->
                    if (manual) { // Estrutura de decisão condicional principal
                        estatisticas[index] = stat.copy(casa = novoValor.coerceAtLeast(0))
                    }
                },
                onForaChange = { novoValor ->
                    if (manual) { // Estrutura de decisão condicional principal
                        estatisticas[index] = stat.copy(fora = novoValor.coerceAtLeast(0))
                    }
                }
            )

            Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
        }

        mensagem?.let {
            Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = it,
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (it.contains("guardadas", ignoreCase = true)) Color(0xFF16A34A) else LMRed // Estrutura de decisão condicional principal
            )
        }

        Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

        Button( // Componente Compose: Desenha um botão interativo
            onClick = { // Callback: Define a ação executada ao clicar no componente
                scope.launch {
                    isSaving = true
                    mensagem = null

                    try { // Tenta executar bloco que pode lançar exceções
                        val listaParaGuardar = estatisticas // Declara constante local (leitura única)
                            .filter {
                                it.titulo.equals("Remates", ignoreCase = true) ||
                                        it.titulo.equals("Remates à baliza", ignoreCase = true)
                            }
                            .toMutableStateListCompat()
                            .toEstatisticasJogo()

                        val sucesso = repository.guardarEstatisticasJogo( // Efetua chamada remota ou local ao repositório de dados
                            partidaId = jogo.id,
                            estatisticas = listaParaGuardar
                        )

                        isSaving = false

                        if (sucesso) { // Estrutura de decisão condicional principal
                            mensagem = "Estatísticas guardadas com sucesso."
                            onGuardarClick()
                        } else { // Fluxo condicional alternativo caso o 'if' seja falso
                            mensagem = "Erro ao guardar estatísticas."
                        }
                    } catch (e: Exception) { // Captura e trata eventuais exceções ocorridas no bloco try
                        isSaving = false
                        mensagem = "Erro: ${e.message}"
                    }
                }
            },
            enabled = !isSaving && !jogoFinalizado,
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LMRed)
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = if (isSaving) "A guardar..." else "Guardar remates", // Estrutura de decisão condicional principal
                color = Color.White,
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

private fun List<EstatisticaEditavel>.toMutableStateListCompat(): SnapshotStateList<EstatisticaEditavel> { // Declaração de função / método de lógica
    return mutableStateListOf<EstatisticaEditavel>().apply { // Retorna o resultado da execução da função
        addAll(this@toMutableStateListCompat)
    }
}

private fun SnapshotStateList<EstatisticaEditavel>.toEstatisticasJogo(): List<EstatisticaJogo> { // Declaração de função / método de lógica
    return flatMap { stat -> // Retorna o resultado da execução da função
        listOf(
            EstatisticaJogo(
                tipo = stat.titulo,
                equipa = "casa",
                valor = stat.casa
            ),
            EstatisticaJogo(
                tipo = stat.titulo,
                equipa = "fora",
                valor = stat.fora
            )
        )
    }
}

@Composable
private fun StatBarEditable( // Declaração de função / método de lógica
    titulo: String,
    casa: Int,
    fora: Int,
    bloqueado: Boolean,
    onCasaChange: (Int) -> Unit,
    onForaChange: (Int) -> Unit
) {
    val total = (casa + fora).coerceAtLeast(1) // Declara constante local (leitura única)
    val casaPeso = casa.toFloat() / total // Declara constante local (leitura única)
    val foraPeso = fora.toFloat() / total // Declara constante local (leitura única)

    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
        shadowElevation = 2.dp
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier.padding(14.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = titulo,
                fontFamily = Geist,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row(verticalAlignment = Alignment.CenterVertically) { // Contentor Compose: Alinha os filhos numa linha horizontal
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = casa.toString(),
                    color = LMRed,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    modifier = Modifier.width(34.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .weight(1f)
                        .height(14.dp)
                        .background(Color(0xFFE5E7EB), RoundedCornerShape(50))
                ) {
                    if (casa > 0) { // Estrutura de decisão condicional principal
                        Box( // Contentor Compose: Sobrepõe os elementos filhos
                            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                .fillMaxHeight()
                                .weight(casaPeso)
                                .background(LMRed, RoundedCornerShape(50))
                        )
                    }

                    if (fora > 0) { // Estrutura de decisão condicional principal
                        Box( // Contentor Compose: Sobrepõe os elementos filhos
                            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                .fillMaxHeight()
                                .weight(foraPeso)
                                .background(Color.Black, RoundedCornerShape(50))
                        )
                    }
                }

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = fora.toString(),
                    color = Color.Black,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .width(34.dp)
                        .padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { // Contentor Compose: Alinha os filhos numa linha horizontal
                StatCounterBox(
                    title = "Equipa Casa",
                    value = casa,
                    bloqueado = bloqueado,
                    onMinusClick = { onCasaChange(casa - 1) },
                    onPlusClick = { onCasaChange(casa + 1) },
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                StatCounterBox(
                    title = "Equipa Fora",
                    value = fora,
                    bloqueado = bloqueado,
                    onMinusClick = { onForaChange(fora - 1) },
                    onPlusClick = { onForaChange(fora + 1) },
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }
        }
    }
}

@Composable
private fun StatCounterBox( // Declaração de função / método de lógica
    title: String,
    value: Int,
    bloqueado: Boolean,
    onMinusClick: () -> Unit,
    onPlusClick: () -> Unit,
    modifier: Modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFF7F7F7),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier.padding(10.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = title,
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = Color(0xFF6B7280)
            )

            Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (!bloqueado) { // Estrutura de decisão condicional principal
                    SmallStatButton(
                        text = "-",
                        onClick = onMinusClick // Callback: Define a ação executada ao clicar no componente
                    )
                }

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = value.toString(),
                    color = Color.Black,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(horizontal = 10.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                if (!bloqueado) { // Estrutura de decisão condicional principal
                    SmallStatButton(
                        text = "+",
                        onClick = onPlusClick // Callback: Define a ação executada ao clicar no componente
                    )
                }
            }
        }
    }
}

@Composable
private fun SmallStatButton( // Declaração de função / método de lógica
    text: String,
    onClick: () -> Unit // Callback: Define a ação executada ao clicar no componente
) {
    Surface(
        onClick = onClick, // Callback: Define a ação executada ao clicar no componente
        shape = RoundedCornerShape(8.dp),
        color = LMRed
    ) {
        Box( // Contentor Compose: Sobrepõe os elementos filhos
            modifier = Modifier.size(30.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            contentAlignment = Alignment.Center
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = text,
                color = Color.White,
                fontFamily = Geist,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun TeamSelectButton( // Declaração de função / método de lógica
    text: String,
    selected: Boolean,
    onClick: () -> Unit, // Callback: Define a ação executada ao clicar no componente
    modifier: Modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
) {
    Surface(
        onClick = onClick, // Callback: Define a ação executada ao clicar no componente
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) LMRed else Color.White, // Estrutura de decisão condicional principal
        border = BorderStroke(
            1.dp,
            if (selected) LMRed else Color(0xFFE5E7EB) // Estrutura de decisão condicional principal
        ),
        shadowElevation = 2.dp
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(horizontal = 10.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                Icons.Default.Groups,
                contentDescription = null,
                tint = if (selected) Color.White else LMRed, // Estrutura de decisão condicional principal
                modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )

            Spacer(modifier = Modifier.width(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = text,
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = if (selected) Color.White else Color.Black, // Estrutura de decisão condicional principal
                maxLines = 1
            )
        }
    }
}

@Composable
private fun TargetButton( // Declaração de função / método de lógica
    text: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit, // Callback: Define a ação executada ao clicar no componente
    modifier: Modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
) {
    Surface(
        onClick = onClick, // Callback: Define a ação executada ao clicar no componente
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) LMRed else Color.White, // Estrutura de decisão condicional principal
        border = BorderStroke(
            1.dp,
            if (selected) LMRed else Color(0xFFE5E7EB) // Estrutura de decisão condicional principal
        ),
        shadowElevation = 2.dp
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(horizontal = 10.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                icon,
                contentDescription = null,
                tint = if (selected) Color.White else LMRed, // Estrutura de decisão condicional principal
                modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )

            Spacer(modifier = Modifier.width(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = text,
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = if (selected) Color.White else Color.Black // Estrutura de decisão condicional principal
            )
        }
    }
}

private data class EstatisticaEditavel( // Declaração de classe para modelar objetos
    val titulo: String, // Declara constante local (leitura única)
    val casa: Int, // Declara constante local (leitura única)
    val fora: Int // Declara constante local (leitura única)
)