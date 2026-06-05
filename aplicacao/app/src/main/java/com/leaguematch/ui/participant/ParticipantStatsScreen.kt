package com.leaguematch.ui.participant

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.translations.AppStrings
import com.leaguematch.ui.components.ParticipantBottomBar
import com.leaguematch.ui.theme.*

data class ParticipantStats(
    val jogos: Int = 0,
    val golos: Int = 0,
    val assistencias: Int = 0,
    val mvp: Int = 0
)

@Composable
fun ParticipantStatsScreen(
    stats: ParticipantStats,
    strings: AppStrings,
    primaryColor: Color,
    onHomeClick: () -> Unit,
    onTorneiosClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEquipaClick: () -> Unit,
    onEstatisticasClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
    val golosPorJogo = if (stats.jogos > 0) stats.golos.toDouble() / stats.jogos else 0.0
    val assistPorJogo = if (stats.jogos > 0) stats.assistencias.toDouble() / stats.jogos else 0.0

    Scaffold(
        bottomBar = {
            ParticipantBottomBar(
                selectedItem = "estatisticas",
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
                text = strings.myStatsTitle,
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = strings.myStatsSubtitle,
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard(
                    label = strings.gamesStat,
                    value = stats.jogos.toString(),
                    icon = Icons.Default.Assessment,
                    primaryColor = primaryColor,
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    label = strings.goalsStat,
                    value = stats.golos.toString(),
                    icon = Icons.Default.SportsSoccer,
                    primaryColor = primaryColor,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard(
                    label = strings.assistsStat,
                    value = stats.assistencias.toString(),
                    icon = Icons.Default.EmojiEvents,
                    primaryColor = primaryColor,
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    label = strings.mvpStat,
                    value = stats.mvp.toString(),
                    icon = Icons.Default.Star,
                    primaryColor = primaryColor,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            PerformanceChartCard(
                stats = stats,
                strings = strings,
                primaryColor = primaryColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = LMWhite,
                border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
                shadowElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = strings.performanceTitle,
                        fontFamily = Bricolage,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = LMInk
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        MiniPerformanceCard(
                            label = strings.goalsPerGame,
                            value = String.format("%.1f", golosPorJogo),
                            modifier = Modifier.weight(1f)
                        )

                        MiniPerformanceCard(
                            label = strings.assistsPerGame,
                            value = String.format("%.1f", assistPorJogo),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            InfoCard(
                stats = stats,
                strings = strings,
                primaryColor = primaryColor
            )
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    icon: ImageVector,
    primaryColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(114.dp),
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = primaryColor,
                modifier = Modifier.size(26.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = value,
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 26.sp,
                color = LMInk
            )

            Text(
                text = label,
                fontFamily = Geist,
                fontSize = 12.sp,
                color = LMGray500
            )
        }
    }
}

@Composable
private fun PerformanceChartCard(
    stats: ParticipantStats,
    strings: AppStrings,
    primaryColor: Color
) {
    val maxValue = listOf(
        stats.jogos,
        stats.golos,
        stats.assistencias,
        stats.mvp
    ).maxOrNull()?.coerceAtLeast(1) ?: 1

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = strings.overallPerformance,
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = strings.overallPerformanceSubtitle,
                fontFamily = Geist,
                fontSize = 12.sp,
                color = LMGray500
            )

            Spacer(modifier = Modifier.height(18.dp))

            ChartBar(strings.gamesStat, stats.jogos, maxValue, primaryColor)
            Spacer(modifier = Modifier.height(10.dp))

            ChartBar(strings.goalsStat, stats.golos, maxValue, primaryColor)
            Spacer(modifier = Modifier.height(10.dp))

            ChartBar(strings.assistsStat, stats.assistencias, maxValue, primaryColor)
            Spacer(modifier = Modifier.height(10.dp))

            ChartBar(strings.mvpStat, stats.mvp, maxValue, primaryColor)

            if (stats.jogos == 0 && stats.golos == 0 && stats.assistencias == 0 && stats.mvp == 0) {
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = strings.notEnoughStats,
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }
        }
    }
}

@Composable
private fun ChartBar(
    label: String,
    value: Int,
    maxValue: Int,
    primaryColor: Color
) {
    val progress = if (maxValue > 0) value.toFloat() / maxValue.toFloat() else 0f

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMInk,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = value.toString(),
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(Color(0xFFF0F0F4), RoundedCornerShape(50.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .height(10.dp)
                    .background(primaryColor, RoundedCornerShape(50.dp))
            )
        }
    }
}

@Composable
private fun MiniPerformanceCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFF7F7FA)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = value,
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = LMInk
            )

            Text(
                text = label,
                fontFamily = Geist,
                fontSize = 12.sp,
                color = LMGray500
            )
        }
    }
}

@Composable
private fun InfoCard(
    stats: ParticipantStats,
    strings: AppStrings,
    primaryColor: Color
) {
    val message = if (stats.jogos == 0) {
        strings.statsStartMessage
    } else {
        strings.statsContinueMessage
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = primaryColor.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, primaryColor.copy(alpha = 0.20f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = primaryColor,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = message,
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMInk
            )
        }
    }
}