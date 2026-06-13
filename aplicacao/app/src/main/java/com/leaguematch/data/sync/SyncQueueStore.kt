package com.leaguematch.data.sync

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/**
 * Fila de alterações pendentes — guarda updates de resultado que falharam
 * por falta de rede para serem reenviadas quando voltar online.
 *
 * Armazenamento: SharedPreferences com JSON serializado (leve e suficiente
 * para a dimensão da app).
 */
class SyncQueueStore(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    data class PendingResultUpdate(
        val jogoId: Int,
        val resultadoCasa: Int,
        val resultadoFora: Int,
        val estado: String,
        val timestamp: Long = System.currentTimeMillis()
    )

    fun enqueueResultUpdate(update: PendingResultUpdate) {
        val list = readAll().toMutableList()
        // Substituir update existente para o mesmo jogo se já existir
        list.removeAll { it.jogoId == update.jogoId }
        list.add(update)
        writeAll(list)
    }

    fun pending(): List<PendingResultUpdate> = readAll()

    fun pendingCount(): Int = readAll().size

    fun remove(jogoId: Int) {
        val list = readAll().filterNot { it.jogoId == jogoId }
        writeAll(list)
    }

    fun clear() {
        prefs.edit().remove(KEY).apply()
    }

    private fun readAll(): List<PendingResultUpdate> {
        val raw = prefs.getString(KEY, null) ?: return emptyList()
        return try {
            val arr = JSONArray(raw)
            (0 until arr.length()).map { i ->
                val o = arr.getJSONObject(i)
                PendingResultUpdate(
                    jogoId = o.optInt("jogoId"),
                    resultadoCasa = o.optInt("resultadoCasa"),
                    resultadoFora = o.optInt("resultadoFora"),
                    estado = o.optString("estado"),
                    timestamp = o.optLong("timestamp")
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun writeAll(list: List<PendingResultUpdate>) {
        val arr = JSONArray()
        for (item in list) {
            val o = JSONObject().apply {
                put("jogoId", item.jogoId)
                put("resultadoCasa", item.resultadoCasa)
                put("resultadoFora", item.resultadoFora)
                put("estado", item.estado)
                put("timestamp", item.timestamp)
            }
            arr.put(o)
        }
        prefs.edit().putString(KEY, arr.toString()).apply()
    }

    companion object {
        private const val PREFS = "lm_sync_queue"
        private const val KEY = "pending_result_updates"
    }
}
