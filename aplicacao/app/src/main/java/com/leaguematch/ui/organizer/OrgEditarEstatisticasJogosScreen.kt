package com.leaguematch.ui.organizer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.BuildConfig
import com.leaguematch.data.remote.model.EstatisticaJogo
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.data.repository.SupabaseLeagueMatchRepository
import com.leaguematch.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun OrgEditarEstatisticasJogoScreen(
    jogo: Jogo,
    modalidade: String,
    onBackClick: () -> Unit,
    onGuardarClick: () -> Unit
) {
    var tabSelecionada by remember { mutableStateOf("Eventos") }

    Scaffold(containerColor = Color(0xFFF7F7F7)) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF7F7F7))
        ) {
            TopBarEditarEstatisticas(onBackClick)
            JogoHeaderCard(jogo)

            TabEventosEstatisticas(
                selected = tabSelecionada,
                onSelected = { tabSelecionada = it }
            )

            if (tabSelecionada == "Eventos") {
                EventosJogoContent(
                    jogo = jogo,
                    modalidade = modalidade
                )
            } else {
                EstatisticasJogoContent(
                    jogo = jogo,
                    modalidade = modalidade,
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

        Text(
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
            Text(
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
private fun JogoHeaderCard(jogo: Jogo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .height(150.dp)
            .background(
                color = Color(0xFF111111),
                shape = RoundedCornerShape(18.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                shape = RoundedCornerShape(50),
                color = Color.White.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
            ) {
                Text(
                    text = "60:23",
                    color = Color.White,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 34.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "${jogo.casa} VS ${jogo.fora}",
                color = Color.White,
                fontFamily = Geist,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White.copy(alpha = 0.10f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.20f))
            ) {
                Text(
                    text = "${jogo.resultadoCasa} - ${jogo.resultadoFora}",
                    color = Color.White,
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 34.sp,
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 2.dp)
                )
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
    modalidade: String
) {
    var eventoSelecionado by remember { mutableStateOf<MatchEventType?>(null) }
    var equipaSelecionada by remember { mutableStateOf("casa") }
    var tipoAlvo by remember { mutableStateOf("equipa") }
    var jogador by remember { mutableStateOf("") }
    var minuto by remember { mutableStateOf("60") }

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
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LMRed)
        ) {
            Text(
                text = "Registar evento",
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

                    val repository = SupabaseLeagueMatchRepository(
                        supabaseUrl = BuildConfig.SUPABASE_URL,
                        anonKey = BuildConfig.SUPABASE_ANON_KEY
                    )

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
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(casaPeso)
                            .background(LMRed, RoundedCornerShape(50))
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(foraPeso)
                            .background(Color.Black, RoundedCornerShape(50))
                    )
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

private data class EstatisticaInicial(
    val titulo: String,
    val casa: Int,
    val fora: Int
)

private data class EstatisticaEditavel(
    val titulo: String,
    val casa: Int,
    val fora: Int
)

private fun estatisticasPorModalidade(modalidade: String): List<EstatisticaInicial> {
    val lista = mutableListOf<EstatisticaInicial>()

    if (modalidadeUsaPosseBola(modalidade)) {
        lista.add(EstatisticaInicial("Posse de Bola", 57, 43))
    }

    when (modalidade.lowercase()) {
        "futebol" -> {
            lista.add(EstatisticaInicial("Remates", 9, 4))
            lista.add(EstatisticaInicial("Remates à baliza", 8, 2))
            lista.add(EstatisticaInicial("Cantos", 5, 7))
            lista.add(EstatisticaInicial("Faltas", 3, 7))
            lista.add(EstatisticaInicial("Cartões amarelos", 5, 0))
            lista.add(EstatisticaInicial("Cartões vermelhos", 1, 0))
        }

        "andebol" -> {
            lista.add(EstatisticaInicial("Remates", 18, 14))
            lista.add(EstatisticaInicial("Defesas", 6, 8))
            lista.add(EstatisticaInicial("Faltas", 5, 7))
            lista.add(EstatisticaInicial("Cartões amarelos", 2, 1))
            lista.add(EstatisticaInicial("Cartões vermelhos", 0, 1))
        }

        "basquetebol" -> {
            lista.add(EstatisticaInicial("Lançamentos 2 pts", 12, 10))
            lista.add(EstatisticaInicial("Lançamentos 3 pts", 5, 7))
            lista.add(EstatisticaInicial("Lances livres", 8, 4))
            lista.add(EstatisticaInicial("Faltas", 6, 9))
        }

        "padel" -> {
            lista.add(EstatisticaInicial("Aces", 3, 5))
            lista.add(EstatisticaInicial("Break points", 2, 4))
            lista.add(EstatisticaInicial("Erros", 6, 3))
        }

        "ténis", "tenis" -> {
            lista.add(EstatisticaInicial("Aces", 4, 6))
            lista.add(EstatisticaInicial("Break points", 2, 3))
            lista.add(EstatisticaInicial("Erros", 5, 4))
        }

        else -> {
            lista.add(EstatisticaInicial("Faltas", 0, 0))
        }
    }

    return lista
}