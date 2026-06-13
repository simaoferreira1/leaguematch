package com.leaguematch.ui.organizer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.DetalheTorneio
import com.leaguematch.ui.components.TranslatedText
import com.leaguematch.ui.theme.Bricolage
import com.leaguematch.ui.theme.Geist
import com.leaguematch.ui.theme.LMBorder
import com.leaguematch.ui.theme.LMGray100
import com.leaguematch.ui.theme.LMGray400
import com.leaguematch.ui.theme.LMGray50
import com.leaguematch.ui.theme.LMGray500
import com.leaguematch.ui.theme.LMInk
import com.leaguematch.ui.theme.LMRed
import com.leaguematch.ui.theme.LMRed50
import com.leaguematch.ui.theme.LMWhite

@Composable
fun OrgEstatisticasTorneioScreen(
    detalhe: DetalheTorneio,
    onBackClick: () -> Unit
) {
    val totalJogos = detalhe.jogos.size
    val jogosFinalizados = detalhe.jogos.count { it.estado.equals("Finalizado", ignoreCase = true) }
    val jogosAoVivo = detalhe.jogos.count { it.estado.equals("A Decorrer", ignoreCase = true) }
    val jogosAgendados = detalhe.jogos.count {
        it.estado.equals("Agendado", ignoreCase = true) || it.estado.equals("Por iniciar", ignoreCase = true)
    }
    val totalGolos = detalhe.totalGolos
    val mediaGolos = if (jogosFinalizados > 0) totalGolos.toDouble() / jogosFinalizados else 0.0

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
                .padding(bottom = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = null, tint = LMInk)
                }
                Column(modifier = Modifier.weight(1f)) {
                    TranslatedText(
                        text = "Estatísticas",
                        fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp, color = LMInk
                    )
                    Text(text = detalhe.torneio.nome, fontFamily = Geist, fontSize = 12.sp, color = LMGray500)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCell(
                    label = "Equipas",
                    value = detalhe.torneio.equipas.toString(),
                    icon = Icons.Default.Groups,
                    color = LMRed,
                    bg = LMRed50,
                    modifier = Modifier.weight(1f)
                )
                StatCell(
                    label = "Jogos",
                    value = totalJogos.toString(),
                    icon = Icons.Default.SportsScore,
                    color = Color(0xFF2563EB),
                    bg = Color(0xFFDBEAFE),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCell(
                    label = "Golos",
                    value = totalGolos.toString(),
                    icon = Icons.Default.SportsSoccer,
                    color = Color(0xFF16A34A),
                    bg = Color(0xFFDCFCE7),
                    modifier = Modifier.weight(1f)
                )
                StatCell(
                    label = "Média / jogo",
                    value = "%.1f".format(mediaGolos),
                    icon = Icons.Default.QueryStats,
                    color = Color(0xFF0891B2),
                    bg = Color(0xFFCFFAFE),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            TranslatedText(
                text = "ESTADO DOS JOGOS",
                fontFamily = Geist, fontWeight = FontWeight.Bold,
                fontSize = 11.sp, color = LMGray500, letterSpacing = 0.4.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            EstadoLinha("Finalizados", jogosFinalizados, totalJogos, Color(0xFF16A34A))
            Spacer(modifier = Modifier.height(8.dp))
            EstadoLinha("A decorrer", jogosAoVivo, totalJogos, Color(0xFFEA580C))
            Spacer(modifier = Modifier.height(8.dp))
            EstadoLinha("Agendados", jogosAgendados, totalJogos, LMGray500)

            Spacer(modifier = Modifier.height(20.dp))

            TranslatedText(
                text = "MELHORES MARCADORES",
                fontFamily = Geist, fontWeight = FontWeight.Bold,
                fontSize = 11.sp, color = LMGray500, letterSpacing = 0.4.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (detalhe.goleadores.isEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = LMGray50,
                    border = BorderStroke(1.dp, LMBorder)
                ) {
                    TranslatedText(
                        text = "Sem marcadores registados.",
                        modifier = Modifier.padding(18.dp),
                        fontFamily = Geist, fontSize = 13.sp, color = LMGray500
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    detalhe.goleadores.take(10).forEachIndexed { index, goleador ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = LMWhite,
                            border = BorderStroke(1.dp, LMBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${index + 1}.",
                                    fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp, color = LMRed,
                                    modifier = Modifier.width(28.dp)
                                )
                                Icon(
                                    Icons.Default.EmojiEvents, contentDescription = null,
                                    tint = LMGray400, modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = goleador.nome,
                                    fontFamily = Geist, fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp, color = LMInk,
                                    modifier = Modifier.weight(1f)
                                )
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
private fun StatCell(
    label: String,
    value: String,
    icon: ImageVector,
    color: Color,
    bg: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, LMBorder),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(bg, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                fontFamily = Bricolage, fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp, color = LMInk
            )
            Text(
                text = label,
                fontFamily = Geist, fontSize = 12.sp, color = LMGray500
            )
        }
    }
}

@Composable
private fun EstadoLinha(label: String, valor: Int, total: Int, cor: Color) {
    val fracao = if (total > 0) valor.toFloat() / total else 0f

    Column {
        Row {
            Text(
                text = label,
                fontFamily = Geist, fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp, color = LMInk,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$valor / $total",
                fontFamily = Geist, fontWeight = FontWeight.Bold,
                fontSize = 12.sp, color = LMGray500
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(LMGray100, RoundedCornerShape(50))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fracao)
                    .height(8.dp)
                    .background(cor, RoundedCornerShape(50))
            )
        }
    }
}
