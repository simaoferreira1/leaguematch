package com.leaguematch.ui.organizer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.leaguematch.data.remote.model.Equipa
import com.leaguematch.data.remote.model.TeamCode
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.ui.theme.*

@Composable
fun OrgGerirEquipasScreen(
    torneio: Torneio,
    equipas: List<Equipa>,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onCriarEquipaClick: () -> Unit,
    onRemoverEquipa: (Equipa) -> Unit,
    onEditarEquipa: (Equipa, String) -> Unit
) {
    var equipaParaRemover by remember { mutableStateOf<Equipa?>(null) }
    var equipaParaEditar by remember { mutableStateOf<Equipa?>(null) }

    if (equipaParaRemover != null) {
        AlertDialog(
            onDismissRequest = { equipaParaRemover = null },
            title = {
                Text("Remover equipa?", fontFamily = Geist, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Text(
                    "Tens a certeza que queres remover \"${equipaParaRemover!!.nome}\"?",
                    fontFamily = Geist, fontSize = 13.sp, color = LMGray600
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onRemoverEquipa(equipaParaRemover!!)
                    equipaParaRemover = null
                }) {
                    Text("Remover", color = LMRed, fontFamily = Geist, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { equipaParaRemover = null }) {
                    Text("Cancelar", fontFamily = Geist, color = LMGray500)
                }
            },
            shape = RoundedCornerShape(18.dp)
        )
    }

    if (equipaParaEditar != null) {
        var novoNome by remember { mutableStateOf(equipaParaEditar!!.nome) }
        AlertDialog(
            onDismissRequest = { equipaParaEditar = null },
            title = {
                Text("Editar Nome da Equipa", fontFamily = Geist, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Column {
                    Text(
                        "Altere o nome da equipa no torneio:",
                        fontFamily = Geist, fontSize = 12.sp, color = LMGray500,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = novoNome,
                        onValueChange = { novoNome = it },
                        placeholder = { Text("Nome da equipa", fontFamily = Geist, fontSize = 14.sp) },
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
                    Text("Guardar", color = if (novoNome.isNotBlank()) LMInk else LMGray400, fontFamily = Geist, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { equipaParaEditar = null }) {
                    Text("Cancelar", fontFamily = Geist, color = LMGray500)
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
                    Icon(Icons.Rounded.ArrowBack, contentDescription = null, tint = LMInk)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Gerir Equipas",
                        fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp, color = LMInk
                    )
                    Text(text = torneio.nome, fontFamily = Geist, fontSize = 12.sp, color = LMGray500)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "EQUIPAS NO TORNEIO · ${equipas.size}",
                fontFamily = Geist, fontWeight = FontWeight.Bold,
                fontSize = 11.sp, color = LMGray500, letterSpacing = 0.4.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
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
                            Icons.Default.Groups, contentDescription = null,
                            tint = LMGray300, modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Sem equipas neste torneio.", fontFamily = Geist, fontSize = 13.sp, color = LMGray500)
                        Text("Cria a primeira equipa abaixo.", fontFamily = Geist, fontSize = 12.sp, color = LMGray400)
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    equipas.forEach { equipa ->
                        EquipaListItem(
                            equipa = equipa,
                            onEditar = { equipaParaEditar = equipa },
                            onRemover = { equipaParaRemover = equipa }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onCriarEquipaClick,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LMInk)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = LMWhite, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Criar nova equipa", fontFamily = Geist, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = LMWhite)
            }
        }
    }
}

@Composable
private fun EquipaListItem(
    equipa: Equipa,
    onEditar: () -> Unit,
    onRemover: () -> Unit
) {
    val context = LocalContext.current
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
                    Icon(Icons.Default.Groups, contentDescription = null, tint = LMGray500, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = equipa.nome,
                    fontFamily = Geist, fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp, color = LMInk, modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onEditar, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = LMGray400, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = onRemover, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Remover", tint = LMGray400, modifier = Modifier.size(18.dp))
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
                        Text(
                            text = "CÓDIGO DE INTEGRAÇÃO",
                            fontFamily = Geist, fontWeight = FontWeight.Bold,
                            fontSize = 9.sp, color = LMGray500, letterSpacing = 0.6.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = codigo,
                            fontFamily = Geist, fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp, color = LMInk, letterSpacing = 2.sp
                        )
                    }
                    TextButton(onClick = { copiarParaClipboard(context, codigo) }) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "Copiar código",
                            tint = LMRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Copiar",
                            fontFamily = Geist, fontWeight = FontWeight.Bold,
                            fontSize = 12.sp, color = LMRed
                        )
                    }
                }
            }
        }
    }
}

private fun copiarParaClipboard(context: Context, texto: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("Código de equipa", texto))
    Toast.makeText(context, "Código copiado: $texto", Toast.LENGTH_SHORT).show()
}
