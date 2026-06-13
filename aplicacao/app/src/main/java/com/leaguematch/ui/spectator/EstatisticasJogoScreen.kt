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
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.EstatisticaJogo
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.translations.AppStrings
import com.leaguematch.translations.StringsPt
import com.leaguematch.ui.components.TranslatedText
import com.leaguematch.ui.theme.Bricolage
import com.leaguematch.ui.theme.Geist
import com.leaguematch.ui.theme.GeistMono
import com.leaguematch.ui.theme.LMGray500
import com.leaguematch.ui.theme.LMInk
import com.leaguematch.ui.theme.LMRed
import com.leaguematch.ui.theme.LMWhite

@Composable
fun EstatisticasJogoScreen(
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            EstatisticasHeaderCard(jogo = jogo)

            val estatisticasExibicao = if (estatisticas.isEmpty()) {
                com.leaguematch.ui.organizer.estatisticasPorModalidade(modalidade).flatMap {
                    listOf(
                        EstatisticaJogo(tipo = it.titulo, equipa = "casa", valor = it.casa),
                        EstatisticaJogo(tipo = it.titulo, equipa = "fora", valor = it.fora)
                    )
                }
            } else {
                estatisticas
            }

            val groupedStats = estatisticasExibicao.groupBy { it.tipo }
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
private fun EstatisticasTopBar(
    onBackClick: () -> Unit
) {
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
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.Analytics,
            contentDescription = null,
            tint = LMRed,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun EstatisticasHeaderCard(
    jogo: Jogo
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF0A0A0B), Color(0xFF1F1F22))
                ),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(18.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = jogo.estado.uppercase(),
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                EquipaHeader(
                    nome = jogo.casa,
                    modifier = Modifier.weight(1f)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Text(
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
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun EquipaHeader(
    nome: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .background(
                    brush = Brush.linearGradient(
                        listOf(LMRed, Color(0xFF0A0A0B))
                    ),
                    shape = RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
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

        Spacer(modifier = Modifier.height(8.dp))

        Text(
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
private fun EstatisticaComparativaCard(
    titulo: String,
    tipo: String,
    estatisticas: List<EstatisticaJogo>,
    percentagem: Boolean = false
) {
    val estatisticasTipo = estatisticas.filter {
        it.tipo.equals(tipo, ignoreCase = true)
    }

    val casa = estatisticasTipo
        .firstOrNull { it.equipa.equals("casa", ignoreCase = true) }
        ?.valor ?: 0

    val fora = estatisticasTipo
        .firstOrNull { it.equipa.equals("fora", ignoreCase = true) }
        ?.valor ?: 0

    val total = (casa + fora).coerceAtLeast(1)
    val casaPeso = casa.toFloat() / total.toFloat()
    val foraPeso = fora.toFloat() / total.toFloat()

    val sufixo = if (percentagem) "%" else ""

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LMWhite),
        border = BorderStroke(1.dp, Color(0xFFE2E2E7))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(
                            color = LMRed.copy(alpha = 0.12f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    val icon = when {
                        titulo.contains("Posse", ignoreCase = true) ->
                            Icons.Default.PanTool

                        titulo.contains("Remates à baliza", ignoreCase = true) ->
                            Icons.Default.GpsFixed

                        titulo.contains("Remates", ignoreCase = true) ->
                            Icons.Default.SportsSoccer

                        titulo.contains("Cantos", ignoreCase = true) ->
                            Icons.Default.Flag

                        else ->
                            Icons.Default.BarChart
                    }

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = LMRed,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = titulo,
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = LMInk
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$casa$sufixo",
                    fontFamily = GeistMono,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = LMInk
                )

                Text(
                    text = "$fora$sufixo",
                    fontFamily = GeistMono,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = LMInk
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(9.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFEDEDF1))
            ) {
                if (casa > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(casaPeso)
                            .background(LMRed)
                    )
                }

                if (fora > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(foraPeso)
                            .background(Color(0xFF111827))
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Casa",
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    color = LMGray500
                )

                Text(
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
private fun SemEstatisticasCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LMWhite),
        border = BorderStroke(1.dp, Color(0xFFE2E2E7))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 36.dp, horizontal = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Ainda não existem estatísticas registadas para este jogo.",
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500,
                textAlign = TextAlign.Center
            )
        }
    }
}