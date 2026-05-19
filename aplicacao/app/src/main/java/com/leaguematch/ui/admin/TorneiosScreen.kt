package com.leaguematch.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.SportsHandball
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.ui.components.*
import com.leaguematch.ui.theme.*
import com.leaguematch.data.remote.model.ResumoModalidade

data class ModalidadeUi(
    val nome: String,
    val torneios: String,
    val icon: ImageVector
)

@Composable
fun TorneiosScreen(
    modalidades: List<ResumoModalidade> = listOf(
        ResumoModalidade("Futebol", 5),
        ResumoModalidade("Padel", 2),
        ResumoModalidade("Ténis", 3),
        ResumoModalidade("Basquetebol", 1),
        ResumoModalidade("Andebol", 1)
    ),
    totalTorneios: Int = 12,
    onHomeClick: () -> Unit = {},
    onUtilizadoresClick: () -> Unit = {},
    onGraficosClick: () -> Unit = {},
    onDefinicoesClick: () -> Unit = {},
    onModalidadeClick: (String) -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            AdminBottomBar(
                selectedItem = "torneios",
                onHomeClick = onHomeClick,
                onUtilizadoresClick = onUtilizadoresClick,
                onTorneiosClick = {},
                onGraficosClick = onGraficosClick,
                onDefinicoesClick = onDefinicoesClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 12.dp)
        ) {
            // Header TopBar
            TopBar(
                title = "Torneios",
                big = true,
                sub = "Moderação global por modalidade"
            )

            // Total modalities premium gradient card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(LMRed, LMRed700)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "TOTAL DE MODALIDADES",
                            color = LMWhite.copy(alpha = 0.65f),
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 0.4.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = modalidades.size.toString(),
                            color = LMWhite,
                            fontFamily = Bricolage,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$totalTorneios torneios registados",
                            color = LMWhite.copy(alpha = 0.8f),
                            fontFamily = Geist,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = LMWhite.copy(alpha = 0.25f),
                        modifier = Modifier.size(54.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Section Title
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "SELECIONE UMA MODALIDADE",
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = LMGray500,
                    letterSpacing = 0.4.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    modalidades.forEach { modalidade ->
                        val ui = modalidade.toUi()
                        
                        CardWrapper(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onModalidadeClick(modalidade.nome) },
                            pad = 12.dp
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Mini Sport Crest
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = when (modalidade.nome) {
                                                    "Futebol" -> listOf(LMRed, LMRed700)
                                                    "Ténis" -> listOf(LMInk, LMInk3)
                                                    "Basquetebol" -> listOf(Color(0xFF2563EB), Color(0xFF1D4ED8))
                                                    "Andebol" -> listOf(Color(0xFF16A34A), Color(0xFF15803D))
                                                    else -> listOf(LMWarn, Color(0xFFD97706))
                                                }
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = ui.icon,
                                        contentDescription = null,
                                        tint = LMWhite,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = ui.nome,
                                        fontFamily = Geist,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = LMInk
                                    )
                                    Spacer(modifier = Modifier.height(1.dp))
                                    Text(
                                        text = ui.torneios,
                                        fontFamily = Geist,
                                        fontSize = 12.sp,
                                        color = LMGray500
                                    )
                                }
                                
                                Text(
                                    text = "Consultar ›",
                                    fontFamily = Geist,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = LMRed
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun ResumoModalidade.toUi(): ModalidadeUi {
    val textoTorneios = if (totalTorneios == 1) "1 torneio ativo" else "$totalTorneios torneios ativos"
    return ModalidadeUi(nome, textoTorneios, iconForModalidade(nome))
}

private fun iconForModalidade(modalidade: String): ImageVector {
    return when (modalidade) {
        "Futebol" -> Icons.Default.SportsSoccer
        "Basquetebol" -> Icons.Default.SportsBasketball
        "Andebol" -> Icons.Default.SportsHandball
        else -> Icons.Default.SportsTennis
    }
}

@Preview(showBackground = true)
@Composable
fun TorneiosScreenPreview() {
    LeagueMatchTheme {
        TorneiosScreen()
    }
}
