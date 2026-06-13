package com.leaguematch.ui.spectator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.EstatisticaJogo
import com.leaguematch.data.remote.model.EventoJogo
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.ui.components.TranslatedText
import com.leaguematch.ui.theme.Bricolage
import com.leaguematch.ui.theme.Geist
import com.leaguematch.ui.theme.GeistMono
import com.leaguematch.ui.theme.LMGray400
import com.leaguematch.ui.theme.LMGray500
import com.leaguematch.ui.theme.LMGray600
import com.leaguematch.ui.theme.LMInk
import com.leaguematch.ui.theme.LMRed
import com.leaguematch.ui.theme.LMWhite

@Composable
fun JogoEmDiretoScreen(
    jogo: Jogo,
    estatisticas: List<EstatisticaJogo>,
    eventos: List<EventoJogo>,
    onBackClick: () -> Unit,
    onVerEstatisticasClick: () -> Unit,
    modalidade: String = "Futebol"
) {
    val isFinished = jogo.estado.equals("Finalizado", ignoreCase = true)
    val isOngoing = jogo.estado.equals("A Decorrer", ignoreCase = true)

    Scaffold(
        topBar = {
            TopBarJogoEmDireto(onBackClick)
        },
        containerColor = Color(0xFFF6F6F8)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Scoreboard Card
            ScoreboardHeaderCard(jogo = jogo, isFinished = isFinished, isOngoing = isOngoing)
            Button(
                onClick = onVerEstatisticasClick,
                modifier = Modifier.fillMaxWidth(),
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
private fun TopBarJogoEmDireto(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = "Retroceder",
                tint = Color.Black
            )
        }

        Text(
            text = "Detalhes do Jogo",
            fontFamily = Geist,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = { /* Seguir jogo */ }) {
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = "Favorito",
                tint = LMGray500
            )
        }

        IconButton(onClick = { /* Partilhar */ }) {
            Icon(
                imageVector = Icons.Rounded.Share,
                contentDescription = "Partilhar",
                tint = LMGray500
            )
        }
    }
}

