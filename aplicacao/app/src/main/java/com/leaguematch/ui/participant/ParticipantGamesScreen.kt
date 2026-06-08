package com.leaguematch.ui.participant

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.translations.AppStrings
import com.leaguematch.ui.components.ParticipantBottomBar
import com.leaguematch.ui.theme.*

private enum class GamesTab {
    PROXIMOS,
    RESULTADOS,
    HISTORICO
}

@Composable
fun ParticipantGamesScreen(
    jogos: List<Jogo>,
    strings: AppStrings,
    primaryColor: Color,
    onHomeClick: () -> Unit,
    onTorneiosClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEquipaClick: () -> Unit,
    onEstatisticasClick: () -> Unit,
    onPerfilClick: () -> Unit,
    onJogoClick: (Jogo) -> Unit
) {
    var selectedTab by remember { mutableStateOf(GamesTab.PROXIMOS) }

    val proximosJogos = jogos.filter {
        it.estado.equals("Agendado", ignoreCase = true) ||
                it.estado.equals("Por iniciar", ignoreCase = true)
    }

    val resultados = jogos.filter {
        it.estado.equals("Finalizado", ignoreCase = true)
    }

    val historico = jogos.filter {
        it.estado.equals("Finalizado", ignoreCase = true) ||
                it.estado.equals("A Decorrer", ignoreCase = true)
    }

    val jogosFiltrados = when (selectedTab) {
        GamesTab.PROXIMOS -> proximosJogos
        GamesTab.RESULTADOS -> resultados
        GamesTab.HISTORICO -> historico
    }

    Scaffold(
        bottomBar = {
            ParticipantBottomBar(
                selectedItem = "jogos",
                onHomeClick = onHomeClick,
                onTorneiosClick = onTorneiosClick,
                onJogosClick = onJogosClick,
                onEquipaClick = onEquipaClick,
                onEstatisticasClick = onEstatisticasClick,
                onPerfilClick = onPerfilClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 18.dp)
                .padding(bottom = 80.dp)
        ) {
            Text(
                text = strings.myGamesTitle,
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = strings.myGamesSubtitle,
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GamesFilterChip(
                    text = strings.upcomingTab,
                    selected = selectedTab == GamesTab.PROXIMOS,
                    primaryColor = primaryColor,
                    onClick = { selectedTab = GamesTab.PROXIMOS },
                    modifier = Modifier.weight(1f)
                )

                GamesFilterChip(
                    text = strings.resultsTab,
                    selected = selectedTab == GamesTab.RESULTADOS,
                    primaryColor = primaryColor,
                    onClick = { selectedTab = GamesTab.RESULTADOS },
                    modifier = Modifier.weight(1f)
                )

                GamesFilterChip(
                    text = strings.historyTab,
                    selected = selectedTab == GamesTab.HISTORICO,
                    primaryColor = primaryColor,
                    onClick = { selectedTab = GamesTab.HISTORICO },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (jogosFiltrados.isEmpty()) {
                Text(
                    text = when (selectedTab) {
                        GamesTab.PROXIMOS -> strings.noUpcomingGames
                        GamesTab.RESULTADOS -> strings.noResultsYet
                        GamesTab.HISTORICO -> strings.noGameHistory
                    },
                    fontFamily = Geist,
                    fontSize = 14.sp,
                    color = LMGray500
                )
            } else {
                jogosFiltrados.forEach { jogo ->
                    ParticipantGameCard(
                        jogo = jogo,
                        primaryColor = primaryColor,
                        onCardClick = { onJogoClick(jogo) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun GamesFilterChip(
    text: String,
    selected: Boolean,
    primaryColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = if (selected) primaryColor else LMWhite,
        border = BorderStroke(
            1.dp,
            if (selected) primaryColor else Color(0xFFE5E5EA)
        ),
        shadowElevation = 1.dp,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = if (selected) LMWhite else LMInk
            )
        }
    }
}

@Composable
private fun ParticipantGameCard(
    jogo: Jogo,
    primaryColor: Color,
    onCardClick: () -> Unit
) {
    Surface(
        onClick = onCardClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.SportsSoccer,
                contentDescription = null,
                tint = primaryColor,
                modifier = Modifier.size(34.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${jogo.casa} vs ${jogo.fora}",
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = LMInk
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = buildString {
                        append(jogo.estado)

                        if (jogo.data.isNotBlank()) {
                            append(" • ")
                            append(jogo.data)
                        }

                        if (jogo.hora.isNotBlank()) {
                            append(" ")
                            append(jogo.hora)
                        }
                    },
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }

            if (
                jogo.estado.equals("Finalizado", ignoreCase = true) ||
                jogo.estado.equals("A Decorrer", ignoreCase = true)
            ) {
                Text(
                    text = "${jogo.resultadoCasa}-${jogo.resultadoFora}",
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = LMInk
                )
            }
        }
    }
}