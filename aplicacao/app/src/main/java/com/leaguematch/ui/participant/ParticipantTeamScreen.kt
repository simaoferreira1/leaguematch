package com.leaguematch.ui.participant

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.Classificacao
import com.leaguematch.data.remote.model.Equipa
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.data.remote.model.TeamCode
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.translations.AppStrings
import com.leaguematch.ui.components.ParticipantBottomBar
import com.leaguematch.ui.theme.*

@Composable
fun ParticipantTeamScreen(
    equipa: Equipa?,
    jogadores: List<Utilizador>,
    classificacao: Classificacao?,
    jogos: List<Jogo>,
    strings: AppStrings,
    primaryColor: Color,
    onJoinTeamClick: () -> Unit,
    onHomeClick: () -> Unit,
    onTorneiosClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEquipaClick: () -> Unit,
    onEstatisticasClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
    val ultimosJogos = jogos.take(3)

    Scaffold(
        bottomBar = {
            ParticipantBottomBar(
                selectedItem = "equipa",
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
                text = strings.myTeamTitle,
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = strings.myTeamSubtitle,
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (equipa == null) {
                TeamInfoCard(
                    title = strings.noTeamTitle,
                    value = strings.noTeamDescription,
                    icon = Icons.Default.Groups,
                    primaryColor = primaryColor
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onJoinTeamClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text(
                        text = strings.joinTeamButton,
                        fontFamily = Geist,
                        fontWeight = FontWeight.ExtraBold,
                        color = LMWhite
                    )
                }

                return@Column
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = primaryColor.copy(alpha = 0.10f)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = strings.teamCode,
                        fontFamily = Geist,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = TeamCode.encode(equipa.id),
                        fontFamily = Bricolage,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = LMInk
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            TeamInfoCard(
                title = strings.teamName,
                value = equipa.nome,
                icon = Icons.Default.Groups,
                primaryColor = primaryColor
            )

            Spacer(modifier = Modifier.height(10.dp))

            TeamInfoCard(
                title = strings.playersTitle,
                value = strings.playersCount(jogadores.size),
                icon = Icons.Default.Person,
                primaryColor = primaryColor
            )

            Spacer(modifier = Modifier.height(10.dp))

            TeamInfoCard(
                title = strings.teamStandingTitle,
                value = if (classificacao != null) {
                    "${strings.pointsLabel(classificacao.pontos)} • ${
                        strings.classificationRecord(
                            classificacao.vitorias,
                            classificacao.empates,
                            classificacao.derrotas
                        )
                    }"
                } else {
                    strings.noStandingYet
                },
                icon = Icons.Default.EmojiEvents,
                primaryColor = primaryColor
            )

            Spacer(modifier = Modifier.height(10.dp))

            TeamInfoCard(
                title = strings.lastGamesTitle,
                value = if (ultimosJogos.isEmpty()) {
                    strings.noRegisteredGames
                } else {
                    strings.gamesFound(ultimosJogos.size)
                },
                icon = Icons.Default.SportsSoccer,
                primaryColor = primaryColor
            )

            if (jogadores.isNotEmpty()) {
                Spacer(modifier = Modifier.height(22.dp))

                SectionTitle(strings.playersTitle)

                Spacer(modifier = Modifier.height(10.dp))

                jogadores.forEach { jogador ->
                    SimpleListCard(
                        title = jogador.nome,
                        subtitle = jogador.email
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            if (ultimosJogos.isNotEmpty()) {
                Spacer(modifier = Modifier.height(22.dp))

                SectionTitle(strings.lastGamesTitle)

                Spacer(modifier = Modifier.height(10.dp))

                ultimosJogos.forEach { jogo ->
                    SimpleListCard(
                        title = "${jogo.casa} vs ${jogo.fora}",
                        subtitle = "${jogo.estado} • ${jogo.resultadoCasa}-${jogo.resultadoFora}"
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontFamily = Bricolage,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 21.sp,
        color = LMInk
    )
}

@Composable
private fun TeamInfoCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    primaryColor: Color
) {
    Surface(
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
                imageVector = icon,
                contentDescription = null,
                tint = primaryColor,
                modifier = Modifier.size(32.dp)
            )

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
                    text = value,
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }
        }
    }
}

@Composable
private fun SimpleListCard(
    title: String,
    subtitle: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                text = title,
                fontFamily = Geist,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = subtitle,
                fontFamily = Geist,
                fontSize = 12.sp,
                color = LMGray500
            )
        }
    }
}