@Composable
private fun ScoreboardHeaderCard(
    jogo: Jogo,
    isFinished: Boolean,
    isOngoing: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0A0A0B), Color(0xFF1F1F22))
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Live / Finished Badge and viewer count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val badgeBg = when {
                    isOngoing -> Color(0xFF22C55E).copy(alpha = 0.2f)
                    isFinished -> Color(0xFF52525B).copy(alpha = 0.2f)
                    else -> Color(0xFFF59E0B).copy(alpha = 0.2f)
                }
                val badgeColor = when {
                    isOngoing -> Color(0xFF86EFAC)
                    isFinished -> Color(0xFFD4D4D8)
                    else -> Color(0xFFFDE68A)
                }
                val badgeText = when {
                    isOngoing -> "Em direto"
                    isFinished -> "Terminado"
                    else -> "Agendado"
                }

                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = badgeBg
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isOngoing) {
                            Box(
                                modifier = Modifier
                                    .padding(end = 6.dp)
                                    .size(6.dp)
                                    .background(Color(0xFF22C55E), CircleShape)
                            )
                        }
                        Text(
                            text = badgeText.uppercase(),
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = badgeColor
                        )
                    }
                }

                if (isOngoing) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.5f),
                            modifier = Modifier.size(13.dp)
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

            Spacer(modifier = Modifier.height(18.dp))

            // Scoreboard (Team names & Scores)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Team A (Casa)
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                brush = Brush.linearGradient(listOf(LMRed, Color(0xFF0A0A0B))),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = jogo.casa.split("\\s+".toRegex()).take(2).mapNotNull { it.firstOrNull()?.toString() }.joinToString("").uppercase(),
                            color = LMWhite,
                            fontFamily = Bricolage,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    Text(
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
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (isOngoing || isFinished) {
                        Text(
                            text = jogo.resultadoCasa.toString(),
                            fontFamily = Bricolage,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 42.sp,
                            color = LMWhite
                        )
                        Text(
                            text = "·",
                            fontFamily = Bricolage,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp,
                            color = Color.White.copy(alpha = 0.3f)
                        )
                        Text(
                            text = jogo.resultadoFora.toString(),
                            fontFamily = Bricolage,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 42.sp,
                            color = LMWhite.copy(alpha = 0.9f)
                        )
                    } else {
                        Text(
                            text = "VS",
                            fontFamily = Bricolage,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            color = Color.White.copy(alpha = 0.4f)
                        )
                    }
                }

                // Team B (Fora)
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                brush = Brush.linearGradient(listOf(Color(0xFF27272A), Color(0xFF0A0A0B))),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = jogo.fora.split("\\s+".toRegex()).take(2).mapNotNull { it.firstOrNull()?.toString() }.joinToString("").uppercase(),
                            color = LMWhite,
                            fontFamily = Bricolage,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    Text(
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

            Spacer(modifier = Modifier.height(18.dp))
            Divider(color = Color.White.copy(alpha = 0.08f))
            Spacer(modifier = Modifier.height(10.dp))

            // Footer Information
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TranslatedText(
                    text = "Torneio ID: ${jogo.torneioId}",
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.55f)
                )

                Text(
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
private fun EstatisticasDiretoSection(
    estatisticas: List<EstatisticaJogo>,
    eventos: List<EventoJogo>,
    modalidade: String = "Futebol"
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LMWhite),
        border = BorderStroke(1.dp, Color(0xFFE2E2E7))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
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

            val base = com.leaguematch.ui.organizer.estatisticasPorModalidade(modalidade).flatMap {
                listOf(
                    EstatisticaJogo(tipo = it.titulo, equipa = "casa", valor = it.casa),
                    EstatisticaJogo(tipo = it.titulo, equipa = "fora", valor = it.fora)
                )
            }.toMutableList()

            estatisticas.forEach { salva ->
                val index = base.indexOfFirst {
                    it.tipo.equals(salva.tipo, ignoreCase = true) &&
                            it.equipa.equals(salva.equipa, ignoreCase = true)
                }

                if (index >= 0) {
                    base[index] = salva
                }
            }

            fun contarEvento(tipo: String, equipa: String): Int {
                return eventos.count {
                    it.tipo.equals(tipo, ignoreCase = true) &&
                            it.equipa.equals(equipa, ignoreCase = true)
                }
            }

            fun atualizarStat(titulo: String, casa: Int, fora: Int) {
                val indexCasa = base.indexOfFirst {
                    it.tipo.equals(titulo, ignoreCase = true) &&
                            it.equipa.equals("casa", ignoreCase = true)
                }

                val indexFora = base.indexOfFirst {
                    it.tipo.equals(titulo, ignoreCase = true) &&
                            it.equipa.equals("fora", ignoreCase = true)
                }

                if (indexCasa >= 0) {
                    base[indexCasa] = base[indexCasa].copy(valor = casa)
                }

                if (indexFora >= 0) {
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

            val estatisticasExibicao = base

            // Group by statistical metric (e.g. "Posse de Bola", "Remates", etc.)
            val groupedStats = estatisticasExibicao.groupBy { it.tipo }
            groupedStats.forEach { (tipo, list) ->
                val casaVal = list.firstOrNull { it.equipa == "casa" }?.valor ?: 0
                val foraVal = list.firstOrNull { it.equipa == "fora" }?.valor ?: 0

                val total = (casaVal + foraVal).coerceAtLeast(1)
                val casaPeso = casaVal.toFloat() / total
                val foraPeso = foraVal.toFloat() / total

                val suffix = if (tipo.contains("Posse", ignoreCase = true)) "%" else ""

                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$casaVal$suffix",
                                fontFamily = GeistMono,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = LMInk
                            )

                            Text(
                                text = tipo,
                                fontFamily = Geist,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = LMGray500
                            )

                            Text(
                                text = "$foraVal$suffix",
                                fontFamily = GeistMono,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = LMInk
                            )
                        }

                        // Progressive comparison bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFFF3F3F5))
                        ) {
                            if (casaPeso > 0f) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .weight(casaPeso)
                                        .background(LMRed)
                                )
                            }
                            if (foraPeso > 0f) {
                                Box(
                                    modifier = Modifier
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
private fun TimelineEventosSection(eventos: List<EventoJogo>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TranslatedText(
            text = "Cronologia",
            fontFamily = Geist,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp,
            color = LMInk
        )

        if (eventos.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = LMWhite),
                border = BorderStroke(1.dp, Color(0xFFE2E2E7))
            ) {
                Box(
                    modifier = Modifier
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
        } else {
            eventos.forEach { evento ->
                val icon = when (evento.tipo.uppercase()) {
                    "GOLO" -> Icons.Default.SportsSoccer
                    "FALTA" -> Icons.Default.Warning
                    "AMARELO" -> Icons.Default.Warning
                    "VERMELHO" -> Icons.Default.Report
                    "CANTO" -> Icons.Default.Flag
                    "SUBSTITUICAO" -> Icons.Default.SwapHoriz
                    "ACE" -> Icons.Default.SportsTennis
                    "BREAK_POINT" -> Icons.Default.Bolt
                    else -> Icons.Default.EmojiEvents
                }

                val iconBg = when (evento.tipo.uppercase()) {
                    "GOLO", "ACE" -> Color(0xFF22C55E).copy(alpha = 0.18f)
                    "FALTA", "AMARELO" -> Color(0xFFF59E0B).copy(alpha = 0.18f)
                    "VERMELHO" -> Color(0xFFEF4444).copy(alpha = 0.18f)
                    else -> Color(0xFF3B82F6).copy(alpha = 0.18f)
                }

                val iconColor = when (evento.tipo.uppercase()) {
                    "GOLO", "ACE" -> Color(0xFF16A34A)
                    "FALTA", "AMARELO" -> Color(0xFFD97706)
                    "VERMELHO" -> Color(0xFFDC2626)
                    else -> Color(0xFF2563EB)
                }

                val eventLabel = when (evento.tipo.uppercase()) {
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
                    else -> evento.tipo
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = LMWhite),
                    border = BorderStroke(1.dp, Color(0xFFE2E2E7))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Minute / Tempo
                        Text(
                            text = "${evento.tempo}'",
                            fontFamily = GeistMono,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = LMGray600,
                            modifier = Modifier.width(36.dp)
                        )

                        // Icon box
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(iconBg, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = iconColor,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Event content
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "$eventLabel de ${evento.userName}",
                                fontFamily = Geist,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = LMInk
                            )
                        }

                        // Side
                        if (evento.equipa != "center") {
                            TranslatedText(
                                text = if (evento.equipa == "casa") "CASA" else "FORA",
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
