package com.leaguematch.ui.organizer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.ui.components.*
import com.leaguematch.ui.theme.*

// Mock Tournament Data
data class MockTournament(
    val name: String,
    val teams: Int,
    val status: String,
    val sport: String,
    val startColor: Color,
    val endColor: Color
)

@Composable
fun OrgTournamentsScreen(
    onNavigateToCreate: () -> Unit = {},
    onNavigateToActions: (String) -> Unit = {},
    onEquipasClick: () -> Unit = {},
    onJogosClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {}
) {
    val tlist = remember {
        listOf(
            MockTournament("Carabao CUP", 16, "A Decorrer", "Futebol", LMRed, Color(0xFF7A0C19)),
            MockTournament("Barca CUP", 16, "A Decorrer", "Futebol", LMInk2, LMGray700),
            MockTournament("MinhoFut Cup", 8, "Por Iniciar", "Futebol", Color(0xFF166534), Color(0xFF22C55E))
        )
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedSport by remember { mutableStateOf("football") }

    Scaffold(
        bottomBar = {
            OrganizerBottomBar(
                selectedItem = "torneios",
                onTorneiosClick = {},
                onEquipasClick = onEquipasClick,
                onJogosClick = onJogosClick,
                onPerfilClick = onPerfilClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 12.dp)
            ) {
                // TopBar with Notifications and Avatar
                TopBar(
                    title = "Os meus torneios",
                    big = true,
                    sub = "${tlist.size} ativos · Organizador",
                    rightContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(LMGray100, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notificações",
                                    tint = LMInk,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                )

                // Search Bar
                Box(modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)) {
                    LocalSearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        placeholder = "Pesquisar torneios, equipas..."
                    )
                }

                // Sports tabs
                Box(modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)) {
                    LocalSportTabs(
                        selectedId = selectedSport,
                        onSelectedChange = { selectedSport = it }
                    )
                }

                // Tournaments List
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val filteredList = tlist.filter {
                        it.name.contains(searchQuery, ignoreCase = true)
                    }

                    filteredList.forEach { tournament ->
                        TournamentCard(
                            tournament = tournament,
                            onClick = { onNavigateToActions(tournament.name) }
                        )
                    }
                }
            }

            // Custom FAB "Criar Torneio" with gradient and shadow
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 18.dp, bottom = 24.dp)
                    .clip(RoundedCornerShape(99.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(LMRed, Color(0xFFC41326))
                        )
                    )
                    .clickable { onNavigateToCreate() }
                    .padding(horizontal = 18.dp, vertical = 13.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Criar torneio",
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun LocalSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LMGray100, RoundedCornerShape(12.dp))
            .border(1.dp, Color.Transparent, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = LMGray500,
            modifier = Modifier.size(16.dp)
        )
        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                Text(
                    text = placeholder,
                    color = LMGray500,
                    fontSize = 13.sp,
                    fontFamily = Geist
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                textStyle = TextStyle(
                    color = LMInk,
                    fontSize = 13.sp,
                    fontFamily = Geist
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(16.dp)
                .background(LMBorder)
        )
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = null,
            tint = LMGray500,
            modifier = Modifier.size(15.dp)
        )
    }
}

data class SportTabItem(
    val id: String,
    val label: String,
    val icon: ImageVector
)

@Composable
fun LocalSportTabs(
    selectedId: String,
    onSelectedChange: (String) -> Unit
) {
    val tabs = remember {
        listOf(
            SportTabItem("football", "Futebol", Icons.Rounded.SportsSoccer),
            SportTabItem("tennis", "Ténis", Icons.Rounded.SportsTennis),
            SportTabItem("basket", "Basquetebol", Icons.Rounded.SportsBasketball),
            SportTabItem("handball", "Andebol", Icons.Rounded.SportsVolleyball),
            SportTabItem("rugby", "Rugby", Icons.Rounded.SportsRugby)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tabs.forEach { tab ->
            val isActive = tab.id == selectedId
            val bg = if (isActive) LMInk else LMWhite
            val fg = if (isActive) Color.White else LMGray700
            val borderCol = if (isActive) LMInk else LMBorder

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(bg)
                    .border(1.dp, borderCol, RoundedCornerShape(999.dp))
                    .clickable { onSelectedChange(tab.id) }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = tab.icon,
                    contentDescription = null,
                    tint = if (isActive) Color.White else LMGray500,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = tab.label,
                    fontFamily = Geist,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = fg
                )
            }
        }
    }
}

@Composable
fun TournamentCard(
    tournament: MockTournament,
    onClick: () -> Unit
) {
    val statusKind = when (tournament.status.trim().lowercase()) {
        "em progresso", "decorrer", "a decorrer", "live", "em curso" -> "live"
        "por iniciar", "brevemente", "soon" -> "soon"
        else -> "done"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LMWhite, RoundedCornerShape(16.dp))
            .border(1.dp, LMBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Trophy logo card
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(
                    Brush.linearGradient(
                        listOf(tournament.startColor, tournament.endColor)
                    ),
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.EmojiEvents,
                contentDescription = "Torneio",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        // Details
        Column(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 64.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = tournament.sport.uppercase(),
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = LMGray500,
                    letterSpacing = 0.4.sp
                )
                Text(
                    text = tournament.name,
                    fontFamily = Bricolage,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = LMInk,
                    letterSpacing = (-0.2).sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${tournament.teams} equipas",
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
                Pill(text = tournament.status, kind = statusKind)
            }
        }
    }
}
