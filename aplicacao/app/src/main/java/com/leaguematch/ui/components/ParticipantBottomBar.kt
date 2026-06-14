/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: ParticipantBottomBar.kt
 * Tipo: Componente Visual Reutilizável
 *
 * Descrição:
 * Este ficheiro define um componente personalizado e reutilizável em Jetpack Compose.\n * É partilhado entre vários ecrãs para manter a consistência visual (botões, listas, caixas de diálogo, etc.).
 */
package com.leaguematch.ui.components // Define o pacote deste ficheiro de código

import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.BarChart // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.EmojiEvents // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Groups // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Home // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Settings // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsSoccer // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.NavigationBar // Importa dependência / biblioteca necessária
import androidx.compose.material3.NavigationBarItem // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária

@Composable
fun ParticipantBottomBar( // Declaração de função / método de lógica
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
            onClick = onHomeClick, // Callback: Define a ação executada ao clicar no componente
            icon = { Icon(Icons.Default.Home, contentDescription = null) }, // Componente Compose: Desenha um ícone vetorial
            label = { TranslatedText("Início") }
        )

        NavigationBarItem(
            selected = selectedItem == "torneios",
            onClick = onTorneiosClick, // Callback: Define a ação executada ao clicar no componente
            icon = { Icon(Icons.Default.EmojiEvents, contentDescription = null) }, // Componente Compose: Desenha um ícone vetorial
            label = { TranslatedText("Torneios") }
        )

        NavigationBarItem(
            selected = selectedItem == "jogos",
            onClick = onJogosClick, // Callback: Define a ação executada ao clicar no componente
            icon = { Icon(Icons.Default.SportsSoccer, contentDescription = null) }, // Componente Compose: Desenha um ícone vetorial
            label = { TranslatedText("Jogos") }
        )

        NavigationBarItem(
            selected = selectedItem == "equipa",
            onClick = onEquipaClick, // Callback: Define a ação executada ao clicar no componente
            icon = { Icon(Icons.Default.Groups, contentDescription = null) }, // Componente Compose: Desenha um ícone vetorial
            label = { TranslatedText("Equipa") }
        )

        NavigationBarItem(
            selected = selectedItem == "estatisticas",
            onClick = onEstatisticasClick, // Callback: Define a ação executada ao clicar no componente
            icon = { Icon(Icons.Default.BarChart, contentDescription = null) }, // Componente Compose: Desenha um ícone vetorial
            label = { TranslatedText("Stats") }
        )

        NavigationBarItem(
            selected = selectedItem == "perfil",
            onClick = onPerfilClick, // Callback: Define a ação executada ao clicar no componente
            icon = { Icon(Icons.Default.Settings, contentDescription = null) }, // Componente Compose: Desenha um ícone vetorial
            label = { TranslatedText("Perfil") }
        )
    }
}