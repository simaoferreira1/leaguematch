package com.leaguematch.ui.participant

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.ui.components.ParticipantBottomBar
import com.leaguematch.ui.theme.*

@Composable
fun ParticipantHomeScreen(
    usuarioLogado: Utilizador?,
    selectedItem: String,
    onTorneiosClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEstatisticasClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
    Scaffold(
        bottomBar = {
            ParticipantBottomBar(
                selectedItem = selectedItem,
                onHomeClick = {},
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
                text = "Olá, ${usuarioLogado?.nome ?: "Participante"}",
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Acompanha os teus torneios, jogos e estatísticas.",
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500
            )

            Spacer(modifier = Modifier.height(20.dp))

            ParticipantHeroCard()

            Spacer(modifier = Modifier.height(18.dp))

            ParticipantActionCard(
                title = "Torneios inscritos",
                description = "Vê os torneios onde estás a participar.",
                icon = Icons.Default.EmojiEvents,
                onClick = onTorneiosClick
            )

            ParticipantActionCard(
                title = "Próximos jogos",
                description = "Consulta calendário, adversários e resultados.",
                icon = Icons.Default.SportsSoccer,
                onClick = onJogosClick
            )

            ParticipantActionCard(
                title = "A minha equipa",
                description = "Vê informações da tua equipa e jogadores.",
                icon = Icons.Default.Groups,
                onClick = onJogosClick
            )

            ParticipantActionCard(
                title = "Estatísticas",
                description = "Consulta golos, jogos e desempenho.",
                icon = Icons.Default.BarChart,
                onClick = onEstatisticasClick
            )
        }
    }
}

@Composable
private fun ParticipantHeroCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    listOf(LMRed, Color(0xFFC41326))
                ),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(18.dp)
    ) {
        Column {
            Text(
                text = "Área do Participante",
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = LMWhite
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Tudo o que precisas para acompanhar a tua participação nos torneios.",
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMWhite.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
private fun ParticipantActionCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        color = Color(0xFFF3F3F5),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = LMRed,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = LMInk
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = description,
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }
        }
    }
}

@Composable
fun ParticipantPlaceholderScreen(
    titulo: String,
    descricao: String,
    selectedItem: String,
    onHomeClick: () -> Unit,
    onTorneiosClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEstatisticasClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
    Scaffold(
        bottomBar = {
            ParticipantBottomBar(
                selectedItem = selectedItem,
                onHomeClick = onHomeClick,
                onTorneiosClick = onTorneiosClick,
                onJogosClick = onJogosClick,
                onEstatisticasClick = onEstatisticasClick,
                onPerfilClick = onPerfilClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(22.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = titulo,
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = LMInk
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = descricao,
                    fontFamily = Geist,
                    fontSize = 14.sp,
                    color = LMGray500
                )
            }
        }
    }
}