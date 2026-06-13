package com.leaguematch.ui.spectator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.ui.components.TranslatedText
import com.leaguematch.ui.theme.Bricolage
import com.leaguematch.ui.theme.Geist
import com.leaguematch.ui.theme.GeistMono
import com.leaguematch.ui.theme.LMGray500
import com.leaguematch.ui.theme.LMInk
import com.leaguematch.ui.theme.LMRed
import com.leaguematch.ui.theme.LMRed700
import com.leaguematch.ui.theme.LMWhite

@Composable
fun ExplorarScreen(
    liveMatches: List<Jogo>,
    trendingTournaments: List<Torneio>,
    onTorneioClick: (Torneio) -> Unit = {},
    onJogoClick: (Jogo) -> Unit = {}
) {
    var pesquisa by remember { mutableStateOf("") }
    var desportoSelecionado by remember { mutableStateOf("Todos") }

    val desportos = listOf("Todos", "Futebol", "Basquetebol", "Andebol", "Ténis", "Padel")

    val torneiosFiltrados = trendingTournaments.filter { torneio ->
        val desportoOk = desportoSelecionado == "Todos" || torneio.modalidade.equals(desportoSelecionado, ignoreCase = true)
        val pesquisaOk = pesquisa.isBlank() || torneio.nome.contains(pesquisa, ignoreCase = true)
        desportoOk && pesquisaOk
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp)
            .padding(top = 54.dp, bottom = 14.dp)
    ) {

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
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

        Spacer(modifier = Modifier.height(18.dp))

        // Barra de Pesquisa
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF3F3F5), shape = RoundedCornerShape(14.dp))
                .padding(horizontal = 12.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = LMGray500,
                modifier = Modifier.size(20.dp)
            )

            TextField(
                value = pesquisa,
                onValueChange = { pesquisa = it },
                placeholder = {
                    Text(
                        text = "Pesquisar torneios, equipas...",
                        fontFamily = Geist,
                        fontSize = 13.sp,
                        color = LMGray500
                    )
                },
                modifier = Modifier.weight(1f),
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

        Spacer(modifier = Modifier.height(20.dp))

        // Secção "AO VIVO" (A decorrer agora)
        if (liveMatches.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
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

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                liveMatches.forEach { jogo ->
                    LiveMatchCard(jogo = jogo, onClick = { onJogoClick(jogo) })
                }
            }

            Spacer(modifier = Modifier.height(22.dp))
        }

        // Tabs de Desportos
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            desportos.forEach { desporto ->
                val ativo = desporto == desportoSelecionado
                Surface(
                    modifier = Modifier.clickable { desportoSelecionado = desporto },
                    shape = RoundedCornerShape(22.dp),
                    color = if (ativo) LMInk else LMWhite,
                    border = BorderStroke(1.dp, if (ativo) LMInk else Color(0xFFE2E2E7))
                ) {
                    Text(
                        text = desporto,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (ativo) LMWhite else LMInk
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Torneios Populares
        Row(
            modifier = Modifier.fillMaxWidth(),
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

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (torneiosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
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
            } else {
                torneiosFiltrados.forEachIndexed { index, torneio ->
                    PopularTournamentCard(
                        torneio = torneio,
                        index = index,
                        onClick = { onTorneioClick(torneio) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun LiveMatchCard(
    jogo: Jogo,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(240.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = LMInk,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        color = Color(0xFF86EFAC)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.55f),
                        modifier = Modifier.size(11.dp)
                    )
                    Text(
                        text = "142",
                        fontFamily = GeistMono,
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.55f),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TeamRow(name = jogo.casa, score = jogo.resultadoCasa)
                TeamRow(name = jogo.fora, score = jogo.resultadoFora)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color.White.copy(alpha = 0.08f))
            Spacer(modifier = Modifier.height(10.dp))

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
private fun TeamRow(name: String, score: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .background(LMRed, shape = RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.take(1).uppercase(),
                color = LMWhite,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = name,
            fontFamily = Geist,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = LMWhite,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = score.toString(),
            fontFamily = Bricolage,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            color = LMWhite
        )
    }
}

@Composable
private fun PopularTournamentCard(
    torneio: Torneio,
    index: Int,
    onClick: () -> Unit
) {
    val gradient = when (torneio.modalidade.lowercase()) {
        "futebol" -> Brush.linearGradient(listOf(LMRed, LMRed700))
        "basquetebol" -> Brush.linearGradient(listOf(Color(0xFF2563EB), Color(0xFF1D4ED8)))
        "andebol" -> Brush.linearGradient(listOf(Color(0xFF16A34A), Color(0xFF15803D)))
        "padel" -> Brush.linearGradient(listOf(Color(0xFF1F2937), Color(0xFF111827)))
        else -> Brush.linearGradient(listOf(Color(0xFFBE123C), Color(0xFF9F1239)))
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = LMWhite,
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, Color(0xFFE8E8EC))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(brush = gradient, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = LMWhite,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = torneio.nome,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = LMInk,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${torneio.equipas} equipas · ${torneio.modalidade}",
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = "›",
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = LMGray500
            )
        }
    }
}

