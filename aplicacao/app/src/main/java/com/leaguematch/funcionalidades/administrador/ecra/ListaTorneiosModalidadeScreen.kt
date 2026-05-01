package com.leaguematch.funcionalidades.administrador.ecra

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.ui.theme.LeagueMatchTheme
import com.leaguematch.ui.theme.RedDark
import com.leaguematch.ui.theme.RedPrimary
import com.leaguematch.dados.modelos.Torneio

data class TorneioModalidadeUi(
    val nome: String,
    val equipas: String,
    val estado: String
)

@Composable
fun ListaTorneiosModalidadeScreen(
    modalidade: String = "Futebol",
    torneios: List<Torneio> = listOf(
        Torneio(1, "Carabao CUP", "Futebol", "Liga todos contra todos", "LIGA", "Em Progresso", 16),
        Torneio(2, "Barca CUP", "Futebol", "Eliminatorias simples", "ELIMINATORIAS", "Em Progresso", 16),
        Torneio(3, "MinhoFut", "Futebol", "Fase de grupos", "GRUPOS", "Em Progresso", 8),
        Torneio(4, "Torneio de Futebol", "Futebol", "Inscricao aberta", "LIGA", "Por Iniciar", 12)
    ),
    onBackClick: () -> Unit = {},
    onTorneioClick: (Int) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onUtilizadoresClick: () -> Unit = {},
    onGraficosClick: () -> Unit = {},
    onDefinicoesClick: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            AdminBottomBar(
                selectedItem = "torneios",
                onHomeClick = onHomeClick,
                onUtilizadoresClick = onUtilizadoresClick,
                onGraficosClick = onGraficosClick,
                onDefinicoesClick = onDefinicoesClick
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = RedPrimary
                    )
                }

                Column {
                    Text(
                        text = modalidade,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = "Torneios registados nesta modalidade.",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.horizontalGradient(listOf(RedPrimary, RedDark)))
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Total de torneios",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 14.sp
                        )

                        Text(
                            text = torneios.size.toString(),
                            color = Color.White,
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = modalidade,
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 13.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(52.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Lista de torneios",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            torneios.forEach { torneio ->
                TorneioModalidadeCard(
                    torneio = TorneioModalidadeUi(
                        nome = torneio.nome,
                        equipas = "${torneio.equipas} equipas",
                        estado = torneio.estado
                    ),
                    onClick = { onTorneioClick(torneio.id) }
                )

                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}

@Composable
fun TorneioModalidadeCard(
    torneio: TorneioModalidadeUi,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(98.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Brush.horizontalGradient(listOf(RedPrimary, RedDark))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SportsSoccer,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = torneio.nome,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = torneio.equipas,
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = torneio.estado,
                    color = if (torneio.estado == "Em Progresso") Color(0xFF2E7D32) else Color(0xFFF9A825),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remover",
                    tint = Color(0xFFD32F2F)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaTorneiosModalidadeScreenPreview() {
    LeagueMatchTheme {
        ListaTorneiosModalidadeScreen()
    }
}
