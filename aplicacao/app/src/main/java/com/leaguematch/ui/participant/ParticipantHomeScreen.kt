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
import com.leaguematch.translations.AppStrings
import com.leaguematch.ui.components.ParticipantBottomBar
import com.leaguematch.ui.theme.*
import com.leaguematch.ui.components.TranslatedText

@Composable
fun ParticipantHomeScreen(
    usuarioLogado: Utilizador?,
    selectedItem: String,
    strings: AppStrings,
    primaryColor: Color,
    onTorneiosClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEquipaClick: () -> Unit,
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
            val nome = usuarioLogado?.nome ?: strings.participantFallbackName

            Text(
                text = "${strings.participantGreetingPrefix}, $nome",
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = strings.participantHomeSubtitle,
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500
            )

            Spacer(modifier = Modifier.height(20.dp))

            ParticipantHeroCard(
                strings = strings,
                primaryColor = primaryColor
            )

            Spacer(modifier = Modifier.height(18.dp))

            ParticipantActionCard(
                title = strings.registeredTournaments,
                description = strings.registeredTournamentsDescription,
                icon = Icons.Default.EmojiEvents,
                primaryColor = primaryColor,
                onClick = onTorneiosClick
            )

            ParticipantActionCard(
                title = strings.upcomingGames,
                description = strings.upcomingGamesDescription,
                icon = Icons.Default.SportsSoccer,
                primaryColor = primaryColor,
                onClick = onJogosClick
            )

            ParticipantActionCard(
                title = strings.myTeam,
                description = strings.myTeamDescription,
                icon = Icons.Default.Groups,
                primaryColor = primaryColor,
                onClick = onEquipaClick
            )

            ParticipantActionCard(
                title = strings.statistics,
                description = strings.statisticsDescription,
                icon = Icons.Default.BarChart,
                primaryColor = primaryColor,
                onClick = onEstatisticasClick
            )
        }
    }
}

@Composable
private fun ParticipantHeroCard(
    strings: AppStrings,
    primaryColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        primaryColor,
                        primaryColor.copy(alpha = 0.82f)
                    )
                ),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(18.dp)
    ) {
        Column {
            Text(
                text = strings.participantAreaTitle,
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = LMWhite
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = strings.participantAreaDescription,
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
    primaryColor: Color,
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
                        color = primaryColor.copy(alpha = 0.10f),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = primaryColor,
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