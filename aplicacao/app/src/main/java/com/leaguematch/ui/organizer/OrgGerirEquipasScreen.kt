/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: OrgGerirEquipasScreen.kt
 * Tipo: Interface (Compose View) do Organizador
 *
 * Descrição:
 * Este ficheiro define um ecrã da área do Organizador em Jetpack Compose.\n * Fornece interface e lógica visual para criar torneios, gerir equipas, registar e editar jogos e estatísticas.
 */
package com.leaguematch.ui.organizer // Define o pacote deste ficheiro de código

import android.content.ClipData // Importa dependência / biblioteca necessária
import android.content.ClipboardManager // Importa dependência / biblioteca necessária
import android.content.Context // Importa dependência / biblioteca necessária
import android.widget.Toast // Importa dependência / biblioteca necessária
import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Arrangement // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Box // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Column // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Row // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.Spacer // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.fillMaxSize // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.fillMaxWidth // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.height // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.padding // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.size // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.width // Importa dependência / biblioteca necessária
import androidx.compose.foundation.rememberScrollState // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.verticalScroll // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Add // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.ContentCopy // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.DeleteOutline // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Edit // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Groups // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.Person // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.rounded.ArrowBack // Importa dependência / biblioteca necessária
import androidx.compose.material3.AlertDialog // Importa dependência / biblioteca necessária
import androidx.compose.material3.Button // Importa dependência / biblioteca necessária
import androidx.compose.material3.ButtonDefaults // Importa dependência / biblioteca necessária
import androidx.compose.material3.CircularProgressIndicator // Importa dependência / biblioteca necessária
import androidx.compose.material3.Icon // Importa dependência / biblioteca necessária
import androidx.compose.material3.IconButton // Importa dependência / biblioteca necessária
import androidx.compose.material3.MaterialTheme // Importa dependência / biblioteca necessária
import androidx.compose.material3.OutlinedTextField // Importa dependência / biblioteca necessária
import androidx.compose.material3.OutlinedTextFieldDefaults // Importa dependência / biblioteca necessária
import androidx.compose.material3.Scaffold // Importa dependência / biblioteca necessária
import androidx.compose.material3.Surface // Importa dependência / biblioteca necessária
import androidx.compose.material3.Text // Importa dependência / biblioteca necessária
import androidx.compose.material3.TextButton // Importa dependência / biblioteca necessária
import androidx.compose.runtime.Composable // Importa dependência / biblioteca necessária
import androidx.compose.runtime.getValue // Importa dependência / biblioteca necessária
import androidx.compose.runtime.mutableStateOf // Importa dependência / biblioteca necessária
import androidx.compose.runtime.remember // Importa dependência / biblioteca necessária
import androidx.compose.runtime.rememberCoroutineScope // Importa dependência / biblioteca necessária
import androidx.compose.runtime.setValue // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.platform.LocalContext // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Equipa // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.TeamCode // Importa dependência / biblioteca necessária
import com.leaguematch.data.remote.model.Torneio // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.LocalLanguage // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.LocalTranslationRepository // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.TranslatedText // Importa dependência / biblioteca necessária
import com.leaguematch.ui.components.showTranslatedToast // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.Geist // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMBorder // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray100 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray300 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray400 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray50 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray500 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMGray600 // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMInk // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMRed // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.LMWhite // Importa dependência / biblioteca necessária
import kotlinx.coroutines.launch // Importa dependência / biblioteca necessária

