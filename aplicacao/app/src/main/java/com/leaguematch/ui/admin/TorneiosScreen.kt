/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: TorneiosScreen.kt
 * Tipo: Interface (Compose View) do Administrador
 *
 * Descrição:
 * Este ficheiro define um ecrã da área do Administrador em Jetpack Compose.\n * Ele desenha componentes visuais reativos baseado no estado fornecido pelo respetivo ViewModel.\n * Permite ao Admin gerir utilizadores (ativar/desativar), visualizar alertas do sistema e gráficos.
 */
package com.leaguematch.ui.admin // Define o pacote deste ficheiro de código

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
import androidx.compose.material.icons.filled.Delete // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.EmojiEvents // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Search // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsBasketball // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsHandball // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsSoccer // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsTennis // Importa dependência / biblioteca necessária
import androidx.compose.material3.AlertDialog // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Surface // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.material3.TextButton // Importa dependência / biblioteca necessária
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
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.ResumoModalidade // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Torneio // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.AdminBottomBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed700 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária

// Ecrã principal de gestão e consulta dos torneios
@Composable
fun TorneiosScreen( // Declaração de função / método de lógica
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
    var pesquisa by remember { mutableStateOf("") } // Declara estado mutável local do Compose
    // Guarda a modalidade atualmente selecionada
    var modalidadeSelecionada by remember(torneios) { mutableStateOf("Todos") } // Declara estado mutável local do Compose
    // Guarda o torneio que o utilizador pretende desativar
    var torneioParaRemover by remember { mutableStateOf<Torneio?>(null) } // Declara estado mutável local do Compose

    // Garante que a modalidade selecionada continua válida após atualizações
    LaunchedEffect(torneios) { // Efeito colateral Compose: executa código assíncrono ao recompor
        if (modalidadeSelecionada != "Todos" && // Estrutura de decisão condicional principal
            torneios.none { it.modalidade == modalidadeSelecionada }
        ) {
            modalidadeSelecionada = "Todos"
        }
    }

    // Adiciona a opção "Todos" às modalidades disponíveis
    val modalidadesFiltro = listOf("Todos") + modalidades.map { it.nome } // Declara constante local (leitura única)

    // Filtra os torneios pela modalidade e pela pesquisa efetuada
    val torneiosFiltrados = torneios.filter { torneio -> // Declara constante local (leitura única)
        // Verifica se o torneio pertence à modalidade selecionada
        val modalidadeOk = // Declara constante local (leitura única)
            modalidadeSelecionada == "Todos" || torneio.modalidade == modalidadeSelecionada

        // Verifica se o texto pesquisado existe no nome ou modalidade
        val pesquisaOk = // Declara constante local (leitura única)
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
                TranslatedText(
                    text = "Desativar torneio?",
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    color = LMInk
                )
            },

            text = {
                TranslatedText(
                    text = "Tens a certeza que queres desativar \"${torneio.nome}\"?",
                    fontFamily = Geist,
                    color = LMGray500
                )
            },

            confirmButton = {
                TextButton(
                    // Desativa o torneio selecionado
                    onClick = { // Callback: Define a ação executada ao clicar no componente
                        onRemoverTorneioClick(torneio.id)
                        torneioParaRemover = null
                    }
                ) {
                    TranslatedText(
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
                    onClick = { // Callback: Define a ação executada ao clicar no componente
                        torneioParaRemover = null
                    }
                ) {
                    TranslatedText(
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
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            // Cabeçalho com campo de pesquisa
            SearchHeader(
                pesquisa = pesquisa,
                onPesquisaChange = { pesquisa = it },
                onSettingsClick = onDefinicoesClick
            )

            Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            // Lista de modalidades para filtrar os torneios
            ModalidadeChips(
                modalidades = modalidadesFiltro,
                selecionada = modalidadeSelecionada,
                onSelecionar = { modalidadeSelecionada = it }
            )

            Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            // Mostra quantos torneios estão visíveis após os filtros
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = "${torneiosFiltrados.size} de $totalTorneios torneios",
                fontFamily = Geist,
                fontSize = 12.sp,
                color = LMGray500,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            // Apresenta os torneios filtrados
            Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Mostra mensagem caso não existam torneios correspondentes aos filtros
                if (torneiosFiltrados.isEmpty()) { // Estrutura de decisão condicional principal
                    EmptyTorneiosMessage()
                } else { // Fluxo condicional alternativo caso o 'if' seja falso
                    // Percorre todos os torneios filtrados para criar os cartões
                    torneiosFiltrados.forEachIndexed { index, torneio ->
                        TorneioCard(
                            torneio = torneio,
                            index = index,
                            onClick = { onTorneioClick(torneio.id) }, // Callback: Define a ação executada ao clicar no componente
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
private fun SearchHeader( // Declaração de função / método de lógica
    pesquisa: String,
    onPesquisaChange: (String) -> Unit,
    onSettingsClick: () -> Unit
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
        // Ícone visual da barra de pesquisa
        Icon( // Componente Compose: Desenha um ícone vetorial
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = LMGray500,
            modifier = Modifier.size(20.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
        )

        // Campo onde o utilizador escreve o texto de pesquisa
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

// Cria os botões de seleção das modalidades
@Composable
private fun ModalidadeChips( // Declaração de função / método de lógica
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
        // Percorre todas as modalidades disponíveis
        modalidades.forEach { modalidade ->
            // Verifica se esta modalidade está selecionada
            val ativo = modalidade == selecionada // Declara constante local (leitura única)

            Surface(
                // Atualiza a modalidade selecionada
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

                    Text( // Componente Compose: Desenha texto estruturado no ecrã
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

// Cartão que apresenta a informação resumida de um torneio
@Composable
private fun TorneioCard( // Declaração de função / método de lógica
    torneio: Torneio,
    index: Int,
    onClick: () -> Unit, // Callback: Define a ação executada ao clicar no componente
    onRemoverClick: () -> Unit
) {
    Surface(
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            // Abre os detalhes do torneio ao clicar no cartão
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
            // Área gráfica com o ícone do troféu
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
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
                // Mostra a modalidade do torneio
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = torneio.modalidade.uppercase(),
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = LMGray500
                )

                Spacer(modifier = Modifier.height(3.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                // Nome do torneio
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = torneio.nome,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = LMInk
                )

                Spacer(modifier = Modifier.height(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                // Mostra o número de equipas e o estado atual do torneio
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "${torneio.equipas} equipas • ${torneio.estado}",
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }

            // Botão para desativar o torneio
            Surface(
                modifier = Modifier.clickable { onRemoverClick() }, // Modificador Compose: Define tamanho, margem, padding ou clique
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFFFFEEF1)
            ) {
                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = LMRed,
                        modifier = Modifier.size(14.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )

                    Spacer(modifier = Modifier.width(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    TranslatedText(
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
private fun EmptyTorneiosMessage() { // Declaração de função / método de lógica
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

// Devolve o ícone correspondente a cada modalidade
private fun iconForModalidade(modalidade: String): ImageVector { // Declaração de função / método de lógica
    return when (modalidade) { // Escolha múltipla condicional (semelhante a switch-case)
        "Futebol" -> Icons.Default.SportsSoccer
        "Basquetebol" -> Icons.Default.SportsBasketball
        "Andebol" -> Icons.Default.SportsHandball
        else -> Icons.Default.SportsTennis // Fluxo condicional alternativo caso o 'if' seja falso
    }
}

// Define as cores do cartão conforme a modalidade do torneio
private fun colorsForTorneio(torneio: Torneio, index: Int): List<Color> { // Declaração de função / método de lógica
    return when { // Escolha múltipla condicional (semelhante a switch-case)
        torneio.modalidade == "Futebol" && index % 3 == 0 -> listOf(LMRed, LMRed700)
        torneio.modalidade == "Basquetebol" -> listOf(Color(0xFF2563EB), Color(0xFF1D4ED8))
        torneio.modalidade == "Andebol" -> listOf(Color(0xFF16A34A), Color(0xFF15803D))
        torneio.modalidade == "Padel" -> listOf(Color(0xFF1F2937), Color(0xFF111827))
        else -> listOf(Color(0xFFBE123C), Color(0xFF9F1239)) // Fluxo condicional alternativo caso o 'if' seja falso
    }
}