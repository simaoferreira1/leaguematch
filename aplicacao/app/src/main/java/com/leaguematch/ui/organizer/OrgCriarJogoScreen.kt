package com.leaguematch.ui.organizer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.leaguematch.data.remote.model.Equipa
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.ui.components.TranslatedText
import com.leaguematch.ui.theme.Bricolage
import com.leaguematch.ui.theme.Geist
import com.leaguematch.ui.theme.LMBorder
import com.leaguematch.ui.theme.LMGray100
import com.leaguematch.ui.theme.LMGray300
import com.leaguematch.ui.theme.LMGray400
import com.leaguematch.ui.theme.LMGray500
import com.leaguematch.ui.theme.LMGray600
import com.leaguematch.ui.theme.LMInk
import com.leaguematch.ui.theme.LMRed
import com.leaguematch.ui.theme.LMWhite
import android.app.TimePickerDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar
import java.util.TimeZone

@Composable
fun OrgCriarJogoScreen(
    torneio: Torneio,
    equipas: List<Equipa>,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onCriarClick: (equipaCasaId: Int, equipaForaId: Int, data: String, hora: String, local: String) -> Unit
) {
    var equipaCasa by remember { mutableStateOf<Equipa?>(null) }
    var equipaFora by remember { mutableStateOf<Equipa?>(null) }
    var data by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var local by remember { mutableStateOf("") }

    var showCasaDialog by remember { mutableStateOf(false) }
    var showForaDialog by remember { mutableStateOf(false) }

    if (showCasaDialog) {
        EquipaPickerDialog(
            titulo = "Equipa da casa",
            equipas = equipas.filter { it.id != equipaFora?.id },
            onDismiss = { showCasaDialog = false },
            onSelect = { equipaCasa = it; showCasaDialog = false }
        )
    }

    if (showForaDialog) {
        EquipaPickerDialog(
            titulo = "Equipa visitante",
            equipas = equipas.filter { it.id != equipaCasa?.id },
            onDismiss = { showForaDialog = false },
            onSelect = { equipaFora = it; showForaDialog = false }
        )
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
                .padding(bottom = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = null, tint = LMInk)
                }
                TranslatedText(
                    text = "Criar Jogo",
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = LMInk
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            VSPreviewCard(
                nomeA = equipaCasa?.nome ?: "Equipa da casa",
                nomeB = equipaFora?.nome ?: "Equipa visitante",
                vazio = equipaCasa == null || equipaFora == null
            )

            Spacer(modifier = Modifier.height(20.dp))

            FormLabel("EQUIPA DA CASA")
            EquipaDropdownField(
                label = equipaCasa?.nome ?: "Selecionar equipa...",
                isEmpty = equipaCasa == null,
                onClick = { showCasaDialog = true }
            )

            Spacer(modifier = Modifier.height(12.dp))

            FormLabel("EQUIPA VISITANTE")
            EquipaDropdownField(
                label = equipaFora?.nome ?: "Selecionar equipa...",
                isEmpty = equipaFora == null,
                onClick = { showForaDialog = true }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    FormLabel("DATA")
                    DateField(
                        value = data,
                        onValueChange = { data = it },
                        placeholder = "dd/mm/aaaa"
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    FormLabel("HORA")
                    TimeField(
                        value = hora,
                        onValueChange = { hora = it },
                        placeholder = "hh:mm"
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            FormLabel("LOCAL")
            FormTextField(
                value = local,
                onValueChange = { local = it },
                placeholder = "Campo / Pavilhão...",
                leadingIcon = {
                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = LMGray400)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            val podeSubmeter = equipaCasa != null && equipaFora != null

            Button(
                onClick = {
                    if (podeSubmeter) {
                        onCriarClick(equipaCasa!!.id, equipaFora!!.id, data, hora, local)
                    }
                },
                enabled = podeSubmeter && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LMRed,
                    disabledContainerColor = LMGray300
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = LMWhite
                    )
                } else {
                    TranslatedText(
                        text = "Criar jogo",
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = LMWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier
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
private fun VSPreviewCard(nomeA: String, nomeB: String, vazio: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = LMInk
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = 30.dp)
                    .background(
                        brush = Brush.radialGradient(listOf(Color.White.copy(alpha = 0.06f), Color.Transparent)),
                        shape = RoundedCornerShape(50)
                    )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TeamSlot(name = nomeA, isEmpty = vazio || nomeA == "Equipa da casa")

                Text(
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
private fun TeamSlot(name: String, isEmpty: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(120.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    color = if (isEmpty) LMWhite.copy(alpha = 0.08f) else LMWhite.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                )
                .then(
                    if (isEmpty) Modifier.border(1.dp, LMWhite.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isEmpty) Icons.Default.Add else Icons.Default.Groups,
                contentDescription = null,
                tint = if (isEmpty) LMWhite.copy(alpha = 0.4f) else LMWhite,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = name,
            fontFamily = Geist,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            color = if (isEmpty) LMWhite.copy(alpha = 0.45f) else LMWhite,
            maxLines = 2,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun FormLabel(text: String) {
    Text(
        text = text,
        fontFamily = Geist,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        color = LMGray500,
        letterSpacing = 0.4.sp,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun EquipaDropdownField(
    label: String,
    isEmpty: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, LMBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Groups,
                contentDescription = null,
                tint = if (isEmpty) LMGray400 else LMInk,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = label,
                fontFamily = Geist,
                fontWeight = if (isEmpty) FontWeight.Normal else FontWeight.SemiBold,
                fontSize = 14.sp,
                color = if (isEmpty) LMGray400 else LMInk,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = LMGray400,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, LMBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = placeholder,
                        fontFamily = Geist,
                        fontSize = 13.sp,
                        color = LMGray400
                    )
                },
                modifier = Modifier.fillMaxWidth(),
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
private fun EquipaPickerDialog(
    titulo: String,
    equipas: List<Equipa>,
    onDismiss: () -> Unit,
    onSelect: (Equipa) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = LMWhite,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                TranslatedText(
                    text = titulo,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = LMInk
                )

                Spacer(modifier = Modifier.height(14.dp))

                if (equipas.isEmpty()) {
                    TranslatedText(
                        text = "Sem equipas neste torneio.",
                        fontFamily = Geist,
                        fontSize = 13.sp,
                        color = LMGray500,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    equipas.forEachIndexed { index, equipa ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(equipa) }
                                .padding(vertical = 12.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(LMGray100, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Groups,
                                    contentDescription = null,
                                    tint = LMGray500,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = equipa.nome,
                                fontFamily = Geist,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = LMInk,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = LMGray400,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        if (index < equipas.lastIndex) {
                            HorizontalDivider(color = LMBorder, thickness = 0.5.dp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    TranslatedText("Cancelar", fontFamily = Geist, color = LMGray500)
                }
            }
        }
    }
}
    @OptIn(ExperimentalMaterial3Api::class)
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
                    showModeToggle = false
                )
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clickable { showDialog = true },
            shape = RoundedCornerShape(12.dp),
            color = LMWhite,
            border = BorderStroke(1.dp, LMBorder)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(16.dp), tint = LMGray400)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (value.isBlank()) placeholder else value,
                    fontFamily = Geist,
                    fontSize = 13.sp,
                    color = if (value.isBlank()) LMGray400 else LMInk
                )
            }
        }
    }

    @Composable
    private fun TimeField(
        value: String,
        onValueChange: (String) -> Unit,
        placeholder: String
    ) {
        val context = LocalContext.current

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clickable {
                    val calendar = Calendar.getInstance()

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
            Row(
                modifier = Modifier.padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(16.dp), tint = LMGray400)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (value.isBlank()) placeholder else value,
                    fontFamily = Geist,
                    fontSize = 13.sp,
                    color = if (value.isBlank()) LMGray400 else LMInk
                )
            }
        }
    }

    private fun parseDateToMillis(date: String): Long? {
        if (date.isBlank()) return null
        val parts = date.split("/")
        if (parts.size != 3) return null

        return runCatching {
            val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            cal.clear()
            cal.set(parts[2].toInt(), parts[1].toInt() - 1, parts[0].toInt())
            cal.timeInMillis
        }.getOrNull()
    }

    private fun formatMillisToDate(millis: Long): String {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.timeInMillis = millis

        val dia = cal.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')
        val mes = (cal.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
        val ano = cal.get(Calendar.YEAR)

        return "$dia/$mes/$ano"
    }

