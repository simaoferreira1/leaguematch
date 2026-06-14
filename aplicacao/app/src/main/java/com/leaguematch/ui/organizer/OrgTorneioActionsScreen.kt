/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: OrgTorneioActionsScreen.kt
 * Tipo: Interface (Compose View) do Organizador
 *
 * Descrição:
 * Este ficheiro define um ecrã da área do Organizador em Jetpack Compose.\n * Fornece interface e lógica visual para criar torneios, gerir equipas, registar e editar jogos e estatísticas.
 */
package com.leaguematch.ui.organizer // Define o pacote deste ficheiro de código

import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.clickable // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Arrangement // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Box // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Column // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Row // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Spacer // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.fillMaxSize // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.fillMaxWidth // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.height // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.offset // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.padding // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.size // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.width // Importa dependência / biblioteca necessária
import androidx.compose.foundation.rememberScrollState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.AddCircle // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.CalendarMonth // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.ChevronRight // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.DeleteForever // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Edit // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.FormatListNumbered // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Groups // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.QueryStats // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsScore // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.rounded.ArrowBack // Importa dependência / biblioteca necessária
import androidx.compose.material3.AlertDialog // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.IconButton // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.OutlinedTextField // Importa dependência / biblioteca necessária
import androidx.compose.material3.OutlinedTextFieldDefaults // Importa dependência / biblioteca necessária
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
import androidx.compose.ui.graphics.Brush // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.vector.ImageVector // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.DetalheTorneio // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Bricolage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMBorder // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray100 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray400 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray50 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray600 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed50 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária

private data class AcaoCard( // Declaração de classe para modelar objetos
    val titulo: String, // Declara constante local (leitura única)
    val subtitulo: String, // Declara constante local (leitura única)
    val icon: ImageVector, // Declara constante local (leitura única)
    val iconColor: Color, // Declara constante local (leitura única)
    val iconBg: Color, // Declara constante local (leitura única)
    val onClick: () -> Unit // Callback: Define a ação executada ao clicar no componente
)

