/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: ParticipantGamesScreen.kt
 * Tipo: Interface (Compose View) do Participante
 *
 * Descrição:
 * Este ficheiro define um ecrã do fluxo do Jogador/Participante em Jetpack Compose.\n * Mostra ao participante o estado do seu torneio, código de equipas para inscrição, estatísticas e notificações.
 */
package com.leaguematch.ui.participant // Define o pacote deste ficheiro de código

import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.* // Importa dependência / biblioteca necessária
import androidx.compose.foundation.rememberScrollState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsSoccer // Importa dependência / biblioteca necessária
import androidx.compose.material3.* // Importa dependência / biblioteca necessária
import androidx.compose.runtime.* // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Jogo // Importa dependência / biblioteca necessária
import com.leaguematch.translations.AppStrings // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.ParticipantBottomBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.* // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária

private enum class GamesTab { // Declaração de classe para modelar objetos
    PROXIMOS,
    RESULTADOS
}

@Composable
fun ParticipantGamesScreen( // Declaração de função / método de lógica
    jogos: List<Jogo>,
    strings: AppStrings,
    primaryColor: Color,
    onHomeClick: () -> Unit,
    onTorneiosClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEquipaClick: () -> Unit,
    onEstatisticasClick: () -> Unit,
    onPerfilClick: () -> Unit,
    onJogoClick: (Jogo) -> Unit
) {
    var selectedTab by remember { mutableStateOf(GamesTab.PROXIMOS) } // Declara estado mutável local do Compose

    val proximosJogos = jogos.filter { // Declara constante local (leitura única)
        it.estado.equals("Agendado", ignoreCase = true) ||
                it.estado.equals("Por iniciar", ignoreCase = true)
    }

    val resultados = jogos.filter { // Declara constante local (leitura única)
        it.estado.equals("Finalizado", ignoreCase = true)
    }

    val jogosFiltrados = when (selectedTab) { // Escolha múltipla condicional (semelhante a switch-case)
        GamesTab.PROXIMOS -> proximosJogos
        GamesTab.RESULTADOS -> resultados
    }

    Scaffold(
        bottomBar = {
            ParticipantBottomBar(
                selectedItem = "jogos",
                onHomeClick = onHomeClick,
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
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = strings.myGamesTitle,
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = strings.myGamesSubtitle,
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500
            )

            Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GamesFilterChip(
                    text = strings.upcomingTab,
                    selected = selectedTab == GamesTab.PROXIMOS,
                    primaryColor = primaryColor,
                    onClick = { selectedTab = GamesTab.PROXIMOS }, // Callback: Define a ação executada ao clicar no componente
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                GamesFilterChip(
                    text = strings.resultsTab,
                    selected = selectedTab == GamesTab.RESULTADOS,
                    primaryColor = primaryColor,
                    onClick = { selectedTab = GamesTab.RESULTADOS }, // Callback: Define a ação executada ao clicar no componente
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            if (jogosFiltrados.isEmpty()) { // Estrutura de decisão condicional principal
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = when (selectedTab) { // Escolha múltipla condicional (semelhante a switch-case)
                        GamesTab.PROXIMOS -> strings.noUpcomingGames
                        GamesTab.RESULTADOS -> strings.noResultsYet
                    },
                    fontFamily = Geist,
                    fontSize = 14.sp,
                    color = LMGray500
                )
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                jogosFiltrados.forEach { jogo ->
                    ParticipantGameCard(
                        jogo = jogo,
                        primaryColor = primaryColor,
                        onCardClick = { onJogoClick(jogo) }
                    )

                    Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                }
            }
        }
    }
}

@Composable
private fun GamesFilterChip( // Declaração de função / método de lógica
    text: String,
    selected: Boolean,
    primaryColor: Color,
    onClick: () -> Unit, // Callback: Define a ação executada ao clicar no componente
    modifier: Modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = if (selected) primaryColor else LMWhite, // Estrutura de decisão condicional principal
        border = BorderStroke(
            1.dp,
            if (selected) primaryColor else Color(0xFFE5E5EA) // Estrutura de decisão condicional principal
        ),
        shadowElevation = 1.dp,
        onClick = onClick // Callback: Define a ação executada ao clicar no componente
    ) {
        Box( // Contentor Compose: Sobrepõe os elementos filhos
            modifier = Modifier.padding(vertical = 10.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            contentAlignment = Alignment.Center
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = text,
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = if (selected) LMWhite else LMInk // Estrutura de decisão condicional principal
            )
        }
    }
}

@Composable
private fun ParticipantGameCard( // Declaração de função / método de lógica
    jogo: Jogo,
    primaryColor: Color,
    onCardClick: () -> Unit
) {
    Surface(
        onClick = onCardClick, // Callback: Define a ação executada ao clicar no componente
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                imageVector = Icons.Default.SportsSoccer,
                contentDescription = null,
                tint = primaryColor,
                modifier = Modifier.size(34.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )

            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "${jogo.casa} vs ${jogo.fora}",
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = LMInk
                )

                Spacer(modifier = Modifier.height(3.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = buildString {
                        append(jogo.estado)

                        if (jogo.data.isNotBlank()) { // Estrutura de decisão condicional principal
                            append(" • ")
                            append(jogo.data)
                        }

                        if (jogo.hora.isNotBlank()) { // Estrutura de decisão condicional principal
                            append(" ")
                            append(jogo.hora)
                        }
                    },
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }

            if ( // Estrutura de decisão condicional principal
                jogo.estado.equals("Finalizado", ignoreCase = true) ||
                jogo.estado.equals("A Decorrer", ignoreCase = true)
            ) {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "${jogo.resultadoCasa}-${jogo.resultadoFora}",
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = LMInk
                )
            }
        }
    }
}