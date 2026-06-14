/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: ComponentesBase.kt
 * Tipo: Componente Visual Reutilizável
 *
 * Descrição:
 * Este ficheiro define um componente personalizado e reutilizável em Jetpack Compose.\n * É partilhado entre vários ecrãs para manter a consistência visual (botões, listas, caixas de diálogo, etc.).
 */
package com.leaguematch.ui.components // Define o pacote deste ficheiro de código

import androidx.compose.animation.core.* // Importa dependência / biblioteca necessária
import androidx.compose.foundation.Image // Importa dependência / biblioteca necessária
import androidx.compose.foundation.background // Importa dependência / biblioteca necessária
import androidx.compose.foundation.BorderStroke // Importa dependência / biblioteca necessária
import androidx.compose.foundation.border // Importa dependência / biblioteca necessária
import androidx.compose.foundation.clickable // Importa dependência / biblioteca necessária
import androidx.compose.foundation.layout.* // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.CircleShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.foundation.text.KeyboardOptions // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.Icons // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.filled.* // Importa dependência / biblioteca necessária
import androidx.compose.material.icons.rounded.* // Importa dependência / biblioteca necessária
import androidx.compose.material3.* // Importa dependência / biblioteca necessária
import androidx.compose.runtime.* // Importa dependência / biblioteca necessária
import androidx.compose.ui.Alignment // Importa dependência / biblioteca necessária
import androidx.compose.ui.Modifier // Importa dependência / biblioteca necessária
import androidx.compose.ui.draw.alpha // Importa dependência / biblioteca necessária
import androidx.compose.ui.draw.clip // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Brush // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.Color // Importa dependência / biblioteca necessária
import androidx.compose.ui.graphics.vector.ImageVector // Importa dependência / biblioteca necessária
import androidx.compose.ui.layout.ContentScale // Importa dependência / biblioteca necessária
import androidx.compose.ui.res.painterResource // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.SpanStyle // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.TextStyle // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.buildAnnotatedString // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontStyle // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.FontWeight // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.input.PasswordVisualTransformation // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.input.VisualTransformation // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.style.TextAlign // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.style.TextOverflow // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.withStyle // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.Dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.R // Importa dependência / biblioteca necessária
import com.leaguematch.ui.theme.* // Importa dependência / biblioteca necessária

// ─── Logo ─────────────────────────────────────────────────────────
@Composable
fun LMLogo( // Declaração de função / método de lógica
    modifier: Modifier = Modifier, // Modificador Compose: Define tamanho, margem, padding ou clique
    size: Float = 28f,
    variant: String = "full", // "full", "mark", "word"
    color: Color = LMRed,
    ink: Color = MaterialTheme.colorScheme.onBackground
) {
    when (variant) { // Escolha múltipla condicional (semelhante a switch-case)
        "mark" -> {
            Image(
                painter = painterResource(id = R.drawable.logo_leaguematch_mark),
                contentDescription = "LeagueMatch Logo Mark",
                modifier = modifier
                    .size(size.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit
            )
        }
        "word" -> {
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = buildAnnotatedString {
                    append("L")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = ink)) {
                        append("EAGUE")
                    }
                    withStyle(SpanStyle(color = color, fontWeight = FontWeight.ExtraBold)) {
                        append("MATCH")
                    }
                },
                fontFamily = Bricolage,
                fontWeight = FontWeight.ExtraBold,
                fontStyle = FontStyle.Italic,
                fontSize = size.sp,
                color = ink,
                modifier = modifier
            )
        }
        else -> { // Fluxo condicional alternativo caso o 'if' seja falso
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy((size * 0.32f).dp),
                modifier = modifier
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_leaguematch_mark),
                    contentDescription = "LeagueMatch Logo Mark",
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .size((size * 1.05f).dp)
                        .clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.Fit
                )
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = buildAnnotatedString {
                        append("L")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = ink)) {
                            append("EAGUE")
                        }
                        withStyle(SpanStyle(color = color, fontWeight = FontWeight.ExtraBold)) {
                            append("MATCH")
                        }
                    },
                    fontFamily = Bricolage,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic,
                    fontSize = (size * 0.7f).sp,
                    color = ink
                )
            }
        }
    }
}

