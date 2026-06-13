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
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.Equipa
import com.leaguematch.data.remote.model.TeamCode
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.ui.components.TranslatedText
import com.leaguematch.ui.theme.Geist
import com.leaguematch.ui.theme.LMBorder
import com.leaguematch.ui.theme.LMGray100
import com.leaguematch.ui.theme.LMGray300
import com.leaguematch.ui.theme.LMGray400
import com.leaguematch.ui.theme.LMGray50
import com.leaguematch.ui.theme.LMGray500
import com.leaguematch.ui.theme.LMGray600
import com.leaguematch.ui.theme.LMInk
import com.leaguematch.ui.theme.LMRed
import com.leaguematch.ui.theme.LMRed50
import com.leaguematch.ui.theme.LMWhite

@Composable
fun OrgGerirJogadoresScreen(
    equipa: Equipa,
    jogadores: List<Utilizador>,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onRemoverJogador: (Utilizador) -> Unit
) {
    var jogadorParaRemover by remember { mutableStateOf<Utilizador?>(null) }

    if (jogadorParaRemover != null) {
        AlertDialog(
            onDismissRequest = { jogadorParaRemover = null },
            title = {
                TranslatedText(
                    text = "Remover jogador?",
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                TranslatedText(
                    text = "Queres remover \"${jogadorParaRemover!!.nome}\" da equipa?",
                    fontFamily = Geist,
                    fontSize = 13.sp,
                    color = LMGray600
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRemoverJogador(jogadorParaRemover!!)
                        jogadorParaRemover = null
                    }
                ) {
                    TranslatedText(
                        text = "Remover",
                        color = LMRed,
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { jogadorParaRemover = null }) {
                    TranslatedText(
                        text = "Cancelar",
                        fontFamily = Geist,
                        color = LMGray500
                    )
                }
            },
            shape = RoundedCornerShape(18.dp)
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
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = null,
                        tint = LMInk
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    TranslatedText(
                        text = "Jogadores",
                        fontFamily = Geist,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = LMInk
                    )

                    Text(
                        text = equipa.nome,
                        fontFamily = Geist,
                        fontSize = 12.sp,
                        color = LMGray500
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = LMRed50,
                border = BorderStroke(1.dp, LMBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    TranslatedText(
                        text = "CÓDIGO DE INTEGRAÇÃO",
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = LMGray500,
                        letterSpacing = 0.4.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = TeamCode.encode(equipa.id),
                        fontFamily = Geist,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = LMInk,
                        letterSpacing = 3.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    TranslatedText(
                        text = "Partilha este código com os jogadores. Eles entram pela secção \"Integrar Equipa\" da app.",
                        fontFamily = Geist,
                        fontSize = 12.sp,
                        color = LMGray600
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            TranslatedText(
                text = "JOGADORES · ${jogadores.size}",
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = LMGray500,
                letterSpacing = 0.4.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = LMRed)
                }
            } else if (jogadores.isEmpty()) {
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
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = LMGray300,
                            modifier = Modifier.size(40.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        TranslatedText(
                            text = "Sem jogadores na equipa.",
                            fontFamily = Geist,
                            fontSize = 13.sp,
                            color = LMGray500
                        )

                        TranslatedText(
                            text = "Partilha o código acima para os jogadores se inscreverem.",
                            fontFamily = Geist,
                            fontSize = 12.sp,
                            color = LMGray400
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    jogadores.forEach { jogador ->
                        JogadorListItem(
                            jogador = jogador,
                            onRemover = { jogadorParaRemover = jogador }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun JogadorListItem(
    jogador: Utilizador,
    onRemover: () -> Unit
) {
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
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(LMGray100, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = LMGray500,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = jogador.nome,
                    fontFamily = Geist,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = LMInk
                )

                Text(
                    text = jogador.email,
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }

            IconButton(
                onClick = onRemover,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Remover",
                    tint = LMGray400,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}