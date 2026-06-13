package com.leaguematch.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Converte o `iniciado_em` da BD para epoch millis.
 *
 * O servidor escreve `Instant.now().toString()` (ISO-8601 UTC com `Z`), mas
 * o PostgREST pode devolver a string sem o sufixo `Z` consoante o tipo da
 * coluna. Esta função aceita ambos os formatos e, no fallback, assume UTC —
 * caso contrário o cliente em Portugal (UTC+1) arrancaria com 60 min de
 * atraso visível.
 */
fun parseIniciadoEmEpochMillis(raw: String?): Long? {
    if (raw.isNullOrBlank()) return null
    val cleaned = raw.trim().replace(' ', 'T')

    runCatching {
        return Instant.parse(cleaned).toEpochMilli()
    }

    return runCatching {
        LocalDateTime
            .parse(cleaned.removeSuffix("Z"))
            .toInstant(ZoneOffset.UTC)
            .toEpochMilli()
    }.getOrNull()
}
