package com.leaguematch.dados.repositorios

import com.leaguematch.dados.modelos.DetalheTorneio
import com.leaguematch.dados.modelos.EstatisticasAdmin
import com.leaguematch.dados.modelos.Goleador
import com.leaguematch.dados.modelos.Jogo
import com.leaguematch.dados.modelos.ParGrafico
import com.leaguematch.dados.modelos.ResumoDashboard
import com.leaguematch.dados.modelos.ResumoModalidade
import com.leaguematch.dados.modelos.TipoUtilizador
import com.leaguematch.dados.modelos.Torneio
import com.leaguematch.dados.modelos.Utilizador
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class SupabaseLeagueMatchRepository(
    private val supabaseUrl: String,
    private val anonKey: String
) : LeagueMatchRepository {
    override suspend fun autenticar(email: String, password: String): Utilizador? {
        if (email.isBlank() || password.isBlank()) return null
        val users = getArray(
            table = "utilizador",
            query = mapOf(
                "select" to "id,nome,email,tipo",
                "email" to "eq.${email.trim()}",
                "password" to "eq.$password",
                "limit" to "1"
            )
        )
        return users.optJSONObject(0)?.toUtilizador()
    }

    override suspend fun obterDashboard(): ResumoDashboard {
        val utilizadores = listarUtilizadores()
        val torneios = listarTorneios()
        return ResumoDashboard(
            totalUtilizadores = utilizadores.size,
            totalTorneios = torneios.size,
            torneiosEmCurso = torneios.count { it.estado == "Em Progresso" },
            alertasSistema = utilizadores.count { !it.ativo }
        )
    }

    override suspend fun listarUtilizadores(): List<Utilizador> {
        return getArray("utilizador", mapOf("select" to "id,nome,email,tipo"))
            .toObjectList()
            .map { it.toUtilizador() }
    }

    override suspend fun obterUtilizador(id: Int): Utilizador? {
        return getArray(
            "utilizador",
            mapOf("select" to "id,nome,email,tipo", "id" to "eq.$id", "limit" to "1")
        ).optJSONObject(0)?.toUtilizador()
    }

    override suspend fun listarModalidades(): List<ResumoModalidade> {
        return listarTorneios()
            .groupBy { it.modalidade }
            .map { (modalidade, torneios) -> ResumoModalidade(modalidade, torneios.size) }
            .sortedBy { it.nome }
    }

    override suspend fun listarTorneiosPorModalidade(modalidade: String): List<Torneio> {
        return listarTorneios().filter { it.modalidade == modalidade }
    }

    override suspend fun obterDetalheTorneio(id: Int): DetalheTorneio? {
        val torneio = listarTorneios().firstOrNull { it.id == id } ?: return null
        val jogos = listarJogos(id)
        return DetalheTorneio(
            torneio = torneio,
            goleadores = listarGoleadores(id),
            jogos = jogos
        )
    }

    override suspend fun obterEstatisticasAdmin(): EstatisticasAdmin {
        val utilizadores = listarUtilizadores()
        val torneios = listarTorneios()
        val jogos = listarJogos()
        val maxEquipas = torneios.maxOfOrNull { it.equipas }?.coerceAtLeast(1) ?: 1
        val divisor = torneios.size.coerceAtLeast(1).toFloat()

        return EstatisticasAdmin(
            totalUtilizadores = utilizadores.size,
            totalTorneios = torneios.size,
            totalJogos = jogos.size,
            alertas = utilizadores.count { !it.ativo },
            jogosPorPeriodo = calcularSerieJogos(jogos.size),
            torneiosPorEstado = listOf(
                ParGrafico("Ativos", torneios.count { it.estado == "Em Progresso" } / divisor),
                ParGrafico("Inscrições", torneios.count { it.estado == "Por Iniciar" } / divisor),
                ParGrafico("Termin.", torneios.count { it.estado == "Finalizado" } / divisor)
            ),
            topTorneios = torneios
                .sortedByDescending { it.equipas }
                .take(4)
                .map { ParGrafico(it.nome, it.equipas / maxEquipas.toFloat()) }
        )
    }

    private suspend fun listarTorneios(): List<Torneio> {
        val equipasPorTorneio = listarEquipasPorTorneio()
        val jogosPorTorneio = listarJogos().groupBy { it.torneioId }
        return getArray("torneio", mapOf("select" to "id,nome,modalidade,regras,formato"))
            .toObjectList()
            .map { json ->
                val id = json.optInt("id")
                val jogos = jogosPorTorneio[id].orEmpty()
                Torneio(
                    id = id,
                    nome = json.optString("nome"),
                    modalidade = json.optString("modalidade"),
                    regras = json.optString("regras"),
                    formato = json.optString("formato"),
                    estado = inferirEstado(jogos),
                    equipas = equipasPorTorneio[id] ?: 0
                )
            }
    }

    private suspend fun listarJogos(torneioId: Int? = null): List<Jogo> {
        val query = mutableMapOf("select" to "id,torneio_id,team_a_id,team_b_id,estado,resultado_a,resultado_b")
        if (torneioId != null) query["torneio_id"] = "eq.$torneioId"
        return getArray("partida", query)
            .toObjectList()
            .map { json ->
                Jogo(
                    id = json.optInt("id"),
                    torneioId = json.optInt("torneio_id"),
                    casa = "Equipa ${json.optInt("team_a_id")}",
                    fora = "Equipa ${json.optInt("team_b_id")}",
                    resultadoCasa = json.optInt("resultado_a"),
                    resultadoFora = json.optInt("resultado_b"),
                    estado = json.optString("estado", "AGENDADO").toEstadoLegivel()
                )
            }
    }

    private suspend fun listarEquipasPorTorneio(): Map<Int, Int> {
        return getArray("equipa", mapOf("select" to "id,torneio_id"))
            .toObjectList()
            .groupingBy { it.optInt("torneio_id") }
            .eachCount()
    }

    private suspend fun listarGoleadores(torneioId: Int): List<Goleador> {
        return getArray(
            "evento_jogo",
            mapOf("select" to "id,user_id,tipo,match_id", "tipo" to "eq.GOLO")
        )
            .toObjectList()
            .groupingBy { "Utilizador ${it.optInt("user_id")}" }
            .eachCount()
            .map { (nome, golos) -> Goleador(nome, golos) }
            .sortedByDescending { it.golos }
            .take(6)
            .ifEmpty { listOf(Goleador("Sem golos registados", 0)) }
    }

    private suspend fun getArray(table: String, query: Map<String, String>): JSONArray = withContext(Dispatchers.IO) {
        if (supabaseUrl.isBlank() || anonKey.isBlank()) {
            error("Configura SUPABASE_URL e SUPABASE_ANON_KEY no aplicacao/local.properties.")
        }

        val queryString = query.entries.joinToString("&") { (key, value) ->
            "${encode(key)}=${encode(value)}"
        }
        val url = URL("${supabaseUrl.trimEnd('/')}/rest/v1/$table?$queryString")
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 10_000
            readTimeout = 10_000
            setRequestProperty("apikey", anonKey)
            setRequestProperty("Authorization", "Bearer $anonKey")
            setRequestProperty("Accept", "application/json")
        }

        val code = connection.responseCode
        val body = if (code in 200..299) {
            connection.inputStream.bufferedReader().use { it.readText() }
        } else {
            connection.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty()
        }
        connection.disconnect()

        if (code !in 200..299) {
            error("Erro Supabase ($code): $body")
        }
        JSONArray(body)
    }

    private fun JSONObject.toUtilizador(): Utilizador {
        return Utilizador(
            id = optInt("id"),
            nome = optString("nome"),
            email = optString("email"),
            tipo = optString("tipo").toTipoUtilizador(),
            ativo = true,
            equipas = 0,
            torneios = 0,
            jogos = 0
        )
    }

    private fun JSONArray.toObjectList(): List<JSONObject> {
        return List(length()) { index -> getJSONObject(index) }
    }

    private fun inferirEstado(jogos: List<Jogo>): String {
        if (jogos.isEmpty()) return "Por Iniciar"
        if (jogos.all { it.estado == "Finalizado" }) return "Finalizado"
        return "Em Progresso"
    }

    private fun calcularSerieJogos(total: Int): List<Float> {
        if (total <= 0) return List(7) { 0.05f }
        return List(7) { index -> ((index + 1).coerceAtMost(total) / total.toFloat()).coerceIn(0.1f, 1f) }
    }

    private fun String.toTipoUtilizador(): TipoUtilizador {
        return TipoUtilizador.entries.firstOrNull {
            it.name.equals(this, ignoreCase = true) || it.descricao.equals(this, ignoreCase = true)
        } ?: TipoUtilizador.PARTICIPANTE
    }

    private fun String.toEstadoLegivel(): String {
        return when (uppercase()) {
            "FINALIZADO" -> "Finalizado"
            "EM_CURSO" -> "Em Progresso"
            else -> "Agendado"
        }
    }

    private fun encode(value: String): String = URLEncoder.encode(value, "UTF-8")
}
