package com.leaguematch.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.ui.components.AdminBottomBar
import com.leaguematch.ui.components.LeagueMatchTextField
import com.leaguematch.ui.components.Pill
import com.leaguematch.ui.components.TopBar
import com.leaguematch.ui.components.TranslatedText
import com.leaguematch.ui.theme.Bricolage
import com.leaguematch.ui.theme.Geist
import com.leaguematch.ui.theme.LMBorder
import com.leaguematch.ui.theme.LMGray500
import com.leaguematch.ui.theme.LMGray700
import com.leaguematch.ui.theme.LMInk
import com.leaguematch.ui.theme.LMRed
import com.leaguematch.ui.theme.LMRed100
import com.leaguematch.ui.theme.LMRed50
import com.leaguematch.ui.theme.LMRed700
import com.leaguematch.ui.theme.LMWhite
import com.leaguematch.ui.theme.LeagueMatchTheme

// Ecrã que apresenta a lista de torneios filtrados por modalidade
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
    // Guarda o texto introduzido na barra de pesquisa
    var searchQuery by remember { mutableStateOf("") }
    // Guarda a modalidade atualmente selecionada nos separadores
    var selectedModalidadeTab by remember { mutableStateOf(modalidade) }

    // Filtra os torneios com base na pesquisa e na modalidade selecionada
    val filteredTorneios = torneios.filter { t ->
        // Verifica se o nome do torneio contém o texto pesquisado
        val matchesSearch = t.nome.contains(searchQuery, ignoreCase = true)
        // Verifica se a modalidade corresponde à aba selecionada
        val matchesTab = t.modalidade.lowercase() == selectedModalidadeTab.lowercase()
        matchesSearch && matchesTab
    }

    // Estrutura principal do ecrã com barra inferior de navegação
    Scaffold(
        // Barra de navegação do administrador
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
        // Conteúdo principal do ecrã
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 12.dp)
        ) {
            // Cabeçalho da página
            TopBar(
                title = "Torneios",
                big = true,
                back = true,
                onBackClick = onBackClick,
                sub = "Moderação global"
            )

            // Campo de pesquisa de torneios
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
                // Lista de modalidades disponíveis para filtrar torneios
                val sportTabs = listOf("Futebol", "Ténis", "Padel", "Basquetebol", "Andebol")
                
                sportTabs.forEach { sport ->
                    // Verifica se esta modalidade está atualmente selecionada
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
                            // Seleciona a modalidade quando o utilizador clica
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

            // Secção que apresenta os torneios encontrados
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, end = 18.dp, bottom = 18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Mensagem apresentada quando não existem torneios para mostrar
                if (filteredTorneios.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        TranslatedText(
                            text = "Nenhum torneio encontrado",
                            fontFamily = Geist,
                            fontSize = 13.sp,
                            color = LMGray500
                        )
                    }
                }

                // Percorre todos os torneios filtrados
                filteredTorneios.forEach { t ->
                    // Define as cores do cartão consoante o nome do torneio
                    val gradient = when {
                        t.nome.contains("Carabao", ignoreCase = true) -> listOf(LMRed, LMRed700)
                        t.nome.contains("Minho", ignoreCase = true) -> listOf(Color(0xFF166534), Color(0xFF22C55E))
                        t.nome.contains("Vinho", ignoreCase = true) -> listOf(Color(0xFF9F1239), Color(0xFFBE123C))
                        t.nome.contains("Norte", ignoreCase = true) -> listOf(Color(0xFF1E3A8A), Color(0xFF2563EB))
                        else -> listOf(LMInk, LMGray700)
                    }

                    // Simulação do número de alertas associados ao torneio
                    val reports = when {
                        t.nome.contains("Barca", ignoreCase = true) -> 1
                        t.nome.contains("Norte", ignoreCase = true) -> 2
                        else -> 0
                    }

                    // Cartão com informação resumida do torneio
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
                            // Abre os detalhes do torneio ao clicar
                            .clickable { onTorneioClick(t.id) }
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Área gráfica que identifica visualmente o torneio
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(brush = Brush.linearGradient(colors = gradient)),
                                contentAlignment = Alignment.Center
                            ) {
                                // Ícone representativo de torneios
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
                                    // Mostra a modalidade do torneio
                                    Text(
                                        text = t.modalidade.uppercase(),
                                        fontFamily = Geist,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        color = LMGray500,
                                        letterSpacing = 0.4.sp
                                    )

                                    // Mostra aviso caso existam alertas associados ao torneio
                                    if (reports > 0) {
                                        Pill(
                                            text = "$reports alerta${if (reports > 1) "s" else ""}",
                                            kind = "red"
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(2.dp))

                                // Nome do torneio
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
                                    // Número de equipas inscritas no torneio
                                    TranslatedText(
                                        text = "${t.equipas} equipas",
                                        fontFamily = Geist,
                                        fontSize = 11.sp,
                                        color = LMGray500
                                    )

                                    // Botão para remover o torneio
                                    Box(
                                        modifier = Modifier
                                            .background(LMRed50, CircleShape)
                                            // Executa a remoção do torneio selecionado
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
                                            TranslatedText(
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
