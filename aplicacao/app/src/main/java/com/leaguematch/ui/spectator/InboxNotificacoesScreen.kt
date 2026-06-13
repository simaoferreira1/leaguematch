package com.leaguematch.ui.spectator

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.NotificacaoItem
import com.leaguematch.ui.components.TextBtn
import com.leaguematch.ui.components.TranslatedText
import com.leaguematch.ui.theme.Bricolage
import com.leaguematch.ui.theme.Geist
import com.leaguematch.ui.theme.LMBorder
import com.leaguematch.ui.theme.LMGray400
import com.leaguematch.ui.theme.LMGray500
import com.leaguematch.ui.theme.LMInk
import com.leaguematch.ui.theme.LMRed
import com.leaguematch.ui.theme.LMRed50
import com.leaguematch.ui.theme.LMWhite

@Composable
fun InboxNotificacoesScreen(
    notificacoes: List<NotificacaoItem>,
    onBackClick: () -> Unit,
    onMarcarTodasLidas: () -> Unit,
    onNotificacaoClick: (NotificacaoItem) -> Unit
) {
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
                .padding(bottom = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = null, tint = LMInk)
                }
                Column(modifier = Modifier.weight(1f)) {
                    TranslatedText(
                        text = "Notificações",
                        fontFamily = Bricolage,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = LMInk
                    )
                    Text(
                        text = "${notificacoes.count { !it.lida }} por ler",
                        fontFamily = Geist,
                        fontSize = 12.sp,
                        color = LMGray500
                    )
                }
                if (notificacoes.any { !it.lida }) {
                    TextBtn(onClick = onMarcarTodasLidas) {
                        TranslatedText(
                            text = "Marcar todas",
                            color = LMRed,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (notificacoes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.NotificationsNone,
                            contentDescription = null,
                            tint = LMGray400,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        TranslatedText(
                            text = "Sem notificações por agora.",
                            fontFamily = Geist,
                            fontSize = 13.sp,
                            color = LMGray500
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    notificacoes.forEach { n ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = if (n.lida) LMWhite else LMRed50,
                            border = BorderStroke(1.dp, LMBorder)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(
                                            if (n.lida) Color(0xFFF2F2F4) else LMRed.copy(alpha = 0.15f),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.NotificationsActive,
                                        contentDescription = null,
                                        tint = if (n.lida) LMGray500 else LMRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.size(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = n.mensagem,
                                        fontFamily = Geist,
                                        fontSize = 14.sp,
                                        fontWeight = if (n.lida) FontWeight.Normal else FontWeight.SemiBold,
                                        color = LMInk
                                    )
                                    if (n.data.isNotBlank()) {
                                        Text(
                                            text = n.data.take(16).replace('T', ' '),
                                            fontFamily = Geist,
                                            fontSize = 10.sp,
                                            color = LMGray400,
                                            modifier = Modifier.padding(top = 2.dp)
                                        )
                                    }
                                }
                                if (!n.lida) {
                                    TextBtn(onClick = { onNotificacaoClick(n) }) {
                                        TranslatedText(
                                            text = "✓",
                                            color = LMRed,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
