package com.leaguematch.ui.spectator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leaguematch.data.remote.model.Equipa
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.ui.components.SpectatorBottomBar
import com.leaguematch.ui.theme.RedDark
import com.leaguematch.ui.theme.RedPrimary

@Composable
fun EquipasScreen(
    torneio: Torneio,
    equipas: List<Equipa>,
    onHomeClick: () -> Unit,
    onClassificacaoClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEquipasClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
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
        containerColor = Color(0xFFF6F6F8)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            TorneioHeaderEquipas(torneio)

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Equipas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = RedDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (equipas.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B20))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Groups,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Ainda não existem equipas.",
                            color = Color.White.copy(alpha = 0.65f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 12.dp)
                ) {
                    itemsIndexed(equipas) { index, equipa ->
                        EquipaCard(posicao = index + 1, equipa = equipa)
                    }
                }
            }
        }
    }
}

@Composable
private fun EquipaCard(posicao: Int, equipa: Equipa) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B20)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(RedPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = equipa.nome.take(2).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = equipa.nome,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "#$posicao",
                color = Color.White.copy(alpha = 0.45f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun TorneioHeaderEquipas(torneio: Torneio) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(RedPrimary, RedDark, Color(0xFF17171C))))
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(torneio.nome, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("${torneio.equipas} equipas • ${torneio.modalidade}", color = Color.White.copy(alpha = 0.82f), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
