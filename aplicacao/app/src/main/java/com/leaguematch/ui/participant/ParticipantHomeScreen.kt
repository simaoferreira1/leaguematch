/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: ParticipantHomeScreen.kt
 * Tipo: Interface (Compose View) do Participante
 *
 * Descrição:
 * Este ficheiro define um ecrã do fluxo do Jogador/Participante em Jetpack Compose.\n * Mostra ao participante o estado do seu torneio, código de equipas para inscrição, estatísticas e notificações.
 */
package com.leaguematch.ui.participant // Define o pacote deste ficheiro de código

import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.clickable // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.* // Importa dependência / biblioteca necessária
import androidx.compose.foundation.rememberScrollState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.BarChart // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.EmojiEvents // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Groups // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsSoccer // Importa dependência / biblioteca necessária
import androidx.compose.material3.* // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Brush // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.vector.ImageVector // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Utilizador // Importa dependência / biblioteca necessária
import com.leaguematch.translations.AppStrings // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.ParticipantBottomBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.* // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária

@Composable
fun ParticipantHomeScreen( // Declaração de função / método de lógica
    usuarioLogado: Utilizador?,
    selectedItem: String,
    strings: AppStrings,
    primaryColor: Color,
    onTorneiosClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEquipaClick: () -> Unit,
    onEstatisticasClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
    Scaffold(
        bottomBar = {
            ParticipantBottomBar(
                selectedItem = selectedItem,
                onHomeClick = {},
                onTorneiosClick = onTorneiosClick,
                onJogosClick = onJogosClick,
                onEquipaClick = onEquipaClick,
                onEstatisticasClick = onEstatisticasClick,
                onPerfilClick = onPerfilClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 18.dp)
                .padding(bottom = 80.dp)
        ) {
            val nome = usuarioLogado?.nome ?: strings.participantFallbackName // Declara constante local (leitura única)

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = "${strings.participantGreetingPrefix}, $nome",
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = strings.participantHomeSubtitle,
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500
            )

            Spacer(modifier = Modifier.height(20.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            ParticipantHeroCard(
                strings = strings,
                primaryColor = primaryColor
            )

            Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            ParticipantActionCard(
                title = strings.registeredTournaments,
                description = strings.registeredTournamentsDescription,
                icon = Icons.Default.EmojiEvents,
                primaryColor = primaryColor,
                onClick = onTorneiosClick // Callback: Define a ação executada ao clicar no componente
            )

            ParticipantActionCard(
                title = strings.upcomingGames,
                description = strings.upcomingGamesDescription,
                icon = Icons.Default.SportsSoccer,
                primaryColor = primaryColor,
                onClick = onJogosClick // Callback: Define a ação executada ao clicar no componente
            )

            ParticipantActionCard(
                title = strings.myTeam,
                description = strings.myTeamDescription,
                icon = Icons.Default.Groups,
                primaryColor = primaryColor,
                onClick = onEquipaClick // Callback: Define a ação executada ao clicar no componente
            )

            ParticipantActionCard(
                title = strings.statistics,
                description = strings.statisticsDescription,
                icon = Icons.Default.BarChart,
                primaryColor = primaryColor,
                onClick = onEstatisticasClick // Callback: Define a ação executada ao clicar no componente
            )
        }
    }
}

@Composable
private fun ParticipantHeroCard( // Declaração de função / método de lógica
    strings: AppStrings,
    primaryColor: Color
) {
    Box( // Contentor Compose: Sobrepõe os elementos filhos
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        primaryColor,
                        primaryColor.copy(alpha = 0.82f)
                    )
                ),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(18.dp)
    ) {
        Column { // Contentor Compose: Alinha os filhos numa coluna vertical
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = strings.participantAreaTitle,
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = LMWhite
            )

            Spacer(modifier = Modifier.height(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = strings.participantAreaDescription,
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMWhite.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
private fun ParticipantActionCard( // Declaração de função / método de lógica
    title: String,
    description: String,
    icon: ImageVector,
    primaryColor: Color,
    onClick: () -> Unit // Callback: Define a ação executada ao clicar no componente
) {
    Surface(
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clickable { onClick() }, // Callback: Define a ação executada ao clicar no componente
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(52.dp)
                    .background(
                        color = primaryColor.copy(alpha = 0.10f),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon( // Componente Compose: Desenha um ícone vetorial
                    imageVector = icon,
                    contentDescription = null,
                    tint = primaryColor,
                    modifier = Modifier.size(26.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column { // Contentor Compose: Alinha os filhos numa coluna vertical
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = title,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = LMInk
                )

                Spacer(modifier = Modifier.height(3.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = description,
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }
        }
    }
}