/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: CalendarioScreen.kt
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
import androidx.compose.material.icons.filled.ArrowBack // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.CalendarMonth // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.LocationOn // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Schedule // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsSoccer // Importa dependência / biblioteca necessária
import androidx.compose.material3.Card // Importa dependência / biblioteca necessária
import androidx.compose.material3.CardDefaults // Importa dependência / biblioteca necessária
import androidx.compose.material3.Divider // Importa dependência / biblioteca necessária
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
import androidx.compose.ui.text.style.TextAlign // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Jogo // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Torneio // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.SpectatorBottomBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.RedDark // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.RedPrimary // Importa dependência / biblioteca necessária

@Composable
fun CalendarioScreen( // Declaração de função / método de lógica
    torneio: Torneio,
    jogos: List<Jogo>,
    onJogoClick: (Jogo) -> Unit,
    onBackClick: () -> Unit,
    onNavigateExplorar: () -> Unit,
    onNavigateClassificacao: () -> Unit,
    onNavigateJogos: () -> Unit,
    onNavigateEquipas: () -> Unit,
    onNavigatePerfil: () -> Unit
) {
    Scaffold(
        bottomBar = {
            SpectatorBottomBar(
                selectedItem = "calendario",
                onHomeClick = onNavigateExplorar,
                onClassificacaoClick = onNavigateClassificacao,
                onJogosClick = onNavigateJogos,
                onEquipasClick = onNavigateEquipas,
                onPerfilClick = onNavigatePerfil
            )
        }
    ) { padding ->

        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))
                .padding(padding)
        ) {

            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(RedPrimary, RedDark)
                        )
                    )
                    .padding(start = 18.dp, end = 18.dp, top = 18.dp, bottom = 22.dp)
            ) {
                Column { // Contentor Compose: Alinha os filhos numa coluna vertical
                    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box( // Contentor Compose: Sobrepõe os elementos filhos
                            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.18f))
                                .clickable { onBackClick() },
                            contentAlignment = Alignment.Center
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
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon( // Componente Compose: Desenha um ícone vetorial
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                            modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                        ) {
                            TranslatedText(
                                text = "Próximos Jogos",
                                color = Color.White,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            TranslatedText(
                                text = torneio.nome,
                                color = Color.White.copy(alpha = 0.85f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    TranslatedText(
                        text = "Consulta os jogos marcados e acompanha os próximos confrontos do torneio.",
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (jogos.isEmpty()) { // Estrutura de decisão condicional principal
                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "Ainda não existem jogos agendados.",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                LazyColumn( // Lista Compose: Renderiza uma lista vertical com scroll eficiente
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(jogos) { jogo ->
                        ProximoJogoCard(
                            jogo = jogo,
                            onClick = { onJogoClick(jogo) } // Callback: Define a ação executada ao clicar no componente
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProximoJogoCard( // Declaração de função / método de lógica
    jogo: Jogo,
    onClick: () -> Unit // Callback: Define a ação executada ao clicar no componente
) {
    Card( // Contentor Compose: Cartão visual com elevação e cantos
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .clickable { onClick() }, // Callback: Define a ação executada ao clicar no componente
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier.padding(16.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
        ) {

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon( // Componente Compose: Desenha um ícone vetorial
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = RedPrimary,
                    modifier = Modifier.size(20.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                Spacer(modifier = Modifier.width(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "${jogo.data} • ${jogo.hora}",
                    color = Color(0xFF333333),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Divider(
                color = Color(0xFFE9E9E9),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(16.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = jogo.casa,
                    modifier = Modifier.weight(1f), // Modificador Compose: Define tamanho, margem, padding ou clique
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222),
                    textAlign = TextAlign.Start
                )

                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .padding(horizontal = 10.dp)
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF5F2F3)),
                    contentAlignment = Alignment.Center
                ) {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "VS",
                        color = RedPrimary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = jogo.fora,
                    modifier = Modifier.weight(1f), // Modificador Compose: Define tamanho, margem, padding ou clique
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222),
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                    modifier = Modifier.weight(1f), // Modificador Compose: Define tamanho, margem, padding ou clique
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = Icons.Default.SportsSoccer,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )

                    Spacer(modifier = Modifier.width(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = jogo.estado,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if (jogo.local.isNotBlank()) { // Estrutura de decisão condicional principal
                    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                        modifier = Modifier.weight(1f), // Modificador Compose: Define tamanho, margem, padding ou clique
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon( // Componente Compose: Desenha um ícone vetorial
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                        )

                        Spacer(modifier = Modifier.width(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = jogo.local,
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }
}