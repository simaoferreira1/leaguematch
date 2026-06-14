/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: AdminNotificacoesScreen.kt
 * Tipo: Interface (Compose View) do Administrador
 *
 * Descrição:
 * Este ficheiro define um ecrã da área do Administrador em Jetpack Compose.\n * Ele desenha componentes visuais reativos baseado no estado fornecido pelo respetivo ViewModel.\n * Permite ao Admin gerir utilizadores (ativar/desativar), visualizar alertas do sistema e gráficos.
 */
package com.leaguematch.ui.admin // Define o pacote deste ficheiro de código

import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.* // Importa dependência / biblioteca necessária
import androidx.compose.foundation.rememberScrollState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.CircleShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Notifications // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.NotificationsActive // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.NotificationsNone // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.rounded.ArrowBack // Importa dependência / biblioteca necessária
import androidx.compose.material3.* // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.draw.clip // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Brush // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.NotificacaoItem // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TextBtn // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.* // Importa dependência / biblioteca necessária

@Composable
fun AdminNotificacoesScreen( // Declaração de função / método de lógica
    notificacoes: List<NotificacaoItem>,
    onBackClick: () -> Unit,
    onMarcarTodasLidas: () -> Unit,
    onNotificacaoClick: (NotificacaoItem) -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFF6F6F8)
    ) { padding ->

        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            AdminNotificacoesHeader(
                naoLidas = notificacoes.count { !it.lida },
                mostrarMarcarTodas = notificacoes.any { !it.lida },
                onBackClick = onBackClick,
                onMarcarTodasLidas = onMarcarTodasLidas
            )

            Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            if (notificacoes.isEmpty()) { // Estrutura de decisão condicional principal
                AdminEmptyNotificationsCard()
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    notificacoes.forEach { notificacao ->
                        AdminNotificacaoCard(
                            notificacao = notificacao,
                            onClick = { onNotificacaoClick(notificacao) } // Callback: Define a ação executada ao clicar no componente
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminNotificacoesHeader( // Declaração de função / método de lógica
    naoLidas: Int,
    mostrarMarcarTodas: Boolean,
    onBackClick: () -> Unit,
    onMarcarTodasLidas: () -> Unit
) {
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
                        listOf(LMRed, RedDark, Color(0xFF17171C))
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) { // Componente Compose: Desenha um botão com ícone
                Icon( // Componente Compose: Desenha um ícone vetorial
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White
                )
            }

            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon( // Componente Compose: Desenha um ícone vetorial
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                TranslatedText(
                    text = "Notificações",
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 21.sp,
                    color = Color.White
                )

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "$naoLidas por ler",
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.82f)
                )

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = "Alertas de todos os jogos",
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.68f)
                )
            }

            if (mostrarMarcarTodas) { // Estrutura de decisão condicional principal
                TextBtn(onClick = onMarcarTodasLidas) { // Callback: Define a ação executada ao clicar no componente
                    TranslatedText(
                        text = "Marcar todas",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminEmptyNotificationsCard() { // Declaração de função / método de lógica
    Card( // Contentor Compose: Cartão visual com elevação e cantos
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxWidth()
                .padding(vertical = 54.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(LMRed.copy(alpha = 0.10f)),
                contentAlignment = Alignment.Center
            ) {
                Icon( // Componente Compose: Desenha um ícone vetorial
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = null,
                    tint = LMRed,
                    modifier = Modifier.size(34.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.height(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            TranslatedText(
                text = "Sem notificações por agora.",
                fontFamily = Geist,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = "Quando houver jogos agendados, começados ou terminados, aparecem aqui.",
                fontFamily = Geist,
                fontSize = 12.sp,
                color = LMGray500
            )
        }
    }
}

@Composable
private fun AdminNotificacaoCard( // Declaração de função / método de lógica
    notificacao: NotificacaoItem,
    onClick: () -> Unit // Callback: Define a ação executada ao clicar no componente
) {
    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(16.dp),
        color = if (notificacao.lida) LMWhite else LMRed50, // Estrutura de decisão condicional principal
        border = BorderStroke(1.dp, LMBorder),
        tonalElevation = 2.dp
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .size(40.dp)
                    .background(
                        if (notificacao.lida) Color(0xFFF2F2F4) // Estrutura de decisão condicional principal
                        else LMRed.copy(alpha = 0.15f), // Fluxo condicional alternativo caso o 'if' seja falso
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon( // Componente Compose: Desenha um ícone vetorial
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = if (notificacao.lida) LMGray500 else LMRed, // Estrutura de decisão condicional principal
                    modifier = Modifier.size(20.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            }

            Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = notificacao.mensagem,
                    fontFamily = Geist,
                    fontSize = 14.sp,
                    fontWeight = if (notificacao.lida) FontWeight.Normal else FontWeight.SemiBold, // Estrutura de decisão condicional principal
                    color = LMInk
                )

                if (notificacao.data.isNotBlank()) { // Estrutura de decisão condicional principal
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = notificacao.data.take(16).replace('T', ' '),
                        fontFamily = Geist,
                        fontSize = 10.sp,
                        color = LMGray400,
                        modifier = Modifier.padding(top = 3.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                }
            }

            if (!notificacao.lida) { // Estrutura de decisão condicional principal
                TextBtn(onClick = onClick) { // Callback: Define a ação executada ao clicar no componente
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "✓",
                        color = LMRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}