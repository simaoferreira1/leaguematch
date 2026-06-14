/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: InboxNotificacoesScreen.kt
 * Tipo: Interface (Compose View) do Espectador
 *
 * Descrição:
 * Este ficheiro define um ecrã de visualização pública (Espectador) em Jetpack Compose.\n * Apenas exibe dados para leitura (como tabelas de classificação, jogos ao vivo e calendários) sem permitir alteração.
 */
package com.leaguematch.ui.spectator // Define o pacote deste ficheiro de código

import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
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
import androidx.compose.foundation.rememberScrollState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.CircleShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.NotificationsActive // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.NotificationsNone // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.rounded.ArrowBack // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.IconButton // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Surface // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.NotificacaoItem // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TextBtn // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Bricolage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMBorder // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray400 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed50 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária

@Composable
fun InboxNotificacoesScreen( // Declaração de função / método de lógica
    notificacoes: List<NotificacaoItem>,
    onBackClick: () -> Unit,
    onMarcarTodasLidas: () -> Unit,
    onNotificacaoClick: (NotificacaoItem) -> Unit
) {
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
                .padding(bottom = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row(verticalAlignment = Alignment.CenterVertically) { // Contentor Compose: Alinha os filhos numa linha horizontal
                IconButton(onClick = onBackClick) { // Componente Compose: Desenha um botão com ícone
                    Icon(Icons.Rounded.ArrowBack, contentDescription = null, tint = LMInk) // Componente Compose: Desenha um ícone vetorial
                }
                Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    TranslatedText(
                        text = "Notificações",
                        fontFamily = Bricolage,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = LMInk
                    )
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "${notificacoes.count { !it.lida }} por ler",
                        fontFamily = Geist,
                        fontSize = 12.sp,
                        color = LMGray500
                    )
                }
                if (notificacoes.any { !it.lida }) { // Estrutura de decisão condicional principal
                    TextBtn(onClick = onMarcarTodasLidas) { // Callback: Define a ação executada ao clicar no componente
                        TranslatedText(
                            text = "Marcar todas",
                            color = LMRed,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            if (notificacoes.isEmpty()) { // Estrutura de decisão condicional principal
                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .fillMaxWidth()
                        .padding(vertical = 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) { // Contentor Compose: Alinha os filhos numa coluna vertical
                        Icon( // Componente Compose: Desenha um ícone vetorial
                            Icons.Default.NotificationsNone,
                            contentDescription = null,
                            tint = LMGray400,
                            modifier = Modifier.size(44.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                        )
                        Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                        TranslatedText(
                            text = "Sem notificações por agora.",
                            fontFamily = Geist,
                            fontSize = 13.sp,
                            color = LMGray500
                        )
                    }
                }
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    notificacoes.forEach { n ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                            shape = RoundedCornerShape(14.dp),
                            color = if (n.lida) LMWhite else LMRed50, // Estrutura de decisão condicional principal
                            border = BorderStroke(1.dp, LMBorder)
                        ) {
                            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box( // Contentor Compose: Sobrepõe os elementos filhos
                                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                        .size(36.dp)
                                        .background(
                                            if (n.lida) Color(0xFFF2F2F4) else LMRed.copy(alpha = 0.15f), // Estrutura de decisão condicional principal
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon( // Componente Compose: Desenha um ícone vetorial
                                        Icons.Default.NotificationsActive,
                                        contentDescription = null,
                                        tint = if (n.lida) LMGray500 else LMRed, // Estrutura de decisão condicional principal
                                        modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                                    )
                                }
                                Spacer(modifier = Modifier.size(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
                                Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                                        text = n.mensagem,
                                        fontFamily = Geist,
                                        fontSize = 14.sp,
                                        fontWeight = if (n.lida) FontWeight.Normal else FontWeight.SemiBold, // Estrutura de decisão condicional principal
                                        color = LMInk
                                    )
                                    if (n.data.isNotBlank()) { // Estrutura de decisão condicional principal
                                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                                            text = n.data.take(16).replace('T', ' '),
                                            fontFamily = Geist,
                                            fontSize = 10.sp,
                                            color = LMGray400,
                                            modifier = Modifier.padding(top = 2.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                                        )
                                    }
                                }
                                if (!n.lida) { // Estrutura de decisão condicional principal
                                    TextBtn(onClick = { onNotificacaoClick(n) }) { // Callback: Define a ação executada ao clicar no componente
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
