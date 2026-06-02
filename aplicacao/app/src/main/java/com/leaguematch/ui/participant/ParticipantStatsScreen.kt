package com.leaguematch.ui.participant

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    onHomeClick: () -> Unit,
    onTorneiosClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEstatisticasClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
    Scaffold(
        bottomBar = {
            ParticipantBottomBar(
                selectedItem = "estatisticas",
                onHomeClick = onHomeClick,
                onTorneiosClick = onTorneiosClick,
                onJogosClick = onJogosClick,
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
                text = "As minhas estatísticas",
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Resumo do teu desempenho.",
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard("Jogos", stats.jogos.toString(), Icons.Default.Assessment, Modifier.weight(1f))
                StatCard("Golos", stats.golos.toString(), Icons.Default.SportsSoccer, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard("Assist.", stats.assistencias.toString(), Icons.Default.EmojiEvents, Modifier.weight(1f))
                StatCard("MVP", stats.mvp.toString(), Icons.Default.Star, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, androidx.compose.ui.graphics.Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = LMRed,
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