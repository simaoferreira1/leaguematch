/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: EstatisticasJogoScreen.kt
 * Tipo: Interface (Compose View) do Espectador
 *
 * Descrição:
 * Este ficheiro define um ecrã de visualização pública (Espectador) em Jetpack Compose.\n * Apenas exibe dados para leitura (como tabelas de classificação, jogos ao vivo e calendários) sem permitir alteração.
 */
package com.leaguematch.ui.spectator // Define o pacote deste ficheiro de código

import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Arrangement // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Box // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Column // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Row // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Spacer // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.fillMaxHeight // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.fillMaxSize // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.fillMaxWidth // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.height // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.padding // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.size // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.statusBarsPadding // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.width // Importa dependência / biblioteca necessária
import androidx.compose.foundation.rememberScrollState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.CircleShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Analytics // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.BarChart // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Flag // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.GpsFixed // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.PanTool // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsSoccer // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.rounded.ArrowBack // Importa dependência / biblioteca necessária
import androidx.compose.material3.Card // Importa dependência / biblioteca necessária
import androidx.compose.material3.CardDefaults // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.IconButton // Importa dependência / biblioteca necessária
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
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.EstatisticaJogo // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Jogo // Importa dependência / biblioteca necessária
import com.leaguematch.translations.AppStrings // Importa dependência / biblioteca necessária
import com.leaguematch.translations.StringsPt // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Bricolage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.GeistMono // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária

@Composable
fun EstatisticasJogoScreen( // Declaração de função / método de lógica
    jogo: Jogo,
    estatisticas: List<EstatisticaJogo>,
    onBackClick: () -> Unit,
    modalidade: String = "Futebol",
    strings: AppStrings = StringsPt
) {
    Scaffold(
        topBar = {
            EstatisticasTopBar(onBackClick = onBackClick)
        },
        containerColor = Color(0xFFF6F6F8)
    ) { innerPadding ->

        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            EstatisticasHeaderCard(jogo = jogo)

            val estatisticasExibicao = if (estatisticas.isEmpty()) { // Estrutura de decisão condicional principal
                com.leaguematch.ui.organizer.estatisticasPorModalidade(modalidade).flatMap {
                    listOf(
                        EstatisticaJogo(tipo = it.titulo, equipa = "casa", valor = it.casa),
                        EstatisticaJogo(tipo = it.titulo, equipa = "fora", valor = it.fora)
                    )
                }
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                estatisticas
            }

            val groupedStats = estatisticasExibicao.groupBy { it.tipo } // Declara constante local (leitura única)
            groupedStats.forEach { (tipo, _) ->
                EstatisticaComparativaCard(
                    titulo = tipo,
                    tipo = tipo,
                    estatisticas = estatisticasExibicao,
                    percentagem = tipo.contains("Posse", ignoreCase = true)
                )
            }
        }
    }
}

@Composable
private fun EstatisticasTopBar( // Declaração de função / método de lógica
    onBackClick: () -> Unit
) {
    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) { // Componente Compose: Desenha um botão com ícone
            Icon( // Componente Compose: Desenha um ícone vetorial
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = "Voltar",
                tint = Color.Black
            )
        }

        TranslatedText(
            text = "Estatísticas do Jogo",
            fontFamily = Geist,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
        )

        Icon( // Componente Compose: Desenha um ícone vetorial
            imageVector = Icons.Default.Analytics,
            contentDescription = null,
            tint = LMRed,
            modifier = Modifier.size(24.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
        )
    }
}

@Composable
private fun EstatisticasHeaderCard( // Declaração de função / método de lógica
    jogo: Jogo
) {
    Box( // Contentor Compose: Sobrepõe os elementos filhos
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF0A0A0B), Color(0xFF1F1F22))
                ),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(18.dp)
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = jogo.estado.uppercase(),
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                verticalAlignment = Alignment.CenterVertically
            ) {
                EquipaHeader(
                    nome = jogo.casa,
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 12.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                ) {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "${jogo.resultadoCasa} - ${jogo.resultadoFora}",
                        fontFamily = Bricolage,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 36.sp,
                        color = Color.White
                    )

                    TranslatedText(
                        text = "Resultado",
                        fontFamily = Geist,
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }

                EquipaHeader(
                    nome = jogo.fora,
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }
        }
    }
}

