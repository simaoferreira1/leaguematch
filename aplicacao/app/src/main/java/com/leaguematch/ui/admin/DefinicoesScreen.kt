package com.leaguematch.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.ui.components.*
import com.leaguematch.ui.theme.*

@Composable
fun DefinicoesScreen(
    onTerminarSessaoClick: () -> Unit = {},
    onGerirNotificacoesClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onUtilizadoresClick: () -> Unit = {},
    onTorneiosClick: () -> Unit = {},
    onGraficosClick: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            AdminBottomBar(
                selectedItem = "definicoes",
                onHomeClick = onHomeClick,
                onUtilizadoresClick = onUtilizadoresClick,
                onTorneiosClick = onTorneiosClick,
                onGraficosClick = onGraficosClick,
                onDefinicoesClick = {}
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
            // Header TopBar
            TopBar(
                title = "Perfil",
                big = true,
                rightContent = {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(LMGray100, RoundedCornerShape(10.dp))
                            .clickable {},
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = LMGray600,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            )

            // Black Elevated Profile Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
                    .background(LMInk, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Avatar(
                        name = "Simão Ferreira",
                        size = 56.dp,
                        color = LMRed
                    )
                    
                    Spacer(modifier = Modifier.width(14.dp))
                    
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Simão Ferreira",
                            fontFamily = Bricolage,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LMWhite
                        )
                        
                        Text(
                            text = "Administrador · GD Rio Torto",
                            fontFamily = Geist,
                            fontSize = 11.sp,
                            color = LMWhite.copy(alpha = 0.6f),
                            modifier = Modifier.padding(top = 1.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(6.dp))
                        
                        // Mini outline edit profile button
                        Box(
                            modifier = Modifier
                                .background(LMWhite.copy(alpha = 0.1f), RoundedCornerShape(99.dp))
                                .border(
                                    BorderStroke(1.dp, LMWhite.copy(alpha = 0.2f)),
                                    RoundedCornerShape(99.dp)
                                )
                                .clickable {}
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
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

            Spacer(modifier = Modifier.height(10.dp))

            // Options Group 1 Card Wrapper
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
                        // Notificações Row
                        ProfileRow(
                            icon = Icons.Default.Notifications,
                            label = "Notificações",
                            onClick = onGerirNotificacoesClick,
                            rightContent = {
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = LMGray400,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                        HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = LMBorder, thickness = 1.dp)
                        
                        // Idioma Row
                        ProfileRow(
                            icon = Icons.Default.Language,
                            label = "Idioma",
                            rightContent = {
                                Text(
                                    text = "Português ›",
                                    fontFamily = Geist,
                                    fontSize = 12.sp,
                                    color = LMGray500
                                )
                            }
                        )
                        HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = LMBorder, thickness = 1.dp)

                        // Aparência Row
                        ProfileRow(
                            icon = Icons.Default.BrightnessMedium,
                            label = "Aparência",
                            rightContent = {
                                Text(
                                    text = "Sistema ›",
                                    fontFamily = Geist,
                                    fontSize = 12.sp,
                                    color = LMGray500
                                )
                            }
                        )
                        HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = LMBorder, thickness = 1.dp)

                        // Cor da aplicação Row
                        ProfileRow(
                            icon = Icons.Default.Palette,
                            label = "Cor da aplicação",
                            rightContent = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clip(CircleShape)
                                            .background(LMRed)
                                            .border(BorderStroke(1.5.dp, LMInk), CircleShape)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF2563EB))
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF16A34A))
                                    )
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Options Group 2 Card Wrapper
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
                        // Privacidade e Segurança Row
                        ProfileRow(
                            icon = Icons.Default.Security,
                            label = "Privacidade e segurança",
                            rightContent = {
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = LMGray400,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                        HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = LMBorder, thickness = 1.dp)

                        // Sincronização offline Row
                        ProfileRow(
                            icon = Icons.Default.Sync,
                            label = "Sincronização offline",
                            rightContent = {
                                Pill(text = "Ativa", kind = "live")
                            }
                        )
                        HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = LMBorder, thickness = 1.dp)

                        // Terminar sessão Row (Red)
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
                                    tint = LMRed,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                    }
                }
            }

            // Footer version
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
}

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
        // Icon wrapper
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

@Preview(showBackground = true)
@Composable
fun DefinicoesScreenPreview() {
    LeagueMatchTheme {
        DefinicoesScreen()
    }
}