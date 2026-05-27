package com.leaguematch.ui.organizer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.ui.theme.*

@Composable
fun OrgVerJogosScreen(
    torneio: Torneio,
    jogos: List<Jogo>,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onCriarJogoClick: () -> Unit,
    onEditarJogo: (Jogo) -> Unit,
    onRemoverJogo: (Jogo) -> Unit
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
                    Text(
                        text = "Jogos",
                        fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp, color = LMInk
                    )
                    Text(text = torneio.nome, fontFamily = Geist, fontSize = 12.sp, color = LMGray500)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
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
                        Text("Sem jogos neste torneio.", fontFamily = Geist, fontSize = 13.sp, color = LMGray500)
                        Text("Cria o primeiro jogo abaixo.", fontFamily = Geist, fontSize = 12.sp, color = LMGray400)
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    jogos.forEach { jogo ->
                        JogoOrgCard(
                            jogo = jogo,
                            onEditar = { onEditarJogo(jogo) },
                            onRemover = { jogoParaRemover = jogo }
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
                Text("Criar novo jogo", fontFamily = Geist, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = LMWhite)
            }
        }
    }
}

@Composable
private fun JogoOrgCard(
    jogo: Jogo,
    onEditar: () -> Unit,
    onRemover: () -> Unit
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
                Surface(shape = RoundedCornerShape(99.dp), color = estadoBg) {
                    Text(
                        text = jogo.estado,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                        fontFamily = Geist, fontWeight = FontWeight.Bold,
                        fontSize = 10.sp, color = estadoCor
                    )
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

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
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
