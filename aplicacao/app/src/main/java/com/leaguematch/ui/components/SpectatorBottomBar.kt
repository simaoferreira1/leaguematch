package com.leaguematch.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable

@Composable
fun SpectatorBottomBar(
    selectedItem: String,
    onHomeClick: () -> Unit,
    onClassificacaoClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEquipasClick: () -> Unit,
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
            selected = selectedItem == "classificacao",
            onClick = onClassificacaoClick,
            icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
            label = { TranslatedText("Tabela") }
        )

        NavigationBarItem(
            selected = selectedItem == "jogos",
            onClick = onJogosClick,
            icon = { Icon(Icons.Default.SportsSoccer, contentDescription = null) },
            label = { TranslatedText("Jogos") }
        )

        NavigationBarItem(
            selected = selectedItem == "equipas",
            onClick = onEquipasClick,
            icon = { Icon(Icons.Default.Groups, contentDescription = null) },
            label = { TranslatedText("Equipas") }
        )

        NavigationBarItem(
            selected = selectedItem == "perfil",
            onClick = onPerfilClick,
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { TranslatedText("Perfil") }
        )
    }
}