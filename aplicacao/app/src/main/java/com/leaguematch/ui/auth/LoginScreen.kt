/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: LoginScreen.kt
 * Tipo: Lógica Utilitária / Auxiliar
 *
 * Descrição:
 * Contém funções utilitárias ou auxiliares transversais à aplicação.
 */
package com.leaguematch.ui.auth // Define o pacote deste ficheiro de código

import android.widget.Toast // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Arrangement // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Box // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Column // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Row // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Spacer // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.fillMaxSize // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.fillMaxWidth // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.height // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.offset // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.padding // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.size // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.width // Importa dependência / biblioteca necessária
import androidx.compose.foundation.rememberScrollState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Language // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Lock // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Person // Importa dependência / biblioteca necessária
import androidx.compose.material3.AlertDialog // Importa dependência / biblioteca necessária
import androidx.compose.material3.HorizontalDivider // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.IconButton // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.remember // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.platform.LocalContext // Importa dependência / biblioteca necessária
import androidx.compose.ui.res.painterResource // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.R // Importa dependência / biblioteca necessária
import com.leaguematch.data.repository.TranslationRepository // Importa dependência / biblioteca necessária
import com.leaguematch.translations.Language // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.GhostBtn // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.LMLogo // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.LeagueMatchTextField // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.PrimaryBtn // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TextBtn // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Bricolage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMBorder // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray400 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray600 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária


@Composable
fun LoginScreen( // Declaração de função / método de lógica
    erro: String? = null,
    language: Language,
    onLanguageChange: (Language) -> Unit,
    translationRepository: TranslationRepository,
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit
) {
    val context = LocalContext.current // Declara constante local (leitura única)
    var email by remember { mutableStateOf("") } // Declara estado mutável local do Compose
    var password by remember { mutableStateOf("") } // Declara estado mutável local do Compose
    var showLanguageDialog by remember { mutableStateOf(false) } // Declara estado mutável local do Compose

    if (showLanguageDialog) { // Estrutura de decisão condicional principal
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            shape = RoundedCornerShape(18.dp),
            containerColor = Color.White,
            title = {
                TranslatedText(
                    text = "Escolher idioma",
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Column { // Contentor Compose: Alinha os filhos numa coluna vertical
                    TextBtn(
                        onClick = { // Callback: Define a ação executada ao clicar no componente
                            onLanguageChange(Language.PT)
                            showLanguageDialog = false
                        },
                        color = LMInk
                    ) {
                        Text("Português", fontFamily = Geist) // Componente Compose: Desenha texto estruturado no ecrã
                    }

                    TextBtn(
                        onClick = { // Callback: Define a ação executada ao clicar no componente
                            onLanguageChange(Language.EN)
                            showLanguageDialog = false
                        },
                        color = LMInk
                    ) {
                        Text("English", fontFamily = Geist) // Componente Compose: Desenha texto estruturado no ecrã
                    }
                }
            },
            confirmButton = {}
        )
    }

    Box( // Contentor Compose: Sobrepõe os elementos filhos
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            LMLogo(size = 32f, variant = "full")

            Spacer(modifier = Modifier.height(38.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
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
                    modifier = Modifier.padding(top = 10.dp, bottom = 28.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
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
                label = if (language == Language.PT) "Palavra-passe" else "Password", // Estrutura de decisão condicional principal
                placeholder = "••••••••••",
                icon = Icons.Default.Lock,
                isPassword = true,
                error = erro
            )

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .offset(y = (-6).dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextBtn(
                    onClick = { // Callback: Define a ação executada ao clicar no componente
                        Toast.makeText(
                            context,
                            if (language == Language.PT) // Estrutura de decisão condicional principal
                                "Contacte o administrador para repor a sua palavra-passe."
                            else // Fluxo condicional alternativo caso o 'if' seja falso
                                "Please contact the administrator to reset your password.",
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

            Spacer(modifier = Modifier.height(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            PrimaryBtn(
                onClick = { // Callback: Define a ação executada ao clicar no componente
                    if (email.isBlank() || password.isBlank()) { // Estrutura de decisão condicional principal
                        Toast.makeText(
                            context,
                            if (language == Language.PT) // Estrutura de decisão condicional principal
                                "Por favor, preencha todos os campos."
                            else // Fluxo condicional alternativo caso o 'if' seja falso
                                "Please fill in all fields.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else { // Fluxo condicional alternativo caso o 'if' seja falso
                        onLoginClick(email, password)
                    }
                },
                size = "lg"
            ) {
                TranslatedText(
                    text = "Entrar",
                )
            }

            Spacer(modifier = Modifier.height(28.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
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
                    onClick = onRegisterClick, // Callback: Define a ação executada ao clicar no componente
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

        IconButton( // Componente Compose: Desenha um botão com ícone
            onClick = { showLanguageDialog = true }, // Callback: Define a ação executada ao clicar no componente
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .align(Alignment.TopEnd)
                .padding(top = 32.dp, end = 18.dp)
        ) {
            Icon( // Componente Compose: Desenha um ícone vetorial
                imageVector = Icons.Default.Language,
                contentDescription = "Idioma",
                tint = LMInk
            )
        }
    }
}