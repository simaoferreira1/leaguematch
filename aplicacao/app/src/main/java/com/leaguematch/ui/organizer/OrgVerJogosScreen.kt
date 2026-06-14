/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: OrgVerJogosScreen.kt
 * Tipo: Interface (Compose View) do Organizador
 *
 * Descrição:
 * Este ficheiro define um ecrã da área do Organizador em Jetpack Compose.\n * Fornece interface e lógica visual para criar torneios, gerir equipas, registar e editar jogos e estatísticas.
 */
package com.leaguematch.ui.organizer // Define o pacote deste ficheiro de código

import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Arrangement // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Box // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Column // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Row // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Spacer // Importa dependência / biblioteca necessária
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
import androidx.compose.material.icons.filled.Add // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.DeleteOutline // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Edit // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsScore // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.rounded.ArrowBack // Importa dependência / biblioteca necessária
import androidx.compose.material3.AlertDialog // Importa dependência / biblioteca necessária
import androidx.compose.material3.Button // Importa dependência / biblioteca necessária
import androidx.compose.material3.ButtonDefaults // Importa dependência / biblioteca necessária
import androidx.compose.material3.CircularProgressIndicator // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.IconButton // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Surface // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.material3.TextButton // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.LaunchedEffect // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.remember // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Jogo // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Torneio // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Bricolage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMBorder // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray100 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray300 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray400 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray50 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray600 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária

