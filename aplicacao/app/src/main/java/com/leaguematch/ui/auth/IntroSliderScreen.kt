package com.leaguematch.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.ui.theme.Bricolage
import com.leaguematch.ui.theme.Geist
import com.leaguematch.ui.theme.LMGray500
import com.leaguematch.ui.theme.LMInk
import com.leaguematch.ui.theme.LMRed
import com.leaguematch.ui.theme.LMWhite
import kotlinx.coroutines.launch

private data class IntroSlide(
    val icon: ImageVector,
    val titulo: String,
    val descricao: String
)

private val slides = listOf(
    IntroSlide(
        icon = Icons.Default.EmojiEvents,
        titulo = "Domina os teus torneios",
        descricao = "Cria, organiza e acompanha torneios com ferramentas profissionais."
    ),
    IntroSlide(
        icon = Icons.Default.SportsScore,
        titulo = "Acompanha em direto",
        descricao = "Resultados ao minuto, eventos de jogo e estatísticas em tempo real."
    ),
    IntroSlide(
        icon = Icons.Default.Groups,
        titulo = "A tua equipa, sempre contigo",
        descricao = "Junta-te a equipas por código e segue o desempenho dos teus colegas."
    )
)

@Composable
fun IntroSliderScreen(
    onConcluir: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { slides.size })
    val scope = rememberCoroutineScope()

    Scaffold(containerColor = LMWhite) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp, bottom = 6.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onConcluir) {
                    Text(
                        text = "Saltar",
                        fontFamily = Geist,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = LMGray500
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                SlideContent(slide = slides[page])
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                slides.indices.forEach { index ->
                    val selecionado = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .height(5.dp)
                            .width(if (selecionado) 22.dp else 5.dp)
                            .clip(CircleShape)
                            .background(if (selecionado) LMRed else Color(0xFFD4D4DD))
                    )
                }
            }

            val ultimaPagina = pagerState.currentPage == slides.lastIndex

            Button(
                onClick = {
                    if (ultimaPagina) {
                        onConcluir()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LMRed)
            ) {
                Text(
                    text = if (ultimaPagina) "Começar" else "Continuar",
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = LMWhite
                )
            }

            Spacer(modifier = Modifier.height(22.dp))
        }
    }
}

@Composable
private fun SlideContent(slide: IntroSlide) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF0A0A0B), Color(0xFF1F1F22))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(LMRed.copy(alpha = 0.55f), Color.Transparent)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = slide.icon,
                        contentDescription = null,
                        tint = LMWhite,
                        modifier = Modifier.size(84.dp)
                    )
                }
            }
        }

        Text(
            text = slide.titulo,
            fontFamily = Bricolage,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp,
            color = LMInk,
            textAlign = TextAlign.Center,
            lineHeight = 34.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = slide.descricao,
            fontFamily = Geist,
            fontSize = 14.sp,
            color = LMGray500,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 18.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}
