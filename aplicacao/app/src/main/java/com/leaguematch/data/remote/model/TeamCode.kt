package com.leaguematch.data.remote.model

object TeamCode {
    const val LENGTH = 6
    private const val BODY = LENGTH - 1

    fun encode(teamId: Int): String {
        val body = teamId.toString(radix = 36).uppercase().padStart(BODY, '0')
        return "T$body"
    }

    fun decode(codigo: String): Int? {
        val limpo = codigo.trim().uppercase().removePrefix("T")
        if (limpo.isEmpty()) return null
        return limpo.toIntOrNull(radix = 36)?.takeIf { it > 0 }
    }
}
