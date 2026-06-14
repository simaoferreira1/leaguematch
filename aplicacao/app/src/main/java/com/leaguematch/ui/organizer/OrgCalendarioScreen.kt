/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: OrgCalendarioScreen.kt
 * Tipo: Interface (Compose View) do Organizador
 *
 * Descrição:
 * Este ficheiro define um ecrã da área do Organizador em Jetpack Compose.\n * Fornece interface e lógica visual para criar torneios, gerir equipas, registar e editar jogos e estatísticas.
 */
package com.leaguematch.ui.organizer // Define o pacote deste ficheiro de código

import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
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
import androidx.compose.foundation.layout.width // Importa dependência / biblioteca necessária
import androidx.compose.foundation.rememberScrollState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.CalendarMonth // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsSoccer // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.rounded.ArrowBack // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.IconButton // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Surface // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Jogo // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Torneio // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMBorder // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray100 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray300 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray50 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária
import androidx.compose.foundation.clickable // Importa dependência / biblioteca necessária
import androidx.compose.material3.Button // Importa dependência / biblioteca necessária
import androidx.compose.material3.ExperimentalMaterial3Api // Importa dependência / biblioteca necessária
import androidx.compose.material3.TextButton // Importa dependência / biblioteca necessária
import androidx.compose.material3.rememberDatePickerState // Importa dependência / biblioteca necessária
import androidx.compose.material3.rememberTimePickerState // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.remember // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária
import androidx.compose.material3.DatePicker // Importa dependência / biblioteca necessária
import androidx.compose.material3.TimePicker // Importa dependência / biblioteca necessária
import androidx.compose.ui.window.Dialog // Importa dependência / biblioteca necessária

@Composable
fun OrgCalendarioScreen( // Declaração de função / método de lógica
    torneio: Torneio,
    jogos: List<Jogo>,
    onBackClick: () -> Unit,
    onAtualizarDataHoraJogo: (Jogo, String, String) -> Unit
) {
    val agrupados = jogos // Declara constante local (leitura única)
        .groupBy { it.data.ifBlank { "Sem data" } }
        .toSortedMap(comparator = compareBy {
            if (it == "Sem data") "9999-99-99" else it // Estrutura de decisão condicional principal
        })

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
                        text = "Calendário",
                        fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp, color = LMInk
                    )
                    Text(text = torneio.nome, fontFamily = Geist, fontSize = 12.sp, color = LMGray500) // Componente Compose: Desenha texto estruturado no ecrã
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            if (agrupados.isEmpty()) { // Estrutura de decisão condicional principal
                EmptyCalendarState()
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                agrupados.forEach { (data, jogosDoDia) ->
                    DiaSection(
                        data = data,
                        jogos = jogosDoDia,
                        onAtualizarDataHoraJogo = onAtualizarDataHoraJogo
                    )
                    Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                }
            }
        }
    }
}

