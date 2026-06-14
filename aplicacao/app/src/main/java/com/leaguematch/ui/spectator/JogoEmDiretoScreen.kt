/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: JogoEmDiretoScreen.kt
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
import androidx.compose.material.icons.filled.Bolt // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.EmojiEvents // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Flag // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Report // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsSoccer // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsTennis // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SwapHoriz // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Visibility // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Warning // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.rounded.ArrowBack // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.rounded.Share // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.rounded.Star // Importa dependência / biblioteca necessária
import androidx.compose.material3.Button // Importa dependência / biblioteca necessária
import androidx.compose.material3.ButtonDefaults // Importa dependência / biblioteca necessária
import androidx.compose.material3.Card // Importa dependência / biblioteca necessária
import androidx.compose.material3.CardDefaults // Importa dependência / biblioteca necessária
import androidx.compose.material3.Divider // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.IconButton // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Surface // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.draw.clip // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Brush // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.style.TextAlign // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.style.TextOverflow // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.EstatisticaJogo // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.EventoJogo // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Jogo // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Bricolage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.GeistMono // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray400 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray600 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária

@Composable
fun JogoEmDiretoScreen( // Declaração de função / método de lógica
    jogo: Jogo,
    estatisticas: List<EstatisticaJogo>,
    eventos: List<EventoJogo>,
    onBackClick: () -> Unit,
    onVerEstatisticasClick: () -> Unit,
    modalidade: String = "Futebol"
) {
    val isFinished = jogo.estado.equals("Finalizado", ignoreCase = true) // Declara constante local (leitura única)
    val isOngoing = jogo.estado.equals("A Decorrer", ignoreCase = true) // Declara constante local (leitura única)

    Scaffold(
        topBar = {
            TopBarJogoEmDireto(onBackClick)
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
            // Scoreboard Card
            ScoreboardHeaderCard(jogo = jogo, isFinished = isFinished, isOngoing = isOngoing)
            Button( // Componente Compose: Desenha um botão interativo
                onClick = onVerEstatisticasClick, // Callback: Define a ação executada ao clicar no componente
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                colors = ButtonDefaults.buttonColors(
                    containerColor = LMRed
                )
            ) {
                TranslatedText(
                    text = "Ver Estatísticas Completas",
                    color = Color.White
                )
            }

            // Direct Stats Section
            EstatisticasDiretoSection(
                estatisticas = estatisticas,
                eventos = eventos,
                modalidade = modalidade
            )

            // Timeline Section
            TimelineEventosSection(eventos = eventos)
        }
    }
}

@Composable
private fun TopBarJogoEmDireto(onBackClick: () -> Unit) { // Declaração de função / método de lógica
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
                contentDescription = "Retroceder",
                tint = Color.Black
            )
        }

        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = "Detalhes do Jogo",
            fontFamily = Geist,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
        )

        IconButton(onClick = { /* Seguir jogo */ }) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                imageVector = Icons.Rounded.Star,
                contentDescription = "Favorito",
                tint = LMGray500
            )
        }

        IconButton(onClick = { /* Partilhar */ }) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                imageVector = Icons.Rounded.Share,
                contentDescription = "Partilhar",
                tint = LMGray500
            )
        }
    }
}

