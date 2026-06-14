/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: JogosScreen.kt
 * Tipo: Interface (Compose View) do Espectador
 *
 * Descrição:
 * Este ficheiro define um ecrã de visualização pública (Espectador) em Jetpack Compose.\n * Apenas exibe dados para leitura (como tabelas de classificação, jogos ao vivo e calendários) sem permitir alteração.
 */
package com.leaguematch.ui.spectator // Define o pacote deste ficheiro de código

import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.clickable // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Arrangement // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Box // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Column // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.PaddingValues // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Row // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Spacer // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.fillMaxSize // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.fillMaxWidth // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.height // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.padding // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.size // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.width // Importa dependência / biblioteca necessária
import androidx.compose.foundation.lazy.LazyColumn // Importa dependência / biblioteca necessária
import androidx.compose.foundation.lazy.items // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.CircleShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.EmojiEvents // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsSoccer // Importa dependência / biblioteca necessária
import androidx.compose.material3.Card // Importa dependência / biblioteca necessária
import androidx.compose.material3.CardDefaults // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.draw.clip // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Brush // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Jogo // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Torneio // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.SpectatorBottomBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.RedDark // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.RedPrimary // Importa dependência / biblioteca necessária

@Composable
fun JogosScreen( // Declaração de função / método de lógica
    torneio: Torneio,
    jogos: List<Jogo>,
    onHomeClick: () -> Unit,
    onClassificacaoClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEquipasClick: () -> Unit,
    onPerfilClick: () -> Unit,
    onJogoClick: (Jogo) -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            SpectatorBottomBar(
                selectedItem = "jogos",
                onHomeClick = onHomeClick,
                onClassificacaoClick = onClassificacaoClick,
                onJogosClick = onJogosClick,
                onEquipasClick = onEquipasClick,
                onPerfilClick = onPerfilClick
            )
        },
        containerColor = Color(0xFFF6F6F8)
    ) { innerPadding ->
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            TorneioHeaderJogos(torneio)

            Spacer(modifier = Modifier.height(20.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            TranslatedText(
                text = "Jogos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = RedDark
            )

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            if (jogos.isEmpty()) { // Estrutura de decisão condicional principal
                Card( // Contentor Compose: Cartão visual com elevação e cantos
                    modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B20))
                ) {
                    Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                        modifier = Modifier.padding(24.dp).fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon( // Componente Compose: Desenha um ícone vetorial
                            Icons.Default.SportsSoccer,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(36.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                        )
                        Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                        TranslatedText(
                            "Ainda não existem jogos.",
                            color = Color.White.copy(alpha = 0.65f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                LazyColumn( // Lista Compose: Renderiza uma lista vertical com scroll eficiente
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 12.dp)
                ) {
                    items(jogos) { jogo ->
                        JogoCard(jogo, onClick = { onJogoClick(jogo) }) // Callback: Define a ação executada ao clicar no componente
                    }
                }
            }
        }
    }
}

@Composable
private fun JogoCard(jogo: Jogo, onClick: () -> Unit) { // Declaração de função / método de lógica
    val isFinished = jogo.estado.equals("Finalizado", ignoreCase = true) // Declara constante local (leitura única)
    val isOngoing = jogo.estado.equals("A Decorrer", ignoreCase = true) // Declara constante local (leitura única)

    Card( // Contentor Compose: Cartão visual com elevação e cantos
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .clickable { onClick() }, // Callback: Define a ação executada ao clicar no componente
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B20)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val estadoCor = when { // Escolha múltipla condicional (semelhante a switch-case)
                    isFinished -> Color(0xFF6B6B72)
                    isOngoing -> Color(0xFF21C064)
                    else -> Color(0xFFE4A11B) // Fluxo condicional alternativo caso o 'if' seja falso
                }
                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .clip(RoundedCornerShape(50))
                        .background(estadoCor.copy(alpha = 0.18f))
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = jogo.estado,
                        color = estadoCor,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = jogo.casa,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = if (isFinished || isOngoing) "${jogo.resultadoCasa} - ${jogo.resultadoFora}" else "vs", // Estrutura de decisão condicional principal
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = jogo.fora,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f), // Modificador Compose: Define tamanho, margem, padding ou clique
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
            }
        }
    }
}

@Composable
private fun TorneioHeaderJogos(torneio: Torneio) { // Declaração de função / método de lógica
    Card( // Contentor Compose: Cartão visual com elevação e cantos
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(RedPrimary, RedDark, Color(0xFF17171C))))
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp)) // Componente Compose: Desenha um ícone vetorial
            }
            Spacer(modifier = Modifier.width(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
            Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                Text(torneio.nome, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium) // Componente Compose: Desenha texto estruturado no ecrã
                Text("${torneio.modalidade} • ${torneio.estado}", color = Color.White.copy(alpha = 0.82f), style = MaterialTheme.typography.bodySmall) // Componente Compose: Desenha texto estruturado no ecrã
            }
        }
    }
}
