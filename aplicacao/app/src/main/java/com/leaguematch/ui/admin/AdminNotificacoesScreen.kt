package com.leaguematch.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.NotificacaoItem
import com.leaguematch.ui.components.TextBtn
import com.leaguematch.ui.components.TranslatedText
import com.leaguematch.ui.theme.*

@Composable
fun AdminNotificacoesScreen(
    notificacoes: List<NotificacaoItem>,
    onBackClick: () -> Unit,
    onMarcarTodasLidas: () -> Unit,
    onNotificacaoClick: (NotificacaoItem) -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFF6F6F8)
    ) { padding ->

        Column(
            modifier = Modifier
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

            Spacer(modifier = Modifier.height(18.dp))

            if (notificacoes.isEmpty()) {
                AdminEmptyNotificationsCard()
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    notificacoes.forEach { notificacao ->
                        AdminNotificacaoCard(
                            notificacao = notificacao,
                            onClick = { onNotificacaoClick(notificacao) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminNotificacoesHeader(
    naoLidas: Int,
    mostrarMarcarTodas: Boolean,
    onBackClick: () -> Unit,
    onMarcarTodasLidas: () -> Unit
) {
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
                        listOf(LMRed, RedDark, Color(0xFF17171C))
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                TranslatedText(
                    text = "Notificações",
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 21.sp,
                    color = Color.White
                )

                Text(
                    text = "$naoLidas por ler",
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.82f)
                )

                Text(
                    text = "Alertas de todos os jogos",
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.68f)
                )
            }

            if (mostrarMarcarTodas) {
                TextBtn(onClick = onMarcarTodasLidas) {
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
private fun AdminEmptyNotificationsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 54.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(LMRed.copy(alpha = 0.10f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = null,
                    tint = LMRed,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            TranslatedText(
                text = "Sem notificações por agora.",
                fontFamily = Geist,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = LMInk
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Quando houver jogos agendados, começados ou terminados, aparecem aqui.",
                fontFamily = Geist,
                fontSize = 12.sp,
                color = LMGray500
            )
        }
    }
}

@Composable
private fun AdminNotificacaoCard(
    notificacao: NotificacaoItem,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (notificacao.lida) LMWhite else LMRed50,
        border = BorderStroke(1.dp, LMBorder),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        if (notificacao.lida) Color(0xFFF2F2F4)
                        else LMRed.copy(alpha = 0.15f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = if (notificacao.lida) LMGray500 else LMRed,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notificacao.mensagem,
                    fontFamily = Geist,
                    fontSize = 14.sp,
                    fontWeight = if (notificacao.lida) FontWeight.Normal else FontWeight.SemiBold,
                    color = LMInk
                )

                if (notificacao.data.isNotBlank()) {
                    Text(
                        text = notificacao.data.take(16).replace('T', ' '),
                        fontFamily = Geist,
                        fontSize = 10.sp,
                        color = LMGray400,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }
            }

            if (!notificacao.lida) {
                TextBtn(onClick = onClick) {
                    Text(
                        text = "✓",
                        color = LMRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}