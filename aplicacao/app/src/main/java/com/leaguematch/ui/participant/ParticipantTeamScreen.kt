/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: ParticipantTeamScreen.kt
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
import androidx.compose.material.icons.filled.EmojiEvents // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Groups // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Person // Importa dependência / biblioteca necessária
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
import com.leaguematch.data.remote.model.Equipa // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Jogo // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.TeamCode // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Utilizador // Importa dependência / biblioteca necessária
import com.leaguematch.translations.AppStrings // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.ParticipantBottomBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.* // Importa dependência / biblioteca necessária

@Composable
fun ParticipantTeamScreen( // Declaração de função / método de lógica
    equipa: Equipa?,
    equipas: List<Equipa>,
    jogadores: List<Utilizador>,
    classificacao: Classificacao?,
    jogos: List<Jogo>,
    strings: AppStrings,
    primaryColor: Color,
    onJoinTeamClick: () -> Unit,
    onSelecionarEquipaClick: (Int) -> Unit,
    onSairEquipaClick: (Int) -> Unit,
    onHomeClick: () -> Unit,
    onTorneiosClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEquipaClick: () -> Unit,
    onEstatisticasClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
    val ultimosJogos = jogos // Declara constante local (leitura única)
        .filter { jogo ->
            equipa == null ||
                    jogo.casa.equals(equipa.nome, ignoreCase = true) ||
                    jogo.fora.equals(equipa.nome, ignoreCase = true)
        }
        .take(3)

    Scaffold(
        bottomBar = {
            ParticipantBottomBar(
                selectedItem = "equipa",
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
                text = strings.myTeamTitle,
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = strings.myTeamSubtitle,
                fontFamily = Geist,
                fontSize = 13.sp,
                color = LMGray500
            )

            Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            if (equipas.isEmpty()) { // Estrutura de decisão condicional principal
                TeamInfoCard(
                    title = strings.noTeamTitle,
                    value = strings.noTeamDescription,
                    icon = Icons.Default.Groups,
                    primaryColor = primaryColor
                )

                Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Button( // Componente Compose: Desenha um botão interativo
                    onClick = onJoinTeamClick, // Callback: Define a ação executada ao clicar no componente
                    modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = strings.joinTeamButton,
                        fontFamily = Geist,
                        fontWeight = FontWeight.ExtraBold,
                        color = LMWhite
                    )
                }

                return@Column // Contentor Compose: Alinha os filhos numa coluna vertical
            }

            Button( // Componente Compose: Desenha um botão interativo
                onClick = onJoinTeamClick, // Callback: Define a ação executada ao clicar no componente
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
            ) {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = strings.joinTeamButton,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    color = LMWhite
                )
            }

            Spacer(modifier = Modifier.height(22.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            SectionTitle(strings.myTeamsTitle)

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            equipas.forEach { equipaItem ->
                TeamMembershipCard(
                    equipa = equipaItem,
                    selecionada = equipa?.id == equipaItem.id,
                    strings = strings,
                    primaryColor = primaryColor,
                    onSelecionarEquipaClick = {
                        onSelecionarEquipaClick(equipaItem.id)
                    },
                    onSairEquipaClick = {
                        onSairEquipaClick(equipaItem.id)
                    }
                )

                Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
            }

            if (equipa == null) { // Estrutura de decisão condicional principal
                return@Column // Contentor Compose: Alinha os filhos numa coluna vertical
            }

            Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            SectionTitle(strings.selectedTeamTitle)

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Surface(
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                shape = RoundedCornerShape(14.dp),
                color = primaryColor.copy(alpha = 0.10f)
            ) {
                Column(modifier = Modifier.padding(14.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = strings.teamCode,
                        fontFamily = Geist,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )

                    Spacer(modifier = Modifier.height(2.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = TeamCode.encode(equipa.id),
                        fontFamily = Bricolage,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = LMInk
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            TeamInfoCard(
                title = strings.teamName,
                value = equipa.nome,
                icon = Icons.Default.Groups,
                primaryColor = primaryColor
            )

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            TeamInfoCard(
                title = strings.playersTitle,
                value = strings.playersCount(jogadores.size),
                icon = Icons.Default.Person,
                primaryColor = primaryColor
            )

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            TeamInfoCard(
                title = strings.teamStandingTitle,
                value = if (classificacao != null) { // Estrutura de decisão condicional principal
                    "${strings.pointsLabel(classificacao.pontos)} • ${
                        strings.classificationRecord(
                            classificacao.vitorias,
                            classificacao.empates,
                            classificacao.derrotas
                        )
                    }"
                } else { // Fluxo condicional alternativo caso o 'if' seja falso
                    strings.noStandingYet
                },
                icon = Icons.Default.EmojiEvents,
                primaryColor = primaryColor
            )

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            TeamInfoCard(
                title = strings.lastGamesTitle,
                value = if (ultimosJogos.isEmpty()) { // Estrutura de decisão condicional principal
                    strings.noRegisteredGames
                } else { // Fluxo condicional alternativo caso o 'if' seja falso
                    strings.gamesFound(ultimosJogos.size)
                },
                icon = Icons.Default.SportsSoccer,
                primaryColor = primaryColor
            )

            if (jogadores.isNotEmpty()) { // Estrutura de decisão condicional principal
                Spacer(modifier = Modifier.height(22.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                SectionTitle(strings.playersTitle)

                Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                jogadores.forEach { jogador ->
                    SimpleListCard(
                        title = jogador.nome,
                        subtitle = jogador.email
                    )

                    Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                }
            }

            if (ultimosJogos.isNotEmpty()) { // Estrutura de decisão condicional principal
                Spacer(modifier = Modifier.height(22.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                SectionTitle(strings.lastGamesTitle)

                Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                ultimosJogos.forEach { jogo ->
                    SimpleListCard(
                        title = "${jogo.casa} vs ${jogo.fora}",
                        subtitle = "${jogo.estado} • ${jogo.resultadoCasa}-${jogo.resultadoFora}"
                    )

                    Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                }
            }
        }
    }
}

@Composable
private fun TeamMembershipCard( // Declaração de função / método de lógica
    equipa: Equipa,
    selecionada: Boolean,
    strings: AppStrings,
    primaryColor: Color,
    onSelecionarEquipaClick: () -> Unit,
    onSairEquipaClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(16.dp),
        color = LMWhite,
        border = BorderStroke(
            2.dp,
            if (selecionada) { // Estrutura de decisão condicional principal
                primaryColor
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                Color(0xFFE5E5EA)
            }
        ),
        shadowElevation = 1.dp
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier.padding(14.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = equipa.nome,
                fontFamily = Geist,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = "${strings.tournamentIdLabel}: ${equipa.torneioId} • ${strings.codeLabel}: ${
                    TeamCode.encode(equipa.id)
                }",
                fontFamily = Geist,
                fontSize = 12.sp,
                color = LMGray500
            )

            if (selecionada) { // Estrutura de decisão condicional principal
                Spacer(modifier = Modifier.height(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = strings.selectedTeam,
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = primaryColor
                )
            }

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Button( // Componente Compose: Desenha um botão interativo
                onClick = onSelecionarEquipaClick, // Callback: Define a ação executada ao clicar no componente
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
            ) {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = if (selecionada) { // Estrutura de decisão condicional principal
                        strings.viewDetails
                    } else { // Fluxo condicional alternativo caso o 'if' seja falso
                        strings.selectTeam
                    },
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    color = LMWhite
                )
            }

            Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            OutlinedButton(
                onClick = onSairEquipaClick, // Callback: Define a ação executada ao clicar no componente
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = primaryColor
                )
            ) {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = strings.leaveTeam,
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold
                )
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
private fun TeamInfoCard( // Declaração de função / método de lógica
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    primaryColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                imageVector = icon,
                contentDescription = null,
                tint = primaryColor,
                modifier = Modifier.size(32.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )

            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column { // Contentor Compose: Alinha os filhos numa coluna vertical
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = title,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = LMInk
                )

                Spacer(modifier = Modifier.height(3.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = value,
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }
        }
    }
}

@Composable
private fun SimpleListCard( // Declaração de função / método de lógica
    title: String,
    subtitle: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(16.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        shadowElevation = 1.dp
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier.padding(14.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = title,
                fontFamily = Geist,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(3.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = subtitle,
                fontFamily = Geist,
                fontSize = 12.sp,
                color = LMGray500
            )
        }
    }
}