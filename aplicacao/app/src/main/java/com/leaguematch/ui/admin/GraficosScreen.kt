package com.leaguematch.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.ui.components.*
import com.leaguematch.ui.theme.*
import com.leaguematch.data.remote.model.EstatisticasAdmin
import com.leaguematch.data.remote.model.ParGrafico

@Composable
fun GraficosScreen(
    estatisticas: EstatisticasAdmin = EstatisticasAdmin(
        totalUtilizadores = 70,
        totalTorneios = 5,
        totalJogos = 30,
        alertas = 0,
        jogosPorPeriodo = listOf(0.12f, 0.18f, 0.14f, 0.22f, 0.26f, 0.30f, 0.28f, 0.38f, 0.42f, 0.36f, 0.48f, 0.56f, 0.52f, 0.62f),
        torneiosPorEstado = listOf(
            ParGrafico("Futebol", 38f),
            ParGrafico("Ténis", 18f),
            ParGrafico("Basquetebol", 9f),
            ParGrafico("Andebol", 4f),
            ParGrafico("Rugby", 1f)
        ),
        topTorneios = listOf(
            ParGrafico("Participantes", 68f),
            ParGrafico("Espectadores", 20f),
            ParGrafico("Organizadores", 12f)
        )
    ),
    onHomeClick: () -> Unit = {},
    onUtilizadoresClick: () -> Unit = {},
    onTorneiosClick: () -> Unit = {},
    onDefinicoesClick: () -> Unit = {}
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
            // Header TopBar
            TopBar(
                title = "Gráficos",
                big = true,
                sub = "Análise geral da plataforma",
                rightContent = {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(LMGray100, RoundedCornerShape(10.dp))
                            .clickable {},
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = LMGray600,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            )

            // Period Selector Tabs Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val periods = listOf("7d", "30d", "90d", "Ano")
                
                periods.forEach { period ->
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

            // Big Active Users Chart Card
            CardWrapper(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "UTILIZADORES ATIVOS",
                                fontFamily = Geist,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = LMGray500,
                                letterSpacing = 0.4.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "1 248",
                                fontFamily = Bricolage,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = LMInk,
                                letterSpacing = (-0.5).sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "↑ 18% vs período anterior",
                                fontFamily = Geist,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = LMLive
                            )
                        }
                        
                        Pill(
                            text = "30 dias",
                            kind = "red"
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(18.dp))

                    // Area Chart Drawing using Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                    ) {
                        val strokeColor = LMRed
                        
                        Canvas(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            val rawData = estatisticas.jogosPorPeriodo
                            val data = if (rawData.isEmpty()) listOf(0.1f) else rawData
                            val w = size.width
                            val h = size.height
                            val maxVal = data.maxOrNull()?.coerceAtLeast(0.01f) ?: 1f
                            val step = if (data.size > 1) w / (data.size - 1) else w
                            
                            val path = Path()
                            val fillPath = Path()
                            
                            data.forEachIndexed { i, v ->
                                val x = if (data.size > 1) i * step else w / 2f
                                val y = h * 0.1f + (h * 0.8f) * (1f - (v / maxVal))
                                
                                if (i == 0) {
                                    path.moveTo(x, y)
                                    fillPath.moveTo(x, y)
                                } else {
                                    path.lineTo(x, y)
                                    fillPath.lineTo(x, y)
                                }
                            }
                            
                            if (data.size > 1) {
                                // Close fill path
                                fillPath.lineTo(w, h)
                                fillPath.lineTo(0f, h)
                                fillPath.close()
                                
                                // Draw filled area with linear gradient
                                drawPath(
                                    path = fillPath,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(strokeColor.copy(alpha = 0.25f), Color.Transparent),
                                        startY = 0f,
                                        endY = h
                                    )
                                )
                                
                                // Draw line path
                                drawPath(
                                    path = path,
                                    color = strokeColor,
                                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                                )
                            }
                            
                            // Draw endpoint circles
                            val lastIdx = data.size - 1
                            val endX = if (data.size > 1) lastIdx * step else w / 2f
                            val endY = h * 0.1f + (h * 0.8f) * (1f - (data[lastIdx] / maxVal))
                            
                            drawCircle(
                                color = strokeColor.copy(alpha = 0.2f),
                                radius = 6.dp.toPx(),
                                center = Offset(endX, endY)
                            )
                            drawCircle(
                                color = strokeColor,
                                radius = 3.5f.dp.toPx(),
                                center = Offset(endX, endY)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sport Breakdown Progress Bars Card
            CardWrapper(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
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
                        Color(0xFFCA8A04),
                        Color(0xFF7C3AED),
                        Color(0xFFDB2777)
                    )
                    
                    val breakdown = estatisticas.torneiosPorEstado.mapIndexed { index, item ->
                        val color = colors.getOrElse(index) { LMGray500 }
                        Triple(item.legenda, item.valorNormalizado.toInt(), color)
                    }.ifEmpty {
                        listOf(
                            Triple("Sem torneios", 0, LMGray300)
                        )
                    }
                    
                    val maxVal = breakdown.maxOfOrNull { it.second.toFloat() }?.coerceAtLeast(1f) ?: 1f
                    
                    breakdown.forEachIndexed { index, (sport, value, color) ->
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
                                    text = sport,
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
                            
                            // Custom progress bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .background(LMGray100, RoundedCornerShape(99.dp))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(fraction = (value.toFloat() / maxVal).coerceIn(0f, 1f))
                                        .fillMaxHeight()
                                        .background(color, RoundedCornerShape(99.dp))
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Profiles Donut and Info Card
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
                    }.ifEmpty {
                        listOf(
                            Triple("Sem utilizadores", "0%", LMGray300)
                        )
                    }

                    // Donut Chart Graphic using Canvas drawing arcs
                    Box(
                        modifier = Modifier.size(76.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeWidth = 10.dp.toPx()
                            val sizeOffset = strokeWidth
                            val innerSize = Size(size.width - sizeOffset, size.height - sizeOffset)
                            val centerOffset = Offset(sizeOffset / 2, sizeOffset / 2)
                            
                            // Draw base grey background circle
                            drawCircle(
                                color = LMGray100,
                                radius = (size.width - strokeWidth) / 2,
                                style = Stroke(width = strokeWidth)
                            )
                            
                            val slice1 = estatisticas.topTorneios.getOrNull(0)?.valorNormalizado ?: 0f
                            val slice2 = estatisticas.topTorneios.getOrNull(1)?.valorNormalizado ?: 0f
                            val slice3 = estatisticas.topTorneios.getOrNull(2)?.valorNormalizado ?: 0f
                            
                            val totalPct = (slice1 + slice2 + slice3).coerceAtLeast(1f)
                            
                            val sweep1 = (slice1 / totalPct) * 360f
                            val sweep2 = (slice2 / totalPct) * 360f
                            val sweep3 = (slice3 / totalPct) * 360f
                            
                            if (slice1 > 0f) {
                                drawArc(
                                    color = LMRed,
                                    startAngle = -90f,
                                    sweepAngle = sweep1,
                                    useCenter = false,
                                    topLeft = centerOffset,
                                    size = innerSize,
                                    style = Stroke(width = strokeWidth)
                                )
                            }
                            
                            if (slice2 > 0f) {
                                drawArc(
                                    color = LMInk,
                                    startAngle = -90f + sweep1,
                                    sweepAngle = sweep2,
                                    useCenter = false,
                                    topLeft = centerOffset,
                                    size = innerSize,
                                    style = Stroke(width = strokeWidth)
                                )
                            }
                            
                            if (slice3 > 0f) {
                                drawArc(
                                    color = LMGray300,
                                    startAngle = -90f + sweep1 + sweep2,
                                    sweepAngle = sweep3,
                                    useCenter = false,
                                    topLeft = centerOffset,
                                    size = innerSize,
                                    style = Stroke(width = strokeWidth)
                                )
                            }
                        }
                        
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            val totalUsers = estatisticas.totalUtilizadores
                            val userText = if (totalUsers >= 1000) {
                                "%.1fk".format(totalUsers / 1000f)
                            } else {
                                totalUsers.toString()
                            }
                            Text(
                                text = userText,
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
                    
                    // Donut breakdown list
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Perfis",
                            fontFamily = Geist,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = LMInk,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
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
    }
}

@Preview(showBackground = true)
@Composable
fun GraficosScreenPreview() {
    LeagueMatchTheme {
        GraficosScreen()
    }
}
