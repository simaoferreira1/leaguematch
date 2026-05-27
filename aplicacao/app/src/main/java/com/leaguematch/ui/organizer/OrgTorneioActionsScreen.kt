package com.leaguematch.ui.organizer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.DetalheTorneio
import com.leaguematch.ui.components.OrganizerBottomBar
import com.leaguematch.ui.theme.*

private data class AcaoCard(
    val titulo: String,
    val subtitulo: String,
    val icon: ImageVector,
    val iconColor: Color,
    val iconBg: Color,
    val onClick: () -> Unit
)

@Composable
fun OrgTorneioActionsScreen(
    detalhe: DetalheTorneio?,
    onBackClick: () -> Unit,
    onCriarJogoClick: () -> Unit,
    onVerJogosClick: () -> Unit,
    onVerClassificacaoClick: () -> Unit,
    onEditarTorneio: (nome: String, regras: String, formato: String) -> Unit,
    onRemoverTorneio: (id: Int) -> Unit,
    onGerirEquipasClick: () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val torneio = detalhe?.torneio
    var mostrarEdicao by remember { mutableStateOf(false) }
    var mostrarRemocao by remember { mutableStateOf(false) }

    if (mostrarEdicao && torneio != null) {
        var novoNome by remember { mutableStateOf(torneio.nome) }
        var novasRegras by remember { mutableStateOf(torneio.regras) }
        var novoFormato by remember { mutableStateOf(torneio.formato) }

        AlertDialog(
            onDismissRequest = { mostrarEdicao = false },
            title = {
                Text("Editar Torneio", fontFamily = Bricolage, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = LMInk)
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedTextField(
                        value = novoNome,
                        onValueChange = { novoNome = it },
                        label = { Text("Nome do Torneio", fontFamily = Geist) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LMInk,
                            unfocusedBorderColor = LMBorder
                        ),
                        singleLine = true
                    )

                    Column {
                        Text(
                            "Formato do Torneio",
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = LMGray500,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Liga", "Eliminatória", "Grupos").forEach { f ->
                                val selected = novoFormato.equals(f, ignoreCase = true)
                                val bg = if (selected) LMInk else LMGray50
                                val fg = if (selected) LMWhite else LMGray600
                                val border = if (selected) BorderStroke(0.dp, Color.Transparent) else BorderStroke(1.dp, LMBorder)

                                Surface(
                                    onClick = { novoFormato = f },
                                    shape = RoundedCornerShape(10.dp),
                                    color = bg,
                                    border = border,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            f,
                                            fontFamily = Geist,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = fg
                                        )
                                    }
                                }
                            }
                        }
                    }


                    OutlinedTextField(
                        value = novasRegras,
                        onValueChange = { novasRegras = it },
                        label = { Text("Regras do Torneio", fontFamily = Geist) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LMInk,
                            unfocusedBorderColor = LMBorder
                        ),
                        maxLines = 4
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (novoNome.isNotBlank() && novoFormato.isNotBlank()) {
                            onEditarTorneio(novoNome, novasRegras, novoFormato)
                            mostrarEdicao = false
                        }
                    },
                    enabled = novoNome.isNotBlank() && novoFormato.isNotBlank()
                ) {
                    Text("Guardar", color = LMInk, fontFamily = Geist, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarEdicao = false }) {
                    Text("Cancelar", fontFamily = Geist, color = LMGray500)
                }
            },
            shape = RoundedCornerShape(18.dp)
        )
    }

    if (mostrarRemocao && torneio != null) {
        AlertDialog(
            onDismissRequest = { mostrarRemocao = false },
            title = {
                Text("Remover Torneio?", fontFamily = Geist, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Text(
                    "Tens a certeza que queres remover definitivamente o torneio \"${torneio.nome}\"? Esta ação irá eliminar todas as equipas e jogos associados e é irreversível.",
                    fontFamily = Geist, fontSize = 13.sp, color = LMGray600
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onRemoverTorneio(torneio.id)
                    mostrarRemocao = false
                }) {
                    Text("Remover", color = LMRed, fontFamily = Geist, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarRemocao = false }) {
                    Text("Cancelar", fontFamily = Geist, color = LMGray500)
                }
            },
            shape = RoundedCornerShape(18.dp)
        )
    }

    Scaffold(
        bottomBar = bottomBar,
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        if (torneio == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Torneio não encontrado.", color = LMGray500)
            }
            return@Scaffold
        }

        val acoes = listOf(
            AcaoCard(
                titulo = "Criar Jogo",
                subtitulo = "Agendar nova partida no torneio",
                icon = Icons.Default.AddCircle,
                iconColor = Color(0xFF16A34A),
                iconBg = Color(0xFFDCFCE7),
                onClick = onCriarJogoClick
            ),
            AcaoCard(
                titulo = "Ver Jogos",
                subtitulo = "Consultar todas as partidas",
                icon = Icons.Default.SportsScore,
                iconColor = Color(0xFF2563EB),
                iconBg = Color(0xFFDBEAFE),
                onClick = onVerJogosClick
            ),
            AcaoCard(
                titulo = "Classificação",
                subtitulo = "Tabela classificativa do torneio",
                icon = Icons.Default.FormatListNumbered,
                iconColor = Color(0xFF7C3AED),
                iconBg = Color(0xFFEDE9FE),
                onClick = onVerClassificacaoClick
            ),
            AcaoCard(
                titulo = "Gerir Equipas",
                subtitulo = "Adicionar ou remover equipas",
                icon = Icons.Default.Groups,
                iconColor = LMRed,
                iconBg = LMRed50,
                onClick = onGerirEquipasClick
            ),
            AcaoCard(
                titulo = "Editar Torneio",
                subtitulo = "Nome, regras e formato",
                icon = Icons.Default.Edit,
                iconColor = LMInk,
                iconBg = LMGray100,
                onClick = { mostrarEdicao = true }
            ),
            AcaoCard(
                titulo = "Remover Torneio",
                subtitulo = "Apagar definitivamente este torneio",
                icon = Icons.Default.DeleteForever,
                iconColor = LMRed,
                iconBg = LMRed50,
                onClick = { mostrarRemocao = true }
            ),
        )

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
                Text(
                    text = torneio.nome,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = LMInk
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TorneioHeroCard(torneio = torneio, jogos = detalhe.jogos.size)

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "AÇÕES DO ORGANIZADOR",
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = LMGray500,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                acoes.forEach { acao ->
                    AcaoCardItem(acao = acao)
                }
            }
        }
    }
}

