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
import com.leaguematch.ui.components.TranslatedText

// Ecrã que apresenta e permite filtrar os utilizadores
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
    // Guarda o texto escrito na barra de pesquisa
    var searchQuery by remember { mutableStateOf("") }
    // Guarda o filtro de tipo de utilizador selecionado
    var selectedFilter by remember { mutableStateOf("Todos") }

    // Conta o número total de utilizadores
    val totalCount = utilizadores.size
    // Conta quantos utilizadores existem de cada tipo
    val adminCount = utilizadores.count { it.tipo == TipoUtilizador.ADMIN }
    val organizerCount = utilizadores.count { it.tipo == TipoUtilizador.ORGANIZADOR }
    val participantCount = utilizadores.count { it.tipo == TipoUtilizador.PARTICIPANTE }
    val spectatorCount = utilizadores.count { it.tipo == TipoUtilizador.ESPECTADOR }

    // Filtra os utilizadores pela pesquisa e pelo tipo selecionado
    val filteredUtilizadores = utilizadores.filter { user ->
        // Verifica se o nome ou email corresponde ao texto pesquisado
        val matchesSearch = user.nome.contains(searchQuery, ignoreCase = true) || 
                            user.email.contains(searchQuery, ignoreCase = true)
        // Verifica se o utilizador corresponde ao filtro escolhido
        val matchesFilter = when (selectedFilter) {
            "Admin" -> user.tipo == TipoUtilizador.ADMIN
            "Organizador" -> user.tipo == TipoUtilizador.ORGANIZADOR
            "Participante" -> user.tipo == TipoUtilizador.PARTICIPANTE
            "Espectador" -> user.tipo == TipoUtilizador.ESPECTADOR
            else -> true
        }
        matchesSearch && matchesFilter
    }

    // Estrutura principal do ecrã com barra inferior
    Scaffold(
        bottomBar = {
            // Barra de navegação inferior do administrador
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
        // Conteúdo principal da página
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 12.dp)
        ) {
            // Cabeçalho da página com título, subtítulo e botão de adicionar
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
                        // Botão visual para adicionar novo utilizador
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
                // Campo de pesquisa dos utilizadores
                LeagueMatchTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = "Procurar utilizador...",
                    icon = Icons.Default.Search
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Lista de filtros disponíveis com a contagem de utilizadores
                val filters = listOf(
                    "Todos" to "Todos · $totalCount",
                    "Admin" to "Admin · $adminCount",
                    "Organizador" to "Organizador · $organizerCount",
                    "Participante" to "Participante · $participantCount",
                    "Espectador" to "Espectador · $spectatorCount"
                )

                // Percorre todos os filtros e cria um botão para cada um
                filters.forEach { (filterKey, filterLabel) ->
                    // Verifica se este filtro está selecionado
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
                            // Atualiza o filtro quando o utilizador clica
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

            // Lista de utilizadores filtrados
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, end = 18.dp, bottom = 18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Percorre os utilizadores filtrados e cria um cartão para cada um
                filteredUtilizadores.forEach { user ->
                    // Cartão clicável de cada utilizador
                    CardWrapper(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onUtilizadorClick(user.id) },
                        pad = 12.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar com as iniciais/nome do utilizador
                            Avatar(
                                name = user.nome,
                                size = 38.dp,
                                color = if (user.tipo == TipoUtilizador.ADMIN) LMRed else LMInk
                            )
                            
                            Spacer(modifier = Modifier.width(12.dp))

                            // Informação principal do utilizador
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
                                    // Nome do utilizador
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
                                    // Mostra etiqueta especial caso o utilizador seja administrador
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
                                    // Email do utilizador
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
                                        // Etiqueta com o tipo de utilizador
                                        Text(
                                            text = user.tipo.descricao,
                                            fontFamily = Geist,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 10.sp,
                                            color = LMGray700
                                        )
                                    }

                                    // Mostra se o utilizador está ativo ou desativado
                                    Pill(
                                        text = if (user.active) "Ativo" else "Desativo",
                                        kind = if (user.active) "live" else "warn"
                                    )
                                }
                            }

                            // Botão para abrir mais opções ou detalhes do utilizador
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
