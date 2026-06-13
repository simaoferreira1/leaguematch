package com.leaguematch.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable

@Composable
fun ParticipantBottomBar(
    selectedItem: String,
    onHomeClick: () -> Unit,
    onTorneiosClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEquipaClick: () -> Unit,
    onEstatisticasClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedItem == "home",
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { TranslatedText("Início") }
        )

        NavigationBarItem(
            selected = selectedItem == "torneios",
            onClick = onTorneiosClick,
            icon = { Icon(Icons.Default.EmojiEvents, contentDescription = null) },
            label = { TranslatedText("Torneios") }
        )

        NavigationBarItem(
            selected = selectedItem == "jogos",
            onClick = onJogosClick,
            icon = { Icon(Icons.Default.SportsSoccer, contentDescription = null) },
            label = { TranslatedText("Jogos") }
        )

        NavigationBarItem(
            selected = selectedItem == "equipa",
            onClick = onEquipaClick,
            icon = { Icon(Icons.Default.Groups, contentDescription = null) },
            label = { TranslatedText("Equipa") }
        )

        NavigationBarItem(
            selected = selectedItem == "estatisticas",
            onClick = onEstatisticasClick,
            icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
            label = { TranslatedText("Stats") }
        )

        NavigationBarItem(
            selected = selectedItem == "perfil",
            onClick = onPerfilClick,
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { TranslatedText("Perfil") }
        )
    }
}