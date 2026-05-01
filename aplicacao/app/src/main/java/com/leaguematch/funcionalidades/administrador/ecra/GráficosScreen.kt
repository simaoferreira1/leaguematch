package com.leaguematch.funcionalidades.administrador.ecra

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.ui.theme.LeagueMatchTheme
import com.leaguematch.ui.theme.RedDark
import com.leaguematch.ui.theme.RedPrimary
import com.leaguematch.dados.modelos.EstatisticasAdmin
import com.leaguematch.dados.modelos.ParGrafico

@Composable
fun GraficosScreen(
    estatisticas: EstatisticasAdmin = EstatisticasAdmin(
        totalUtilizadores = 70,
        totalTorneios = 5,
        totalJogos = 30,
        alertas = 0,
        jogosPorPeriodo = listOf(0.2f, 0.45f, 0.28f, 0.55f, 0.48f, 0.78f, 0.50f),
        torneiosPorEstado = listOf(
            ParGrafico("Ativos", 0.55f),
            ParGrafico("Inscrições", 0.8f),
            ParGrafico("Termin.", 0.35f)
        ),
        topTorneios = listOf(
            ParGrafico("Carabao CUP", 0.9f),
            ParGrafico("Barça CUP", 0.75f),
            ParGrafico("MinhoFut", 0.6f),
            ParGrafico("Padel", 0.45f)
        )
    ),
    onHomeClick: () -> Unit = {},
    onUtilizadoresClick: () -> Unit = {},
    onTorneiosClick: () -> Unit = {},
    onDefinicoesClick: () -> Unit = {}
) {
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
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Gráficos",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Resumo visual das estatísticas da aplicação.",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Resumo geral",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MiniResumoCard("Utilizadores", estatisticas.totalUtilizadores.toString(), Icons.Default.Group, Modifier.weight(1f))
                MiniResumoCard("Torneios", estatisticas.totalTorneios.toString(), Icons.Default.EmojiEvents, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MiniResumoCard("Jogos", estatisticas.totalJogos.toString(), Icons.Default.SportsSoccer, Modifier.weight(1f))
                MiniResumoCard("Alertas", estatisticas.alertas.toString(), Icons.Default.Warning, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(22.dp))

            GraficoLinhaCard(points = estatisticas.jogosPorPeriodo)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GraficoBarrasCard(
                    barras = estatisticas.torneiosPorEstado,
                    modifier = Modifier.weight(1f)
                )
                TopTorneiosCard(
                    torneios = estatisticas.topTorneios,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun MiniResumoCard(
    titulo: String,
    valor: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(95.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(listOf(RedPrimary, RedDark)))
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.75f),
                modifier = Modifier.size(26.dp)
            )

            Column {
                Text(
                    text = titulo,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 12.sp,
                    maxLines = 1
                )

                Text(
                    text = valor,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun GraficoLinhaCard(points: List<Float> = listOf(0.2f, 0.45f, 0.28f, 0.55f, 0.48f, 0.78f, 0.50f)) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                text = "Jogos realizados",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                val step = size.width / (points.size - 1)
                val path = Path()

                points.forEachIndexed { index, value ->
                    val x = index * step
                    val y = size.height - (value * size.height)

                    if (index == 0) {
                        path.moveTo(x, y)
                    } else {
                        path.lineTo(x, y)
                    }
                }

                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 2f
                )

                drawPath(
                    path = path,
                    color = RedPrimary,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 5f)
                )

                points.forEachIndexed { index, value ->
                    drawCircle(
                        color = RedPrimary,
                        radius = 7f,
                        center = Offset(index * step, size.height - (value * size.height))
                    )
                }
            }
        }
    }
}

@Composable
fun GraficoBarrasCard(
    barras: List<ParGrafico> = listOf(
        ParGrafico("Ativos", 0.55f),
        ParGrafico("Inscrições", 0.8f),
        ParGrafico("Termin.", 0.35f)
    ),
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(170.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                text = "Torneios por estado",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                barras.forEach { barra ->
                    BarraFake(barra.valorNormalizado, barra.legenda)
                }
            }
        }
    }
}

@Composable
fun BarraFake(valor: Float, texto: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .width(22.dp)
                .height((80 * valor).dp)
                .background(RedPrimary, RoundedCornerShape(6.dp))
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = texto,
            fontSize = 9.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun TopTorneiosCard(
    torneios: List<ParGrafico> = listOf(
        ParGrafico("Carabao CUP", 0.9f),
        ParGrafico("Barça CUP", 0.75f),
        ParGrafico("MinhoFut", 0.6f),
        ParGrafico("Padel", 0.45f)
    ),
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(170.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                text = "Top torneios",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            torneios.forEach { torneio ->
                TopLinha(torneio.legenda, torneio.valorNormalizado)
            }
        }
    }
}

@Composable
fun TopLinha(nome: String, progresso: Float) {
    Column {
        Text(
            text = nome,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = { progresso },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = RedPrimary,
            trackColor = Color.LightGray
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun AdminBottomBar(
    selectedItem: String,
    onHomeClick: () -> Unit = {},
    onUtilizadoresClick: () -> Unit = {},
    onTorneiosClick: () -> Unit = {},
    onGraficosClick: () -> Unit = {},
    onDefinicoesClick: () -> Unit = {}
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        NavigationBarItem(
            selected = selectedItem == "home",
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = {
                Text("Home", fontSize = 9.sp, maxLines = 1)
            },
            colors = navColors()
        )

        NavigationBarItem(
            selected = selectedItem == "utilizadores",
            onClick = onUtilizadoresClick,
            icon = { Icon(Icons.Default.Person, contentDescription = "Utilizadores") },
            label = {
                Text("Utilizadores", fontSize = 9.sp, maxLines = 1)
            },
            colors = navColors()
        )

        NavigationBarItem(
            selected = selectedItem == "torneios",
            onClick = onTorneiosClick,
            icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Torneios") },
            label = {
                Text("Torneios", fontSize = 9.sp, maxLines = 1)
            },
            colors = navColors()
        )

        NavigationBarItem(
            selected = selectedItem == "graficos",
            onClick = onGraficosClick,
            icon = { Icon(Icons.Default.BarChart, contentDescription = "Gráficos") },
            label = {
                Text("Gráficos", fontSize = 9.sp, maxLines = 1)
            },
            colors = navColors()
        )

        NavigationBarItem(
            selected = selectedItem == "definicoes",
            onClick = onDefinicoesClick,
            icon = { Icon(Icons.Default.Settings, contentDescription = "Definições") },
            label = {
                Text("Definições", fontSize = 9.sp, maxLines = 1)
            },
            colors = navColors()
        )
    }
}

@Composable
fun navColors(): NavigationBarItemColors {
    return NavigationBarItemDefaults.colors(
        selectedIconColor = RedPrimary,
        selectedTextColor = RedPrimary,
        unselectedIconColor = Color.Gray,
        unselectedTextColor = Color.Gray,
        indicatorColor = Color.Transparent
    )
}

@Preview(showBackground = true)
@Composable
fun GraficosScreenPreview() {
    LeagueMatchTheme {
        GraficosScreen()
    }
}
