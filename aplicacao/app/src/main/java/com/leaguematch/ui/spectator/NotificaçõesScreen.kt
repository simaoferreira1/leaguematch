/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: NotificaçõesScreen.kt
 * Tipo: Interface (Compose View) do Espectador
 *
 * Descrição:
 * Este ficheiro define um ecrã de visualização pública (Espectador) em Jetpack Compose.\n * Apenas exibe dados para leitura (como tabelas de classificação, jogos ao vivo e calendários) sem permitir alteração.
 */
package com.leaguematch.ui.spectator // Define o pacote deste ficheiro de código

import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Box // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Column // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.ColumnScope // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Row // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Spacer // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.fillMaxSize // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.fillMaxWidth // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.height // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.padding // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.size // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.width // Importa dependência / biblioteca necessária
import androidx.compose.foundation.rememberScrollState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.CircleShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Notifications // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsBasketball // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsHandball // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsSoccer // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.SportsTennis // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.VolumeUp // Importa dependência / biblioteca necessária
import androidx.compose.material3.Card // Importa dependência / biblioteca necessária
import androidx.compose.material3.CardDefaults // Importa dependência / biblioteca necessária
import androidx.compose.material3.HorizontalDivider // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Switch // Importa dependência / biblioteca necessária
import androidx.compose.material3.SwitchDefaults // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.remember // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.draw.clip // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Brush // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.vector.ImageVector // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.ConfiguracaoNotificacoes // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.SpectatorBottomBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.RedDark // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.RedPrimary // Importa dependência / biblioteca necessária

@Composable
fun NotificacoesScreen( // Declaração de função / método de lógica
    configuracao: ConfiguracaoNotificacoes,
    onGuardarConfiguracao: (ConfiguracaoNotificacoes) -> Unit,
    onHomeClick: () -> Unit,
    onClassificacaoClick: () -> Unit,
    onJogosClick: () -> Unit,
    onEquipasClick: () -> Unit,
    onPerfilClick: () -> Unit,
    onAbrirInbox: () -> Unit = {}
) {
    var configuracaoAtual by remember(configuracao) { // Memoriza estado para evitar perda durante a recomposição
        mutableStateOf(configuracao) // Declara estado mutável local do Compose
    }

    fun atualizarConfiguracao(novaConfiguracao: ConfiguracaoNotificacoes) { // Declaração de função / método de lógica
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

        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            NotificacoesHeader()

            Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            // Botão para abrir a caixa de entrada
            androidx.compose.material3.Button( // Componente Compose: Desenha um botão interativo
                onClick = onAbrirInbox, // Callback: Define a ação executada ao clicar no componente
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = RedPrimary
                )
            ) {
                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                    TranslatedText(
                        text = "Ver caixa de entrada",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

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

            Spacer(modifier = Modifier.height(16.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

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
private fun NotificacoesHeader() { // Declaração de função / método de lógica
    Card( // Contentor Compose: Cartão visual com elevação e cantos
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(RedPrimary, RedDark, Color(0xFF17171C))
                    )
                )
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon( // Componente Compose: Desenha um ícone vetorial
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(30.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.width(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column { // Contentor Compose: Alinha os filhos numa coluna vertical
                TranslatedText(
                    text = "Notificações",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                TranslatedText(
                    text = "Gere os alertas dos teus torneios",
                    color = Color.White.copy(alpha = 0.82f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun NotificationSectionCard( // Declaração de função / método de lógica
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card( // Contentor Compose: Cartão visual com elevação e cantos
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier.padding(14.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
        ) {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = title,
                color = RedDark,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            content()
        }
    }
}

@Composable
private fun NotificationSwitchRow( // Declaração de função / método de lógica
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: ImageVector
) {
    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .padding(vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box( // Contentor Compose: Sobrepõe os elementos filhos
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .size(42.dp)
                .clip(CircleShape)
                .background(RedPrimary.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center
        ) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                imageVector = icon,
                contentDescription = null,
                tint = RedPrimary,
                modifier = Modifier.size(22.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )
        }

        Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

        Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = title,
                color = Color(0xFF17171C),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )

            Text( // Componente Compose: Desenha texto estruturado no ecrã
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
private fun NotificationDivider() { // Declaração de função / método de lógica
    HorizontalDivider(
        modifier = Modifier.padding(start = 54.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
        color = Color(0xFFE8E8EC),
        thickness = 1.dp
    )
}