@Composable
private fun ScoreboardHeaderCard( // Declaração de função / método de lógica
    jogo: Jogo,
    isFinished: Boolean,
    isOngoing: Boolean
) {
    Box( // Contentor Compose: Sobrepõe os elementos filhos
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0A0A0B), Color(0xFF1F1F22))
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Live / Finished Badge and viewer count
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val badgeBg = when { // Escolha múltipla condicional (semelhante a switch-case)
                    isOngoing -> Color(0xFF22C55E).copy(alpha = 0.2f)
                    isFinished -> Color(0xFF52525B).copy(alpha = 0.2f)
                    else -> Color(0xFFF59E0B).copy(alpha = 0.2f) // Fluxo condicional alternativo caso o 'if' seja falso
                }
                val badgeColor = when { // Escolha múltipla condicional (semelhante a switch-case)
                    isOngoing -> Color(0xFF86EFAC)
                    isFinished -> Color(0xFFD4D4D8)
                    else -> Color(0xFFFDE68A) // Fluxo condicional alternativo caso o 'if' seja falso
                }
                val badgeText = when { // Escolha múltipla condicional (semelhante a switch-case)
                    isOngoing -> "Em direto"
                    isFinished -> "Terminado"
                    else -> "Agendado" // Fluxo condicional alternativo caso o 'if' seja falso
                }

                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = badgeBg
                ) {
                    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isOngoing) { // Estrutura de decisão condicional principal
                            Box( // Contentor Compose: Sobrepõe os elementos filhos
                                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                    .padding(end = 6.dp)
                                    .size(6.dp)
                                    .background(Color(0xFF22C55E), CircleShape)
                            )
                        }
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = badgeText.uppercase(),
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = badgeColor
                        )
                    }
                }

                if (isOngoing) { // Estrutura de decisão condicional principal
                    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon( // Componente Compose: Desenha um ícone vetorial
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.5f),
                            modifier = Modifier.size(13.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                        )
                        TranslatedText(
                            text = "142 a assistir",
                            fontFamily = Geist,
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            // Scoreboard (Team names & Scores)
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Team A (Casa)
                Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                    modifier = Modifier.weight(1f), // Modificador Compose: Define tamanho, margem, padding ou clique
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .size(56.dp)
                            .background(
                                brush = Brush.linearGradient(listOf(LMRed, Color(0xFF0A0A0B))),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = jogo.casa.split("\\s+".toRegex()).take(2).mapNotNull { it.firstOrNull()?.toString() }.joinToString("").uppercase(),
                            color = LMWhite,
                            fontFamily = Bricolage,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = jogo.casa,
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = LMWhite,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Score / VS
                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                    modifier = Modifier.padding(horizontal = 8.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (isOngoing || isFinished) { // Estrutura de decisão condicional principal
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = jogo.resultadoCasa.toString(),
                            fontFamily = Bricolage,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 42.sp,
                            color = LMWhite
                        )
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = "·",
                            fontFamily = Bricolage,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp,
                            color = Color.White.copy(alpha = 0.3f)
                        )
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = jogo.resultadoFora.toString(),
                            fontFamily = Bricolage,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 42.sp,
                            color = LMWhite.copy(alpha = 0.9f)
                        )
                    } else { // Fluxo condicional alternativo caso o 'if' seja falso
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = "VS",
                            fontFamily = Bricolage,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            color = Color.White.copy(alpha = 0.4f)
                        )
                    }
                }

                // Team B (Fora)
                Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                    modifier = Modifier.weight(1f), // Modificador Compose: Define tamanho, margem, padding ou clique
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .size(56.dp)
                            .background(
                                brush = Brush.linearGradient(listOf(Color(0xFF27272A), Color(0xFF0A0A0B))),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = jogo.fora.split("\\s+".toRegex()).take(2).mapNotNull { it.firstOrNull()?.toString() }.joinToString("").uppercase(),
                            color = LMWhite,
                            fontFamily = Bricolage,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = jogo.fora,
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = LMWhite,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
            Divider(color = Color.White.copy(alpha = 0.08f))
            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            // Footer Information
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TranslatedText(
                    text = "Torneio ID: ${jogo.torneioId}",
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.55f)
                )

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = jogo.local,
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.55f)
                )
            }
        }
    }
}

