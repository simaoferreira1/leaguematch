package com.leaguematch.ui.organizer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.ui.components.TranslatedText
import com.leaguematch.ui.theme.Bricolage
import com.leaguematch.ui.theme.Geist
import com.leaguematch.ui.theme.LMGray500
import com.leaguematch.ui.theme.LMInk
import com.leaguematch.ui.theme.LMRed
import com.leaguematch.ui.theme.LMWhite

@Composable
fun CreateTournamentStep2Screen(
    nome: String,
    modalidade: String,
    formato: String,
    dataInicio: String,
    dataFim: String,
    maxEquipas: String,
    descricao: String,
    regras: String,
    onBackClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    onCreateClick: (
        publico: Boolean,
        jogosIdaVolta: Boolean,
        pontosVitoria: Int
    ) -> Unit = { _, _, _ -> }
) {
    var publico by remember { mutableStateOf(true) }
    var jogosIdaVolta by remember { mutableStateOf(false) }
    var pontosVitoria by remember { mutableStateOf(3) }

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
                TranslatedText(
                    text = "‹",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = LMInk,
                    modifier = Modifier
                        .clickable { onBackClick() }
                        .padding(end = 12.dp)
                )

                TranslatedText(
                    text = "Confirmar Torneio",
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = LMInk,
                    modifier = Modifier.weight(1f)
                )

                TranslatedText(
                    text = "Cancelar",
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = LMGray500,
                    modifier = Modifier.clickable { onCancelClick() }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            StepHeaderCard()

            Spacer(modifier = Modifier.height(20.dp))

            TranslatedText(
                text = "Resumo",
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(10.dp))

            SummaryCard(
                nome = nome,
                modalidade = modalidade,
                formato = formato,
                dataInicio = dataInicio,
                dataFim = dataFim,
                maxEquipas = maxEquipas,
                descricao = descricao,
                regras = regras
            )

            Spacer(modifier = Modifier.height(22.dp))

            TranslatedText(
                text = "Configurações",
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(14.dp))

            PointsCard(
                pontos = pontosVitoria,
                onMinus = {
                    if (pontosVitoria > 1) pontosVitoria--
                },
                onPlus = {
                    if (pontosVitoria < 5) pontosVitoria++
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

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
                        TranslatedText(
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
                            onCreateClick(
                                publico,
                                jogosIdaVolta,
                                pontosVitoria
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    TranslatedText(
                        text = "Criar torneio",
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
private fun StepHeaderCard() {
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
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = LMWhite,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                TranslatedText(
                    text = "Confirmação final",
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = LMInk
                )

                TranslatedText(
                    text = "Passo 2 de 2 — Rever e configurar",
                    fontFamily = Geist,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }
        }
    }
}

@Composable
private fun SummaryCard(
    nome: String,
    modalidade: String,
    formato: String,
    dataInicio: String,
    dataFim: String,
    maxEquipas: String,
    descricao: String,
    regras: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
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
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    TranslatedText(
                        text = nome.ifBlank { "Sem nome definido" },
                        fontFamily = Geist,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = LMInk
                    )

                    TranslatedText(
                        text = "$modalidade · $formato",
                        fontFamily = Geist,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = LMGray500
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            SummaryRow("Data início", dataInicio.ifBlank { "Não definida" })
            SummaryRow("Data fim", dataFim.ifBlank { "Não definida" })
            SummaryRow("Máximo de equipas", maxEquipas.ifBlank { "Não definido" })

            if (descricao.isNotBlank()) {
                SummaryRow("Descrição", descricao)
            }

            if (regras.isNotBlank()) {
                SummaryRow("Regras", regras)
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        TranslatedText(
            text = label.uppercase(),
            fontFamily = Geist,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 10.sp,
            color = LMGray500
        )

        Spacer(modifier = Modifier.height(2.dp))

        TranslatedText(
            text = value,
            fontFamily = Geist,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = LMInk
        )
    }
}

@Composable
private fun OptionSwitchCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .clickable { onCheckedChange(!checked) }
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color = Color(0xFFFFEEF1),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = LMRed,
                    modifier = Modifier.size(21.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                TranslatedText(
                    text = title,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = LMInk
                )

                Spacer(modifier = Modifier.height(2.dp))

                TranslatedText(
                    text = subtitle,
                    fontFamily = Geist,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = LMWhite,
                    checkedTrackColor = LMRed,
                    uncheckedThumbColor = LMWhite,
                    uncheckedTrackColor = Color(0xFFD4D4DD)
                )
            )
        }
    }
}

@Composable
private fun PointsCard(
    pontos: Int,
    onMinus: () -> Unit,
    onPlus: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color = Color(0xFFFFEEF1),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SportsScore,
                    contentDescription = null,
                    tint = LMRed,
                    modifier = Modifier.size(21.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                TranslatedText(
                    text = "Pontos por vitória",
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = LMInk
                )

                TranslatedText(
                    text = "Valor usado na classificação.",
                    fontFamily = Geist,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = LMGray500
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                PointsButton("-", onMinus)

                TranslatedText(
                    text = pontos.toString(),
                    modifier = Modifier.padding(horizontal = 14.dp),
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = LMInk
                )

                PointsButton("+", onPlus)
            }
        }
    }
}

@Composable
private fun PointsButton(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(34.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF3F3F5)
    ) {
        Box(contentAlignment = Alignment.Center) {
            TranslatedText(
                text = text,
                fontFamily = Geist,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = LMInk
            )
        }
    }
}