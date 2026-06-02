package com.leaguematch.ui.participant

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.ui.components.ParticipantBottomBar
import com.leaguematch.ui.theme.*

@Composable
fun ParticipantGamesScreen(
    jogos: List<Jogo>,
    onHomeClick: () -> Unit,
    onTorneiosClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEstatisticasClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
    Scaffold(
        bottomBar = {
            ParticipantBottomBar(
                selectedItem = "jogos",
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
                text = "Os meus jogos",
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Consulta próximos jogos e resultados.",
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (jogos.isEmpty()) {
                Text(
                    text = "Ainda não existem jogos associados à tua conta.",
                    fontFamily = Geist,
                    fontSize = 14.sp,
                    color = LMGray500
                )
            } else {
                jogos.forEach {
                    ParticipantGameCard(jogo = it)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun ParticipantGameCard(jogo: Jogo) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, androidx.compose.ui.graphics.Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.SportsSoccer,
                contentDescription = null,
                tint = LMRed,
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
                    text = jogo.estado ?: "Por iniciar",
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }
        }
    }
}