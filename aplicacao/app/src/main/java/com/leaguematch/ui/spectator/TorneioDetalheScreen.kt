/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: TorneioDetalheScreen.kt
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
import androidx.compose.foundation.layout.ColumnScope // Importa dependência / biblioteca necessária
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
import androidx.compose.foundation.shape.CircleShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.ArrowBack // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.EmojiEvents // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Groups // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsSoccer // Importa dependência / biblioteca necessária
import androidx.compose.material3.Card // Importa dependência / biblioteca necessária
import androidx.compose.material3.CardDefaults // Importa dependência / biblioteca necessária
import androidx.compose.material3.HorizontalDivider // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.IconButton // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.material3.TextButton // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.draw.clip // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Brush // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.vector.ImageVector // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Torneio // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.SpectatorBottomBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.RedDark // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.RedPrimary // Importa dependência / biblioteca necessária

data class MelhorMarcadorItem( // Declaração de classe para modelar objetos
    val nome: String, // Declara constante local (leitura única)
    val golos: Int, // Declara constante local (leitura única)
    val equipa: String // Declara constante local (leitura única)
)

data class JogoResumoItem( // Declaração de classe para modelar objetos
    val equipaCasa: String, // Declara constante local (leitura única)
    val equipaFora: String, // Declara constante local (leitura única)
    val golosCasa: Int?, // Declara constante local (leitura única)
    val golosFora: Int? // Declara constante local (leitura única)
)