// ─── Buttons ──────────────────────────────────────────────────────
@Composable
fun PrimaryBtn( // Declaração de função / método de lógica
    onClick: () -> Unit, // Callback: Define a ação executada ao clicar no componente
    modifier: Modifier = Modifier, // Modificador Compose: Define tamanho, margem, padding ou clique
    full: Boolean = true,
    dark: Boolean = false,
    size: String = "md", // "sm", "md", "lg"
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val padY = when (size) { // Escolha múltipla condicional (semelhante a switch-case)
        "sm" -> 9.dp
        "lg" -> 16.dp
        else -> 13.dp // Fluxo condicional alternativo caso o 'if' seja falso
    }
    val fontSize = when (size) { // Escolha múltipla condicional (semelhante a switch-case)
        "sm" -> 13.sp
        "lg" -> 16.sp
        else -> 15.sp // Fluxo condicional alternativo caso o 'if' seja falso
    }
    val containerColor = if (dark) LMInk else LMRed // Estrutura de decisão condicional principal
    val contentColor = LMWhite // Declara constante local (leitura única)

    Button( // Componente Compose: Desenha um botão interativo
        onClick = onClick, // Callback: Define a ação executada ao clicar no componente
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = LMGray300,
            disabledContentColor = LMGray500
        ),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = padY),
        modifier = if (full) modifier.fillMaxWidth() else modifier // Estrutura de decisão condicional principal
    ) {
        ProvideTextStyle(TextStyle(fontFamily = Geist, fontWeight = FontWeight.Bold, fontSize = fontSize)) {
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                content()
            }
        }
    }
}

@Composable
fun GhostBtn( // Declaração de função / método de lógica
    onClick: () -> Unit, // Callback: Define a ação executada ao clicar no componente
    modifier: Modifier = Modifier, // Modificador Compose: Define tamanho, margem, padding ou clique
    full: Boolean = true,
    size: String = "md",
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val padY = when (size) { // Escolha múltipla condicional (semelhante a switch-case)
        "sm" -> 9.dp
        "lg" -> 15.dp
        else -> 12.dp // Fluxo condicional alternativo caso o 'if' seja falso
    }
    val fontSize = when (size) { // Escolha múltipla condicional (semelhante a switch-case)
        "sm" -> 13.sp
        "lg" -> 16.sp
        else -> 14.sp // Fluxo condicional alternativo caso o 'if' seja falso
    }

    OutlinedButton(
        onClick = onClick, // Callback: Define a ação executada ao clicar no componente
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = LMWhite,
            contentColor = LMInk
        ),
        border = BorderStroke(1.dp, LMBorderStrong),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = padY),
        modifier = if (full) modifier.fillMaxWidth() else modifier // Estrutura de decisão condicional principal
    ) {
        ProvideTextStyle(TextStyle(fontFamily = Geist, fontWeight = FontWeight.SemiBold, fontSize = fontSize)) {
            Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                content()
            }
        }
    }
}

@Composable
fun TextBtn( // Declaração de função / método de lógica
    onClick: () -> Unit, // Callback: Define a ação executada ao clicar no componente
    modifier: Modifier = Modifier, // Modificador Compose: Define tamanho, margem, padding ou clique
    color: Color = LMRed,
    content: @Composable RowScope.() -> Unit
) {
    Button( // Componente Compose: Desenha um botão interativo
        onClick = onClick, // Callback: Define a ação executada ao clicar no componente
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = color
        ),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
        modifier = modifier
    ) {
        ProvideTextStyle(TextStyle(fontFamily = Geist, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)) {
            Row(verticalAlignment = Alignment.CenterVertically) { // Contentor Compose: Alinha os filhos numa linha horizontal
                content()
            }
        }
    }
}

