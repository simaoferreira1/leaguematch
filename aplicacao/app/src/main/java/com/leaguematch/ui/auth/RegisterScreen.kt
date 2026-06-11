package com.leaguematch.ui.auth

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.ui.components.LocalLanguage
import com.leaguematch.ui.components.LocalTranslationRepository
import com.leaguematch.ui.components.PrimaryBtn
import com.leaguematch.ui.components.TopBar
import com.leaguematch.ui.components.TranslatedLeagueMatchTextField
import com.leaguematch.ui.components.TranslatedText
import com.leaguematch.ui.components.showTranslatedToast
import com.leaguematch.ui.theme.Bricolage
import com.leaguematch.ui.theme.Geist
import com.leaguematch.ui.theme.LMBorder
import com.leaguematch.ui.theme.LMGray500
import com.leaguematch.ui.theme.LMGray600
import com.leaguematch.ui.theme.LMGray700
import com.leaguematch.ui.theme.LMInk
import com.leaguematch.ui.theme.LMRed
import com.leaguematch.ui.theme.LMRed50
import com.leaguematch.ui.theme.LMRed700
import com.leaguematch.ui.theme.LMWhite
import com.leaguematch.ui.theme.LeagueMatchTheme
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    erro: String? = null,
    sucesso: Boolean = false,
    onBackClick: () -> Unit,
    onRegisterClick: (String, String, String, String) -> Unit,
    onSuccessRedirect: () -> Unit
) {
    val context = LocalContext.current
    val language = LocalLanguage.current
    val translationRepository = LocalTranslationRepository.current
    val scope = rememberCoroutineScope()

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var selectedPerfil by remember { mutableStateOf("PARTICIPANTE") } // "PARTICIPANTE", "ORGANIZADOR", "ESPECTADOR"

    LaunchedEffect(sucesso) {
        if (sucesso) {
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
                TranslatedText(
                    text = "Criar conta.",
                    fontFamily = Bricolage,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LMInk,
                    letterSpacing = (-0.8).sp,
                    lineHeight = 34.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
                TranslatedText(
                    text = "Junta-te ao LeagueMatch e começa a competir.",
                    fontFamily = Geist,
                    fontSize = 14.sp,
                    color = LMGray500,
                    modifier = Modifier.padding(top = 8.dp, bottom = 22.dp)
                )

                // Error message banner
                if (erro != null) {
                    TranslatedText(
                        text = erro,
                        fontFamily = Geist,
                        fontSize = 13.sp,
                        color = LMRed700,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 14.dp)
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

                Spacer(modifier = Modifier.height(6.dp))

                // Perfil / Role Title
                TranslatedText(
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

                Spacer(modifier = Modifier.height(18.dp))

                // Register Button
                PrimaryBtn(
                    onClick = {
                        when {
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
                            else -> {
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
fun RegisterScreenPreview() {
    LeagueMatchTheme {
        RegisterScreen(
            onBackClick = {},
            onRegisterClick = { _, _, _, _ -> },
            onSuccessRedirect = {}
        )
    }
}