@Composable
private fun TorneioHeroCard(torneio: com.leaguematch.data.remote.model.Torneio, jogos: Int) {
    val estadoLower = torneio.estado.lowercase()
    val (estadoBg, estadoText) = when {
        estadoLower.contains("decorrer") || estadoLower.contains("progresso") ->
            Color(0xFFDCFCE7) to Color(0xFF15803D)
        estadoLower.contains("iniciar") ->
            Color(0xFFFEF3C7) to Color(0xFFD97706)
        else -> LMGray100 to LMGray500
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = LMInk,
        shadowElevation = 2.dp
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = 30.dp)
                    .background(
                        brush = Brush.radialGradient(listOf(Color.White.copy(alpha = 0.07f), Color.Transparent)),
                        shape = RoundedCornerShape(50)
                    )
            )
            Column(modifier = Modifier.padding(18.dp)) {
                Surface(
                    shape = RoundedCornerShape(99.dp),
                    color = estadoBg
                ) {
                    Text(
                        text = torneio.estado,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = estadoText
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = torneio.nome,
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = LMWhite,
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${torneio.formato} · ${torneio.modalidade}",
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMWhite.copy(alpha = 0.55f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    listOf(
                        "Equipas" to torneio.equipas.toString(),
                        "Jogos" to jogos.toString()
                    ).forEach { (label, valor) ->
                        Column(
                            modifier = Modifier
                                .background(LMWhite.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = valor,
                                fontFamily = Bricolage,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                color = LMWhite
                            )
                            Text(
                                text = label.uppercase(),
                                fontFamily = Geist,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                color = LMWhite.copy(alpha = 0.6f),
                                letterSpacing = 0.4.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AcaoCardItem(acao: AcaoCard) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { acao.onClick() },
        shape = RoundedCornerShape(16.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, LMBorder),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(acao.iconBg, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = acao.icon,
                    contentDescription = null,
                    tint = acao.iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = acao.titulo,
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = LMInk
                )
                Text(
                    text = acao.subtitulo,
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    color = LMGray500,
                    modifier = Modifier.padding(top = 1.dp)
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = LMGray400,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
