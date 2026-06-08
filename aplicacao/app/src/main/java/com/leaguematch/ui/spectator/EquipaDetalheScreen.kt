package com.leaguematch.ui.spectator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.Equipa
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.ui.components.SpectatorBottomBar
import com.leaguematch.ui.theme.*

@Composable
fun EquipaDetalheScreen(
    torneio: Torneio,
    equipa: Equipa,
    jogos: List<Jogo>,
    onBackClick: () -> Unit,
    onCalendarioClick: () -> Unit,
    onHomeClick: () -> Unit,
    onClassificacaoClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEquipasClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
    val jogosDaEquipa = jogos.filter {
        it.casa.equals(equipa.nome, ignoreCase = true) ||
                it.fora.equals(equipa.nome, ignoreCase = true)
    }

    Scaffold(
        bottomBar = {
            SpectatorBottomBar(
                selectedItem = "equipas",
                onHomeClick = onHomeClick,
                onClassificacaoClick = onClassificacaoClick,
                onJogosClick = onJogosClick,
                onEquipasClick = onEquipasClick,
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
                .padding(horizontal = 18.dp)
                .padding(top = 26.dp, bottom = 90.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onBackClick() },
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF3F3F5)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = LMInk,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Detalhe da equipa",
                        fontFamily = Bricolage,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 26.sp,
                        color = LMInk
                    )

                    Text(
                        text = torneio.nome,
                        fontFamily = Geist,
                        fontSize = 13.sp,
                        color = LMGray500,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = LMRed
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(62.dp)
                            .background(
                                color = LMWhite.copy(alpha = 0.18f),
                                shape = RoundedCornerShape(18.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = null,
                            tint = LMWhite,
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = equipa.nome,
                        fontFamily = Bricolage,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp,
                        color = LMWhite,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${torneio.modalidade} · ${jogosDaEquipa.size} jogos",
                        fontFamily = Geist,
                        fontSize = 13.sp,
                        color = LMWhite.copy(alpha = 0.82f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Menu da equipa",
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 21.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(10.dp))

            MenuEquipaCard(
                title = "Calendário da equipa",
                subtitle = "Consulta os próximos jogos desta equipa",
                icon = Icons.Default.CalendarMonth,
                onClick = onCalendarioClick
            )

            Spacer(modifier = Modifier.height(10.dp))

            MenuEquipaCard(
                title = "Jogos da equipa",
                subtitle = if (jogosDaEquipa.isEmpty()) {
                    "Ainda não existem jogos registados"
                } else {
                    "${jogosDaEquipa.size} jogos encontrados"
                },
                icon = Icons.Default.SportsSoccer,
                onClick = onJogosClick
            )

            Spacer(modifier = Modifier.height(10.dp))

            MenuEquipaCard(
                title = "Classificação",
                subtitle = "Ver posição da equipa no torneio",
                icon = Icons.Default.EmojiEvents,
                onClick = onClassificacaoClick
            )

            if (jogosDaEquipa.isNotEmpty()) {
                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = "Últimos jogos",
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 21.sp,
                    color = LMInk
                )

                Spacer(modifier = Modifier.height(10.dp))

                jogosDaEquipa.take(3).forEach { jogo ->
                    JogoEquipaCard(jogo = jogo)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun MenuEquipaCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        color = LMRed.copy(alpha = 0.10f),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = LMRed,
                    modifier = Modifier.size(25.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
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

            Text(
                text = "›",
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 26.sp,
                color = LMGray500
            )
        }
    }
}

@Composable
private fun JogoEquipaCard(
    jogo: Jogo
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
                text = "${jogo.casa} vs ${jogo.fora}",
                fontFamily = Geist,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = LMInk,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${jogo.estado} · ${jogo.resultadoCasa}-${jogo.resultadoFora}",
                fontFamily = Geist,
                fontSize = 12.sp,
                color = LMGray500
            )
        }
    }
}