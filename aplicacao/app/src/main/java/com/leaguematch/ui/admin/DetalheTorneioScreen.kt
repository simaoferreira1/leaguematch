package com.leaguematch.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.People
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.ui.components.*
import com.leaguematch.ui.theme.*
import com.leaguematch.data.remote.model.DetalheTorneio
import com.leaguematch.data.remote.model.Torneio

data class ParticipanteTorneioUi(
    val nome: String,
    val golos: Int
)

data class JogoTorneioUi(
    val casa: String,
    val resultado: String,
    val fora: String
)

@Composable
fun DetalheTorneioScreen(
    detalhe: DetalheTorneio? = null,
    nomeTorneio: String = "Carabao CUP",
    modalidade: String = "Futebol",
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onUtilizadoresClick: () -> Unit = {},
    onTorneiosClick: () -> Unit = {},
    onGraficosClick: () -> Unit = {},
    onDefinicoesClick: () -> Unit = {}
) {
    val torneio = detalhe?.torneio ?: Torneio(1, nomeTorneio, modalidade, "", "LIGA", "Em Progresso", 16)
    val participantes = detalhe?.goleadores
        ?.map { ParticipanteTorneioUi(it.nome, it.golos) }
        ?: listOf(
            ParticipanteTorneioUi("Rúben Ferreira", 12),
            ParticipanteTorneioUi("Simão Ferreira", 8),
            ParticipanteTorneioUi("João Fernandes", 4),
            ParticipanteTorneioUi("Diogo Gomes", 4)
        )

    val jogos = detalhe?.jogos
        ?.map { JogoTorneioUi(it.casa, "${it.resultadoCasa}-${it.resultadoFora}", it.fora) }
        ?: listOf(
            JogoTorneioUi("Prata da Casa FC", "2-0", "Bola na Rede FC"),
            JogoTorneioUi("Bola Parada FC", "1-2", "Tiki Tasca FC"),
            JogoTorneioUi("Prata da Casa FC", "4-0", "Tiki Tasca FC"),
            JogoTorneioUi("Bola na Rede FC", "1-0", "Bola Parada FC")
        )

    Scaffold(
        bottomBar = {
            AdminBottomBar(
                selectedItem = "torneios",
                onHomeClick = onHomeClick,
                onUtilizadoresClick = onUtilizadoresClick,
                onTorneiosClick = onTorneiosClick,
                onGraficosClick = onGraficosClick,
                onDefinicoesClick = onDefinicoesClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 12.dp)
        ) {
            // Header TopBar
            TopBar(
                title = "Torneio",
                back = true,
                onBackClick = onBackClick
            )

            // Tournament Banner Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(LMRed, LMRed700)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(LMWhite.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = LMWhite,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = torneio.nome,
                            color = LMWhite,
                            fontFamily = Bricolage,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.3).sp
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "${torneio.modalidade} • ${torneio.formato}",
                            color = LMWhite.copy(alpha = 0.85f),
                            fontFamily = Geist,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                        
                        Spacer(modifier = Modifier.height(6.dp))

                        Pill(
                            text = torneio.estado,
                            kind = if (torneio.estado == "Em Progresso") "live" else "soon"
                        )
                    }
                }
            }

            // Quick Stats Row (3 blocks)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val stats = listOf(
                    Triple("Equipas", torneio.equipas.toString(), Icons.Rounded.People),
                    Triple("Jogos", jogos.size.toString(), Icons.Default.EmojiEvents),
                    Triple("Golos", (detalhe?.totalGolos ?: 28).toString(), Icons.Default.SportsSoccer)
                )

                stats.forEach { (label, value, icon) ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(LMGray100, RoundedCornerShape(12.dp))
                            .padding(vertical = 12.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = LMRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = value,
                            fontFamily = GeistMono,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LMInk
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                        Text(
                            text = label.uppercase(),
                            fontFamily = Geist,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = LMGray500,
                            letterSpacing = 0.4.sp
                        )
                    }
                }
            }

            // Goleadores / Melhores Marcadores
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "MELHORES MARCADORES",
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = LMGray500,
                    letterSpacing = 0.4.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                CardWrapper(
                    modifier = Modifier.fillMaxWidth(),
                    pad = 0.dp
                ) {
                    Column {
                        participantes.forEachIndexed { index, p ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Avatar(name = p.nome, size = 26.dp, color = LMInk)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = p.nome,
                                    fontFamily = Geist,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = LMInk,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "${p.golos} golos",
                                    fontFamily = GeistMono,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LMRed
                                )
                            }

                            if (index < participantes.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(start = 50.dp),
                                    color = LMBorder,
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Recent Matches List
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "JOGOS RECENTES",
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = LMGray500,
                    letterSpacing = 0.4.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                CardWrapper(
                    modifier = Modifier.fillMaxWidth(),
                    pad = 0.dp
                ) {
                    Column {
                        jogos.forEachIndexed { index, j ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = j.casa,
                                    fontFamily = Geist,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = LMInk,
                                    modifier = Modifier.weight(1.2f)
                                )
                                
                                Box(
                                    modifier = Modifier
                                        .background(LMGray100, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = j.resultado,
                                        fontFamily = GeistMono,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LMInk
                                    )
                                }
                                
                                Text(
                                    text = j.fora,
                                    fontFamily = Geist,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = LMInk,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.weight(1.2f)
                                )
                            }

                            if (index < jogos.size - 1) {
                                HorizontalDivider(
                                    color = LMBorder,
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetalheTorneioScreenPreview() {
    LeagueMatchTheme {
        DetalheTorneioScreen()
    }
}
