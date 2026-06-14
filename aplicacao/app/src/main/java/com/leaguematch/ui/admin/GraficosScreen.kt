/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: GraficosScreen.kt
 * Tipo: Interface (Compose View) do Administrador
 *
 * Descrição:
 * Este ficheiro define um ecrã da área do Administrador em Jetpack Compose.\n * Ele desenha componentes visuais reativos baseado no estado fornecido pelo respetivo ViewModel.\n * Permite ao Admin gerir utilizadores (ativar/desativar), visualizar alertas do sistema e gráficos.
 */
package com.leaguematch.ui.admin // Define o pacote deste ficheiro de código

import androidx.compose.foundation.Canvas // Importa dependência / biblioteca necessária
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
import androidx.compose.foundation.rememberScrollState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.remember // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.draw.clip // Importa dependência / biblioteca necessária
import androidx.compose.ui.geometry.Offset // Importa dependência / biblioteca necessária
import androidx.compose.ui.geometry.Size // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Brush // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Path // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.StrokeCap // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.drawscope.Stroke // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.EstatisticasAdmin // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.AdminBottomBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.CardWrapper // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TopBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Bricolage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.GeistMono // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray100 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray300 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray600 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray700 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMLive // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária

// Ecrã que apresenta gráficos e estatísticas gerais da plataforma
@Composable
fun GraficosScreen( // Declaração de função / método de lógica
    estatisticas: EstatisticasAdmin,
    onHomeClick: () -> Unit,
    onUtilizadoresClick: () -> Unit,
    onTorneiosClick: () -> Unit,
    onDefinicoesClick: () -> Unit,
    onPeriodChange: (String) -> Unit
) {
    // Guarda o período atualmente selecionado para análise dos dados
    var selectedPeriod by remember { mutableStateOf("30d") } // Declara estado mutável local do Compose

    // Estrutura principal da página com barra de navegação inferior
    Scaffold(
        bottomBar = {
            AdminBottomBar(
                selectedItem = "graficos",
                onHomeClick = onHomeClick,
                onUtilizadoresClick = onUtilizadoresClick,
                onTorneiosClick = onTorneiosClick,
                onGraficosClick = {},
                onDefinicoesClick = onDefinicoesClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 12.dp)
        ) {
            // Cabeçalho da página de estatísticas
            TopBar(
                title = "Gráficos",
                big = true,
                sub = "Análise geral da plataforma",
                rightContent = {
                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .size(36.dp)
                            .background(LMGray100, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {

                    }
                }
            )

            // Cartão com resumo geral da plataforma
            CardWrapper(
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                // Apresenta o número total de utilizadores, torneios e jogos
                Column(modifier = Modifier.fillMaxWidth()) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column { // Contentor Compose: Alinha os filhos numa coluna vertical
                            TranslatedText(
                                text = "TOTAL DE UTILIZADORES",
                                fontFamily = Geist,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = LMGray500,
                                letterSpacing = 0.4.sp
                            )

                            Spacer(modifier = Modifier.height(2.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                            Text( // Componente Compose: Desenha texto estruturado no ecrã
                                text = estatisticas.totalUtilizadores.toString(),
                                fontFamily = Bricolage,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = LMInk,
                                letterSpacing = (-0.5).sp
                            )

                            Spacer(modifier = Modifier.height(2.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                            Text( // Componente Compose: Desenha texto estruturado no ecrã
                                text = "${estatisticas.totalTorneios} torneios • ${estatisticas.totalJogos} jogos",
                                fontFamily = Geist,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = LMLive
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    // Gráfico de evolução dos jogos ao longo do tempo
                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .fillMaxWidth()
                            .height(110.dp)
                    ) {
                        // Desenho manual do gráfico de linha utilizando Canvas
                        Canvas(modifier = Modifier.fillMaxSize()) { // Modificador Compose: Define tamanho, margem, padding ou clique
                            // Dados utilizados para construir a linha do gráfico
                            val data = estatisticas.jogosPorPeriodo.ifEmpty { listOf(0.05f) } // Declara constante local (leitura única)
                            val w = size.width // Declara constante local (leitura única)
                            val h = size.height // Declara constante local (leitura única)
                            val maxVal = data.maxOrNull()?.coerceAtLeast(0.01f) ?: 1f // Declara constante local (leitura única)
                            val step = if (data.size > 1) w / (data.size - 1) else w // Estrutura de decisão condicional principal
                            val strokeColor = LMRed // Declara constante local (leitura única)

                            // Criação da linha principal e da área preenchida do gráfico
                            val path = Path() // Declara constante local (leitura única)
                            val fillPath = Path() // Declara constante local (leitura única)

                            data.forEachIndexed { index, value ->
                                val x = if (data.size > 1) index * step else w / 2f // Estrutura de decisão condicional principal
                                val y = h * 0.1f + (h * 0.8f) * (1f - (value / maxVal)) // Declara constante local (leitura única)

                                if (index == 0) { // Estrutura de decisão condicional principal
                                    path.moveTo(x, y)
                                    fillPath.moveTo(x, y)
                                } else { // Fluxo condicional alternativo caso o 'if' seja falso
                                    path.lineTo(x, y)
                                    fillPath.lineTo(x, y)
                                }
                            }

                            if (data.size > 1) { // Estrutura de decisão condicional principal
                                fillPath.lineTo(w, h)
                                fillPath.lineTo(0f, h)
                                fillPath.close()

                                drawPath(
                                    path = fillPath,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            strokeColor.copy(alpha = 0.25f),
                                            Color.Transparent
                                        ),
                                        startY = 0f,
                                        endY = h
                                    )
                                )

                                drawPath(
                                    path = path,
                                    color = strokeColor,
                                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                                )
                            }

                            val lastIndex = data.lastIndex // Declara constante local (leitura única)
                            val endX = if (data.size > 1) lastIndex * step else w / 2f // Estrutura de decisão condicional principal
                            val endY = h * 0.1f + (h * 0.8f) * (1f - (data[lastIndex] / maxVal)) // Declara constante local (leitura única)

                            drawCircle(
                                color = strokeColor.copy(alpha = 0.2f),
                                radius = 6.dp.toPx(),
                                center = Offset(endX, endY)
                            )

                            // Destaca visualmente o último valor do gráfico
                            drawCircle(
                                color = strokeColor,
                                radius = 3.5.dp.toPx(),
                                center = Offset(endX, endY)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            // Distribuição dos torneios por categoria
            CardWrapper(
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    TranslatedText(
                        text = "Torneios por modalidade",
                        fontFamily = Geist,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = LMInk,
                        modifier = Modifier.padding(bottom = 12.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )

                    // Cores utilizadas para representar cada categoria
                    val colors = listOf( // Declara constante local (leitura única)
                        LMRed,
                        LMInk,
                        Color(0xFF2563EB),
                        Color(0xFF16A34A),
                        Color(0xFFCA8A04)
                    )

                    // Converte os dados recebidos para o formato utilizado pelo gráfico
                    val breakdown = estatisticas.torneiosPorEstado.mapIndexed { index, item -> // Declara constante local (leitura única)
                        Triple(
                            item.legenda,
                            item.valorNormalizado.toInt(),
                            colors.getOrElse(index) { LMGray500 }
                        )
                    }

                    // Mensagem apresentada caso não existam torneios registados
                    if (breakdown.isEmpty()) { // Estrutura de decisão condicional principal
                        TranslatedText(
                            text = "Sem torneios registados.",
                            fontFamily = Geist,
                            fontSize = 13.sp,
                            color = LMGray500
                        )
                    } else { // Fluxo condicional alternativo caso o 'if' seja falso
                        val maxValue = breakdown.maxOf { it.second }.coerceAtLeast(1) // Declara constante local (leitura única)

                        // Apresenta uma barra proporcional ao valor de cada categoria
                        breakdown.forEachIndexed { index, (modalidade, value, color) ->
                            Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                    .fillMaxWidth()
                                    .padding(bottom = if (index < breakdown.size - 1) 8.dp else 0.dp) // Estrutura de decisão condicional principal
                            ) {
                                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                                    modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                                        text = modalidade,
                                        fontFamily = Geist,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = LMGray700
                                    )

                                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                                        text = value.toString(),
                                        fontFamily = GeistMono,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LMInk
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                                Box( // Contentor Compose: Sobrepõe os elementos filhos
                                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .background(LMGray100, RoundedCornerShape(99.dp))
                                ) {
                                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                            .fillMaxWidth(
                                                fraction = (value.toFloat() / maxValue.toFloat())
                                                    .coerceIn(0f, 1f)
                                            )
                                            .fillMaxHeight()
                                            .background(color, RoundedCornerShape(99.dp))
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            // Distribuição percentual dos diferentes perfis de utilizador
            CardWrapper(
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                    modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Constrói os dados utilizados no gráfico circular
                    val profiles = estatisticas.topTorneios.mapIndexed { index, item -> // Declara constante local (leitura única)
                        val color = when (item.legenda.lowercase()) { // Escolha múltipla condicional (semelhante a switch-case)
                            "participantes" -> LMRed
                            "espectadores" -> LMInk
                            "organizadores" -> LMGray300
                            else -> when (index) { // Escolha múltipla condicional (semelhante a switch-case)
                                0 -> LMRed
                                1 -> LMInk
                                else -> LMGray300 // Fluxo condicional alternativo caso o 'if' seja falso
                            }
                        }

                        Triple(item.legenda, "${item.valorNormalizado.toInt()}%", color)
                    }

                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                        modifier = Modifier.size(76.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                        contentAlignment = Alignment.Center
                    ) {
                        // Gráfico circular que representa a distribuição dos utilizadores
                        Canvas(modifier = Modifier.fillMaxSize()) { // Modificador Compose: Define tamanho, margem, padding ou clique
                            val strokeWidth = 10.dp.toPx() // Declara constante local (leitura única)
                            val sizeOffset = strokeWidth // Declara constante local (leitura única)
                            val innerSize = Size(size.width - sizeOffset, size.height - sizeOffset) // Declara constante local (leitura única)
                            val centerOffset = Offset(sizeOffset / 2, sizeOffset / 2) // Declara constante local (leitura única)

                            drawCircle(
                                color = LMGray100,
                                radius = (size.width - strokeWidth) / 2,
                                style = Stroke(width = strokeWidth)
                            )

                            val values = estatisticas.topTorneios.map { it.valorNormalizado } // Declara constante local (leitura única)
                            val total = values.sum().coerceAtLeast(1f) // Declara constante local (leitura única)

                            var startAngle = -90f // Declara variável local (leitura e escrita)

                            // Calcula a dimensão de cada setor do gráfico circular
                            values.forEachIndexed { index, value ->
                                if (value > 0f) { // Estrutura de decisão condicional principal
                                    val color = when (index) { // Escolha múltipla condicional (semelhante a switch-case)
                                        0 -> LMRed
                                        1 -> LMInk
                                        else -> LMGray300 // Fluxo condicional alternativo caso o 'if' seja falso
                                    }

                                    val sweep = (value / total) * 360f // Declara constante local (leitura única)

                                    drawArc(
                                        color = color,
                                        startAngle = startAngle,
                                        sweepAngle = sweep,
                                        useCenter = false,
                                        topLeft = centerOffset,
                                        size = innerSize,
                                        style = Stroke(width = strokeWidth)
                                    )

                                    startAngle += sweep
                                }
                            }
                        }

                        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Valor total apresentado no centro do gráfico
                            Text( // Componente Compose: Desenha texto estruturado no ecrã
                                text = estatisticas.totalUtilizadores.toString(),
                                fontFamily = Bricolage,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = LMInk,
                                lineHeight = 14.sp
                            )

                            Text( // Componente Compose: Desenha texto estruturado no ecrã
                                text = "USERS",
                                fontFamily = Geist,
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Bold,
                                color = LMGray500,
                                lineHeight = 7.sp
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                        TranslatedText(
                            text = "Perfis",
                            fontFamily = Geist,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = LMInk,
                            modifier = Modifier.padding(bottom = 8.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                        )

                        if (profiles.isEmpty()) { // Estrutura de decisão condicional principal
                            TranslatedText(
                                text = "Sem utilizadores registados.",
                                fontFamily = Geist,
                                fontSize = 13.sp,
                                color = LMGray500
                            )
                        } else { // Fluxo condicional alternativo caso o 'if' seja falso
                            // Legenda que identifica cada categoria representada no gráfico
                            profiles.forEach { (label, value, color) ->
                                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                            .size(8.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(color)
                                    )

                                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                                        text = label,
                                        fontFamily = Geist,
                                        fontSize = 11.sp,
                                        color = LMGray600,
                                        modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                                    )

                                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                                        text = value,
                                        fontFamily = GeistMono,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LMInk
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
        }
    }
}