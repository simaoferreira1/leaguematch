package com.leaguematch.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import com.leaguematch.R
import com.leaguematch.ui.components.*
import com.leaguematch.ui.theme.*

@Composable
fun LoginScreen(
    erro: String? = null,
    onLoginClick: (String, String) -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            
            // Logo
            LMLogo(size = 32f, variant = "full")
            
            Spacer(modifier = Modifier.height(38.dp))

            // Headline
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Bem-vindo\nde volta.",
                    fontFamily = Bricolage,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LMInk,
                    letterSpacing = (-0.8).sp,
                    lineHeight = 34.sp
                )
                Text(
                    text = "Inicia sessão para continuar.",
                    fontFamily = Geist,
                    fontSize = 14.sp,
                    color = LMGray500,
                    modifier = Modifier.padding(top = 10.dp, bottom = 28.dp)
                )
            }

            // Input Email
            LeagueMatchTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                placeholder = "simao@leaguematch.com",
                icon = Icons.Default.Person
            )

            // Input Password
            LeagueMatchTextField(
                value = password,
                onValueChange = { password = it },
                label = "Palavra-passe",
                placeholder = "••••••••••",
                icon = Icons.Default.Lock,
                isPassword = true,
                error = erro
            )

            // Forgot Password button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-6).dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextBtn(
                    onClick = {
                        Toast.makeText(context, "Recuperação de password em desenvolvimento.", Toast.LENGTH_LONG).show()
                    }, 
                    color = LMGray600
                ) {
                    Text("Esqueceu-se da password?", fontSize = 13.sp, fontFamily = Geist)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Enter Button
            PrimaryBtn(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                    } else {
                        onLoginClick(email, password)
                    }
                },
                size = "lg"
            ) {
                Text("Entrar")
            }

            // Divider OR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 22.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = LMBorder
                )
                Text(
                    text = "OU",
                    color = LMGray400,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Geist,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = LMBorder
                )
            }

            // Google login Button
            GhostBtn(onClick = {
                Toast.makeText(context, "A autenticar via Google (Bypass Dev)...", Toast.LENGTH_SHORT).show()
                // Directly triggers bypass login for easy verification of dashboard
                onLoginClick("simao@leaguematch.com", "password")
            }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google Logo",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Continuar com Google", fontFamily = Geist, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Register bottom text
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Não tem conta? ",
                    fontFamily = Geist,
                    fontSize = 13.sp,
                    color = LMGray500
                )
                TextBtn(
                    onClick = {
                        Toast.makeText(context, "Criação de conta em desenvolvimento.", Toast.LENGTH_LONG).show()
                    }, 
                    color = LMInk
                ) {
                    Text("Criar conta", fontWeight = FontWeight.Bold, fontSize = 13.sp, fontFamily = Geist)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LeagueMatchTheme {
        LoginScreen(onLoginClick = { _, _ -> })
    }
}
