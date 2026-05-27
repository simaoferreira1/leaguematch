package com.leaguematch.data.repository

import com.leaguematch.data.remote.model.DetalheTorneio
import com.leaguematch.data.remote.model.Equipa
import com.leaguematch.data.remote.model.EstatisticasAdmin
import com.leaguematch.data.remote.model.Goleador
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.data.remote.model.ParGrafico
import com.leaguematch.data.remote.model.ResumoDashboard
import com.leaguematch.data.remote.model.ResumoModalidade
import com.leaguematch.data.remote.model.TipoUtilizador
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.data.remote.model.Classificacao
import com.leaguematch.data.remote.model.EstatisticaJogo
import com.leaguematch.ui.spectator.JogoResumoItem
import com.leaguematch.ui.spectator.MelhorMarcadorItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.security.MessageDigest

class SupabaseLeagueMatchRepository(
    private val supabaseUrl: String,
    private val anonKey: String
) : LeagueMatchRepository {
    override suspend fun autenticar(email: String, password: String): Utilizador? {
        if (email.isBlank() || password.isBlank()) return null
        val trimmedEmail = email.trim()

        // Developer bypass for testing / sandbox verification
        if (trimmedEmail.equals(
                "simao@leaguematch.com",
                ignoreCase = true
            ) && password == "password"
        ) {
            return Utilizador(
                id = 999,
                nome = "Simão Ferreira (Bypass Dev)",
                email = "simao@leaguematch.com",
                tipo = TipoUtilizador.ADMIN,
                active = true,
                equipas = 4,
                torneios = 2,
                jogos = 12
            )
        }

        if (trimmedEmail.equals(
                "organizador@leaguematch.com",
                ignoreCase = true
            ) && password == "password"
        ) {
            return Utilizador(
                id = 888,
                nome = "Organizador (Bypass Dev)",
                email = "organizador@leaguematch.com",
                tipo = TipoUtilizador.ORGANIZADOR,
                active = true,
                equipas = 8,
                torneios = 3,
                jogos = 24
            )
        }

        val users = getArray(
            table = "utilizador",
            query = mapOf(
                "select" to "id,nome,email,tipo",
                "email" to "eq.$trimmedEmail",
                "password" to "eq.${password.toSha256()}",
                "limit" to "1"
            )
        )
        return users.optJSONObject(0)?.toUtilizador()
    }

    override suspend fun registar(
        nome: String,
        email: String,
        password: String,
        tipo: String
    ): Utilizador? {
        val trimmedEmail = email.trim()
        val json = JSONObject().apply {
            put("nome", nome.trim())
            put("email", trimmedEmail)
            put("password", password.toSha256())
            put("tipo", tipo)
        }
        val response = postObject("utilizador", json)
        return response?.toUtilizador()
    }

    override suspend fun atualizarUtilizador(
        id: Int,
        nome: String,
        password: String?
    ): Utilizador? {
        val json = JSONObject().apply {
            put("nome", nome.trim())
            if (!password.isNullOrBlank()) {
                put("password", password.toSha256())
            }
        }
        val response = patchObject("utilizador", id, json)
        return response?.toUtilizador()
    }

    override suspend fun obterDashboard(): ResumoDashboard {
        val utilizadores = listarUtilizadores()
        val torneios = listarTorneios()
        return ResumoDashboard(
            totalUtilizadores = utilizadores.size,
            totalTorneios = torneios.size,
            torneiosEmCurso = torneios.count { it.estado == "Em Progresso" },
            alertasSistema = utilizadores.count { !it.active }
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

    override suspend fun obterMelhoresMarcadores(torneioId: Int): List<MelhorMarcadorItem> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val partidas = getArray(
                    "partida",
                    mapOf("select" to "id", "torneio_id" to "eq.$torneioId")
                ).toObjectList()
                if (partidas.isEmpty()) return@runCatching emptyList<MelhorMarcadorItem>()

                val matchIds = partidas.map { it.optInt("id") }
                val idsFilter = matchIds.joinToString(",")

                val eventos = getArray(
                    "evento_jogo", mapOf(
                        "select" to "user_id,match_id",
                        "tipo" to "eq.GOLO",
                        "match_id" to "in.($idsFilter)"
                    )
                ).toObjectList()

                if (eventos.isEmpty()) return@runCatching emptyList<MelhorMarcadorItem>()

                val golosPorUser = eventos.groupingBy { it.optInt("user_id") }.eachCount()

                val equipas = getArray(
                    "equipa",
                    mapOf("select" to "id,nome", "torneio_id" to "eq.$torneioId")
                ).toObjectList()
                val equipasMap = equipas.associate { it.optInt("id") to it.optString("nome") }

                val teamIds = equipas.map { it.optInt("id") }
                val teamIdsFilter = teamIds.joinToString(",")

                val userTeamMap = if (teamIds.isNotEmpty()) {
                    val members = getArray(
                        "team_member", mapOf(
                            "select" to "user_id,team_id",
                            "team_id" to "in.($teamIdsFilter)"
                        )
                    ).toObjectList()
                    members.associate {
                        it.optInt("user_id") to (equipasMap[it.optInt("team_id")] ?: "Equipa")
                    }
                } else {
                    emptyMap()
                }

                val userIds = golosPorUser.keys.joinToString(",")
                val users = if (userIds.isNotEmpty()) {
                    getArray(
                        "utilizador", mapOf(
                            "select" to "id,nome",
                            "id" to "in.($userIds)"
                        )
                    ).toObjectList().associate { it.optInt("id") to it.optString("nome") }
                } else {
                    emptyMap()
                }

                golosPorUser.map { (userId, golos) ->
                    MelhorMarcadorItem(
                        nome = users[userId] ?: "Jogador $userId",
                        golos = golos,
                        equipa = userTeamMap[userId] ?: "Sem Equipa"
                    )
                }.sortedByDescending { it.golos }
            }.getOrElse {
                it.printStackTrace()
                emptyList()
            }
        }
    }

    override suspend fun obterJogosDoTorneio(torneioId: Int): List<JogoResumoItem> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val equipas = getArray(
                    "equipa",
                    mapOf("select" to "id,nome", "torneio_id" to "eq.$torneioId")
                ).toObjectList()
                val equipasMap = equipas.associate { it.optInt("id") to it.optString("nome") }

                val partidas = getArray(
                    "partida", mapOf(
                        "select" to "team_a_id,team_b_id,resultado_a,resultado_b,estado",
                        "torneio_id" to "eq.$torneioId"
                    )
                ).toObjectList()

                partidas.map { json ->
                    val teamAId = json.optInt("team_a_id")
                    val teamBId = json.optInt("team_b_id")
                    val estado = json.optString("estado")
                    val finalizado =
                        estado.equals("FINALIZADO", ignoreCase = true) || estado.equals(
                            "EM_CURSO",
                            ignoreCase = true
                        )
                    JogoResumoItem(
                        equipaCasa = equipasMap[teamAId] ?: "Equipa $teamAId",
                        equipaFora = equipasMap[teamBId] ?: "Equipa $teamBId",
                        golosCasa = if (finalizado) json.optInt("resultado_a") else null,
                        golosFora = if (finalizado) json.optInt("resultado_b") else null
                    )
                }
            }.getOrElse {
                it.printStackTrace()
                emptyList()
            }
        }
    }

    override suspend fun listarJogosAoVivo(): List<Jogo> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val matchesJson = getArray(
                    "partida", mapOf(
                        "select" to "id,torneio_id,team_a_id,team_b_id,resultado_a,resultado_b,estado",
                        "estado" to "eq.EM_CURSO"
                    )
                ).toObjectList()

                if (matchesJson.isEmpty()) return@runCatching emptyList<Jogo>()

                val teamIds =
                    matchesJson.flatMap { listOf(it.optInt("team_a_id"), it.optInt("team_b_id")) }
                        .distinct()
                val equipasMap = if (teamIds.isNotEmpty()) {
                    val teamIdsFilter = teamIds.joinToString(",")
                    getArray(
                        "equipa", mapOf(
                            "select" to "id,nome",
                            "id" to "in.($teamIdsFilter)"
                        )
                    ).toObjectList().associate { it.optInt("id") to it.optString("nome") }
                } else {
                    emptyMap()
                }

                matchesJson.map { json ->
                    val teamAId = json.optInt("team_a_id")
                    val teamBId = json.optInt("team_b_id")
                    Jogo(
                        id = json.optInt("id"),
                        torneioId = json.optInt("torneio_id"),
                        casa = equipasMap[teamAId] ?: "Equipa $teamAId",
                        fora = equipasMap[teamBId] ?: "Equipa $teamBId",
                        resultadoCasa = json.optInt("resultado_a"),
                        resultadoFora = json.optInt("resultado_b"),
                        estado = "A Decorrer"
                    )
                }
            }.getOrElse {
                it.printStackTrace()
                emptyList()
            }
        }
    }

    override suspend fun listarTodosJogos(): List<Jogo> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val matchesJson = getArray(
                    "partida", mapOf(
                        "select" to "id,torneio_id,team_a_id,team_b_id,resultado_a,resultado_b,estado"
                    )
                ).toObjectList()

                if (matchesJson.isEmpty()) return@runCatching emptyList<Jogo>()

                val teamIds =
                    matchesJson.flatMap { listOf(it.optInt("team_a_id"), it.optInt("team_b_id")) }
                        .distinct()
                val equipasMap = if (teamIds.isNotEmpty()) {
                    val teamIdsFilter = teamIds.joinToString(",")
                    getArray(
                        "equipa", mapOf(
                            "select" to "id,nome",
                            "id" to "in.($teamIdsFilter)"
                        )
                    ).toObjectList().associate { it.optInt("id") to it.optString("nome") }
                } else {
                    emptyMap()
                }

                matchesJson.map { json ->
                    val teamAId = json.optInt("team_a_id")
                    val teamBId = json.optInt("team_b_id")
                    val estadoRaw = json.optString("estado", "AGENDADO")
                    val estado = when (estadoRaw.uppercase()) {
                        "FINALIZADO" -> "Finalizado"
                        "EM_CURSO" -> "A Decorrer"
                        else -> "Agendado"
                    }
                    Jogo(
                        id = json.optInt("id"),
                        torneioId = json.optInt("torneio_id"),
                        casa = equipasMap[teamAId] ?: "Equipa $teamAId",
                        fora = equipasMap[teamBId] ?: "Equipa $teamBId",
                        resultadoCasa = json.optInt("resultado_a"),
                        resultadoFora = json.optInt("resultado_b"),
                        estado = estado
                    )
                }
            }.getOrElse {
                it.printStackTrace()
                emptyList()
            }
        }
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

        val totalUsers = utilizadores.size.coerceAtLeast(1).toFloat()

        // Dynamic sports breakdown
        val modalidadesCount = torneios.groupBy { it.modalidade }.map { (modalidade, list) ->
            ParGrafico(modalidade, list.size.toFloat())
        }.sortedByDescending { it.valorNormalizado }

        // Dynamic user profile breakdown
        val perfilBreakdown = listOf(
            ParGrafico(
                "Participantes",
                (utilizadores.count { it.tipo == TipoUtilizador.PARTICIPANTE } / totalUsers) * 100f),
            ParGrafico(
                "Espectadores",
                (utilizadores.count { it.tipo == TipoUtilizador.ESPECTADOR } / totalUsers) * 100f),
            ParGrafico(
                "Organizadores",
                (utilizadores.count { it.tipo == TipoUtilizador.ORGANIZADOR } / totalUsers) * 100f)
        )

        return EstatisticasAdmin(
            totalUtilizadores = utilizadores.size,
            totalTorneios = torneios.size,
            totalJogos = jogos.size,
            alertas = utilizadores.count { !it.active },
            jogosPorPeriodo = calcularSerieJogos(jogos.size),
            torneiosPorEstado = modalidadesCount,
            topTorneios = perfilBreakdown
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

    override suspend fun listarEquipasTorneio(torneioId: Int): List<Equipa> {
        return getArray(
            "equipa",
            mapOf("select" to "id,nome,torneio_id", "torneio_id" to "eq.$torneioId")
        ).toObjectList().map { json ->
            Equipa(
                id = json.optInt("id"),
                nome = json.optString("nome"),
                torneioId = json.optInt("torneio_id")
            )
        }
    }

    override suspend fun criarEquipa(nome: String, torneioId: Int): Equipa? {
        val json = JSONObject().apply {
            put("nome", nome.trim())
            put("torneio_id", torneioId)
        }
        val response = postObject("equipa", json) ?: return null
        return Equipa(
            id = response.optInt("id"),
            nome = response.optString("nome"),
            torneioId = torneioId
        )
    }

    override suspend fun removerEquipa(equipaId: Int): Boolean {
        deleteObject("equipa", equipaId)
        return true
    }

    override suspend fun atualizarEquipa(id: Int, nome: String): Equipa? {
        val json = JSONObject().apply {
            put("nome", nome.trim())
        }
        val response = patchObject("equipa", id, json) ?: return null
        return Equipa(
            id = response.optInt("id"),
            nome = response.optString("nome"),
            torneioId = response.optInt("torneio_id")
        )
    }

    override suspend fun removerJogo(id: Int): Boolean {
        deleteObject("partida", id)
        return true
    }

    override suspend fun atualizarJogo(
        id: Int,
        resultadoCasa: Int,
        resultadoFora: Int,
        estado: String,
        local: String
    ): Jogo? {
        val estadoRaw = when (estado) {
            "Finalizado" -> "FINALIZADO"
            "A Decorrer" -> "EM_CURSO"
            else -> "AGENDADO"
        }
        val json = JSONObject().apply {
            put("resultado_a", resultadoCasa)
            put("resultado_b", resultadoFora)
            put("estado", estadoRaw)
            put("local", local.trim())
        }
        val response = patchObject("partida", id, json) ?: return null
        return Jogo(
            id = response.optInt("id"),
            torneioId = response.optInt("torneio_id"),
            casa = "Equipa ${response.optInt("team_a_id")}",
            fora = "Equipa ${response.optInt("team_b_id")}",
            resultadoCasa = response.optInt("resultado_a"),
            resultadoFora = response.optInt("resultado_b"),
            estado = response.optString("estado").toEstadoLegivel()
        )
    }

    override suspend fun criarJogo(
        torneioId: Int,
        equipaCasaId: Int,
        equipaForaId: Int,
        data: String,
        hora: String,
        local: String
    ): Jogo? {
        val dataHora = if (data.isNotBlank() && hora.isNotBlank()) {
            val partes = data.split("/")
            if (partes.size == 3) "${partes[2]}-${partes[1]}-${partes[0]}T${hora}:00" else "2026-01-01T00:00:00"
        } else "2026-01-01T00:00:00"
        val localFinal = local.ifBlank { "A definir" }

        val json = JSONObject().apply {
            put("torneio_id", torneioId)
            put("team_a_id", equipaCasaId)
            put("team_b_id", equipaForaId)
            put("estado", "AGENDADO")
            put("resultado_a", 0)
            put("resultado_b", 0)
            put("data_hora", dataHora)
            put("local", localFinal)
        }
        val response = postObject("partida", json) ?: return null
        return Jogo(
            id = response.optInt("id"),
            torneioId = torneioId,
            casa = "Equipa $equipaCasaId",
            fora = "Equipa $equipaForaId",
            resultadoCasa = 0,
            resultadoFora = 0,
            estado = "Agendado"
        )
    }

    override suspend fun removerTorneio(id: Int): Boolean {
        deleteObject("torneio", id)
        return true
    }

    override suspend fun atualizarTorneio(
        id: Int,
        nome: String,
        regras: String,
        formato: String
    ): Torneio? {
        val json = JSONObject().apply {
            put("nome", nome.trim())
            put("regras", regras.trim())
            put("formato", formato.trim())
        }
        val response = patchObject("torneio", id, json) ?: return null
        return Torneio(
            id = response.optInt("id"),
            nome = response.optString("nome"),
            modalidade = response.optString("modalidade"),
            regras = response.optString("regras"),
            formato = response.optString("formato"),
            estado = "Por Iniciar",
            equipas = 0,
            active = true
        )
    }

    override suspend fun criarTorneio(
        nome: String,
        modalidade: String,
        regras: String,
        formato: String,
        organizadorId: Int
    ): Torneio? {
        val json = JSONObject().apply {
            put("nome", nome.trim())
            put("modalidade", modalidade.trim())
            put("regras", regras.trim())
            put("formato", formato.trim())
            put("organizador_id", organizadorId)
        }
        val response = postObject("torneio", json) ?: return null
        return Torneio(
            id = response.optInt("id"),
            nome = response.optString("nome"),
            modalidade = response.optString("modalidade"),
            regras = response.optString("regras"),
            formato = response.optString("formato"),
            estado = "Por Iniciar",
            equipas = 0,
            active = true
        )
    }

    private suspend fun deleteObject(table: String, id: Int): Unit = withContext(Dispatchers.IO) {
        if (supabaseUrl.isBlank() || anonKey.isBlank()) {
            error("Configura SUPABASE_URL e SUPABASE_ANON_KEY no local.properties.")
        }

        val url = URL("${supabaseUrl.trimEnd('/')}/rest/v1/$table?id=eq.$id")

        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "DELETE"
            connectTimeout = 10_000
            readTimeout = 10_000
            setRequestProperty("apikey", anonKey)
            setRequestProperty("Authorization", "Bearer $anonKey")
            setRequestProperty("Content-Type", "application/json")
        }

        val code = connection.responseCode
        val responseBody = if (code in 200..299) {
            connection.inputStream?.bufferedReader()?.use { it.readText() }.orEmpty()
        } else {
            connection.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty()
        }

        connection.disconnect()

        if (code !in 200..299) {
            error("Erro Supabase ($code): $responseBody")
        }
    }

    private suspend fun listarJogos(torneioId: Int? = null): List<Jogo> {
        val query =
            mutableMapOf("select" to "id,torneio_id,team_a_id,team_b_id,estado,resultado_a,resultado_b")
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
    }

    override suspend fun obterClassificacao(torneioId: Int): List<Classificacao> {
        val equipas = getArray(
            table = "equipa",
            query = mapOf(
                "select" to "id,nome",
                "torneio_id" to "eq.$torneioId"
            )
        ).toObjectList().associateBy { it.optInt("id") }

        return getArray(
            table = "classificacao",
            query = mapOf(
                "select" to "team_id,torneio_id,pontos,jogos,vitorias,empates,derrotas,golos_marcados,golos_sofridos",
                "torneio_id" to "eq.$torneioId",
                "order" to "pontos.desc"
            )
        ).toObjectList().map { json ->
            val equipaId = json.optInt("team_id")

            Classificacao(
                equipaId = equipaId,
                nomeEquipa = equipas[equipaId]?.optString("nome") ?: "Equipa $equipaId",
                pontos = json.optInt("pontos"),
                jogos = json.optInt("jogos"),
                vitorias = json.optInt("vitorias"),
                empates = json.optInt("empates"),
                derrotas = json.optInt("derrotas"),
                golosMarcados = json.optInt("golos_marcados"),
                golosSofridos = json.optInt("golos_sofridos")
            )
        }
    }

    private suspend fun getArray(table: String, query: Map<String, String>): JSONArray =
        withContext(Dispatchers.IO) {
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

    private suspend fun postObject(table: String, bodyJson: JSONObject): JSONObject? =
        withContext(Dispatchers.IO) {
            if (supabaseUrl.isBlank() || anonKey.isBlank()) {
                error("Configura SUPABASE_URL e SUPABASE_ANON_KEY no aplicacao/local.properties.")
            }

            val url = URL("${supabaseUrl.trimEnd('/')}/rest/v1/$table")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                connectTimeout = 10_000
                readTimeout = 10_000
                setRequestProperty("apikey", anonKey)
                setRequestProperty("Authorization", "Bearer $anonKey")
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Prefer", "return=representation")
                doOutput = true
            }

            connection.outputStream.bufferedWriter().use { it.write(bodyJson.toString()) }

            val code = connection.responseCode
            val responseBody = if (code in 200..299) {
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                connection.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty()
            }
            connection.disconnect()

            if (code !in 200..299) {
                error("Erro Supabase ($code): $responseBody")
            }

            val array = JSONArray(responseBody)
            if (array.length() > 0) array.getJSONObject(0) else null
        }

    private suspend fun patchObject(table: String, id: Int, bodyJson: JSONObject): JSONObject? =
        withContext(Dispatchers.IO) {
            if (supabaseUrl.isBlank() || anonKey.isBlank()) {
                error("Configura SUPABASE_URL e SUPABASE_ANON_KEY no aplicacao/local.properties.")
            }

            val url = URL("${supabaseUrl.trimEnd('/')}/rest/v1/$table?id=eq.$id")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "PATCH"
                connectTimeout = 10_000
                readTimeout = 10_000
                setRequestProperty("apikey", anonKey)
                setRequestProperty("Authorization", "Bearer $anonKey")
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Prefer", "return=representation")
                doOutput = true
            }

            connection.outputStream.bufferedWriter().use { it.write(bodyJson.toString()) }

            val code = connection.responseCode
            val responseBody = if (code in 200..299) {
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                connection.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty()
            }
            connection.disconnect()

            if (code !in 200..299) {
                error("Erro Supabase ($code): $responseBody")
            }

            val array = JSONArray(responseBody)
            if (array.length() > 0) array.getJSONObject(0) else null
        }

    private fun String.toSha256(): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun JSONObject.toUtilizador(): Utilizador {
        return Utilizador(
            id = optInt("id"),
            nome = optString("nome"),
            email = optString("email"),
            tipo = optString("tipo").toTipoUtilizador(),
            active = true,
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
        return "A Decorrer"
    }

    private fun calcularSerieJogos(total: Int): List<Float> {
        if (total <= 0) return List(7) { 0.05f }
        return List(7) { index ->
            ((index + 1).coerceAtMost(total) / total.toFloat()).coerceIn(
                0.1f,
                1f
            )
        }
    }

    private fun String.toTipoUtilizador(): TipoUtilizador {
        return TipoUtilizador.entries.firstOrNull {
            it.name.equals(this, ignoreCase = true) || it.descricao.equals(this, ignoreCase = true)
        } ?: TipoUtilizador.PARTICIPANTE
    }

    private fun String.toEstadoLegivel(): String {
        return when (uppercase()) {
            "FINALIZADO" -> "Finalizado"
            "EM_CURSO" -> "A Decorrer"
            else -> "Agendado"
        }
    }

    private fun encode(value: String): String =
        URLEncoder.encode(value, "UTF-8")

    suspend fun guardarEstatisticasJogo(
        partidaId: Int,
        estatisticas: List<EstatisticaJogo>
    ): Boolean {

        return withContext(Dispatchers.IO) {

            runCatching {

                estatisticas.forEach { estatistica ->

                    val json = JSONObject().apply {
                        put("partida_id", partidaId)
                        put("tipo", estatistica.tipo)
                        put("equipa", estatistica.equipa)
                        put("valor", estatistica.valor)
                    }

                    postObject(
                        table = "estatistica_jogo",
                        bodyJson = json
                    )
                }

                true

            }.getOrElse {
                it.printStackTrace()
                false
            }
        }
    }
}