@Composable
fun OrgGerirEquipasScreen( // Declaração de função / método de lógica
    torneio: Torneio,
    equipas: List<Equipa>,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onCriarEquipaClick: () -> Unit,
    onRemoverEquipa: (Equipa) -> Unit,
    onEditarEquipa: (Equipa, String) -> Unit,
    onGerirJogadores: (Equipa) -> Unit = {}
) {
    var equipaParaRemover by remember { mutableStateOf<Equipa?>(null) } // Declara estado mutável local do Compose
    var equipaParaEditar by remember { mutableStateOf<Equipa?>(null) } // Declara estado mutável local do Compose

    if (equipaParaRemover != null) { // Estrutura de decisão condicional principal
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
                    onClick = { // Callback: Define a ação executada ao clicar no componente
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
                TextButton(onClick = { equipaParaRemover = null }) { // Callback: Define a ação executada ao clicar no componente
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

    if (equipaParaEditar != null) { // Estrutura de decisão condicional principal
        var novoNome by remember(equipaParaEditar) { // Memoriza estado para evitar perda durante a recomposição
            mutableStateOf(equipaParaEditar!!.nome) // Declara estado mutável local do Compose
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
                Column { // Contentor Compose: Alinha os filhos numa coluna vertical
                    TranslatedText(
                        text = "Altere o nome da equipa no torneio:",
                        fontFamily = Geist,
                        fontSize = 12.sp,
                        color = LMGray500,
                        modifier = Modifier.padding(bottom = 8.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )

                    OutlinedTextField( // Campo Compose: Entrada de texto com contorno visual
                        value = novoNome,
                        onValueChange = { novoNome = it },
                        placeholder = {
                            TranslatedText(
                                text = "Nome da equipa",
                                fontFamily = Geist,
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
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
                    onClick = { // Callback: Define a ação executada ao clicar no componente
                        if (novoNome.isNotBlank()) { // Estrutura de decisão condicional principal
                            onEditarEquipa(equipaParaEditar!!, novoNome)
                            equipaParaEditar = null
                        }
                    },
                    enabled = novoNome.isNotBlank()
                ) {
                    TranslatedText(
                        text = "Guardar",
                        color = if (novoNome.isNotBlank()) LMInk else LMGray400, // Estrutura de decisão condicional principal
                        fontFamily = Geist,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { equipaParaEditar = null }) { // Callback: Define a ação executada ao clicar no componente
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
        Column( // Contentor Compose: Alinha os filhos numa coluna vertical
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
                .padding(bottom = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Row(verticalAlignment = Alignment.CenterVertically) { // Contentor Compose: Alinha os filhos numa linha horizontal
                IconButton(onClick = onBackClick) { // Componente Compose: Desenha um botão com ícone
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = null,
                        tint = LMInk
                    )
                }

                Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                    TranslatedText(
                        text = "Gerir Equipas",
                        fontFamily = Geist,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = LMInk
                    )

                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = torneio.nome,
                        fontFamily = Geist,
                        fontSize = 12.sp,
                        color = LMGray500
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            TranslatedText(
                text = "EQUIPAS NO TORNEIO · ${equipas.size}",
                fontFamily = Geist,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = LMGray500,
                letterSpacing = 0.4.sp
            )

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            if (isLoading) { // Estrutura de decisão condicional principal
                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = LMRed)
                }
            } else if (equipas.isEmpty()) { // Estrutura de decisão condicional principal
                Surface(
                    modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
                    shape = RoundedCornerShape(16.dp),
                    color = LMGray50,
                    border = BorderStroke(1.dp, LMBorder)
                ) {
                    Column( // Contentor Compose: Alinha os filhos numa coluna vertical
                        modifier = Modifier.padding(24.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon( // Componente Compose: Desenha um ícone vetorial
                            imageVector = Icons.Default.Groups,
                            contentDescription = null,
                            tint = LMGray300,
                            modifier = Modifier.size(40.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                        )

                        Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

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
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
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

            Spacer(modifier = Modifier.height(20.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Button( // Componente Compose: Desenha um botão interativo
                onClick = onCriarEquipaClick, // Callback: Define a ação executada ao clicar no componente
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LMInk)
            ) {
                Icon( // Componente Compose: Desenha um ícone vetorial
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = LMWhite,
                    modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                Spacer(modifier = Modifier.width(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

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
private fun EquipaListItem( // Declaração de função / método de lógica
    equipa: Equipa,
    onEditar: () -> Unit,
    onRemover: () -> Unit,
    onGerirJogadores: () -> Unit
) {
    val context = LocalContext.current // Declara constante local (leitura única)
    val language = LocalLanguage.current // Declara constante local (leitura única)
    val translationRepository = LocalTranslationRepository.current // Declara constante local (leitura única)
    val scope = rememberCoroutineScope() // Cria escopo local para lançar coroutines em cliques na UI

    val codigo = TeamCode.encode(equipa.id) // Declara constante local (leitura única)

    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        shape = RoundedCornerShape(14.dp),
        color = LMWhite,
        border = BorderStroke(1.dp, LMBorder),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
            Row(verticalAlignment = Alignment.CenterVertically) { // Contentor Compose: Alinha os filhos numa linha horizontal
                Box( // Contentor Compose: Sobrepõe os elementos filhos
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .size(40.dp)
                        .background(LMGray100, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = Icons.Default.Groups,
                        contentDescription = null,
                        tint = LMGray500,
                        modifier = Modifier.size(20.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                }

                Spacer(modifier = Modifier.width(12.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = equipa.nome,
                    fontFamily = Geist,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = LMInk,
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )

                IconButton( // Componente Compose: Desenha um botão com ícone
                    onClick = onGerirJogadores, // Callback: Define a ação executada ao clicar no componente
                    modifier = Modifier.size(32.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                ) {
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = Icons.Default.Person,
                        contentDescription = "Jogadores",
                        tint = LMGray400,
                        modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                }

                Spacer(modifier = Modifier.width(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                IconButton( // Componente Compose: Desenha um botão com ícone
                    onClick = onEditar, // Callback: Define a ação executada ao clicar no componente
                    modifier = Modifier.size(32.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                ) {
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = LMGray400,
                        modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                }

                Spacer(modifier = Modifier.width(4.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                IconButton( // Componente Compose: Desenha um botão com ícone
                    onClick = onRemover, // Callback: Define a ação executada ao clicar no componente
                    modifier = Modifier.size(32.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                ) {
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Remover",
                        tint = LMGray400,
                        modifier = Modifier.size(18.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = LMGray50,
                border = BorderStroke(1.dp, LMBorder),
                modifier = Modifier.fillMaxWidth() // Modificador Compose: Define tamanho, margem, padding ou clique
            ) {
                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), // Modificador Compose: Define tamanho, margem, padding ou clique
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                        TranslatedText(
                            text = "CÓDIGO DE INTEGRAÇÃO",
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = LMGray500,
                            letterSpacing = 0.6.sp
                        )

                        Spacer(modifier = Modifier.height(2.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

                        Text( // Componente Compose: Desenha texto estruturado no ecrã
                            text = codigo,
                            fontFamily = Geist,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = LMInk,
                            letterSpacing = 2.sp
                        )
                    }

                    TextButton(
                        onClick = { // Callback: Define a ação executada ao clicar no componente
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
                        Icon( // Componente Compose: Desenha um ícone vetorial
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copiar código",
                            tint = LMRed,
                            modifier = Modifier.size(16.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                        )

                        Spacer(modifier = Modifier.width(6.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes

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

private suspend fun copiarParaClipboard( // Declaração de função / método de lógica
    context: Context,
    texto: String,
    language: com.leaguematch.translations.Language,
    translationRepository: com.leaguematch.data.repository.TranslationRepository? // Efetua chamada remota ou local ao repositório de dados
) {
    val clipboard = // Declara constante local (leitura única)
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