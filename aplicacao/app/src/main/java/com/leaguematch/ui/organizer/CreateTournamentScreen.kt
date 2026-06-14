/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: CreateTournamentScreen.kt
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
import androidx.compose.foundation.layout.heightIn // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.padding // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.size // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.width // Importa dependência / biblioteca necessária
import androidx.compose.foundation.rememberScrollState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.CalendarMonth // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.EmojiEvents // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.KeyboardArrowDown // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsSoccer // Importa dependência / biblioteca necessária
import androidx.compose.material3.DatePicker // Importa dependência / biblioteca necessária
import androidx.compose.material3.DatePickerDialog // Importa dependência / biblioteca necessária
import androidx.compose.material3.DropdownMenu // Importa dependência / biblioteca necessária
import androidx.compose.material3.DropdownMenuItem // Importa dependência / biblioteca necessária
import androidx.compose.material3.ExperimentalMaterial3Api // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.LocalTextStyle // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Surface // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.material3.TextButton // Importa dependência / biblioteca necessária
import androidx.compose.material3.TextField // Importa dependência / biblioteca necessária
import androidx.compose.material3.TextFieldDefaults // Importa dependência / biblioteca necessária
import androidx.compose.material3.rememberDatePickerState // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.remember // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Brush // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.Dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Bricolage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária

