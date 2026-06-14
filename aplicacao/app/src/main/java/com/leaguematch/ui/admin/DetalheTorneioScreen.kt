/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: DetalheTorneioScreen.kt
 * Tipo: Interface (Compose View) do Administrador
 *
 * Descrição:
 * Este ficheiro define um ecrã da área do Administrador em Jetpack Compose.\n * Ele desenha componentes visuais reativos baseado no estado fornecido pelo respetivo ViewModel.\n * Permite ao Admin gerir utilizadores (ativar/desativar), visualizar alertas do sistema e gráficos.
 */
package com.leaguematch.ui.admin // Define o pacote deste ficheiro de código

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
import androidx.compose.material.icons.filled.EmojiEvents // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsBasketball // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsHandball // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsSoccer // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsTennis // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.rounded.ArrowBack // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.rounded.Groups // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.rounded.SportsScore // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.IconButton // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Surface // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Brush // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.vector.ImageVector // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.style.TextAlign // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.DetalheTorneio // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.Avatar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.GeistMono // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMBorder // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed700 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária

// Ecrã que apresenta os detalhes e estatísticas de um torneio
@Composable
fun DetalheTorneioScreen( // Declaração de função / método de lógica
    detalhe: DetalheTorneio?,
    onBackClick: () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    // Obtém o torneio recebido através do objeto de detalhe
    val torneio = detalhe?.torneio // Declara constante local (leitura única)

    // Estrutura principal da página com conteúdo e barra inferior
    Scaffold(
        bottomBar = bottomBar,
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        // Caso o torneio não exista, apresenta uma mensagem ao utilizador
        if (torneio == null) { // Estrutura de decisão condicional principal
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                TranslatedText("Torneio não encontrado.", color = LMGray500)
            }
            return@Scaffold // Retorna o resultado da execução da função
        }

        // Carrega configurações específicas da modalidade (ícone, cores e textos)
        val config = modalidadeConfig(torneio.modalidade) // Declara constante local (leitura única)
        // Obtém os jogos, ranking e estatísticas do torneio
        val jogos = detalhe.jogos // Declara constante local (leitura única)
        val goleadores = detalhe.goleadores // Declara constante local (leitura única)
        val totalEventos = detalhe.totalGolos // Declara constante local (leitura única)

        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            // Cabeçalho com botão de voltar e título da página
            Row(verticalAlignment = Alignment.CenterVertically) { // Contentor Compose: Alinha os filhos numa linha horizontal
                IconButton(onClick = onBackClick) { // Componente Compose: Desenha um botão com ícone
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = null,
                        tint = LMInk
                    )
                }

                TranslatedText(
                    text = "Detalhes do Torneio",
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = LMInk
                )
            }

            Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            // Cartão com informações gerais do torneio
            Surface(
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                shape = RoundedCornerShape(22.dp),
                color = LMWhite,
                border = BorderStroke(1.dp, LMBorder),
                shadowElevation = 1.dp
            ) {
                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                    modifier = Modifier.padding(14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Área visual que representa a modalidade do torneio
                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .size(64.dp)
                            .background(
                                brush = Brush.linearGradient(config.colors),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon( // Componente Compose: Desenha um ícone vetorial
                            imageVector = config.icon,
                            contentDescription = null,
                            tint = LMWhite,
                            modifier = Modifier.size(34.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = torneio.modalidade.uppercase(),
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = LMGray500
                        )

                        Spacer(modifier = Modifier.height(3.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = torneio.nome,
                            fontFamily = Geist,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = LMInk
                        )

                        Spacer(modifier = Modifier.height(5.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = "${torneio.formato} • ${torneio.estado}",
                            fontFamily = Geist,
                            fontSize = 12.sp,
                            color = LMGray500
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            // Resumo rápido das principais estatísticas do torneio
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MiniStatCard(
                    label = config.participantsLabel,
                    value = torneio.equipas.toString(),
                    icon = Icons.Rounded.Groups,
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                MiniStatCard(
                    label = config.gamesLabel,
                    value = jogos.size.toString(),
                    icon = Icons.Rounded.SportsScore,
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                MiniStatCard(
                    label = config.eventsLabel,
                    value = totalEventos.toString(),
                    icon = config.icon,
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.height(22.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            // Secção que apresenta os melhores jogadores da competição
            SectionTitle(config.rankingTitle)

            // Caso não existam estatísticas registadas
            if (goleadores.isEmpty()) { // Estrutura de decisão condicional principal
                EmptyCard(config.emptyRankingText)
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    // Percorre e apresenta cada jogador do ranking
                    goleadores.forEach { jogador ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                            shape = RoundedCornerShape(18.dp),
                            color = LMWhite,
                            border = BorderStroke(1.dp, LMBorder)
                        ) {
                            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                                modifier = Modifier.padding(14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Avatar(
                                    name = jogador.nome,
                                    size = 34.dp,
                                    color = LMInk
                                )

                                Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                                Text( // Componente Compose: Desenha texto estruturado no ecrã
                                    text = jogador.nome,
                                    fontFamily = Geist,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = LMInk,
                                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                                )

                                Text( // Componente Compose: Desenha texto estruturado no ecrã
                                    text = "${jogador.golos} ${config.eventsLabel.lowercase()}",
                                    fontFamily = Geist,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 12.sp,
                                    color = LMRed
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            // Secção com os jogos já realizados no torneio
            SectionTitle(config.gamesSectionTitle)

            if (jogos.isEmpty()) { // Estrutura de decisão condicional principal
                EmptyCard("Sem ${config.gamesLabel.lowercase()} registados.")
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    // Percorre todos os jogos associados ao torneio
                    jogos.forEach { jogo ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                            shape = RoundedCornerShape(18.dp),
                            color = LMWhite,
                            border = BorderStroke(1.dp, LMBorder)
                        ) {
                            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                                modifier = Modifier.padding(14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text( // Componente Compose: Desenha texto estruturado no ecrã
                                    text = jogo.casa,
                                    fontFamily = Geist,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = LMInk,
                                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                                )

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = LMInk
                                ) {
                                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                                        text = "${jogo.resultadoCasa}-${jogo.resultadoFora}",
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                                        fontFamily = GeistMono,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = LMWhite
                                    )
                                }

                                Text( // Componente Compose: Desenha texto estruturado no ecrã
                                    text = jogo.fora,
                                    fontFamily = Geist,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = LMInk,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
        }
    }
}

// Estrutura que guarda informações específicas de cada modalidade
private data class ModalidadeConfig( // Declaração de classe para modelar objetos
    val icon: ImageVector, // Declara constante local (leitura única)
    val colors: List<Color>, // Declara constante local (leitura única)
    val participantsLabel: String, // Declara constante local (leitura única)
    val gamesLabel: String, // Declara constante local (leitura única)
    val eventsLabel: String, // Declara constante local (leitura única)
    val rankingTitle: String, // Declara constante local (leitura única)
    val gamesSectionTitle: String, // Declara constante local (leitura única)
    val emptyRankingText: String // Declara constante local (leitura única)
)

// Define cores, textos e ícones de acordo com a modalidade selecionada
private fun modalidadeConfig(modalidade: String): ModalidadeConfig { // Declaração de função / método de lógica
    return when (modalidade.lowercase()) { // Escolha múltipla condicional (semelhante a switch-case)
        "futebol" -> ModalidadeConfig(
            icon = Icons.Default.SportsSoccer,
            colors = listOf(LMRed, LMRed700),
            participantsLabel = "Equipas",
            gamesLabel = "Jogos",
            eventsLabel = "Golos",
            rankingTitle = "Melhores marcadores",
            gamesSectionTitle = "Jogos recentes",
            emptyRankingText = "Sem golos registados."
        )

        "andebol" -> ModalidadeConfig(
            icon = Icons.Default.SportsHandball,
            colors = listOf(Color(0xFF16A34A), Color(0xFF15803D)),
            participantsLabel = "Equipas",
            gamesLabel = "Jogos",
            eventsLabel = "Golos",
            rankingTitle = "Melhores marcadores",
            gamesSectionTitle = "Jogos recentes",
            emptyRankingText = "Sem golos registados."
        )

        "basquetebol" -> ModalidadeConfig(
            icon = Icons.Default.SportsBasketball,
            colors = listOf(Color(0xFF2563EB), Color(0xFF1D4ED8)),
            participantsLabel = "Equipas",
            gamesLabel = "Jogos",
            eventsLabel = "Pontos",
            rankingTitle = "Melhores pontuadores",
            gamesSectionTitle = "Jogos recentes",
            emptyRankingText = "Sem pontos registados."
        )

        "padel" -> ModalidadeConfig(
            icon = Icons.Default.SportsTennis,
            colors = listOf(Color(0xFF1F2937), Color(0xFF111827)),
            participantsLabel = "Duplas",
            gamesLabel = "Partidas",
            eventsLabel = "Sets",
            rankingTitle = "Destaques",
            gamesSectionTitle = "Partidas recentes",
            emptyRankingText = "Sem destaques registados."
        )

        "ténis", "tenis" -> ModalidadeConfig(
            icon = Icons.Default.SportsTennis,
            colors = listOf(Color(0xFFBE123C), Color(0xFF9F1239)),
            participantsLabel = "Jogadores",
            gamesLabel = "Partidas",
            eventsLabel = "Sets",
            rankingTitle = "Destaques",
            gamesSectionTitle = "Partidas recentes",
            emptyRankingText = "Sem destaques registados."
        )

        else -> ModalidadeConfig( // Fluxo condicional alternativo caso o 'if' seja falso
            icon = Icons.Default.EmojiEvents,
            colors = listOf(LMRed, LMRed700),
            participantsLabel = "Equipas",
            gamesLabel = "Jogos",
            eventsLabel = "Eventos",
            rankingTitle = "Destaques",
            gamesSectionTitle = "Jogos recentes",
            emptyRankingText = "Sem eventos registados."
        )
    }
}

// Cartão reutilizável para apresentar estatísticas resumidas
@Composable
private fun MiniStatCard( // Declaração de função / método de lógica
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, LMBorder),
        shadowElevation = 1.dp
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier.padding(vertical = 14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                imageVector = icon,
                contentDescription = null,
                tint = LMRed,
                modifier = Modifier.size(20.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )

            Spacer(modifier = Modifier.height(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = value,
                fontFamily = GeistMono,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = LMInk
            )

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = label.uppercase(),
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp,
                color = LMGray500
            )
        }
    }
}

// Título padronizado utilizado nas várias secções da página
@Composable
private fun SectionTitle(text: String) { // Declaração de função / método de lógica
    Text( // Componente Compose: Desenha texto estruturado no ecrã
        text = text.uppercase(),
        fontFamily = Geist,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        color = LMGray500,
        letterSpacing = 0.4.sp,
        modifier = Modifier.padding(bottom = 8.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
    )
}

// Cartão apresentado quando não existem dados para mostrar
@Composable
private fun EmptyCard(text: String) { // Declaração de função / método de lógica
    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, LMBorder)
    ) {
        Box( // Contentor Compose: Sobrepõe os elementos filhos
            modifier = Modifier.padding(18.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            contentAlignment = Alignment.Center
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = text,
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500
            )
        }
    }
}