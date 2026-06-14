/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: OrgEstatisticasTorneioScreen.kt
 * Tipo: Interface (Compose View) do Organizador
 *
 * Descrição:
 * Este ficheiro define um ecrã da área do Organizador em Jetpack Compose.\n * Fornece interface e lógica visual para criar torneios, gerir equipas, registar e editar jogos e estatísticas.
 */
package com.leaguematch.ui.organizer // Define o pacote deste ficheiro de código

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
import androidx.compose.material.icons.filled.Groups // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.QueryStats // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsScore // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsSoccer // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Style // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Warning // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.rounded.ArrowBack // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.IconButton // Importa dependência / biblioteca necessária
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
import com.leaguematch.data.remote.model.DetalheTorneio // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Bricolage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMBorder // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray100 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray400 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray50 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed50 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária

@Composable
fun OrgEstatisticasTorneioScreen( // Declaração de função / método de lógica
    detalhe: DetalheTorneio,
    onBackClick: () -> Unit
) {
    val totalJogos = detalhe.jogos.size // Declara constante local (leitura única)
    val jogosFinalizados = detalhe.jogos.count { it.estado.equals("Finalizado", ignoreCase = true) } // Declara constante local (leitura única)
    val jogosAoVivo = detalhe.jogos.count { it.estado.equals("A Decorrer", ignoreCase = true) } // Declara constante local (leitura única)
    val jogosAgendados = detalhe.jogos.count { // Declara constante local (leitura única)
        it.estado.equals("Agendado", ignoreCase = true) || it.estado.equals("Por iniciar", ignoreCase = true)
    }
    val totalGolos = detalhe.totalGolos // Declara constante local (leitura única)
    val mediaGolos = if (jogosFinalizados > 0) totalGolos.toDouble() / jogosFinalizados else 0.0 // Estrutura de decisão condicional principal

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
                .padding(bottom = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row(verticalAlignment = Alignment.CenterVertically) { // Contentor Compose: Alinha os filhos numa linha horizontal
                IconButton(onClick = onBackClick) { // Componente Compose: Desenha um botão com ícone
                    Icon(Icons.Rounded.ArrowBack, contentDescription = null, tint = LMInk) // Componente Compose: Desenha um ícone vetorial
                }
                Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    TranslatedText(
                        text = "Estatísticas",
                        fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp, color = LMInk
                    )
                    Text(text = detalhe.torneio.nome, fontFamily = Geist, fontSize = 12.sp, color = LMGray500) // Componente Compose: Desenha texto estruturado no ecrã
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { // Contentor Compose: Alinha os filhos numa linha horizontal
                StatCell(
                    label = "Equipas",
                    value = detalhe.torneio.equipas.toString(),
                    icon = Icons.Default.Groups,
                    color = LMRed,
                    bg = LMRed50,
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
                StatCell(
                    label = "Jogos",
                    value = totalJogos.toString(),
                    icon = Icons.Default.SportsScore,
                    color = Color(0xFF2563EB),
                    bg = Color(0xFFDBEAFE),
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { // Contentor Compose: Alinha os filhos numa linha horizontal
                StatCell(
                    label = "Golos",
                    value = totalGolos.toString(),
                    icon = Icons.Default.SportsSoccer,
                    color = Color(0xFF16A34A),
                    bg = Color(0xFFDCFCE7),
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
                StatCell(
                    label = "Média / jogo",
                    value = "%.1f".format(mediaGolos),
                    icon = Icons.Default.QueryStats,
                    color = Color(0xFF0891B2),
                    bg = Color(0xFFCFFAFE),
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { // Contentor Compose: Alinha os filhos numa linha horizontal
                StatCell(
                    label = "Faltas",
                    value = detalhe.totalFaltas.toString(),
                    icon = Icons.Default.Warning,
                    color = Color(0xFFD97706),
                    bg = Color(0xFFFEF3C7),
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
                StatCell(
                    label = "Cartões",
                    value = detalhe.totalCartoes.toString(),
                    icon = Icons.Default.Style,
                    color = Color(0xFFDC2626),
                    bg = Color(0xFFFEE2E2),
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.height(20.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            TranslatedText(
                text = "ESTADO DOS JOGOS",
                fontFamily = Geist, fontWeight = FontWeight.Bold,
                fontSize = 11.sp, color = LMGray500, letterSpacing = 0.4.sp
            )

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            EstadoLinha("Finalizados", jogosFinalizados, totalJogos, Color(0xFF16A34A))
            Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
            EstadoLinha("A decorrer", jogosAoVivo, totalJogos, Color(0xFFEA580C))
            Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
            EstadoLinha("Agendados", jogosAgendados, totalJogos, LMGray500)

            Spacer(modifier = Modifier.height(20.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            TranslatedText(
                text = "MELHORES MARCADORES",
                fontFamily = Geist, fontWeight = FontWeight.Bold,
                fontSize = 11.sp, color = LMGray500, letterSpacing = 0.4.sp
            )

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            if (detalhe.goleadores.isEmpty()) { // Estrutura de decisão condicional principal
                Surface(
                    modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                    shape = RoundedCornerShape(16.dp),
                    color = LMGray50,
                    border = BorderStroke(1.dp, LMBorder)
                ) {
                    TranslatedText(
                        text = "Sem marcadores registados.",
                        modifier = Modifier.padding(18.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                        fontFamily = Geist, fontSize = 13.sp, color = LMGray500
                    )
                }
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    detalhe.goleadores.take(10).forEachIndexed { index, goleador ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                            shape = RoundedCornerShape(14.dp),
                            color = LMWhite,
                            border = BorderStroke(1.dp, LMBorder)
                        ) {
                            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text( // Componente Compose: Desenha texto estruturado no ecrã
                                    text = "${index + 1}.",
                                    fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp, color = LMRed,
                                    modifier = Modifier.width(28.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                                )
                                Icon( // Componente Compose: Desenha um ícone vetorial
                                    Icons.Default.EmojiEvents, contentDescription = null,
                                    tint = LMGray400, modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                                )
                                Spacer(modifier = Modifier.width(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                                Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                                        text = goleador.nome,
                                        fontFamily = Geist, fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp, color = LMInk
                                    )
                                    if (goleador.equipa.isNotBlank()) { // Estrutura de decisão condicional principal
                                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                                            text = goleador.equipa,
                                            fontFamily = Geist, fontSize = 11.sp, color = LMGray500
                                        )
                                    }
                                }
                                TranslatedText(
                                    text = "${goleador.golos} golos",
                                    fontFamily = Geist, fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp, color = LMInk
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCell( // Declaração de função / método de lógica
    label: String,
    value: String,
    icon: ImageVector,
    color: Color,
    bg: Color,
    modifier: Modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, LMBorder),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(36.dp)
                    .background(bg, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp)) // Componente Compose: Desenha um ícone vetorial
            }
            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = value,
                fontFamily = Bricolage, fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp, color = LMInk
            )
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = label,
                fontFamily = Geist, fontSize = 12.sp, color = LMGray500
            )
        }
    }
}

@Composable
private fun EstadoLinha(label: String, valor: Int, total: Int, cor: Color) { // Declaração de função / método de lógica
    val fracao = if (total > 0) valor.toFloat() / total else 0f // Estrutura de decisão condicional principal

    Column { // Contentor Compose: Alinha os filhos numa coluna vertical
        Row { // Contentor Compose: Alinha os filhos numa linha horizontal
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = label,
                fontFamily = Geist, fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp, color = LMInk,
                modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
            )
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = "$valor / $total",
                fontFamily = Geist, fontWeight = FontWeight.Bold,
                fontSize = 12.sp, color = LMGray500
            )
        }
        Spacer(modifier = Modifier.height(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
        Box( // Contentor Compose: Sobrepõe os elementos filhos
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxWidth()
                .height(8.dp)
                .background(LMGray100, RoundedCornerShape(50))
        ) {
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth(fracao)
                    .height(8.dp)
                    .background(cor, RoundedCornerShape(50))
            )
        }
    }
}
