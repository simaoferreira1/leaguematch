package com.leaguematch.ui.admin

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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.ui.components.AdminBottomBar
import com.leaguematch.ui.components.CardWrapper
import com.leaguematch.ui.components.PrimaryBtn
import com.leaguematch.ui.components.TopBar
import com.leaguematch.ui.components.TranslatedText
import com.leaguematch.ui.theme.Bricolage
import com.leaguematch.ui.theme.Geist
import com.leaguematch.ui.theme.LMBorder
import com.leaguematch.ui.theme.LMGray100
import com.leaguematch.ui.theme.LMGray300
import com.leaguematch.ui.theme.LMGray500
import com.leaguematch.ui.theme.LMInk
import com.leaguematch.ui.theme.LMRed
import com.leaguematch.ui.theme.LMWhite
import com.leaguematch.ui.theme.LeagueMatchTheme

// Ecrã de gestão das preferências de notificações do administrador
@Composable
fun GestaoNotificacoesScreen(
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onUtilizadoresClick: () -> Unit = {},
    onTorneiosClick: () -> Unit = {},
    onGraficosClick: () -> Unit = {},
    onDefinicoesClick: () -> Unit = {}
) {
    // Estados que controlam quais os tipos de notificações ativos
    var notificacoesGerais by remember { mutableStateOf(true) }
    var notificacoesTorneios by remember { mutableStateOf(true) }
    var notificacoesJogos by remember { mutableStateOf(true) }
    var notificacoesAlertas by remember { mutableStateOf(false) }

    // Calcula quantas categorias de notificações estão atualmente ativadas
    val totalAtivas = listOf(
        notificacoesGerais,
        notificacoesTorneios,
        notificacoesJogos,
        notificacoesAlertas
    ).count { it }

    // Estrutura principal da página com barra de navegação inferior
    Scaffold(
        bottomBar = {
            AdminBottomBar(
                selectedItem = "definicoes",
                onHomeClick = onHomeClick,
                onUtilizadoresClick = onUtilizadoresClick,
                onTorneiosClick = onTorneiosClick,
                onGraficosClick = onGraficosClick,
                onDefinicoesClick = onDefinicoesClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 12.dp)
        ) {
            // Barra superior com título da página e botão de voltar
            TopBar(
                title = "Notificações",
                back = true,
                onBackClick = onBackClick
            )

            // Cartão que apresenta o número total de notificações ativas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
                    .background(LMInk, RoundedCornerShape(16.dp))
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        TranslatedText(
                            text = "NOTIFICAÇÕES ATIVAS",
                            color = LMWhite.copy(alpha = 0.6f),
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 0.4.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "$totalAtivas / 4",
                            color = LMWhite,
                            fontFamily = Bricolage,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        TranslatedText(
                            text = "Canais de alerta ativados",
                            color = LMWhite.copy(alpha = 0.8f),
                            fontFamily = Geist,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = LMWhite.copy(alpha = 0.25f),
                        modifier = Modifier.size(52.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Options Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
            ) {
                // Área onde são apresentadas todas as opções de notificações
                TranslatedText(
                    text = "CANAIS E PREFERÊNCIAS",
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = LMGray500,
                    letterSpacing = 0.4.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                CardWrapper(
                    modifier = Modifier.fillMaxWidth(),
                    pad = 0.dp
                ) {
                    Column {
                        // Ativa ou desativa notificações gerais da aplicação
                        SwitchRow(
                            icon = Icons.Default.Notifications,
                            title = "Notificações gerais",
                            description = "Avisos importantes sobre a aplicação.",
                            checked = notificacoesGerais,
                            onCheckedChange = { notificacoesGerais = it }
                        )
                        HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = LMBorder, thickness = 1.dp)

                        // Controla notificações relacionadas com torneios
                        SwitchRow(
                            icon = Icons.Default.SportsSoccer,
                            title = "Torneios",
                            description = "Novos torneios, alterações e estados.",
                            checked = notificacoesTorneios,
                            onCheckedChange = { notificacoesTorneios = it }
                        )
                        HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = LMBorder, thickness = 1.dp)

                        // Controla notificações relacionadas com jogos e resultados
                        SwitchRow(
                            icon = Icons.Default.NotificationsActive,
                            title = "Jogos",
                            description = "Resultados, horários e atualizações.",
                            checked = notificacoesJogos,
                            onCheckedChange = { notificacoesJogos = it }
                        )
                        HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = LMBorder, thickness = 1.dp)

                        // Controla notificações críticas e alertas do sistema
                        SwitchRow(
                            icon = Icons.Default.Warning,
                            title = "Alertas do sistema",
                            description = "Problemas, denúncias ou avisos críticos.",
                            checked = notificacoesAlertas,
                            onCheckedChange = { notificacoesAlertas = it }
                        )
                    }
                }
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 24.dp)
            ) {
                // Guarda as preferências selecionadas pelo utilizador
                PrimaryBtn(
                    onClick = { },
                    size = "lg"
                ) {
                    TranslatedText("Guardar preferências", color = LMWhite)
                }
            }
        }
    }
}

// Componente reutilizável que apresenta uma opção com descrição e interruptor
@Composable
fun SwitchRow(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    // Estrutura horizontal que organiza ícone, texto e switch
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Área visual que apresenta o ícone da categoria
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(LMGray100, RoundedCornerShape(9.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = LMInk,
                modifier = Modifier.size(16.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))

        // Informações da categoria de notificações
        Column(
            modifier = Modifier.weight(1f)
        ) {
            TranslatedText(
                text = title,
                fontFamily = Geist,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = LMInk
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = description,
                fontFamily = Geist,
                fontSize = 11.sp,
                color = LMGray500
            )
        }
        
        Spacer(modifier = Modifier.width(10.dp))

        // Interruptor responsável por ativar ou desativar a opção
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = LMWhite,
                checkedTrackColor = LMRed,
                uncheckedThumbColor = LMWhite,
                uncheckedTrackColor = LMGray300,
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GestaoNotificacoesScreenPreview() {
    LeagueMatchTheme {
        GestaoNotificacoesScreen()
    }
}