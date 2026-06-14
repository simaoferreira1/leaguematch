/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: ClassificacaoScreen.kt
 * Tipo: Interface (Compose View) do Espectador
 *
 * Descrição:
 * Este ficheiro define um ecrã de visualização pública (Espectador) em Jetpack Compose.\n * Apenas exibe dados para leitura (como tabelas de classificação, jogos ao vivo e calendários) sem permitir alteração.
 */
package com.leaguematch.ui.spectator // Define o pacote deste ficheiro de código

import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.EmojiEvents // Importa dependência / biblioteca necessária
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
import com.leaguematch.data.remote.model.Torneio // Importa dependência / biblioteca necessária
import com.leaguematch.translations.AppStrings // Importa dependência / biblioteca necessária
import com.leaguematch.translations.StringsPt // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.RedDark // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.RedPrimary // Importa dependência / biblioteca necessária

data class ClassificacaoItem( // Declaração de classe para modelar objetos
    val nomeEquipa: String, // Declara constante local (leitura única)
    val pontos: Int, // Declara constante local (leitura única)
    val jogos: Int, // Declara constante local (leitura única)
    val vitorias: Int, // Declara constante local (leitura única)
    val empates: Int, // Declara constante local (leitura única)
    val derrotas: Int, // Declara constante local (leitura única)
    val golosMarcados: Int, // Declara constante local (leitura única)
    val golosSofridos: Int // Declara constante local (leitura única)
) {
    val diferencaGolos: Int // Declara constante local (leitura única)
        get() = golosMarcados - golosSofridos
}

@Composable
fun ClassificacaoScreen( // Declaração de função / método de lógica
    torneio: Torneio,
    classificacao: List<ClassificacaoItem>,
    onBackClick: (() -> Unit)? = null,
    bottomBar: @Composable () -> Unit = {},
    strings: AppStrings = StringsPt
) {
    Scaffold(
        bottomBar = bottomBar,
        containerColor = Color(0xFFF6F6F8)
    ) { innerPadding ->

        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            TorneioHeader(
                torneio = torneio,
                onBackClick = onBackClick,
                strings = strings
            )

            Spacer(modifier = Modifier.height(20.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            TranslatedText(
                text = strings.classification,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = RedDark
            )

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            ClassificacaoTableHeader(strings = strings)

            Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            if (classificacao.isEmpty()) { // Estrutura de decisão condicional principal
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = strings.noClassificationAvailable,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                LazyColumn( // Lista Compose: Renderiza uma lista vertical com scroll eficiente
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 12.dp)
                ) {
                    itemsIndexed(classificacao) { index, item ->
                        ClassificacaoRow(
                            posicao = index + 1,
                            item = item
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TorneioHeader( // Declaração de função / método de lógica
    torneio: Torneio,
    onBackClick: (() -> Unit)? = null,
    strings: AppStrings
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
            if (onBackClick != null) { // Estrutura de decisão condicional principal
                IconButton( // Componente Compose: Desenha um botão com ícone
                    onClick = onBackClick, // Callback: Define a ação executada ao clicar no componente
                    modifier = Modifier.padding(end = 8.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                ) {
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = strings.back,
                        tint = Color.White
                    )
                }
            }

            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(48.dp)
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

                Spacer(modifier = Modifier.height(3.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "${torneio.modalidade} • ${torneio.estado}",
                    color = Color.White.copy(alpha = 0.82f),
                    style = MaterialTheme.typography.bodySmall
                )

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = strings.participantsTeams(torneio.equipas),
                    color = Color.White.copy(alpha = 0.82f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun ClassificacaoTableHeader( // Declaração de função / método de lógica
    strings: AppStrings
) {
    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(strings.positionShort, modifier = Modifier.width(36.dp), style = MaterialTheme.typography.labelSmall) // Componente Compose: Desenha texto estruturado no ecrã
        Text(strings.team, modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall) // Componente Compose: Desenha texto estruturado no ecrã

        HeaderCell(strings.gamesShort)
        HeaderCell(strings.winsShort)
        HeaderCell(strings.drawsShort)
        HeaderCell(strings.lossesShort)
        HeaderCell(strings.goalDifferenceShort)
        HeaderCell(strings.pointsShort, width = 38.dp)
    }
}

@Composable
private fun HeaderCell( // Declaração de função / método de lógica
    text: String,
    width: androidx.compose.ui.unit.Dp = 28.dp
) {
    Text( // Componente Compose: Desenha texto estruturado no ecrã
        text = text,
        modifier = Modifier.width(width), // Modificador Compose: Define tamanho, margem, padding ou clique
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF6B6B72)
    )
}

@Composable
private fun ClassificacaoRow( // Declaração de função / método de lógica
    posicao: Int,
    item: ClassificacaoItem
) {
    val isTopThree = posicao <= 3 // Declara constante local (leitura única)
    val backgroundColor = if (isTopThree) Color.White else Color(0xFF1B1B20) // Estrutura de decisão condicional principal
    val textColor = if (isTopThree) Color(0xFF17171C) else Color.White // Estrutura de decisão condicional principal
    val subtitleColor = if (isTopThree) Color(0xFF6E6E76) else Color.White.copy(alpha = 0.72f) // Estrutura de decisão condicional principal
    val positionColor = if (isTopThree) RedPrimary else Color(0xFF2A2A31) // Estrutura de decisão condicional principal

    Card( // Contentor Compose: Cartão visual com elevação e cantos
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isTopThree) 4.dp else 2.dp) // Estrutura de decisão condicional principal
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(positionColor),
                contentAlignment = Alignment.Center
            ) {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = posicao.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.width(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = item.nomeEquipa,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "${item.golosMarcados} GM • ${item.golosSofridos} GS",
                    color = subtitleColor,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            StatCell(item.jogos.toString(), textColor)
            StatCell(item.vitorias.toString(), textColor)
            StatCell(item.empates.toString(), textColor)
            StatCell(item.derrotas.toString(), textColor)

            StatCell(
                text = if (item.diferencaGolos > 0) "+${item.diferencaGolos}" else item.diferencaGolos.toString(), // Estrutura de decisão condicional principal
                color = textColor
            )

            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .width(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isTopThree) RedPrimary else Color.White.copy(alpha = 0.12f)) // Estrutura de decisão condicional principal
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = item.pontos.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun StatCell( // Declaração de função / método de lógica
    text: String,
    color: Color
) {
    Text( // Componente Compose: Desenha texto estruturado no ecrã
        text = text,
        modifier = Modifier.width(28.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
        color = color,
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.bodySmall
    )
}