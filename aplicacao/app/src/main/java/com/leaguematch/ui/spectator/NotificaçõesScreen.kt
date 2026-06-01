package com.leaguematch.ui.spectator

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leaguematch.data.remote.model.ConfiguracaoNotificacoes
import com.leaguematch.ui.components.SpectatorBottomBar
import com.leaguematch.ui.theme.RedDark
import com.leaguematch.ui.theme.RedPrimary

@Composable
fun NotificacoesScreen(
    configuracao: ConfiguracaoNotificacoes,
    onGuardarConfiguracao: (ConfiguracaoNotificacoes) -> Unit,
    onHomeClick: () -> Unit,
    onClassificacaoClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEquipasClick: () -> Unit,
    onPerfilClick: () -> Unit
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
            SpectatorBottomBar(
                selectedItem = "perfil",
                onHomeClick = onHomeClick,
                onClassificacaoClick = onClassificacaoClick,
                onJogosClick = onJogosClick,
                onEquipasClick = onEquipasClick,
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
            NotificacoesHeader()

            Spacer(modifier = Modifier.height(18.dp))

            NotificationSectionCard(title = "Configuração Geral") {
                NotificationSwitchRow(
                    title = "Notificações dos jogos",
                    description = "Alertas sobre novos jogos agendados",
                    checked = configuracaoAtual.notificacoesJogos,
                    onCheckedChange = {
                        atualizarConfiguracao(
                            configuracaoAtual.copy(notificacoesJogos = it)
                        )
                    },
                    icon = Icons.Default.Notifications
                )

                NotificationDivider()

                NotificationSwitchRow(
                    title = "Notificações de golos",
                    description = "Receber aviso quando houver golos",
                    checked = configuracaoAtual.notificacoesGolos,
                    onCheckedChange = {
                        atualizarConfiguracao(
                            configuracaoAtual.copy(notificacoesGolos = it)
                        )
                    },
                    icon = Icons.Default.SportsSoccer
                )

                NotificationDivider()

                NotificationSwitchRow(
                    title = "Notificações de cartões",
                    description = "Alertas de cartões durante os jogos",
                    checked = configuracaoAtual.notificacoesCartoes,
                    onCheckedChange = {
                        atualizarConfiguracao(
                            configuracaoAtual.copy(notificacoesCartoes = it)
                        )
                    },
                    icon = Icons.Default.Notifications
                )

                NotificationDivider()

                NotificationSwitchRow(
                    title = "Fim de partida",
                    description = "Aviso quando um jogo terminar",
                    checked = configuracaoAtual.notificacoesFimPartida,
                    onCheckedChange = {
                        atualizarConfiguracao(
                            configuracaoAtual.copy(notificacoesFimPartida = it)
                        )
                    },
                    icon = Icons.Default.Notifications
                )

                NotificationDivider()

                NotificationSwitchRow(
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

            NotificationSectionCard(title = "Notificação por Desporto") {
                NotificationSwitchRow(
                    title = "Futebol",
                    description = "Receber notificações de futebol",
                    checked = configuracaoAtual.futebol,
                    onCheckedChange = {
                        atualizarConfiguracao(
                            configuracaoAtual.copy(futebol = it)
                        )
                    },
                    icon = Icons.Default.SportsSoccer
                )

                NotificationDivider()

                NotificationSwitchRow(
                    title = "Ténis",
                    description = "Receber notificações de ténis",
                    checked = configuracaoAtual.tenis,
                    onCheckedChange = {
                        atualizarConfiguracao(
                            configuracaoAtual.copy(tenis = it)
                        )
                    },
                    icon = Icons.Default.SportsTennis
                )

                NotificationDivider()

                NotificationSwitchRow(
                    title = "Basquetebol",
                    description = "Receber notificações de basquetebol",
                    checked = configuracaoAtual.basquetebol,
                    onCheckedChange = {
                        atualizarConfiguracao(
                            configuracaoAtual.copy(basquetebol = it)
                        )
                    },
                    icon = Icons.Default.SportsBasketball
                )

                NotificationDivider()

                NotificationSwitchRow(
                    title = "Andebol",
                    description = "Receber notificações de andebol",
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
private fun NotificacoesHeader() {
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
                Text(
                    text = "Notificações",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Gere os alertas dos teus torneios",
                    color = Color.White.copy(alpha = 0.82f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun NotificationSectionCard(
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
private fun NotificationSwitchRow(
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
private fun NotificationDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 54.dp),
        color = Color(0xFFE8E8EC),
        thickness = 1.dp
    )
}