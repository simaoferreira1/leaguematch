package com.leaguematch.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.ConfiguracaoNotificacoes
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.translations.Language
import com.leaguematch.ui.components.AdminBottomBar
import com.leaguematch.ui.components.Avatar
import com.leaguematch.ui.components.CardWrapper
import com.leaguematch.ui.components.LeagueMatchTextField
import com.leaguematch.ui.components.Pill
import com.leaguematch.ui.components.TopBar
import com.leaguematch.ui.components.TranslatedText
import com.leaguematch.ui.theme.BrandTheme
import com.leaguematch.ui.theme.Bricolage
import com.leaguematch.ui.theme.Geist
import com.leaguematch.ui.theme.LMBorder
import com.leaguematch.ui.theme.LMGray100
import com.leaguematch.ui.theme.LMGray400
import com.leaguematch.ui.theme.LMGray500
import com.leaguematch.ui.theme.LMGray600
import com.leaguematch.ui.theme.LMInk
import com.leaguematch.ui.theme.LMRed
import com.leaguematch.ui.theme.LMWhite

@Composable
fun DefinicoesScreen(
    utilizadorLogado: Utilizador? = null,
    language: Language = Language.PT,
    onLanguageChange: (Language) -> Unit = {},
    primaryColor: Color = LMRed,
    onPrimaryColorChange: (Color) -> Unit = { BrandTheme.primaryColor = it },

    configuracaoNotificacoes: ConfiguracaoNotificacoes? = null,
    onGuardarConfiguracaoNotificacoes: ((ConfiguracaoNotificacoes) -> Unit)? = null,

    onTerminarSessaoClick: () -> Unit = {},
    onEditarPerfilClick: (String, String?) -> Unit = { _, _ -> },
    onGerirNotificacoesClick: () -> Unit = {},
    alteracoesPendentes: Int = 0,
    onSincronizarPendentesClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onUtilizadoresClick: () -> Unit = {},
    onTorneiosClick: () -> Unit = {},
    onGraficosClick: () -> Unit = {},
    bottomBar: @Composable () -> Unit = {
        AdminBottomBar(
            selectedItem = "definicoes",
            onHomeClick = onHomeClick,
            onUtilizadoresClick = onUtilizadoresClick,
            onTorneiosClick = onTorneiosClick,
            onGraficosClick = onGraficosClick,
            onDefinicoesClick = {}
        )
    }
) {
    // Controla a abertura do diálogo de edição de perfil.
    var showEditDialog by remember { mutableStateOf(false) }
    // Guarda temporariamente os dados introduzidos pelo utilizador.
    var newNome by remember(utilizadorLogado) { mutableStateOf(utilizadorLogado?.nome.orEmpty()) }
    var newPassword by remember { mutableStateOf("") }
    // Controla a abertura do seletor de idioma.
    var showLanguageDialog by remember { mutableStateOf(false) }
    // Estados locais das preferências de notificações.
    var notificacoesJogos by rememberSaveable { mutableStateOf(configuracaoNotificacoes?.notificacoesJogos ?: true) }
    var notificacoesGolos by rememberSaveable { mutableStateOf(configuracaoNotificacoes?.notificacoesGolos ?: true) }
    var notificacoesCartoes by rememberSaveable { mutableStateOf(configuracaoNotificacoes?.notificacoesCartoes ?: true) }
    var notificacoesFimPartida by rememberSaveable { mutableStateOf(configuracaoNotificacoes?.notificacoesFimPartida ?: true) }
    var somNotificacao by rememberSaveable { mutableStateOf(configuracaoNotificacoes?.somNotificacao ?: true) }

    fun guardarConfig(transform: (ConfiguracaoNotificacoes) -> ConfiguracaoNotificacoes) {
        val base = configuracaoNotificacoes ?: ConfiguracaoNotificacoes(
            utilizadorId = utilizadorLogado?.id ?: return,
            notificacoesJogos = notificacoesJogos,
            notificacoesGolos = notificacoesGolos,
            notificacoesCartoes = notificacoesCartoes,
            notificacoesFimPartida = notificacoesFimPartida,
            somNotificacao = somNotificacao
        )

        onGuardarConfiguracaoNotificacoes?.invoke(transform(base))
    }


    // Conteúdo e barra de navegação inferior.
    Scaffold(
        bottomBar = bottomBar,
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 12.dp)
        ) {
            TopBar(
                title = "Perfil",
                big = true,
                rightContent = {
                    if (true) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(LMGray100, RoundedCornerShape(10.dp))
                                .clickable { onGerirNotificacoesClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notificações",
                                tint = LMGray600,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(LMGray100, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = LMGray600,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            )

            // Cartão de apresentação do utilizador.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
                    .background(LMInk, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                val nomeExibido = utilizadorLogado?.nome ?: "Utilizador LeagueMatch"
                val tipoExibido = utilizadorLogado?.tipo?.descricao ?: "Participante"

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Avatar(
                        name = nomeExibido,
                        size = 56.dp,
                        color = primaryColor
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        TranslatedText(
                            text = nomeExibido,
                            fontFamily = Bricolage,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LMWhite
                        )

                        TranslatedText(
                            text = "$tipoExibido · LeagueMatch",
                            fontFamily = Geist,
                            fontSize = 11.sp,
                            color = LMWhite.copy(alpha = 0.6f),
                            modifier = Modifier.padding(top = 1.dp)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .background(LMWhite.copy(alpha = 0.1f), RoundedCornerShape(99.dp))
                                .border(
                                    BorderStroke(1.dp, LMWhite.copy(alpha = 0.2f)),
                                    RoundedCornerShape(99.dp)
                                )
                                .clickable { showEditDialog = true }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            TranslatedText(
                                text = "Editar perfil",
                                fontFamily = Geist,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = LMWhite
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Configurações gerais da aplicação.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                CardWrapper(
                    modifier = Modifier.fillMaxWidth(),
                    pad = 0.dp
                ) {
                    Column {
                        ProfileRow(
                            icon = Icons.Default.Language,
                            label = "Idioma",
                            onClick = { showLanguageDialog = true },
                            rightContent = {
                                Text(
                                    text = if (language == Language.PT) "Português ›" else "English ›",
                                    fontFamily = Geist,
                                    fontSize = 12.sp,
                                    color = LMGray500
                                )
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(start = 58.dp),
                            color = LMBorder,
                            thickness = 1.dp
                        )

                        ProfileRow(
                            icon = Icons.Default.BrightnessMedium,
                            label = "Aparência",
                            rightContent = {
                                TranslatedText(
                                    text = "Sistema ›",
                                    fontFamily = Geist,
                                    fontSize = 12.sp,
                                    color = LMGray500
                                )
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(start = 58.dp),
                            color = LMBorder,
                            thickness = 1.dp
                        )

                        ProfileRow(
                            icon = Icons.Default.Palette,
                            label = "Cor da aplicação",
                            rightContent = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ColorOption(
                                        color = Color(0xFFE31734),
                                        selectedColor = primaryColor,
                                        onClick = { onPrimaryColorChange(Color(0xFFE31734)) }
                                    )

                                    ColorOption(
                                        color = Color(0xFF2563EB),
                                        selectedColor = primaryColor,
                                        onClick = { onPrimaryColorChange(Color(0xFF2563EB)) }
                                    )

                                    ColorOption(
                                        color = Color(0xFF16A34A),
                                        selectedColor = primaryColor,
                                        onClick = { onPrimaryColorChange(Color(0xFF16A34A)) }
                                    )

                                    ColorOption(
                                        color = Color(0xFF9333EA),
                                        selectedColor = primaryColor,
                                        onClick = { onPrimaryColorChange(Color(0xFF9333EA)) }
                                    )
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Permite verificar o estado da sincronização e terminar sessão.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                CardWrapper(
                    modifier = Modifier.fillMaxWidth(),
                    pad = 0.dp
                ) {
                    Column {
                        ProfileRow(
                            icon = Icons.Default.Sync,
                            label = if (alteracoesPendentes > 0)
                                "Sincronizar ($alteracoesPendentes pendentes)"
                            else
                                "Sincronização offline",
                            onClick = { if (alteracoesPendentes > 0) onSincronizarPendentesClick() },
                            rightContent = {
                                Pill(
                                    text = if (alteracoesPendentes > 0) "$alteracoesPendentes" else "Ativa",
                                    kind = if (alteracoesPendentes > 0) "warn" else "live"
                                )
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(start = 58.dp),
                            color = LMBorder,
                            thickness = 1.dp
                        )

                        ProfileRow(
                            icon = Icons.AutoMirrored.Filled.Logout,
                            label = "Terminar sessão",
                            labelColor = LMRed,
                            iconTint = LMRed,
                            onClick = onTerminarSessaoClick,
                            rightContent = {
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = primaryColor,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                    }
                }
            }

            Text(
                text = "LeagueMatch v1.0.0 · EI-3A-Grupo C · 2025/2026",
                fontFamily = Geist,
                fontSize = 10.sp,
                color = LMGray400,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp)
            )
        }
    }

    // Janela modal utilizada para editar os dados do perfil.
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = {
                showEditDialog = false
                newPassword = ""
            },
            title = {
                TranslatedText(
                    text = "Editar Perfil",
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = LMInk
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    LeagueMatchTextField(
                        value = newNome,
                        onValueChange = { newNome = it },
                        label = "Nome completo",
                        placeholder = "Nome completo"
                    )

                    LeagueMatchTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = if (language == Language.PT) "Nova palavra-passe (opcional)" else "New password (optional)",
                        placeholder = "••••••••",
                        isPassword = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newNome.isNotBlank()) {
                            onEditarPerfilClick(newNome, newPassword.takeIf { it.isNotBlank() })
                            showEditDialog = false
                            newPassword = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    TranslatedText(
                        text = "Guardar",
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        color = LMWhite
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showEditDialog = false
                        newPassword = ""
                    }
                ) {
                    TranslatedText(
                        text = "Cancelar",
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold,
                        color = LMGray600
                    )
                }
            },
            containerColor = LMWhite,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Janela modal para seleção do idioma da aplicação.
    if (showLanguageDialog) {
            AlertDialog(
                onDismissRequest = {
                    showLanguageDialog = false
                },
                containerColor = LMWhite,
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 8.dp,
                title = {
                    TranslatedText(
                        text = "Escolher idioma",
                        fontFamily = Bricolage,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = LMInk
                    )
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ProfileRow(
                            icon = Icons.Default.Language,
                            label = "Português",
                            onClick = {
                                onLanguageChange(Language.PT)
                                showLanguageDialog = false
                            }
                        )

                        HorizontalDivider(color = LMBorder)

                        ProfileRow(
                            icon = Icons.Default.Language,
                            label = "English",
                            onClick = {
                                onLanguageChange(Language.EN)
                                showLanguageDialog = false
                            }
                        )
                    }
                },
                confirmButton = {}
            )
        }
}

// Componente reutilizável para apresentar uma preferência
@Composable
fun ProfileSwitchRow(
    icon: ImageVector,
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(LMGray100, RoundedCornerShape(9.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = LMRed,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontFamily = Geist,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = LMInk
            )

            Text(
                text = description,
                fontFamily = Geist,
                fontSize = 10.sp,
                color = LMGray500
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = LMWhite,
                checkedTrackColor = Color(0xFF22C55E),
                uncheckedThumbColor = LMWhite,
                uncheckedTrackColor = Color(0xFFE0E0E5),
                checkedBorderColor = Color(0xFF22C55E),
                uncheckedBorderColor = Color(0xFFE0E0E5)
            )
        )
    }
}

// Linha reutilizável utilizada nas definições.
@Composable
fun ProfileRow(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    iconTint: Color = LMInk,
    labelColor: Color = LMInk,
    onClick: () -> Unit = {},
    rightContent: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(LMGray100, RoundedCornerShape(9.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = label,
            fontFamily = Geist,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = labelColor,
            modifier = Modifier.weight(1f)
        )

        rightContent()
    }
}

// Representa uma cor selecionável para personalizar
@Composable
fun ColorOption(
    color: Color,
    selectedColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(18.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                BorderStroke(
                    width = if (selectedColor == color) 2.dp else 0.dp,
                    color = LMInk
                ),
                CircleShape
            )
            .clickable { onClick() }
    )
}