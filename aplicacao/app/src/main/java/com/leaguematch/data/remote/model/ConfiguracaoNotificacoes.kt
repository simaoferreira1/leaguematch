/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: ConfiguracaoNotificacoes.kt
 * Tipo: Modelo de Dados (Model)
 *
 * Descrição:
 * Este ficheiro define as estruturas de dados e entidades do domínio da aplicação.\n * É usado para mapear os dados lidos das tabelas do Supabase ou Room local.
 */
package com.leaguematch.data.remote.model // Define o pacote deste ficheiro de código

data class ConfiguracaoNotificacoes( // Declaração de classe para modelar objetos
    val utilizadorId: Int, // Declara constante local (leitura única)

    val notificacoesJogos: Boolean = true, // Declara constante local (leitura única)
    val notificacoesGolos: Boolean = true, // Declara constante local (leitura única)
    val notificacoesCartoes: Boolean = false, // Declara constante local (leitura única)
    val notificacoesFimPartida: Boolean = true, // Declara constante local (leitura única)

    val somNotificacao: Boolean = true, // Declara constante local (leitura única)

    val futebol: Boolean = true, // Declara constante local (leitura única)
    val tenis: Boolean = false, // Declara constante local (leitura única)
    val basquetebol: Boolean = true, // Declara constante local (leitura única)
    val andebol: Boolean = false // Declara constante local (leitura única)
)