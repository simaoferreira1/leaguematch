/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: TranslationRepository.kt
 * Tipo: Camada de Acesso a Dados (Repositório)
 *
 * Descrição:
 * Este ficheiro define a interface ou implementação do repositório de dados.\n * Abstrai a origem dos dados da aplicação, servindo de ponte entre os ViewModels e as APIs externas.
 */
package com.leaguematch.data.repository // Define o pacote deste ficheiro de código

import kotlinx.coroutines.Dispatchers // Importa dependência / biblioteca necessária
import kotlinx.coroutines.withContext // Importa dependência / biblioteca necessária
import org.json.JSONArray // Importa dependência / biblioteca necessária
import org.json.JSONObject // Importa dependência / biblioteca necessária
import java.net.HttpURLConnection // Importa dependência / biblioteca necessária
import java.net.URL // Importa dependência / biblioteca necessária

class TranslationRepository( // Declaração de classe para modelar objetos
    private val supabaseUrl: String, // Declara constante local (leitura única)
    private val anonKey: String // Declara constante local (leitura única)
) {
    private val cache = mutableMapOf<String, String>() // Declara constante local (leitura única)

    suspend fun translateText( // Declaração de função / método de lógica
        text: String,
        targetLang: String
    ): String = withContext(Dispatchers.IO) {
        if (text.isBlank()) return@withContext text // Estrutura de decisão condicional principal
        if (targetLang.uppercase() == "PT") return@withContext text // Estrutura de decisão condicional principal

        val cacheKey = "${targetLang.uppercase()}:$text" // Declara constante local (leitura única)
        cache[cacheKey]?.let { return@withContext it } // Retorna o resultado da execução da função

        val translated = translateTexts( // Declara constante local (leitura única)
            texts = listOf(text),
            targetLang = targetLang
        ).firstOrNull() ?: text

        cache[cacheKey] = translated
        translated
    }

    suspend fun translateTexts( // Declaração de função / método de lógica
        texts: List<String>,
        targetLang: String
    ): List<String> = withContext(Dispatchers.IO) {
        if (texts.isEmpty()) return@withContext emptyList() // Estrutura de decisão condicional principal
        if (targetLang.uppercase() == "PT") return@withContext texts // Estrutura de decisão condicional principal

        val url = URL("$supabaseUrl/functions/v1/translate") // Declara constante local (leitura única)

        val jsonBody = JSONObject().apply { // Manipula objeto em formato JSON para transporte de dados
            put("texts", JSONArray(texts)) // Manipula array em formato JSON para transporte de dados
            put("targetLang", targetLang.uppercase())
        }

        val connection = (url.openConnection() as HttpURLConnection).apply { // Declara constante local (leitura única)
            requestMethod = "POST"
            connectTimeout = 15_000
            readTimeout = 15_000
            doOutput = true

            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("apikey", anonKey)
            setRequestProperty("Authorization", "Bearer $anonKey")
        }

        try { // Tenta executar bloco que pode lançar exceções
            connection.outputStream.use { output ->
                output.write(jsonBody.toString().toByteArray(Charsets.UTF_8))
            }

            val responseCode = connection.responseCode // Declara constante local (leitura única)

            val responseText = if (responseCode in 200..299) { // Estrutura de decisão condicional principal
                connection.inputStream.bufferedReader().use { it.readText() }
            } else { // Fluxo condicional alternativo caso o 'if' seja falso
                connection.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty()
            }

            if (responseCode !in 200..299) { // Estrutura de decisão condicional principal
                return@withContext texts // Retorna o resultado da execução da função
            }

            val json = JSONObject(responseText) // Manipula objeto em formato JSON para transporte de dados
            val translationsArray = json.optJSONArray("translations") ?: return@withContext texts // Retorna o resultado da execução da função

            List(translationsArray.length()) { index ->
                translationsArray.optString(index, texts.getOrElse(index) { "" })
            }
        } catch (_: Exception) { // Captura e trata eventuais exceções ocorridas no bloco try
            texts
        } finally {
            connection.disconnect()
        }
    }
}