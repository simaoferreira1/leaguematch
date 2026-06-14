/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: OrgCriarEquipaScreen.kt
 * Tipo: Interface (Compose View) do Organizador
 *
 * Descrição:
 * Este ficheiro define um ecrã da área do Organizador em Jetpack Compose.\n * Fornece interface e lógica visual para criar torneios, gerir equipas, registar e editar jogos e estatísticas.
 */
package com.leaguematch.ui.organizer // Define o pacote deste ficheiro de código

import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
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
import androidx.compose.material.icons.filled.EmojiEvents // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Groups // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.rounded.ArrowBack // Importa dependência / biblioteca necessária
import androidx.compose.material3.Button // Importa dependência / biblioteca necessária
import androidx.compose.material3.ButtonDefaults // Importa dependência / biblioteca necessária
import androidx.compose.material3.CircularProgressIndicator // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.IconButton // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.OutlinedButton // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Surface // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.material3.TextField // Importa dependência / biblioteca necessária
import androidx.compose.material3.TextFieldDefaults // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.remember // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.TextStyle // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Torneio // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Bricolage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMBorder // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray100 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray300 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray400 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray600 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária

@Composable
fun OrgCriarEquipaScreen( // Declaração de função / método de lógica
    torneio: Torneio,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onCriarClick: (nome: String) -> Unit
) {
    var nome by remember { mutableStateOf("") } // Declara estado mutável local do Compose

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
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
                Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    TranslatedText(
                        text = "Criar Equipa",
                        fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp, color = LMInk
                    )
                    Text(text = torneio.nome, fontFamily = Geist, fontSize = 12.sp, color = LMGray500) // Componente Compose: Desenha texto estruturado no ecrã
                }
            }

            Spacer(modifier = Modifier.height(20.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            // Preview do escudo
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .size(80.dp)
                            .background(
                                color = if (nome.isBlank()) LMGray100 else LMInk, // Estrutura de decisão condicional principal
                                shape = RoundedCornerShape(20.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (nome.isBlank()) { // Estrutura de decisão condicional principal
                            Icon( // Componente Compose: Desenha um ícone vetorial
                                Icons.Default.Groups,
                                contentDescription = null,
                                tint = LMGray300,
                                modifier = Modifier.size(38.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                            )
                        } else { // Fluxo condicional alternativo caso o 'if' seja falso
                            Text( // Componente Compose: Desenha texto estruturado no ecrã
                                text = nome.trim().split(" ")
                                    .take(2)
                                    .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                                    .joinToString(""),
                                fontFamily = Bricolage,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp,
                                color = LMWhite
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = if (nome.isBlank()) "Pré-visualização" else nome, // Estrutura de decisão condicional principal
                        fontFamily = Geist,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = if (nome.isBlank()) LMGray400 else LMInk // Estrutura de decisão condicional principal
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            TranslatedText(
                text = "NOME DA EQUIPA",
                fontFamily = Geist, fontWeight = FontWeight.Bold,
                fontSize = 11.sp, color = LMGray500, letterSpacing = 0.4.sp,
                modifier = Modifier.padding(bottom = 6.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )

            Surface(
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                shape = RoundedCornerShape(12.dp),
                color = LMWhite,
                border = BorderStroke(
                    width = if (nome.isNotBlank()) 1.5.dp else 1.dp, // Estrutura de decisão condicional principal
                    color = if (nome.isNotBlank()) LMInk else LMBorder // Estrutura de decisão condicional principal
                )
            ) {
                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 2.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = if (nome.isNotBlank()) LMInk else LMGray400, // Estrutura de decisão condicional principal
                        modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                    Spacer(modifier = Modifier.width(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                    TextField( // Campo Compose: Entrada de texto simples para utilizador
                        value = nome,
                        onValueChange = { nome = it },
                        placeholder = {
                            TranslatedText("Ex: Rio Torto FC", fontFamily = Geist, fontSize = 14.sp, color = LMGray400)
                        },
                        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(
                            fontFamily = Geist,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = LMInk
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            TranslatedText(
                text = "A equipa será adicionada ao torneio e ficará disponível para agendamento de jogos.",
                fontFamily = Geist,
                fontSize = 12.sp,
                color = LMGray500,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(28.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Button( // Componente Compose: Desenha um botão interativo
                onClick = { if (nome.isNotBlank()) onCriarClick(nome.trim()) }, // Callback: Define a ação executada ao clicar no componente
                enabled = nome.isNotBlank() && !isLoading,
                modifier = Modifier.fillMaxWidth().height(50.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LMRed,
                    disabledContainerColor = LMGray300
                )
            ) {
                if (isLoading) { // Estrutura de decisão condicional principal
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = LMWhite) // Modificador Compose: Define tamanho, margem, padding ou clique
                } else { // Fluxo condicional alternativo caso o 'if' seja falso
                    TranslatedText("Criar equipa", fontFamily = Geist, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = LMWhite)
                }
            }

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            OutlinedButton(
                onClick = onBackClick, // Callback: Define a ação executada ao clicar no componente
                modifier = Modifier.fillMaxWidth().height(50.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, LMBorder)
            ) {
                Text("Cancelar", fontFamily = Geist, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = LMGray600) // Componente Compose: Desenha texto estruturado no ecrã
            }
        }
    }
}
