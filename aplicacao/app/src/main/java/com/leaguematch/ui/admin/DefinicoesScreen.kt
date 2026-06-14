/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: DefinicoesScreen.kt
 * Tipo: Interface (Compose View) do Administrador
 *
 * Descrição:
 * Este ficheiro define um ecrã da área do Administrador em Jetpack Compose.\n * Ele desenha componentes visuais reativos baseado no estado fornecido pelo respetivo ViewModel.\n * Permite ao Admin gerir utilizadores (ativar/desativar), visualizar alertas do sistema e gráficos.
 */
package com.leaguematch.ui.admin // Define o pacote deste ficheiro de código

import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.border // Importa dependência / biblioteca necessária
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
import androidx.compose.foundation.shape.CircleShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.automirrored.filled.Logout // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.BrightnessMedium // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.ChevronRight // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Language // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Notifications // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Palette // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Settings // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Sync // Importa dependência / biblioteca necessária
import androidx.compose.material3.AlertDialog // Importa dependência / biblioteca necessária
import androidx.compose.material3.Button // Importa dependência / biblioteca necessária
import androidx.compose.material3.ButtonDefaults // Importa dependência / biblioteca necessária
import androidx.compose.material3.HorizontalDivider // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Switch // Importa dependência / biblioteca necessária
import androidx.compose.material3.SwitchDefaults // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.material3.TextButton // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.remember // Importa dependência / biblioteca necessária
import androidx.compose.runtime.saveable.rememberSaveable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.draw.clip // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.vector.ImageVector // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.style.TextAlign // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.ConfiguracaoNotificacoes // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Utilizador // Importa dependência / biblioteca necessária
import com.leaguematch.translations.Language // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.AdminBottomBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.Avatar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.CardWrapper // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.LeagueMatchTextField // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.Pill // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TopBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.BrandTheme // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Bricolage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMBorder // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray100 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray400 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray600 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária

