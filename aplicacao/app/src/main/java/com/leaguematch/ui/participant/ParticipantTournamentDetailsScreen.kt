package com.leaguematch.ui.participant

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.Classificacao
import com.leaguematch.data.remote.model.DetalheTorneio
import com.leaguematch.ui.theme.*

@Composable
fun ParticipantTournamentDetailScreen(
    detalhe: DetalheTorneio?,
    classificacao: List<Classificacao>,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = LMInk
                    )
                }

                Text(
                    text = "Detalhes do torneio",
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp,
                    color = LMInk
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (detalhe == null) {
                Text(
                    text = "Não foi possível carregar os detalhes deste torneio.",
                    fontFamily = Geist,
                    fontSize = 14.sp,
                    color = LMGray500
                )
                return@Column
            }

            TournamentInfoCard(
                title = detalhe.torneio.nome,
                subtitle = "${detalhe.torneio.modalidade} • ${detalhe.torneio.formato}",
                extra = detalhe.torneio.estado
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Classificação",
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 21.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (classificacao.isEmpty()) {
                EmptyText("Ainda não existe classificação.")
            } else {
                classificacao.forEachIndexed { index, item ->
                    ClassificationCard(
                        posicao = index + 1,
                        classificacao = item
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Jogos",
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 21.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (detalhe.jogos.isEmpty()) {
                EmptyText("Ainda não existem jogos neste torneio.")
            } else {
                detalhe.jogos.forEach { jogo ->
                    MatchCard(
                        title = "${jogo.casa} vs ${jogo.fora}",
                        subtitle = "${jogo.estado} • ${jogo.data} ${jogo.hora}",
                        result = "${jogo.resultadoCasa}-${jogo.resultadoFora}"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Melhores marcadores",
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 21.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (detalhe.goleadores.isEmpty()) {
                EmptyText("Ainda não existem marcadores registados.")
            } else {
                detalhe.goleadores.forEach { goleador ->
                    SimpleInfoCard(
                        title = goleador.nome,
                        subtitle = "${goleador.golos} golos",
                        icon = Icons.Default.SportsSoccer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun TournamentInfoCard(
    title: String,
    subtitle: String,
    extra: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, androidx.compose.ui.graphics.Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = androidx.compose.ui.graphics.Color(0xFFEFFBF3)
            ) {
                Text(
                    text = extra,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = androidx.compose.ui.graphics.Color(0xFF15803D)
                )
            }
        }
    }
}

@Composable
private fun ClassificationCard(
    posicao: Int,
    classificacao: Classificacao
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, androidx.compose.ui.graphics.Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$posicao.º",
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = LMRed
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = classificacao.nomeEquipa,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = LMInk
                )

                Text(
                    text = "${classificacao.vitorias}V ${classificacao.empates}E ${classificacao.derrotas}D",
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }

            Text(
                text = "${classificacao.pontos} pts",
                fontFamily = Geist,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = LMInk
            )
        }
    }
}

@Composable
private fun MatchCard(
    title: String,
    subtitle: String,
    result: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, androidx.compose.ui.graphics.Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.SportsSoccer,
                contentDescription = null,
                tint = LMRed,
                modifier = Modifier.size(30.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = LMInk
                )

                Text(
                    text = subtitle,
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }

            Text(
                text = result,
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = LMInk
            )
        }
    }
}

@Composable
private fun SimpleInfoCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, androidx.compose.ui.graphics.Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = LMRed,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = LMInk
                )

                Text(
                    text = subtitle,
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }
        }
    }
}

@Composable
private fun EmptyText(text: String) {
    Text(
        text = text,
        fontFamily = Geist,
        fontSize = 14.sp,
        color = LMGray500
    )
}