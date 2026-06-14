/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: ParticipantJoinTeamScreen.kt
 * Tipo: Interface (Compose View) do Participante
 *
 * Descrição:
 * Este ficheiro define um ecrã do fluxo do Jogador/Participante em Jetpack Compose.\n * Mostra ao participante o estado do seu torneio, código de equipas para inscrição, estatísticas e notificações.
 */
package com.leaguematch.ui.participant // Define o pacote deste ficheiro de código

import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.border // Importa dependência / biblioteca necessária
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
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.text.BasicTextField // Importa dependência / biblioteca necessária
import androidx.compose.foundation.text.KeyboardOptions // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.ArrowBack // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.QrCode2 // Importa dependência / biblioteca necessária
import androidx.compose.material3.Button // Importa dependência / biblioteca necessária
import androidx.compose.material3.ButtonDefaults // Importa dependência / biblioteca necessária
import androidx.compose.material3.CircularProgressIndicator // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.IconButton // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Surface // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.material3.TextButton // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.LaunchedEffect // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.remember // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.draw.clip // Importa dependência / biblioteca necessária
import androidx.compose.ui.focus.FocusRequester // Importa dependência / biblioteca necessária
import androidx.compose.ui.focus.focusRequester // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.platform.LocalSoftwareKeyboardController // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.TextStyle // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.input.ImeAction // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.input.KeyboardCapitalization // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.input.KeyboardType // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.style.TextAlign // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.TeamCode // Importa dependência / biblioteca necessária
import com.leaguematch.translations.AppStrings // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Bricolage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária

@Composable
fun ParticipantJoinTeamScreen( // Declaração de função / método de lógica
    isLoading: Boolean,
    erro: String?,
    strings: AppStrings,
    primaryColor: Color,
    onBackClick: () -> Unit,
    onConfirmClick: (codigo: String) -> Unit
) {
    var codigo by remember { mutableStateOf("") } // Declara estado mutável local do Compose
    val focusRequester = remember { FocusRequester() } // Memoriza estado para evitar perda durante a recomposição
    val keyboard = LocalSoftwareKeyboardController.current // Declara constante local (leitura única)

    LaunchedEffect(Unit) { // Efeito colateral Compose: executa código assíncrono ao recompor
        focusRequester.requestFocus()
        keyboard?.show()
    }

    Scaffold(
        containerColor = Color(0xFFF6F6F8)
    ) { innerPadding ->
        Box( // Contentor Compose: Sobrepõe os elementos filhos
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 18.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) { // Contentor Compose: Alinha os filhos numa linha horizontal
                    IconButton(onClick = onBackClick) { // Componente Compose: Desenha um botão com ícone
                        Icon( // Componente Compose: Desenha um ícone vetorial
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = LMInk
                        )
                    }
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = strings.teamsTitle,
                        fontFamily = Bricolage,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 26.sp,
                        color = LMInk
                    )
                }
            }

            Surface(
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
                color = LMWhite,
                shadowElevation = 12.dp
            ) {
                Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                    modifier = Modifier.padding(horizontal = 22.dp, vertical = 22.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .size(width = 40.dp, height = 4.dp)
                            .clip(RoundedCornerShape(99.dp))
                            .background(Color(0xFFD4D4DA))
                    )

                    Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(primaryColor.copy(alpha = 0.10f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon( // Componente Compose: Desenha um ícone vetorial
                            imageVector = Icons.Default.QrCode2,
                            contentDescription = null,
                            tint = primaryColor,
                            modifier = Modifier.size(30.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = strings.joinTeamTitle,
                        fontFamily = Bricolage,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = LMInk
                    )

                    Spacer(modifier = Modifier.height(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = strings.joinTeamDescription,
                        fontFamily = Geist,
                        fontSize = 12.sp,
                        color = LMGray500,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    CodeBoxes(
                        value = codigo,
                        length = TeamCode.LENGTH,
                        primaryColor = primaryColor,
                        focusRequester = focusRequester,
                        onChange = { novo ->
                            codigo = novo.uppercase().filter {
                                it.isLetterOrDigit()
                            }.take(TeamCode.LENGTH)
                        }
                    )

                    if (erro != null) { // Estrutura de decisão condicional principal
                        Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = erro,
                            fontFamily = Geist,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    Button( // Componente Compose: Desenha um botão interativo
                        onClick = { onConfirmClick(codigo) }, // Callback: Define a ação executada ao clicar no componente
                        enabled = !isLoading && codigo.length == TeamCode.LENGTH,
                        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                    ) {
                        if (isLoading) { // Estrutura de decisão condicional principal
                            CircularProgressIndicator(
                                color = LMWhite,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                            )
                        } else { // Fluxo condicional alternativo caso o 'if' seja falso
                            Text( // Componente Compose: Desenha texto estruturado no ecrã
                                text = strings.confirmAndJoin,
                                fontFamily = Geist,
                                fontWeight = FontWeight.ExtraBold,
                                color = LMWhite
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    TextButton(
                        onClick = onBackClick, // Callback: Define a ação executada ao clicar no componente
                        modifier = Modifier.fillMaxWidth() // Modificador Compose: Define tamanho, margem, padding ou clique
                    ) {
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = strings.cancel,
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            color = LMGray500
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CodeBoxes( // Declaração de função / método de lógica
    value: String,
    length: Int,
    primaryColor: Color,
    focusRequester: FocusRequester,
    onChange: (String) -> Unit
) {
    Box { // Contentor Compose: Sobrepõe os elementos filhos
        BasicTextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .focusRequester(focusRequester)
                .size(1.dp),
            textStyle = TextStyle(color = Color.Transparent),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, // Componente Compose: Desenha texto estruturado no ecrã
                capitalization = KeyboardCapitalization.Characters,
                imeAction = ImeAction.Done
            )
        )

        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(length) { index ->
                val char = value.getOrNull(index)?.toString().orEmpty() // Declara constante local (leitura única)
                val filled = char.isNotEmpty() // Declara constante local (leitura única)

                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .size(width = 38.dp, height = 48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (filled) primaryColor.copy(alpha = 0.10f) else LMWhite) // Estrutura de decisão condicional principal
                        .border(
                            border = BorderStroke(
                                width = 1.5.dp,
                                color = if (filled) primaryColor else LMInk // Estrutura de decisão condicional principal
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = char,
                        fontFamily = Bricolage,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = if (filled) primaryColor else LMInk // Estrutura de decisão condicional principal
                    )
                }
            }
        }
    }
}