@Composable
fun DefinicoesScreen( // Declaração de função / método de lógica
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
    var showEditDialog by remember { mutableStateOf(false) } // Declara estado mutável local do Compose
    // Guarda temporariamente os dados introduzidos pelo utilizador.
    var newNome by remember(utilizadorLogado) { mutableStateOf(utilizadorLogado?.nome.orEmpty()) } // Declara estado mutável local do Compose
    var newPassword by remember { mutableStateOf("") } // Declara estado mutável local do Compose
    // Controla a abertura do seletor de idioma.
    var showLanguageDialog by remember { mutableStateOf(false) } // Declara estado mutável local do Compose
    // Estados locais das preferências de notificações.
    var notificacoesJogos by rememberSaveable { mutableStateOf(configuracaoNotificacoes?.notificacoesJogos ?: true) } // Declara estado mutável local do Compose
    var notificacoesGolos by rememberSaveable { mutableStateOf(configuracaoNotificacoes?.notificacoesGolos ?: true) } // Declara estado mutável local do Compose
    var notificacoesCartoes by rememberSaveable { mutableStateOf(configuracaoNotificacoes?.notificacoesCartoes ?: true) } // Declara estado mutável local do Compose
    var notificacoesFimPartida by rememberSaveable { mutableStateOf(configuracaoNotificacoes?.notificacoesFimPartida ?: true) } // Declara estado mutável local do Compose
    var somNotificacao by rememberSaveable { mutableStateOf(configuracaoNotificacoes?.somNotificacao ?: true) } // Declara estado mutável local do Compose

    fun guardarConfig(transform: (ConfiguracaoNotificacoes) -> ConfiguracaoNotificacoes) { // Declaração de função / método de lógica
        val base = configuracaoNotificacoes ?: ConfiguracaoNotificacoes( // Declara constante local (leitura única)
            utilizadorId = utilizadorLogado?.id ?: return, // Retorna o resultado da execução da função
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

        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 12.dp)
        ) {
            TopBar(
                title = "Perfil",
                big = true,
                rightContent = {
                    if (true) { // Estrutura de decisão condicional principal
                        Box( // Contentor Compose: Sobrepõe os elementos filhos
                            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                .size(36.dp)
                                .background(LMGray100, RoundedCornerShape(10.dp))
                                .clickable { onGerirNotificacoesClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon( // Componente Compose: Desenha um ícone vetorial
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notificações",
                                tint = LMGray600,
                                modifier = Modifier.size(16.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                            )
                        }
                    } else { // Fluxo condicional alternativo caso o 'if' seja falso
                        Box( // Contentor Compose: Sobrepõe os elementos filhos
                            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                .size(36.dp)
                                .background(LMGray100, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon( // Componente Compose: Desenha um ícone vetorial
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = LMGray600,
                                modifier = Modifier.size(16.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                            )
                        }
                    }
                }
            )

            // Cartão de apresentação do utilizador.
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
                    .background(LMInk, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                val nomeExibido = utilizadorLogado?.nome ?: "Utilizador LeagueMatch" // Declara constante local (leitura única)
                val tipoExibido = utilizadorLogado?.tipo?.descricao ?: "Participante" // Declara constante local (leitura única)

                Row(verticalAlignment = Alignment.CenterVertically) { // Contentor Compose: Alinha os filhos numa linha horizontal
                    Avatar(
                        name = nomeExibido,
                        size = 56.dp,
                        color = primaryColor
                    )

                    Spacer(modifier = Modifier.width(14.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                    Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
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
                            modifier = Modifier.padding(top = 1.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                        )

                        Spacer(modifier = Modifier.height(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                        Box( // Contentor Compose: Sobrepõe os elementos filhos
                            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
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

            Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            // Configurações gerais da aplicação.
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                CardWrapper(
                    modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                    pad = 0.dp
                ) {
                    Column { // Contentor Compose: Alinha os filhos numa coluna vertical
                        ProfileRow(
                            icon = Icons.Default.Language,
                            label = "Idioma",
                            onClick = { showLanguageDialog = true }, // Callback: Define a ação executada ao clicar no componente
                            rightContent = {
                                Text( // Componente Compose: Desenha texto estruturado no ecrã
                                    text = if (language == Language.PT) "Português ›" else "English ›", // Estrutura de decisão condicional principal
                                    fontFamily = Geist,
                                    fontSize = 12.sp,
                                    color = LMGray500
                                )
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(start = 58.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
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
                            modifier = Modifier.padding(start = 58.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                            color = LMBorder,
                            thickness = 1.dp
                        )

                        ProfileRow(
                            icon = Icons.Default.Palette,
                            label = "Cor da aplicação",
                            rightContent = {
                                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ColorOption(
                                        color = Color(0xFFE31734),
                                        selectedColor = primaryColor,
                                        onClick = { onPrimaryColorChange(Color(0xFFE31734)) } // Callback: Define a ação executada ao clicar no componente
                                    )

                                    ColorOption(
                                        color = Color(0xFF2563EB),
                                        selectedColor = primaryColor,
                                        onClick = { onPrimaryColorChange(Color(0xFF2563EB)) } // Callback: Define a ação executada ao clicar no componente
                                    )

                                    ColorOption(
                                        color = Color(0xFF16A34A),
                                        selectedColor = primaryColor,
                                        onClick = { onPrimaryColorChange(Color(0xFF16A34A)) } // Callback: Define a ação executada ao clicar no componente
                                    )

                                    ColorOption(
                                        color = Color(0xFF9333EA),
                                        selectedColor = primaryColor,
                                        onClick = { onPrimaryColorChange(Color(0xFF9333EA)) } // Callback: Define a ação executada ao clicar no componente
                                    )
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            // Permite verificar o estado da sincronização e terminar sessão.
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                CardWrapper(
                    modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                    pad = 0.dp
                ) {
                    Column { // Contentor Compose: Alinha os filhos numa coluna vertical
                        ProfileRow(
                            icon = Icons.Default.Sync,
                            label = if (alteracoesPendentes > 0) // Estrutura de decisão condicional principal
                                "Sincronizar ($alteracoesPendentes pendentes)"
                            else // Fluxo condicional alternativo caso o 'if' seja falso
                                "Sincronização offline",
                            onClick = { if (alteracoesPendentes > 0) onSincronizarPendentesClick() }, // Callback: Define a ação executada ao clicar no componente
                            rightContent = {
                                Pill(
                                    text = if (alteracoesPendentes > 0) "$alteracoesPendentes" else "Ativa", // Estrutura de decisão condicional principal
                                    kind = if (alteracoesPendentes > 0) "warn" else "live" // Estrutura de decisão condicional principal
                                )
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(start = 58.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                            color = LMBorder,
                            thickness = 1.dp
                        )

                        ProfileRow(
                            icon = Icons.AutoMirrored.Filled.Logout,
                            label = "Terminar sessão",
                            labelColor = LMRed,
                            iconTint = LMRed,
                            onClick = onTerminarSessaoClick, // Callback: Define a ação executada ao clicar no componente
                            rightContent = {
                                Icon( // Componente Compose: Desenha um ícone vetorial
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = primaryColor,
                                    modifier = Modifier.size(16.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                                )
                            }
                        )
                    }
                }
            }

            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = "LeagueMatch v1.0.0 · EI-3A-Grupo C · 2025/2026",
                fontFamily = Geist,
                fontSize = 10.sp,
                color = LMGray400,
                textAlign = TextAlign.Center,
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(vertical = 18.dp)
            )
        }
    }

    // Janela modal utilizada para editar os dados do perfil.
    if (showEditDialog) { // Estrutura de decisão condicional principal
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
                Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
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
                        label = if (language == Language.PT) "Nova palavra-passe (opcional)" else "New password (optional)", // Estrutura de decisão condicional principal
                        placeholder = "••••••••",
                        isPassword = true
                    )
                }
            },
            confirmButton = {
                Button( // Componente Compose: Desenha um botão interativo
                    onClick = { // Callback: Define a ação executada ao clicar no componente
                        if (newNome.isNotBlank()) { // Estrutura de decisão condicional principal
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
                    onClick = { // Callback: Define a ação executada ao clicar no componente
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
    if (showLanguageDialog) { // Estrutura de decisão condicional principal
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
                    Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ProfileRow(
                            icon = Icons.Default.Language,
                            label = "Português",
                            onClick = { // Callback: Define a ação executada ao clicar no componente
                                onLanguageChange(Language.PT)
                                showLanguageDialog = false
                            }
                        )

                        HorizontalDivider(color = LMBorder)

                        ProfileRow(
                            icon = Icons.Default.Language,
                            label = "English",
                            onClick = { // Callback: Define a ação executada ao clicar no componente
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
fun ProfileSwitchRow( // Declaração de função / método de lógica
    icon: ImageVector,
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box( // Contentor Compose: Sobrepõe os elementos filhos
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .size(32.dp)
                .background(LMGray100, RoundedCornerShape(9.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                imageVector = icon,
                contentDescription = null,
                tint = LMRed,
                modifier = Modifier.size(16.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )
        }

        Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

        Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = label,
                fontFamily = Geist,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = LMInk
            )

            Text( // Componente Compose: Desenha texto estruturado no ecrã
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
fun ProfileRow( // Declaração de função / método de lógica
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier, // Modificador Compose: Define tamanho, margem, padding ou clique
    iconTint: Color = LMInk,
    labelColor: Color = LMInk,
    onClick: () -> Unit = {}, // Callback: Define a ação executada ao clicar no componente
    rightContent: @Composable () -> Unit = {}
) {
    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() } // Callback: Define a ação executada ao clicar no componente
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box( // Contentor Compose: Sobrepõe os elementos filhos
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .size(32.dp)
                .background(LMGray100, RoundedCornerShape(9.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(16.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )
        }

        Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = label,
            fontFamily = Geist,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = labelColor,
            modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
        )

        rightContent()
    }
}

// Representa uma cor selecionável para personalizar
@Composable
fun ColorOption( // Declaração de função / método de lógica
    color: Color,
    selectedColor: Color,
    onClick: () -> Unit // Callback: Define a ação executada ao clicar no componente
) {
    Box( // Contentor Compose: Sobrepõe os elementos filhos
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .size(18.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                BorderStroke(
                    width = if (selectedColor == color) 2.dp else 0.dp, // Estrutura de decisão condicional principal
                    color = LMInk
                ),
                CircleShape
            )
            .clickable { onClick() } // Callback: Define a ação executada ao clicar no componente
    )
}