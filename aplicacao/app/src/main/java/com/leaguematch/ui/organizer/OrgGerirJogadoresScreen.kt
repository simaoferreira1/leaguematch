package com.leaguematch.ui.organizer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.Equipa
import com.leaguematch.data.remote.model.TeamCode
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.ui.theme.*

@Composable
fun OrgGerirJogadoresScreen(
    equipa: Equipa,
    jogadores: List<Utilizador>,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onRemoverJogador: (Utilizador) -> Unit
) {
    var jogadorParaRemover by remember { mutableStateOf<Utilizador?>(null) }

    if (jogadorParaRemover != null) {
        AlertDialog(
            onDismissRequest = { jogadorParaRemover = null },
            title = {
                Text("Remover jogador?", fontFamily = Geist, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Text(
                    "Queres remover \"${jogadorParaRemover!!.nome}\" da equipa?",
                    fontFamily = Geist, fontSize = 13.sp, color = LMGray600
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onRemoverJogador(jogadorParaRemover!!)
                    jogadorParaRemover = null
                }) {
                    Text("Remover", color = LMRed, fontFamily = Geist, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { jogadorParaRemover = null }) {
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
                        text = "Jogadores",
                        fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp, color = LMInk
                    )
                    Text(text = equipa.nome, fontFamily = Geist, fontSize = 12.sp, color = LMGray500)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = LMRed50,
                border = BorderStroke(1.dp, LMBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "CÓDIGO DE INTEGRAÇÃO",
                        fontFamily = Geist, fontWeight = FontWeight.Bold,
                        fontSize = 11.sp, color = LMGray500, letterSpacing = 0.4.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = TeamCode.encode(equipa.id),
                        fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp, color = LMInk, letterSpacing = 3.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Partilha este código com os jogadores. Eles entram pela secção \"Integrar Equipa\" da app.",
                        fontFamily = Geist, fontSize = 12.sp, color = LMGray600
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "JOGADORES · ${jogadores.size}",
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
            } else if (jogadores.isEmpty()) {
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
                            Icons.Default.Person, contentDescription = null,
                            tint = LMGray300, modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Sem jogadores na equipa.", fontFamily = Geist, fontSize = 13.sp, color = LMGray500)
                        Text(
                            "Partilha o código acima para os jogadores se inscreverem.",
                            fontFamily = Geist, fontSize = 12.sp, color = LMGray400
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    jogadores.forEach { jogador ->
                        JogadorListItem(
                            jogador = jogador,
                            onRemover = { jogadorParaRemover = jogador }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun JogadorListItem(
    jogador: Utilizador,
    onRemover: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, LMBorder),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(LMGray100, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = LMGray500, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = jogador.nome,
                    fontFamily = Geist, fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp, color = LMInk
                )
                Text(
                    text = jogador.email,
                    fontFamily = Geist, fontSize = 12.sp, color = LMGray500
                )
            }
            IconButton(onClick = onRemover, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "Remover", tint = LMGray400, modifier = Modifier.size(18.dp))
            }
        }
    }
}