@Composable
fun OrgTorneioActionsScreen( // Declaração de função / método de lógica
    detalhe: DetalheTorneio?,
    onBackClick: () -> Unit,
    onCriarJogoClick: () -> Unit,
    onVerJogosClick: () -> Unit,
    onVerClassificacaoClick: () -> Unit,
    onEditarTorneio: (nome: String, regras: String, formato: String) -> Unit,
    onRemoverTorneio: (id: Int) -> Unit,
    onGerirEquipasClick: () -> Unit,
    onCalendarioClick: () -> Unit = {},
    onEstatisticasClick: () -> Unit = {},
    bottomBar: @Composable () -> Unit
) {
    val torneio = detalhe?.torneio // Declara constante local (leitura única)
    var mostrarEdicao by remember { mutableStateOf(false) } // Declara estado mutável local do Compose
    var mostrarRemocao by remember { mutableStateOf(false) } // Declara estado mutável local do Compose

    if (mostrarEdicao && torneio != null) { // Estrutura de decisão condicional principal
        var novoNome by remember { mutableStateOf(torneio.nome) } // Declara estado mutável local do Compose
        var novasRegras by remember { mutableStateOf(torneio.regras) } // Declara estado mutável local do Compose
        var novoFormato by remember { mutableStateOf(torneio.formato) } // Declara estado mutável local do Compose

        AlertDialog(
            onDismissRequest = { mostrarEdicao = false },
            title = {
                Text("Editar Torneio", fontFamily = Bricolage, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = LMInk) // Componente Compose: Desenha texto estruturado no ecrã
            },
            text = {
                Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                    modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedTextField( // Campo Compose: Entrada de texto com contorno visual
                        value = novoNome,
                        onValueChange = { novoNome = it },
                        label = { Text("Nome do Torneio", fontFamily = Geist) }, // Componente Compose: Desenha texto estruturado no ecrã
                        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LMInk,
                            unfocusedBorderColor = LMBorder
                        ),
                        singleLine = true
                    )

                    Column { // Contentor Compose: Alinha os filhos numa coluna vertical
                        TranslatedText(
                            "Formato do Torneio",
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = LMGray500,
                            modifier = Modifier.padding(bottom = 6.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                        )
                        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                            modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Liga", "Eliminatória", "Grupos").forEach { f ->
                                val selected = novoFormato.equals(f, ignoreCase = true) // Declara constante local (leitura única)
                                val bg = if (selected) LMInk else LMGray50 // Estrutura de decisão condicional principal
                                val fg = if (selected) LMWhite else LMGray600 // Estrutura de decisão condicional principal
                                val border = if (selected) BorderStroke(0.dp, Color.Transparent) else BorderStroke(1.dp, LMBorder) // Estrutura de decisão condicional principal

                                Surface(
                                    onClick = { novoFormato = f }, // Callback: Define a ação executada ao clicar no componente
                                    shape = RoundedCornerShape(10.dp),
                                    color = bg,
                                    border = border,
                                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                                ) {
                                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                                        modifier = Modifier.padding(vertical = 10.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                                            f,
                                            fontFamily = Geist,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = fg
                                        )
                                    }
                                }
                            }
                        }
                    }


                    OutlinedTextField( // Campo Compose: Entrada de texto com contorno visual
                        value = novasRegras,
                        onValueChange = { novasRegras = it },
                        label = { Text("Regras do Torneio", fontFamily = Geist) }, // Componente Compose: Desenha texto estruturado no ecrã
                        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LMInk,
                            unfocusedBorderColor = LMBorder
                        ),
                        maxLines = 4
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { // Callback: Define a ação executada ao clicar no componente
                        if (novoNome.isNotBlank() && novoFormato.isNotBlank()) { // Estrutura de decisão condicional principal
                            onEditarTorneio(novoNome, novasRegras, novoFormato)
                            mostrarEdicao = false
                        }
                    },
                    enabled = novoNome.isNotBlank() && novoFormato.isNotBlank()
                ) {
                    Text("Guardar", color = LMInk, fontFamily = Geist, fontWeight = FontWeight.Bold) // Componente Compose: Desenha texto estruturado no ecrã
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarEdicao = false }) { // Callback: Define a ação executada ao clicar no componente
                    Text("Cancelar", fontFamily = Geist, color = LMGray500) // Componente Compose: Desenha texto estruturado no ecrã
                }
            },
            shape = RoundedCornerShape(18.dp)
        )
    }

    if (mostrarRemocao && torneio != null) { // Estrutura de decisão condicional principal
        AlertDialog(
            onDismissRequest = { mostrarRemocao = false },
            title = {
                Text("Remover Torneio?", fontFamily = Geist, fontWeight = FontWeight.Bold, fontSize = 16.sp) // Componente Compose: Desenha texto estruturado no ecrã
            },
            text = {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    "Tens a certeza que queres remover definitivamente o torneio \"${torneio.nome}\"? Esta ação irá eliminar todas as equipas e jogos associados e é irreversível.",
                    fontFamily = Geist, fontSize = 13.sp, color = LMGray600
                )
            },
            confirmButton = {
                TextButton(onClick = { // Callback: Define a ação executada ao clicar no componente
                    onRemoverTorneio(torneio.id)
                    mostrarRemocao = false
                }) {
                    TranslatedText("Remover", color = LMRed, fontFamily = Geist, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarRemocao = false }) { // Callback: Define a ação executada ao clicar no componente
                    TranslatedText("Cancelar", fontFamily = Geist, color = LMGray500)
                }
            },
            shape = RoundedCornerShape(18.dp)
        )
    }

    Scaffold(
        bottomBar = bottomBar,
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        if (torneio == null) { // Estrutura de decisão condicional principal
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                TranslatedText("Torneio não encontrado.", color = LMGray500)
            }
            return@Scaffold // Retorna o resultado da execução da função
        }

        val acoes = listOf( // Declara constante local (leitura única)
            AcaoCard(
                titulo = "Criar Jogo",
                subtitulo = "Agendar nova partida no torneio",
                icon = Icons.Default.AddCircle,
                iconColor = Color(0xFF16A34A),
                iconBg = Color(0xFFDCFCE7),
                onClick = onCriarJogoClick // Callback: Define a ação executada ao clicar no componente
            ),
            AcaoCard(
                titulo = "Ver Jogos",
                subtitulo = "Consultar todas as partidas",
                icon = Icons.Default.SportsScore,
                iconColor = Color(0xFF2563EB),
                iconBg = Color(0xFFDBEAFE),
                onClick = onVerJogosClick // Callback: Define a ação executada ao clicar no componente
            ),
            AcaoCard(
                titulo = "Classificação",
                subtitulo = "Tabela classificativa do torneio",
                icon = Icons.Default.FormatListNumbered,
                iconColor = Color(0xFF7C3AED),
                iconBg = Color(0xFFEDE9FE),
                onClick = onVerClassificacaoClick // Callback: Define a ação executada ao clicar no componente
            ),
            AcaoCard(
                titulo = "Gerir Equipas",
                subtitulo = "Adicionar ou remover equipas",
                icon = Icons.Default.Groups,
                iconColor = LMRed,
                iconBg = LMRed50,
                onClick = onGerirEquipasClick // Callback: Define a ação executada ao clicar no componente
            ),
            AcaoCard(
                titulo = "Calendário",
                subtitulo = "Jogos agrupados por data",
                icon = Icons.Default.CalendarMonth,
                iconColor = Color(0xFFEA580C),
                iconBg = Color(0xFFFFEDD5),
                onClick = onCalendarioClick // Callback: Define a ação executada ao clicar no componente
            ),
            AcaoCard(
                titulo = "Estatísticas",
                subtitulo = "Resumo do torneio",
                icon = Icons.Default.QueryStats,
                iconColor = Color(0xFF0891B2),
                iconBg = Color(0xFFCFFAFE),
                onClick = onEstatisticasClick // Callback: Define a ação executada ao clicar no componente
            ),
            AcaoCard(
                titulo = "Editar Torneio",
                subtitulo = "Nome, regras e formato",
                icon = Icons.Default.Edit,
                iconColor = LMInk,
                iconBg = LMGray100,
                onClick = { mostrarEdicao = true } // Callback: Define a ação executada ao clicar no componente
            ),
            AcaoCard(
                titulo = "Remover Torneio",
                subtitulo = "Apagar definitivamente este torneio",
                icon = Icons.Default.DeleteForever,
                iconColor = LMRed,
                iconBg = LMRed50,
                onClick = { mostrarRemocao = true } // Callback: Define a ação executada ao clicar no componente
            ),
        )

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
                    Icon(Icons.Rounded.ArrowBack, contentDescription = null, tint = LMInk) // Componente Compose: Desenha um ícone vetorial
                }
                TranslatedText(
                    text = torneio.nome,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = LMInk
                )
            }

            Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            TorneioHeroCard(torneio = torneio, jogos = detalhe.jogos.size)

            Spacer(modifier = Modifier.height(20.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            TranslatedText(
                text = "AÇÕES DO ORGANIZADOR",
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = LMGray500,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                acoes.forEach { acao ->
                    AcaoCardItem(acao = acao)
                }
            }
        }
    }
}

