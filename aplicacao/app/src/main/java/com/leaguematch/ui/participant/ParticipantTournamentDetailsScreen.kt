package com.leaguematch.ui.participant

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.Classificacao
import com.leaguematch.data.remote.model.DetalheTorneio
import com.leaguematch.translations.AppStrings
import com.leaguematch.ui.theme.*

@Composable
fun ParticipantTournamentDetailScreen(
    detalhe: DetalheTorneio?,
    classificacao: List<Classificacao>,
    strings: AppStrings,
    primaryColor: Color,
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
                        contentDescription = null,
                        tint = LMInk
                    )
                }

                Text(
                    text = strings.tournamentDetailsTitle,
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp,
                    color = LMInk
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (detalhe == null) {
                EmptyText(strings.tournamentDetailsLoadError)
                return@Column
            }

            TournamentInfoCard(
                title = detalhe.torneio.nome,
                subtitle = "${detalhe.torneio.modalidade} • ${detalhe.torneio.formato}",
                extra = detalhe.torneio.estado
            )

            Spacer(modifier = Modifier.height(18.dp))

            SectionTitle(strings.standingsTitle)

            Spacer(modifier = Modifier.height(10.dp))

            if (classificacao.isEmpty()) {
                EmptyText(strings.noStandingsYet)
            } else {
                classificacao.forEachIndexed { index, item ->
                    ClassificationCard(
                        posicao = index + 1,
                        classificacao = item,
                        strings = strings,
                        primaryColor = primaryColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            SectionTitle(strings.matchesTitle)

            Spacer(modifier = Modifier.height(10.dp))

            if (detalhe.jogos.isEmpty()) {
                EmptyText(strings.noMatchesYet)
            } else {
                detalhe.jogos.forEach { jogo ->
                    MatchCard(
                        title = "${jogo.casa} vs ${jogo.fora}",
                        subtitle = "${jogo.estado} • ${jogo.data} ${jogo.hora}",
                        result = "${jogo.resultadoCasa}-${jogo.resultadoFora}",
                        primaryColor = primaryColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            SectionTitle(strings.topScorersTitle)

            Spacer(modifier = Modifier.height(10.dp))

            if (detalhe.goleadores.isEmpty()) {
                EmptyText(strings.noScorersYet)
            } else {
                detalhe.goleadores.forEach { goleador ->
                    SimpleInfoCard(
                        title = goleador.nome,
                        subtitle = strings.goalsLabel(goleador.golos),
                        primaryColor = primaryColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontFamily = Bricolage,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 21.sp,
        color = LMInk
    )
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
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
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
                color = Color(0xFFEFFBF3)
            ) {
                Text(
                    text = extra,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color(0xFF15803D)
                )
            }
        }
    }
}

@Composable
private fun ClassificationCard(
    posicao: Int,
    classificacao: Classificacao,
    strings: AppStrings,
    primaryColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
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
                color = primaryColor
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
                    text = strings.classificationRecord(
                        classificacao.vitorias,
                        classificacao.empates,
                        classificacao.derrotas
                    ),
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }

            Text(
                text = strings.pointsLabel(classificacao.pontos),
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
    result: String,
    primaryColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.SportsSoccer,
                contentDescription = null,
                tint = primaryColor,
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
    primaryColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.SportsSoccer,
                contentDescription = null,
                tint = primaryColor,
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