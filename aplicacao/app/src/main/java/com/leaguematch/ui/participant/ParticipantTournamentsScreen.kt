package com.leaguematch.ui.participant

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.ui.components.ParticipantBottomBar
import com.leaguematch.ui.theme.*

@Composable
fun ParticipantTournamentsScreen(
    torneios: List<Torneio>,
    onTournamentClick: (Int) -> Unit,
    onHomeClick: () -> Unit,
    onTorneiosClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEquipaClick: () -> Unit,
    onEstatisticasClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
    var pesquisa by remember { mutableStateOf("") }

    val torneiosFiltrados = torneios.filter {
        pesquisa.isBlank() ||
                it.nome.contains(pesquisa, ignoreCase = true) ||
                it.modalidade.contains(pesquisa, ignoreCase = true) ||
                it.estado.contains(pesquisa, ignoreCase = true)
    }

    Scaffold(
        bottomBar = {
            ParticipantBottomBar(
                selectedItem = "torneios",
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
                text = "Torneios inscritos",
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${torneios.size} torneios associados à tua conta",
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF3F3F5), RoundedCornerShape(14.dp))
                    .padding(horizontal = 12.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = LMGray500,
                    modifier = Modifier.size(20.dp)
                )

                TextField(
                    value = pesquisa,
                    onValueChange = { pesquisa = it },
                    placeholder = {
                        Text(
                            text = "Pesquisar torneios...",
                            fontFamily = Geist,
                            fontSize = 13.sp,
                            color = LMGray500
                        )
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (torneiosFiltrados.isEmpty()) {
                Text(
                    text = "Ainda não estás inscrito em nenhum torneio.",
                    fontFamily = Geist,
                    fontSize = 14.sp,
                    color = LMGray500
                )
            } else {
                torneiosFiltrados.forEachIndexed { index, torneio ->
                    ParticipantTournamentCard(
                        torneio = torneio,
                        index = index,
                        onClick = {
                            onTournamentClick(torneio.id)
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun ParticipantTournamentCard(
    torneio: Torneio,
    index: Int,
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
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(LMRed, Color(0xFFC41326))
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = LMWhite,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = torneio.modalidade.uppercase(),
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = LMGray500
                )

                Text(
                    text = torneio.nome,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = LMInk,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${torneio.equipas} equipas",
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }

            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFFEFFBF3)
            ) {
                Text(
                    text = torneio.estado,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = Color(0xFF15803D)
                )
            }
        }
    }
}