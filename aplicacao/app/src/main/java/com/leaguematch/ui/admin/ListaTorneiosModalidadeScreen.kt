/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: ListaTorneiosModalidadeScreen.kt
 * Tipo: Interface (Compose View) do Administrador
 *
 * Descrição:
 * Este ficheiro define um ecrã da área do Administrador em Jetpack Compose.\n * Ele desenha componentes visuais reativos baseado no estado fornecido pelo respetivo ViewModel.\n * Permite ao Admin gerir utilizadores (ativar/desativar), visualizar alertas do sistema e gráficos.
 */
package com.leaguematch.ui.admin // Define o pacote deste ficheiro de código

import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.border // Importa dependência / biblioteca necessária
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
import androidx.compose.foundation.shape.CircleShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Delete // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.EmojiEvents // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Search // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.remember // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.draw.clip // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Brush // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.style.TextOverflow // Importa dependência / biblioteca necessária
import androidx.compose.ui.tooling.preview.Preview // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Torneio // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.AdminBottomBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.LeagueMatchTextField // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.Pill // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TopBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Bricolage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMBorder // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray700 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed100 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed50 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed700 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LeagueMatchTheme // Importa dependência / biblioteca necessária

// Ecrã que apresenta a lista de torneios filtrados por modalidade
@Composable
fun ListaTorneiosModalidadeScreen( // Declaração de função / método de lógica
    modalidade: String = "Futebol",
    torneios: List<Torneio> = listOf(
        Torneio(1, "Carabao CUP", "Futebol", "Liga todos contra todos", "LIGA", "Em Progresso", 16),
        Torneio(2, "Barca CUP", "Futebol", "Eliminatorias simples", "ELIMINATORIAS", "Em Progresso", 16),
        Torneio(3, "MinhoFut Cup", "Futebol", "Fase de grupos", "GRUPOS", "Em Progresso", 8),
        Torneio(4, "Liga do Vinho", "Futebol", "Inscricao aberta", "LIGA", "Por Iniciar", 16),
        Torneio(5, "Norte Open", "Ténis", "Inscricao aberta", "LIGA", "Por Iniciar", 12)
    ),
    onBackClick: () -> Unit = {},
    onTorneioClick: (Int) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onUtilizadoresClick: () -> Unit = {},
    onGraficosClick: () -> Unit = {},
    onDefinicoesClick: () -> Unit = {},
    onRemoveTorneioClick: (Int) -> Unit = {}
) {
    // Guarda o texto introduzido na barra de pesquisa
    var searchQuery by remember { mutableStateOf("") } // Declara estado mutável local do Compose
    // Guarda a modalidade atualmente selecionada nos separadores
    var selectedModalidadeTab by remember { mutableStateOf(modalidade) } // Declara estado mutável local do Compose

    // Filtra os torneios com base na pesquisa e na modalidade selecionada
    val filteredTorneios = torneios.filter { t -> // Declara constante local (leitura única)
        // Verifica se o nome do torneio contém o texto pesquisado
        val matchesSearch = t.nome.contains(searchQuery, ignoreCase = true) // Declara constante local (leitura única)
        // Verifica se a modalidade corresponde à aba selecionada
        val matchesTab = t.modalidade.lowercase() == selectedModalidadeTab.lowercase() // Declara constante local (leitura única)
        matchesSearch && matchesTab
    }

    // Estrutura principal do ecrã com barra inferior de navegação
    Scaffold(
        // Barra de navegação do administrador
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
        // Conteúdo principal do ecrã
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 12.dp)
        ) {
            // Cabeçalho da página
            TopBar(
                title = "Torneios",
                big = true,
                back = true,
                onBackClick = onBackClick,
                sub = "Moderação global"
            )

            // Campo de pesquisa de torneios
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                LeagueMatchTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = "Procurar torneio...",
                    icon = Icons.Default.Search
                )
            }

            // SportTabs horizontal scroll
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Lista de modalidades disponíveis para filtrar torneios
                val sportTabs = listOf("Futebol", "Ténis", "Padel", "Basquetebol", "Andebol") // Declara constante local (leitura única)
                
                sportTabs.forEach { sport ->
                    // Verifica se esta modalidade está atualmente selecionada
                    val isSelected = selectedModalidadeTab.lowercase() == sport.lowercase() // Declara constante local (leitura única)
                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .background(
                                if (isSelected) LMInk else LMWhite, // Estrutura de decisão condicional principal
                                shape = RoundedCornerShape(99.dp)
                            )
                            .let {
                                if (isSelected) it else it.border( // Estrutura de decisão condicional principal
                                    BorderStroke(1.dp, LMBorder),
                                    RoundedCornerShape(99.dp)
                                )
                            }
                            // Seleciona a modalidade quando o utilizador clica
                            .clickable { selectedModalidadeTab = sport }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = sport,
                            fontFamily = Geist,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) LMWhite else LMGray700 // Estrutura de decisão condicional principal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            // Secção que apresenta os torneios encontrados
            Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(start = 18.dp, end = 18.dp, bottom = 18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Mensagem apresentada quando não existem torneios para mostrar
                if (filteredTorneios.isEmpty()) { // Estrutura de decisão condicional principal
                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        TranslatedText(
                            text = "Nenhum torneio encontrado",
                            fontFamily = Geist,
                            fontSize = 13.sp,
                            color = LMGray500
                        )
                    }
                }

                // Percorre todos os torneios filtrados
                filteredTorneios.forEach { t ->
                    // Define as cores do cartão consoante o nome do torneio
                    val gradient = when { // Escolha múltipla condicional (semelhante a switch-case)
                        t.nome.contains("Carabao", ignoreCase = true) -> listOf(LMRed, LMRed700)
                        t.nome.contains("Minho", ignoreCase = true) -> listOf(Color(0xFF166534), Color(0xFF22C55E))
                        t.nome.contains("Vinho", ignoreCase = true) -> listOf(Color(0xFF9F1239), Color(0xFFBE123C))
                        t.nome.contains("Norte", ignoreCase = true) -> listOf(Color(0xFF1E3A8A), Color(0xFF2563EB))
                        else -> listOf(LMInk, LMGray700) // Fluxo condicional alternativo caso o 'if' seja falso
                    }

                    // Simulação do número de alertas associados ao torneio
                    val reports = when { // Escolha múltipla condicional (semelhante a switch-case)
                        t.nome.contains("Barca", ignoreCase = true) -> 1
                        t.nome.contains("Norte", ignoreCase = true) -> 2
                        else -> 0 // Fluxo condicional alternativo caso o 'if' seja falso
                    }

                    // Cartão com informação resumida do torneio
                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .fillMaxWidth()
                            .background(LMWhite, shape = RoundedCornerShape(16.dp))
                            .border(
                                BorderStroke(
                                    1.dp,
                                    if (reports > 0) LMRed100 else LMBorder // Estrutura de decisão condicional principal
                                ),
                                RoundedCornerShape(16.dp)
                            )
                            // Abre os detalhes do torneio ao clicar
                            .clickable { onTorneioClick(t.id) }
                            .padding(12.dp)
                    ) {
                        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Área gráfica que identifica visualmente o torneio
                            Box( // Contentor Compose: Sobrepõe os elementos filhos
                                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(brush = Brush.linearGradient(colors = gradient)),
                                contentAlignment = Alignment.Center
                            ) {
                                // Ícone representativo de torneios
                                Icon( // Componente Compose: Desenha um ícone vetorial
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = "Trophy Icon", // Componente Compose: Desenha um ícone vetorial
                                    tint = LMWhite,
                                    modifier = Modifier.size(26.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                            
                            Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                                modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                            ) {
                                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    // Mostra a modalidade do torneio
                                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                                        text = t.modalidade.uppercase(),
                                        fontFamily = Geist,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        color = LMGray500,
                                        letterSpacing = 0.4.sp
                                    )

                                    // Mostra aviso caso existam alertas associados ao torneio
                                    if (reports > 0) { // Estrutura de decisão condicional principal
                                        Pill(
                                            text = "$reports alerta${if (reports > 1) "s" else ""}", // Estrutura de decisão condicional principal
                                            kind = "red"
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(2.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                                // Nome do torneio
                                Text( // Componente Compose: Desenha texto estruturado no ecrã
                                    text = t.nome,
                                    fontFamily = Bricolage,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = LMInk,
                                    letterSpacing = (-0.2).sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                
                                Spacer(modifier = Modifier.height(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                                
                                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                                    modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Número de equipas inscritas no torneio
                                    TranslatedText(
                                        text = "${t.equipas} equipas",
                                        fontFamily = Geist,
                                        fontSize = 11.sp,
                                        color = LMGray500
                                    )

                                    // Botão para remover o torneio
                                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                            .background(LMRed50, CircleShape)
                                            // Executa a remoção do torneio selecionado
                                            .clickable { onRemoveTorneioClick(t.id) }
                                            .padding(horizontal = 10.dp, vertical = 5.dp)
                                    ) {
                                        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon( // Componente Compose: Desenha um ícone vetorial
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Trash Icon", // Componente Compose: Desenha um ícone vetorial
                                                tint = LMRed,
                                                modifier = Modifier.size(11.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                                            )
                                            TranslatedText(
                                                text = "Remover",
                                                fontFamily = Geist,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp,
                                                color = LMRed700
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaTorneiosModalidadeScreenPreview() { // Declaração de função / método de lógica
    LeagueMatchTheme {
        ListaTorneiosModalidadeScreen()
    }
}
