package com.leaguematch.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.ui.components.*
import com.leaguematch.ui.theme.*

@Composable
fun DetalheUtilizadorScreen(
    nome: String = "Simão Rodrigues Ferreira",
    email: String = "fsimao530@gmail.com",
    tipo: String = "Organizador",
    equipas: Int = 2,
    torneios: Int = 1,
    jogos: Int = 18,
    golos: Int = 12,
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onUtilizadoresClick: () -> Unit = {},
    onTorneiosClick: () -> Unit = {},
    onGraficosClick: () -> Unit = {},
    onDefinicoesClick: () -> Unit = {},
    onRemoverClick: () -> Unit = {},
    onGuardarClick: (String) -> Unit = {}
) {
    var tipoSelecionado by remember(tipo) { mutableStateOf(tipo) }

    Scaffold(
        bottomBar = {
            AdminBottomBar(
                selectedItem = "utilizadores",
                onHomeClick = onHomeClick,
                onUtilizadoresClick = onUtilizadoresClick,
                onTorneiosClick = onTorneiosClick,
                onGraficosClick = onGraficosClick,
                onDefinicoesClick = onDefinicoesClick
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
                title = "Utilizador",
                back = true,
                onBackClick = onBackClick,
                rightContent = {
                    TextBtn(onClick = onRemoverClick, color = LMRed) {
                        Text("Remover", fontFamily = Geist, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                }
            )

            // Profile info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Avatar(
                    name = nome,
                    size = 86.dp,
                    color = LMRed
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Simão R. Ferreira or name
                Text(
                    text = nome,
                    fontFamily = Bricolage,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LMInk,
                    letterSpacing = (-0.4).sp,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = email,
                    fontFamily = Geist,
                    fontSize = 12.sp,
                    color = LMGray500,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = "Membro desde 12 Mar 2025",
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    color = LMGray400,
                    textAlign = TextAlign.Center
                )
            }

            // Stats grid (4 blocks)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val stats = listOf(
                    "Equipas" to equipas,
                    "Torneios" to torneios,
                    "Jogos" to jogos,
                    "Golos" to golos
                )
                
                stats.forEach { (label, value) ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(LMGray100, RoundedCornerShape(12.dp))
                            .padding(vertical = 10.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = value.toString(),
                            fontFamily = GeistMono,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LMInk
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = label.uppercase(),
                            fontFamily = Geist,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = LMGray500,
                            letterSpacing = 0.4.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Tipo de Utilizador Selection Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "TIPO DE UTILIZADOR",
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = LMGray500,
                    letterSpacing = 0.4.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val options = listOf("Organizador", "Participante", "Espectador")
                    
                    options.forEach { option ->
                        val isActive = tipoSelecionado.lowercase() == option.lowercase()
                        
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = if (isActive) LMRed50 else LMWhite,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .border(
                                    BorderStroke(if (isActive) 1.5.dp else 1.dp, if (isActive) LMRed else LMBorder),
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { tipoSelecionado = option }
                                .padding(vertical = 9.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = option,
                                fontFamily = Geist,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isActive) LMRed700 else LMGray700
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Contactos de Equipa Card Wrapper Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
            ) {
                Text(
                    text = "CONTACTOS DE EQUIPA",
                    fontFamily = Geist,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = LMGray500,
                    letterSpacing = 0.4.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                CardWrapper(
                    modifier = Modifier.fillMaxWidth(),
                    pad = 0.dp
                ) {
                    val teams = listOf(
                        "GD Rio Torto" to "Capitão",
                        "Bola Parada FC" to "Membro"
                    )
                    
                    Column {
                        teams.forEachIndexed { index, (teamName, role) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TeamCrest(name = teamName, size = 26.dp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = teamName,
                                    fontFamily = Geist,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = LMInk,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = role,
                                    fontFamily = Geist,
                                    fontSize = 11.sp,
                                    color = LMGray500
                                )
                            }
                            
                            if (index < teams.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(start = 50.dp),
                                    color = LMBorder,
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }
            }

            // Save and Reset Password actions at bottom
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 18.dp)
            ) {
                PrimaryBtn(
                    onClick = { onGuardarClick(tipoSelecionado) },
                    size = "lg"
                ) {
                    Text("Guardar alterações", color = LMWhite)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                GhostBtn(
                    onClick = {}
                ) {
                    Text("Repor password", color = LMInk)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetalheUtilizadorScreenPreview() {
    LeagueMatchTheme {
        DetalheUtilizadorScreen()
    }
}
