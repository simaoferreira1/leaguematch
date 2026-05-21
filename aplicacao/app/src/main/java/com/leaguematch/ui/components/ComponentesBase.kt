package com.leaguematch.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leaguematch.R
import com.leaguematch.ui.theme.*

// ─── Logo ─────────────────────────────────────────────────────────
@Composable
fun LMLogo(
    modifier: Modifier = Modifier,
    size: Float = 28f,
    variant: String = "full", // "full", "mark", "word"
    color: Color = LMRed,
    ink: Color = MaterialTheme.colorScheme.onBackground
) {
    when (variant) {
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
            Text(
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
        else -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy((size * 0.32f).dp),
                modifier = modifier
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_leaguematch_mark),
                    contentDescription = "LeagueMatch Logo Mark",
                    modifier = Modifier
                        .size((size * 1.05f).dp)
                        .clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.Fit
                )
                Text(
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
fun PrimaryBtn(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    full: Boolean = true,
    dark: Boolean = false,
    size: String = "md", // "sm", "md", "lg"
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val padY = when (size) {
        "sm" -> 9.dp
        "lg" -> 16.dp
        else -> 13.dp
    }
    val fontSize = when (size) {
        "sm" -> 13.sp
        "lg" -> 16.sp
        else -> 15.sp
    }
    val containerColor = if (dark) LMInk else LMRed
    val contentColor = LMWhite

    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = LMGray300,
            disabledContentColor = LMGray500
        ),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = padY),
        modifier = if (full) modifier.fillMaxWidth() else modifier
    ) {
        ProvideTextStyle(TextStyle(fontFamily = Geist, fontWeight = FontWeight.Bold, fontSize = fontSize)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                content()
            }
        }
    }
}

@Composable
fun GhostBtn(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    full: Boolean = true,
    size: String = "md",
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val padY = when (size) {
        "sm" -> 9.dp
        "lg" -> 15.dp
        else -> 12.dp
    }
    val fontSize = when (size) {
        "sm" -> 13.sp
        "lg" -> 16.sp
        else -> 14.sp
    }

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = LMWhite,
            contentColor = LMInk
        ),
        border = BorderStroke(1.dp, LMBorderStrong),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = padY),
        modifier = if (full) modifier.fillMaxWidth() else modifier
    ) {
        ProvideTextStyle(TextStyle(fontFamily = Geist, fontWeight = FontWeight.SemiBold, fontSize = fontSize)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                content()
            }
        }
    }
}

@Composable
fun TextBtn(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = LMRed,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = color
        ),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
        modifier = modifier
    ) {
        ProvideTextStyle(TextStyle(fontFamily = Geist, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                content()
            }
        }
    }
}

