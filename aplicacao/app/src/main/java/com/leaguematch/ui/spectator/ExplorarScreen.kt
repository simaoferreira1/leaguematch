/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: ExplorarScreen.kt
 * Tipo: Interface (Compose View) do Espectador
 *
 * Descrição:
 * Este ficheiro define um ecrã de visualização pública (Espectador) em Jetpack Compose.\n * Apenas exibe dados para leitura (como tabelas de classificação, jogos ao vivo e calendários) sem permitir alteração.
 */
package com.leaguematch.ui.spectator // Define o pacote deste ficheiro de código

import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.clickable // Importa dependência / biblioteca necessária
import androidx.compose.foundation.horizontalScroll // Importa dependência / biblioteca necessária
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
import androidx.compose.material.icons.filled.Search // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Visibility // Importa dependência / biblioteca necessária
import androidx.compose.material3.Divider // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.Surface // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.material3.TextField // Importa dependência / biblioteca necessária
import androidx.compose.material3.TextFieldDefaults // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.remember // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Brush // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.style.TextOverflow // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Jogo // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Torneio // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Bricolage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.GeistMono // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed700 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária

@Composable
fun ExplorarScreen( // Declaração de função / método de lógica
    liveMatches: List<Jogo>,
    trendingTournaments: List<Torneio>,
    onTorneioClick: (Torneio) -> Unit = {},
    onJogoClick: (Jogo) -> Unit = {}
) {
    var pesquisa by remember { mutableStateOf("") } // Declara estado mutável local do Compose
    var desportoSelecionado by remember { mutableStateOf("Todos") } // Declara estado mutável local do Compose

    val desportos = listOf("Todos", "Futebol", "Basquetebol", "Andebol", "Ténis", "Padel") // Declara constante local (leitura única)

    val torneiosFiltrados = trendingTournaments.filter { torneio -> // Declara constante local (leitura única)
        val desportoOk = desportoSelecionado == "Todos" || torneio.modalidade.equals(desportoSelecionado, ignoreCase = true) // Declara constante local (leitura única)
        val pesquisaOk = pesquisa.isBlank() || torneio.nome.contains(pesquisa, ignoreCase = true) // Declara constante local (leitura única)
        desportoOk && pesquisaOk
    }

    Column( // Contentor Compose: Alinha os filhos numa coluna vertical
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp)
            .padding(top = 54.dp, bottom = 14.dp)
    ) {

        // Header
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                TranslatedText(
                    text = "Explorar",
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = LMInk,
                    letterSpacing = (-0.8).sp
                )
                TranslatedText(
                    text = "Descobre torneios e jogos em direto",
                    fontFamily = Geist,
                    fontSize = 13.sp,
                    color = LMGray500,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

        // Barra de Pesquisa
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxWidth()
                .background(Color(0xFFF3F3F5), shape = RoundedCornerShape(14.dp))
                .padding(horizontal = 12.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = LMGray500,
                modifier = Modifier.size(20.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )

            TextField( // Campo Compose: Entrada de texto simples para utilizador
                value = pesquisa,
                onValueChange = { pesquisa = it },
                placeholder = {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "Pesquisar torneios, equipas...",
                        fontFamily = Geist,
                        fontSize = 13.sp,
                        color = LMGray500
                    )
                },
                modifier = Modifier.weight(1f), // Modificador Compose: Define tamanho, margem, padding ou clique
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        Spacer(modifier = Modifier.height(20.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

        // Secção "AO VIVO" (A decorrer agora)
        if (liveMatches.isNotEmpty()) { // Estrutura de decisão condicional principal
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .size(8.dp)
                            .background(LMRed, shape = RoundedCornerShape(99.dp))
                    )
                    TranslatedText(
                        text = "A decorrer agora",
                        fontFamily = Geist,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = LMInk
                    )
                }

                TranslatedText(
                    text = "Ver tudo ›",
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = LMRed
                )
            }

            Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                liveMatches.forEach { jogo ->
                    LiveMatchCard(jogo = jogo, onClick = { onJogoClick(jogo) }) // Callback: Define a ação executada ao clicar no componente
                }
            }

            Spacer(modifier = Modifier.height(22.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
        }

        // Tabs de Desportos
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            desportos.forEach { desporto ->
                val ativo = desporto == desportoSelecionado // Declara constante local (leitura única)
                Surface(
                    modifier = Modifier.clickable { desportoSelecionado = desporto }, // Modificador Compose: Define tamanho, margem, padding ou clique
                    shape = RoundedCornerShape(22.dp),
                    color = if (ativo) LMInk else LMWhite, // Estrutura de decisão condicional principal
                    border = BorderStroke(1.dp, if (ativo) LMInk else Color(0xFFE2E2E7)) // Estrutura de decisão condicional principal
                ) {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = desporto,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (ativo) LMWhite else LMInk // Estrutura de decisão condicional principal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

        // Torneios Populares
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TranslatedText(
                text = "Torneios populares",
                fontFamily = Geist,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = LMInk
            )
            TranslatedText(
                text = "Ver tudo ›",
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = LMRed
            )
        }

        Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (torneiosFiltrados.isEmpty()) { // Estrutura de decisão condicional principal
                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .fillMaxWidth()
                        .padding(vertical = 30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TranslatedText(
                        text = "Nenhum torneio encontrado.",
                        fontFamily = Geist,
                        fontSize = 13.sp,
                        color = LMGray500
                    )
                }
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                torneiosFiltrados.forEachIndexed { index, torneio ->
                    PopularTournamentCard(
                        torneio = torneio,
                        index = index,
                        onClick = { onTorneioClick(torneio) } // Callback: Define a ação executada ao clicar no componente
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
    }
}

@Composable
private fun LiveMatchCard( // Declaração de função / método de lógica
    jogo: Jogo,
    onClick: () -> Unit // Callback: Define a ação executada ao clicar no componente
) {
    Surface(
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .width(240.dp)
            .clickable { onClick() }, // Callback: Define a ação executada ao clicar no componente
        shape = RoundedCornerShape(16.dp),
        color = LMInk,
        tonalElevation = 1.dp
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier.padding(14.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
        ) {
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Pill
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFF15803D).copy(alpha = 0.18f)
                ) {
                    TranslatedText(
                        text = "EM DIRETO",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        color = Color(0xFF86EFAC)
                    )
                }

                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.55f),
                        modifier = Modifier.size(11.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "142",
                        fontFamily = GeistMono,
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.55f),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TeamRow(name = jogo.casa, score = jogo.resultadoCasa)
                TeamRow(name = jogo.fora, score = jogo.resultadoFora)
            }

            Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
            Divider(color = Color.White.copy(alpha = 0.08f))
            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            TranslatedText(
                text = "Torneio ID: ${jogo.torneioId}",
                fontFamily = Geist,
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.55f)
            )
        }
    }
}

