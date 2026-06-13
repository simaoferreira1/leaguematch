package com.leaguematch.ui.participant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.SportsHandball
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leaguematch.data.remote.model.ConfiguracaoNotificacoes
import com.leaguematch.ui.components.ParticipantBottomBar
import com.leaguematch.ui.components.TranslatedText
import com.leaguematch.ui.theme.RedDark
import com.leaguematch.ui.theme.RedPrimary

@Composable
fun ParticipantNotificationsScreen(
    configuracao: ConfiguracaoNotificacoes,
    onGuardarConfiguracao: (ConfiguracaoNotificacoes) -> Unit,
    onHomeClick: () -> Unit,
    onTorneiosClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEquipaClick: () -> Unit,
    onEstatisticasClick: () -> Unit,
    onPerfilClick: () -> Unit,
    onAbrirInbox: () -> Unit = {}
) {
    var configuracaoAtual by remember(configuracao) {
        mutableStateOf(configuracao)
    }

    fun atualizarConfiguracao(novaConfiguracao: ConfiguracaoNotificacoes) {
        configuracaoAtual = novaConfiguracao
        onGuardarConfiguracao(novaConfiguracao)
    }

    Scaffold(
        containerColor = Color(0xFFF6F6F8),
        bottomBar = {
            ParticipantBottomBar(
                selectedItem = "perfil",
                onHomeClick = onHomeClick,
                onTorneiosClick = onTorneiosClick,
                onJogosClick = onJogosClick,
                onEquipaClick = onEquipaClick,
                onEstatisticasClick = onEstatisticasClick,
                onPerfilClick = onPerfilClick
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            ParticipantNotificacoesHeader()

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onAbrirInbox,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RedPrimary
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    TranslatedText(
                        text = "Ver caixa de entrada",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            ParticipantNotificationSectionCard(title = "Configuração Geral") {
                ParticipantNotificationSwitchRow(
                    title = "Notificações dos jogos",
                    description = "Alertas dos torneios onde estás inscrito",
                    checked = configuracaoAtual.notificacoesJogos,
                    onCheckedChange = {
                        atualizarConfiguracao(
                            configuracaoAtual.copy(notificacoesJogos = it)
                        )
                    },
                    icon = Icons.Default.Notifications
                )

                ParticipantNotificationDivider()

                ParticipantNotificationSwitchRow(
                    title = "Notificações de golos",
                    description = "Aviso quando houver golos nos teus torneios",
                    checked = configuracaoAtual.notificacoesGolos,
                    onCheckedChange = {
                        atualizarConfiguracao(
                            configuracaoAtual.copy(notificacoesGolos = it)
                        )
                    },
                    icon = Icons.Default.SportsSoccer
                )

                ParticipantNotificationDivider()

                ParticipantNotificationSwitchRow(
                    title = "Notificações de cartões",
                    description = "Alertas de cartões nos jogos dos teus torneios",
                    checked = configuracaoAtual.notificacoesCartoes,
                    onCheckedChange = {
                        atualizarConfiguracao(
                            configuracaoAtual.copy(notificacoesCartoes = it)
                        )
                    },
                    icon = Icons.Default.Notifications
                )

                ParticipantNotificationDivider()

                ParticipantNotificationSwitchRow(
                    title = "Fim de partida",
                    description = "Aviso quando um jogo dos teus torneios terminar",
                    checked = configuracaoAtual.notificacoesFimPartida,
                    onCheckedChange = {
                        atualizarConfiguracao(
                            configuracaoAtual.copy(notificacoesFimPartida = it)
                        )
                    },
                    icon = Icons.Default.Notifications
                )

                ParticipantNotificationDivider()

                ParticipantNotificationSwitchRow(
                    title = "Som da notificação",
                    description = "Ativar som nos alertas recebidos",
                    checked = configuracaoAtual.somNotificacao,
                    onCheckedChange = {
                        atualizarConfiguracao(
                            configuracaoAtual.copy(somNotificacao = it)
                        )
                    },
                    icon = Icons.Default.VolumeUp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ParticipantNotificationSectionCard(title = "Notificação por Desporto") {
                ParticipantNotificationSwitchRow(
                    title = "Futebol",
                    description = "Receber notificações de futebol dos teus torneios",
                    checked = configuracaoAtual.futebol,
                    onCheckedChange = {
                        atualizarConfiguracao(
                            configuracaoAtual.copy(futebol = it)
                        )
                    },
                    icon = Icons.Default.SportsSoccer
                )

                ParticipantNotificationDivider()

                ParticipantNotificationSwitchRow(
                    title = "Ténis",
                    description = "Receber notificações de ténis dos teus torneios",
                    checked = configuracaoAtual.tenis,
                    onCheckedChange = {
                        atualizarConfiguracao(
                            configuracaoAtual.copy(tenis = it)
                        )
                    },
                    icon = Icons.Default.SportsTennis
                )

                ParticipantNotificationDivider()

                ParticipantNotificationSwitchRow(
                    title = "Basquetebol",
                    description = "Receber notificações de basquetebol dos teus torneios",
                    checked = configuracaoAtual.basquetebol,
                    onCheckedChange = {
                        atualizarConfiguracao(
                            configuracaoAtual.copy(basquetebol = it)
                        )
                    },
                    icon = Icons.Default.SportsBasketball
                )

                ParticipantNotificationDivider()

                ParticipantNotificationSwitchRow(
                    title = "Andebol",
                    description = "Receber notificações de andebol dos teus torneios",
                    checked = configuracaoAtual.andebol,
                    onCheckedChange = {
                        atualizarConfiguracao(
                            configuracaoAtual.copy(andebol = it)
                        )
                    },
                    icon = Icons.Default.SportsHandball
                )
            }
        }
    }
}

@Composable
private fun ParticipantNotificacoesHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(RedPrimary, RedDark, Color(0xFF17171C))
                    )
                )
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                TranslatedText(
                    text = "Notificações",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                TranslatedText(
                    text = "Alertas apenas dos torneios onde participas",
                    color = Color.White.copy(alpha = 0.82f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun ParticipantNotificationSectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                text = title,
                color = RedDark,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            content()
        }
    }
}

@Composable
private fun ParticipantNotificationSwitchRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(RedPrimary.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = RedPrimary,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color(0xFF17171C),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = description,
                color = Color(0xFF74747C),
                style = MaterialTheme.typography.labelSmall
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF20C866),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE0E0E5),
                checkedBorderColor = Color(0xFF20C866),
                uncheckedBorderColor = Color(0xFFE0E0E5)
            )
        )
    }
}

@Composable
private fun ParticipantNotificationDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 54.dp),
        color = Color(0xFFE8E8EC),
        thickness = 1.dp
    )
}