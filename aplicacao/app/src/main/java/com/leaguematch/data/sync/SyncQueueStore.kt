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
/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * A SyncQueueStore gere a persistência local de alterações de dados que falharam
 * por falta de ligação à internet (Offline).
 *
 * Como funciona:
 * 1. Quando uma operação de rede falha, os detalhes do resultado do jogo são encapsulados em `PendingResultUpdate`.
 * 2. Esta classe converte o objeto/lista para uma String no formato JSON (usando JSONArray e JSONObject).
 * 3. Guarda a String JSON nas SharedPreferences do Android sob a chave "pending_result_updates".
 * 4. Assim que a rede é restabelecida, o repositório ou o worker correspondente lê esta lista e volta a tentar enviar.
 *
 * NOTA: Esta é uma solução de sincronização leve e eficiente, ideal para projetos académicos ou apps
 * onde não se queira implementar esquemas complexos de sincronização de bases de dados relacionais (como WorkManager + Room).
 */
class SyncQueueStore(context: Context) { // Define a classe e o construtor primário que aceita um Contexto Android

    private val prefs = context.applicationContext // Obtém o contexto global da app para evitar leaks de memória
        .getSharedPreferences(PREFS, Context.MODE_PRIVATE) // Cria/abre as SharedPreferences LM em modo privado

    data class PendingResultUpdate( // Define a estrutura de dados (data class) para atualizações pendentes
        val jogoId: Int, // Guarda o ID único da partida/jogo a atualizar
        val resultadoCasa: Int, // Guarda a nova pontuação da equipa visitada (casa)
        val resultadoFora: Int, // Guarda a nova pontuação da equipa visitante (fora)
        val estado: String, // Guarda o estado novo do jogo (ex: "FINALIZADO")
        val timestamp: Long = System.currentTimeMillis() // Regista a data/hora em milissegundos em que a queixa ocorreu
    ) // Fim da data class

    fun enqueueResultUpdate(update: PendingResultUpdate) { // Método para enfileirar uma atualização pendente
        val list = readAll().toMutableList() // Lê os registos guardados e converte-os numa lista mutável Kotlin
        list.removeAll { it.jogoId == update.jogoId } // Remove qualquer atualização anterior para este mesmo jogo (evita duplicados)
        list.add(update) // Adiciona a nova atualização à lista
        writeAll(list) // Persiste a lista completa atualizada nas SharedPreferences
    } // Fim de enqueueResultUpdate

    fun pending(): List<PendingResultUpdate> = readAll() // Devolve a lista completa de atualizações pendentes

    fun pendingCount(): Int = readAll().size // Devolve o número total de jogos na fila pendente

    fun remove(jogoId: Int) { // Remove um jogo da fila de pendentes (ex: após sincronização com sucesso)
        val list = readAll().filterNot { it.jogoId == jogoId } // Filtra e exclui o jogo com o ID correspondente
        writeAll(list) // Grava a nova lista sem o elemento removido
    } // Fim de remove

    fun clear() { // Limpa por completo a fila local nas SharedPreferences
        prefs.edit().remove(KEY).apply() // Remove a chave nas SharedPreferences de forma assíncrona
    } // Fim de clear

    private fun readAll(): List<PendingResultUpdate> { // Função privada para ler e desserializar a lista JSON
        val raw = prefs.getString(KEY, null) ?: return emptyList() // Obtém a string JSON ou retorna lista vazia
        return try { // Bloco de segurança para lidar com potenciais dados JSON corrompidos
            val arr = JSONArray(raw) // Tenta ler a string e criar um array JSON
            (0 until arr.length()).map { i -> // Itera sobre cada elemento do array JSON
                val o = arr.getJSONObject(i) // Obtém o objeto JSON no índice i
                PendingResultUpdate( // Instancia o objeto Kotlin com as propriedades lidas
                    jogoId = o.optInt("jogoId"), // Obtém o ID do jogo como inteiro
                    resultadoCasa = o.optInt("resultadoCasa"), // Obtém golos casa
                    resultadoFora = o.optInt("resultadoFora"), // Obtém golos fora
                    estado = o.optString("estado"), // Obtém estado
                    timestamp = o.optLong("timestamp") // Obtém timestamp
                ) // Fim de PendingResultUpdate
            } // Fim do map
        } catch (e: Exception) { // Captura qualquer erro de parsing JSON
            emptyList() // Em caso de erro, retorna lista vazia de forma segura
        } // Fim do try-catch
    } // Fim de readAll

    private fun writeAll(list: List<PendingResultUpdate>) { // Método para serializar e gravar a lista nas SharedPreferences
        val arr = JSONArray() // Cria um novo array JSON vazio
        for (item in list) { // Itera sobre cada objeto da lista de atualizações pendentes
            val o = JSONObject().apply { // Cria um objeto JSON e popula com os campos do item
                put("jogoId", item.jogoId) // Grava ID
                put("resultadoCasa", item.resultadoCasa) // Grava golos casa
                put("resultadoFora", item.resultadoFora) // Grava golos fora
                put("estado", item.estado) // Grava estado
                put("timestamp", item.timestamp) // Grava timestamp
            } // Fim do apply
            arr.put(o) // Adiciona o objeto JSON ao array
        } // Fim do loop for
        prefs.edit().putString(KEY, arr.toString()).apply() // Grava a String JSON final persistida nas SharedPreferences
    } // Fim de writeAll

    companion object { // Bloco com constantes estáticas
        private const val PREFS = "lm_sync_queue" // Nome do ficheiro de SharedPreferences
        private const val KEY = "pending_result_updates" // Chave de armazenamento
    } // Fim do companion object
}