@Composable
private fun TeamRow(name: String, score: Int) { // Declaração de função / método de lógica
    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box( // Contentor Compose: Sobrepõe os elementos filhos
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .size(22.dp)
                .background(LMRed, shape = RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = name.take(1).uppercase(),
                color = LMWhite,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = name,
            fontFamily = Geist,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = LMWhite,
            modifier = Modifier.weight(1f), // Modificador Compose: Define tamanho, margem, padding ou clique
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = score.toString(),
            fontFamily = Bricolage,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            color = LMWhite
        )
    }
}

@Composable
private fun PopularTournamentCard( // Declaração de função / método de lógica
    torneio: Torneio,
    index: Int,
    onClick: () -> Unit // Callback: Define a ação executada ao clicar no componente
) {
    val gradient = when (torneio.modalidade.lowercase()) { // Escolha múltipla condicional (semelhante a switch-case)
        "futebol" -> Brush.linearGradient(listOf(LMRed, LMRed700))
        "basquetebol" -> Brush.linearGradient(listOf(Color(0xFF2563EB), Color(0xFF1D4ED8)))
        "andebol" -> Brush.linearGradient(listOf(Color(0xFF16A34A), Color(0xFF15803D)))
        "padel" -> Brush.linearGradient(listOf(Color(0xFF1F2937), Color(0xFF111827)))
        else -> Brush.linearGradient(listOf(Color(0xFFBE123C), Color(0xFF9F1239))) // Fluxo condicional alternativo caso o 'if' seja falso
    }

    Surface(
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .clickable { onClick() }, // Callback: Define a ação executada ao clicar no componente
        shape = RoundedCornerShape(20.dp),
        color = LMWhite,
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, Color(0xFFE8E8EC))
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(58.dp)
                    .background(brush = gradient, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon( // Componente Compose: Desenha um ícone vetorial
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = LMWhite,
                    modifier = Modifier.size(28.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.width(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = torneio.nome,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = LMInk,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "${torneio.equipas} equipas · ${torneio.modalidade}",
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = "›",
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = LMGray500
            )
        }
    }
}

