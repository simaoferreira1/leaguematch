/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: Time.kt
 * Tipo: Lógica Utilitária / Auxiliar
 *
 * Descrição:
 * Contém funções utilitárias ou auxiliares transversais à aplicação.
 */
package com.leaguematch.util // Define o pacote deste ficheiro de código

import java.time.Instant // Importa dependência / biblioteca necessária
import java.time.LocalDateTime // Importa dependência / biblioteca necessária
import java.time.ZoneOffset // Importa dependência / biblioteca necessária

/**
 * Converte o `iniciado_em` da BD para epoch millis.
 *
 * O servidor escreve `Instant.now().toString()` (ISO-8601 UTC com `Z`), mas
 * o PostgREST pode devolver a string sem o sufixo `Z` consoante o tipo da
 * coluna. Esta função aceita ambos os formatos e, no fallback, assume UTC —
 * caso contrário o cliente em Portugal (UTC+1) arrancaria com 60 min de
 * atraso visível.
 */
fun parseIniciadoEmEpochMillis(raw: String?): Long? { // Declaração de função / método de lógica
    if (raw.isNullOrBlank()) return null // Estrutura de decisão condicional principal
    val cleaned = raw.trim().replace(' ', 'T') // Declara constante local (leitura única)

    runCatching {
        return Instant.parse(cleaned).toEpochMilli() // Retorna o resultado da execução da função
    }

    return runCatching { // Retorna o resultado da execução da função
        LocalDateTime
            .parse(cleaned.removeSuffix("Z"))
            .toInstant(ZoneOffset.UTC)
            .toEpochMilli()
    }.getOrNull()
}
