/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: OrgGerirJogadoresScreen.kt
 * Tipo: Interface (Compose View) do Organizador
 *
 * Descrição:
 * Este ficheiro define um ecrã da área do Organizador em Jetpack Compose.\n * Fornece interface e lógica visual para criar torneios, gerir equipas, registar e editar jogos e estatísticas.
 */
package com.leaguematch.ui.organizer // Define o pacote deste ficheiro de código

import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Arrangement // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Box // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Column // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Row // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Spacer // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.fillMaxSize // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.fillMaxWidth // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.height // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.padding // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.size // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.width // Importa dependência / biblioteca necessária
import androidx.compose.foundation.rememberScrollState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.DeleteOutline // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Person // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.rounded.ArrowBack // Importa dependência / biblioteca necessária
import androidx.compose.material3.AlertDialog // Importa dependência / biblioteca necessária
import androidx.compose.material3.CircularProgressIndicator // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.IconButton // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Surface // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.material3.TextButton // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.remember // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Equipa // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.TeamCode // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Utilizador // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMBorder // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray100 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray300 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray400 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray50 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray600 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed50 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária

@Composable
fun OrgGerirJogadoresScreen( // Declaração de função / método de lógica
    equipa: Equipa,
    jogadores: List<Utilizador>,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onRemoverJogador: (Utilizador) -> Unit
) {
    var jogadorParaRemover by remember { mutableStateOf<Utilizador?>(null) } // Declara estado mutável local do Compose

    if (jogadorParaRemover != null) { // Estrutura de decisão condicional principal
        AlertDialog(
            onDismissRequest = { jogadorParaRemover = null },
            title = {
                TranslatedText(
                    text = "Remover jogador?",
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                TranslatedText(
                    text = "Queres remover \"${jogadorParaRemover!!.nome}\" da equipa?",
                    fontFamily = Geist,
                    fontSize = 13.sp,
                    color = LMGray600
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { // Callback: Define a ação executada ao clicar no componente
                        onRemoverJogador(jogadorParaRemover!!)
                        jogadorParaRemover = null
                    }
                ) {
                    TranslatedText(
                        text = "Remover",
                        color = LMRed,
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { jogadorParaRemover = null }) { // Callback: Define a ação executada ao clicar no componente
                    TranslatedText(
                        text = "Cancelar",
                        fontFamily = Geist,
                        color = LMGray500
                    )
                }
            },
            shape = RoundedCornerShape(18.dp)
        )
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
                .padding(bottom = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row(verticalAlignment = Alignment.CenterVertically) { // Contentor Compose: Alinha os filhos numa linha horizontal
                IconButton(onClick = onBackClick) { // Componente Compose: Desenha um botão com ícone
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = null,
                        tint = LMInk
                    )
                }

                Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    TranslatedText(
                        text = "Jogadores",
                        fontFamily = Geist,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = LMInk
                    )

                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = equipa.nome,
                        fontFamily = Geist,
                        fontSize = 12.sp,
                        color = LMGray500
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Surface(
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                shape = RoundedCornerShape(14.dp),
                color = LMRed50,
                border = BorderStroke(1.dp, LMBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    TranslatedText(
                        text = "CÓDIGO DE INTEGRAÇÃO",
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = LMGray500,
                        letterSpacing = 0.4.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = TeamCode.encode(equipa.id),
                        fontFamily = Geist,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = LMInk,
                        letterSpacing = 3.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    TranslatedText(
                        text = "Partilha este código com os jogadores. Eles entram pela secção \"Integrar Equipa\" da app.",
                        fontFamily = Geist,
                        fontSize = 12.sp,
                        color = LMGray600
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            TranslatedText(
                text = "JOGADORES · ${jogadores.size}",
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = LMGray500,
                letterSpacing = 0.4.sp
            )

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            if (isLoading) { // Estrutura de decisão condicional principal
                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = LMRed)
                }
            } else if (jogadores.isEmpty()) { // Estrutura de decisão condicional principal
                Surface(
                    modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                    shape = RoundedCornerShape(16.dp),
                    color = LMGray50,
                    border = BorderStroke(1.dp, LMBorder)
                ) {
                    Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                        modifier = Modifier.padding(24.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon( // Componente Compose: Desenha um ícone vetorial
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = LMGray300,
                            modifier = Modifier.size(40.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                        )

                        Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                        TranslatedText(
                            text = "Sem jogadores na equipa.",
                            fontFamily = Geist,
                            fontSize = 13.sp,
                            color = LMGray500
                        )

                        TranslatedText(
                            text = "Partilha o código acima para os jogadores se inscreverem.",
                            fontFamily = Geist,
                            fontSize = 12.sp,
                            color = LMGray400
                        )
                    }
                }
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    jogadores.forEach { jogador ->
                        JogadorListItem(
                            jogador = jogador,
                            onRemover = { jogadorParaRemover = jogador }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun JogadorListItem( // Declaração de função / método de lógica
    jogador: Utilizador,
    onRemover: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(14.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, LMBorder),
        shadowElevation = 1.dp
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(40.dp)
                    .background(LMGray100, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon( // Componente Compose: Desenha um ícone vetorial
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = LMGray500,
                    modifier = Modifier.size(20.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = jogador.nome,
                    fontFamily = Geist,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = LMInk
                )

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = jogador.email,
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }

            IconButton( // Componente Compose: Desenha um botão com ícone
                onClick = onRemover, // Callback: Define a ação executada ao clicar no componente
                modifier = Modifier.size(32.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            ) {
                Icon( // Componente Compose: Desenha um ícone vetorial
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Remover",
                    tint = LMGray400,
                    modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }
        }
    }
}