package com.leaguematch.ui.organizer

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.EstatisticaJogo
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.data.repository.LeagueMatchRepository
import com.leaguematch.ui.components.TranslatedText
import com.leaguematch.ui.theme.Bricolage
import com.leaguematch.ui.theme.Geist
import com.leaguematch.ui.theme.LMRed
import kotlinx.coroutines.launch

@Composable
fun OrgEditarEstatisticasJogoScreen(
    jogo: Jogo,
    modalidade: String,
    repository: LeagueMatchRepository,
    onBackClick: () -> Unit,
    onGuardarClick: () -> Unit
) {
    var tabSelecionada by remember { mutableStateOf("Eventos") }

    var scoreCasa by remember { mutableStateOf(jogo.resultadoCasa) }
    var scoreFora by remember { mutableStateOf(jogo.resultadoFora) }
    var estadoJogo by remember { mutableStateOf(jogo.estado) }
    var dataText by remember { mutableStateOf(jogo.data) }
    var horaText by remember { mutableStateOf(jogo.hora) }
    var isSavingScore by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = androidx.compose.ui.platform.LocalContext.current

    Scaffold(containerColor = Color(0xFFF7F7F7)) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF7F7F7))
        ) {
            TopBarEditarEstatisticas(onBackClick)
            JogoHeaderCard(
                jogo = jogo,
                modalidade = modalidade,
                scoreCasa = scoreCasa,
                scoreFora = scoreFora,
                estadoJogo = estadoJogo,
                onCasaChange = { scoreCasa = it },
                onForaChange = { scoreFora = it },
                dataText = dataText,
                onDataChange = { dataText = it },
                horaText = horaText,
                onHoraChange = { horaText = it },
                onSaveScoreClick = {
                    scope.launch {
                        isSavingScore = true
                        val result = repository.atualizarJogo(
                            id = jogo.id,
                            resultadoCasa = scoreCasa,
                            resultadoFora = scoreFora,
                            estado = estadoJogo,
                            data = dataText,
                            hora = horaText
                        )
                        isSavingScore = false
                        if (result != null) {
                            Toast.makeText(context, "Jogo guardado com sucesso!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Erro ao guardar alterações.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onStartGameClick = {
                    scope.launch {
                        isSavingScore = true
                        val result = repository.atualizarJogo(
                            id = jogo.id,
                            resultadoCasa = 0,
                            resultadoFora = 0,
                            estado = "A Decorrer",
                            data = dataText,
                            hora = horaText
                        )
                        isSavingScore = false
                        if (result != null) {
                            scoreCasa = 0
                            scoreFora = 0
                            estadoJogo = "A Decorrer"
                            Toast.makeText(context, "O jogo começou a zeros!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Erro ao começar o jogo.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onFinishGameClick = {
                    scope.launch {
                        isSavingScore = true
                        val result = repository.atualizarJogo(
                            id = jogo.id,
                            resultadoCasa = scoreCasa,
                            resultadoFora = scoreFora,
                            estado = "Finalizado",
                            data = dataText,
                            hora = horaText
                        )
                        isSavingScore = false
                        if (result != null) {
                            estadoJogo = "Finalizado"
                            Toast.makeText(context, "Jogo finalizado com sucesso!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Erro ao finalizar o jogo.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                isSavingScore = isSavingScore
            )

            TabEventosEstatisticas(
                selected = tabSelecionada,
                onSelected = { tabSelecionada = it }
            )

            if (tabSelecionada == "Eventos") {
                EventosJogoContent(
                    jogo = jogo,
                    modalidade = modalidade,
                    repository = repository,
                    onEventRegistered = { tipo, equipa ->
                        val tipoUpper = tipo.uppercase()
                        val incremento = when (tipoUpper) {
                            "GOLO", "ACE", "LANCE_LIVRE" -> 1
                            "DOIS_PONTOS" -> 2
                            "TRES_PONTOS" -> 3
                            else -> 0
                        }
                        if (incremento > 0) {
                            if (equipa == "casa") {
                                scoreCasa += incremento
                            } else {
                                scoreFora += incremento
                            }
                        }
                    }
                )
            } else {
                EstatisticasJogoContent(
                    jogo = jogo,
                    modalidade = modalidade,
                    repository = repository,
                    onGuardarClick = onGuardarClick
                )
            }
        }
    }
}

@Composable
private fun TopBarEditarEstatisticas(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                Icons.Rounded.ArrowBack,
                contentDescription = null,
                tint = Color.Black
            )
        }

        TranslatedText(
            text = "Editar estatísticas",
            fontFamily = Geist,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        Surface(
            shape = RoundedCornerShape(50),
            color = Color(0xFFDCFCE7)
        ) {
            TranslatedText(
                text = "Em Direto",
                color = Color(0xFF16A34A),
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
            )
        }
    }
}

@Composable
private fun JogoHeaderCard(
    jogo: Jogo,
    modalidade: String,
    scoreCasa: Int,
    scoreFora: Int,
    estadoJogo: String,
    onCasaChange: (Int) -> Unit,
    onForaChange: (Int) -> Unit,
    dataText: String,
    onDataChange: (String) -> Unit,
    horaText: String,
    onHoraChange: (String) -> Unit,
    onSaveScoreClick: () -> Unit,
    onStartGameClick: () -> Unit,
    onFinishGameClick: () -> Unit,
    isSavingScore: Boolean
) {
    val isAgendado = estadoJogo.uppercase() == "AGENDADO" || estadoJogo.uppercase() == "AGENDADO"
    val isLive = estadoJogo.uppercase() == "EM_CURSO" || estadoJogo.uppercase() == "A DECORRER" || estadoJogo.uppercase() == "EM DIRETO"
    val isFinalizado = estadoJogo.uppercase() == "FINALIZADO"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .background(
                color = Color(0xFF111111),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Live / Estado header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            if (isLive) Color(0xFF22C55E) else if (isFinalizado) Color(0xFF9CA3AF) else Color(0xFFEF4444),
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = when {
                        isLive -> "A DECORRER"
                        isFinalizado -> "FINALIZADO"
                        else -> "AGENDADO"
                    },
                    color = Color.White.copy(alpha = 0.8f),
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }

            // Dynamic ticking clock for active match (Football / Handball / Basketball)
            if (isLive) {
                val isBasquetebol = modalidade.lowercase() == "basquetebol"
                var elapsedSeconds by remember { mutableStateOf(if (isBasquetebol) 2400 else 0) }
                
                LaunchedEffect(Unit) {
                    while (true) {

                        kotlinx.coroutines.delay(1000)
                        if (isBasquetebol) {
                            elapsedSeconds = (elapsedSeconds - 1).coerceAtLeast(0)
                        } else {
                            elapsedSeconds++
                        }
                    }
                }
                
                val min = elapsedSeconds / 60
                val sec = elapsedSeconds % 60
                val clockText = String.format("%02d:%02d", min, sec)
                
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color.White.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
                ) {
                    Text(
                        text = clockText,
                        color = Color.White,
                        fontFamily = Geist,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 3.dp)
                    )
                }
            }

            // Realtime Countdown (if state is AGENDADO)
            if (isAgendado) {
                var countdownText by remember { mutableStateOf("") }
                
                LaunchedEffect(jogo.data, jogo.hora, dataText, horaText) {
                    while (true) {
                        val now = java.util.Calendar.getInstance()
                        val matchCal = java.util.Calendar.getInstance()
                        
                        try {
                            val activeData = dataText.ifBlank { jogo.data }
                            val activeHora = horaText.ifBlank { jogo.hora }
                            val dateSplit = activeData.split("/")
                            val timeSplit = activeHora.split(":")
                            if (dateSplit.size == 3 && timeSplit.size == 2) {
                                matchCal.set(
                                    dateSplit[2].toInt(),
                                    dateSplit[1].toInt() - 1,
                                    dateSplit[0].toInt(),
                                    timeSplit[0].toInt(),
                                    timeSplit[1].toInt(),
                                    0
                                )
                                
                                val diff = matchCal.timeInMillis - now.timeInMillis
                                if (diff > 0) {
                                    val days = diff / (24 * 60 * 60 * 1000)
                                    val hours = (diff / (60 * 60 * 1000)) % 24
                                    val minutes = (diff / (60 * 1000)) % 60
                                    val seconds = (diff / 1000) % 60
                                    
                                    countdownText = if (days > 0) {
                                        "Começa em: ${days}d ${hours}h ${minutes}m"
                                    } else if (hours > 0) {
                                        "Começa em: ${hours}h ${minutes}m ${seconds}s"
                                    } else {
                                        "Começa em: ${minutes}m ${seconds}s"
                                    }
                                } else {
                                    countdownText = "Hora do jogo atingida"
                                }
                            } else {
                                countdownText = "Aguardando data/hora"
                            }
                        } catch (e: Exception) {
                            countdownText = ""
                        }
                        
                        kotlinx.coroutines.delay(1000)
                    }
                }
                
                if (countdownText.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = countdownText,
                        color = Color(0xFFEF4444),
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Score and team selector area
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Team Casa
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = jogo.casa,
                        color = Color.White,
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isLive) {
                            IconButton(
                                onClick = { onCasaChange((scoreCasa - 1).coerceAtLeast(0)) },
                                modifier = Modifier.size(28.dp).background(Color.White.copy(alpha = 0.1f), androidx.compose.foundation.shape.CircleShape)
                            ) {
                                Text("-", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                        
                        Text(
                            text = if (isAgendado) "0" else scoreCasa.toString(),
                            color = Color.White,
                            fontFamily = Bricolage,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 32.sp
                        )
                        
                        if (isLive) {
                            Spacer(modifier = Modifier.width(12.dp))
                            IconButton(
                                onClick = { onCasaChange(scoreCasa + 1) },
                                modifier = Modifier.size(28.dp).background(Color.White.copy(alpha = 0.1f), androidx.compose.foundation.shape.CircleShape)
                            ) {
                                Text("+", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                    }
                }

                // Separator
                Text(
                    text = "-",
                    color = Color.White.copy(alpha = 0.4f),
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                // Team Fora
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = jogo.fora,
                        color = Color.White,
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isLive) {
                            IconButton(
                                onClick = { onForaChange((scoreFora - 1).coerceAtLeast(0)) },
                                modifier = Modifier.size(28.dp).background(Color.White.copy(alpha = 0.1f), androidx.compose.foundation.shape.CircleShape)
                            ) {
                                Text("-", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                        
                        Text(
                            text = if (isAgendado) "0" else scoreFora.toString(),
                            color = Color.White,
                            fontFamily = Bricolage,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 32.sp
                        )
                        
                        if (isLive) {
                            Spacer(modifier = Modifier.width(12.dp))
                            IconButton(
                                onClick = { onForaChange(scoreFora + 1) },
                                modifier = Modifier.size(28.dp).background(Color.White.copy(alpha = 0.1f), androidx.compose.foundation.shape.CircleShape)
                            ) {
                                Text("+", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Schedule input fields (visible in all states, editable)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = dataText,
                    onValueChange = onDataChange,
                    label = { Text("Data (DD/MM/AAAA)", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp) },
                    placeholder = { Text("01/01/2026", color = Color.White.copy(alpha = 0.3f)) },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LMRed,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                        focusedLabelColor = LMRed,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.White.copy(alpha = 0.05f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                )

                OutlinedTextField(
                    value = horaText,
                    onValueChange = onHoraChange,
                    label = { Text("Hora (HH:MM)", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp) },
                    placeholder = { Text("12:00", color = Color.White.copy(alpha = 0.3f)) },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LMRed,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                        focusedLabelColor = LMRed,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.White.copy(alpha = 0.05f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons based on status
            when {
                isAgendado -> {
                    Button(
                        onClick = onStartGameClick,
                        enabled = !isSavingScore,
                        modifier = Modifier.fillMaxWidth().height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E)), // Green premium start button
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                    ) {
                        Text(
                            text = if (isSavingScore) "A começar jogo..." else "Começar Jogo (A Zeros)",
                            color = Color.White,
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
                
                isLive -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onSaveScoreClick,
                            enabled = !isSavingScore,
                            modifier = Modifier.weight(1f).height(40.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LMRed),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                        ) {
                            Text(
                                text = if (isSavingScore) "A guardar..." else "Guardar Placar",
                                color = Color.White,
                                fontFamily = Geist,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        Button(
                            onClick = onFinishGameClick,
                            enabled = !isSavingScore,
                            modifier = Modifier.weight(1f).height(40.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black), // Premium black end match button
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = if (isSavingScore) "A finalizar..." else "Finalizar Jogo",
                                color = Color.White,
                                fontFamily = Geist,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
                
                isFinalizado -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Jogo Terminado - Resultados Bloqueados",
                            color = Color.White.copy(alpha = 0.7f),
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TabEventosEstatisticas(
    selected: String,
    onSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 26.dp, vertical = 6.dp)
            .background(Color(0xFFE5E5E5), RoundedCornerShape(50))
            .padding(3.dp)
    ) {
        listOf("Eventos", "Estatísticas").forEach { tab ->
            val ativo = selected == tab

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (ativo) LMRed else Color.Transparent,
                        RoundedCornerShape(50)
                    )
                    .clickable { onSelected(tab) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tab,
                    color = if (ativo) Color.White else Color(0xFF6B7280),
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun EventosJogoContent(
    jogo: Jogo,
    modalidade: String,
    repository: LeagueMatchRepository,
    onEventRegistered: (String, String) -> Unit
) {
    var eventoSelecionado by remember { mutableStateOf<MatchEventType?>(null) }
    var equipaSelecionada by remember { mutableStateOf("casa") }
    var tipoAlvo by remember { mutableStateOf("equipa") }
    var jogador by remember { mutableStateOf("") }
    var minuto by remember { mutableStateOf("60") }

    var isSaving by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = androidx.compose.ui.platform.LocalContext.current

    val eventos = eventosPorModalidade(modalidade)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Tipo de evento",
            color = Color.Black,
            fontFamily = Geist,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        eventos.chunked(3).forEach { linha ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                linha.forEach { evento ->
                    EventoButton(
                        evento = evento,
                        selected = eventoSelecionado == evento,
                        onClick = { eventoSelecionado = evento },
                        modifier = Modifier.weight(1f)
                    )
                }

                repeat(3 - linha.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        Text(
            text = "Equipa",
            color = Color.Black,
            fontFamily = Geist,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            TeamSelectButton(
                text = jogo.casa,
                selected = equipaSelecionada == "casa",
                onClick = { equipaSelecionada = "casa" },
                modifier = Modifier.weight(1f)
            )

            TeamSelectButton(
                text = jogo.fora,
                selected = equipaSelecionada == "fora",
                onClick = { equipaSelecionada = "fora" },
                modifier = Modifier.weight(1f)
            )
        }

        Text(
            text = "Aplicar a",
            color = Color.Black,
            fontFamily = Geist,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            TargetButton(
                text = "Equipa",
                icon = Icons.Default.Groups,
                selected = tipoAlvo == "equipa",
                onClick = { tipoAlvo = "equipa" },
                modifier = Modifier.weight(1f)
            )

            TargetButton(
                text = "Jogador",
                icon = Icons.Default.Person,
                selected = tipoAlvo == "jogador",
                onClick = { tipoAlvo = "jogador" },
                modifier = Modifier.weight(1f)
            )
        }

        if (tipoAlvo == "jogador") {
            OutlinedTextField(
                value = jogador,
                onValueChange = { jogador = it },
                label = { Text("Jogador") },
                placeholder = { Text("Nome do jogador") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LMRed,
                    unfocusedBorderColor = Color(0xFFD1D5DB),
                    focusedLabelColor = LMRed,
                    unfocusedLabelColor = Color(0xFF6B7280),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )
        }

        OutlinedTextField(
            value = minuto,
            onValueChange = { minuto = it },
            label = { Text("Minuto") },
            leadingIcon = {
                Icon(Icons.Default.Schedule, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LMRed,
                unfocusedBorderColor = Color(0xFFD1D5DB),
                focusedLabelColor = LMRed,
                unfocusedLabelColor = Color(0xFF6B7280),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedLeadingIconColor = LMRed,
                unfocusedLeadingIconColor = Color(0xFF6B7280)
            )
        )

        Button(
            onClick = {
                val ev = eventoSelecionado
                if (ev == null) {
                    Toast.makeText(context, "Por favor, selecione um tipo de evento.", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                val minInt = minuto.toIntOrNull()
                if (minInt == null || minInt < 0) {
                    Toast.makeText(context, "Por favor, insira um minuto válido.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                scope.launch {
                    isSaving = true
                    val success = repository.registarEventoJogo(
                        partidaId = jogo.id,
                        tipo = ev.name,
                        equipa = equipaSelecionada,
                        tempo = minInt,
                        jogadorNome = if (tipoAlvo == "jogador" && jogador.isNotBlank()) jogador.trim() else null
                    )
                    isSaving = false

                    if (success) {
                        Toast.makeText(context, "Evento registado com sucesso!", Toast.LENGTH_SHORT).show()
                        onEventRegistered(ev.name, equipaSelecionada)
                        jogador = ""
                    } else {
                        Toast.makeText(context, "Erro ao registar o evento.", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            enabled = !isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LMRed)
        ) {
            Text(
                text = if (isSaving) "A registar..." else "Registar evento",
                color = Color.White,
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
private fun EventoButton(
    evento: MatchEventType,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(78.dp),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) LMRed else Color.White,
        border = BorderStroke(
            1.dp,
            if (selected) LMRed else Color(0xFFE5E7EB)
        ),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                evento.icon,
                contentDescription = null,
                tint = if (selected) Color.White else LMRed,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = evento.label,
                color = if (selected) Color.White else Color.Black,
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun EstatisticasJogoContent(
    jogo: Jogo,
    modalidade: String,
    repository: LeagueMatchRepository,
    onGuardarClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var isSaving by remember { mutableStateOf(false) }
    var mensagem by remember { mutableStateOf<String?>(null) }

    val estatisticas = remember(modalidade) {
        mutableStateListOf<EstatisticaEditavel>().apply {
            addAll(
                estatisticasPorModalidade(modalidade).map {
                    EstatisticaEditavel(
                        titulo = it.titulo,
                        casa = it.casa,
                        fora = it.fora
                    )
                }
            )
        }
    }

    LaunchedEffect(jogo.id) {
        val salvas = repository.obterEstatisticasJogo(jogo.id)
        if (salvas.isNotEmpty()) {
            estatisticas.clear()
            val grouped = salvas.groupBy { it.tipo }
            grouped.forEach { (tipo, list) ->
                val casaVal = list.firstOrNull { it.equipa.equals("casa", ignoreCase = true) }?.valor ?: 0
                val foraVal = list.firstOrNull { it.equipa.equals("fora", ignoreCase = true) }?.valor ?: 0
                estatisticas.add(EstatisticaEditavel(tipo, casaVal, foraVal))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Estatísticas do jogo",
            fontFamily = Geist,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Use os botões + e - para atualizar os valores.",
            fontFamily = Geist,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = Color(0xFF6B7280)
        )

        Spacer(modifier = Modifier.height(18.dp))

        estatisticas.forEachIndexed { index, stat ->
            StatBarEditable(
                titulo = stat.titulo,
                casa = stat.casa,
                fora = stat.fora,
                onCasaChange = { novoValor ->
                    estatisticas[index] = stat.copy(casa = novoValor.coerceAtLeast(0))
                },
                onForaChange = { novoValor ->
                    estatisticas[index] = stat.copy(fora = novoValor.coerceAtLeast(0))
                }
            )

            Spacer(modifier = Modifier.height(14.dp))
        }

        mensagem?.let {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = it,
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (it.contains("guardadas", ignoreCase = true)) Color(0xFF16A34A) else LMRed
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                scope.launch {
                    isSaving = true
                    mensagem = null

                    try {
                        val listaParaGuardar = estatisticas.toEstatisticasJogo()
                        val sucesso = repository.guardarEstatisticasJogo(
                            partidaId = jogo.id,
                            estatisticas = listaParaGuardar
                        )

                        isSaving = false

                        if (sucesso) {
                            mensagem = "Estatísticas guardadas com sucesso."
                            onGuardarClick()
                        } else {
                            mensagem = "Erro ao guardar estatísticas."
                        }
                    } catch (e: Exception) {
                        isSaving = false
                        mensagem = "Erro: ${e.message}"
                    }
                }
            },
            enabled = !isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LMRed)
        ) {
            Text(
                text = if (isSaving) "A guardar..." else "Guardar estatísticas",
                color = Color.White,
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

private fun SnapshotStateList<EstatisticaEditavel>.toEstatisticasJogo(): List<EstatisticaJogo> {
    return flatMap { stat ->
        listOf(
            EstatisticaJogo(
                tipo = stat.titulo,
                equipa = "casa",
                valor = stat.casa
            ),
            EstatisticaJogo(
                tipo = stat.titulo,
                equipa = "fora",
                valor = stat.fora
            )
        )
    }
}

@Composable
private fun StatBarEditable(
    titulo: String,
    casa: Int,
    fora: Int,
    onCasaChange: (Int) -> Unit,
    onForaChange: (Int) -> Unit
) {
    val total = (casa + fora).coerceAtLeast(1)
    val casaPeso = casa.toFloat() / total
    val foraPeso = fora.toFloat() / total

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                text = titulo,
                fontFamily = Geist,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = casa.toString(),
                    color = LMRed,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    modifier = Modifier.width(34.dp)
                )

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(14.dp)
                        .background(Color(0xFFE5E7EB), RoundedCornerShape(50))
                ) {
                    if (casa > 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(casaPeso)
                                .background(LMRed, RoundedCornerShape(50))
                        )
                    }

                    if (fora > 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(foraPeso)
                                .background(Color.Black, RoundedCornerShape(50))
                        )
                    }
                }

                Text(
                    text = fora.toString(),
                    color = Color.Black,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .width(34.dp)
                        .padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCounterBox(
                    title = "Equipa Casa",
                    value = casa,
                    onMinusClick = { onCasaChange(casa - 1) },
                    onPlusClick = { onCasaChange(casa + 1) },
                    modifier = Modifier.weight(1f)
                )

                StatCounterBox(
                    title = "Equipa Fora",
                    value = fora,
                    onMinusClick = { onForaChange(fora - 1) },
                    onPlusClick = { onForaChange(fora + 1) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatCounterBox(
    title: String,
    value: Int,
    onMinusClick: () -> Unit,
    onPlusClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFF7F7F7),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = Color(0xFF6B7280)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                SmallStatButton(
                    text = "-",
                    onClick = onMinusClick
                )

                Text(
                    text = value.toString(),
                    color = Color.Black,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                SmallStatButton(
                    text = "+",
                    onClick = onPlusClick
                )
            }
        }
    }
}

@Composable
private fun SmallStatButton(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = LMRed
    ) {
        Box(
            modifier = Modifier.size(30.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontFamily = Geist,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun TeamSelectButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) LMRed else Color.White,
        border = BorderStroke(
            1.dp,
            if (selected) LMRed else Color(0xFFE5E7EB)
        ),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Groups,
                contentDescription = null,
                tint = if (selected) Color.White else LMRed,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = text,
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = if (selected) Color.White else Color.Black,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun TargetButton(
    text: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) LMRed else Color.White,
        border = BorderStroke(
            1.dp,
            if (selected) LMRed else Color(0xFFE5E7EB)
        ),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (selected) Color.White else LMRed,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = text,
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = if (selected) Color.White else Color.Black
            )
        }
    }
}

private data class EstatisticaEditavel(
    val titulo: String,
    val casa: Int,
    val fora: Int
)