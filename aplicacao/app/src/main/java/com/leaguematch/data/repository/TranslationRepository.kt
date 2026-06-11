package com.leaguematch.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class TranslationRepository(
    private val supabaseUrl: String,
    private val anonKey: String
) {
    private val cache = mutableMapOf<String, String>()

    suspend fun translateText(
        text: String,
        targetLang: String
    ): String = withContext(Dispatchers.IO) {
        if (text.isBlank()) return@withContext text
        if (targetLang.uppercase() == "PT") return@withContext text

        val cacheKey = "${targetLang.uppercase()}:$text"
        cache[cacheKey]?.let { return@withContext it }

        val translated = translateTexts(
            texts = listOf(text),
            targetLang = targetLang
        ).firstOrNull() ?: text

        cache[cacheKey] = translated
        translated
    }

    suspend fun translateTexts(
        texts: List<String>,
        targetLang: String
    ): List<String> = withContext(Dispatchers.IO) {
        if (texts.isEmpty()) return@withContext emptyList()
        if (targetLang.uppercase() == "PT") return@withContext texts

        val url = URL("$supabaseUrl/functions/v1/translate")

        val jsonBody = JSONObject().apply {
            put("texts", JSONArray(texts))
            put("targetLang", targetLang.uppercase())
        }

        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 15_000
            readTimeout = 15_000
            doOutput = true

            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("apikey", anonKey)
            setRequestProperty("Authorization", "Bearer $anonKey")
        }

        try {
            connection.outputStream.use { output ->
                output.write(jsonBody.toString().toByteArray(Charsets.UTF_8))
            }

            val responseCode = connection.responseCode

            val responseText = if (responseCode in 200..299) {
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                connection.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty()
            }

            if (responseCode !in 200..299) {
                return@withContext texts
            }

            val json = JSONObject(responseText)
            val translationsArray = json.optJSONArray("translations") ?: return@withContext texts

            List(translationsArray.length()) { index ->
                translationsArray.optString(index, texts.getOrElse(index) { "" })
            }
        } catch (_: Exception) {
            texts
        } finally {
            connection.disconnect()
        }
    }
}