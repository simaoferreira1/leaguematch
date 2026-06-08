package com.leaguematch.ui.organizer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.ui.theme.*

@Composable
fun CreateTournamentScreen(
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
    var nome by remember { mutableStateOf("") }
    var modalidade by remember { mutableStateOf("Futebol") }
    var formato by remember { mutableStateOf("Liga") }
    var dataInicio by remember { mutableStateOf("") }
    var dataFim by remember { mutableStateOf("") }
    var maxEquipas by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var regras by remember { mutableStateOf("") }

    val modalidades = listOf(
        "Futebol",
        "Basquetebol",
        "Andebol",
        "Ténis",
        "Padel"
    )

    val formatos = listOf(
        "Liga",
        "Eliminatória",
        "Grupos"
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "‹",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = LMInk,
                    modifier = Modifier
                        .clickable { onBackClick() }
                        .padding(end = 12.dp)
                )

                Text(
                    text = "Criar Torneio",
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = LMInk,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "Cancelar",
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = LMGray500,
                    modifier = Modifier.clickable { onCancelClick() }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFFFFF5F6),
                border = BorderStroke(1.dp, Color(0xFFFFCCD3))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    listOf(LMRed, Color(0xFFC41326))
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = LMWhite,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Novo torneio",
                            fontFamily = Geist,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            color = LMInk
                        )

                        Text(
                            text = "Passo 1 de 2 — Informação geral",
                            fontFamily = Geist,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = LMGray500
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            FormLabel("NOME DO TORNEIO")
            FormTextField(
                value = nome,
                onValueChange = { nome = it },
                placeholder = "Ex: Liga do Vinho 2026"
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    FormLabel("MODALIDADE")

                    SelectBox(
                        text = modalidade,
                        options = modalidades,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.SportsSoccer,
                                contentDescription = null,
                                tint = LMInk,
                                modifier = Modifier.size(17.dp)
                            )
                        },
                        onSelected = { modalidade = it }
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    FormLabel("FORMATO")

                    SelectBox(
                        text = formato,
                        options = formatos,
                        onSelected = { formato = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    FormLabel("DATA INÍCIO")
                    DateField(
                        value = dataInicio,
                        onValueChange = { dataInicio = it },
                        placeholder = "dd/mm/aaaa"
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    FormLabel("DATA FIM")
                    DateField(
                        value = dataFim,
                        onValueChange = { dataFim = it },
                        placeholder = "dd/mm/aaaa"
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            FormLabel("MÁXIMO DE EQUIPAS")
            FormTextField(
                value = maxEquipas,
                onValueChange = { maxEquipas = it },
                placeholder = "Ex: 16"
            )

            Spacer(modifier = Modifier.height(14.dp))

            FormLabel("DESCRIÇÃO")
            FormTextField(
                value = descricao,
                onValueChange = { descricao = it },
                placeholder = "Adiciona contexto, prémios e detalhes...",
                minHeight = 76.dp
            )

            Spacer(modifier = Modifier.height(14.dp))

            FormLabel("REGRAS")
            FormTextField(
                value = regras,
                onValueChange = { regras = it },
                placeholder = "Define regras específicas do torneio...",
                minHeight = 76.dp
            )

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp)
                        .clickable { onBackClick() },
                    shape = RoundedCornerShape(16.dp),
                    color = LMWhite,
                    border = BorderStroke(1.dp, Color(0xFFE2E2E7))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "Anterior",
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = LMInk
                        )
                    }
                }

                Box(
                    modifier = Modifier
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
                    Text(
                        text = "Continuar",
                        fontFamily = Geist,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = LMWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FormLabel(text: String) {
    Text(
        text = text,
        fontFamily = Geist,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 12.sp,
        color = LMGray500
    )

    Spacer(modifier = Modifier.height(7.dp))
}

@Composable
private fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    minHeight: Dp = 52.dp
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                fontFamily = Geist,
                fontSize = 14.sp,
                color = Color(0xFFA3A3AE)
            )
        },
        modifier = Modifier
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

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
private fun DateField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        val initialMillis = parseDateToMillis(value)
        val state = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            shape = RoundedCornerShape(24.dp),
            properties = androidx.compose.ui.window.DialogProperties(
                usePlatformDefaultWidth = false
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let {
                        onValueChange(formatMillisToDate(it))
                    }
                    showDialog = false
                }) {
                    Text("OK", color = LMRed, fontFamily = Geist, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar", color = LMGray500, fontFamily = Geist)
                }
            }
        ) {
            DatePicker(
                state = state,
                title = {
                    Text(
                        text = "Escolher data",
                        modifier = Modifier.padding(start = 24.dp, top = 16.dp),
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = LMGray500
                    )
                },
                headline = {
                    val texto = state.selectedDateMillis?.let { formatMillisToDate(it) } ?: "—/—/——"
                    Text(
                        text = texto,
                        modifier = Modifier.padding(start = 24.dp, bottom = 12.dp),
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
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clickable { showDialog = true },
        shape = RoundedCornerShape(16.dp),
        color = LMWhite
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = LMGray500,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = value.ifBlank { placeholder },
                fontFamily = Geist,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = if (value.isBlank()) Color(0xFFA3A3AE) else LMInk
            )
        }
    }
}

private fun parseDateToMillis(date: String): Long? {
    if (date.isBlank()) return null
    val parts = date.split("/")
    if (parts.size != 3) return null
    return runCatching {
        val cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
        cal.clear()
        cal.set(parts[2].toInt(), parts[1].toInt() - 1, parts[0].toInt())
        cal.timeInMillis
    }.getOrNull()
}

private fun formatMillisToDate(millis: Long): String {
    val cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
    cal.timeInMillis = millis
    val dia = cal.get(java.util.Calendar.DAY_OF_MONTH).toString().padStart(2, '0')
    val mes = (cal.get(java.util.Calendar.MONTH) + 1).toString().padStart(2, '0')
    val ano = cal.get(java.util.Calendar.YEAR)
    return "$dia/$mes/$ano"
}

@Composable
private fun SelectBox(
    text: String,
    options: List<String>,
    leadingIcon: @Composable (() -> Unit)? = null,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clickable { expanded = true },
            shape = RoundedCornerShape(16.dp),
            color = LMWhite,
            border = BorderStroke(1.dp, Color(0xFFE2E2E7))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = text,
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = LMInk,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = LMGray500,
                    modifier = Modifier.size(18.dp)
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
                        Text(
                            text = option,
                            fontFamily = Geist,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = LMInk
                        )
                    },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}