@Composable
private fun EquipaHeader( // Declaração de função / método de lógica
    nome: String,
    modifier: Modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
) {
    Column( // Contentor Compose: Alinha os filhos numa coluna vertical
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box( // Contentor Compose: Sobrepõe os elementos filhos
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .size(52.dp)
                .background(
                    brush = Brush.linearGradient(
                        listOf(LMRed, Color(0xFF0A0A0B))
                    ),
                    shape = RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = nome
                    .split("\\s+".toRegex())
                    .take(2)
                    .mapNotNull { it.firstOrNull()?.toString() }
                    .joinToString("")
                    .uppercase(),
                fontFamily = Bricolage,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = nome,
            fontFamily = Geist,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
private fun EstatisticaComparativaCard( // Declaração de função / método de lógica
    titulo: String,
    tipo: String,
    estatisticas: List<EstatisticaJogo>,
    percentagem: Boolean = false
) {
    val estatisticasTipo = estatisticas.filter { // Declara constante local (leitura única)
        it.tipo.equals(tipo, ignoreCase = true)
    }

    val casa = estatisticasTipo // Declara constante local (leitura única)
        .firstOrNull { it.equipa.equals("casa", ignoreCase = true) }
        ?.valor ?: 0

    val fora = estatisticasTipo // Declara constante local (leitura única)
        .firstOrNull { it.equipa.equals("fora", ignoreCase = true) }
        ?.valor ?: 0

    val total = (casa + fora).coerceAtLeast(1) // Declara constante local (leitura única)
    val casaPeso = casa.toFloat() / total.toFloat() // Declara constante local (leitura única)
    val foraPeso = fora.toFloat() / total.toFloat() // Declara constante local (leitura única)

    val sufixo = if (percentagem) "%" else "" // Estrutura de decisão condicional principal

    Card( // Contentor Compose: Cartão visual com elevação e cantos
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LMWhite),
        border = BorderStroke(1.dp, Color(0xFFE2E2E7))
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier.padding(14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .size(30.dp)
                        .background(
                            color = LMRed.copy(alpha = 0.12f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    val icon = when { // Escolha múltipla condicional (semelhante a switch-case)
                        titulo.contains("Posse", ignoreCase = true) ->
                            Icons.Default.PanTool

                        titulo.contains("Remates à baliza", ignoreCase = true) ->
                            Icons.Default.GpsFixed

                        titulo.contains("Remates", ignoreCase = true) ->
                            Icons.Default.SportsSoccer

                        titulo.contains("Cantos", ignoreCase = true) ->
                            Icons.Default.Flag

                        else -> // Fluxo condicional alternativo caso o 'if' seja falso
                            Icons.Default.BarChart
                    }

                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = icon,
                        contentDescription = null,
                        tint = LMRed,
                        modifier = Modifier.size(16.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                }

                Spacer(modifier = Modifier.width(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = titulo,
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = LMInk
                )
            }

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "$casa$sufixo",
                    fontFamily = GeistMono,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = LMInk
                )

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "$fora$sufixo",
                    fontFamily = GeistMono,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = LMInk
                )
            }

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .height(9.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFEDEDF1))
            ) {
                if (casa > 0) { // Estrutura de decisão condicional principal
                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .fillMaxHeight()
                            .weight(casaPeso)
                            .background(LMRed)
                    )
                }

                if (fora > 0) { // Estrutura de decisão condicional principal
                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .fillMaxHeight()
                            .weight(foraPeso)
                            .background(Color(0xFF111827))
                    )
                }
            }

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "Casa",
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    color = LMGray500
                )

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "Fora",
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    color = LMGray500
                )
            }
        }
    }
}

@Composable
private fun SemEstatisticasCard() { // Declaração de função / método de lógica
    Card( // Contentor Compose: Cartão visual com elevação e cantos
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LMWhite),
        border = BorderStroke(1.dp, Color(0xFFE2E2E7))
    ) {
        Box( // Contentor Compose: Sobrepõe os elementos filhos
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxWidth()
                .padding(vertical = 36.dp, horizontal = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = "Ainda não existem estatísticas registadas para este jogo.",
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500,
                textAlign = TextAlign.Center
            )
        }
    }
}