@Composable
private fun DiaSection( // Declaração de função / método de lógica
    data: String,
    jogos: List<Jogo>,
    onAtualizarDataHoraJogo: (Jogo, String, String) -> Unit
) {
    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
    ) {
        Box( // Contentor Compose: Sobrepõe os elementos filhos
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .size(34.dp)
                .background(Color(0xFFFFEDD5), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = Color(0xFFEA580C),
                modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )
        }
        Spacer(modifier = Modifier.width(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = data,
            fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp, color = LMInk
        )
        Spacer(modifier = Modifier.width(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
        TranslatedText(
            text = "· ${jogos.size} ${if (jogos.size == 1) "jogo" else "jogos"}", // Estrutura de decisão condicional principal
            fontFamily = Geist, fontSize = 12.sp, color = LMGray500
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
        jogos.sortedBy { it.hora }.forEach { jogo ->
            JogoCalendarioCard(
                jogo = jogo,
                onAtualizarDataHoraJogo = onAtualizarDataHoraJogo
            )
        }
    }
}

@Composable
private fun JogoCalendarioCard( // Declaração de função / método de lógica
    jogo: Jogo,
    onAtualizarDataHoraJogo: (Jogo, String, String) -> Unit
) {
    var mostrarDialog by remember { mutableStateOf(false) } // Declara estado mutável local do Compose
    var novaData by remember { mutableStateOf(jogo.data) } // Declara estado mutável local do Compose
    var novaHora by remember { mutableStateOf(jogo.hora) } // Declara estado mutável local do Compose
    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(14.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, LMBorder),
        shadowElevation = 1.dp
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = jogo.hora.ifBlank { "--:--" },
                fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp, color = LMInk,
                modifier = Modifier.width(54.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )

            Spacer(modifier = Modifier.width(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(34.dp)
                    .background(LMGray100, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.SportsSoccer, contentDescription = null, tint = LMRed, modifier = Modifier.size(18.dp)) // Componente Compose: Desenha um ícone vetorial
            }

            Spacer(modifier = Modifier.width(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
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
            if (jogo.estado.equals("Agendado", ignoreCase = true)) { // Estrutura de decisão condicional principal
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "Alterar data/hora",
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    color = LMRed,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .padding(top = 4.dp)
                        .clickable {
                            novaData = jogo.data
                            novaHora = jogo.hora
                            mostrarDialog = true
                        }
                )
            }
            if (jogo.estado.equals("Finalizado", ignoreCase = true) || // Estrutura de decisão condicional principal
                jogo.estado.equals("A Decorrer", ignoreCase = true)) {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "${jogo.resultadoCasa}-${jogo.resultadoFora}",
                    fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp, color = LMInk
                )
            }
        }
    }
    if (mostrarDialog) { // Estrutura de decisão condicional principal
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

@OptIn(ExperimentalMaterial3Api::class) // Declaração de classe para modelar objetos
@Composable
private fun AlterarDataHoraDialog( // Declaração de função / método de lógica
    dataAtual: String,
    horaAtual: String,
    onDismiss: () -> Unit,
    onGuardar: (String, String) -> Unit
) {
    val dataMillis = parseDateToMillis(dataAtual) // Declara constante local (leitura única)
    val datePickerState = rememberDatePickerState( // Declara constante local (leitura única)
        initialSelectedDateMillis = dataMillis
    )

    var mostrarTimePicker by remember { mutableStateOf(false) } // Declara estado mutável local do Compose

    val partesHora = horaAtual.split(":") // Declara constante local (leitura única)
    val horaInicial = partesHora.getOrNull(0)?.toIntOrNull() ?: 12 // Declara constante local (leitura única)
    val minutoInicial = partesHora.getOrNull(1)?.toIntOrNull() ?: 0 // Declara constante local (leitura única)

    val timePickerState = rememberTimePickerState( // Declara constante local (leitura única)
        initialHour = horaInicial,
        initialMinute = minutoInicial,
        is24Hour = true
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = LMWhite,
            modifier = Modifier.fillMaxWidth() // Modificador Compose: Define tamanho, margem, padding ou clique
        ) {
            Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                modifier = Modifier.padding(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            ) {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "Alterar data e hora",
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = LMInk
                )

                Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                DatePicker(
                    state = datePickerState,
                    title = null,
                    headline = null,
                    showModeToggle = false
                )

                Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Surface(
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .fillMaxWidth()
                        .clickable { mostrarTimePicker = true },
                    shape = RoundedCornerShape(14.dp),
                    color = LMGray50,
                    border = BorderStroke(1.dp, LMBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = "Hora",
                            fontFamily = Geist,
                            fontSize = 12.sp,
                            color = LMGray500
                        )

                        Text( // Componente Compose: Desenha texto estruturado no ecrã
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

                Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                    modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { // Callback: Define a ação executada ao clicar no componente
                        Text("Cancelar") // Componente Compose: Desenha texto estruturado no ecrã
                    }

                    Spacer(modifier = Modifier.width(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    Button( // Componente Compose: Desenha um botão interativo
                        onClick = { // Callback: Define a ação executada ao clicar no componente
                            val dataEscolhida = formatMillisToDate( // Declara constante local (leitura única)
                                datePickerState.selectedDateMillis
                            )

                            val horaEscolhida = "%02d:%02d".format( // Declara constante local (leitura única)
                                timePickerState.hour,
                                timePickerState.minute
                            )

                            onGuardar(dataEscolhida, horaEscolhida)
                        }
                    ) {
                        Text("Guardar") // Componente Compose: Desenha texto estruturado no ecrã
                    }
                }
            }
        }
    }

    if (mostrarTimePicker) { // Estrutura de decisão condicional principal
        Dialog(onDismissRequest = { mostrarTimePicker = false }) {
            Surface(
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                shape = RoundedCornerShape(18.dp),
                color = LMWhite
            ) {
                Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                    modifier = Modifier.padding(20.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "Escolher hora",
                        fontFamily = Geist,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = LMInk
                    )

                    Spacer(modifier = Modifier.height(16.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    TimePicker(state = timePickerState)

                    Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { mostrarTimePicker = false }) { // Callback: Define a ação executada ao clicar no componente
                            Text("Cancelar") // Componente Compose: Desenha texto estruturado no ecrã
                        }

                        Spacer(modifier = Modifier.width(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                        Button(onClick = { mostrarTimePicker = false }) { // Componente Compose: Desenha um botão interativo
                            Text("OK") // Componente Compose: Desenha texto estruturado no ecrã
                        }
                    }
                }
            }
        }
    }
}

private fun parseDateToMillis(date: String): Long? { // Declaração de função / método de lógica
    return try { // Tenta executar bloco que pode lançar exceções
        val partes = date.split("/") // Declara constante local (leitura única)
        if (partes.size != 3) return null // Estrutura de decisão condicional principal

        val day = partes[0].toInt() // Declara constante local (leitura única)
        val month = partes[1].toInt() - 1 // Declara constante local (leitura única)
        val year = partes[2].toInt() // Declara constante local (leitura única)

        java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.YEAR, year)
            set(java.util.Calendar.MONTH, month)
            set(java.util.Calendar.DAY_OF_MONTH, day)
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }.timeInMillis
    } catch (e: Exception) { // Captura e trata eventuais exceções ocorridas no bloco try
        null
    }
}

private fun formatMillisToDate(millis: Long?): String { // Declaração de função / método de lógica
    if (millis == null) return "" // Estrutura de decisão condicional principal

    val calendar = java.util.Calendar.getInstance().apply { // Declara constante local (leitura única)
        timeInMillis = millis
    }

    val day = calendar.get(java.util.Calendar.DAY_OF_MONTH) // Declara constante local (leitura única)
    val month = calendar.get(java.util.Calendar.MONTH) + 1 // Declara constante local (leitura única)
    val year = calendar.get(java.util.Calendar.YEAR) // Declara constante local (leitura única)

    return "%02d/%02d/%04d".format(day, month, year) // Retorna o resultado da execução da função
}

@Composable
private fun EmptyCalendarState() { // Declaração de função / método de lógica
    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(16.dp),
        color = LMGray50,
        border = BorderStroke(1.dp, LMBorder)
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier.padding(24.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                Icons.Default.CalendarMonth, contentDescription = null,
                tint = LMGray300, modifier = Modifier.size(40.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )
            Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
            TranslatedText("Sem jogos agendados.", fontFamily = Geist, fontSize = 13.sp, color = LMGray500)
        }
    }
}
