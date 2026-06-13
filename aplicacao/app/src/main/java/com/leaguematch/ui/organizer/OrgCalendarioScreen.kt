package com.leaguematch.ui.organizer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.ui.components.TranslatedText
import com.leaguematch.ui.theme.Geist
import com.leaguematch.ui.theme.LMBorder
import com.leaguematch.ui.theme.LMGray100
import com.leaguematch.ui.theme.LMGray300
import com.leaguematch.ui.theme.LMGray50
import com.leaguematch.ui.theme.LMGray500
import com.leaguematch.ui.theme.LMInk
import com.leaguematch.ui.theme.LMRed
import com.leaguematch.ui.theme.LMWhite
import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.DatePicker
import androidx.compose.material3.TimePicker
import androidx.compose.ui.window.Dialog

@Composable
fun OrgCalendarioScreen(
    torneio: Torneio,
    jogos: List<Jogo>,
    onBackClick: () -> Unit,
    onAtualizarDataHoraJogo: (Jogo, String, String) -> Unit
) {
    val agrupados = jogos
        .groupBy { it.data.ifBlank { "Sem data" } }
        .toSortedMap(comparator = compareBy {
            if (it == "Sem data") "9999-99-99" else it
        })

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
                Column(modifier = Modifier.weight(1f)) {
                    TranslatedText(
                        text = "Calendário",
                        fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp, color = LMInk
                    )
                    Text(text = torneio.nome, fontFamily = Geist, fontSize = 12.sp, color = LMGray500)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (agrupados.isEmpty()) {
                EmptyCalendarState()
            } else {
                agrupados.forEach { (data, jogosDoDia) ->
                    DiaSection(
                        data = data,
                        jogos = jogosDoDia,
                        onAtualizarDataHoraJogo = onAtualizarDataHoraJogo
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }
    }
}

@Composable
private fun DiaSection(
    data: String,
    jogos: List<Jogo>,
    onAtualizarDataHoraJogo: (Jogo, String, String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(Color(0xFFFFEDD5), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = Color(0xFFEA580C),
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = data,
            fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp, color = LMInk
        )
        Spacer(modifier = Modifier.width(8.dp))
        TranslatedText(
            text = "· ${jogos.size} ${if (jogos.size == 1) "jogo" else "jogos"}",
            fontFamily = Geist, fontSize = 12.sp, color = LMGray500
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        jogos.sortedBy { it.hora }.forEach { jogo ->
            JogoCalendarioCard(
                jogo = jogo,
                onAtualizarDataHoraJogo = onAtualizarDataHoraJogo
            )
        }
    }
}

@Composable
private fun JogoCalendarioCard(
    jogo: Jogo,
    onAtualizarDataHoraJogo: (Jogo, String, String) -> Unit
) {
    var mostrarDialog by remember { mutableStateOf(false) }
    var novaData by remember { mutableStateOf(jogo.data) }
    var novaHora by remember { mutableStateOf(jogo.hora) }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, LMBorder),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = jogo.hora.ifBlank { "--:--" },
                fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp, color = LMInk,
                modifier = Modifier.width(54.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(LMGray100, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.SportsSoccer, contentDescription = null, tint = LMRed, modifier = Modifier.size(18.dp))
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                TranslatedText(
                    text = "${jogo.casa} vs ${jogo.fora}",
                    fontFamily = Geist, fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp, color = LMInk
                )
                TranslatedText(
                    text = jogo.estado,
                    fontFamily = Geist, fontSize = 11.sp, color = LMGray500
                )
            }
            if (jogo.estado.equals("Agendado", ignoreCase = true)) {
                Text(
                    text = "Alterar data/hora",
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    color = LMRed,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clickable {
                            novaData = jogo.data
                            novaHora = jogo.hora
                            mostrarDialog = true
                        }
                )
            }
            if (jogo.estado.equals("Finalizado", ignoreCase = true) ||
                jogo.estado.equals("A Decorrer", ignoreCase = true)) {
                Text(
                    text = "${jogo.resultadoCasa}-${jogo.resultadoFora}",
                    fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp, color = LMInk
                )
            }
        }
    }
    if (mostrarDialog) {
        AlterarDataHoraDialog(
            dataAtual = novaData,
            horaAtual = novaHora,
            onDismiss = { mostrarDialog = false },
            onGuardar = { dataEscolhida, horaEscolhida ->
                onAtualizarDataHoraJogo(jogo, dataEscolhida, horaEscolhida)
                mostrarDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlterarDataHoraDialog(
    dataAtual: String,
    horaAtual: String,
    onDismiss: () -> Unit,
    onGuardar: (String, String) -> Unit
) {
    val dataMillis = parseDateToMillis(dataAtual)
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = dataMillis
    )

    var mostrarTimePicker by remember { mutableStateOf(false) }

    val partesHora = horaAtual.split(":")
    val horaInicial = partesHora.getOrNull(0)?.toIntOrNull() ?: 12
    val minutoInicial = partesHora.getOrNull(1)?.toIntOrNull() ?: 0

    val timePickerState = rememberTimePickerState(
        initialHour = horaInicial,
        initialMinute = minutoInicial,
        is24Hour = true
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = LMWhite,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                Text(
                    text = "Alterar data e hora",
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = LMInk
                )

                Spacer(modifier = Modifier.height(14.dp))

                DatePicker(
                    state = datePickerState,
                    title = null,
                    headline = null,
                    showModeToggle = false
                )

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { mostrarTimePicker = true },
                    shape = RoundedCornerShape(14.dp),
                    color = LMGray50,
                    border = BorderStroke(1.dp, LMBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Hora",
                            fontFamily = Geist,
                            fontSize = 12.sp,
                            color = LMGray500
                        )

                        Text(
                            text = "%02d:%02d".format(
                                timePickerState.hour,
                                timePickerState.minute
                            ),
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = LMInk
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            val dataEscolhida = formatMillisToDate(
                                datePickerState.selectedDateMillis
                            )

                            val horaEscolhida = "%02d:%02d".format(
                                timePickerState.hour,
                                timePickerState.minute
                            )

                            onGuardar(dataEscolhida, horaEscolhida)
                        }
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }

    if (mostrarTimePicker) {
        Dialog(onDismissRequest = { mostrarTimePicker = false }) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = LMWhite
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Escolher hora",
                        fontFamily = Geist,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = LMInk
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TimePicker(state = timePickerState)

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { mostrarTimePicker = false }) {
                            Text("Cancelar")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(onClick = { mostrarTimePicker = false }) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}

private fun parseDateToMillis(date: String): Long? {
    return try {
        val partes = date.split("/")
        if (partes.size != 3) return null

        val day = partes[0].toInt()
        val month = partes[1].toInt() - 1
        val year = partes[2].toInt()

        java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.YEAR, year)
            set(java.util.Calendar.MONTH, month)
            set(java.util.Calendar.DAY_OF_MONTH, day)
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }.timeInMillis
    } catch (e: Exception) {
        null
    }
}

private fun formatMillisToDate(millis: Long?): String {
    if (millis == null) return ""

    val calendar = java.util.Calendar.getInstance().apply {
        timeInMillis = millis
    }

    val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
    val month = calendar.get(java.util.Calendar.MONTH) + 1
    val year = calendar.get(java.util.Calendar.YEAR)

    return "%02d/%02d/%04d".format(day, month, year)
}

@Composable
private fun EmptyCalendarState() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = LMGray50,
        border = BorderStroke(1.dp, LMBorder)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.CalendarMonth, contentDescription = null,
                tint = LMGray300, modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TranslatedText("Sem jogos agendados.", fontFamily = Geist, fontSize = 13.sp, color = LMGray500)
        }
    }
}
