/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: HomeScreen.kt
 * Tipo: Interface (Compose View) do Administrador
 *
 * Descrição:
 * Este ficheiro define um ecrã da área do Administrador em Jetpack Compose.\n * Ele desenha componentes visuais reativos baseado no estado fornecido pelo respetivo ViewModel.\n * Permite ao Admin gerir utilizadores (ativar/desativar), visualizar alertas do sistema e gráficos.
 */
package com.leaguematch.ui.admin // Define o pacote deste ficheiro de código

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
import androidx.compose.foundation.layout.width // Importa dependência / biblioteca necessária
import androidx.compose.foundation.rememberScrollState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.CircleShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.EmojiEvents // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Flag // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Group // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Notifications // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Person // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Refresh // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsVolleyball // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Warning // Importa dependência / biblioteca necessária
import androidx.compose.material3.HorizontalDivider // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.draw.clip // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.vector.ImageVector // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.SpanStyle // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.buildAnnotatedString // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.withStyle // Importa dependência / biblioteca necessária
import androidx.compose.ui.tooling.preview.Preview // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.ResumoDashboard // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.AdminBottomBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.Avatar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.CardWrapper // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TextBtn // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TopBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Bricolage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.GeistMono // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMBorder // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray100 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray400 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray600 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInfo // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMLive // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMLiveBg // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed50 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWarn // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LeagueMatchTheme // Importa dependência / biblioteca necessária

