/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: Shape.kt
 * Tipo: Lógica Utilitária / Auxiliar
 *
 * Descrição:
 * Contém funções utilitárias ou auxiliares transversais à aplicação.
 */
package com.leaguematch.ui.theme // Define o pacote deste ficheiro de código

import androidx.compose.foundation.shape.RoundedCornerShape // Importa dependência / biblioteca necessária
import androidx.compose.material3.Shapes // Importa dependência / biblioteca necessária
import androidx.compose.ui.unit.dp // Importa dependência / biblioteca necessária

val LMShapes = Shapes( // Declara constante local (leitura única)
    extraSmall = RoundedCornerShape(8.dp),
    small      = RoundedCornerShape(12.dp),
    medium     = RoundedCornerShape(16.dp),
    large      = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(999.dp),  // pill
)
