/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: Type.kt
 * Tipo: Lógica Utilitária / Auxiliar
 *
 * Descrição:
 * Contém funções utilitárias ou auxiliares transversais à aplicação.
 */
package com.leaguematch.ui.theme // Define o pacote deste ficheiro de código

import androidx.compose.material3.Typography // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.TextStyle // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.font.* // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.googlefonts.Font // Importa dependência / biblioteca necessária
import androidx.compose.ui.text.googlefonts.GoogleFont // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.sp // Importa dependência / biblioteca necessária
import com.leaguematch.R // Importa dependência / biblioteca necessária

private val provider = GoogleFont.Provider( // Declara constante local (leitura única)
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val Geist = FontFamily( // Declara constante local (leitura única)
    Font(GoogleFont("Geist"), provider, weight = FontWeight.Normal),
    Font(GoogleFont("Geist"), provider, weight = FontWeight.Medium),
    Font(GoogleFont("Geist"), provider, weight = FontWeight.SemiBold),
    Font(GoogleFont("Geist"), provider, weight = FontWeight.Bold),
    Font(GoogleFont("Geist"), provider, weight = FontWeight.ExtraBold),
)
val Bricolage = FontFamily( // Declara constante local (leitura única)
    Font(GoogleFont("Bricolage Grotesque"), provider, weight = FontWeight.SemiBold),
    Font(GoogleFont("Bricolage Grotesque"), provider, weight = FontWeight.Bold),
    Font(GoogleFont("Bricolage Grotesque"), provider, weight = FontWeight.ExtraBold),
)
val GeistMono = FontFamily( // Declara constante local (leitura única)
    Font(GoogleFont("Geist Mono"), provider, weight = FontWeight.Medium),
    Font(GoogleFont("Geist Mono"), provider, weight = FontWeight.Bold),
)

val LMTypography = Typography( // Declara constante local (leitura única)
    displayLarge  = TextStyle(fontFamily = Bricolage, fontWeight = FontWeight.ExtraBold, fontSize = 40.sp, letterSpacing = (-0.8).sp),
    displayMedium = TextStyle(fontFamily = Bricolage, fontWeight = FontWeight.Bold,      fontSize = 32.sp, letterSpacing = (-0.6).sp),
    headlineLarge = TextStyle(fontFamily = Bricolage, fontWeight = FontWeight.Bold,      fontSize = 24.sp, letterSpacing = (-0.4).sp),
    titleLarge    = TextStyle(fontFamily = Geist,     fontWeight = FontWeight.Bold,      fontSize = 18.sp),
    titleMedium   = TextStyle(fontFamily = Geist,     fontWeight = FontWeight.SemiBold,  fontSize = 15.sp),
    bodyLarge     = TextStyle(fontFamily = Geist,     fontWeight = FontWeight.Normal,    fontSize = 15.sp, lineHeight = 22.sp),
    bodyMedium    = TextStyle(fontFamily = Geist,     fontWeight = FontWeight.Normal,    fontSize = 13.sp, lineHeight = 20.sp),
    labelLarge    = TextStyle(fontFamily = Geist,     fontWeight = FontWeight.SemiBold,  fontSize = 13.sp),
    labelSmall    = TextStyle(fontFamily = GeistMono, fontWeight = FontWeight.Bold,      fontSize = 11.sp, letterSpacing = 0.6.sp),
)

// Legacy alias for compatibility with unrefactored screens
val Typography = LMTypography // Declara constante local (leitura única)