// Ecrã inicial do administrador com resumo geral da plataforma
@Composable
fun HomeScreen( // Declaração de função / método de lógica
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
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 12.dp)
        ) {
            // Barra superior com ícone de notificações e avatar do administrador
            TopBar(
                title = "",
                rightContent = {
                    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box( // Contentor Compose: Sobrepõe os elementos filhos
                            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                .size(34.dp)
                                .background(LMGray100, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon( // Componente Compose: Desenha um ícone vetorial
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notificações",
                                tint = LMInk,
                                modifier = Modifier.size(16.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                            )
                        }
                        Avatar(name = "Admin", size = 34.dp, color = LMInk)
                    }
                }
            )

            // Área de saudação e título do painel administrativo
            Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                TranslatedText(
                    text = "Bom dia,",
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
                TranslatedText(
                    text = "Painel do administrador",
                    fontFamily = Bricolage,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LMInk,
                    letterSpacing = (-0.6).sp,
                    modifier = Modifier.padding(top = 2.dp, bottom = 4.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
                TranslatedText(
                    text = "Resumo do sistema · atualizado às 18:30",
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }

            // Grelha com os principais indicadores do sistema
            Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                    modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
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
                        modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                    KPIBlock(
                        label = "Torneios",
                        value = dashboard.totalTorneios.toString(),
                        diff = "+1",
                        trend = "up",
                        icon = Icons.Default.EmojiEvents,
                        iconColor = LMRed,
                        iconBg = LMRed50,
                        modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                }
                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                    modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
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
                        modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                    KPIBlock(
                        label = "Alertas",
                        value = dashboard.alertasSistema.toString(),
                        diff = "0",
                        trend = "flat",
                        icon = Icons.Default.Warning,
                        iconColor = Color(0xFFCA8A04),
                        iconBg = Color(0xFFFEF9C3),
                        modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                }
            }

            // Cartão com gráfico simples da atividade dos últimos 7 dias
            CardWrapper(
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 10.dp),
                backgroundColor = LMInk,
                borderStroke = null
            ) {
                Column(modifier = Modifier.fillMaxWidth()) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column { // Contentor Compose: Alinha os filhos numa coluna vertical
                            TranslatedText(
                                text = "ATIVIDADE · 7 DIAS",
                                fontFamily = Geist,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                            Text( // Componente Compose: Desenha texto estruturado no ecrã
                                text = "${dashboard.totalEventos7Dias} eventos",
                                fontFamily = Bricolage,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                modifier = Modifier.padding(top = 2.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                            )
                        }
                        Box( // Contentor Compose: Sobrepõe os elementos filhos
                            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                .background(Color(0xFF22C55E).copy(alpha = 0.2f), CircleShape)
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Text( // Componente Compose: Desenha texto estruturado no ecrã
                                text = "+12%",
                                fontFamily = Geist,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF86EFAC)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    // MiniChart com dados reais dos últimos 7 dias
                    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .fillMaxWidth()
                            .height(70.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        val chartData = dashboard.atividadeUltimos7Dias // Declara constante local (leitura única)
                        val maxVal = (chartData.maxOrNull() ?: 0).coerceAtLeast(1) // Declara constante local (leitura única)
                        chartData.forEachIndexed { index, value ->
                            val heightFraction = (value.toFloat() / maxVal).coerceAtLeast(0.04f) // Declara constante local (leitura única)
                            Box( // Contentor Compose: Sobrepõe os elementos filhos
                                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                    .weight(1f)
                                    .fillMaxHeight(heightFraction)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (index == chartData.size - 1) LMRed else Color.White.copy(alpha = 0.2f)) // Estrutura de decisão condicional principal
                            )
                        }
                    }

                    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Legenda inferior do gráfico com os dias da semana
                        val days = listOf("Q", "S", "S", "D", "S", "T", "Q") // Declara constante local (leitura única)
                        days.forEach { day ->
                            Text( // Componente Compose: Desenha texto estruturado no ecrã
                                text = day,
                                fontFamily = Geist,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.4f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                            )
                        }
                    }
                }
            }

            // Secção que apresenta as ações recentes efetuadas na plataforma
            Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TranslatedText(
                        text = "Atividade recente",
                        fontFamily = Geist,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = LMInk
                    )
                    TextBtn(onClick = {}) { // Callback: Define a ação executada ao clicar no componente
                        TranslatedText("Ver tudo ›", color = LMRed, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    }
                }

                CardWrapper(
                    modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                    pad = 0.dp
                ) {
                    Column { // Contentor Compose: Alinha os filhos numa coluna vertical
                        val activities = dashboard.atividadeRecente.map { item -> // Declara constante local (leitura única)
                            val (icon, color) = when (item.categoria) { // Escolha múltipla condicional (semelhante a switch-case)
                                "JOGO" -> Icons.Default.Refresh to LMLive
                                "REGISTO" -> Icons.Default.Person to LMInfo
                                "TORNEIO" -> Icons.Default.EmojiEvents to LMRed
                                else -> Icons.Default.Notifications to LMWarn // Fluxo condicional alternativo caso o 'if' seja falso
                            }
                            ActivityItem(item.who, item.what, item.whenLabel, icon, color)
                        }.ifEmpty {
                            // Fallback se a BD estiver vazia
                            listOf(
                                ActivityItem("Sistema", "sem atividade recente", "-", Icons.Default.Notifications, LMGray400)
                            )
                        }

                        activities.forEachIndexed { index, act ->
                            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box( // Contentor Compose: Sobrepõe os elementos filhos
                                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                        .size(28.dp)
                                        .background(LMGray100, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon( // Componente Compose: Desenha um ícone vetorial
                                        imageVector = act.icon,
                                        contentDescription = null,
                                        tint = act.color,
                                        modifier = Modifier.size(14.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                                Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                                    Text( // Componente Compose: Desenha texto estruturado no ecrã
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
                                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                                        text = "há ${act.whenOccurred}",
                                        fontFamily = Geist,
                                        fontSize = 10.sp,
                                        color = LMGray400,
                                        modifier = Modifier.padding(top = 1.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                                    )
                                }
                            }

                            if (index < activities.size - 1) { // Estrutura de decisão condicional principal
                                HorizontalDivider(
                                    modifier = Modifier.padding(start = 52.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
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
private fun KPIBlock( // Declaração de função / método de lógica
    label: String,
    value: String,
    diff: String,
    trend: String,
    icon: ImageVector,
    iconColor: Color,
    iconBg: Color,
    modifier: Modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
) {
    CardWrapper(
        modifier = modifier,
        pad = 12.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) { // Contentor Compose: Alinha os filhos numa coluna vertical
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Caixa visual com o ícone representativo do indicador
                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .size(32.dp)
                        .background(iconBg, RoundedCornerShape(9.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(16.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                }
                // Badge que mostra se o valor aumentou ou se manteve estável
                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .background(
                            if (trend == "up") Color(0xFFDCFCE7) else LMGray100, // Estrutura de decisão condicional principal
                            CircleShape
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = if (trend == "up") "↑ $diff" else diff, // Estrutura de decisão condicional principal
                        fontFamily = Geist,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (trend == "up") Color(0xFF16A34A) else LMGray500 // Estrutura de decisão condicional principal
                    )
                }
            }

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = value,
                fontFamily = GeistMono,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = LMInk,
                letterSpacing = (-0.5).sp,
                modifier = Modifier.padding(top = 8.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = label,
                fontFamily = Geist,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = LMGray500,
                modifier = Modifier.padding(top = 1.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )
        }
    }
}

// Modelo local utilizado para representar uma atividade recente
private data class ActivityItem( // Declaração de classe para modelar objetos
    val who: String, // Declara constante local (leitura única)
    val what: String, // Declara constante local (leitura única)
    val whenOccurred: String, // Declara constante local (leitura única)
    val icon: ImageVector, // Declara constante local (leitura única)
    val color: Color // Declara constante local (leitura única)
)

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() { // Declaração de função / método de lógica
    LeagueMatchTheme {
        HomeScreen()
    }
}
