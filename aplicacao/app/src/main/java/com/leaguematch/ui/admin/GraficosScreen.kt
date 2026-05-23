package com.leaguematch.ui.admin

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.EstatisticasAdmin
import com.leaguematch.ui.components.AdminBottomBar
import com.leaguematch.ui.components.CardWrapper
import com.leaguematch.ui.components.Pill
import com.leaguematch.ui.components.TopBar
import com.leaguematch.ui.theme.*

@Composable
fun GraficosScreen(
    estatisticas: EstatisticasAdmin,
    onHomeClick: () -> Unit,
    onUtilizadoresClick: () -> Unit,
    onTorneiosClick: () -> Unit,
    onDefinicoesClick: () -> Unit
) {
    var selectedPeriod by remember { mutableStateOf("30d") }

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 12.dp)
        ) {
            TopBar(
                title = "Gráficos",
                big = true,
                sub = "Análise geral da plataforma",
                rightContent = {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(LMGray100, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = LMGray600,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("7d", "30d", "90d", "Ano").forEach { period ->
                    val isSelected = selectedPeriod == period

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = if (isSelected) LMInk else LMGray100,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedPeriod = period }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = period,
                            fontFamily = Geist,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) LMWhite else LMGray700
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            CardWrapper(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "TOTAL DE UTILIZADORES",
                                fontFamily = Geist,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = LMGray500,
                                letterSpacing = 0.4.sp
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = estatisticas.totalUtilizadores.toString(),
                                fontFamily = Bricolage,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = LMInk,
                                letterSpacing = (-0.5).sp
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "${estatisticas.totalTorneios} torneios • ${estatisticas.totalJogos} jogos",
                                fontFamily = Geist,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = LMLive
                            )
                        }

                        Pill(
                            text = selectedPeriod,
                            kind = "red"
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val data = estatisticas.jogosPorPeriodo.ifEmpty { listOf(0.05f) }
                            val w = size.width
                            val h = size.height
                            val maxVal = data.maxOrNull()?.coerceAtLeast(0.01f) ?: 1f
                            val step = if (data.size > 1) w / (data.size - 1) else w
                            val strokeColor = LMRed

                            val path = Path()
                            val fillPath = Path()

                            data.forEachIndexed { index, value ->
                                val x = if (data.size > 1) index * step else w / 2f
                                val y = h * 0.1f + (h * 0.8f) * (1f - (value / maxVal))

                                if (index == 0) {
                                    path.moveTo(x, y)
                                    fillPath.moveTo(x, y)
                                } else {
                                    path.lineTo(x, y)
                                    fillPath.lineTo(x, y)
                                }
                            }

                            if (data.size > 1) {
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

                            val lastIndex = data.lastIndex
                            val endX = if (data.size > 1) lastIndex * step else w / 2f
                            val endY = h * 0.1f + (h * 0.8f) * (1f - (data[lastIndex] / maxVal))

                            drawCircle(
                                color = strokeColor.copy(alpha = 0.2f),
                                radius = 6.dp.toPx(),
                                center = Offset(endX, endY)
                            )

                            drawCircle(
                                color = strokeColor,
                                radius = 3.5.dp.toPx(),
                                center = Offset(endX, endY)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            CardWrapper(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Torneios por modalidade",
                        fontFamily = Geist,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = LMInk,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    val colors = listOf(
                        LMRed,
                        LMInk,
                        Color(0xFF2563EB),
                        Color(0xFF16A34A),
                        Color(0xFFCA8A04)
                    )

                    val breakdown = estatisticas.torneiosPorEstado.mapIndexed { index, item ->
                        Triple(
                            item.legenda,
                            item.valorNormalizado.toInt(),
                            colors.getOrElse(index) { LMGray500 }
                        )
                    }

                    if (breakdown.isEmpty()) {
                        Text(
                            text = "Sem torneios registados.",
                            fontFamily = Geist,
                            fontSize = 13.sp,
                            color = LMGray500
                        )
                    } else {
                        val maxValue = breakdown.maxOf { it.second }.coerceAtLeast(1)

                        breakdown.forEachIndexed { index, (modalidade, value, color) ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = if (index < breakdown.size - 1) 8.dp else 0.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = modalidade,
                                        fontFamily = Geist,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = LMGray700
                                    )

                                    Text(
                                        text = value.toString(),
                                        fontFamily = GeistMono,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LMInk
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .background(LMGray100, RoundedCornerShape(99.dp))
                                ) {
                                    Box(
                                        modifier = Modifier
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

            Spacer(modifier = Modifier.height(8.dp))

            CardWrapper(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    val profiles = estatisticas.topTorneios.mapIndexed { index, item ->
                        val color = when (item.legenda.lowercase()) {
                            "participantes" -> LMRed
                            "espectadores" -> LMInk
                            "organizadores" -> LMGray300
                            else -> when (index) {
                                0 -> LMRed
                                1 -> LMInk
                                else -> LMGray300
                            }
                        }

                        Triple(item.legenda, "${item.valorNormalizado.toInt()}%", color)
                    }

                    Box(
                        modifier = Modifier.size(76.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeWidth = 10.dp.toPx()
                            val sizeOffset = strokeWidth
                            val innerSize = Size(size.width - sizeOffset, size.height - sizeOffset)
                            val centerOffset = Offset(sizeOffset / 2, sizeOffset / 2)

                            drawCircle(
                                color = LMGray100,
                                radius = (size.width - strokeWidth) / 2,
                                style = Stroke(width = strokeWidth)
                            )

                            val values = estatisticas.topTorneios.map { it.valorNormalizado }
                            val total = values.sum().coerceAtLeast(1f)

                            var startAngle = -90f

                            values.forEachIndexed { index, value ->
                                if (value > 0f) {
                                    val color = when (index) {
                                        0 -> LMRed
                                        1 -> LMInk
                                        else -> LMGray300
                                    }

                                    val sweep = (value / total) * 360f

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

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = estatisticas.totalUtilizadores.toString(),
                                fontFamily = Bricolage,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = LMInk,
                                lineHeight = 14.sp
                            )

                            Text(
                                text = "USERS",
                                fontFamily = Geist,
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Bold,
                                color = LMGray500,
                                lineHeight = 7.sp
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Perfis",
                            fontFamily = Geist,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = LMInk,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        if (profiles.isEmpty()) {
                            Text(
                                text = "Sem utilizadores registados.",
                                fontFamily = Geist,
                                fontSize = 13.sp,
                                color = LMGray500
                            )
                        } else {
                            profiles.forEach { (label, value, color) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(color)
                                    )

                                    Text(
                                        text = label,
                                        fontFamily = Geist,
                                        fontSize = 11.sp,
                                        color = LMGray600,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Text(
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

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}