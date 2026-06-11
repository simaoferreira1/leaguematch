package com.leaguematch.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.R
import com.leaguematch.data.repository.TranslationRepository
import com.leaguematch.translations.Language
import com.leaguematch.ui.components.GhostBtn
import com.leaguematch.ui.components.LMLogo
import com.leaguematch.ui.components.LeagueMatchTextField
import com.leaguematch.ui.components.PrimaryBtn
import com.leaguematch.ui.components.TextBtn
import com.leaguematch.ui.components.TranslatedText
import com.leaguematch.ui.theme.Bricolage
import com.leaguematch.ui.theme.Geist
import com.leaguematch.ui.theme.LMBorder
import com.leaguematch.ui.theme.LMGray400
import com.leaguematch.ui.theme.LMGray500
import com.leaguematch.ui.theme.LMGray600
import com.leaguematch.ui.theme.LMInk

@Composable
fun LoginScreen(
    erro: String? = null,
    language: Language,
    onLanguageChange: (Language) -> Unit,
    translationRepository: TranslationRepository,
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showLanguageDialog by remember { mutableStateOf(false) }

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = {
                TranslatedText(
                    text = "Escolher idioma",
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    TextBtn(
                        onClick = {
                            onLanguageChange(Language.PT)
                            showLanguageDialog = false
                        },
                        color = LMInk
                    ) {
                        Text("Português", fontFamily = Geist)
                    }

                    TextBtn(
                        onClick = {
                            onLanguageChange(Language.EN)
                            showLanguageDialog = false
                        },
                        color = LMInk
                    ) {
                        Text("English", fontFamily = Geist)
                    }
                }
            },
            confirmButton = {}
        )
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
                .padding(horizontal = 22.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            LMLogo(size = 32f, variant = "full")

            Spacer(modifier = Modifier.height(38.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                TranslatedText(
                    text = "Bem-vindo\nde volta.",
                    fontFamily = Bricolage,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LMInk,
                    letterSpacing = (-0.8).sp,
                    lineHeight = 34.sp
                )

                TranslatedText(
                    text = "Inicia sessão para continuar.",
                    fontFamily = Geist,
                    fontSize = 14.sp,
                    color = LMGray500,
                    modifier = Modifier.padding(top = 10.dp, bottom = 28.dp)
                )
            }

            LeagueMatchTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                placeholder = "exemplo@leaguematch.com",
                icon = Icons.Default.Person
            )

            LeagueMatchTextField(
                value = password,
                onValueChange = { password = it },
                label = if (language == Language.PT) "Palavra-passe" else "Password",
                placeholder = "••••••••••",
                icon = Icons.Default.Lock,
                isPassword = true,
                error = erro
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-6).dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextBtn(
                    onClick = {
                        Toast.makeText(
                            context,
                            if (language == Language.PT)
                                "Recuperação de password em desenvolvimento."
                            else
                                "Password recovery under development.",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    color = LMGray600
                ) {
                    TranslatedText(
                        text = "Esqueceu-se da password?",
                        fontSize = 13.sp,
                        fontFamily = Geist
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            PrimaryBtn(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(
                            context,
                            if (language == Language.PT)
                                "Por favor, preencha todos os campos."
                            else
                                "Please fill in all fields.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        onLoginClick(email, password)
                    }
                },
                size = "lg"
            ) {
                TranslatedText(
                    text = "Entrar",
                )
            }

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

                TranslatedText(
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

            GhostBtn(
                onClick = {
                    Toast.makeText(
                        context,
                        if (language == Language.PT)
                            "A autenticar via Google (Bypass Dev)..."
                        else
                            "Authenticating with Google (Dev Bypass)...",
                        Toast.LENGTH_SHORT
                    ).show()

                    onLoginClick("simao@leaguematch.com", "password")
                }
            ) {
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

                    TranslatedText(
                        text = "Continuar com Google",
                        fontFamily = Geist,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TranslatedText(
                    text = "Não tem conta? ",
                    fontFamily = Geist,
                    fontSize = 13.sp,
                    color = LMGray500
                )

                TextBtn(
                    onClick = onRegisterClick,
                    color = LMInk
                ) {
                    TranslatedText(
                        text = "Criar conta",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        fontFamily = Geist
                    )
                }
            }
        }

        IconButton(
            onClick = { showLanguageDialog = true },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 32.dp, end = 18.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Language,
                contentDescription = "Idioma",
                tint = LMInk
            )
        }
    }
}