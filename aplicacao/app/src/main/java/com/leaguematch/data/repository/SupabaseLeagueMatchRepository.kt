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
import com.leaguematch.data.remote.model.ConfiguracaoNotificacoes
import com.leaguematch.data.remote.model.EstatisticaJogo
import com.leaguematch.data.remote.model.EventoJogo
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
import com.leaguematch.viewmodel.ParticipantStatsData

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
                    "partida",
                    mapOf("select" to "id,torneio_id,team_a_id,team_b_id,resultado_a,resultado_b,estado,data_hora", "estado" to "eq.EM_CURSO")
                ).toObjectList()

                val equipasMap = if (matchesJson.isNotEmpty()) {
                    val ids = matchesJson.flatMap { listOf(it.optInt("team_a_id"), it.optInt("team_b_id")) }.distinct().joinToString(",")
                    getArray(
                        "equipa",
                        mapOf("select" to "id,nome", "id" to "in.($ids)")
                    ).toObjectList().associate { it.optInt("id") to it.optString("nome") }
                } else {
                    emptyMap()
                }

                matchesJson.map { json ->
                    val teamAId = json.optInt("team_a_id")
                    val teamBId = json.optInt("team_b_id")
                    val dataHoraStr = json.optString("data_hora", "")
                    val (dataVal, horaVal) = parseDataHora(dataHoraStr)
                    Jogo(
                        id = json.optInt("id"),
                        torneioId = json.optInt("torneio_id"),
                        casa = equipasMap[teamAId] ?: "Equipa $teamAId",
                        fora = equipasMap[teamBId] ?: "Equipa $teamBId",
                        resultadoCasa = json.optInt("resultado_a"),
                        resultadoFora = json.optInt("resultado_b"),
                        estado = "A Decorrer",
                        data = dataVal,
                        hora = horaVal
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
                        "select" to "id,torneio_id,team_a_id,team_b_id,resultado_a,resultado_b,estado,data_hora"
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
                    val dataHoraStr = json.optString("data_hora", "")
                    val (dataVal, horaVal) = parseDataHora(dataHoraStr)
                    Jogo(
                        id = json.optInt("id"),
                        torneioId = json.optInt("torneio_id"),
                        casa = equipasMap[teamAId] ?: "Equipa $teamAId",
                        fora = equipasMap[teamBId] ?: "Equipa $teamBId",
                        resultadoCasa = json.optInt("resultado_a"),
                        resultadoFora = json.optInt("resultado_b"),
                        estado = estado,
                        data = dataVal,
                        hora = horaVal
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

    override suspend fun listarTorneios(): List<Torneio> {
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
        local: String?,
        data: String?,
        hora: String?
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
            if (local != null) {
                put("local", local.trim())
            }
            if (data != null && hora != null) {
                val partes = data.split("/")
                val dataHora = if (partes.size == 3) "${partes[2]}-${partes[1]}-${partes[0]}T${hora}:00" else "2026-01-01T00:00:00"
                put("data_hora", dataHora)
            }
        }
        val response = patchObject("partida", id, json) ?: return null

        val teamAId = response.optInt("team_a_id")
        val teamBId = response.optInt("team_b_id")
        val equipasMap = runCatching {
            getArray("equipa", mapOf("select" to "id,nome", "id" to "in.($teamAId,$teamBId)"))
                .toObjectList()
                .associate { it.optInt("id") to it.optString("nome") }
        }.getOrDefault(emptyMap())

        val dataHoraStr = response.optString("data_hora", "")
        val (dataVal, horaVal) = parseDataHora(dataHoraStr)

        return Jogo(
            id = response.optInt("id"),
            torneioId = response.optInt("torneio_id"),
            casa = equipasMap[teamAId] ?: "Equipa $teamAId",
            fora = equipasMap[teamBId] ?: "Equipa $teamBId",
            resultadoCasa = response.optInt("resultado_a"),
            resultadoFora = response.optInt("resultado_b"),
            estado = response.optString("estado").toEstadoLegivel(),
            data = dataVal,
            hora = horaVal
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
            mutableMapOf("select" to "id,torneio_id,team_a_id,team_b_id,estado,resultado_a,resultado_b,data_hora")
        if (torneioId != null) query["torneio_id"] = "eq.$torneioId"

        val equipasMap = runCatching {
            getArray("equipa", mapOf("select" to "id,nome"))
                .toObjectList()
                .associate { it.optInt("id") to it.optString("nome") }
        }.getOrDefault(emptyMap())

        return getArray("partida", query)
            .toObjectList()
            .map { json ->
                val teamAId = json.optInt("team_a_id")
                val teamBId = json.optInt("team_b_id")
                val dataHoraStr = json.optString("data_hora", "")
                val (dataVal, horaVal) = parseDataHora(dataHoraStr)
                Jogo(
                    id = json.optInt("id"),
                    torneioId = json.optInt("torneio_id"),
                    casa = equipasMap[teamAId] ?: "Equipa $teamAId",
                    fora = equipasMap[teamBId] ?: "Equipa $teamBId",
                    resultadoCasa = json.optInt("resultado_a"),
                    resultadoFora = json.optInt("resultado_b"),
                    estado = json.optString("estado", "AGENDADO").toEstadoLegivel(),
                    data = dataVal,
                    hora = horaVal
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
        ).toObjectList()

        // Fetch all finalized matches for this tournament
        val matches = getArray(
            table = "partida",
            query = mapOf(
                "select" to "team_a_id,team_b_id,resultado_a,resultado_b,estado",
                "torneio_id" to "eq.$torneioId",
                "estado" to "eq.FINALIZADO"
            )
        ).toObjectList()

        // Create classification map for all teams
        val statsMap = equipas.associate { it.optInt("id") to Classificacao(
            equipaId = it.optInt("id"),
            nomeEquipa = it.optString("nome"),
            pontos = 0,
            jogos = 0,
            vitorias = 0,
            empates = 0,
            derrotas = 0,
            golosMarcados = 0,
            golosSofridos = 0
        ) }.toMutableMap()

        // Process all matches
        for (match in matches) {
            val teamAId = match.optInt("team_a_id")
            val teamBId = match.optInt("team_b_id")
            val scoreA = match.optInt("resultado_a")
            val scoreB = match.optInt("resultado_b")

            val statA = statsMap[teamAId]
            val statB = statsMap[teamBId]

            if (statA != null && statB != null) {
                val updatedA = statA.copy(
                    jogos = statA.jogos + 1,
                    golosMarcados = statA.golosMarcados + scoreA,
                    golosSofridos = statA.golosSofridos + scoreB
                )
                val updatedB = statB.copy(
                    jogos = statB.jogos + 1,
                    golosMarcados = statB.golosMarcados + scoreB,
                    golosSofridos = statB.golosSofridos + scoreA
                )

                val (newStatA, newStatB) = when {
                    scoreA > scoreB -> {
                        Pair(
                            updatedA.copy(pontos = updatedA.pontos + 3, vitorias = updatedA.vitorias + 1),
                            updatedB.copy(derrotas = updatedB.derrotas + 1)
                        )
                    }
                    scoreA < scoreB -> {
                        Pair(
                            updatedA.copy(derrotas = updatedA.derrotas + 1),
                            updatedB.copy(pontos = updatedB.pontos + 3, vitorias = updatedB.vitorias + 1)
                        )
                    }
                    else -> {
                        Pair(
                            updatedA.copy(pontos = updatedA.pontos + 1, empates = updatedA.empates + 1),
                            updatedB.copy(pontos = updatedB.pontos + 1, empates = updatedB.empates + 1)
                        )
                    }
                }

                statsMap[teamAId] = newStatA
                statsMap[teamBId] = newStatB
            }
        }

        // Return sorted by points desc, then goal difference desc, then goals marked desc
        return statsMap.values.sortedWith(
            compareByDescending<Classificacao> { it.pontos }
                .thenByDescending { it.golosMarcados - it.golosSofridos }
                .thenByDescending { it.golosMarcados }
        )
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

    override suspend fun guardarEstatisticasJogo(
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

    override suspend fun obterEstatisticasJogo(partidaId: Int): List<EstatisticaJogo> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val array = getArray(
                    "estatistica_jogo",
                    mapOf("select" to "tipo,equipa,valor", "partida_id" to "eq.$partidaId")
                ).toObjectList()
                array.map { json ->
                    EstatisticaJogo(
                        tipo = json.optString("tipo"),
                        equipa = json.optString("equipa"),
                        valor = json.optInt("valor")
                    )
                }
            }.getOrElse {
                it.printStackTrace()
                emptyList()
            }
        }
    }

    override suspend fun obterEventosJogo(partidaId: Int): List<EventoJogo> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val array = getArray(
                    "evento_jogo",
                    mapOf("select" to "id,tipo,user_id,tempo", "match_id" to "eq.$partidaId")
                ).toObjectList()
                if (array.isEmpty()) return@runCatching emptyList<EventoJogo>()

                val userIds = array.map { it.optInt("user_id") }.distinct().joinToString(",")
                val usersMap = if (userIds.isNotEmpty()) {
                    getArray(
                        "utilizador",
                        mapOf("select" to "id,nome", "id" to "in.($userIds)")
                    ).toObjectList().associate { it.optInt("id") to it.optString("nome") }
                } else {
                    emptyMap()
                }

                val partidaJson = getArray(
                    "partida",
                    mapOf("select" to "team_a_id,team_b_id", "id" to "eq.$partidaId", "limit" to "1")
                ).optJSONObject(0)

                val teamAId = partidaJson?.optInt("team_a_id") ?: -1
                val teamBId = partidaJson?.optInt("team_b_id") ?: -1

                val membersMap = if (userIds.isNotEmpty()) {
                    getArray(
                        "team_member",
                        mapOf("select" to "user_id,team_id", "user_id" to "in.($userIds)")
                    ).toObjectList().associate { it.optInt("user_id") to it.optInt("team_id") }
                } else {
                    emptyMap()
                }

                array.map { json ->
                    val id = json.optInt("id")
                    val tipo = json.optString("tipo")
                    val userId = json.optInt("user_id")
                    val tempo = json.optInt("tempo")
                    val userName = usersMap[userId] ?: "Jogador $userId"
                    val userTeamId = membersMap[userId] ?: -1
                    val equipa = when (userTeamId) {
                        teamAId -> "casa"
                        teamBId -> "fora"
                        else -> "center"
                    }
                    EventoJogo(
                        id = id,
                        matchId = partidaId,
                        tipo = tipo,
                        userId = userId,
                        userName = userName,
                        tempo = tempo,
                        equipa = equipa
                    )
                }.sortedBy { it.tempo }
            }.getOrElse {
                it.printStackTrace()
                emptyList()
            }
        }
    }

    override suspend fun registarEventoJogo(
        partidaId: Int,
        tipo: String,
        equipa: String,
        tempo: Int,
        jogadorNome: String?
    ): Boolean {
        return withContext(Dispatchers.IO) {
            runCatching {
                val partidaJson = getArray(
                    "partida",
                    mapOf("select" to "team_a_id,team_b_id,resultado_a,resultado_b", "id" to "eq.$partidaId", "limit" to "1")
                ).optJSONObject(0)

                val teamAId = partidaJson?.optInt("team_a_id") ?: -1
                val teamBId = partidaJson?.optInt("team_b_id") ?: -1
                val targetTeamId = if (equipa == "casa") teamAId else teamBId

                var selectedUserId = -1

                if (!jogadorNome.isNullOrBlank() && targetTeamId != -1) {
                    val members = getArray(
                        "team_member",
                        mapOf("select" to "user_id", "team_id" to "eq.$targetTeamId")
                    ).toObjectList()

                    if (members.isNotEmpty()) {
                        val memberIds = members.map { it.optInt("user_id") }.joinToString(",")
                        val users = getArray(
                            "utilizador",
                            mapOf("select" to "id,nome", "id" to "in.($memberIds)")
                        ).toObjectList()

                        val matchedUser = users.firstOrNull {
                            it.optString("nome").equals(jogadorNome, ignoreCase = true) ||
                                    it.optString("nome").contains(jogadorNome, ignoreCase = true)
                        }
                        if (matchedUser != null) {
                            selectedUserId = matchedUser.optInt("id")
                        } else if (users.isNotEmpty()) {
                            selectedUserId = users.first().optInt("id")
                        }
                    }
                }

                if (selectedUserId == -1 && targetTeamId != -1) {
                    val members = getArray(
                        "team_member",
                        mapOf("select" to "user_id", "team_id" to "eq.$targetTeamId")
                    ).toObjectList()
                    if (members.isNotEmpty()) {
                        selectedUserId = members.first().optInt("user_id")
                    }
                }

                if (selectedUserId == -1) {
                    val users = getArray(
                        "utilizador",
                        mapOf("select" to "id", "limit" to "1")
                    ).toObjectList()
                    if (users.isNotEmpty()) {
                        selectedUserId = users.first().optInt("id")
                    }
                }

                if (selectedUserId == -1) {
                    selectedUserId = 1
                }

                val json = JSONObject().apply {
                    put("match_id", partidaId)
                    put("tipo", tipo.uppercase())
                    put("user_id", selectedUserId)
                    put("tempo", tempo)
                }

                postObject(
                    table = "evento_jogo",
                    bodyJson = json
                )

                val tipoUpper = tipo.uppercase()
                val incremento = when (tipoUpper) {
                    "GOLO", "ACE", "LANCE_LIVRE" -> 1
                    "DOIS_PONTOS" -> 2
                    "TRES_PONTOS" -> 3
                    else -> 0
                }

                if (incremento > 0) {
                    var resA = partidaJson?.optInt("resultado_a") ?: 0
                    var resB = partidaJson?.optInt("resultado_b") ?: 0

                    if (equipa == "casa") {
                        resA += incremento
                    } else {
                        resB += incremento
                    }

                    val updateJson = JSONObject().apply {
                        put("resultado_a", resA)
                        put("resultado_b", resB)
                    }

                    patchObject("partida", partidaId, updateJson)
                }

                true
            }.getOrElse {
                it.printStackTrace()
                false
            }
        }
    }
    private suspend fun patchObjectByColumn(
        table: String,
        column: String,
        value: Int,
        bodyJson: JSONObject
    ): JSONObject? =
        withContext(Dispatchers.IO) {
            if (supabaseUrl.isBlank() || anonKey.isBlank()) {
                error("Configura SUPABASE_URL e SUPABASE_ANON_KEY no aplicacao/local.properties.")
            }

            val url = URL("${supabaseUrl.trimEnd('/')}/rest/v1/$table?$column=eq.$value")
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

            connection.outputStream.bufferedWriter().use {
                it.write(bodyJson.toString())
            }

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

    override suspend fun obterConfiguracaoNotificacoes(
        utilizadorId: Int
    ): ConfiguracaoNotificacoes {
        val resultado = getArray(
            table = "configuracao_notificacoes",
            query = mapOf(
                "select" to "*",
                "utilizador_id" to "eq.$utilizadorId",
                "limit" to "1"
            )
        )

        val json = resultado.optJSONObject(0)

        return if (json != null) {
            json.toConfiguracaoNotificacoes()
        } else {
            val nova = ConfiguracaoNotificacoes(utilizadorId = utilizadorId)
            criarConfiguracaoNotificacoes(nova)
            nova
        }
    }

    override suspend fun atualizarConfiguracaoNotificacoes(
        configuracao: ConfiguracaoNotificacoes
    ): ConfiguracaoNotificacoes? {
        val body = JSONObject().apply {
            put("notificacoes_jogos", configuracao.notificacoesJogos)
            put("notificacoes_golos", configuracao.notificacoesGolos)
            put("notificacoes_cartoes", configuracao.notificacoesCartoes)
            put("notificacoes_fim_partida", configuracao.notificacoesFimPartida)
            put("som_notificacao", configuracao.somNotificacao)
            put("futebol", configuracao.futebol)
            put("tenis", configuracao.tenis)
            put("basquetebol", configuracao.basquetebol)
            put("andebol", configuracao.andebol)
        }

        return patchObjectByColumn(
            table = "configuracao_notificacoes",
            column = "utilizador_id",
            value = configuracao.utilizadorId,
            bodyJson = body
        )?.toConfiguracaoNotificacoes()
    }

    private suspend fun criarConfiguracaoNotificacoes(
        configuracao: ConfiguracaoNotificacoes
    ) {
        val body = JSONObject().apply {
            put("utilizador_id", configuracao.utilizadorId)
            put("notificacoes_jogos", configuracao.notificacoesJogos)
            put("notificacoes_golos", configuracao.notificacoesGolos)
            put("notificacoes_cartoes", configuracao.notificacoesCartoes)
            put("notificacoes_fim_partida", configuracao.notificacoesFimPartida)
            put("som_notificacao", configuracao.somNotificacao)
            put("futebol", configuracao.futebol)
            put("tenis", configuracao.tenis)
            put("basquetebol", configuracao.basquetebol)
            put("andebol", configuracao.andebol)
        }

        postObject(
            table = "configuracao_notificacoes",
            bodyJson = body
        )
    }

    private fun JSONObject.toConfiguracaoNotificacoes(): ConfiguracaoNotificacoes {
        return ConfiguracaoNotificacoes(
            utilizadorId = optInt("utilizador_id"),
            notificacoesJogos = optBoolean("notificacoes_jogos", true),
            notificacoesGolos = optBoolean("notificacoes_golos", true),
            notificacoesCartoes = optBoolean("notificacoes_cartoes", false),
            notificacoesFimPartida = optBoolean("notificacoes_fim_partida", true),
            somNotificacao = optBoolean("som_notificacao", true),
            futebol = optBoolean("futebol", true),
            tenis = optBoolean("tenis", false),
            basquetebol = optBoolean("basquetebol", true),
            andebol = optBoolean("andebol", false)
        )
    }

    override suspend fun obterEquipaDoParticipante(utilizadorId: Int): Equipa? {
        return withContext(Dispatchers.IO) {
            runCatching {
                val membro = getArray(
                    "team_member",
                    mapOf(
                        "select" to "team_id",
                        "user_id" to "eq.$utilizadorId",
                        "limit" to "1"
                    )
                ).optJSONObject(0) ?: return@runCatching null

                val equipaId = membro.optInt("team_id")

                val equipaJson = getArray(
                    "equipa",
                    mapOf(
                        "select" to "id,nome,torneio_id",
                        "id" to "eq.$equipaId",
                        "limit" to "1"
                    )
                ).optJSONObject(0) ?: return@runCatching null

                Equipa(
                    id = equipaJson.optInt("id"),
                    nome = equipaJson.optString("nome"),
                    torneioId = equipaJson.optInt("torneio_id")
                )
            }.getOrElse {
                it.printStackTrace()
                null
            }
        }
    }

    override suspend fun listarJogadoresEquipa(equipaId: Int): List<Utilizador> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val membros = getArray(
                    "team_member",
                    mapOf(
                        "select" to "user_id",
                        "team_id" to "eq.$equipaId"
                    )
                ).toObjectList()

                if (membros.isEmpty()) return@runCatching emptyList<Utilizador>()

                val ids = membros
                    .map { it.optInt("user_id") }
                    .distinct()
                    .joinToString(",")

                getArray(
                    "utilizador",
                    mapOf(
                        "select" to "id,nome,email,tipo",
                        "id" to "in.($ids)"
                    )
                ).toObjectList().map { it.toUtilizador() }
            }.getOrElse {
                it.printStackTrace()
                emptyList()
            }
        }
    }

    override suspend fun obterClassificacaoEquipa(
        equipaId: Int,
        torneioId: Int
    ): Classificacao? {
        return runCatching {
            obterClassificacao(torneioId)
                .firstOrNull { it.equipaId == equipaId }
        }.getOrElse {
            it.printStackTrace()
            null
        }
    }

    override suspend fun listarJogosDaEquipa(equipaId: Int): List<Jogo> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val partidas = getArray(
                    "partida",
                    mapOf(
                        "select" to "id,torneio_id,team_a_id,team_b_id,resultado_a,resultado_b,estado,data_hora",
                        "or" to "(team_a_id.eq.$equipaId,team_b_id.eq.$equipaId)"
                    )
                ).toObjectList()

                if (partidas.isEmpty()) return@runCatching emptyList<Jogo>()

                val teamIds = partidas
                    .flatMap { listOf(it.optInt("team_a_id"), it.optInt("team_b_id")) }
                    .distinct()
                    .joinToString(",")

                val equipasMap = getArray(
                    "equipa",
                    mapOf(
                        "select" to "id,nome",
                        "id" to "in.($teamIds)"
                    )
                ).toObjectList().associate {
                    it.optInt("id") to it.optString("nome")
                }

                partidas.map { json ->
                    val teamAId = json.optInt("team_a_id")
                    val teamBId = json.optInt("team_b_id")
                    val dataHoraStr = json.optString("data_hora", "")
                    val (dataVal, horaVal) = parseDataHora(dataHoraStr)

                    Jogo(
                        id = json.optInt("id"),
                        torneioId = json.optInt("torneio_id"),
                        casa = equipasMap[teamAId] ?: "Equipa $teamAId",
                        fora = equipasMap[teamBId] ?: "Equipa $teamBId",
                        resultadoCasa = json.optInt("resultado_a"),
                        resultadoFora = json.optInt("resultado_b"),
                        estado = json.optString("estado", "AGENDADO").toEstadoLegivel(),
                        data = dataVal,
                        hora = horaVal
                    )
                }
            }.getOrElse {
                it.printStackTrace()
                emptyList()
            }
        }
    }

    override suspend fun obterEstatisticasParticipante(
        utilizadorId: Int,
        equipaId: Int
    ): ParticipantStatsData {
        return withContext(Dispatchers.IO) {
            runCatching {
                val jogosDaEquipa = listarJogosDaEquipa(equipaId)

                val eventos = getArray(
                    "evento_jogo",
                    mapOf(
                        "select" to "tipo,user_id",
                        "user_id" to "eq.$utilizadorId"
                    )
                ).toObjectList()

                val golos = eventos.count {
                    it.optString("tipo").equals("GOLO", ignoreCase = true)
                }

                val assistencias = eventos.count {
                    it.optString("tipo").equals("ASSISTENCIA", ignoreCase = true)
                }

                val mvp = eventos.count {
                    it.optString("tipo").equals("MVP", ignoreCase = true)
                }

                ParticipantStatsData(
                    jogos = jogosDaEquipa.size,
                    golos = golos,
                    assistencias = assistencias,
                    mvp = mvp
                )
            }.getOrElse {
                it.printStackTrace()
                ParticipantStatsData()
            }
        }
    }
}

private fun parseDataHora(dataHoraStr: String?): Pair<String, String> {
    if (dataHoraStr.isNullOrBlank()) return Pair("", "")
    return try {
        val tIndex = dataHoraStr.indexOf('T')
        if (tIndex != -1) {
            val datePart = dataHoraStr.substring(0, tIndex)
            val timePart = dataHoraStr.substring(tIndex + 1, minOf(tIndex + 6, dataHoraStr.length))
            val dateSplit = datePart.split("-")
            val dateFormatted = if (dateSplit.size == 3) "${dateSplit[2]}/${dateSplit[1]}/${dateSplit[0]}" else datePart
            Pair(dateFormatted, timePart)
        } else {
            Pair(dataHoraStr, "")
        }
    } catch (e: Exception) {
        Pair(dataHoraStr, "")
    }
}