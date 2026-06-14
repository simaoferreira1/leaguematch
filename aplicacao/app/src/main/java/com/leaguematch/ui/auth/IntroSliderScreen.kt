/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: IntroSliderScreen.kt
 * Tipo: Lógica Utilitária / Auxiliar
 *
 * Descrição:
 * Contém funções utilitárias ou auxiliares transversais à aplicação.
 */
package com.leaguematch.ui.auth // Define o pacote deste ficheiro de código

import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.* // Importa dependência / biblioteca necessária
import androidx.compose.foundation.pager.HorizontalPager // Importa dependência / biblioteca necessária
import androidx.compose.foundation.pager.rememberPagerState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.CircleShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.EmojiEvents // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Groups // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsScore // Importa dependência / biblioteca necessária
import androidx.compose.material3.Button // Importa dependência / biblioteca necessária
import androidx.compose.material3.ButtonDefaults // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.material3.TextButton // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.LaunchedEffect // Importa dependência / biblioteca necessária
import androidx.compose.runtime.rememberCoroutineScope // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.draw.clip // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Brush // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.vector.ImageVector // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.style.TextAlign // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Bricolage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária
import kotlinx.coroutines.launch // Importa dependência / biblioteca necessária

private data class IntroSlide( // Declaração de classe para modelar objetos
    val icon: ImageVector, // Declara constante local (leitura única)
    val titulo: String, // Declara constante local (leitura única)
    val descricao: String // Declara constante local (leitura única)
)

private val slides = listOf( // Declara constante local (leitura única)
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
fun IntroSliderScreen( // Declaração de função / método de lógica
    onConcluir: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { slides.size }) // Declara constante local (leitura única)
    val scope = rememberCoroutineScope() // Cria escopo local para lançar coroutines em cliques na UI

    Scaffold(containerColor = LMWhite) { innerPadding ->
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(top = 14.dp, bottom = 6.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onConcluir) { // Callback: Define a ação executada ao clicar no componente
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
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
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                SlideContent(slide = slides[page])
            }

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(vertical = 18.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                slides.indices.forEach { index ->
                    val selecionado = pagerState.currentPage == index // Declara constante local (leitura única)
                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .padding(horizontal = 3.dp)
                            .height(5.dp)
                            .width(if (selecionado) 22.dp else 5.dp) // Estrutura de decisão condicional principal
                            .clip(CircleShape)
                            .background(if (selecionado) LMRed else Color(0xFFD4D4DD)) // Estrutura de decisão condicional principal
                    )
                }
            }

            val ultimaPagina = pagerState.currentPage == slides.lastIndex // Declara constante local (leitura única)

            Button( // Componente Compose: Desenha um botão interativo
                onClick = { // Callback: Define a ação executada ao clicar no componente
                    if (ultimaPagina) { // Estrutura de decisão condicional principal
                        onConcluir()
                    } else { // Fluxo condicional alternativo caso o 'if' seja falso
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LMRed)
            ) {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = if (ultimaPagina) "Começar" else "Continuar", // Estrutura de decisão condicional principal
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = LMWhite
                )
            }

            Spacer(modifier = Modifier.height(22.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
        }
    }
}

@Composable
private fun SlideContent(slide: IntroSlide) { // Declaração de função / método de lógica
    Column( // Contentor Compose: Alinha os filhos numa coluna vertical
        modifier = Modifier.fillMaxSize(), // Modificador Compose: Define tamanho, margem, padding ou clique
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box( // Contentor Compose: Sobrepõe os elementos filhos
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(260.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF0A0A0B), Color(0xFF1F1F22))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(LMRed.copy(alpha = 0.55f), Color.Transparent)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = slide.icon,
                        contentDescription = null,
                        tint = LMWhite,
                        modifier = Modifier.size(84.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                }
            }
        }

        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = slide.titulo,
            fontFamily = Bricolage,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp,
            color = LMInk,
            textAlign = TextAlign.Center,
            lineHeight = 34.sp
        )

        Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = slide.descricao,
            fontFamily = Geist,
            fontSize = 14.sp,
            color = LMGray500,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
        )

        Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
    }
}
