/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: EventoJogo.kt
 * Tipo: Modelo de Dados (Model)
 *
 * Descrição:
 * Este ficheiro define as estruturas de dados e entidades do domínio da aplicação.\n * É usado para mapear os dados lidos das tabelas do Supabase ou Room local.
 */
package com.leaguematch.data.remote.model // Define o pacote deste ficheiro de código

data class EventoJogo( // Declaração de classe para modelar objetos
    val id: Int, // Declara constante local (leitura única)
    val matchId: Int, // Declara constante local (leitura única)
    val tipo: String, // GOLO, FALTA, CARTAO, etc.
    val userId: Int, // Declara constante local (leitura única)
    val userName: String, // Declara constante local (leitura única)
    val tempo: Int, // Minuto
    val equipa: String // "casa", "fora" ou "center"
)