// ─── Inputs ───────────────────────────────────────────────────────
@Composable
fun LeagueMatchTextField( // Declaração de função / método de lógica
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier, // Modificador Compose: Define tamanho, margem, padding ou clique
    label: String? = null,
    placeholder: String = "",
    icon: ImageVector? = null,
    hint: String? = null,
    isPassword: Boolean = false,
    singleLine: Boolean = true,
    error: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) } // Declara estado mutável local do Compose

    Column(modifier = modifier.padding(bottom = 14.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
        if (label != null) { // Estrutura de decisão condicional principal
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = label.uppercase(),
                fontFamily = Geist,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = LMGray600,
                letterSpacing = 0.2.sp,
                modifier = Modifier.padding(bottom = 6.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )
        }

        OutlinedTextField( // Campo Compose: Entrada de texto com contorno visual
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = placeholder,
                    color = LMGray400,
                    fontFamily = Geist,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp
                )
            },
            leadingIcon = if (icon != null) { // Estrutura de decisão condicional principal
                {
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = icon,
                        contentDescription = null,
                        tint = LMGray400,
                        modifier = Modifier.size(20.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                }
            } else null, // Fluxo condicional alternativo caso o 'if' seja falso
            trailingIcon = if (isPassword) { // Estrutura de decisão condicional principal
                {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) { // Componente Compose: Desenha um botão com ícone
                        Icon( // Componente Compose: Desenha um ícone vetorial
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, // Estrutura de decisão condicional principal
                            contentDescription = if (passwordVisible) "Ocultar palavra-passe" else "Mostrar palavra-passe", // Estrutura de decisão condicional principal
                            tint = LMGray400
                        )
                    }
                }
            } else null, // Fluxo condicional alternativo caso o 'if' seja falso
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None, // Estrutura de decisão condicional principal
            singleLine = singleLine,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = LMWhite,
                unfocusedContainerColor = LMWhite,
                focusedBorderColor = LMRed,
                unfocusedBorderColor = LMBorder,
                focusedTextColor = LMInk,
                unfocusedTextColor = LMInk
            ),
            isError = error != null,
            modifier = Modifier.fillMaxWidth() // Modificador Compose: Define tamanho, margem, padding ou clique
        )

        if (error != null) { // Estrutura de decisão condicional principal
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = error,
                fontFamily = Geist,
                fontSize = 11.sp,
                color = LMRed700,
                modifier = Modifier.padding(top = 4.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )
        } else if (hint != null) { // Estrutura de decisão condicional principal
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = hint,
                fontFamily = Geist,
                fontSize = 11.sp,
                color = LMGray500,
                modifier = Modifier.padding(top = 4.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
            )
        }
    }
}

// ─── Badge / Pill ─────────────────────────────────────────────────
@Composable
fun Pill( // Declaração de função / método de lógica
    text: String,
    modifier: Modifier = Modifier, // Modificador Compose: Define tamanho, margem, padding ou clique
    kind: String = "default" // "default", "live", "soon", "done", "red", "warn"
) {
    val (bg, color) = when (kind) { // Escolha múltipla condicional (semelhante a switch-case)
        "live" -> LMLiveBg to Color(0xFF15803D)
        "soon" -> Color(0xFFEEF2FF) to Color(0xFF3730A3)
        "done" -> LMGray100 to LMGray600
        "red" -> LMRed50 to LMRed700
        "warn" -> LMAmberBg to Color(0xFF92400E)
        else -> LMGray100 to LMGray700 // Fluxo condicional alternativo caso o 'if' seja falso
    }

    Row( // Contentor Compose: Alinha os filhos numa linha horizontal
        modifier = modifier
            .background(bg, shape = CircleShape)
            .padding(horizontal = 9.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (kind == "live") { // Estrutura de decisão condicional principal
            val infiniteTransition = rememberInfiniteTransition(label = "pulse") // Declara constante local (leitura única)
            val alpha by infiniteTransition.animateFloat( // Declara constante local (leitura única)
                initialValue = 0.4f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(700, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "alpha"
            )
            Box( // Contentor Compose: Sobrepõe os elementos filhos
                modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                    .padding(end = 5.dp)
                    .size(6.dp)
                    .alpha(alpha)
                    .background(LMLive, CircleShape)
            )
        }
        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = text.uppercase(),
            fontFamily = Geist,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = color,
            letterSpacing = 0.2.sp
        )
    }
}

// ─── TopBar ────────────────────────────────────────────
@Composable
fun TopBar( // Declaração de função / método de lógica
    title: String,
    modifier: Modifier = Modifier, // Modificador Compose: Define tamanho, margem, padding ou clique
    back: Boolean = false,
    onBackClick: () -> Unit = {},
    big: Boolean = false,
    sub: String? = null,
    rightContent: @Composable (RowScope.() -> Unit)? = null
) {
    Column( // Contentor Compose: Alinha os filhos numa coluna vertical
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = if (big) 18.dp else 16.dp, vertical = if (big) 10.dp else 8.dp) // Estrutura de decisão condicional principal
    ) {
        Row( // Contentor Compose: Alinha os filhos numa linha horizontal
            modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                .fillMaxWidth()
                .heightIn(min = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (back) { // Estrutura de decisão condicional principal
                IconButton( // Componente Compose: Desenha um botão com ícone
                    onClick = onBackClick, // Callback: Define a ação executada ao clicar no componente
                    modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
                        .offset(x = (-6).dp)
                        .size(32.dp)
                ) {
                    Icon( // Componente Compose: Desenha um ícone vetorial
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Retroceder",
                        tint = LMInk,
                        modifier = Modifier.size(20.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                }
            }

            if (!big) { // Estrutura de decisão condicional principal
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = title,
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = LMInk,
                    modifier = Modifier.weight(1f) // Modificador Compose: Define tamanho, margem, padding ou clique
                )
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                Spacer(modifier = Modifier.weight(1f)) // Espaçador Compose: Cria distanciamento visual entre componentes
            }

            if (rightContent != null) { // Estrutura de decisão condicional principal
                Row( // Contentor Compose: Alinha os filhos numa linha horizontal
                    verticalAlignment = Alignment.CenterVertically,
                    content = rightContent
                )
            }
        }

        if (big) { // Estrutura de decisão condicional principal
            Column(modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)) { // Contentor Compose: Alinha os filhos numa coluna vertical
                Text( // Componente Compose: Desenha texto estruturado no ecrã
                    text = title,
                    fontFamily = Bricolage,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LMInk,
                    letterSpacing = (-0.8).sp,
                    lineHeight = 34.sp
                )
                if (sub != null) { // Estrutura de decisão condicional principal
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = sub,
                        fontFamily = Geist,
                        fontSize = 13.sp,
                        color = LMGray500,
                        modifier = Modifier.padding(top = 4.dp) // Modificador Compose: Define tamanho, margem, padding ou clique
                    )
                }
            }
        }
    }
}

