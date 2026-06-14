/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: OrgTournamentsScreen.kt
 * Tipo: Interface (Compose View) do Organizador
 *
 * Descrição:
 * Este ficheiro define um ecrã da área do Organizador em Jetpack Compose.\n * Fornece interface e lógica visual para criar torneios, gerir equipas, registar e editar jogos e estatísticas.
 */
package com.leaguematch.ui.organizer // Define o pacote deste ficheiro de código

import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.clickable // Importa dependência / biblioteca necessária
import androidx.compose.foundation.horizontalScroll // Importa dependência / biblioteca necessária
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
import androidx.compose.material.icons.filled.Add // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.EmojiEvents // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Search // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsBasketball // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsHandball // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsSoccer // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsTennis // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Surface // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.material3.TextField // Importa dependência / biblioteca necessária
import androidx.compose.material3.TextFieldDefaults // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.LaunchedEffect // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.remember // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Brush // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.vector.ImageVector // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.style.TextOverflow // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.ResumoModalidade // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Torneio // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.OrganizerBottomBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Bricolage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária

@Composable
fun OrgTournamentsScreen( // Declaração de função / método de lógica
    modalidades: List<ResumoModalidade>,
    torneios: List<Torneio>,
    totalTorneios: Int,
    onNavigateToCreate: () -> Unit = {},
    onNavigateToActions: (Int) -> Unit = {},
    onEquipasClick: () -> Unit = {},
    onJogosClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {},
    accentColor: Color = LMRed
) {
    var pesquisa by remember { mutableStateOf("") } // Declara estado mutável local do Compose
    var modalidadeSelecionada by remember(torneios) { mutableStateOf("Todos") } // Declara estado mutável local do Compose

    LaunchedEffect(torneios) { // Efeito colateral Compose: executa código assíncrono ao recompor
        if ( // Estrutura de decisão condicional principal
            modalidadeSelecionada != "Todos" &&
            torneios.none { it.modalidade == modalidadeSelecionada }
        ) {
            modalidadeSelecionada = "Todos"
        }
    }

    val modalidadesFiltro = listOf("Todos") + modalidades.map { it.nome } // Declara constante local (leitura única)

    val torneiosFiltrados = torneios.filter { torneio -> // Declara constante local (leitura única)
        val modalidadeOk = // Declara constante local (leitura única)
            modalidadeSelecionada == "Todos" || torneio.modalidade == modalidadeSelecionada

        val pesquisaOk = // Declara constante local (leitura única)
            pesquisa.isBlank() ||
                    torneio.nome.contains(pesquisa, ignoreCase = true) ||
                    torneio.modalidade.contains(pesquisa, ignoreCase = true) ||
                    torneio.estado.contains(pesquisa, ignoreCase = true)

        modalidadeOk && pesquisaOk
    }

    Scaffold(
        bottomBar = {
            OrganizerBottomBar(
                selectedItem = "torneios",
                onTorneiosClick = {},
                onPerfilClick = onPerfilClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Box( // Contentor Compose: Sobrepõe os elementos filhos
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp, vertical = 14.dp)
                    .padding(bottom = 90.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                TranslatedText(
                    text = "Os meus torneios",
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = LMInk,
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                TranslatedText(
                    text = "$totalTorneios torneios associados ao organizador",
                    fontFamily = Geist,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = LMGray500
                )

                Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                OrganizerSearchHeader(
                    pesquisa = pesquisa,
                    onPesquisaChange = { pesquisa = it }
                )

                Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                OrganizerModalidadeChips(
                    modalidades = modalidadesFiltro,
                    selecionada = modalidadeSelecionada,
                    onSelecionar = { modalidadeSelecionada = it }
                )

                Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                TranslatedText(
                    text = "${torneiosFiltrados.size} de $totalTorneios torneios",
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (torneiosFiltrados.isEmpty()) { // Estrutura de decisão condicional principal
                        EmptyOrganizerTorneiosMessage()
                    } else { // Fluxo condicional alternativo caso o 'if' seja falso
                        torneiosFiltrados.forEachIndexed { index, torneio ->
                            OrganizerTorneioCard(
                                torneio = torneio,
                                index = index,
                                onClick = { onNavigateToActions(torneio.id) }, // Callback: Define a ação executada ao clicar no componente
                                accentColor = accentColor
                            )
                        }
                    }
                }
            }

            CriarTorneioButton(
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .align(Alignment.BottomEnd)
                    .padding(end = 18.dp, bottom = 24.dp),
                onClick = onNavigateToCreate, // Callback: Define a ação executada ao clicar no componente
                accentColor = accentColor
            )
        }
    }
}

@Composable
private fun OrganizerSearchHeader( // Declaração de função / método de lógica
    pesquisa: String,
    onPesquisaChange: (String) -> Unit
) {
    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .background(
                color = Color(0xFFF3F3F5),
                shape = RoundedCornerShape(14.dp)
            )
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
            onValueChange = onPesquisaChange,
            placeholder = {
                TranslatedText(
                    text = "Pesquisar torneios...",
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
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun OrganizerModalidadeChips( // Declaração de função / método de lógica
    modalidades: List<String>,
    selecionada: String,
    onSelecionar: (String) -> Unit
) {
    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        modalidades.forEach { modalidade ->
            val ativo = modalidade == selecionada // Declara constante local (leitura única)

            Surface(
                modifier = Modifier.clickable { onSelecionar(modalidade) }, // Modificador Compose: Define tamanho, margem, padding ou clique
                shape = RoundedCornerShape(22.dp),
                color = if (ativo) LMInk else LMWhite, // Estrutura de decisão condicional principal
                border = BorderStroke(
                    width = 1.dp,
                    color = if (ativo) LMInk else Color(0xFFE2E2E7) // Estrutura de decisão condicional principal
                )
            ) {
                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = iconForModalidade(modalidade),
                        contentDescription = null,
                        tint = if (ativo) LMWhite else LMGray500, // Estrutura de decisão condicional principal
                        modifier = Modifier.size(16.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )

                    Spacer(modifier = Modifier.width(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    TranslatedText(
                        text = modalidade,
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (ativo) LMWhite else LMInk // Estrutura de decisão condicional principal
                    )
                }
            }
        }
    }
}

@Composable
private fun OrganizerTorneioCard( // Declaração de função / método de lógica
    torneio: Torneio,
    index: Int,
    onClick: () -> Unit, // Callback: Define a ação executada ao clicar no componente
    accentColor: Color = LMRed
) {
    Surface(
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .clickable { onClick() }, // Callback: Define a ação executada ao clicar no componente
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(12.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(60.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = colorsForTorneio(torneio, index, accentColor)
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

            Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
            ) {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = torneio.modalidade.uppercase(),
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = LMGray500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(3.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = torneio.nome,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = LMInk,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                TranslatedText(
                    text = "${torneio.equipas} equipas",
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }

            OrganizerEstadoPill(estado = torneio.estado)
        }
    }
}

@Composable
private fun OrganizerEstadoPill( // Declaração de função / método de lógica
    estado: String
) {
    val estadoLower = estado.lowercase() // Declara constante local (leitura única)

    val backgroundColor = when { // Escolha múltipla condicional (semelhante a switch-case)
        estadoLower.contains("decorrer") || estadoLower.contains("progresso") ->
            Color(0xFFEFFBF3)

        estadoLower.contains("iniciar") || estadoLower.contains("brevemente") ->
            Color(0xFFFFF7E6)

        estadoLower.contains("terminado") || estadoLower.contains("finalizado") ->
            Color(0xFFF3F3F5)

        else -> Color(0xFFF3F3F5) // Fluxo condicional alternativo caso o 'if' seja falso
    }

    val textColor = when { // Escolha múltipla condicional (semelhante a switch-case)
        estadoLower.contains("decorrer") || estadoLower.contains("progresso") ->
            Color(0xFF15803D)

        estadoLower.contains("iniciar") || estadoLower.contains("brevemente") ->
            Color(0xFFD97706)

        estadoLower.contains("terminado") || estadoLower.contains("finalizado") ->
            LMGray500

        else -> LMGray500 // Fluxo condicional alternativo caso o 'if' seja falso
    }

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = backgroundColor
    ) {
        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = estado,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            fontFamily = Geist,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun CriarTorneioButton( // Declaração de função / método de lógica
    modifier: Modifier = Modifier, // Modificador Compose: Define tamanho, margem, padding ou clique
    onClick: () -> Unit, // Callback: Define a ação executada ao clicar no componente
    accentColor: Color = LMRed
) {
    Box( // Contentor Compose: Sobrepõe os elementos filhos
        modifier = modifier
            .background(
                brush = Brush.linearGradient(
                    listOf(accentColor, accentColor.copy(alpha = 0.85f))
                ),
                shape = RoundedCornerShape(99.dp)
            )
            .clickable { onClick() } // Callback: Define a ação executada ao clicar no componente
            .padding(horizontal = 18.dp, vertical = 13.dp),
        contentAlignment = Alignment.Center
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = LMWhite,
                modifier = Modifier.size(16.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )

            TranslatedText(
                text = "Criar torneio",
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = LMWhite
            )
        }
    }
}

@Composable
private fun EmptyOrganizerTorneiosMessage() { // Declaração de função / método de lógica
    Box( // Contentor Compose: Sobrepõe os elementos filhos
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .padding(top = 60.dp),
        contentAlignment = Alignment.Center
    ) {
        TranslatedText(
            text = "Nenhum torneio encontrado.",
            fontFamily = Geist,
            fontSize = 14.sp,
            color = LMGray500
        )
    }
}

private fun iconForModalidade(modalidade: String): ImageVector { // Declaração de função / método de lógica
    return when (modalidade) { // Escolha múltipla condicional (semelhante a switch-case)
        "Futebol" -> Icons.Default.SportsSoccer
        "Basquetebol" -> Icons.Default.SportsBasketball
        "Andebol" -> Icons.Default.SportsHandball
        else -> Icons.Default.SportsTennis // Fluxo condicional alternativo caso o 'if' seja falso
    }
}

private fun colorsForTorneio(torneio: Torneio, index: Int, accentColor: Color = LMRed): List<Color> { // Declaração de função / método de lógica
    return when { // Escolha múltipla condicional (semelhante a switch-case)
        torneio.modalidade == "Futebol" && index % 3 == 0 ->
            listOf(accentColor, accentColor.copy(alpha = 0.85f))

        torneio.modalidade == "Basquetebol" ->
            listOf(Color(0xFF2563EB), Color(0xFF1D4ED8))

        torneio.modalidade == "Andebol" ->
            listOf(Color(0xFF16A34A), Color(0xFF15803D))

        torneio.modalidade == "Padel" ->
            listOf(Color(0xFF1F2937), Color(0xFF111827))

        else -> // Fluxo condicional alternativo caso o 'if' seja falso
            listOf(Color(0xFFBE123C), Color(0xFF9F1239))
    }
}