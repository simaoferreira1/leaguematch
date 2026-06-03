package com.leaguematch.ui.participant

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.Classificacao
import com.leaguematch.ui.theme.*

@Composable
fun ParticipantClassificationScreen(
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
                    text = "Classificação",
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = LMInk
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (classificacao.isEmpty()) {
                Text(
                    text = "Ainda não existe classificação para este torneio.",
                    fontFamily = Geist,
                    fontSize = 14.sp,
                    color = LMGray500
                )
            } else {
                classificacao.forEachIndexed { index, item ->
                    ClassificationRow(
                        posicao = index + 1,
                        item = item
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun ClassificationRow(
    posicao: Int,
    item: Classificacao
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
                color = LMRed,
                modifier = Modifier.width(44.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.nomeEquipa,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = LMInk
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "${item.jogos}J • ${item.vitorias}V • ${item.empates}E • ${item.derrotas}D",
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )

                Text(
                    text = "GM ${item.golosMarcados} • GS ${item.golosSofridos}",
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }

            Text(
                text = "${item.pontos} pts",
                fontFamily = Geist,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = LMInk
            )
        }
    }
}