@Composable
private fun EstatisticasDiretoSection( // Declaração de função / método de lógica
    estatisticas: List<EstatisticaJogo>,
    eventos: List<EventoJogo>,
    modalidade: String = "Futebol"
) {
    Card( // Contentor Compose: Cartão visual com elevação e cantos
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LMWhite),
        border = BorderStroke(1.dp, Color(0xFFE2E2E7))
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier.padding(14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TranslatedText(
                text = "Estatísticas em direto",
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = LMGray500,
                letterSpacing = 0.4.sp
            )

            val base = com.leaguematch.ui.organizer.estatisticasPorModalidade(modalidade).flatMap { // Declara constante local (leitura única)
                listOf(
                    EstatisticaJogo(tipo = it.titulo, equipa = "casa", valor = it.casa),
                    EstatisticaJogo(tipo = it.titulo, equipa = "fora", valor = it.fora)
                )
            }.toMutableList()

            estatisticas.forEach { salva ->
                val index = base.indexOfFirst { // Declara constante local (leitura única)
                    it.tipo.equals(salva.tipo, ignoreCase = true) &&
                            it.equipa.equals(salva.equipa, ignoreCase = true)
                }

                if (index >= 0) { // Estrutura de decisão condicional principal
                    base[index] = salva
                }
            }

            fun contarEvento(tipo: String, equipa: String): Int { // Declaração de função / método de lógica
                return eventos.count { // Retorna o resultado da execução da função
                    it.tipo.equals(tipo, ignoreCase = true) &&
                            it.equipa.equals(equipa, ignoreCase = true)
                }
            }

            fun atualizarStat(titulo: String, casa: Int, fora: Int) { // Declaração de função / método de lógica
                val indexCasa = base.indexOfFirst { // Declara constante local (leitura única)
                    it.tipo.equals(titulo, ignoreCase = true) &&
                            it.equipa.equals("casa", ignoreCase = true)
                }

                val indexFora = base.indexOfFirst { // Declara constante local (leitura única)
                    it.tipo.equals(titulo, ignoreCase = true) &&
                            it.equipa.equals("fora", ignoreCase = true)
                }

                if (indexCasa >= 0) { // Estrutura de decisão condicional principal
                    base[indexCasa] = base[indexCasa].copy(valor = casa)
                }

                if (indexFora >= 0) { // Estrutura de decisão condicional principal
                    base[indexFora] = base[indexFora].copy(valor = fora)
                }
            }

            atualizarStat(
                "Cantos",
                contarEvento("CANTO", "casa"),
                contarEvento("CANTO", "fora")
            )

            atualizarStat(
                "Faltas",
                contarEvento("FALTA", "casa"),
                contarEvento("FALTA", "fora")
            )

            atualizarStat(
                "Cartões amarelos",
                contarEvento("CARTAO_AMARELO", "casa") + contarEvento("AMARELO", "casa"),
                contarEvento("CARTAO_AMARELO", "fora") + contarEvento("AMARELO", "fora")
            )

            atualizarStat(
                "Cartões vermelhos",
                contarEvento("CARTAO_VERMELHO", "casa") + contarEvento("VERMELHO", "casa"),
                contarEvento("CARTAO_VERMELHO", "fora") + contarEvento("VERMELHO", "fora")
            )
            atualizarStat(
                "Aces",
                contarEvento("ACE", "casa"),
                contarEvento("ACE", "fora")
            )

            atualizarStat(
                "Break points",
                contarEvento("BREAK_POINT", "casa"),
                contarEvento("BREAK_POINT", "fora")
            )

            atualizarStat(
                "Erros",
                contarEvento("DUPLA_FALTA", "casa"),
                contarEvento("DUPLA_FALTA", "fora")
            )

            atualizarStat(
                "Remates",
                contarEvento("GOLO", "casa"),
                contarEvento("GOLO", "fora")
            )

            atualizarStat(
                "Faltas",
                contarEvento("FALTA", "casa") + contarEvento("EXCLUSAO_2_MIN", "casa"),
                contarEvento("FALTA", "fora") + contarEvento("EXCLUSAO_2_MIN", "fora")
            )

            atualizarStat(
                "Defesas",
                0,
                0
            )

            val estatisticasExibicao = base // Declara constante local (leitura única)

            // Group by statistical metric (e.g. "Posse de Bola", "Remates", etc.)
            val groupedStats = estatisticasExibicao.groupBy { it.tipo } // Declara constante local (leitura única)
            groupedStats.forEach { (tipo, list) ->
                val casaVal = list.firstOrNull { it.equipa == "casa" }?.valor ?: 0 // Declara constante local (leitura única)
                val foraVal = list.firstOrNull { it.equipa == "fora" }?.valor ?: 0 // Declara constante local (leitura única)

                val total = (casaVal + foraVal).coerceAtLeast(1) // Declara constante local (leitura única)
                val casaPeso = casaVal.toFloat() / total // Declara constante local (leitura única)
                val foraPeso = foraVal.toFloat() / total // Declara constante local (leitura única)

                val suffix = if (tipo.contains("Posse", ignoreCase = true)) "%" else "" // Estrutura de decisão condicional principal

                    Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                            modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text( // Componente Compose: Desenha texto estruturado no ecrã
                                text = "$casaVal$suffix",
                                fontFamily = GeistMono,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = LMInk
                            )

                            Text( // Componente Compose: Desenha texto estruturado no ecrã
                                text = tipo,
                                fontFamily = Geist,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = LMGray500
                            )

                            Text( // Componente Compose: Desenha texto estruturado no ecrã
                                text = "$foraVal$suffix",
                                fontFamily = GeistMono,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = LMInk
                            )
                        }

                        // Progressive comparison bar
                        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFFF3F3F5))
                        ) {
                            if (casaPeso > 0f) { // Estrutura de decisão condicional principal
                                Box( // Contentor Compose: Sobrepõe os elementos filhos
                                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                        .fillMaxHeight()
                                        .weight(casaPeso)
                                        .background(LMRed)
                                )
                            }
                            if (foraPeso > 0f) { // Estrutura de decisão condicional principal
                                Box( // Contentor Compose: Sobrepõe os elementos filhos
                                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                        .fillMaxHeight()
                                        .weight(foraPeso)
                                        .background(Color(0xFF0A0A0B))
                                )
                            }
                    }
                }
            }
        }
    }
}

