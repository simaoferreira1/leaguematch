/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: ParticipantTournamentDetailsScreen.kt
 * Tipo: Interface (Compose View) do Participante
 *
 * Descrição:
 * Este ficheiro define um ecrã do fluxo do Jogador/Participante em Jetpack Compose.\n * Mostra ao participante o estado do seu torneio, código de equipas para inscrição, estatísticas e notificações.
 */
package com.leaguematch.ui.participant // Define o pacote deste ficheiro de código

import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.* // Importa dependência / biblioteca necessária
import androidx.compose.foundation.rememberScrollState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.ArrowBack // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsSoccer // Importa dependência / biblioteca necessária
import androidx.compose.material3.* // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Classificacao // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.DetalheTorneio // Importa dependência / biblioteca necessária
import com.leaguematch.translations.AppStrings // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.* // Importa dependência / biblioteca necessária

@Composable
fun ParticipantTournamentDetailScreen( // Declaração de função / método de lógica
    detalhe: DetalheTorneio?,
    classificacao: List<Classificacao>,
    strings: AppStrings,
    primaryColor: Color,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 18.dp)
        ) {
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) { // Componente Compose: Desenha um botão com ícone
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = LMInk
                    )
                }

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = strings.tournamentDetailsTitle,
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp,
                    color = LMInk
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            if (detalhe == null) { // Estrutura de decisão condicional principal
                EmptyText(strings.tournamentDetailsLoadError)
                return@Column // Contentor Compose: Alinha os filhos numa coluna vertical
            }

            TournamentInfoCard(
                title = detalhe.torneio.nome,
                subtitle = "${detalhe.torneio.modalidade} • ${detalhe.torneio.formato}",
                extra = detalhe.torneio.estado
            )

            Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            SectionTitle(strings.standingsTitle)

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            if (classificacao.isEmpty()) { // Estrutura de decisão condicional principal
                EmptyText(strings.noStandingsYet)
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                classificacao.forEachIndexed { index, item ->
                    ClassificationCard(
                        posicao = index + 1,
                        classificacao = item,
                        strings = strings,
                        primaryColor = primaryColor
                    )
                    Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                }
            }

            Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            SectionTitle(strings.matchesTitle)

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            if (detalhe.jogos.isEmpty()) { // Estrutura de decisão condicional principal
                EmptyText(strings.noMatchesYet)
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                detalhe.jogos.forEach { jogo ->
                    MatchCard(
                        title = "${jogo.casa} vs ${jogo.fora}",
                        subtitle = "${jogo.estado} • ${jogo.data} ${jogo.hora}",
                        result = "${jogo.resultadoCasa}-${jogo.resultadoFora}",
                        primaryColor = primaryColor
                    )
                    Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                }
            }

            Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            SectionTitle(strings.topScorersTitle)

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            if (detalhe.goleadores.isEmpty()) { // Estrutura de decisão condicional principal
                EmptyText(strings.noScorersYet)
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                detalhe.goleadores.forEach { goleador ->
                    SimpleInfoCard(
                        title = goleador.nome,
                        subtitle = strings.goalsLabel(goleador.golos),
                        primaryColor = primaryColor
                    )
                    Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) { // Declaração de função / método de lógica
    Text( // Componente Compose: Desenha texto estruturado no ecrã
        text = text,
        fontFamily = Bricolage,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 21.sp,
        color = LMInk
    )
}

@Composable
private fun TournamentInfoCard( // Declaração de função / método de lógica
    title: String,
    subtitle: String,
    extra: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(20.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier.padding(16.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = title,
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = subtitle,
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500
            )

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFEFFBF3)
            ) {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = extra,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
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
private fun ClassificationCard( // Declaração de função / método de lógica
    posicao: Int,
    classificacao: Classificacao,
    strings: AppStrings,
    primaryColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(16.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = "$posicao.º",
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = primaryColor
            )

            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = classificacao.nomeEquipa,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = LMInk
                )

                Text( // Componente Compose: Desenha texto estruturado no ecrã
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

            Text( // Componente Compose: Desenha texto estruturado no ecrã
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
private fun MatchCard( // Declaração de função / método de lógica
    title: String,
    subtitle: String,
    result: String,
    primaryColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(16.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                imageVector = Icons.Default.SportsSoccer,
                contentDescription = null,
                tint = primaryColor,
                modifier = Modifier.size(30.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )

            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = title,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = LMInk
                )

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = subtitle,
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }

            Text( // Componente Compose: Desenha texto estruturado no ecrã
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
private fun SimpleInfoCard( // Declaração de função / método de lógica
    title: String,
    subtitle: String,
    primaryColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(16.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                imageVector = Icons.Default.SportsSoccer,
                contentDescription = null,
                tint = primaryColor,
                modifier = Modifier.size(28.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )

            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column { // Contentor Compose: Alinha os filhos numa coluna vertical
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = title,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = LMInk
                )

                Text( // Componente Compose: Desenha texto estruturado no ecrã
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
private fun EmptyText(text: String) { // Declaração de função / método de lógica
    Text( // Componente Compose: Desenha texto estruturado no ecrã
        text = text,
        fontFamily = Geist,
        fontSize = 14.sp,
        color = LMGray500
    )
}