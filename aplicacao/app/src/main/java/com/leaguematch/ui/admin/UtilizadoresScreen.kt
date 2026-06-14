/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: UtilizadoresScreen.kt
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
import androidx.compose.foundation.layout.* // Importa dependência / biblioteca necessária
import androidx.compose.foundation.rememberScrollState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Add // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.MoreVert // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Search // Importa dependência / biblioteca necessária
import androidx.compose.material3.* // Importa dependência / biblioteca necessária
import androidx.compose.runtime.* // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.style.TextOverflow // Importa dependência / biblioteca necessária
import androidx.compose.ui.tooling.preview.Preview // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.* // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.* // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.TipoUtilizador // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Utilizador // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária

// Ecrã que apresenta e permite filtrar os utilizadores
@Composable
fun UtilizadoresScreen( // Declaração de função / método de lógica
    utilizadores: List<Utilizador> = listOf(
        Utilizador(1, "Rúben Miguel Gomes Ferreira", "ruben@leaguematch.com", TipoUtilizador.ORGANIZADOR, true, 1, 3, 9),
        Utilizador(2, "Diogo Gomes", "diogo@leaguematch.com", TipoUtilizador.PARTICIPANTE, true, 2, 4, 12),
        Utilizador(3, "João Fernandes", "joao@leaguematch.com", TipoUtilizador.PARTICIPANTE, false, 1, 2, 7),
        Utilizador(4, "Simão Rodrigues Ferreira", "fsimao530@gmail.com", TipoUtilizador.ORGANIZADOR, false, 2, 4, 12),
        Utilizador(5, "Ricardo Castro", "rcastro@leaguematch.com", TipoUtilizador.ADMIN, true, 0, 0, 0)
    ),
    onUtilizadorClick: (Int) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onTorneiosClick: () -> Unit = {},
    onGraficosClick: () -> Unit = {},
    onDefinicoesClick: () -> Unit = {}
) {
    // Guarda o texto escrito na barra de pesquisa
    var searchQuery by remember { mutableStateOf("") } // Declara estado mutável local do Compose
    // Guarda o filtro de tipo de utilizador selecionado
    var selectedFilter by remember { mutableStateOf("Todos") } // Declara estado mutável local do Compose

    // Conta o número total de utilizadores
    val totalCount = utilizadores.size // Declara constante local (leitura única)
    // Conta quantos utilizadores existem de cada tipo
    val adminCount = utilizadores.count { it.tipo == TipoUtilizador.ADMIN } // Declara constante local (leitura única)
    val organizerCount = utilizadores.count { it.tipo == TipoUtilizador.ORGANIZADOR } // Declara constante local (leitura única)
    val participantCount = utilizadores.count { it.tipo == TipoUtilizador.PARTICIPANTE } // Declara constante local (leitura única)
    val spectatorCount = utilizadores.count { it.tipo == TipoUtilizador.ESPECTADOR } // Declara constante local (leitura única)

    // Filtra os utilizadores pela pesquisa e pelo tipo selecionado
    val filteredUtilizadores = utilizadores.filter { user -> // Declara constante local (leitura única)
        // Verifica se o nome ou email corresponde ao texto pesquisado
        val matchesSearch = user.nome.contains(searchQuery, ignoreCase = true) ||  // Declara constante local (leitura única)
                            user.email.contains(searchQuery, ignoreCase = true)
        // Verifica se o utilizador corresponde ao filtro escolhido
        val matchesFilter = when (selectedFilter) { // Escolha múltipla condicional (semelhante a switch-case)
            "Admin" -> user.tipo == TipoUtilizador.ADMIN
            "Organizador" -> user.tipo == TipoUtilizador.ORGANIZADOR
            "Participante" -> user.tipo == TipoUtilizador.PARTICIPANTE
            "Espectador" -> user.tipo == TipoUtilizador.ESPECTADOR
            else -> true // Fluxo condicional alternativo caso o 'if' seja falso
        }
        matchesSearch && matchesFilter
    }

    // Estrutura principal do ecrã com barra inferior
    Scaffold(
        bottomBar = {
            // Barra de navegação inferior do administrador
            AdminBottomBar(
                selectedItem = "utilizadores",
                onHomeClick = onHomeClick,
                onUtilizadoresClick = {},
                onTorneiosClick = onTorneiosClick,
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
                .padding(vertical = 12.dp)
        ) {
            // Cabeçalho da página com título, subtítulo e botão de adicionar
            TopBar(
                title = "Utilizadores",
                big = true,
                sub = "$totalCount contas registadas",
                rightContent = {
                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .size(36.dp)
                            .background(LMInk, RoundedCornerShape(10.dp))
                            .clickable {},
                        contentAlignment = Alignment.Center
                    ) {
                        // Botão visual para adicionar novo utilizador
                        Icon( // Componente Compose: Desenha um ícone vetorial
                            imageVector = Icons.Default.Add,
                            contentDescription = "Adicionar Utilizador",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                        )
                    }
                }
            )

            // Search Bar
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                // Campo de pesquisa dos utilizadores
                LeagueMatchTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = "Procurar utilizador...",
                    icon = Icons.Default.Search
                )
            }

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Lista de filtros disponíveis com a contagem de utilizadores
                val filters = listOf( // Declara constante local (leitura única)
                    "Todos" to "Todos · $totalCount",
                    "Admin" to "Admin · $adminCount",
                    "Organizador" to "Organizador · $organizerCount",
                    "Participante" to "Participante · $participantCount",
                    "Espectador" to "Espectador · $spectatorCount"
                )

                // Percorre todos os filtros e cria um botão para cada um
                filters.forEach { (filterKey, filterLabel) ->
                    // Verifica se este filtro está selecionado
                    val isSelected = selectedFilter == filterKey // Declara constante local (leitura única)
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
                            // Atualiza o filtro quando o utilizador clica
                            .clickable { selectedFilter = filterKey }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = filterLabel,
                            fontFamily = Geist,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) LMWhite else LMGray700 // Estrutura de decisão condicional principal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            // Lista de utilizadores filtrados
            Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(start = 18.dp, end = 18.dp, bottom = 18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Percorre os utilizadores filtrados e cria um cartão para cada um
                filteredUtilizadores.forEach { user ->
                    // Cartão clicável de cada utilizador
                    CardWrapper(
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .fillMaxWidth()
                            .clickable { onUtilizadorClick(user.id) },
                        pad = 12.dp
                    ) {
                        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar com as iniciais/nome do utilizador
                            Avatar(
                                name = user.nome,
                                size = 38.dp,
                                color = if (user.tipo == TipoUtilizador.ADMIN) LMRed else LMInk // Estrutura de decisão condicional principal
                            )
                            
                            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                            // Informação principal do utilizador
                            Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                    .weight(1f)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    // Nome do utilizador
                                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                                        text = user.nome,
                                        fontFamily = Geist,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = LMInk,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f, fill = false) // Modificador Compose: Define tamanho, margem, padding ou clique
                                    )
                                    // Mostra etiqueta especial caso o utilizador seja administrador
                                    if (user.tipo == TipoUtilizador.ADMIN) { // Estrutura de decisão condicional principal
                                        Box( // Contentor Compose: Sobrepõe os elementos filhos
                                            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                                .background(LMRed, RoundedCornerShape(4.dp))
                                                .padding(horizontal = 5.dp, vertical = 2.dp)
                                        ) {
                                            Text( // Componente Compose: Desenha texto estruturado no ecrã
                                                text = "ADMIN",
                                                fontFamily = Geist,
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 9.sp,
                                                color = LMWhite
                                            )
                                        }
                                    }
                                }
                                
                                Text( // Componente Compose: Desenha texto estruturado no ecrã
                                    // Email do utilizador
                                    text = user.email,
                                    fontFamily = Geist,
                                    fontSize = 11.sp,
                                    color = LMGray500,
                                    modifier = Modifier.padding(top = 1.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                                )
                                
                                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                                    modifier = Modifier.padding(top = 4.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                            .background(LMGray100, RoundedCornerShape(99.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        // Etiqueta com o tipo de utilizador
                                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                                            text = user.tipo.descricao,
                                            fontFamily = Geist,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 10.sp,
                                            color = LMGray700
                                        )
                                    }

                                    // Mostra se o utilizador está ativo ou desativado
                                    Pill(
                                        text = if (user.active) "Ativo" else "Desativo", // Estrutura de decisão condicional principal
                                        kind = if (user.active) "live" else "warn" // Estrutura de decisão condicional principal
                                    )
                                }
                            }

                            // Botão para abrir mais opções ou detalhes do utilizador
                            IconButton( // Componente Compose: Desenha um botão com ícone
                                onClick = { onUtilizadorClick(user.id) }, // Callback: Define a ação executada ao clicar no componente
                                modifier = Modifier.size(30.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                            ) {
                                Icon( // Componente Compose: Desenha um ícone vetorial
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Mais opções",
                                    tint = LMGray500,
                                    modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                                )
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
fun UtilizadoresScreenPreview() { // Declaração de função / método de lógica
    LeagueMatchTheme {
        UtilizadoresScreen()
    }
}