// ─── Section Card Wrapper ─────────────────────────────────────────
@Composable
fun CardWrapper( // Declaração de função / método de lógica
    modifier: Modifier = Modifier, // Modificador Compose: Define tamanho, margem, padding ou clique
    pad: Dp = 14.dp,
    borderStroke: BorderStroke? = BorderStroke(1.dp, LMBorder),
    backgroundColor: Color = LMSurface,
    content: @Composable () -> Unit
) {
    Box( // Contentor Compose: Sobrepõe os elementos filhos
        modifier = modifier
            .background(backgroundColor, shape = RoundedCornerShape(16.dp))
            .let { if (borderStroke != null) it.border(borderStroke, RoundedCornerShape(16.dp)) else it } // Estrutura de decisão condicional principal
            .padding(pad)
    ) {
        content()
    }
}

// ─── Avatares / Crests ────────────────────────────────────────────
@Composable
fun TeamCrest( // Declaração de função / método de lógica
    name: String,
    modifier: Modifier = Modifier, // Modificador Compose: Define tamanho, margem, padding ou clique
    size: Dp = 36.dp,
    c1: Color = LMRed,
    c2: Color = LMInk
) {
    val initials = name.split("\\s+".toRegex()) // Declara constante local (leitura única)
        .take(2)
        .mapNotNull { it.firstOrNull()?.toString() }
        .joinToString("")
        .uppercase()

    Box( // Contentor Compose: Sobrepõe os elementos filhos
        modifier = modifier
            .size(size)
            .background(
                Brush.linearGradient(listOf(c1, c2)),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = initials,
            color = LMWhite,
            fontWeight = FontWeight.Bold,
            fontFamily = Bricolage,
            fontSize = (size.value * 0.36f).sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Avatar( // Declaração de função / método de lógica
    name: String,
    modifier: Modifier = Modifier, // Modificador Compose: Define tamanho, margem, padding ou clique
    size: Dp = 32.dp,
    color: Color = LMRed
) {
    val initials = name.split("\\s+".toRegex()) // Declara constante local (leitura única)
        .take(2)
        .mapNotNull { it.firstOrNull()?.toString() }
        .joinToString("")
        .uppercase()

    Box( // Contentor Compose: Sobrepõe os elementos filhos
        modifier = modifier
            .size(size)
            .background(color, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text( // Componente Compose: Desenha texto estruturado no ecrã
            text = initials,
            color = LMWhite,
            fontWeight = FontWeight.Bold,
            fontFamily = Geist,
            fontSize = (size.value * 0.4f).sp,
            textAlign = TextAlign.Center
        )
    }
}

// ─── Admin Bottom Bar ────────────────────────────────────────────
@Composable
fun AdminBottomBar( // Declaração de função / método de lógica
    selectedItem: String,
    onHomeClick: () -> Unit,
    onUtilizadoresClick: () -> Unit,
    onTorneiosClick: () -> Unit,
    onGraficosClick: () -> Unit,
    onDefinicoesClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        color = LMSurface,
        border = BorderStroke(1.dp, LMBorder)
    ) {
        NavigationBar(
            containerColor = LMSurface,
            contentColor = LMInk,
            tonalElevation = 0.dp
        ) {
            NavigationBarItem(
                selected = selectedItem == "home",
                onClick = onHomeClick, // Callback: Define a ação executada ao clicar no componente
                icon = { Icon(Icons.Rounded.Dashboard, contentDescription = "Dashboard") }, // Componente Compose: Desenha um ícone vetorial
                label = { 
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "Painel", 
                        fontSize = 9.5.sp, 
                        fontFamily = Geist, 
                        maxLines = 1, 
                        softWrap = false,
                        overflow = TextOverflow.Clip
                    ) 
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = LMRed,
                    selectedTextColor = LMRed,
                    unselectedIconColor = LMGray500,
                    unselectedTextColor = LMGray500,
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = selectedItem == "utilizadores",
                onClick = onUtilizadoresClick, // Callback: Define a ação executada ao clicar no componente
                icon = { Icon(Icons.Rounded.People, contentDescription = "Utilizadores") }, // Componente Compose: Desenha um ícone vetorial
                label = { 
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "Utilizadores", 
                        fontSize = 9.5.sp, 
                        fontFamily = Geist, 
                        maxLines = 1, 
                        softWrap = false,
                        overflow = TextOverflow.Clip
                    ) 
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = LMRed,
                    selectedTextColor = LMRed,
                    unselectedIconColor = LMGray500,
                    unselectedTextColor = LMGray500,
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = selectedItem == "torneios",
                onClick = onTorneiosClick, // Callback: Define a ação executada ao clicar no componente
                icon = { Icon(Icons.Rounded.EmojiEvents, contentDescription = "Torneios") }, // Componente Compose: Desenha um ícone vetorial
                label = { 
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "Torneios", 
                        fontSize = 9.5.sp, 
                        fontFamily = Geist, 
                        maxLines = 1, 
                        softWrap = false,
                        overflow = TextOverflow.Clip
                    ) 
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = LMRed,
                    selectedTextColor = LMRed,
                    unselectedIconColor = LMGray500,
                    unselectedTextColor = LMGray500,
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = selectedItem == "graficos",
                onClick = onGraficosClick, // Callback: Define a ação executada ao clicar no componente
                icon = { Icon(Icons.Rounded.Assessment, contentDescription = "Gráficos") }, // Componente Compose: Desenha um ícone vetorial
                label = { 
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "Gráficos", 
                        fontSize = 9.5.sp, 
                        fontFamily = Geist, 
                        maxLines = 1, 
                        softWrap = false,
                        overflow = TextOverflow.Clip
                    ) 
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = LMRed,
                    selectedTextColor = LMRed,
                    unselectedIconColor = LMGray500,
                    unselectedTextColor = LMGray500,
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = selectedItem == "definicoes",
                onClick = onDefinicoesClick, // Callback: Define a ação executada ao clicar no componente
                icon = { Icon(Icons.Rounded.Settings, contentDescription = "Definições") }, // Componente Compose: Desenha um ícone vetorial
                label = { 
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "Definições", 
                        fontSize = 9.5.sp, 
                        fontFamily = Geist, 
                        maxLines = 1, 
                        softWrap = false,
                        overflow = TextOverflow.Clip
                    ) 
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = LMRed,
                    selectedTextColor = LMRed,
                    unselectedIconColor = LMGray500,
                    unselectedTextColor = LMGray500,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

// ─── Organizer Bottom Bar ────────────────────────────────────────
@Composable
fun OrganizerBottomBar( // Declaração de função / método de lógica
    selectedItem: String,
    onTorneiosClick: () -> Unit,
    onPerfilClick: () -> Unit,
    accentColor: Color = LMRed
) {
    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        color = LMSurface,
        border = BorderStroke(1.dp, LMBorder)
    ) {
        NavigationBar(
            containerColor = LMSurface,
            contentColor = LMInk,
            tonalElevation = 0.dp
        ) {
            NavigationBarItem(
                selected = selectedItem == "torneios",
                onClick = onTorneiosClick, // Callback: Define a ação executada ao clicar no componente
                icon = { Icon(Icons.Rounded.EmojiEvents, contentDescription = "Torneios") }, // Componente Compose: Desenha um ícone vetorial
                label = { 
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "Torneios", 
                        fontSize = 9.5.sp, 
                        fontFamily = Geist, 
                        maxLines = 1, 
                        softWrap = false,
                        overflow = TextOverflow.Clip
                    ) 
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = accentColor,
                    selectedTextColor = accentColor,
                    unselectedIconColor = LMGray500,
                    unselectedTextColor = LMGray500,
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = selectedItem == "perfil",
                onClick = onPerfilClick, // Callback: Define a ação executada ao clicar no componente
                icon = { Icon(Icons.Rounded.Person, contentDescription = "Perfil") }, // Componente Compose: Desenha um ícone vetorial
                label = {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "Perfil",
                        fontSize = 9.5.sp,
                        fontFamily = Geist,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Clip
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = accentColor,
                    selectedTextColor = accentColor,
                    unselectedIconColor = LMGray500,
                    unselectedTextColor = LMGray500,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

// ─── Spectator Bottom Bar ────────────────────────────────────────
@Composable
fun SpectatorBottomBar( // Declaração de função / método de lógica
    selectedItem: String,
    onTorneiosClick: () -> Unit,
    onPerfilClick: () -> Unit,
    accentColor: Color = LMRed
) {
    Surface(
        modifier = Modifier.fillMaxWidth(), // Modificador Compose: Define tamanho, margem, padding ou clique
        color = LMSurface,
        border = BorderStroke(1.dp, LMBorder)
    ) {
        NavigationBar(
            containerColor = LMSurface,
            contentColor = LMInk,
            tonalElevation = 0.dp
        ) {
            NavigationBarItem(
                selected = selectedItem == "torneios",
                onClick = onTorneiosClick, // Callback: Define a ação executada ao clicar no componente
                icon = { Icon(Icons.Rounded.EmojiEvents, contentDescription = "Torneios") }, // Componente Compose: Desenha um ícone vetorial
                label = { 
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "Torneios", 
                        fontSize = 9.5.sp, 
                        fontFamily = Geist, 
                        maxLines = 1, 
                        softWrap = false,
                        overflow = TextOverflow.Clip
                    ) 
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = accentColor,
                    selectedTextColor = accentColor,
                    unselectedIconColor = LMGray500,
                    unselectedTextColor = LMGray500,
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = selectedItem == "perfil",
                onClick = onPerfilClick, // Callback: Define a ação executada ao clicar no componente
                icon = { Icon(Icons.Rounded.Person, contentDescription = "Perfil") }, // Componente Compose: Desenha um ícone vetorial
                label = {
                    Text( // Componente Compose: Desenha texto estruturado no ecrã
                        text = "Perfil",
                        fontSize = 9.5.sp,
                        fontFamily = Geist,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Clip
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = accentColor,
                    selectedTextColor = accentColor,
                    unselectedIconColor = LMGray500,
                    unselectedTextColor = LMGray500,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

// ─── Loading and Error Screens ─────────────────────────────────────
@Composable
fun <T> RemoteContent( // Declaração de função / método de lógica
    result: Result<T>?,
    content: @Composable (T) -> Unit
) {
    when { // Escolha múltipla condicional (semelhante a switch-case)
        result == null -> LoadingScreen()
        result.isSuccess -> content(result.getOrThrow())
        else -> ErrorScreen(result.exceptionOrNull()?.message ?: "Erro ao carregar dados.") // Fluxo condicional alternativo caso o 'if' seja falso
    }
}

@Composable
fun LoadingScreen() { // Declaração de função / método de lógica
    Box( // Contentor Compose: Sobrepõe os elementos filhos
        modifier = Modifier.fillMaxSize(), // Modificador Compose: Define tamanho, margem, padding ou clique
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = LMRed)
    }
}

@Composable
fun ErrorScreen(message: String) { // Declaração de função / método de lógica
    Box( // Contentor Compose: Sobrepõe os elementos filhos
        modifier = Modifier // Modificador Compose: Define tamanho, margem, padding ou clique
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) { // Contentor Compose: Alinha os filhos numa coluna vertical
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = "Erro ao ligar ao Supabase",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                fontFamily = Geist
            )
            Spacer(modifier = Modifier.height(8.dp)) // Espaçador Compose: Cria distanciamento visual entre componentes
            Text( // Componente Compose: Desenha texto estruturado no ecrã
                text = message,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = Geist,
                textAlign = TextAlign.Center
            )
        }
    }
}


