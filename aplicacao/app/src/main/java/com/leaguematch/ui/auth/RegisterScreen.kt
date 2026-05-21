package com.leaguematch.ui.auth

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.ui.components.*
import com.leaguematch.ui.theme.*

@Composable
fun RegisterScreen(
    erro: String? = null,
    sucesso: Boolean = false,
    onBackClick: () -> Unit,
    onRegisterClick: (String, String, String, String) -> Unit,
    onSuccessRedirect: () -> Unit
) {
    val context = LocalContext.current

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var selectedPerfil by remember { mutableStateOf("PARTICIPANTE") } // "PARTICIPANTE", "ORGANIZADOR", "ESPECTADOR"

    LaunchedEffect(sucesso) {
        if (sucesso) {
            Toast.makeText(context, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()
            onSuccessRedirect()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header TopBar with back button
            TopBar(
                title = "",
                back = true,
                onBackClick = onBackClick
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
                    .padding(bottom = 28.dp)
            ) {
                // Title & Subtitle
                Text(
                    text = "Criar conta.",
                    fontFamily = Bricolage,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LMInk,
                    letterSpacing = (-0.8).sp,
                    lineHeight = 34.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "Junta-te ao LeagueMatch e começa a competir.",
                    fontFamily = Geist,
                    fontSize = 14.sp,
                    color = LMGray500,
                    modifier = Modifier.padding(top = 8.dp, bottom = 22.dp)
                )

                // Error message banner
                if (erro != null) {
                    Text(
                        text = erro,
                        fontFamily = Geist,
                        fontSize = 13.sp,
                        color = LMRed700,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 14.dp)
                    )
                }

                // Input Nome completo
                LeagueMatchTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = "Nome completo",
                    placeholder = "Nome Completo"
                )

                // Input Email
                LeagueMatchTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    placeholder = "exemplo@leaguematch.com"
                )

                // Input Palavra-passe
                LeagueMatchTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Palavra-passe",
                    placeholder = "••••••••••",
                    isPassword = true
                )

                // Input Repetir password
                LeagueMatchTextField(
                    value = repeatPassword,
                    onValueChange = { repeatPassword = it },
                    label = "Repetir password",
                    placeholder = "••••••••••",
                    isPassword = true,
                    hint = "Mínimo 8 caracteres, 1 número e 1 símbolo."
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Perfil / Role Title
                Text(
                    text = "PERFIL",
                    fontFamily = Geist,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = LMGray600,
                    letterSpacing = 0.2.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Perfil Selection Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 18.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val perfis = listOf(
                        "PARTICIPANTE" to "Participante",
                        "ORGANIZADOR" to "Organizador",
                        "ESPECTADOR" to "Espectador"
                    )

                    perfis.forEach { (perfilCode, label) ->
                        val isSelected = selectedPerfil == perfilCode
                        
                        val containerColor by animateColorAsState(
                            targetValue = if (isSelected) LMRed50 else LMWhite,
                            animationSpec = tween(150),
                            label = "perfilBg"
                        )
                        val borderColor by animateColorAsState(
                            targetValue = if (isSelected) LMRed else LMBorder,
                            animationSpec = tween(150),
                            label = "perfilBorder"
                        )
                        val textColor by animateColorAsState(
                            targetValue = if (isSelected) LMRed700 else LMGray700,
                            animationSpec = tween(150),
                            label = "perfilText"
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(containerColor)
                                .border(
                                    BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor),
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedPerfil = perfilCode }
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontFamily = Geist,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Register Button
                PrimaryBtn(
                    onClick = {
                        when {
                            nome.isBlank() || email.isBlank() || password.isBlank() || repeatPassword.isBlank() -> {
                                Toast.makeText(context, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                            }
                            password.length < 8 -> {
                                Toast.makeText(context, "A password deve ter pelo menos 8 caracteres.", Toast.LENGTH_SHORT).show()
                            }
                            password != repeatPassword -> {
                                Toast.makeText(context, "As passwords introduzidas não coincidem.", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                onRegisterClick(nome, email, password, selectedPerfil)
                            }
                        }
                    },
                    size = "lg"
                ) {
                    Text("Registar")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    LeagueMatchTheme {
        RegisterScreen(
            onBackClick = {},
            onRegisterClick = { _, _, _, _ -> },
            onSuccessRedirect = {}
        )
    }
}
