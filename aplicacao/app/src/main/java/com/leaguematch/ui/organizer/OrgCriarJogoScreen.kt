/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: OrgCriarJogoScreen.kt
 * Tipo: Interface (Compose View) do Organizador
 *
 * Descrição:
 * Este ficheiro define um ecrã da área do Organizador em Jetpack Compose.\n * Fornece interface e lógica visual para criar torneios, gerir equipas, registar e editar jogos e estatísticas.
 */
package com.leaguematch.ui.organizer // Define o pacote deste ficheiro de código

import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.border // Importa dependência / biblioteca necessária
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
import androidx.compose.material.icons.filled.Add // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.CalendarToday // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.ChevronRight // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Groups // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.KeyboardArrowDown // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.LocationOn // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Schedule // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.rounded.ArrowBack // Importa dependência / biblioteca necessária
import androidx.compose.material3.Button // Importa dependência / biblioteca necessária
import androidx.compose.material3.ButtonDefaults // Importa dependência / biblioteca necessária
import androidx.compose.material3.CircularProgressIndicator // Importa dependência / biblioteca necessária
import androidx.compose.material3.HorizontalDivider // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.IconButton // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.OutlinedButton // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Surface // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.material3.TextButton // Importa dependência / biblioteca necessária
import androidx.compose.material3.TextField // Importa dependência / biblioteca necessária
import androidx.compose.material3.TextFieldDefaults // Importa dependência / biblioteca necessária
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
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import androidx.compose.ui.window.Dialog // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Equipa // Importa dependência / biblioteca necessária
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
import android.app.TimePickerDialog // Importa dependência / biblioteca necessária
import androidx.compose.material3.DatePicker // Importa dependência / biblioteca necessária
import androidx.compose.material3.DatePickerDialog // Importa dependência / biblioteca necessária
import androidx.compose.material3.ExperimentalMaterial3Api // Importa dependência / biblioteca necessária
import androidx.compose.material3.rememberDatePickerState // Importa dependência / biblioteca necessária
import androidx.compose.ui.platform.LocalContext // Importa dependência / biblioteca necessária
import java.util.Calendar // Importa dependência / biblioteca necessária
import java.util.TimeZone // Importa dependência / biblioteca necessária

