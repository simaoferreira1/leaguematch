/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: ParticipantStatsScreen.kt
 * Tipo: Interface (Compose View) do Participante
 *
 * Descrição:
 * Este ficheiro define um ecrã do fluxo do Jogador/Participante em Jetpack Compose.\n * Mostra ao participante o estado do seu torneio, código de equipas para inscrição, estatísticas e notificações.
 */
package com.leaguematch.ui.participant // Define o pacote deste ficheiro de código

import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Arrangement // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Box // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Column // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Row // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Spacer // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.fillMaxSize // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.fillMaxWidth // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.height // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.padding // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.size // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.width // Importa dependência / biblioteca necessária
import androidx.compose.foundation.rememberScrollState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Assessment // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.EmojiEvents // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsSoccer // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Star // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Surface // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.vector.ImageVector // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.translations.AppStrings // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.ParticipantBottomBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Bricolage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária

data class ParticipantStats( // Declaração de classe para modelar objetos
    val jogos: Int = 0, // Declara constante local (leitura única)
    val golos: Int = 0, // Declara constante local (leitura única)
    val faltas: Int = 0, // Declara constante local (leitura única)
    val cartoes: Int = 0 // Declara constante local (leitura única)
)

@Composable
fun ParticipantStatsScreen( // Declaração de função / método de lógica
    stats: ParticipantStats,
    strings: AppStrings,
    primaryColor: Color,
    onHomeClick: () -> Unit,
    onTorneiosClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEquipaClick: () -> Unit,
    onEstatisticasClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
    val golosPorJogo = if (stats.jogos > 0) stats.golos.toDouble() / stats.jogos else 0.0 // Estrutura de decisão condicional principal
    val assistPorJogo = if (stats.jogos > 0) stats.faltas.toDouble() / stats.jogos else 0.0 // Estrutura de decisão condicional principal

    Scaffold(
        bottomBar = {
            ParticipantBottomBar(
                selectedItem = "estatisticas",
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
                text = strings.myStatsTitle,
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = strings.myStatsSubtitle,
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500
            )

            Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { // Contentor Compose: Alinha os filhos numa linha horizontal
                StatCard(
                    label = strings.gamesStat,
                    value = stats.jogos.toString(),
                    icon = Icons.Default.Assessment,
                    primaryColor = primaryColor,
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                StatCard(
                    label = strings.goalsStat,
                    value = stats.golos.toString(),
                    icon = Icons.Default.SportsSoccer,
                    primaryColor = primaryColor,
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { // Contentor Compose: Alinha os filhos numa linha horizontal
                StatCard(
                    label = strings.assistsStat,
                    value = stats.faltas.toString(),
                    icon = Icons.Default.EmojiEvents,
                    primaryColor = primaryColor,
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                StatCard(
                    label = strings.mvpStat,
                    value = stats.cartoes.toString(),
                    icon = Icons.Default.Star,
                    primaryColor = primaryColor,
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.height(20.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            PerformanceChartCard(
                stats = stats,
                strings = strings,
                primaryColor = primaryColor
            )

            Spacer(modifier = Modifier.height(16.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Surface(
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                shape = RoundedCornerShape(18.dp),
                color = LMWhite,
                border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
                shadowElevation = 1.dp
            ) {
                Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                    modifier = Modifier.padding(16.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                ) {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = strings.performanceTitle,
                        fontFamily = Bricolage,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = LMInk
                    )

                    Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { // Contentor Compose: Alinha os filhos numa linha horizontal
                        MiniPerformanceCard(
                            label = strings.goalsPerGame,
                            value = String.format("%.1f", golosPorJogo),
                            modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                        )

                        MiniPerformanceCard(
                            label = strings.assistsPerGame,
                            value = String.format("%.1f", assistPorJogo),
                            modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            InfoCard(
                stats = stats,
                strings = strings,
                primaryColor = primaryColor
            )
        }
    }
}

@Composable
private fun StatCard( // Declaração de função / método de lógica
    label: String,
    value: String,
    icon: ImageVector,
    primaryColor: Color,
    modifier: Modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
) {
    Surface(
        modifier = modifier.height(114.dp),
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier.padding(14.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
        ) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                imageVector = icon,
                contentDescription = null,
                tint = primaryColor,
                modifier = Modifier.size(26.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = value,
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 26.sp,
                color = LMInk
            )

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = label,
                fontFamily = Geist,
                fontSize = 12.sp,
                color = LMGray500
            )
        }
    }
}

@Composable
private fun PerformanceChartCard( // Declaração de função / método de lógica
    stats: ParticipantStats,
    strings: AppStrings,
    primaryColor: Color
) {
    val maxValue = listOf( // Declara constante local (leitura única)
        stats.jogos,
        stats.golos,
        stats.faltas,
        stats.cartoes
    ).maxOrNull()?.coerceAtLeast(1) ?: 1

    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(20.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier.padding(16.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = strings.overallPerformance,
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = strings.overallPerformanceSubtitle,
                fontFamily = Geist,
                fontSize = 12.sp,
                color = LMGray500
            )

            Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            ChartBar(strings.gamesStat, stats.jogos, maxValue, primaryColor)
            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            ChartBar(strings.goalsStat, stats.golos, maxValue, primaryColor)
            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            ChartBar(strings.assistsStat, stats.faltas, maxValue, primaryColor)
            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            ChartBar(strings.mvpStat, stats.cartoes, maxValue, primaryColor)

            if (stats.jogos == 0 && stats.golos == 0 && stats.faltas == 0 && stats.cartoes == 0) { // Estrutura de decisão condicional principal
                Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = strings.notEnoughStats,
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }
        }
    }
}

@Composable
private fun ChartBar( // Declaração de função / método de lógica
    label: String,
    value: Int,
    maxValue: Int,
    primaryColor: Color
) {
    val progress = if (maxValue > 0) value.toFloat() / maxValue.toFloat() else 0f // Estrutura de decisão condicional principal

    Column { // Contentor Compose: Alinha os filhos numa coluna vertical
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = label,
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMInk,
                fontWeight = FontWeight.Medium
            )

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = value.toString(),
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500
            )
        }

        Spacer(modifier = Modifier.height(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

        Box( // Contentor Compose: Sobrepõe os elementos filhos
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxWidth()
                .height(10.dp)
                .background(Color(0xFFF0F0F4), RoundedCornerShape(50.dp))
        ) {
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .height(10.dp)
                    .background(primaryColor, RoundedCornerShape(50.dp))
            )
        }
    }
}

@Composable
private fun MiniPerformanceCard( // Declaração de função / método de lógica
    label: String,
    value: String,
    modifier: Modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFF7F7FA)
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier.padding(14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            horizontalAlignment = Alignment.Start
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = value,
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = LMInk
            )

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = label,
                fontFamily = Geist,
                fontSize = 12.sp,
                color = LMGray500
            )
        }
    }
}

@Composable
private fun InfoCard( // Declaração de função / método de lógica
    stats: ParticipantStats,
    strings: AppStrings,
    primaryColor: Color
) {
    val message = if (stats.jogos == 0) { // Estrutura de decisão condicional principal
        strings.statsStartMessage
    } else { // Fluxo condicional alternativo caso o 'if' seja falso
        strings.statsContinueMessage
    }

    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(18.dp),
        color = primaryColor.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, primaryColor.copy(alpha = 0.20f))
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(16.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = primaryColor,
                modifier = Modifier.size(28.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )

            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = message,
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMInk
            )
        }
    }
}