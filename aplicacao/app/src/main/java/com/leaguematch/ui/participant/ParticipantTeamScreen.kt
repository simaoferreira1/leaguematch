package com.leaguematch.ui.participant

import androidx.compose.foundation.BorderStroke
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
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.ui.components.ParticipantBottomBar
import com.leaguematch.ui.theme.*

@Composable
fun ParticipantTeamScreen(
    equipa: Equipa?,
    jogadores: List<Utilizador>,
    classificacao: Classificacao?,
    jogos: List<Jogo>,
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
                text = "A minha equipa",
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Consulta jogadores, classificação e últimos jogos.",
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (equipa == null) {
                TeamInfoCard(
                    title = "Sem equipa associada",
                    value = "Ainda não foste associado a uma equipa.",
                    icon = Icons.Default.Groups
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onJoinTeamClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LMRed)
                ) {
                    Text(
                        text = "Integrar equipa",
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
                color = Color(0xFFFFEAEC)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Código de equipa",
                        fontFamily = Geist,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = LMRed
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = com.leaguematch.data.remote.model.TeamCode.encode(equipa.id),
                        fontFamily = Bricolage,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = LMInk
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            TeamInfoCard(
                title = "Nome da equipa",
                value = equipa.nome,
                icon = Icons.Default.Groups
            )

            Spacer(modifier = Modifier.height(10.dp))

            TeamInfoCard(
                title = "Jogadores",
                value = "${jogadores.size} jogadores na equipa",
                icon = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(10.dp))

            TeamInfoCard(
                title = "Classificação",
                value = if (classificacao != null) {
                    "${classificacao.pontos} pts • ${classificacao.vitorias}V ${classificacao.empates}E ${classificacao.derrotas}D"
                } else {
                    "Ainda sem classificação"
                },
                icon = Icons.Default.EmojiEvents
            )

            Spacer(modifier = Modifier.height(10.dp))

            TeamInfoCard(
                title = "Últimos jogos",
                value = if (ultimosJogos.isEmpty()) {
                    "Ainda não existem jogos registados"
                } else {
                    "${ultimosJogos.size} jogos encontrados"
                },
                icon = Icons.Default.SportsSoccer
            )

            if (jogadores.isNotEmpty()) {
                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = "Jogadores",
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 21.sp,
                    color = LMInk
                )

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

                Text(
                    text = "Últimos jogos",
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 21.sp,
                    color = LMInk
                )

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
private fun TeamInfoCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
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
                imageVector = icon,
                contentDescription = null,
                tint = LMRed,
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
        border = BorderStroke(1.dp, androidx.compose.ui.graphics.Color(0xFFE5E5EA)),
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