@Composable
fun OrgCriarJogoScreen( // Declaração de função / método de lógica
    torneio: Torneio,
    equipas: List<Equipa>,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onCriarClick: (equipaCasaId: Int, equipaForaId: Int, data: String, hora: String, local: String) -> Unit
) {
    var equipaCasa by remember { mutableStateOf<Equipa?>(null) } // Declara estado mutável local do Compose
    var equipaFora by remember { mutableStateOf<Equipa?>(null) } // Declara estado mutável local do Compose
    var data by remember { mutableStateOf("") } // Declara estado mutável local do Compose
    var hora by remember { mutableStateOf("") } // Declara estado mutável local do Compose
    var local by remember { mutableStateOf("") } // Declara estado mutável local do Compose

    var showCasaDialog by remember { mutableStateOf(false) } // Declara estado mutável local do Compose
    var showForaDialog by remember { mutableStateOf(false) } // Declara estado mutável local do Compose

    if (showCasaDialog) { // Estrutura de decisão condicional principal
        EquipaPickerDialog(
            titulo = "Equipa da casa",
            equipas = equipas.filter { it.id != equipaFora?.id },
            onDismiss = { showCasaDialog = false },
            onSelect = { equipaCasa = it; showCasaDialog = false }
        )
    }

    if (showForaDialog) { // Estrutura de decisão condicional principal
        EquipaPickerDialog(
            titulo = "Equipa visitante",
            equipas = equipas.filter { it.id != equipaCasa?.id },
            onDismiss = { showForaDialog = false },
            onSelect = { equipaFora = it; showForaDialog = false }
        )
    }

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
                TranslatedText(
                    text = "Criar Jogo",
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = LMInk
                )
            }

            Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            VSPreviewCard(
                nomeA = equipaCasa?.nome ?: "Equipa da casa",
                nomeB = equipaFora?.nome ?: "Equipa visitante",
                vazio = equipaCasa == null || equipaFora == null
            )

            Spacer(modifier = Modifier.height(20.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            FormLabel("EQUIPA DA CASA")
            EquipaDropdownField(
                label = equipaCasa?.nome ?: "Selecionar equipa...",
                isEmpty = equipaCasa == null,
                onClick = { showCasaDialog = true } // Callback: Define a ação executada ao clicar no componente
            )

            Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            FormLabel("EQUIPA VISITANTE")
            EquipaDropdownField(
                label = equipaFora?.nome ?: "Selecionar equipa...",
                isEmpty = equipaFora == null,
                onClick = { showForaDialog = true } // Callback: Define a ação executada ao clicar no componente
            )

            Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { // Contentor Compose: Alinha os filhos numa linha horizontal
                Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    FormLabel("DATA")
                    DateField(
                        value = data,
                        onValueChange = { data = it },
                        placeholder = "dd/mm/aaaa"
                    )
                }

                Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    FormLabel("HORA")
                    TimeField(
                        value = hora,
                        onValueChange = { hora = it },
                        placeholder = "hh:mm"
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            FormLabel("LOCAL")
            FormTextField(
                value = local,
                onValueChange = { local = it },
                placeholder = "Campo / Pavilhão...",
                leadingIcon = {
                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = LMGray400) // Componente Compose: Desenha um ícone vetorial
                }
            )

            Spacer(modifier = Modifier.height(24.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            val podeSubmeter = equipaCasa != null && equipaFora != null // Declara constante local (leitura única)

            Button( // Componente Compose: Desenha um botão interativo
                onClick = { // Callback: Define a ação executada ao clicar no componente
                    if (podeSubmeter) { // Estrutura de decisão condicional principal
                        onCriarClick(equipaCasa!!.id, equipaFora!!.id, data, hora, local)
                    }
                },
                enabled = podeSubmeter && !isLoading,
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LMRed,
                    disabledContainerColor = LMGray300
                )
            ) {
                if (isLoading) { // Estrutura de decisão condicional principal
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                        strokeWidth = 2.dp,
                        color = LMWhite
                    )
                } else { // Fluxo condicional alternativo caso o 'if' seja falso
                    TranslatedText(
                        text = "Criar jogo",
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = LMWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            OutlinedButton(
                onClick = onBackClick, // Callback: Define a ação executada ao clicar no componente
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, LMBorder)
            ) {
                TranslatedText(
                    text = "Cancelar",
                    fontFamily = Geist,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = LMGray600
                )
            }
        }
    }
}

@Composable
private fun VSPreviewCard(nomeA: String, nomeB: String, vazio: Boolean) { // Declaração de função / método de lógica
    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(18.dp),
        color = LMInk
    ) {
        Box(modifier = Modifier.fillMaxWidth()) { // Contentor Compose: Sobrepõe os elementos filhos
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(100.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = 30.dp)
                    .background(
                        brush = Brush.radialGradient(listOf(Color.White.copy(alpha = 0.06f), Color.Transparent)),
                        shape = RoundedCornerShape(50)
                    )
            )
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TeamSlot(name = nomeA, isEmpty = vazio || nomeA == "Equipa da casa")

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "VS",
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = LMWhite.copy(alpha = 0.4f)
                )

                TeamSlot(name = nomeB, isEmpty = vazio || nomeB == "Equipa visitante")
            }
        }
    }
}