@Composable
fun CreateTournamentScreen( // Declaração de função / método de lógica
    onBackClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    onContinueClick: (
        nome: String,
        modalidade: String,
        formato: String,
        dataInicio: String,
        dataFim: String,
        maxEquipas: String,
        descricao: String,
        regras: String
    ) -> Unit = { _, _, _, _, _, _, _, _ -> }
) {
    var nome by remember { mutableStateOf("") } // Declara estado mutável local do Compose
    var modalidade by remember { mutableStateOf("Futebol") } // Declara estado mutável local do Compose
    var formato by remember { mutableStateOf("Liga") } // Declara estado mutável local do Compose
    var dataInicio by remember { mutableStateOf("") } // Declara estado mutável local do Compose
    var dataFim by remember { mutableStateOf("") } // Declara estado mutável local do Compose
    var maxEquipas by remember { mutableStateOf("") } // Declara estado mutável local do Compose
    var descricao by remember { mutableStateOf("") } // Declara estado mutável local do Compose
    var regras by remember { mutableStateOf("") } // Declara estado mutável local do Compose

    val modalidades = listOf( // Declara constante local (leitura única)
        "Futebol",
        "Basquetebol",
        "Andebol",
        "Ténis",
        "Padel"
    )

    val formatos = listOf( // Declara constante local (leitura única)
        "Liga",
        "Eliminatória",
        "Grupos"
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "‹",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = LMInk,
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .clickable { onBackClick() }
                        .padding(end = 12.dp)
                )

                TranslatedText(
                    text = "Criar Torneio",
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = LMInk,
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                TranslatedText(
                    text = "Cancelar",
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = LMGray500,
                    modifier = Modifier.clickable { onCancelClick() } // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Surface(
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFFFFF5F6),
                border = BorderStroke(1.dp, Color(0xFFFFCCD3))
            ) {
                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                    modifier = Modifier.padding(14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box( // Contentor Compose: Sobrepõe os elementos filhos
                        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                            .size(42.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    listOf(LMRed, Color(0xFFC41326))
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon( // Componente Compose: Desenha um ícone vetorial
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = LMWhite,
                            modifier = Modifier.size(22.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    Column { // Contentor Compose: Alinha os filhos numa coluna vertical
                        TranslatedText(
                            text = "Novo torneio",
                            fontFamily = Geist,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            color = LMInk
                        )

                        TranslatedText(
                            text = "Passo 1 de 2 — Informação geral",
                            fontFamily = Geist,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = LMGray500
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            FormLabel("NOME DO TORNEIO")
            FormTextField(
                value = nome,
                onValueChange = { nome = it },
                placeholder = "Ex: Liga do Vinho 2026"
            )

            Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    FormLabel("MODALIDADE")

                    SelectBox(
                        text = modalidade,
                        options = modalidades,
                        leadingIcon = {
                            Icon( // Componente Compose: Desenha um ícone vetorial
                                imageVector = Icons.Default.SportsSoccer,
                                contentDescription = null,
                                tint = LMInk,
                                modifier = Modifier.size(17.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                            )
                        },
                        onSelected = { modalidade = it }
                    )
                }

                Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    FormLabel("FORMATO")

                    SelectBox(
                        text = formato,
                        options = formatos,
                        onSelected = { formato = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    FormLabel("DATA INÍCIO")
                    DateField(
                        value = dataInicio,
                        onValueChange = { dataInicio = it },
                        placeholder = "dd/mm/aaaa"
                    )
                }

                Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    FormLabel("DATA FIM")
                    DateField(
                        value = dataFim,
                        onValueChange = { dataFim = it },
                        placeholder = "dd/mm/aaaa"
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            FormLabel("MÁXIMO DE EQUIPAS")
            FormTextField(
                value = maxEquipas,
                onValueChange = { maxEquipas = it },
                placeholder = "Ex: 16"
            )

            Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            FormLabel("DESCRIÇÃO")
            FormTextField(
                value = descricao,
                onValueChange = { descricao = it },
                placeholder = "Adiciona contexto, prémios e detalhes...",
                minHeight = 76.dp
            )

            Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            FormLabel("REGRAS")
            FormTextField(
                value = regras,
                onValueChange = { regras = it },
                placeholder = "Define regras específicas do torneio...",
                minHeight = 76.dp
            )

            Spacer(modifier = Modifier.height(22.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .weight(1f)
                        .height(54.dp)
                        .clickable { onBackClick() },
                    shape = RoundedCornerShape(16.dp),
                    color = LMWhite,
                    border = BorderStroke(1.dp, Color(0xFFE2E2E7))
                ) {
                    Box(contentAlignment = Alignment.Center) { // Contentor Compose: Sobrepõe os elementos filhos
                        TranslatedText(
                            text = "Anterior",
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = LMInk
                        )
                    }
                }

                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .weight(1f)
                        .height(54.dp)
                        .background(
                            brush = Brush.linearGradient(
                                listOf(LMRed, Color(0xFFC41326))
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            onContinueClick(
                                nome,
                                modalidade,
                                formato,
                                dataInicio,
                                dataFim,
                                maxEquipas,
                                descricao,
                                regras
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    TranslatedText(
                        text = "Continuar",
                        fontFamily = Geist,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = LMWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
        }
    }
}

@Composable
private fun FormLabel(text: String) { // Declaração de função / método de lógica
    TranslatedText(
        text = text,
        fontFamily = Geist,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 12.sp,
        color = LMGray500
    )

    Spacer(modifier = Modifier.height(7.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
}

@Composable
private fun FormTextField( // Declaração de função / método de lógica
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    minHeight: Dp = 52.dp
) {
    TextField( // Campo Compose: Entrada de texto simples para utilizador
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            TranslatedText(
                text = placeholder,
                fontFamily = Geist,
                fontSize = 14.sp,
                color = Color(0xFFA3A3AE)
            )
        },
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .heightIn(min = minHeight),
        shape = RoundedCornerShape(16.dp),
        textStyle = LocalTextStyle.current.copy(
            fontFamily = Geist,
            fontSize = 14.sp,
            color = LMInk,
            fontWeight = FontWeight.Medium
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = LMWhite,
            unfocusedContainerColor = LMWhite,
            disabledContainerColor = LMWhite,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class) // Declaração de classe para modelar objetos
@Composable
private fun DateField( // Declaração de função / método de lógica
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    var showDialog by remember { mutableStateOf(false) } // Declara estado mutável local do Compose

    if (showDialog) { // Estrutura de decisão condicional principal
        val initialMillis = parseDateToMillis(value) // Declara constante local (leitura única)
        val state = rememberDatePickerState(initialSelectedDateMillis = initialMillis) // Declara constante local (leitura única)

        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            shape = RoundedCornerShape(24.dp),
            properties = androidx.compose.ui.window.DialogProperties(
                usePlatformDefaultWidth = false
            ),
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            confirmButton = {
                TextButton(onClick = { // Callback: Define a ação executada ao clicar no componente
                    state.selectedDateMillis?.let {
                        onValueChange(formatMillisToDate(it))
                    }
                    showDialog = false
                }) {
                    Text("OK", color = LMRed, fontFamily = Geist, fontWeight = FontWeight.Bold) // Componente Compose: Desenha texto estruturado no ecrã
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { // Callback: Define a ação executada ao clicar no componente
                    TranslatedText(
                        text = "Cancelar",
                        color = LMGray500,
                        fontFamily = Geist
                    )
                }
            }
        ) {
            DatePicker(
                state = state,
                title = {
                    TranslatedText(
                        text = "Escolher data",
                        modifier = Modifier.padding(start = 24.dp, top = 16.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = LMGray500
                    )
                },
                headline = {
                    val texto = state.selectedDateMillis?.let { formatMillisToDate(it) } ?: "—/—/——" // Declara constante local (leitura única)
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = texto,
                        modifier = Modifier.padding(start = 24.dp, bottom = 12.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                        fontFamily = Bricolage,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 26.sp,
                        color = LMInk
                    )
                },
                showModeToggle = false
            )
        }
    }

    Surface(
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .height(52.dp)
            .clickable { showDialog = true },
        shape = RoundedCornerShape(16.dp),
        color = LMWhite
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(horizontal = 14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = LMGray500,
                modifier = Modifier.size(16.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )
            Spacer(modifier = Modifier.width(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            if (value.isBlank()) { // Estrutura de decisão condicional principal
                TranslatedText(
                    text = placeholder,
                    fontFamily = Geist,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFA3A3AE)
                )
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = value,
                    fontFamily = Geist,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = LMInk
                )
            }
        }
    }
}

private fun parseDateToMillis(date: String): Long? { // Declaração de função / método de lógica
    if (date.isBlank()) return null // Estrutura de decisão condicional principal
    val parts = date.split("/") // Declara constante local (leitura única)
    if (parts.size != 3) return null // Estrutura de decisão condicional principal
    return runCatching { // Retorna o resultado da execução da função
        val cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC")) // Declara constante local (leitura única)
        cal.clear()
        cal.set(parts[2].toInt(), parts[1].toInt() - 1, parts[0].toInt())
        cal.timeInMillis
    }.getOrNull()
}

private fun formatMillisToDate(millis: Long): String { // Declaração de função / método de lógica
    val cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC")) // Declara constante local (leitura única)
    cal.timeInMillis = millis
    val dia = cal.get(java.util.Calendar.DAY_OF_MONTH).toString().padStart(2, '0') // Declara constante local (leitura única)
    val mes = (cal.get(java.util.Calendar.MONTH) + 1).toString().padStart(2, '0') // Declara constante local (leitura única)
    val ano = cal.get(java.util.Calendar.YEAR) // Declara constante local (leitura única)
    return "$dia/$mes/$ano" // Retorna o resultado da execução da função
}

@Composable
private fun SelectBox( // Declaração de função / método de lógica
    text: String,
    options: List<String>,
    leadingIcon: @Composable (() -> Unit)? = null,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) } // Declara estado mutável local do Compose

    Box { // Contentor Compose: Sobrepõe os elementos filhos
        Surface(
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxWidth()
                .height(52.dp)
                .clickable { expanded = true },
            shape = RoundedCornerShape(16.dp),
            color = LMWhite,
            border = BorderStroke(1.dp, Color(0xFFE2E2E7))
        ) {
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.padding(horizontal = 12.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) { // Estrutura de decisão condicional principal
                    leadingIcon()
                    Spacer(modifier = Modifier.width(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                }

                TranslatedText(
                    text = text,
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = LMInk,
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                Icon( // Componente Compose: Desenha um ícone vetorial
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = LMGray500,
                    modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        TranslatedText(
                            text = option,
                            fontFamily = Geist,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = LMInk
                        )
                    },
                    onClick = { // Callback: Define a ação executada ao clicar no componente
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}