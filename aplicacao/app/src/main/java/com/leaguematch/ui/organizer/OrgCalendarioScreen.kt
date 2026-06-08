package com.leaguematch.ui.organizer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.ui.theme.*

@Composable
fun OrgCalendarioScreen(
    torneio: Torneio,
    jogos: List<Jogo>,
    onBackClick: () -> Unit
) {
    val agrupados = jogos
        .groupBy { it.data.ifBlank { "Sem data" } }
        .toSortedMap(comparator = compareBy {
            if (it == "Sem data") "9999-99-99" else it
        })

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
                .padding(bottom = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = null, tint = LMInk)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Calendário",
                        fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp, color = LMInk
                    )
                    Text(text = torneio.nome, fontFamily = Geist, fontSize = 12.sp, color = LMGray500)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (agrupados.isEmpty()) {
                EmptyCalendarState()
            } else {
                agrupados.forEach { (data, jogosDoDia) ->
                    DiaSection(data = data, jogos = jogosDoDia)
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }
    }
}

@Composable
private fun DiaSection(data: String, jogos: List<Jogo>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(Color(0xFFFFEDD5), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = Color(0xFFEA580C),
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = data,
            fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp, color = LMInk
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "· ${jogos.size} ${if (jogos.size == 1) "jogo" else "jogos"}",
            fontFamily = Geist, fontSize = 12.sp, color = LMGray500
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        jogos.sortedBy { it.hora }.forEach { jogo ->
            JogoCalendarioCard(jogo)
        }
    }
}

@Composable
private fun JogoCalendarioCard(jogo: Jogo) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, LMBorder),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = jogo.hora.ifBlank { "--:--" },
                fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp, color = LMInk,
                modifier = Modifier.width(54.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(LMGray100, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.SportsSoccer, contentDescription = null, tint = LMRed, modifier = Modifier.size(18.dp))
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${jogo.casa} vs ${jogo.fora}",
                    fontFamily = Geist, fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp, color = LMInk
                )
                Text(
                    text = jogo.estado,
                    fontFamily = Geist, fontSize = 11.sp, color = LMGray500
                )
            }

            if (jogo.estado.equals("Finalizado", ignoreCase = true) ||
                jogo.estado.equals("A Decorrer", ignoreCase = true)) {
                Text(
                    text = "${jogo.resultadoCasa}-${jogo.resultadoFora}",
                    fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp, color = LMInk
                )
            }
        }
    }
}

@Composable
private fun EmptyCalendarState() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = LMGray50,
        border = BorderStroke(1.dp, LMBorder)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.CalendarMonth, contentDescription = null,
                tint = LMGray300, modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Sem jogos agendados.", fontFamily = Geist, fontSize = 13.sp, color = LMGray500)
        }
    }
}
