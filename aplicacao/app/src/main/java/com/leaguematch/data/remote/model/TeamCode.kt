/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: TeamCode.kt
 * Tipo: Modelo de Dados (Model)
 *
 * Descrição:
 * Este ficheiro define as estruturas de dados e entidades do domínio da aplicação.\n * É usado para mapear os dados lidos das tabelas do Supabase ou Room local.
 */
package com.leaguematch.data.remote.model // Define o pacote deste ficheiro de código

object TeamCode { // Declaração de objeto estático / Singleton
    const val LENGTH = 6 // Declara constante local (leitura única)
    private const val BODY = LENGTH - 1 // Declara constante local (leitura única)

    fun encode(teamId: Int): String { // Declaração de função / método de lógica
        val body = teamId.toString(radix = 36).uppercase().padStart(BODY, '0') // Declara constante local (leitura única)
        return "T$body" // Retorna o resultado da execução da função
    }

    fun decode(codigo: String): Int? { // Declaração de função / método de lógica
        val limpo = codigo.trim().uppercase().removePrefix("T") // Declara constante local (leitura única)
        if (limpo.isEmpty()) return null // Estrutura de decisão condicional principal
        return limpo.toIntOrNull(radix = 36)?.takeIf { it > 0 } // Retorna o resultado da execução da função
    }
}
