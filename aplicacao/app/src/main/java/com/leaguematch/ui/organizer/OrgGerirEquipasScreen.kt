package com.leaguematch.ui.organizer

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.data.remote.model.Equipa
import com.leaguematch.data.remote.model.TeamCode
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.ui.components.LocalLanguage
import com.leaguematch.ui.components.LocalTranslationRepository
import com.leaguematch.ui.components.TranslatedText
import com.leaguematch.ui.components.showTranslatedToast
import com.leaguematch.ui.theme.Geist
import com.leaguematch.ui.theme.LMBorder
import com.leaguematch.ui.theme.LMGray100
import com.leaguematch.ui.theme.LMGray300
import com.leaguematch.ui.theme.LMGray400
import com.leaguematch.ui.theme.LMGray50
import com.leaguematch.ui.theme.LMGray500
import com.leaguematch.ui.theme.LMGray600
import com.leaguematch.ui.theme.LMInk
import com.leaguematch.ui.theme.LMRed
import com.leaguematch.ui.theme.LMWhite
import kotlinx.coroutines.launch

@Composable
fun OrgGerirEquipasScreen(
    torneio: Torneio,
    equipas: List<Equipa>,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onCriarEquipaClick: () -> Unit,
    onRemoverEquipa: (Equipa) -> Unit,
    onEditarEquipa: (Equipa, String) -> Unit,
    onGerirJogadores: (Equipa) -> Unit = {}
) {
    var equipaParaRemover by remember { mutableStateOf<Equipa?>(null) }
    var equipaParaEditar by remember { mutableStateOf<Equipa?>(null) }

    if (equipaParaRemover != null) {
        AlertDialog(
            onDismissRequest = { equipaParaRemover = null },
            title = {
                TranslatedText(
                    text = "Remover equipa?",
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                TranslatedText(
                    text = "Tens a certeza que queres remover \"${equipaParaRemover!!.nome}\"?",
                    fontFamily = Geist,
                    fontSize = 13.sp,
                    color = LMGray600
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRemoverEquipa(equipaParaRemover!!)
                        equipaParaRemover = null
                    }
                ) {
                    TranslatedText(
                        text = "Remover",
                        color = LMRed,
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { equipaParaRemover = null }) {
                    TranslatedText(
                        text = "Cancelar",
                        fontFamily = Geist,
                        color = LMGray500
                    )
                }
            },
            shape = RoundedCornerShape(18.dp)
        )
    }

    if (equipaParaEditar != null) {
        var novoNome by remember(equipaParaEditar) {
            mutableStateOf(equipaParaEditar!!.nome)
        }

        AlertDialog(
            onDismissRequest = { equipaParaEditar = null },
            title = {
                TranslatedText(
                    text = "Editar Nome da Equipa",
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column {
                    TranslatedText(
                        text = "Altere o nome da equipa no torneio:",
                        fontFamily = Geist,
                        fontSize = 12.sp,
                        color = LMGray500,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = novoNome,
                        onValueChange = { novoNome = it },
                        placeholder = {
                            TranslatedText(
                                text = "Nome da equipa",
                                fontFamily = Geist,
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LMInk,
                            unfocusedBorderColor = LMBorder
                        ),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (novoNome.isNotBlank()) {
                            onEditarEquipa(equipaParaEditar!!, novoNome)
                            equipaParaEditar = null
                        }
                    },
                    enabled = novoNome.isNotBlank()
                ) {
                    TranslatedText(
                        text = "Guardar",
                        color = if (novoNome.isNotBlank()) LMInk else LMGray400,
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { equipaParaEditar = null }) {
                    TranslatedText(
                        text = "Cancelar",
                        fontFamily = Geist,
                        color = LMGray500
                    )
                }
            },
            shape = RoundedCornerShape(18.dp)
        )
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
                .padding(bottom = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = null,
                        tint = LMInk
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    TranslatedText(
                        text = "Gerir Equipas",
                        fontFamily = Geist,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = LMInk
                    )

                    Text(
                        text = torneio.nome,
                        fontFamily = Geist,
                        fontSize = 12.sp,
                        color = LMGray500
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TranslatedText(
                text = "EQUIPAS NO TORNEIO · ${equipas.size}",
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = LMGray500,
                letterSpacing = 0.4.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = LMRed)
                }
            } else if (equipas.isEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = LMGray50,
                    border = BorderStroke(1.dp, LMBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = null,
                            tint = LMGray300,
                            modifier = Modifier.size(40.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        TranslatedText(
                            text = "Sem equipas neste torneio.",
                            fontFamily = Geist,
                            fontSize = 13.sp,
                            color = LMGray500
                        )

                        TranslatedText(
                            text = "Cria a primeira equipa abaixo.",
                            fontFamily = Geist,
                            fontSize = 12.sp,
                            color = LMGray400
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    equipas.forEach { equipa ->
                        EquipaListItem(
                            equipa = equipa,
                            onEditar = { equipaParaEditar = equipa },
                            onRemover = { equipaParaRemover = equipa },
                            onGerirJogadores = { onGerirJogadores(equipa) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onCriarEquipaClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LMInk)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = LMWhite,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                TranslatedText(
                    text = "Criar nova equipa",
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = LMWhite
                )
            }
        }
    }
}

@Composable
private fun EquipaListItem(
    equipa: Equipa,
    onEditar: () -> Unit,
    onRemover: () -> Unit,
    onGerirJogadores: () -> Unit
) {
    val context = LocalContext.current
    val language = LocalLanguage.current
    val translationRepository = LocalTranslationRepository.current
    val scope = rememberCoroutineScope()

    val codigo = TeamCode.encode(equipa.id)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, LMBorder),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(LMGray100, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = null,
                        tint = LMGray500,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = equipa.nome,
                    fontFamily = Geist,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = LMInk,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onGerirJogadores,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Jogadores",
                        tint = LMGray400,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(
                    onClick = onEditar,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = LMGray400,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(
                    onClick = onRemover,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Remover",
                        tint = LMGray400,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = LMGray50,
                border = BorderStroke(1.dp, LMBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        TranslatedText(
                            text = "CÓDIGO DE INTEGRAÇÃO",
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = LMGray500,
                            letterSpacing = 0.6.sp
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = codigo,
                            fontFamily = Geist,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = LMInk,
                            letterSpacing = 2.sp
                        )
                    }

                    TextButton(
                        onClick = {
                            scope.launch {
                                copiarParaClipboard(
                                    context = context,
                                    texto = codigo,
                                    language = language,
                                    translationRepository = translationRepository
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copiar código",
                            tint = LMRed,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        TranslatedText(
                            text = "Copiar",
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = LMRed
                        )
                    }
                }
            }
        }
    }
}

private suspend fun copiarParaClipboard(
    context: Context,
    texto: String,
    language: com.leaguematch.translations.Language,
    translationRepository: com.leaguematch.data.repository.TranslationRepository?
) {
    val clipboard =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    clipboard.setPrimaryClip(
        ClipData.newPlainText("Código de equipa", texto)
    )

    showTranslatedToast(
        context = context,
        text = "Código copiado: $texto",
        language = language,
        translationRepository = translationRepository,
        duration = Toast.LENGTH_SHORT
    )
}