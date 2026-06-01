package com.leaguematch.ui.spectator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.ui.theme.RedDark
import com.leaguematch.ui.theme.RedPrimary

data class ClassificacaoItem(
    val nomeEquipa: String,
    val pontos: Int,
    val jogos: Int,
    val vitorias: Int,
    val empates: Int,
    val derrotas: Int,
    val golosMarcados: Int,
    val golosSofridos: Int
) {
    val diferencaGolos: Int
        get() = golosMarcados - golosSofridos
}

@Composable
fun ClassificacaoScreen(
    torneio: Torneio,
    classificacao: List<ClassificacaoItem>,
    onBackClick: (() -> Unit)? = null,
    bottomBar: @Composable () -> Unit = {}
) {
    Scaffold(
        bottomBar = bottomBar,
        containerColor = Color(0xFFF6F6F8)
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            TorneioHeader(torneio, onBackClick)

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Classificação",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = RedDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            ClassificacaoTableHeader()

            Spacer(modifier = Modifier.height(8.dp))

            if (classificacao.isEmpty()) {
                Text(
                    text = "Ainda não existem dados de classificação.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 12.dp)
                ) {
                    itemsIndexed(classificacao) { index, item ->
                        ClassificacaoRow(
                            posicao = index + 1,
                            item = item
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TorneioHeader(
    torneio: Torneio,
    onBackClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(RedPrimary, RedDark, Color(0xFF17171C))
                    )
                )
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBackClick != null) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = torneio.nome,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "${torneio.modalidade} • ${torneio.estado}",
                    color = Color.White.copy(alpha = 0.82f),
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Participantes: ${torneio.equipas} equipas",
                    color = Color.White.copy(alpha = 0.82f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun ClassificacaoTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Pos", modifier = Modifier.width(36.dp), style = MaterialTheme.typography.labelSmall)
        Text("Equipa", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall)

        HeaderCell("J")
        HeaderCell("V")
        HeaderCell("E")
        HeaderCell("D")
        HeaderCell("DG")
        HeaderCell("Pts", width = 38.dp)
    }
}

@Composable
private fun HeaderCell(
    text: String,
    width: androidx.compose.ui.unit.Dp = 28.dp
) {
    Text(
        text = text,
        modifier = Modifier.width(width),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF6B6B72)
    )
}

@Composable
private fun ClassificacaoRow(
    posicao: Int,
    item: ClassificacaoItem
) {
    val isTopThree = posicao <= 3
    val backgroundColor = if (isTopThree) Color.White else Color(0xFF1B1B20)
    val textColor = if (isTopThree) Color(0xFF17171C) else Color.White
    val subtitleColor = if (isTopThree) Color(0xFF6E6E76) else Color.White.copy(alpha = 0.72f)
    val positionColor = if (isTopThree) RedPrimary else Color(0xFF2A2A31)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isTopThree) 4.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(positionColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = posicao.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.nomeEquipa,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "${item.golosMarcados} GM • ${item.golosSofridos} GS",
                    color = subtitleColor,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            StatCell(item.jogos.toString(), textColor)
            StatCell(item.vitorias.toString(), textColor)
            StatCell(item.empates.toString(), textColor)
            StatCell(item.derrotas.toString(), textColor)

            StatCell(
                text = if (item.diferencaGolos > 0) "+${item.diferencaGolos}" else item.diferencaGolos.toString(),
                color = textColor
            )

            Box(
                modifier = Modifier
                    .width(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isTopThree) RedPrimary else Color.White.copy(alpha = 0.12f))
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.pontos.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun StatCell(
    text: String,
    color: Color
) {
    Text(
        text = text,
        modifier = Modifier.width(28.dp),
        color = color,
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.bodySmall
    )
}