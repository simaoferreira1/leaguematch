package com.leaguematch.ui.participant

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.TeamCode
import com.leaguematch.ui.theme.*

@Composable
fun ParticipantJoinTeamScreen(
    isLoading: Boolean,
    erro: String?,
    onBackClick: () -> Unit,
    onConfirmClick: (codigo: String) -> Unit
) {
    var codigo by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboard?.show()
    }

    Scaffold(
        containerColor = Color(0xFFF6F6F8)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 18.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = LMInk
                        )
                    }
                    Text(
                        text = "Equipas",
                        fontFamily = Bricolage,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 26.sp,
                        color = LMInk
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
                color = LMWhite,
                shadowElevation = 12.dp
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 22.dp, vertical = 22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 40.dp, height = 4.dp)
                            .clip(RoundedCornerShape(99.dp))
                            .background(Color(0xFFD4D4DA))
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFFFEAEC)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode2,
                            contentDescription = null,
                            tint = LMRed,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Integrar Equipa",
                        fontFamily = Bricolage,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = LMInk
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Introduz o código que recebeste do organizador da equipa.",
                        fontFamily = Geist,
                        fontSize = 12.sp,
                        color = LMGray500,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    CodeBoxes(
                        value = codigo,
                        length = TeamCode.LENGTH,
                        focusRequester = focusRequester,
                        onChange = { novo ->
                            codigo = novo.uppercase().filter {
                                it.isLetterOrDigit()
                            }.take(TeamCode.LENGTH)
                        }
                    )

                    if (erro != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = erro,
                            fontFamily = Geist,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = { onConfirmClick(codigo) },
                        enabled = !isLoading && codigo.length == TeamCode.LENGTH,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LMRed)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = LMWhite,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Text(
                                text = "Confirmar e entrar",
                                fontFamily = Geist,
                                fontWeight = FontWeight.ExtraBold,
                                color = LMWhite
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(
                        onClick = onBackClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Cancelar",
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            color = LMGray500
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CodeBoxes(
    value: String,
    length: Int,
    focusRequester: FocusRequester,
    onChange: (String) -> Unit
) {
    Box {
        BasicTextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier
                .focusRequester(focusRequester)
                .size(1.dp),
            textStyle = TextStyle(color = Color.Transparent),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Characters,
                imeAction = ImeAction.Done
            )
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(length) { index ->
                val char = value.getOrNull(index)?.toString().orEmpty()
                val filled = char.isNotEmpty()
                Box(
                    modifier = Modifier
                        .size(width = 38.dp, height = 48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (filled) Color(0xFFFFEAEC) else LMWhite)
                        .border(
                            border = BorderStroke(
                                width = 1.5.dp,
                                color = if (filled) LMRed else LMInk
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = char,
                        fontFamily = Bricolage,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = if (filled) LMRed else LMInk
                    )
                }
            }
        }
    }
}

