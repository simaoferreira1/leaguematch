/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: EstatistcaJogo.kt
 * Tipo: Modelo de Dados (Model)
 *
 * Descrição:
 * Este ficheiro define as estruturas de dados e entidades do domínio da aplicação.\n * É usado para mapear os dados lidos das tabelas do Supabase ou Room local.
 */
package com.leaguematch.data.remote.model // Define o pacote deste ficheiro de código

data class EstatisticaJogo( // Declaração de classe para modelar objetos
    val tipo: String, // Declara constante local (leitura única)
    val equipa: String, // Declara constante local (leitura única)
    val valor: Int // Declara constante local (leitura única)
)