/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: EquipasScreen.kt
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
import androidx.compose.foundation.lazy.itemsIndexed // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.CircleShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.ArrowBack // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.EmojiEvents // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Groups // Importa dependência / biblioteca necessária
import androidx.compose.material3.Card // Importa dependência / biblioteca necessária
import androidx.compose.material3.CardDefaults // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.IconButton // Importa dependência / biblioteca necessária
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
import com.leaguematch.data.remote.model.Equipa // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Torneio // Importa dependência / biblioteca necessária
import com.leaguematch.translations.AppStrings // Importa dependência / biblioteca necessária
import com.leaguematch.translations.StringsPt // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.SpectatorBottomBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.RedDark // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.RedPrimary // Importa dependência / biblioteca necessária

@Composable
fun EquipasScreen( // Declaração de função / método de lógica
    torneio: Torneio,
    equipas: List<Equipa>,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onClassificacaoClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEquipasClick: () -> Unit,
    onPerfilClick: () -> Unit,
    onEquipaClick: (Equipa) -> Unit,
    strings: AppStrings = StringsPt
) {
    Scaffold(
        bottomBar = {
            SpectatorBottomBar(
                selectedItem = "equipas",
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
            TorneioHeaderEquipas(
                torneio = torneio,
                strings = strings,
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(20.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = strings.teams,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = RedDark
            )

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            if (equipas.isEmpty()) { // Estrutura de decisão condicional principal
                Card( // Contentor Compose: Cartão visual com elevação e cantos
                    modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B20))
                ) {
                    Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon( // Componente Compose: Desenha um ícone vetorial
                            Icons.Default.Groups,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(36.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                        )

                        Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = strings.noTeamsYet,
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
                    itemsIndexed(equipas) { index, equipa ->
                        EquipaCard(
                            posicao = index + 1,
                            equipa = equipa,
                            onClick = { // Callback: Define a ação executada ao clicar no componente
                                onEquipaClick(equipa)
                            }

                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EquipaCard( // Declaração de função / método de lógica
    posicao: Int,
    equipa: Equipa,
    onClick: () -> Unit // Callback: Define a ação executada ao clicar no componente


) {
    Card( // Contentor Compose: Cartão visual com elevação e cantos
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .clickable { onClick() }, // Callback: Define a ação executada ao clicar no componente
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B20)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(RedPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = equipa.nome.take(2).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.width(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = equipa.nome,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
            )

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = "#$posicao",
                color = Color.White.copy(alpha = 0.45f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun TorneioHeaderEquipas( // Declaração de função / método de lógica
    torneio: Torneio,
    strings: AppStrings,
    onBackClick: () -> Unit
) {
    Card( // Contentor Compose: Cartão visual com elevação e cantos
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(RedPrimary, RedDark, Color(0xFF17171C))
                    )
                )
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton( // Componente Compose: Desenha um botão com ícone
                onClick = onBackClick, // Callback: Define a ação executada ao clicar no componente
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.18f))
            ) {
                Icon( // Componente Compose: Desenha um ícone vetorial
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon( // Componente Compose: Desenha um ícone vetorial
                    Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(30.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.width(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = torneio.nome,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "${strings.teamsLabel(torneio.equipas)} • ${torneio.modalidade}",
                    color = Color.White.copy(alpha = 0.82f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}