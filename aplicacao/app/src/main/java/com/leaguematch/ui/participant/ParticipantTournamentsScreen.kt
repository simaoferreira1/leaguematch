/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: ParticipantTournamentsScreen.kt
 * Tipo: Interface (Compose View) do Participante
 *
 * Descrição:
 * Este ficheiro define um ecrã do fluxo do Jogador/Participante em Jetpack Compose.\n * Mostra ao participante o estado do seu torneio, código de equipas para inscrição, estatísticas e notificações.
 */
package com.leaguematch.ui.participant // Define o pacote deste ficheiro de código

import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.clickable // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.* // Importa dependência / biblioteca necessária
import androidx.compose.foundation.rememberScrollState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.EmojiEvents // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Search // Importa dependência / biblioteca necessária
import androidx.compose.material3.* // Importa dependência / biblioteca necessária
import androidx.compose.runtime.* // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Brush // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.style.TextOverflow // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Torneio // Importa dependência / biblioteca necessária
import com.leaguematch.translations.AppStrings // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.ParticipantBottomBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.* // Importa dependência / biblioteca necessária

@Composable
fun ParticipantTournamentsScreen( // Declaração de função / método de lógica
    torneios: List<Torneio>,
    strings: AppStrings,
    primaryColor: Color,
    onTournamentClick: (Int) -> Unit,
    onHomeClick: () -> Unit,
    onTorneiosClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEquipaClick: () -> Unit,
    onEstatisticasClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
    var pesquisa by remember { mutableStateOf("") } // Declara estado mutável local do Compose

    val torneiosFiltrados = torneios.filter { // Declara constante local (leitura única)
        pesquisa.isBlank() ||
                it.nome.contains(pesquisa, ignoreCase = true) ||
                it.modalidade.contains(pesquisa, ignoreCase = true) ||
                it.estado.contains(pesquisa, ignoreCase = true)
    }

    Scaffold(
        bottomBar = {
            ParticipantBottomBar(
                selectedItem = "torneios",
                onHomeClick = onHomeClick,
                onTorneiosClick = onTorneiosClick,
                onJogosClick = onJogosClick,
                onEquipaClick = onEquipaClick,
                onEstatisticasClick = onEstatisticasClick,
                onPerfilClick = onPerfilClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 18.dp)
                .padding(bottom = 80.dp)
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = strings.registeredTournaments,
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = strings.tournamentsAssociated(torneios.size),
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500
            )

            Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .background(Color(0xFFF3F3F5), RoundedCornerShape(14.dp))
                    .padding(horizontal = 12.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon( // Componente Compose: Desenha um ícone vetorial
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = LMGray500,
                    modifier = Modifier.size(20.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                TextField( // Campo Compose: Entrada de texto simples para utilizador
                    value = pesquisa,
                    onValueChange = { pesquisa = it },
                    placeholder = {
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = strings.searchTournamentsPlaceholder,
                            fontFamily = Geist,
                            fontSize = 13.sp,
                            color = LMGray500
                        )
                    },
                    modifier = Modifier.weight(1f), // Modificador Compose: Define tamanho, margem, padding ou clique
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            if (torneiosFiltrados.isEmpty()) { // Estrutura de decisão condicional principal
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = strings.noRegisteredTournaments,
                    fontFamily = Geist,
                    fontSize = 14.sp,
                    color = LMGray500
                )
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                torneiosFiltrados.forEachIndexed { index, torneio ->
                    ParticipantTournamentCard(
                        torneio = torneio,
                        index = index,
                        strings = strings,
                        primaryColor = primaryColor,
                        onClick = { // Callback: Define a ação executada ao clicar no componente
                            onTournamentClick(torneio.id)
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                }
            }
        }
    }
}

@Composable
private fun ParticipantTournamentCard( // Declaração de função / método de lógica
    torneio: Torneio,
    index: Int,
    strings: AppStrings,
    primaryColor: Color,
    onClick: () -> Unit // Callback: Define a ação executada ao clicar no componente
) {
    Surface(
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .clickable { onClick() }, // Callback: Define a ação executada ao clicar no componente
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(12.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(58.dp)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                primaryColor,
                                primaryColor.copy(alpha = 0.82f)
                            )
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon( // Componente Compose: Desenha um ícone vetorial
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = LMWhite,
                    modifier = Modifier.size(30.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = torneio.modalidade.uppercase(),
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = LMGray500
                )

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = torneio.nome,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = LMInk,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = strings.teamsLabel(torneio.equipas),
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }

            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFFEFFBF3)
            ) {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = torneio.estado,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = Color(0xFF15803D)
                )
            }
        }
    }
}