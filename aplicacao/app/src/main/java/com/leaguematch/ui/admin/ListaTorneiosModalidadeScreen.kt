package com.leaguematch.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.ui.components.*
import com.leaguematch.ui.theme.*
import com.leaguematch.data.remote.model.Torneio

@Composable
fun ListaTorneiosModalidadeScreen(
    modalidade: String = "Futebol",
    torneios: List<Torneio> = listOf(
        Torneio(1, "Carabao CUP", "Futebol", "Liga todos contra todos", "LIGA", "Em Progresso", 16),
        Torneio(2, "Barca CUP", "Futebol", "Eliminatorias simples", "ELIMINATORIAS", "Em Progresso", 16),
        Torneio(3, "MinhoFut Cup", "Futebol", "Fase de grupos", "GRUPOS", "Em Progresso", 8),
        Torneio(4, "Liga do Vinho", "Futebol", "Inscricao aberta", "LIGA", "Por Iniciar", 16),
        Torneio(5, "Norte Open", "Ténis", "Inscricao aberta", "LIGA", "Por Iniciar", 12)
    ),
    onBackClick: () -> Unit = {},
    onTorneioClick: (Int) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onUtilizadoresClick: () -> Unit = {},
    onGraficosClick: () -> Unit = {},
    onDefinicoesClick: () -> Unit = {},
    onRemoveTorneioClick: (Int) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedModalidadeTab by remember { mutableStateOf(modalidade) }

    // Filter tournaments based on search and selected sport modality tab
    val filteredTorneios = torneios.filter { t ->
        val matchesSearch = t.nome.contains(searchQuery, ignoreCase = true)
        val matchesTab = t.modalidade.lowercase() == selectedModalidadeTab.lowercase()
        matchesSearch && matchesTab
    }

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
                back = true,
                onBackClick = onBackClick,
                sub = "Moderação global"
            )

            // Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                LeagueMatchTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = "Procurar torneio...",
                    icon = Icons.Default.Search
                )
            }

            // SportTabs horizontal scroll
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val sportTabs = listOf("Futebol", "Ténis", "Padel", "Basquetebol", "Andebol")
                
                sportTabs.forEach { sport ->
                    val isSelected = selectedModalidadeTab.lowercase() == sport.lowercase()
                    Box(
                        modifier = Modifier
                            .background(
                                if (isSelected) LMInk else LMWhite,
                                shape = RoundedCornerShape(99.dp)
                            )
                            .let {
                                if (isSelected) it else it.border(
                                    BorderStroke(1.dp, LMBorder),
                                    RoundedCornerShape(99.dp)
                                )
                            }
                            .clickable { selectedModalidadeTab = sport }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = sport,
                            fontFamily = Geist,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) LMWhite else LMGray700
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Tournaments List Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, end = 18.dp, bottom = 18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (filteredTorneios.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nenhum torneio encontrado",
                            fontFamily = Geist,
                            fontSize = 13.sp,
                            color = LMGray500
                        )
                    }
                }

                filteredTorneios.forEach { t ->
                    // Set gradients depending on tournament name (as defined in screens-admin.jsx)
                    val gradient = when {
                        t.nome.contains("Carabao", ignoreCase = true) -> listOf(LMRed, LMRed700)
                        t.nome.contains("Minho", ignoreCase = true) -> listOf(Color(0xFF166534), Color(0xFF22C55E))
                        t.nome.contains("Vinho", ignoreCase = true) -> listOf(Color(0xFF9F1239), Color(0xFFBE123C))
                        t.nome.contains("Norte", ignoreCase = true) -> listOf(Color(0xFF1E3A8A), Color(0xFF2563EB))
                        else -> listOf(LMInk, LMGray700)
                    }
                    
                    // Set reports warning alert simulating JSX mockups
                    val reports = when {
                        t.nome.contains("Barca", ignoreCase = true) -> 1
                        t.nome.contains("Norte", ignoreCase = true) -> 2
                        else -> 0
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LMWhite, shape = RoundedCornerShape(16.dp))
                            .border(
                                BorderStroke(
                                    1.dp,
                                    if (reports > 0) LMRed100 else LMBorder
                                ),
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { onTorneioClick(t.id) }
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Gradient box containing trophy icon
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(brush = Brush.linearGradient(colors = gradient)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = "Trophy Icon",
                                    tint = LMWhite,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = t.modalidade.uppercase(),
                                        fontFamily = Geist,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        color = LMGray500,
                                        letterSpacing = 0.4.sp
                                    )
                                    
                                    if (reports > 0) {
                                        Pill(
                                            text = "$reports alerta${if (reports > 1) "s" else ""}",
                                            kind = "red"
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(2.dp))
                                
                                Text(
                                    text = t.nome,
                                    fontFamily = Bricolage,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = LMInk,
                                    letterSpacing = (-0.2).sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                
                                Spacer(modifier = Modifier.height(6.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${t.equipas} equipas",
                                        fontFamily = Geist,
                                        fontSize = 11.sp,
                                        color = LMGray500
                                    )
                                    
                                    // Remove capsule button
                                    Box(
                                        modifier = Modifier
                                            .background(LMRed50, CircleShape)
                                            .clickable { onRemoveTorneioClick(t.id) }
                                            .padding(horizontal = 10.dp, vertical = 5.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Trash Icon",
                                                tint = LMRed,
                                                modifier = Modifier.size(11.dp)
                                            )
                                            Text(
                                                text = "Remover",
                                                fontFamily = Geist,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp,
                                                color = LMRed700
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
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
