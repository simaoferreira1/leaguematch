package com.leaguematch.ui.organizer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.ui.components.TranslatedText
import com.leaguematch.ui.theme.Bricolage
import com.leaguematch.ui.theme.Geist
import com.leaguematch.ui.theme.LMBorder
import com.leaguematch.ui.theme.LMGray100
import com.leaguematch.ui.theme.LMGray300
import com.leaguematch.ui.theme.LMGray400
import com.leaguematch.ui.theme.LMGray50
import com.leaguematch.ui.theme.LMGray500
import com.leaguematch.ui.theme.LMGray600
import com.leaguematch.ui.theme.LMInk
import com.leaguematch.ui.theme.LMRed
import com.leaguematch.ui.theme.LMWhite

@Composable
fun OrgVerJogosScreen(
    torneio: Torneio,
    jogos: List<Jogo>,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onCriarJogoClick: () -> Unit,
    onEditarJogo: (Jogo) -> Unit,
    onRemoverJogo: (Jogo) -> Unit,
    onVerEstatisticas: (Jogo) -> Unit
) {
    var jogoParaRemover by remember { mutableStateOf<Jogo?>(null) }

    if (jogoParaRemover != null) {
        AlertDialog(
            onDismissRequest = { jogoParaRemover = null },
            title = {
                Text("Remover jogo?", fontFamily = Geist, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Text(
                    "Tens a certeza que queres remover a partida entre \"${jogoParaRemover!!.casa}\" e \"${jogoParaRemover!!.fora}\"?",
                    fontFamily = Geist, fontSize = 13.sp, color = LMGray600
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onRemoverJogo(jogoParaRemover!!)
                    jogoParaRemover = null
                }) {
                    Text("Remover", color = LMRed, fontFamily = Geist, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { jogoParaRemover = null }) {
                    Text("Cancelar", fontFamily = Geist, color = LMGray500)
                }
            },
            shape = RoundedCornerShape(18.dp)
        )
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
                .padding(bottom = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = null, tint = LMInk)
                }
                Column(modifier = Modifier.weight(1f)) {
                    TranslatedText(
                        text = "Jogos",
                        fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp, color = LMInk
                    )
                    Text(text = torneio.nome, fontFamily = Geist, fontSize = 12.sp, color = LMGray500)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TranslatedText(
                text = "PARTIDAS · ${jogos.size}",
                fontFamily = Geist, fontWeight = FontWeight.Bold,
                fontSize = 11.sp, color = LMGray500, letterSpacing = 0.4.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = LMRed)
                }
            } else if (jogos.isEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = LMGray50,
                    border = BorderStroke(1.dp, LMBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.SportsScore,
                            contentDescription = null,
                            tint = LMGray300,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TranslatedText("Sem jogos neste torneio.", fontFamily = Geist, fontSize = 13.sp, color = LMGray500)
                        TranslatedText("Cria o primeiro jogo abaixo.", fontFamily = Geist, fontSize = 12.sp, color = LMGray400)
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onCriarJogoClick,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LMInk)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = LMWhite, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                TranslatedText("Criar novo jogo", fontFamily = Geist, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = LMWhite)
            }
        }
    }
}

@Composable
private fun JogoOrgCard(
    jogo: Jogo,
    onEditar: () -> Unit,
    onRemover: () -> Unit,
    onCardClick: () -> Unit
) {
    val isFinished = jogo.estado.equals("Finalizado", ignoreCase = true)
    val isOngoing = jogo.estado.equals("A Decorrer", ignoreCase = true)

    val estadoCor = when {
        isFinished -> LMGray500
        isOngoing -> Color(0xFF16A34A)
        else -> Color(0xFFD97706)
    }
    val estadoBg = when {
        isFinished -> LMGray100
        isOngoing -> Color(0xFFDCFCE7)
        else -> Color(0xFFFEF3C7)
    }

    Surface(
        onClick = onCardClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, LMBorder),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = RoundedCornerShape(99.dp), color = estadoBg) {
                        TranslatedText(
                            text = jogo.estado,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                            fontFamily = Geist, fontWeight = FontWeight.Bold,
                            fontSize = 10.sp, color = estadoCor
                        )
                    }

                    if (jogo.estado.equals("Agendado", ignoreCase = true) && jogo.data.isNotBlank()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${jogo.data} às ${jogo.hora}",
                            fontFamily = Geist,
                            fontSize = 11.sp,
                            color = LMGray500
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onEditar, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = LMGray400, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(onClick = onRemover, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Remover", tint = LMGray400, modifier = Modifier.size(16.dp))
                    }
                }
            }

            // Real-time ticking countdown in the list
            if (jogo.estado.equals("Agendado", ignoreCase = true) && jogo.data.isNotBlank()) {
                var listCountdownText by remember { mutableStateOf("") }
                
                LaunchedEffect(jogo.data, jogo.hora) {
                    while (true) {
                        val now = java.util.Calendar.getInstance()
                        val matchCal = java.util.Calendar.getInstance()
                        try {
                            val dateSplit = jogo.data.split("/")
                            val timeSplit = jogo.hora.split(":")
                            if (dateSplit.size == 3 && timeSplit.size == 2) {
                                matchCal.set(
                                    dateSplit[2].toInt(),
                                    dateSplit[1].toInt() - 1,
                                    dateSplit[0].toInt(),
                                    timeSplit[0].toInt(),
                                    timeSplit[1].toInt(),
                                    0
                                )
                                val diff = matchCal.timeInMillis - now.timeInMillis
                                if (diff > 0) {
                                    val days = diff / (24 * 60 * 60 * 1000)
                                    val hours = (diff / (60 * 60 * 1000)) % 24
                                    val minutes = (diff / (60 * 1000)) % 60
                                    val seconds = (diff / 1000) % 60
                                    
                                    listCountdownText = if (days > 0) {
                                        "Começa em: ${days}d ${hours}h ${minutes}m"
                                    } else if (hours > 0) {
                                        "Começa em: ${hours}h ${minutes}m ${seconds}s"
                                    } else {
                                        "Começa em: ${minutes}m ${seconds}s"
                                    }
                                } else {
                                    listCountdownText = "Hora do jogo atingida"
                                }
                            }
                        } catch (e: Exception) {}
                        kotlinx.coroutines.delay(1000)
                    }
                }
                
                if (listCountdownText.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = listCountdownText,
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color(0xFFEF4444)
                    )
                }
            } else if (isOngoing) {
                // Pulses green and counts elapsed seconds
                var elapsedSeconds by remember { mutableStateOf(0) }
                
                LaunchedEffect(Unit) {
                    while (true) {
                        kotlinx.coroutines.delay(1000)
                        elapsedSeconds++
                    }
                }
                
                val min = elapsedSeconds / 60
                val sec = elapsedSeconds % 60
                
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color(0xFF22C55E), shape = androidx.compose.foundation.shape.CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    TranslatedText(
                        text = String.format("Tempo decorrido: %02d:%02d", min, sec),
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color(0xFF16A34A)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TranslatedText(
                    text = jogo.casa,
                    fontFamily = Geist, fontWeight = FontWeight.Bold,
                    fontSize = 14.sp, color = LMInk,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .background(LMGray100, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isFinished || isOngoing) "${jogo.resultadoCasa} - ${jogo.resultadoFora}" else "vs",
                        fontFamily = Bricolage, fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp, color = LMInk
                    )
                }
                Text(
                    text = jogo.fora,
                    fontFamily = Geist, fontWeight = FontWeight.Bold,
                    fontSize = 14.sp, color = LMInk,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
            }
        }
    }
}