@Composable
fun OrgVerJogosScreen( // Declaração de função / método de lógica
    torneio: Torneio,
    jogos: List<Jogo>,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onCriarJogoClick: () -> Unit,
    onEditarJogo: (Jogo) -> Unit,
    onRemoverJogo: (Jogo) -> Unit,
    onVerEstatisticas: (Jogo) -> Unit
) {
    var jogoParaRemover by remember { mutableStateOf<Jogo?>(null) } // Declara estado mutável local do Compose

    if (jogoParaRemover != null) { // Estrutura de decisão condicional principal
        AlertDialog(
            onDismissRequest = { jogoParaRemover = null },
            title = {
                Text("Remover jogo?", fontFamily = Geist, fontWeight = FontWeight.Bold, fontSize = 16.sp) // Componente Compose: Desenha texto estruturado no ecrã
            },
            text = {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    "Tens a certeza que queres remover a partida entre \"${jogoParaRemover!!.casa}\" e \"${jogoParaRemover!!.fora}\"?",
                    fontFamily = Geist, fontSize = 13.sp, color = LMGray600
                )
            },
            confirmButton = {
                TextButton(onClick = { // Callback: Define a ação executada ao clicar no componente
                    onRemoverJogo(jogoParaRemover!!)
                    jogoParaRemover = null
                }) {
                    Text("Remover", color = LMRed, fontFamily = Geist, fontWeight = FontWeight.Bold) // Componente Compose: Desenha texto estruturado no ecrã
                }
            },
            dismissButton = {
                TextButton(onClick = { jogoParaRemover = null }) { // Callback: Define a ação executada ao clicar no componente
                    Text("Cancelar", fontFamily = Geist, color = LMGray500) // Componente Compose: Desenha texto estruturado no ecrã
                }
            },
            shape = RoundedCornerShape(18.dp)
        )
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
                .padding(bottom = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row(verticalAlignment = Alignment.CenterVertically) { // Contentor Compose: Alinha os filhos numa linha horizontal
                IconButton(onClick = onBackClick) { // Componente Compose: Desenha um botão com ícone
                    Icon(Icons.Rounded.ArrowBack, contentDescription = null, tint = LMInk) // Componente Compose: Desenha um ícone vetorial
                }
                Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    TranslatedText(
                        text = "Jogos",
                        fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp, color = LMInk
                    )
                    Text(text = torneio.nome, fontFamily = Geist, fontSize = 12.sp, color = LMGray500) // Componente Compose: Desenha texto estruturado no ecrã
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            TranslatedText(
                text = "PARTIDAS · ${jogos.size}",
                fontFamily = Geist, fontWeight = FontWeight.Bold,
                fontSize = 11.sp, color = LMGray500, letterSpacing = 0.4.sp
            )

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            if (isLoading) { // Estrutura de decisão condicional principal
                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = LMRed)
                }
            } else if (jogos.isEmpty()) { // Estrutura de decisão condicional principal
                Surface(
                    modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                    shape = RoundedCornerShape(16.dp),
                    color = LMGray50,
                    border = BorderStroke(1.dp, LMBorder)
                ) {
                    Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                        modifier = Modifier.padding(24.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon( // Componente Compose: Desenha um ícone vetorial
                            Icons.Default.SportsScore,
                            contentDescription = null,
                            tint = LMGray300,
                            modifier = Modifier.size(40.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                        )
                        Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                        TranslatedText("Sem jogos neste torneio.", fontFamily = Geist, fontSize = 13.sp, color = LMGray500)
                        TranslatedText("Cria o primeiro jogo abaixo.", fontFamily = Geist, fontSize = 12.sp, color = LMGray400)
                    }
                }
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    jogos.forEach { jogo ->
                        JogoOrgCard(
                            jogo = jogo,
                            onEditar = { onEditarJogo(jogo) },
                            onRemover = { jogoParaRemover = jogo },
                            onCardClick = { onVerEstatisticas(jogo) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Button( // Componente Compose: Desenha um botão interativo
                onClick = onCriarJogoClick, // Callback: Define a ação executada ao clicar no componente
                modifier = Modifier.fillMaxWidth().height(50.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LMInk)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = LMWhite, modifier = Modifier.size(18.dp)) // Componente Compose: Desenha um ícone vetorial
                Spacer(modifier = Modifier.width(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                TranslatedText("Criar novo jogo", fontFamily = Geist, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = LMWhite)
            }
        }
    }
}

@Composable
private fun JogoOrgCard( // Declaração de função / método de lógica
    jogo: Jogo,
    onEditar: () -> Unit,
    onRemover: () -> Unit,
    onCardClick: () -> Unit
) {
    val isFinished = jogo.estado.equals("Finalizado", ignoreCase = true) // Declara constante local (leitura única)
    val isOngoing = jogo.estado.equals("A Decorrer", ignoreCase = true) // Declara constante local (leitura única)

    val estadoCor = when { // Escolha múltipla condicional (semelhante a switch-case)
        isFinished -> LMGray500
        isOngoing -> Color(0xFF16A34A)
        else -> Color(0xFFD97706) // Fluxo condicional alternativo caso o 'if' seja falso
    }
    val estadoBg = when { // Escolha múltipla condicional (semelhante a switch-case)
        isFinished -> LMGray100
        isOngoing -> Color(0xFFDCFCE7)
        else -> Color(0xFFFEF3C7) // Fluxo condicional alternativo caso o 'if' seja falso
    }

    Surface(
        onClick = onCardClick, // Callback: Define a ação executada ao clicar no componente
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(14.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, LMBorder),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) { // Contentor Compose: Alinha os filhos numa linha horizontal
                    Surface(shape = RoundedCornerShape(99.dp), color = estadoBg) {
                        TranslatedText(
                            text = jogo.estado,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                            fontFamily = Geist, fontWeight = FontWeight.Bold,
                            fontSize = 10.sp, color = estadoCor
                        )
                    }

                    if (jogo.estado.equals("Agendado", ignoreCase = true) && jogo.data.isNotBlank()) { // Estrutura de decisão condicional principal
                        Spacer(modifier = Modifier.width(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = "${jogo.data} às ${jogo.hora}",
                            fontFamily = Geist,
                            fontSize = 11.sp,
                            color = LMGray500
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) { // Contentor Compose: Alinha os filhos numa linha horizontal
                    IconButton(onClick = onEditar, modifier = Modifier.size(32.dp)) { // Componente Compose: Desenha um botão com ícone
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = LMGray400, modifier = Modifier.size(16.dp)) // Componente Compose: Desenha um ícone vetorial
                    }
                    Spacer(modifier = Modifier.width(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                    IconButton(onClick = onRemover, modifier = Modifier.size(32.dp)) { // Componente Compose: Desenha um botão com ícone
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Remover", tint = LMGray400, modifier = Modifier.size(16.dp)) // Componente Compose: Desenha um ícone vetorial
                    }
                }
            }

            // Real-time ticking countdown in the list
            if (jogo.estado.equals("Agendado", ignoreCase = true) && jogo.data.isNotBlank()) { // Estrutura de decisão condicional principal
                var listCountdownText by remember { mutableStateOf("") } // Declara estado mutável local do Compose
                
                LaunchedEffect(jogo.data, jogo.hora) { // Efeito colateral Compose: executa código assíncrono ao recompor
                    while (true) {
                        val now = java.util.Calendar.getInstance() // Declara constante local (leitura única)
                        val matchCal = java.util.Calendar.getInstance() // Declara constante local (leitura única)
                        try { // Tenta executar bloco que pode lançar exceções
                            val dateSplit = jogo.data.split("/") // Declara constante local (leitura única)
                            val timeSplit = jogo.hora.split(":") // Declara constante local (leitura única)
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
                                    
                                    listCountdownText = if (days > 0) { // Estrutura de decisão condicional principal
                                        "Começa em: ${days}d ${hours}h ${minutes}m"
                                    } else if (hours > 0) { // Estrutura de decisão condicional principal
                                        "Começa em: ${hours}h ${minutes}m ${seconds}s"
                                    } else { // Fluxo condicional alternativo caso o 'if' seja falso
                                        "Começa em: ${minutes}m ${seconds}s"
                                    }
                                } else { // Fluxo condicional alternativo caso o 'if' seja falso
                                    listCountdownText = "Hora do jogo atingida"
                                }
                            }
                        } catch (e: Exception) {} // Captura e trata eventuais exceções ocorridas no bloco try
                        kotlinx.coroutines.delay(1000)
                    }
                }
                
                if (listCountdownText.isNotBlank()) { // Estrutura de decisão condicional principal
                    Spacer(modifier = Modifier.height(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = listCountdownText,
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color(0xFFEF4444)
                    )
                }
            } else if (isOngoing) { // Estrutura de decisão condicional principal

                var elapsedSeconds by remember(jogo.iniciado_em) { // Memoriza estado para evitar perda durante a recomposição
                    mutableStateOf(0) // Declara estado mutável local do Compose
                }

                LaunchedEffect(jogo.iniciado_em) { // Efeito colateral Compose: executa código assíncrono ao recompor
                    while (true) {

                        val inicioMillis = com.leaguematch.util.parseIniciadoEmEpochMillis(jogo.iniciado_em) // Declara constante local (leitura única)

                        elapsedSeconds = if (inicioMillis != null) { // Estrutura de decisão condicional principal
                            val agoraMillis = System.currentTimeMillis() // Declara constante local (leitura única)
                            ((agoraMillis - inicioMillis) / 1000).toInt().coerceAtLeast(0)
                        } else { // Fluxo condicional alternativo caso o 'if' seja falso
                            0
                        }

                        kotlinx.coroutines.delay(1000)
                    }
                }

                val min = elapsedSeconds / 60 // Declara constante local (leitura única)
                val sec = elapsedSeconds % 60 // Declara constante local (leitura única)

                Spacer(modifier = Modifier.height(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Row(verticalAlignment = Alignment.CenterVertically) { // Contentor Compose: Alinha os filhos numa linha horizontal
                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .size(6.dp)
                            .background(
                                Color(0xFF22C55E),
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )

                    Spacer(modifier = Modifier.width(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    TranslatedText(
                        text = String.format("Tempo decorrido: %02d:%02d", min, sec),
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color(0xFF16A34A)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                verticalAlignment = Alignment.CenterVertically
            ) {
                TranslatedText(
                    text = jogo.casa,
                    fontFamily = Geist, fontWeight = FontWeight.Bold,
                    fontSize = 14.sp, color = LMInk,
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .background(LMGray100, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = if (isFinished || isOngoing) "${jogo.resultadoCasa} - ${jogo.resultadoFora}" else "vs", // Estrutura de decisão condicional principal
                        fontFamily = Bricolage, fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp, color = LMInk
                    )
                }
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = jogo.fora,
                    fontFamily = Geist, fontWeight = FontWeight.Bold,
                    fontSize = 14.sp, color = LMInk,
                    modifier = Modifier.weight(1f), // Modificador Compose: Define tamanho, margem, padding ou clique
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
            }
        }
    }
}
