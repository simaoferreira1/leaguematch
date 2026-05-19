package com.leaguematch.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.ui.components.*
import com.leaguematch.ui.theme.*
import com.leaguematch.data.remote.model.TipoUtilizador
import com.leaguematch.data.remote.model.Utilizador

@Composable
fun UtilizadoresScreen(
    utilizadores: List<Utilizador> = listOf(
        Utilizador(1, "Rúben Miguel Gomes Ferreira", "ruben@leaguematch.com", TipoUtilizador.ORGANIZADOR, true, 1, 3, 9),
        Utilizador(2, "Diogo Gomes", "diogo@leaguematch.com", TipoUtilizador.PARTICIPANTE, true, 2, 4, 12),
        Utilizador(3, "João Fernandes", "joao@leaguematch.com", TipoUtilizador.PARTICIPANTE, false, 1, 2, 7),
        Utilizador(4, "Simão Rodrigues Ferreira", "fsimao530@gmail.com", TipoUtilizador.ORGANIZADOR, false, 2, 4, 12),
        Utilizador(5, "Ricardo Castro", "rcastro@leaguematch.com", TipoUtilizador.ADMIN, true, 0, 0, 0)
    ),
    onUtilizadorClick: (Int) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onTorneiosClick: () -> Unit = {},
    onGraficosClick: () -> Unit = {},
    onDefinicoesClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todos") }

    // Count statistics
    val totalCount = utilizadores.size
    val adminCount = utilizadores.count { it.tipo == TipoUtilizador.ADMIN }
    val organizerCount = utilizadores.count { it.tipo == TipoUtilizador.ORGANIZADOR }
    val participantCount = utilizadores.count { it.tipo == TipoUtilizador.PARTICIPANTE }
    val spectatorCount = utilizadores.count { it.tipo == TipoUtilizador.ESPECTADOR }

    // Filtering logic
    val filteredUtilizadores = utilizadores.filter { user ->
        val matchesSearch = user.nome.contains(searchQuery, ignoreCase = true) || 
                            user.email.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (selectedFilter) {
            "Admin" -> user.tipo == TipoUtilizador.ADMIN
            "Organizador" -> user.tipo == TipoUtilizador.ORGANIZADOR
            "Participante" -> user.tipo == TipoUtilizador.PARTICIPANTE
            "Espectador" -> user.tipo == TipoUtilizador.ESPECTADOR
            else -> true
        }
        matchesSearch && matchesFilter
    }

    Scaffold(
        bottomBar = {
            AdminBottomBar(
                selectedItem = "utilizadores",
                onHomeClick = onHomeClick,
                onUtilizadoresClick = {},
                onTorneiosClick = onTorneiosClick,
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
                title = "Utilizadores",
                big = true,
                sub = "$totalCount contas registadas",
                rightContent = {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(LMInk, RoundedCornerShape(10.dp))
                            .clickable {},
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Adicionar Utilizador",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
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
                    placeholder = "Procurar utilizador...",
                    icon = Icons.Default.Search
                )
            }

            // Role Filter Chips Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val filters = listOf(
                    "Todos" to "Todos · $totalCount",
                    "Admin" to "Admin · $adminCount",
                    "Organizador" to "Organizador · $organizerCount",
                    "Participante" to "Participante · $participantCount",
                    "Espectador" to "Espectador · $spectatorCount"
                )

                filters.forEach { (filterKey, filterLabel) ->
                    val isSelected = selectedFilter == filterKey
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
                            .clickable { selectedFilter = filterKey }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = filterLabel,
                            fontFamily = Geist,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) LMWhite else LMGray700
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Users List
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, end = 18.dp, bottom = 18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filteredUtilizadores.forEach { user ->
                    CardWrapper(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onUtilizadorClick(user.id) },
                        pad = 12.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Avatar(
                                name = user.nome,
                                size = 38.dp,
                                color = if (user.tipo == TipoUtilizador.ADMIN) LMRed else LMInk
                            )
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = user.nome,
                                        fontFamily = Geist,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = LMInk,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f, fill = false)
                                    )
                                    if (user.tipo == TipoUtilizador.ADMIN) {
                                        Box(
                                            modifier = Modifier
                                                .background(LMRed, RoundedCornerShape(4.dp))
                                                .padding(horizontal = 5.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "ADMIN",
                                                fontFamily = Geist,
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 9.sp,
                                                color = LMWhite
                                            )
                                        }
                                    }
                                }
                                
                                Text(
                                    text = user.email,
                                    fontFamily = Geist,
                                    fontSize = 11.sp,
                                    color = LMGray500,
                                    modifier = Modifier.padding(top = 1.dp)
                                )
                                
                                Row(
                                    modifier = Modifier.padding(top = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(LMGray100, RoundedCornerShape(99.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = user.tipo.descricao,
                                            fontFamily = Geist,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 10.sp,
                                            color = LMGray700
                                        )
                                    }
                                    
                                    Pill(
                                        text = if (user.active) "Ativo" else "Pendente",
                                        kind = if (user.active) "live" else "warn"
                                    )
                                }
                            }
                            
                            IconButton(
                                onClick = { onUtilizadorClick(user.id) },
                                modifier = Modifier.size(30.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Mais opções",
                                    tint = LMGray500,
                                    modifier = Modifier.size(18.dp)
                                )
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
fun UtilizadoresScreenPreview() {
    LeagueMatchTheme {
        UtilizadoresScreen()
    }
}
