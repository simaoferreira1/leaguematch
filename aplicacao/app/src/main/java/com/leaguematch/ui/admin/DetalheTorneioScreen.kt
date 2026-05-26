package com.leaguematch.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.SportsHandball
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.SportsScore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.DetalheTorneio
import com.leaguematch.ui.components.AdminBottomBar
import com.leaguematch.ui.components.Avatar
import com.leaguematch.ui.theme.*

@Composable
fun DetalheTorneioScreen(
    detalhe: DetalheTorneio?,
    onBackClick: () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val torneio = detalhe?.torneio

    Scaffold(
        bottomBar = bottomBar,
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        if (torneio == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Torneio não encontrado.", color = LMGray500)
            }
            return@Scaffold
        }

        val config = modalidadeConfig(torneio.modalidade)
        val jogos = detalhe.jogos
        val goleadores = detalhe.goleadores
        val totalEventos = detalhe.totalGolos

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = null,
                        tint = LMInk
                    )
                }

                Text(
                    text = "Detalhes do Torneio",
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = LMInk
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = LMWhite,
                border = BorderStroke(1.dp, LMBorder),
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                brush = Brush.linearGradient(config.colors),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = config.icon,
                            contentDescription = null,
                            tint = LMWhite,
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = torneio.modalidade.uppercase(),
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = LMGray500
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = torneio.nome,
                            fontFamily = Geist,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = LMInk
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = "${torneio.formato} • ${torneio.estado}",
                            fontFamily = Geist,
                            fontSize = 12.sp,
                            color = LMGray500
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MiniStatCard(
                    label = config.participantsLabel,
                    value = torneio.equipas.toString(),
                    icon = Icons.Rounded.Groups,
                    modifier = Modifier.weight(1f)
                )

                MiniStatCard(
                    label = config.gamesLabel,
                    value = jogos.size.toString(),
                    icon = Icons.Rounded.SportsScore,
                    modifier = Modifier.weight(1f)
                )

                MiniStatCard(
                    label = config.eventsLabel,
                    value = totalEventos.toString(),
                    icon = config.icon,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle(config.rankingTitle)

            if (goleadores.isEmpty()) {
                EmptyCard(config.emptyRankingText)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    goleadores.forEach { jogador ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            color = LMWhite,
                            border = BorderStroke(1.dp, LMBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Avatar(
                                    name = jogador.nome,
                                    size = 34.dp,
                                    color = LMInk
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = jogador.nome,
                                    fontFamily = Geist,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = LMInk,
                                    modifier = Modifier.weight(1f)
                                )

                                Text(
                                    text = "${jogador.golos} ${config.eventsLabel.lowercase()}",
                                    fontFamily = Geist,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 12.sp,
                                    color = LMRed
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle(config.gamesSectionTitle)

            if (jogos.isEmpty()) {
                EmptyCard("Sem ${config.gamesLabel.lowercase()} registados.")
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    jogos.forEach { jogo ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            color = LMWhite,
                            border = BorderStroke(1.dp, LMBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = jogo.casa,
                                    fontFamily = Geist,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = LMInk,
                                    modifier = Modifier.weight(1f)
                                )

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = LMInk
                                ) {
                                    Text(
                                        text = "${jogo.resultadoCasa}-${jogo.resultadoFora}",
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                        fontFamily = GeistMono,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = LMWhite
                                    )
                                }

                                Text(
                                    text = jogo.fora,
                                    fontFamily = Geist,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = LMInk,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private data class ModalidadeConfig(
    val icon: ImageVector,
    val colors: List<Color>,
    val participantsLabel: String,
    val gamesLabel: String,
    val eventsLabel: String,
    val rankingTitle: String,
    val gamesSectionTitle: String,
    val emptyRankingText: String
)

private fun modalidadeConfig(modalidade: String): ModalidadeConfig {
    return when (modalidade.lowercase()) {
        "futebol" -> ModalidadeConfig(
            icon = Icons.Default.SportsSoccer,
            colors = listOf(LMRed, LMRed700),
            participantsLabel = "Equipas",
            gamesLabel = "Jogos",
            eventsLabel = "Golos",
            rankingTitle = "Melhores marcadores",
            gamesSectionTitle = "Jogos recentes",
            emptyRankingText = "Sem golos registados."
        )

        "andebol" -> ModalidadeConfig(
            icon = Icons.Default.SportsHandball,
            colors = listOf(Color(0xFF16A34A), Color(0xFF15803D)),
            participantsLabel = "Equipas",
            gamesLabel = "Jogos",
            eventsLabel = "Golos",
            rankingTitle = "Melhores marcadores",
            gamesSectionTitle = "Jogos recentes",
            emptyRankingText = "Sem golos registados."
        )

        "basquetebol" -> ModalidadeConfig(
            icon = Icons.Default.SportsBasketball,
            colors = listOf(Color(0xFF2563EB), Color(0xFF1D4ED8)),
            participantsLabel = "Equipas",
            gamesLabel = "Jogos",
            eventsLabel = "Pontos",
            rankingTitle = "Melhores pontuadores",
            gamesSectionTitle = "Jogos recentes",
            emptyRankingText = "Sem pontos registados."
        )

        "padel" -> ModalidadeConfig(
            icon = Icons.Default.SportsTennis,
            colors = listOf(Color(0xFF1F2937), Color(0xFF111827)),
            participantsLabel = "Duplas",
            gamesLabel = "Partidas",
            eventsLabel = "Sets",
            rankingTitle = "Destaques",
            gamesSectionTitle = "Partidas recentes",
            emptyRankingText = "Sem destaques registados."
        )

        "ténis", "tenis" -> ModalidadeConfig(
            icon = Icons.Default.SportsTennis,
            colors = listOf(Color(0xFFBE123C), Color(0xFF9F1239)),
            participantsLabel = "Jogadores",
            gamesLabel = "Partidas",
            eventsLabel = "Sets",
            rankingTitle = "Destaques",
            gamesSectionTitle = "Partidas recentes",
            emptyRankingText = "Sem destaques registados."
        )

        else -> ModalidadeConfig(
            icon = Icons.Default.EmojiEvents,
            colors = listOf(LMRed, LMRed700),
            participantsLabel = "Equipas",
            gamesLabel = "Jogos",
            eventsLabel = "Eventos",
            rankingTitle = "Destaques",
            gamesSectionTitle = "Jogos recentes",
            emptyRankingText = "Sem eventos registados."
        )
    }
}

@Composable
private fun MiniStatCard(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, LMBorder),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = LMInk
            )

            Text(
                text = label.uppercase(),
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp,
                color = LMGray500
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text.uppercase(),
        fontFamily = Geist,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        color = LMGray500,
        letterSpacing = 0.4.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun EmptyCard(text: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, LMBorder)
    ) {
        Box(
            modifier = Modifier.padding(18.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500
            )
        }
    }
}