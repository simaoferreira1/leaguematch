package com.leaguematch.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.ResumoModalidade
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.ui.components.AdminBottomBar
import com.leaguematch.ui.theme.*

// Ecrã principal de gestão e consulta dos torneios
@Composable
fun TorneiosScreen(
    modalidades: List<ResumoModalidade>,
    torneios: List<Torneio>,
    totalTorneios: Int,
    onHomeClick: () -> Unit,
    onUtilizadoresClick: () -> Unit,
    onGraficosClick: () -> Unit,
    onDefinicoesClick: () -> Unit,
    onTorneioClick: (Int) -> Unit,
    onRemoverTorneioClick: (Int) -> Unit
) {
    // Guarda o texto introduzido na pesquisa
    var pesquisa by remember { mutableStateOf("") }
    // Guarda a modalidade atualmente selecionada
    var modalidadeSelecionada by remember(torneios) { mutableStateOf("Todos") }
    // Guarda o torneio que o utilizador pretende desativar
    var torneioParaRemover by remember { mutableStateOf<Torneio?>(null) }

    // Garante que a modalidade selecionada continua válida após atualizações
    LaunchedEffect(torneios) {
        if (modalidadeSelecionada != "Todos" &&
            torneios.none { it.modalidade == modalidadeSelecionada }
        ) {
            modalidadeSelecionada = "Todos"
        }
    }

    // Adiciona a opção "Todos" às modalidades disponíveis
    val modalidadesFiltro = listOf("Todos") + modalidades.map { it.nome }

    // Filtra os torneios pela modalidade e pela pesquisa efetuada
    val torneiosFiltrados = torneios.filter { torneio ->
        // Verifica se o torneio pertence à modalidade selecionada
        val modalidadeOk =
            modalidadeSelecionada == "Todos" || torneio.modalidade == modalidadeSelecionada

        // Verifica se o texto pesquisado existe no nome ou modalidade
        val pesquisaOk =
            pesquisa.isBlank() ||
                    torneio.nome.contains(pesquisa, ignoreCase = true) ||
                    torneio.modalidade.contains(pesquisa, ignoreCase = true)

        modalidadeOk && pesquisaOk
    }

    torneioParaRemover?.let { torneio ->
        // Caixa de diálogo para confirmar a desativação do torneio
        AlertDialog(
            onDismissRequest = { torneioParaRemover = null },

            shape = RoundedCornerShape(22.dp),

            containerColor = LMWhite,

            title = {
                Text(
                    text = "Desativar torneio?",
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    color = LMInk
                )
            },

            text = {
                Text(
                    text = "Tens a certeza que queres desativar \"${torneio.nome}\"?",
                    fontFamily = Geist,
                    color = LMGray500
                )
            },

            confirmButton = {
                TextButton(
                    // Desativa o torneio selecionado
                    onClick = {
                        onRemoverTorneioClick(torneio.id)
                        torneioParaRemover = null
                    }
                ) {
                    Text(
                        "Desativar",
                        color = LMRed,
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold
                    )
                }
            },

            dismissButton = {
                TextButton(
                    // Fecha a janela sem realizar alterações
                    onClick = {
                        torneioParaRemover = null
                    }
                ) {
                    Text(
                        "Cancelar",
                        color = LMGray500,
                        fontFamily = Geist,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        )
    }

    // Estrutura principal do ecrã com barra de navegação inferior
    Scaffold(
        bottomBar = {
            AdminBottomBar(
                selectedItem = "torneios",
                onHomeClick = onHomeClick,
                onUtilizadoresClick = onUtilizadoresClick,
                onTorneiosClick = {},
                onGraficosClick = onGraficosClick,
                onDefinicoesClick = onDefinicoesClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        // Conteúdo principal da página
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Cabeçalho com campo de pesquisa
            SearchHeader(
                pesquisa = pesquisa,
                onPesquisaChange = { pesquisa = it },
                onSettingsClick = onDefinicoesClick
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Lista de modalidades para filtrar os torneios
            ModalidadeChips(
                modalidades = modalidadesFiltro,
                selecionada = modalidadeSelecionada,
                onSelecionar = { modalidadeSelecionada = it }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Mostra quantos torneios estão visíveis após os filtros
            Text(
                text = "${torneiosFiltrados.size} de $totalTorneios torneios",
                fontFamily = Geist,
                fontSize = 12.sp,
                color = LMGray500,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Apresenta os torneios filtrados
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Mostra mensagem caso não existam torneios correspondentes aos filtros
                if (torneiosFiltrados.isEmpty()) {
                    EmptyTorneiosMessage()
                } else {
                    // Percorre todos os torneios filtrados para criar os cartões
                    torneiosFiltrados.forEachIndexed { index, torneio ->
                        TorneioCard(
                            torneio = torneio,
                            index = index,
                            onClick = { onTorneioClick(torneio.id) },
                            onRemoverClick = { torneioParaRemover = torneio }
                        )
                    }
                }
            }
        }
    }
}

// Componente responsável pela pesquisa de torneios
@Composable
private fun SearchHeader(
    pesquisa: String,
    onPesquisaChange: (String) -> Unit,
    onSettingsClick: () -> Unit
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
        // Ícone visual da barra de pesquisa
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = LMGray500,
            modifier = Modifier.size(20.dp)
        )

        // Campo onde o utilizador escreve o texto de pesquisa
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

// Cria os botões de seleção das modalidades
@Composable
private fun ModalidadeChips(
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
        // Percorre todas as modalidades disponíveis
        modalidades.forEach { modalidade ->
            // Verifica se esta modalidade está selecionada
            val ativo = modalidade == selecionada

            Surface(
                // Atualiza a modalidade selecionada
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

// Cartão que apresenta a informação resumida de um torneio
@Composable
private fun TorneioCard(
    torneio: Torneio,
    index: Int,
    onClick: () -> Unit,
    onRemoverClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            // Abre os detalhes do torneio ao clicar no cartão
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
            // Área gráfica com o ícone do troféu
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
                // Ícone representativo do torneio
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
                // Mostra a modalidade do torneio
                Text(
                    text = torneio.modalidade.uppercase(),
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = LMGray500
                )

                Spacer(modifier = Modifier.height(3.dp))

                // Nome do torneio
                Text(
                    text = torneio.nome,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = LMInk
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Mostra o número de equipas e o estado atual do torneio
                Text(
                    text = "${torneio.equipas} equipas • ${torneio.estado}",
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }

            // Botão para desativar o torneio
            Surface(
                modifier = Modifier.clickable { onRemoverClick() },
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFFFFEEF1)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = LMRed,
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "Desativar",
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = LMRed
                    )
                }
            }
        }
    }
}

// Mensagem apresentada quando não existem torneios para mostrar
@Composable
private fun EmptyTorneiosMessage() {
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

// Devolve o ícone correspondente a cada modalidade
private fun iconForModalidade(modalidade: String): ImageVector {
    return when (modalidade) {
        "Futebol" -> Icons.Default.SportsSoccer
        "Basquetebol" -> Icons.Default.SportsBasketball
        "Andebol" -> Icons.Default.SportsHandball
        else -> Icons.Default.SportsTennis
    }
}

// Define as cores do cartão conforme a modalidade do torneio
private fun colorsForTorneio(torneio: Torneio, index: Int): List<Color> {
    return when {
        torneio.modalidade == "Futebol" && index % 3 == 0 -> listOf(LMRed, LMRed700)
        torneio.modalidade == "Basquetebol" -> listOf(Color(0xFF2563EB), Color(0xFF1D4ED8))
        torneio.modalidade == "Andebol" -> listOf(Color(0xFF16A34A), Color(0xFF15803D))
        torneio.modalidade == "Padel" -> listOf(Color(0xFF1F2937), Color(0xFF111827))
        else -> listOf(Color(0xFFBE123C), Color(0xFF9F1239))
    }
}