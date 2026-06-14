/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: DetalhesUtilizadorScreen.kt
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
import androidx.compose.foundation.layout.* // Importa dependência / biblioteca necessária
import androidx.compose.foundation.rememberScrollState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material3.* // Importa dependência / biblioteca necessária
import androidx.compose.runtime.* // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.style.TextAlign // Importa dependência / biblioteca necessária
import androidx.compose.ui.tooling.preview.Preview // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.* // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.* // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária

// Ecrã que permite ao administrador visualizar e editar informações de um utilizador
@Composable
fun DetalheUtilizadorScreen( // Declaração de função / método de lógica
    nome: String = "Simão Rodrigues Ferreira",
    email: String = "fsimao530@gmail.com",
    tipo: String = "Organizador",
    ativo: Boolean = true,
    equipas: Int = 2,
    torneios: Int = 1,
    jogos: Int = 18,
    golos: Int = 12,
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onUtilizadoresClick: () -> Unit = {},
    onTorneiosClick: () -> Unit = {},
    onGraficosClick: () -> Unit = {},
    onDefinicoesClick: () -> Unit = {},
    onAlterarEstadoClick: () -> Unit = {},
    onGuardarClick: (String) -> Unit = {},
    onResetPasswordClick: (String) -> Unit = {}
) {
    // Guarda o tipo de utilizador atualmente selecionado
    var tipoSelecionado by remember(tipo) { mutableStateOf(tipo) } // Declara estado mutável local do Compose
    // Controla a visibilidade da janela de confirmação
    var showConfirmEstadoDialog by remember { mutableStateOf(false) } // Declara estado mutável local do Compose
    // Repor password
    var showResetPasswordDialog by remember { mutableStateOf(false) } // Declara estado mutável local do Compose
    var novaPassword by remember { mutableStateOf("") } // Declara estado mutável local do Compose

    if (showResetPasswordDialog) { // Estrutura de decisão condicional principal
        AlertDialog(
            onDismissRequest = {
                showResetPasswordDialog = false
                novaPassword = ""
            },
            containerColor = LMWhite,
            shape = RoundedCornerShape(20.dp),
            title = {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "Repor palavra-passe",
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    color = LMInk
                )
            },
            text = {
                Column { // Contentor Compose: Alinha os filhos numa coluna vertical
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "Vai definir uma nova palavra-passe para $nome. Comunica-a ao utilizador de forma segura.",
                        fontFamily = Geist,
                        color = LMGray600,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                    LeagueMatchTextField(
                        value = novaPassword,
                        onValueChange = { novaPassword = it },
                        label = "Nova palavra-passe",
                        placeholder = "••••••••",
                        isPassword = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { // Callback: Define a ação executada ao clicar no componente
                        if (novaPassword.length >= 4) { // Estrutura de decisão condicional principal
                            onResetPasswordClick(novaPassword)
                            showResetPasswordDialog = false
                            novaPassword = ""
                        }
                    }
                ) {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "Repor",
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        color = LMRed
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { // Callback: Define a ação executada ao clicar no componente
                        showResetPasswordDialog = false
                        novaPassword = ""
                    }
                ) {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "Cancelar",
                        fontFamily = Geist,
                        fontWeight = FontWeight.SemiBold,
                        color = LMGray600
                    )
                }
            }
        )
    }
    // Define os textos apresentados conforme o estado atual do utilizador
    val textoAcao = if (ativo) "Desativar" else "Ativar" // Estrutura de decisão condicional principal
    val tituloDialog = if (ativo) "Desativar utilizador?" else "Ativar utilizador?" // Estrutura de decisão condicional principal
    val mensagemDialog = if (ativo) { // Estrutura de decisão condicional principal
        "Tens a certeza que queres desativar \"$nome\"?"
    } else { // Fluxo condicional alternativo caso o 'if' seja falso
        "Tens a certeza que queres ativar \"$nome\"?"
    }
    // Janela de confirmação antes de alterar o estado do utilizador
    if (showConfirmEstadoDialog) { // Estrutura de decisão condicional principal
        AlertDialog(
            onDismissRequest = { showConfirmEstadoDialog = false },
            containerColor = LMWhite,
            shape = RoundedCornerShape(20.dp),
            title = {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = tituloDialog,
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    color = LMInk
                )
            },
            text = {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = mensagemDialog,
                    fontFamily = Geist,
                    color = LMGray600
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { // Callback: Define a ação executada ao clicar no componente
                        showConfirmEstadoDialog = false
                        onAlterarEstadoClick()
                    }
                ) {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = textoAcao,
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        color = LMRed
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmEstadoDialog = false } // Callback: Define a ação executada ao clicar no componente
                ) {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "Cancelar",
                        fontFamily = Geist,
                        fontWeight = FontWeight.SemiBold,
                        color = LMGray600
                    )
                }
            }
        )
    }

    // Estrutura principal da página com barra de navegação inferior
    Scaffold(
        bottomBar = {
            AdminBottomBar(
                selectedItem = "utilizadores",
                onHomeClick = onHomeClick,
                onUtilizadoresClick = onUtilizadoresClick,
                onTorneiosClick = onTorneiosClick,
                onGraficosClick = onGraficosClick,
                onDefinicoesClick = onDefinicoesClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 12.dp)
        ) {
            // Barra superior com botão voltar e ação de ativar/desativar utilizador
            TopBar(
                title = "Utilizador",
                back = true,
                onBackClick = onBackClick,
                rightContent = {
                    TextBtn(
                        onClick = { showConfirmEstadoDialog = true }, // Callback: Define a ação executada ao clicar no componente
                        color = LMRed
                    ) {
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = textoAcao,
                            fontFamily = Geist,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                }
            )

            // Área de apresentação das informações principais do utilizador
            Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar gerado automaticamente a partir do nome do utilizador
                Avatar(
                    name = nome,
                    size = 86.dp,
                    color = LMRed
                )

                Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = nome,
                    fontFamily = Bricolage,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LMInk,
                    letterSpacing = (-0.4).sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(2.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = email,
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                // Indicador visual do estado da conta (ativo ou desativado)
                Pill(
                    text = if (ativo) "Ativo" else "Desativado", // Estrutura de decisão condicional principal
                    kind = if (ativo) "live" else "warn" // Estrutura de decisão condicional principal
                )

                Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "Membro desde 12 Mar 2025",
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    color = LMGray400,
                    textAlign = TextAlign.Center
                )
            }

            // Estatísticas resumidas da atividade do utilizador
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Lista de métricas apresentadas no painel de estatísticas
                val stats = listOf( // Declara constante local (leitura única)
                    "Equipas" to equipas,
                    "Torneios" to torneios,
                    "Jogos" to jogos,
                    "Golos" to golos
                )

                stats.forEach { (label, value) ->
                    Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .weight(1f)
                            .background(LMGray100, RoundedCornerShape(12.dp))
                            .padding(vertical = 10.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = value.toString(),
                            fontFamily = GeistMono,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LMInk
                        )

                        Spacer(modifier = Modifier.height(2.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = label.uppercase(),
                            fontFamily = Geist,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = LMGray500,
                            letterSpacing = 0.4.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 4.dp)
            ) {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "TIPO DE UTILIZADOR",
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = LMGray500,
                    letterSpacing = 0.4.sp,
                    modifier = Modifier.padding(bottom = 8.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                    modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Permite alterar o tipo de utilizador através de seleção visual
                    val options = listOf("Organizador", "Participante", "Espectador") // Declara constante local (leitura única)

                    options.forEach { option ->
                        val isActive = tipoSelecionado.lowercase() == option.lowercase() // Declara constante local (leitura única)

                        Box( // Contentor Compose: Sobrepõe os elementos filhos
                            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                .weight(1f)
                                .background(
                                    color = if (isActive) LMRed50 else LMWhite, // Estrutura de decisão condicional principal
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .border(
                                    BorderStroke(
                                        if (isActive) 1.5.dp else 1.dp, // Estrutura de decisão condicional principal
                                        if (isActive) LMRed else LMBorder // Estrutura de decisão condicional principal
                                    ),
                                    RoundedCornerShape(10.dp)
                                )
                                // Atualiza o tipo de utilizador selecionado
                                .clickable { tipoSelecionado = option }
                                .padding(vertical = 9.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text( // Componente Compose: Desenha texto estruturado no ecrã
                                text = option,
                                fontFamily = Geist,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isActive) LMRed700 else LMGray700 // Estrutura de decisão condicional principal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
            ) {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "CONTACTOS DE EQUIPA",
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = LMGray500,
                    letterSpacing = 0.4.sp,
                    modifier = Modifier.padding(bottom = 8.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                CardWrapper(
                    modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                    pad = 0.dp
                ) {
                    // Lista de equipas às quais o utilizador pertence
                    val teams = listOf( // Declara constante local (leitura única)
                        "GD Rio Torto" to "Capitão",
                        "Bola Parada FC" to "Membro"
                    )

                    Column { // Contentor Compose: Alinha os filhos numa coluna vertical
                        // Apresentação de cada equipa e respetiva função do utilizador
                        teams.forEachIndexed { index, (teamName, role) ->
                            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TeamCrest(name = teamName, size = 26.dp)

                                Spacer(modifier = Modifier.width(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                                Text( // Componente Compose: Desenha texto estruturado no ecrã
                                    text = teamName,
                                    fontFamily = Geist,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = LMInk,
                                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                                )

                                Text( // Componente Compose: Desenha texto estruturado no ecrã
                                    text = role,
                                    fontFamily = Geist,
                                    fontSize = 11.sp,
                                    color = LMGray500
                                )
                            }

                            if (index < teams.size - 1) { // Estrutura de decisão condicional principal
                                HorizontalDivider(
                                    modifier = Modifier.padding(start = 50.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                                    color = LMBorder,
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }
            }

            Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 18.dp)
            ) {
                // Guarda as alterações efetuadas pelo administrador
                PrimaryBtn(
                    onClick = { onGuardarClick(tipoSelecionado) }, // Callback: Define a ação executada ao clicar no componente
                    size = "lg"
                ) {
                    Text("Guardar alterações", color = LMWhite) // Componente Compose: Desenha texto estruturado no ecrã
                }

                Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                OutlinedButton(
                    onClick = { showResetPasswordDialog = true }, // Callback: Define a ação executada ao clicar no componente
                    modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, LMBorder)
                ) {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "Repor palavra-passe",
                        fontFamily = Geist,
                        fontWeight = FontWeight.SemiBold,
                        color = LMInk
                    )
                }

                Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetalheUtilizadorScreenPreview() { // Declaração de função / método de lógica
    LeagueMatchTheme {
        DetalheUtilizadorScreen()
    }
}