@Composable
private fun TeamSlot(name: String, isEmpty: Boolean) { // Declaração de função / método de lógica
    Column( // Contentor Compose: Alinha os filhos numa coluna vertical
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(120.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
    ) {
        Box( // Contentor Compose: Sobrepõe os elementos filhos
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .size(44.dp)
                .background(
                    color = if (isEmpty) LMWhite.copy(alpha = 0.08f) else LMWhite.copy(alpha = 0.15f), // Estrutura de decisão condicional principal
                    shape = RoundedCornerShape(12.dp)
                )
                .then(
                    if (isEmpty) Modifier.border(1.dp, LMWhite.copy(alpha = 0.2f), RoundedCornerShape(12.dp)) // Modificador Compose: Define tamanho, margem, padding ou clique
                    else Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                imageVector = if (isEmpty) Icons.Default.Add else Icons.Default.Groups, // Estrutura de decisão condicional principal
                contentDescription = null,
                tint = if (isEmpty) LMWhite.copy(alpha = 0.4f) else LMWhite, // Estrutura de decisão condicional principal
                modifier = Modifier.size(20.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )
        }
        Spacer(modifier = Modifier.height(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = name,
            fontFamily = Geist,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            color = if (isEmpty) LMWhite.copy(alpha = 0.45f) else LMWhite, // Estrutura de decisão condicional principal
            maxLines = 2,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun FormLabel(text: String) { // Declaração de função / método de lógica
    Text( // Componente Compose: Desenha texto estruturado no ecrã
        text = text,
        fontFamily = Geist,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        color = LMGray500,
        letterSpacing = 0.4.sp,
        modifier = Modifier.padding(bottom = 6.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
    )
}

@Composable
private fun EquipaDropdownField( // Declaração de função / método de lógica
    label: String,
    isEmpty: Boolean,
    onClick: () -> Unit // Callback: Define a ação executada ao clicar no componente
) {
    Surface(
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .clickable { onClick() }, // Callback: Define a ação executada ao clicar no componente
        shape = RoundedCornerShape(12.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, LMBorder)
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 13.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                imageVector = Icons.Default.Groups,
                contentDescription = null,
                tint = if (isEmpty) LMGray400 else LMInk, // Estrutura de decisão condicional principal
                modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )
            Spacer(modifier = Modifier.width(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = label,
                fontFamily = Geist,
                fontWeight = if (isEmpty) FontWeight.Normal else FontWeight.SemiBold, // Estrutura de decisão condicional principal
                fontSize = 14.sp,
                color = if (isEmpty) LMGray400 else LMInk, // Estrutura de decisão condicional principal
                modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
            )
            Icon( // Componente Compose: Desenha um ícone vetorial
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = LMGray400,
                modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )
        }
    }
}

@Composable
private fun FormTextField( // Declaração de função / método de lógica
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(12.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, LMBorder)
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) { // Estrutura de decisão condicional principal
                leadingIcon()
                Spacer(modifier = Modifier.width(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
            }
            TextField( // Campo Compose: Entrada de texto simples para utilizador
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = placeholder,
                        fontFamily = Geist,
                        fontSize = 13.sp,
                        color = LMGray400
                    )
                },
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontFamily = Geist,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = LMInk
                )
            )
        }
    }
}

@Composable
private fun EquipaPickerDialog( // Declaração de função / método de lógica
    titulo: String,
    equipas: List<Equipa>,
    onDismiss: () -> Unit,
    onSelect: (Equipa) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = LMWhite,
            modifier = Modifier.fillMaxWidth() // Modificador Compose: Define tamanho, margem, padding ou clique
        ) {
            Column(modifier = Modifier.padding(20.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                TranslatedText(
                    text = titulo,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = LMInk
                )

                Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                if (equipas.isEmpty()) { // Estrutura de decisão condicional principal
                    TranslatedText(
                        text = "Sem equipas neste torneio.",
                        fontFamily = Geist,
                        fontSize = 13.sp,
                        color = LMGray500,
                        modifier = Modifier.padding(vertical = 16.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                } else { // Fluxo condicional alternativo caso o 'if' seja falso
                    equipas.forEachIndexed { index, equipa ->
                        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                .fillMaxWidth()
                                .clickable { onSelect(equipa) }
                                .padding(vertical = 12.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box( // Contentor Compose: Sobrepõe os elementos filhos
                                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                    .size(36.dp)
                                    .background(LMGray100, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon( // Componente Compose: Desenha um ícone vetorial
                                    Icons.Default.Groups,
                                    contentDescription = null,
                                    tint = LMGray500,
                                    modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                            Text( // Componente Compose: Desenha texto estruturado no ecrã
                                text = equipa.nome,
                                fontFamily = Geist,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = LMInk,
                                modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                            )
                            Icon( // Componente Compose: Desenha um ícone vetorial
                                Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = LMGray400,
                                modifier = Modifier.size(16.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                            )
                        }
                        if (index < equipas.lastIndex) { // Estrutura de decisão condicional principal
                            HorizontalDivider(color = LMBorder, thickness = 0.5.dp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                TextButton(
                    onClick = onDismiss, // Callback: Define a ação executada ao clicar no componente
                    modifier = Modifier.align(Alignment.End) // Modificador Compose: Define tamanho, margem, padding ou clique
                ) {
                    TranslatedText("Cancelar", fontFamily = Geist, color = LMGray500)
                }
            }
        }
    }
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
                        Text("Cancelar", color = LMGray500, fontFamily = Geist) // Componente Compose: Desenha texto estruturado no ecrã
                    }
                }
            ) {
                DatePicker(
                    state = state,
                    showModeToggle = false
                )
            }
        }

        Surface(
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxWidth()
                .height(52.dp)
                .clickable { showDialog = true },
            shape = RoundedCornerShape(12.dp),
            color = LMWhite,
            border = BorderStroke(1.dp, LMBorder)
        ) {
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.padding(horizontal = 14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(16.dp), tint = LMGray400) // Componente Compose: Desenha um ícone vetorial
                Spacer(modifier = Modifier.width(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = if (value.isBlank()) placeholder else value, // Estrutura de decisão condicional principal
                    fontFamily = Geist,
                    fontSize = 13.sp,
                    color = if (value.isBlank()) LMGray400 else LMInk // Estrutura de decisão condicional principal
                )
            }
        }
    }

    @Composable
    private fun TimeField( // Declaração de função / método de lógica
        value: String,
        onValueChange: (String) -> Unit,
        placeholder: String
    ) {
        val context = LocalContext.current // Declara constante local (leitura única)

        Surface(
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxWidth()
                .height(52.dp)
                .clickable {
                    val calendar = Calendar.getInstance() // Declara constante local (leitura única)

                    TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            onValueChange(String.format("%02d:%02d", hour, minute))
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).show()
                },
            shape = RoundedCornerShape(12.dp),
            color = LMWhite,
            border = BorderStroke(1.dp, LMBorder)
        ) {
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.padding(horizontal = 14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(16.dp), tint = LMGray400) // Componente Compose: Desenha um ícone vetorial
                Spacer(modifier = Modifier.width(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = if (value.isBlank()) placeholder else value, // Estrutura de decisão condicional principal
                    fontFamily = Geist,
                    fontSize = 13.sp,
                    color = if (value.isBlank()) LMGray400 else LMInk // Estrutura de decisão condicional principal
                )
            }
        }
    }

    private fun parseDateToMillis(date: String): Long? { // Declaração de função / método de lógica
        if (date.isBlank()) return null // Estrutura de decisão condicional principal
        val parts = date.split("/") // Declara constante local (leitura única)
        if (parts.size != 3) return null // Estrutura de decisão condicional principal

        return runCatching { // Retorna o resultado da execução da função
            val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")) // Declara constante local (leitura única)
            cal.clear()
            cal.set(parts[2].toInt(), parts[1].toInt() - 1, parts[0].toInt())
            cal.timeInMillis
        }.getOrNull()
    }

    private fun formatMillisToDate(millis: Long): String { // Declaração de função / método de lógica
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")) // Declara constante local (leitura única)
        cal.timeInMillis = millis

        val dia = cal.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0') // Declara constante local (leitura única)
        val mes = (cal.get(Calendar.MONTH) + 1).toString().padStart(2, '0') // Declara constante local (leitura única)
        val ano = cal.get(Calendar.YEAR) // Declara constante local (leitura única)

        return "$dia/$mes/$ano" // Retorna o resultado da execução da função
    }