@Composable
private fun TorneioHeroCard(torneio: com.leaguematch.data.remote.model.Torneio, jogos: Int) { // Declaração de função / método de lógica
    val estadoLower = torneio.estado.lowercase() // Declara constante local (leitura única)
    val (estadoBg, estadoText) = when { // Escolha múltipla condicional (semelhante a switch-case)
        estadoLower.contains("decorrer") || estadoLower.contains("progresso") ->
            Color(0xFFDCFCE7) to Color(0xFF15803D)
        estadoLower.contains("iniciar") ->
            Color(0xFFFEF3C7) to Color(0xFFD97706)
        else -> LMGray100 to LMGray500 // Fluxo condicional alternativo caso o 'if' seja falso
    }

    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(20.dp),
        color = LMInk,
        shadowElevation = 2.dp
    ) {
        Box(modifier = Modifier.fillMaxWidth()) { // Contentor Compose: Sobrepõe os elementos filhos
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(120.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = 30.dp)
                    .background(
                        brush = Brush.radialGradient(listOf(Color.White.copy(alpha = 0.07f), Color.Transparent)),
                        shape = RoundedCornerShape(50)
                    )
            )
            Column(modifier = Modifier.padding(18.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                Surface(
                    shape = RoundedCornerShape(99.dp),
                    color = estadoBg
                ) {
                    TranslatedText(
                        text = torneio.estado,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = estadoText
                    )
                }

                Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                TranslatedText(
                    text = torneio.nome,
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = LMWhite,
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "${torneio.formato} · ${torneio.modalidade}",
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMWhite.copy(alpha = 0.55f)
                )

                Spacer(modifier = Modifier.height(16.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) { // Contentor Compose: Alinha os filhos numa linha horizontal
                    listOf(
                        "Equipas" to torneio.equipas.toString(),
                        "Jogos" to jogos.toString()
                    ).forEach { (label, valor) ->
                        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                .background(LMWhite.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            TranslatedText(
                                text = valor,
                                fontFamily = Bricolage,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                color = LMWhite
                            )
                            TranslatedText(
                                text = label.uppercase(),
                                fontFamily = Geist,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                color = LMWhite.copy(alpha = 0.6f),
                                letterSpacing = 0.4.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AcaoCardItem(acao: AcaoCard) { // Declaração de função / método de lógica
    Surface(
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .clickable { acao.onClick() }, // Callback: Define a ação executada ao clicar no componente
        shape = RoundedCornerShape(16.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, LMBorder),
        shadowElevation = 1.dp
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(44.dp)
                    .background(acao.iconBg, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon( // Componente Compose: Desenha um ícone vetorial
                    imageVector = acao.icon,
                    contentDescription = null,
                    tint = acao.iconColor,
                    modifier = Modifier.size(22.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.width(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                TranslatedText(
                    text = acao.titulo,
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = LMInk
                )
                TranslatedText(
                    text = acao.subtitulo,
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    color = LMGray500,
                    modifier = Modifier.padding(top = 1.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Icon( // Componente Compose: Desenha um ícone vetorial
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = LMGray400,
                modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )
        }
    }
}
