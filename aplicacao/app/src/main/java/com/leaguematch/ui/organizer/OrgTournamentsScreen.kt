package com.leaguematch.ui.organizer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.SportsHandball
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.ResumoModalidade
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.ui.components.OrganizerBottomBar
import com.leaguematch.ui.theme.*

@Composable
fun OrgTournamentsScreen(
    modalidades: List<ResumoModalidade>,
    torneios: List<Torneio>,
    totalTorneios: Int,
    onNavigateToCreate: () -> Unit = {},
    onNavigateToActions: (Int) -> Unit = {},
    onEquipasClick: () -> Unit = {},
    onJogosClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {}
) {
    var pesquisa by remember { mutableStateOf("") }
    var modalidadeSelecionada by remember(torneios) { mutableStateOf("Todos") }

    LaunchedEffect(torneios) {
        if (
            modalidadeSelecionada != "Todos" &&
            torneios.none { it.modalidade == modalidadeSelecionada }
        ) {
            modalidadeSelecionada = "Todos"
        }
    }

    val modalidadesFiltro = listOf("Todos") + modalidades.map { it.nome }

    val torneiosFiltrados = torneios.filter { torneio ->
        val modalidadeOk =
            modalidadeSelecionada == "Todos" || torneio.modalidade == modalidadeSelecionada

        val pesquisaOk =
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
                onEquipasClick = onEquipasClick,
                onJogosClick = onJogosClick,
                onPerfilClick = onPerfilClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp, vertical = 14.dp)
                    .padding(bottom = 90.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Os meus torneios",
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = LMInk,
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$totalTorneios torneios associados ao organizador",
                    fontFamily = Geist,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = LMGray500
                )

                Spacer(modifier = Modifier.height(18.dp))

                OrganizerSearchHeader(
                    pesquisa = pesquisa,
                    onPesquisaChange = { pesquisa = it }
                )

                Spacer(modifier = Modifier.height(18.dp))

                OrganizerModalidadeChips(
                    modalidades = modalidadesFiltro,
                    selecionada = modalidadeSelecionada,
                    onSelecionar = { modalidadeSelecionada = it }
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "${torneiosFiltrados.size} de $totalTorneios torneios",
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (torneiosFiltrados.isEmpty()) {
                        EmptyOrganizerTorneiosMessage()
                    } else {
                        torneiosFiltrados.forEachIndexed { index, torneio ->
                            OrganizerTorneioCard(
                                torneio = torneio,
                                index = index,
                                onClick = { onNavigateToActions(torneio.id) }
                            )
                        }
                    }
                }
            }

            CriarTorneioButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 18.dp, bottom = 24.dp),
                onClick = onNavigateToCreate
            )
        }
    }
}

@Composable
private fun OrganizerSearchHeader(
    pesquisa: String,
    onPesquisaChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF3F3F5),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 12.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = LMGray500,
            modifier = Modifier.size(20.dp)
        )

        TextField(
            value = pesquisa,
            onValueChange = onPesquisaChange,
            placeholder = {
                Text(
                    text = "Pesquisar torneios...",
                    fontFamily = Geist,
                    fontSize = 13.sp,
                    color = LMGray500
                )
            },
            modifier = Modifier.weight(1f),
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
private fun OrganizerModalidadeChips(
    modalidades: List<String>,
    selecionada: String,
    onSelecionar: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        modalidades.forEach { modalidade ->
            val ativo = modalidade == selecionada

            Surface(
                modifier = Modifier.clickable { onSelecionar(modalidade) },
                shape = RoundedCornerShape(22.dp),
                color = if (ativo) LMInk else LMWhite,
                border = BorderStroke(
                    width = 1.dp,
                    color = if (ativo) LMInk else Color(0xFFE2E2E7)
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = iconForModalidade(modalidade),
                        contentDescription = null,
                        tint = if (ativo) LMWhite else LMGray500,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = modalidade,
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (ativo) LMWhite else LMInk
                    )
                }
            }
        }
    }
}

@Composable
private fun OrganizerTorneioCard(
    torneio: Torneio,
    index: Int,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = colorsForTorneio(torneio, index)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = LMWhite,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = torneio.modalidade.uppercase(),
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = LMGray500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = torneio.nome,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = LMInk,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
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
private fun OrganizerEstadoPill(
    estado: String
) {
    val estadoLower = estado.lowercase()

    val backgroundColor = when {
        estadoLower.contains("decorrer") || estadoLower.contains("progresso") ->
            Color(0xFFEFFBF3)

        estadoLower.contains("iniciar") || estadoLower.contains("brevemente") ->
            Color(0xFFFFF7E6)

        estadoLower.contains("terminado") || estadoLower.contains("finalizado") ->
            Color(0xFFF3F3F5)

        else -> Color(0xFFF3F3F5)
    }

    val textColor = when {
        estadoLower.contains("decorrer") || estadoLower.contains("progresso") ->
            Color(0xFF15803D)

        estadoLower.contains("iniciar") || estadoLower.contains("brevemente") ->
            Color(0xFFD97706)

        estadoLower.contains("terminado") || estadoLower.contains("finalizado") ->
            LMGray500

        else -> LMGray500
    }

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = backgroundColor
    ) {
        Text(
            text = estado,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
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
private fun CriarTorneioButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(
                    listOf(LMRed, Color(0xFFC41326))
                ),
                shape = RoundedCornerShape(99.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 13.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = LMWhite,
                modifier = Modifier.size(16.dp)
            )

            Text(
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
private fun EmptyOrganizerTorneiosMessage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Nenhum torneio encontrado.",
            fontFamily = Geist,
            fontSize = 14.sp,
            color = LMGray500
        )
    }
}

private fun iconForModalidade(modalidade: String): ImageVector {
    return when (modalidade) {
        "Futebol" -> Icons.Default.SportsSoccer
        "Basquetebol" -> Icons.Default.SportsBasketball
        "Andebol" -> Icons.Default.SportsHandball
        else -> Icons.Default.SportsTennis
    }
}

private fun colorsForTorneio(torneio: Torneio, index: Int): List<Color> {
    return when {
        torneio.modalidade == "Futebol" && index % 3 == 0 ->
            listOf(LMRed, LMRed700)

        torneio.modalidade == "Basquetebol" ->
            listOf(Color(0xFF2563EB), Color(0xFF1D4ED8))

        torneio.modalidade == "Andebol" ->
            listOf(Color(0xFF16A34A), Color(0xFF15803D))

        torneio.modalidade == "Padel" ->
            listOf(Color(0xFF1F2937), Color(0xFF111827))

        else ->
            listOf(Color(0xFFBE123C), Color(0xFF9F1239))
    }
}