@Composable
private fun TimelineEventosSection(eventos: List<EventoJogo>) { // Declaração de função / método de lógica
    Column( // Contentor Compose: Alinha os filhos numa coluna vertical
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TranslatedText(
            text = "Cronologia",
            fontFamily = Geist,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp,
            color = LMInk
        )

        if (eventos.isEmpty()) { // Estrutura de decisão condicional principal
            Card( // Contentor Compose: Cartão visual com elevação e cantos
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = LMWhite),
                border = BorderStroke(1.dp, Color(0xFFE2E2E7))
            ) {
                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TranslatedText(
                        text = "Nenhum evento registado neste jogo.",
                        fontFamily = Geist,
                        fontSize = 12.sp,
                        color = LMGray500
                    )
                }
            }
        } else { // Fluxo condicional alternativo caso o 'if' seja falso
            eventos.forEach { evento ->
                val icon = when (evento.tipo.uppercase()) { // Escolha múltipla condicional (semelhante a switch-case)
                    "GOLO" -> Icons.Default.SportsSoccer
                    "FALTA" -> Icons.Default.Warning
                    "AMARELO" -> Icons.Default.Warning
                    "VERMELHO" -> Icons.Default.Report
                    "CANTO" -> Icons.Default.Flag
                    "SUBSTITUICAO" -> Icons.Default.SwapHoriz
                    "ACE" -> Icons.Default.SportsTennis
                    "BREAK_POINT" -> Icons.Default.Bolt
                    else -> Icons.Default.EmojiEvents // Fluxo condicional alternativo caso o 'if' seja falso
                }

                val iconBg = when (evento.tipo.uppercase()) { // Escolha múltipla condicional (semelhante a switch-case)
                    "GOLO", "ACE" -> Color(0xFF22C55E).copy(alpha = 0.18f)
                    "FALTA", "AMARELO" -> Color(0xFFF59E0B).copy(alpha = 0.18f)
                    "VERMELHO" -> Color(0xFFEF4444).copy(alpha = 0.18f)
                    else -> Color(0xFF3B82F6).copy(alpha = 0.18f) // Fluxo condicional alternativo caso o 'if' seja falso
                }

                val iconColor = when (evento.tipo.uppercase()) { // Escolha múltipla condicional (semelhante a switch-case)
                    "GOLO", "ACE" -> Color(0xFF16A34A)
                    "FALTA", "AMARELO" -> Color(0xFFD97706)
                    "VERMELHO" -> Color(0xFFDC2626)
                    else -> Color(0xFF2563EB) // Fluxo condicional alternativo caso o 'if' seja falso
                }

                val eventLabel = when (evento.tipo.uppercase()) { // Escolha múltipla condicional (semelhante a switch-case)
                    "GOLO" -> "Golo"
                    "FALTA" -> "Falta"
                    "AMARELO" -> "Cartão Amarelo"
                    "VERMELHO" -> "Cartão Vermelho"
                    "CANTO" -> "Canto"
                    "SUBSTITUICAO" -> "Substituição"
                    "DOIS_PONTOS" -> "2 Pontos"
                    "TRES_PONTOS" -> "3 Pontos"
                    "LANCE_LIVRE" -> "Lance Livre"
                    "ACE" -> "Ace"
                    "BREAK_POINT" -> "Break Point"
                    else -> evento.tipo // Fluxo condicional alternativo caso o 'if' seja falso
                }

                Card( // Contentor Compose: Cartão visual com elevação e cantos
                    modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = LMWhite),
                    border = BorderStroke(1.dp, Color(0xFFE2E2E7))
                ) {
                    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Minute / Tempo
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = "${evento.tempo}'",
                            fontFamily = GeistMono,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = LMGray600,
                            modifier = Modifier.width(36.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                        )

                        // Icon box
                        Box( // Contentor Compose: Sobrepõe os elementos filhos
                            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                .size(28.dp)
                                .background(iconBg, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon( // Componente Compose: Desenha um ícone vetorial
                                imageVector = icon,
                                contentDescription = null,
                                tint = iconColor,
                                modifier = Modifier.size(14.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                        // Event content
                        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                            modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                        ) {
                            Text( // Componente Compose: Desenha texto estruturado no ecrã
                                text = "$eventLabel de ${evento.userName}",
                                fontFamily = Geist,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = LMInk
                            )
                        }

                        // Side
                        if (evento.equipa != "center") { // Estrutura de decisão condicional principal
                            TranslatedText(
                                text = if (evento.equipa == "casa") "CASA" else "FORA", // Estrutura de decisão condicional principal
                                fontFamily = Geist,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = LMGray400
                            )
                        }
                    }
                }
            }
        }
    }
}
