/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: CreateTournamentStep2Screen.kt
 * Tipo: Interface (Compose View) do Organizador
 *
 * Descrição:
 * Este ficheiro define um ecrã da área do Organizador em Jetpack Compose.\n * Fornece interface e lógica visual para criar torneios, gerir equipas, registar e editar jogos e estatísticas.
 */
package com.leaguematch.ui.organizer // Define o pacote deste ficheiro de código

import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.clickable // Importa dependência / biblioteca necessária
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
import androidx.compose.material.icons.filled.CheckCircle // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.EmojiEvents // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Lock // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Public // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsScore // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Surface // Importa dependência / biblioteca necessária
import androidx.compose.material3.Switch // Importa dependência / biblioteca necessária
import androidx.compose.material3.SwitchDefaults // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.remember // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Brush // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.vector.ImageVector // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Bricolage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária

@Composable
fun CreateTournamentStep2Screen( // Declaração de função / método de lógica
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
    var publico by remember { mutableStateOf(true) } // Declara estado mutável local do Compose
    var jogosIdaVolta by remember { mutableStateOf(false) } // Declara estado mutável local do Compose
    var pontosVitoria by remember { mutableStateOf(3) } // Declara estado mutável local do Compose

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                verticalAlignment = Alignment.CenterVertically
            ) {
                TranslatedText(
                    text = "‹",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = LMInk,
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .clickable { onBackClick() }
                        .padding(end = 12.dp)
                )

                TranslatedText(
                    text = "Confirmar Torneio",
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = LMInk,
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                TranslatedText(
                    text = "Cancelar",
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = LMGray500,
                    modifier = Modifier.clickable { onCancelClick() } // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            StepHeaderCard()

            Spacer(modifier = Modifier.height(20.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            TranslatedText(
                text = "Resumo",
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

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

            Spacer(modifier = Modifier.height(22.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            TranslatedText(
                text = "Configurações",
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            PointsCard(
                pontos = pontosVitoria,
                onMinus = {
                    if (pontosVitoria > 1) pontosVitoria-- // Estrutura de decisão condicional principal
                },
                onPlus = {
                    if (pontosVitoria < 5) pontosVitoria++ // Estrutura de decisão condicional principal
                }
            )

            Spacer(modifier = Modifier.height(24.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .weight(1f)
                        .height(54.dp)
                        .clickable { onBackClick() },
                    shape = RoundedCornerShape(16.dp),
                    color = LMWhite,
                    border = BorderStroke(1.dp, Color(0xFFE2E2E7))
                ) {
                    Box(contentAlignment = Alignment.Center) { // Contentor Compose: Sobrepõe os elementos filhos
                        TranslatedText(
                            text = "Anterior",
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = LMInk
                        )
                    }
                }

                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
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

            Spacer(modifier = Modifier.height(24.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
        }
    }
}

@Composable
private fun StepHeaderCard() { // Declaração de função / método de lógica
    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFFFF5F6),
        border = BorderStroke(1.dp, Color(0xFFFFCCD3))
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(42.dp)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(LMRed, Color(0xFFC41326))
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon( // Componente Compose: Desenha um ícone vetorial
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = LMWhite,
                    modifier = Modifier.size(22.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column { // Contentor Compose: Alinha os filhos numa coluna vertical
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
private fun SummaryCard( // Declaração de função / método de lógica
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
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier.padding(14.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) { // Contentor Compose: Alinha os filhos numa linha horizontal
                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .size(46.dp)
                        .background(
                            brush = Brush.linearGradient(
                                listOf(LMRed, Color(0xFFC41326))
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = LMWhite,
                        modifier = Modifier.size(24.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                }

                Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Column { // Contentor Compose: Alinha os filhos numa coluna vertical
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

            Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            SummaryRow("Data início", dataInicio.ifBlank { "Não definida" })
            SummaryRow("Data fim", dataFim.ifBlank { "Não definida" })
            SummaryRow("Máximo de equipas", maxEquipas.ifBlank { "Não definido" })

            if (descricao.isNotBlank()) { // Estrutura de decisão condicional principal
                SummaryRow("Descrição", descricao)
            }

            if (regras.isNotBlank()) { // Estrutura de decisão condicional principal
                SummaryRow("Regras", regras)
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) { // Declaração de função / método de lógica
    Column( // Contentor Compose: Alinha os filhos numa coluna vertical
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
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

        Spacer(modifier = Modifier.height(2.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

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
private fun OptionSwitchCard( // Declaração de função / método de lógica
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .clickable { onCheckedChange(!checked) }
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(42.dp)
                    .background(
                        color = Color(0xFFFFEEF1),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon( // Componente Compose: Desenha um ícone vetorial
                    imageVector = icon,
                    contentDescription = null,
                    tint = LMRed,
                    modifier = Modifier.size(21.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
            ) {
                TranslatedText(
                    text = title,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = LMInk
                )

                Spacer(modifier = Modifier.height(2.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

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
private fun PointsCard( // Declaração de função / método de lógica
    pontos: Int,
    onMinus: () -> Unit,
    onPlus: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(18.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(42.dp)
                    .background(
                        color = Color(0xFFFFEEF1),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon( // Componente Compose: Desenha um ícone vetorial
                    imageVector = Icons.Default.SportsScore,
                    contentDescription = null,
                    tint = LMRed,
                    modifier = Modifier.size(21.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
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

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                verticalAlignment = Alignment.CenterVertically
            ) {
                PointsButton("-", onMinus)

                TranslatedText(
                    text = pontos.toString(),
                    modifier = Modifier.padding(horizontal = 14.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
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
private fun PointsButton( // Declaração de função / método de lógica
    text: String,
    onClick: () -> Unit // Callback: Define a ação executada ao clicar no componente
) {
    Surface(
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .size(34.dp)
            .clickable { onClick() }, // Callback: Define a ação executada ao clicar no componente
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF3F3F5)
    ) {
        Box(contentAlignment = Alignment.Center) { // Contentor Compose: Sobrepõe os elementos filhos
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