@Composable
fun TorneioDetalheScreen( // Declaração de função / método de lógica
    torneio: Torneio,
    melhoresMarcadores: List<MelhorMarcadorItem>,
    jogos: List<JogoResumoItem>,
    onBackClick: () -> Unit,
    onVerJogosClick: () -> Unit,
    onHomeClick: () -> Unit,
    onClassificacaoClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEquipasClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFF6F6F8),
        bottomBar = {
            SpectatorBottomBar(
                selectedItem = "home",
                onHomeClick = onHomeClick,
                onClassificacaoClick = onClassificacaoClick,
                onJogosClick = onJogosClick,
                onEquipasClick = onEquipasClick,
                onPerfilClick = onPerfilClick
            )
        }
    ) { innerPadding ->

        LazyColumn( // Lista Compose: Renderiza uma lista vertical com scroll eficiente
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                IconButton( // Componente Compose: Desenha um botão com ícone
                    onClick = onBackClick, // Callback: Define a ação executada ao clicar no componente
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = RedDark
                    )
                }
            }

            item {
                TorneioDetalheHeader(torneio)
            }

            item {
                Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        InfoSmallCard(
                            title = "Jogos",
                            value = jogos.size.toString(),
                            icon = Icons.Default.SportsSoccer,
                            modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                        )

                        InfoSmallCard(
                            title = "Equipas",
                            value = torneio.equipas.toString(),
                            icon = Icons.Default.Groups,
                            modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                        )
                    }

                    InfoSmallCard(
                        title = "Ver classificação completa",
                        value = "Classificação",
                        icon = Icons.Default.EmojiEvents,
                        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                        onClick = onClassificacaoClick // Callback: Define a ação executada ao clicar no componente
                    )
                }
            }

            item {
                SectionCard(
                    title = "Melhores Marcadores",
                    actionText = null,
                    onActionClick = null
                ) {
                    if (melhoresMarcadores.isEmpty()) { // Estrutura de decisão condicional principal
                        EmptyText("Ainda não existem marcadores.")
                    } else { // Fluxo condicional alternativo caso o 'if' seja falso
                        melhoresMarcadores.take(5).forEachIndexed { index, jogador ->
                            MelhorMarcadorRow(index + 1, jogador)

                            if (index != melhoresMarcadores.take(5).lastIndex) { // Estrutura de decisão condicional principal
                                HorizontalDivider(color = Color.White.copy(alpha = 0.12f))
                            }
                        }
                    }
                }
            }

            item {
                SectionCard(
                    title = "Jogos",
                    actionText = "Ver todos",
                    onActionClick = onVerJogosClick
                ) {
                    if (jogos.isEmpty()) { // Estrutura de decisão condicional principal
                        EmptyText("Ainda não existem jogos.")
                    } else { // Fluxo condicional alternativo caso o 'if' seja falso
                        jogos.take(5).forEachIndexed { index, jogo ->
                            JogoResumoRow(jogo)

                            if (index != jogos.take(5).lastIndex) { // Estrutura de decisão condicional principal
                                HorizontalDivider(color = Color.White.copy(alpha = 0.12f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TorneioDetalheHeader(torneio: Torneio) { // Declaração de função / método de lógica
    Card( // Contentor Compose: Cartão visual com elevação e cantos
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(RedPrimary, RedDark, Color(0xFF17171C))
                    )
                )
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) { // Contentor Compose: Alinha os filhos numa linha horizontal
                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = Icons.Default.EmojiEvents,
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
                        text = torneio.modalidade,
                        color = Color.White.copy(alpha = 0.82f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFF21C064))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = torneio.estado,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                HeaderStat("Participantes", "${torneio.equipas} equipas")
                HeaderStat("Modalidade", torneio.modalidade)
            }
        }
    }
}

@Composable
private fun HeaderStat(label: String, value: String) { // Declaração de função / método de lógica
    Column { // Contentor Compose: Alinha os filhos numa coluna vertical
        TranslatedText(
            text = label,
            color = Color.White.copy(alpha = 0.65f),
            style = MaterialTheme.typography.labelSmall
        )

        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = value,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun InfoSmallCard( // Declaração de função / método de lógica
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier, // Modificador Compose: Define tamanho, margem, padding ou clique
    onClick: (() -> Unit)? = null // Callback: Define a ação executada ao clicar no componente
) {
    Card( // Contentor Compose: Cartão visual com elevação e cantos
        modifier = modifier.then(
            if (onClick != null) { // Callback: Define a ação executada ao clicar no componente
                Modifier.clickable { onClick() } // Modificador Compose: Define tamanho, margem, padding ou clique
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            }
        ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(RedPrimary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon( // Componente Compose: Desenha um ícone vetorial
                    imageVector = icon,
                    contentDescription = null,
                    tint = RedPrimary
                )
            }

            Spacer(modifier = Modifier.width(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column { // Contentor Compose: Alinha os filhos numa coluna vertical
                TranslatedText(
                    text = value,
                    color = RedDark,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                TranslatedText(
                    text = title,
                    color = Color(0xFF74747C),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
private fun SectionCard( // Declaração de função / método de lógica
    title: String,
    actionText: String?,
    onActionClick: (() -> Unit)?,
    content: @Composable ColumnScope.() -> Unit
) {
    Card( // Contentor Compose: Cartão visual com elevação e cantos
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B20)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier.padding(14.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
        ) {
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                verticalAlignment = Alignment.CenterVertically
            ) {
                TranslatedText(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                if (actionText != null && onActionClick != null) { // Estrutura de decisão condicional principal
                    TextButton(onClick = onActionClick) { // Callback: Define a ação executada ao clicar no componente
                        TranslatedText(
                            text = actionText,
                            color = RedPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            content()
        }
    }
}

@Composable
private fun MelhorMarcadorRow( // Declaração de função / método de lógica
    posicao: Int,
    item: MelhorMarcadorItem
) {
    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .padding(vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box( // Contentor Compose: Sobrepõe os elementos filhos
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .size(28.dp)
                .clip(CircleShape)
                .background(RedPrimary),
            contentAlignment = Alignment.Center
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = posicao.toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelSmall
            )
        }

        Spacer(modifier = Modifier.width(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

        Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = item.nome,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = item.equipa,
                color = Color.White.copy(alpha = 0.65f),
                style = MaterialTheme.typography.labelSmall
            )
        }

        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = item.golos.toString(),
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.width(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

        TranslatedText(
            text = "golos",
            color = Color.White.copy(alpha = 0.65f),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun JogoResumoRow(jogo: JogoResumoItem) { // Declaração de função / método de lógica
    val resultado = if (jogo.golosCasa != null && jogo.golosFora != null) { // Estrutura de decisão condicional principal
        "${jogo.golosCasa} - ${jogo.golosFora}"
    } else { // Fluxo condicional alternativo caso o 'if' seja falso
        "vs"
    }

    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .padding(vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = jogo.equipaCasa,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
        )

        Box( // Contentor Compose: Sobrepõe os elementos filhos
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White.copy(alpha = 0.12f))
                .padding(horizontal = 10.dp, vertical = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = resultado,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = jogo.equipaFora,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
        )
    }
}

@Composable
private fun EmptyText(text: String) { // Declaração de função / método de lógica
    TranslatedText(
        text = text,
        color = Color.White.copy(alpha = 0.65f),
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(vertical = 8.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
    )
}