// ─── Inputs ───────────────────────────────────────────────────────
@Composable
fun LeagueMatchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "",
    icon: ImageVector? = null,
    hint: String? = null,
    isPassword: Boolean = false,
    singleLine: Boolean = true,
    error: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(bottom = 14.dp)) {
        if (label != null) {
            Text(
                text = label.uppercase(),
                fontFamily = Geist,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = LMGray600,
                letterSpacing = 0.2.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = LMGray400,
                    fontFamily = Geist,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp
                )
            },
            leadingIcon = if (icon != null) {
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = LMGray400,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else null,
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ocultar palavra-passe" else "Mostrar palavra-passe",
                            tint = LMGray400
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
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
            modifier = Modifier.fillMaxWidth()
        )

        if (error != null) {
            Text(
                text = error,
                fontFamily = Geist,
                fontSize = 11.sp,
                color = LMRed700,
                modifier = Modifier.padding(top = 4.dp)
            )
        } else if (hint != null) {
            Text(
                text = hint,
                fontFamily = Geist,
                fontSize = 11.sp,
                color = LMGray500,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

// ─── Badge / Pill ─────────────────────────────────────────────────
@Composable
fun Pill(
    text: String,
    modifier: Modifier = Modifier,
    kind: String = "default" // "default", "live", "soon", "done", "red", "warn"
) {
    val (bg, color) = when (kind) {
        "live" -> LMLiveBg to Color(0xFF15803D)
        "soon" -> Color(0xFFEEF2FF) to Color(0xFF3730A3)
        "done" -> LMGray100 to LMGray600
        "red" -> LMRed50 to LMRed700
        "warn" -> LMAmberBg to Color(0xFF92400E)
        else -> LMGray100 to LMGray700
    }

    Row(
        modifier = modifier
            .background(bg, shape = CircleShape)
            .padding(horizontal = 9.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (kind == "live") {
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.4f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(700, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "alpha"
            )
            Box(
                modifier = Modifier
                    .padding(end = 5.dp)
                    .size(6.dp)
                    .alpha(alpha)
                    .background(LMLive, CircleShape)
            )
        }
        Text(
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
fun TopBar(
    title: String,
    modifier: Modifier = Modifier,
    back: Boolean = false,
    onBackClick: () -> Unit = {},
    big: Boolean = false,
    sub: String? = null,
    rightContent: @Composable (RowScope.() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = if (big) 18.dp else 16.dp, vertical = if (big) 10.dp else 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (back) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .offset(x = (-6).dp)
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Retroceder",
                        tint = LMInk,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (!big) {
                Text(
                    text = title,
                    fontFamily = Geist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = LMInk,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            if (rightContent != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    content = rightContent
                )
            }
        }

        if (big) {
            Column(modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)) {
                Text(
                    text = title,
                    fontFamily = Bricolage,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LMInk,
                    letterSpacing = (-0.8).sp,
                    lineHeight = 34.sp
                )
                if (sub != null) {
                    Text(
                        text = sub,
                        fontFamily = Geist,
                        fontSize = 13.sp,
                        color = LMGray500,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

// ─── Section Card Wrapper ─────────────────────────────────────────
@Composable
fun CardWrapper(
    modifier: Modifier = Modifier,
    pad: Dp = 14.dp,
    borderStroke: BorderStroke? = BorderStroke(1.dp, LMBorder),
    backgroundColor: Color = LMSurface,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(backgroundColor, shape = RoundedCornerShape(16.dp))
            .let { if (borderStroke != null) it.border(borderStroke, RoundedCornerShape(16.dp)) else it }
            .padding(pad)
    ) {
        content()
    }
}

// ─── Avatares / Crests ────────────────────────────────────────────
@Composable
fun TeamCrest(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    c1: Color = LMRed,
    c2: Color = LMInk
) {
    val initials = name.split("\\s+".toRegex())
        .take(2)
        .mapNotNull { it.firstOrNull()?.toString() }
        .joinToString("")
        .uppercase()

    Box(
        modifier = modifier
            .size(size)
            .background(
                Brush.linearGradient(listOf(c1, c2)),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
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
fun Avatar(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    color: Color = LMRed
) {
    val initials = name.split("\\s+".toRegex())
        .take(2)
        .mapNotNull { it.firstOrNull()?.toString() }
        .joinToString("")
        .uppercase()

    Box(
        modifier = modifier
            .size(size)
            .background(color, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
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
fun AdminBottomBar(
    selectedItem: String,
    onHomeClick: () -> Unit,
    onUtilizadoresClick: () -> Unit,
    onTorneiosClick: () -> Unit,
    onGraficosClick: () -> Unit,
    onDefinicoesClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
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
                onClick = onHomeClick,
                icon = { Icon(Icons.Rounded.Dashboard, contentDescription = "Dashboard") },
                label = { 
                    Text(
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
                onClick = onUtilizadoresClick,
                icon = { Icon(Icons.Rounded.People, contentDescription = "Utilizadores") },
                label = { 
                    Text(
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
                onClick = onTorneiosClick,
                icon = { Icon(Icons.Rounded.EmojiEvents, contentDescription = "Torneios") },
                label = { 
                    Text(
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
                onClick = onGraficosClick,
                icon = { Icon(Icons.Rounded.Assessment, contentDescription = "Gráficos") },
                label = { 
                    Text(
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
                onClick = onDefinicoesClick,
                icon = { Icon(Icons.Rounded.Settings, contentDescription = "Definições") },
                label = { 
                    Text(
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
fun OrganizerBottomBar(
    selectedItem: String,
    onTorneiosClick: () -> Unit,
    onEquipasClick: () -> Unit,
    onJogosClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
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
                onClick = onTorneiosClick,
                icon = { Icon(Icons.Rounded.EmojiEvents, contentDescription = "Torneios") },
                label = { 
                    Text(
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
                selected = selectedItem == "equipas",
                onClick = onEquipasClick,
                icon = { Icon(Icons.Rounded.Shield, contentDescription = "Equipas") },
                label = { 
                    Text(
                        text = "Equipas", 
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
                selected = selectedItem == "jogos",
                onClick = onJogosClick,
                icon = { Icon(Icons.Rounded.SportsSoccer, contentDescription = "Jogos") },
                label = { 
                    Text(
                        text = "Jogos", 
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
                selected = selectedItem == "perfil",
                onClick = onPerfilClick,
                icon = { Icon(Icons.Rounded.Person, contentDescription = "Perfil") },
                label = { 
                    Text(
                        text = "Perfil", 
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

