package com.leaguematch.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.ui.components.*
import com.leaguematch.ui.theme.*
import com.leaguematch.data.remote.model.ResumoDashboard

// Ecrã inicial do administrador com resumo geral da plataforma
@Composable
fun HomeScreen(
    dashboard: ResumoDashboard = ResumoDashboard(150, 12, 3, 0),
    onUtilizadoresClick: () -> Unit = {},
    onTorneiosClick: () -> Unit = {},
    onGraficosClick: () -> Unit = {},
    onDefinicoesClick: () -> Unit = {}
) {
    // Estrutura principal do ecrã com barra de navegação inferior
    Scaffold(
        bottomBar = {
            AdminBottomBar(
                selectedItem = "home",
                onHomeClick = {},
                onUtilizadoresClick = onUtilizadoresClick,
                onTorneiosClick = onTorneiosClick,
                onGraficosClick = onGraficosClick,
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
            // Barra superior com ícone de notificações e avatar do administrador
            TopBar(
                title = "",
                rightContent = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(LMGray100, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notificações",
                                tint = LMInk,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Avatar(name = "Admin", size = 34.dp, color = LMInk)
                    }
                }
            )

            // Área de saudação e título do painel administrativo
            Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)) {
                Text(
                    text = "Bom dia,",
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
                Text(
                    text = "Painel do administrador",
                    fontFamily = Bricolage,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LMInk,
                    letterSpacing = (-0.6).sp,
                    modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
                )
                Text(
                    text = "Resumo do sistema · atualizado às 18:30",
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }

            // Grelha com os principais indicadores do sistema
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Cartão que apresenta um indicador estatístico resumido
                    KPIBlock(
                        label = "Utilizadores",
                        value = dashboard.totalUtilizadores.toString(),
                        diff = "+8",
                        trend = "up",
                        icon = Icons.Default.Group,
                        iconColor = Color(0xFF2563EB),
                        iconBg = Color(0xFFDBEAFE),
                        modifier = Modifier.weight(1f)
                    )
                    KPIBlock(
                        label = "Torneios",
                        value = dashboard.totalTorneios.toString(),
                        diff = "+1",
                        trend = "up",
                        icon = Icons.Default.EmojiEvents,
                        iconColor = LMRed,
                        iconBg = LMRed50,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KPIBlock(
                        label = "Jogos disputados",
                        value = (dashboard.torneiosEmCurso * 10).toString(), // derived or mapped value
                        diff = "+3",
                        trend = "up",
                        icon = Icons.Default.SportsVolleyball,
                        iconColor = LMLive,
                        iconBg = LMLiveBg,
                        modifier = Modifier.weight(1f)
                    )
                    KPIBlock(
                        label = "Alertas",
                        value = dashboard.alertasSistema.toString(),
                        diff = "0",
                        trend = "flat",
                        icon = Icons.Default.Warning,
                        iconColor = Color(0xFFCA8A04),
                        iconBg = Color(0xFFFEF9C3),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Cartão com gráfico simples da atividade dos últimos 7 dias
            CardWrapper(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 10.dp),
                backgroundColor = LMInk,
                borderStroke = null
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "ATIVIDADE · 7 DIAS",
                                fontFamily = Geist,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                            Text(
                                text = "15 eventos",
                                fontFamily = Bricolage,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF22C55E).copy(alpha = 0.2f), CircleShape)
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "+12%",
                                fontFamily = Geist,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF86EFAC)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // MiniChart
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // Dados usados para desenhar as barras do gráfico
                        val chartData = listOf(22, 38, 30, 52, 44, 70, 62)
                        val maxVal = 80
                        // Cria uma barra proporcional ao valor de cada dia
                        chartData.forEachIndexed { index, value ->
                            val heightFraction = value.toFloat() / maxVal
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(heightFraction)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (index == chartData.size - 1) LMRed else Color.White.copy(alpha = 0.2f))
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Legenda inferior do gráfico com os dias da semana
                        val days = listOf("Q", "S", "S", "D", "S", "T", "Q")
                        days.forEach { day ->
                            Text(
                                text = day,
                                fontFamily = Geist,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.4f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Secção que apresenta as ações recentes efetuadas na plataforma
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Atividade recente",
                        fontFamily = Geist,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = LMInk
                    )
                    TextBtn(onClick = {}) {
                        Text("Ver tudo ›", color = LMRed, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    }
                }

                CardWrapper(
                    modifier = Modifier.fillMaxWidth(),
                    pad = 0.dp
                ) {
                    Column {
                        // Lista temporária de atividades recentes apresentadas no painel
                        val activities = listOf(
                            ActivityItem("Simão Ferreira", "criou o torneio MinhoFut Cup", "agora", Icons.Default.EmojiEvents, LMRed),
                            ActivityItem("Diogo Gomes", "registou-se na aplicação", "5 min", Icons.Default.Person, LMInfo),
                            ActivityItem("João Fernandes", "atualizou resultado de Prata 2-1", "12 min", Icons.Default.Refresh, LMLive),
                            ActivityItem("Rúben Ferreira", "denunciou conteúdo inadequado", "1 h", Icons.Default.Flag, LMWarn)
                        )

                        // Apresenta cada atividade com ícone, descrição e tempo decorrido
                        activities.forEachIndexed { index, act ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(LMGray100, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = act.icon,
                                        contentDescription = null,
                                        tint = act.color,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = LMInk)) {
                                                append(act.who)
                                            }
                                            append(" ")
                                            withStyle(SpanStyle(color = LMGray600)) {
                                                append(act.what)
                                            }
                                        },
                                        fontFamily = Geist,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "há ${act.whenOccurred}",
                                        fontFamily = Geist,
                                        fontSize = 10.sp,
                                        color = LMGray400,
                                        modifier = Modifier.padding(top = 1.dp)
                                    )
                                }
                            }

                            if (index < activities.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(start = 52.dp),
                                    color = LMBorder
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Componente reutilizável para mostrar indicadores do dashboard
@Composable
private fun KPIBlock(
    label: String,
    value: String,
    diff: String,
    trend: String,
    icon: ImageVector,
    iconColor: Color,
    iconBg: Color,
    modifier: Modifier = Modifier
) {
    CardWrapper(
        modifier = modifier,
        pad = 12.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Caixa visual com o ícone representativo do indicador
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(iconBg, RoundedCornerShape(9.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
                // Badge que mostra se o valor aumentou ou se manteve estável
                Box(
                    modifier = Modifier
                        .background(
                            if (trend == "up") Color(0xFFDCFCE7) else LMGray100,
                            CircleShape
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (trend == "up") "↑ $diff" else diff,
                        fontFamily = Geist,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (trend == "up") Color(0xFF16A34A) else LMGray500
                    )
                }
            }

            Text(
                text = value,
                fontFamily = GeistMono,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = LMInk,
                letterSpacing = (-0.5).sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = label,
                fontFamily = Geist,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = LMGray500,
                modifier = Modifier.padding(top = 1.dp)
            )
        }
    }
}

// Modelo local utilizado para representar uma atividade recente
private data class ActivityItem(
    val who: String,
    val what: String,
    val whenOccurred: String,
    val icon: ImageVector,
    val color: Color
)

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    LeagueMatchTheme {
        HomeScreen()
    }
}
