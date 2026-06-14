/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: RegisterScreen.kt
 * Tipo: Lógica Utilitária / Auxiliar
 *
 * Descrição:
 * Contém funções utilitárias ou auxiliares transversais à aplicação.
 */
package com.leaguematch.ui.auth // Define o pacote deste ficheiro de código

import android.widget.Toast // Importa dependência / biblioteca necessária
import androidx.compose.animation.animateColorAsState // Importa dependência / biblioteca necessária
import androidx.compose.animation.core.tween // Importa dependência / biblioteca necessária
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
import androidx.compose.foundation.rememberScrollState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.LaunchedEffect // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.remember // Importa dependência / biblioteca necessária
import androidx.compose.runtime.rememberCoroutineScope // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.draw.clip // Importa dependência / biblioteca necessária
import androidx.compose.ui.platform.LocalContext // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.tooling.preview.Preview // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.LocalLanguage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.LocalTranslationRepository // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.PrimaryBtn // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TopBar // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedLeagueMatchTextField // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.showTranslatedToast // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Bricolage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMBorder // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray600 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray700 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed50 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed700 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LeagueMatchTheme // Importa dependência / biblioteca necessária
import kotlinx.coroutines.launch // Importa dependência / biblioteca necessária

@Composable
fun RegisterScreen( // Declaração de função / método de lógica
    erro: String? = null,
    sucesso: Boolean = false,
    onBackClick: () -> Unit,
    onRegisterClick: (String, String, String, String) -> Unit,
    onSuccessRedirect: () -> Unit
) {
    val context = LocalContext.current // Declara constante local (leitura única)
    val language = LocalLanguage.current // Declara constante local (leitura única)
    val translationRepository = LocalTranslationRepository.current // Declara constante local (leitura única)
    val scope = rememberCoroutineScope() // Cria escopo local para lançar coroutines em cliques na UI

    var nome by remember { mutableStateOf("") } // Declara estado mutável local do Compose
    var email by remember { mutableStateOf("") } // Declara estado mutável local do Compose
    var password by remember { mutableStateOf("") } // Declara estado mutável local do Compose
    var repeatPassword by remember { mutableStateOf("") } // Declara estado mutável local do Compose
    var selectedPerfil by remember { mutableStateOf("PARTICIPANTE") } // "PARTICIPANTE", "ORGANIZADOR", "ESPECTADOR"

    LaunchedEffect(sucesso) { // Efeito colateral Compose: executa código assíncrono ao recompor
        if (sucesso) { // Estrutura de decisão condicional principal
            showTranslatedToast(
                context = context,
                text = "Conta criada com sucesso!",
                language = language,
                translationRepository = translationRepository,
                duration = Toast.LENGTH_SHORT
            )
            onSuccessRedirect()
        }
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
        ) {
            // Header TopBar with back button
            TopBar(
                title = "",
                back = true,
                onBackClick = onBackClick
            )

            Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
                    .padding(bottom = 28.dp)
            ) {
                // Title & Subtitle
                TranslatedText(
                    text = "Criar conta.",
                    fontFamily = Bricolage,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LMInk,
                    letterSpacing = (-0.8).sp,
                    lineHeight = 34.sp,
                    modifier = Modifier.padding(top = 8.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
                TranslatedText(
                    text = "Junta-te ao LeagueMatch e começa a competir.",
                    fontFamily = Geist,
                    fontSize = 14.sp,
                    color = LMGray500,
                    modifier = Modifier.padding(top = 8.dp, bottom = 22.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                // Error message banner
                if (erro != null) { // Estrutura de decisão condicional principal
                    TranslatedText(
                        text = erro,
                        fontFamily = Geist,
                        fontSize = 13.sp,
                        color = LMRed700,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 14.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                }

                // Input Nome completo
                TranslatedLeagueMatchTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = "Nome completo",
                    placeholder = "Nome Completo"
                )

                // Input Email
                TranslatedLeagueMatchTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    placeholder = "exemplo@leaguematch.com"
                )

                // Input Palavra-passe
                TranslatedLeagueMatchTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Palavra-passe",
                    placeholder = "••••••••••",
                    isPassword = true
                )

                // Input Repetir password
                TranslatedLeagueMatchTextField(
                    value = repeatPassword,
                    onValueChange = { repeatPassword = it },
                    label = "Repetir password",
                    placeholder = "••••••••••",
                    isPassword = true,
                    hint = "Mínimo 8 caracteres, 1 número e 1 símbolo."
                )

                Spacer(modifier = Modifier.height(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                // Perfil / Role Title
                TranslatedText(
                    text = "PERFIL",
                    fontFamily = Geist,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = LMGray600,
                    letterSpacing = 0.2.sp,
                    modifier = Modifier.padding(bottom = 8.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                // Perfil Selection Row
                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .fillMaxWidth()
                        .padding(bottom = 18.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val perfis = listOf( // Declara constante local (leitura única)
                        "PARTICIPANTE" to "Participante",
                        "ORGANIZADOR" to "Organizador",
                        "ESPECTADOR" to "Espectador"
                    )

                    perfis.forEach { (perfilCode, label) ->
                        val isSelected = selectedPerfil == perfilCode // Declara constante local (leitura única)
                        
                        val containerColor by animateColorAsState( // Declara constante local (leitura única)
                            targetValue = if (isSelected) LMRed50 else LMWhite, // Estrutura de decisão condicional principal
                            animationSpec = tween(150),
                            label = "perfilBg"
                        )
                        val borderColor by animateColorAsState( // Declara constante local (leitura única)
                            targetValue = if (isSelected) LMRed else LMBorder, // Estrutura de decisão condicional principal
                            animationSpec = tween(150),
                            label = "perfilBorder"
                        )
                        val textColor by animateColorAsState( // Declara constante local (leitura única)
                            targetValue = if (isSelected) LMRed700 else LMGray700, // Estrutura de decisão condicional principal
                            animationSpec = tween(150),
                            label = "perfilText"
                        )

                        Box( // Contentor Compose: Sobrepõe os elementos filhos
                            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(containerColor)
                                .border(
                                    BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor), // Estrutura de decisão condicional principal
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedPerfil = perfilCode }
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            TranslatedText(
                                text = label,
                                fontFamily = Geist,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                // Register Button
                PrimaryBtn(
                    onClick = { // Callback: Define a ação executada ao clicar no componente
                        when { // Escolha múltipla condicional (semelhante a switch-case)
                            nome.isBlank() || email.isBlank() || password.isBlank() || repeatPassword.isBlank() -> {
                                scope.launch {
                                    showTranslatedToast(
                                        context = context,
                                        text = "Por favor, preencha todos os campos.",
                                        language = language,
                                        translationRepository = translationRepository,
                                        duration = Toast.LENGTH_SHORT
                                    )
                                }
                            }
                            password.length < 8 -> {
                                scope.launch {
                                    showTranslatedToast(
                                        context = context,
                                        text = "A password deve ter pelo menos 8 caracteres.",
                                        language = language,
                                        translationRepository = translationRepository,
                                        duration = Toast.LENGTH_SHORT
                                    )
                                }
                            }
                            password != repeatPassword -> {
                                scope.launch {
                                    showTranslatedToast(
                                        context = context,
                                        text = "As passwords introduzidas não coincidem.",
                                        language = language,
                                        translationRepository = translationRepository,
                                        duration = Toast.LENGTH_SHORT
                                    )
                                }
                            }
                            else -> { // Fluxo condicional alternativo caso o 'if' seja falso
                                onRegisterClick(nome, email, password, selectedPerfil)
                            }
                        }
                    },
                    size = "lg"
                ) {
                    TranslatedText("Registar")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() { // Declaração de função / método de lógica
    LeagueMatchTheme {
        RegisterScreen(
            onBackClick = {},
            onRegisterClick = { _, _, _, _ -> },
            onSuccessRedirect = {}
        )
    }
}
