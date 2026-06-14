package com.leaguematch.data.repository

import com.leaguematch.data.remote.model.AtividadeItem
import com.leaguematch.data.remote.model.DetalheTorneio
import com.leaguematch.data.remote.model.NotificacaoItem
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
import com.leaguematch.data.remote.model.TeamCode
import com.leaguematch.ui.spectator.JogoResumoItem
import com.leaguematch.ui.spectator.MelhorMarcadorItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import org.mindrot.jbcrypt.BCrypt
import com.leaguematch.viewmodel.ParticipantStatsData
import com.leaguematch.data.sync.SyncQueueStore

/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * O SupabaseLeagueMatchRepository implementa o acesso aos dados remotos localizados no Supabase.
 * Ele traduz chamadas Kotlin em pedidos HTTP RESTful (através de métodos como getArray, postObject, patchObject).
 *
 * Características principais para estudar:
 * 1. **Bypasses de Programador**: Credenciais fixas (Ex: simao@leaguematch.com) que permitem testar
 *    rapidamente a aplicação na apresentação com diferentes tipos de utilizadores (Admin, Organizador).
 * 2. **Cifragem BCrypt**: Durante o login e registo, a password é validada contra o hash cifrado
 *    armazenado na BD usando BCrypt para garantir a segurança dos utilizadores.
 */
class SupabaseLeagueMatchRepository(
    private val supabaseUrl: String,
    private val anonKey: String,
    private val syncQueue: SyncQueueStore? = null // Recebe a fila de sincronização offline
) : LeagueMatchRepository {

    /**
     * Valida as credenciais. Primeiro verifica os bypasses locais de teste,
     * e depois consulta a tabela "utilizador" no Supabase e compara o hash BCrypt da password.
     */
    override suspend fun autenticar(email: String, password: String): Utilizador? { // Declara autenticar como suspend (executa assincronamente)
        if (email.isBlank() || password.isBlank()) return null // Se email ou pass forem vazios, aborta e retorna nulo
        val trimmedEmail = email.trim() // Limpa espaços extras no início e fim do email

        // Bypass 1: Verifica se as credenciais correspondem ao programador Simão (Admin)
        if (trimmedEmail.equals(
                "simao@leaguematch.com", // Compara email sem diferenciar maiúsculas/minúsculas
                ignoreCase = true // Ignora se o utilizador digitou com Shift ativo
            ) && password == "password" // Password simples para bypass local
        ) { // Início do bloco bypass
            return Utilizador( // Instancia e retorna um Utilizador fictício completo para fins de testes locais
                id = 999, // Define ID estático
                nome = "Simão Ferreira (Bypass Dev)", // Nome visível na interface
                email = "simao@leaguematch.com", // Email associado
                tipo = TipoUtilizador.ADMIN, // Configura perfil com plenos privilégios de Admin
                active = true, // Define utilizador como ativo
                equipas = 4, // Define dados fictícios de equipas
                torneios = 2, // Define dados fictícios de torneios
                jogos = 12 // Define dados fictícios de jogos
            ) // Fim do objeto
        } // Fim do if do bypass 1

        // Bypass 2: Login rápido simples de administrador local
        if (trimmedEmail.equals("admin", ignoreCase = true) && password == "admin") { // Compara com admin/admin
            return Utilizador( // Instancia utilizador administrador limpo
                id = 777, // ID fictício do admin
                nome = "Admin", // Nome
                email = "admin", // Email
                tipo = TipoUtilizador.ADMIN, // Tipo Admin
                active = true, // Ativo
                equipas = 0, // Sem equipas iniciais
                torneios = 0, // Sem torneios
                jogos = 0 // Sem jogos
            ) // Fim do objeto
        } // Fim do if do bypass 2

        // Bypass 3: Login rápido de organizador local
        if (trimmedEmail.equals(
                "organizador@leaguematch.com", // Compara com email organizador
                ignoreCase = true // Ignora case
            ) && password == "password" // Password de bypass
        ) { // Início do bloco bypass
            return Utilizador( // Instancia utilizador Organizador fictício
                id = 888, // ID fictício
                nome = "Organizador (Bypass Dev)", // Nome do organizador
                email = "organizador@leaguematch.com", // Email
                tipo = TipoUtilizador.ORGANIZADOR, // Atribui perfil de Organizador de torneios
                active = true, // Ativo
                equipas = 8, // Equipas
                torneios = 3, // Torneios
                jogos = 24 // Jogos
            ) // Fim do objeto
        } // Fim do if do bypass 3

        // Consulta remota à BD: Pedido GET à tabela "utilizador" filtrado pelo email
        val candidates = getArray(
            table = "utilizador", // Especifica a tabela no Supabase
            query = mapOf( // Passa os parâmetros de filtragem da Query REST
                "select" to "id,nome,email,tipo,active,password", // Solicita apenas os campos essenciais de segurança
                "email" to "eq.$trimmedEmail", // Filtro "email igual a trimmedEmail"
                "limit" to "1" // Limita a resposta a apenas 1 registo (emails são únicos)
            ) // Fim do map
        ) // Fim do getArray
        val row = candidates.optJSONObject(0) ?: return null // Obtém o primeiro objeto JSON retornado ou aborta se vazio
        val storedHash = row.optString("password") // Extrai o hash da password cifrada guardado no Supabase
        if (storedHash.isBlank()) return null // Se o hash estiver em branco na BD, aborta a autenticação

        // Validação da password fornecida contra o hash encriptado
        val matches = try { // Bloco protetivo para evitar falhas em caso de hash formatado incorretamente
            BCrypt.checkpw(password, storedHash) // Verifica compatibilidade usando o algoritmo BCrypt
        } catch (e: IllegalArgumentException) { // Captura eventuais erros de formato de encriptação
            false // Define matches como falso se houver erro
        } // Fim do try-catch
        return if (matches) row.toUtilizador() else null // Se as passwords baterem, mapeia e devolve o utilizador, senão nulo
    } // Fim de autenticar

    /**
     * Regista um novo utilizador. Aplica BCrypt.hashpw para encriptar a password
     * antes de enviá-la nas chaves do objeto JSON para a tabela "utilizador" do Supabase.
     */
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
            // Gera um sal (salt) e encripta a password do utilizador
            put("password", BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_COST)))
            put("tipo", tipo)
        }
        // Faz o pedido HTTP POST para inserir o registo
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
                put("password", BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_COST)))
            }
        }
        val response = patchObject("utilizador", id, json)
        return response?.toUtilizador()
    }

    override suspend fun redefinirPasswordPorEmail(email: String, novaPassword: String): Boolean {
        val trimmedEmail = email.trim()
        if (trimmedEmail.isBlank() || novaPassword.isBlank()) return false
        val candidates = getArray(
            table = "utilizador",
            query = mapOf(
                "select" to "id",
                "email" to "eq.$trimmedEmail",
                "limit" to "1"
            )
        )
        val row = candidates.optJSONObject(0) ?: return false
        val id = row.optInt("id", -1)
        if (id <= 0) return false
        val json = JSONObject().apply {
            put("password", BCrypt.hashpw(novaPassword, BCrypt.gensalt(BCRYPT_COST)))
        }
        return patchObject("utilizador", id, json) != null
    }

    override suspend fun obterDashboard(): ResumoDashboard {
        val utilizadores = listarUtilizadores()
        val torneios = listarTorneios()

        val agora = java.util.Calendar.getInstance()
        val hoje = agora.clone() as java.util.Calendar
        hoje.set(java.util.Calendar.HOUR_OF_DAY, 0)
        hoje.set(java.util.Calendar.MINUTE, 0)
        hoje.set(java.util.Calendar.SECOND, 0)
        hoje.set(java.util.Calendar.MILLISECOND, 0)

        val seteDiasAtrasMillis = hoje.timeInMillis - 6 * 24 * 60 * 60 * 1000L

        val partidas = getArray(
            "partida",
            mapOf("select" to "id,team_a_id,team_b_id,data_hora,estado,resultado_a,resultado_b,torneio_id")
        ).toObjectList()

        // Agrupar partidas por dia (últimos 7 dias)
        val barras = IntArray(7)
        val isoFmt = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.US)
        val isoFmtNoT = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.US)
        val partidasComData = partidas.mapNotNull { row ->
            val dt = row.optString("data_hora").take(19)
            val ts = runCatching { isoFmt.parse(dt)?.time }.getOrNull()
                ?: runCatching { isoFmtNoT.parse(dt.replace('T', ' '))?.time }.getOrNull()
            ts?.let { row to it }
        }
        for ((_, ts) in partidasComData) {
            if (ts >= seteDiasAtrasMillis && ts <= agora.timeInMillis) {
                val diasAtras = ((agora.timeInMillis - ts) / (24L * 60 * 60 * 1000)).toInt().coerceIn(0, 6)
                val idx = 6 - diasAtras
                barras[idx]++
            }
        }
        val total7d = barras.sum()

        // Atividade recente: últimas 4 partidas terminadas, mais recentes primeiro
        val recentes = partidasComData
            .filter { (row, _) -> row.optString("estado").equals("FINALIZADO", ignoreCase = true) }
            .sortedByDescending { it.second }
            .take(4)

        val equipasMap = getArray("equipa", mapOf("select" to "id,nome"))
            .toObjectList()
            .associate { it.optInt("id") to it.optString("nome") }

        val atividade = recentes.map { (row, ts) ->
            val ca = equipasMap[row.optInt("team_a_id")] ?: "Equipa"
            val cb = equipasMap[row.optInt("team_b_id")] ?: "Equipa"
            AtividadeItem(
                who = "$ca vs $cb",
                what = "terminou ${row.optInt("resultado_a")}-${row.optInt("resultado_b")}",
                whenLabel = formatTempoRelativo(agora.timeInMillis - ts),
                categoria = "JOGO"
            )
        }.toMutableList()

        // Acrescentar utilizadores mais recentes (sem timestamp na BD usamos id descrescente)
        utilizadores.sortedByDescending { it.id }.take(2).forEach {
            atividade += AtividadeItem(
                who = it.nome,
                what = "registou-se na aplicação",
                whenLabel = "recente",
                categoria = "REGISTO"
            )
        }

        return ResumoDashboard(
            totalUtilizadores = utilizadores.size,
            totalTorneios = torneios.size,
            torneiosEmCurso = torneios.count { it.estado == "Em Progresso" },
            alertasSistema = utilizadores.count { !it.active },
            atividadeUltimos7Dias = barras.toList(),
            totalEventos7Dias = total7d,
            atividadeRecente = atividade.take(5)
        )
    }

    private fun formatTempoRelativo(deltaMs: Long): String {
        val seg = deltaMs / 1000
        val min = seg / 60
        val h = min / 60
        val d = h / 24
        return when {
            seg < 60 -> "agora"
            min < 60 -> "${min} min"
            h < 24 -> "${h} h"
            d < 7 -> "${d} d"
            else -> "+ 1 sem"
        }
    }

    override suspend fun listarUtilizadores(): List<Utilizador> {
        return getArray("utilizador", mapOf("select" to "id,nome,email,tipo,active"))
            .toObjectList()
            .map { it.toUtilizador() }
    }

    override suspend fun obterUtilizador(id: Int): Utilizador? {
        return getArray(
            "utilizador",
            mapOf("select" to "id,nome,email,tipo,active", "id" to "eq.$id", "limit" to "1")
        ).optJSONObject(0)?.toUtilizador()
    }

    override suspend fun alterarEstadoUtilizador(
        id: Int,
        ativo: Boolean
    ): Boolean {

        val json = JSONObject().apply {
            put("active", ativo)
        }

        patchObject("utilizador", id, json)

        return true
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
                    mapOf(
                        "select" to "id,torneio_id,team_a_id,team_b_id,resultado_a,resultado_b,estado,data_hora,local,iniciado_em",
                        "estado" to "eq.EM_CURSO"
                    )
                ).toObjectList()

                val equipasMap = if (matchesJson.isNotEmpty()) {
                    val ids = matchesJson
                        .flatMap { listOf(it.optInt("team_a_id"), it.optInt("team_b_id")) }
                        .distinct()
                        .joinToString(",")

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
                        equipaCasaId = teamAId,
                        equipaForaId = teamBId,
                        resultadoCasa = json.optInt("resultado_a"),
                        resultadoFora = json.optInt("resultado_b"),
                        estado = "A Decorrer",
                        data = dataVal,
                        hora = horaVal,
                        local = json.optString("local", "A definir"),
                        iniciado_em = json.optString("iniciado_em")
                            .takeIf { it.isNotBlank() && it != "null" }
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
                    "partida",
                    mapOf(
                        "select" to "id,torneio_id,team_a_id,team_b_id,resultado_a,resultado_b,estado,data_hora,local,iniciado_em"
                    )
                ).toObjectList()

                if (matchesJson.isEmpty()) return@runCatching emptyList<Jogo>()

                val teamIds = matchesJson
                    .flatMap { listOf(it.optInt("team_a_id"), it.optInt("team_b_id")) }
                    .distinct()

                val equipasMap = if (teamIds.isNotEmpty()) {
                    val teamIdsFilter = teamIds.joinToString(",")

                    getArray(
                        "equipa",
                        mapOf(
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
                        equipaCasaId = teamAId,
                        equipaForaId = teamBId,
                        resultadoCasa = json.optInt("resultado_a"),
                        resultadoFora = json.optInt("resultado_b"),
                        estado = estado,
                        data = dataVal,
                        hora = horaVal,
                        local = json.optString("local", "A definir"),
                        iniciado_em = json.optString("iniciado_em")
                            .takeIf { it.isNotBlank() && it != "null" }
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
        val (faltas, cartoes) = contarEventosNaoGolo(jogos.map { it.id })
        return DetalheTorneio(
            torneio = torneio,
            goleadores = listarGoleadores(id),
            jogos = jogos,
            totalFaltas = faltas,
            totalCartoes = cartoes
        )
    }

    private suspend fun contarEventosNaoGolo(matchIds: List<Int>): Pair<Int, Int> {
        if (matchIds.isEmpty()) return 0 to 0
        val idsFilter = matchIds.joinToString(",")
        val eventos = getArray(
            "evento_jogo",
            mapOf(
                "select" to "tipo",
                "match_id" to "in.($idsFilter)"
            )
        ).toObjectList()
        var faltas = 0
        var cartoes = 0
        for (e in eventos) {
            when (e.optString("tipo").uppercase()) {
                "FALTA" -> faltas++
                "CARTAO", "CARTAO_AMARELO", "CARTAO_VERMELHO", "AMARELO", "VERMELHO" -> cartoes++
            }
        }
        return faltas to cartoes
    }

    override suspend fun obterEstatisticasAdmin(): EstatisticasAdmin {
        return obterEstatisticasAdmin("30d")
    }

    override suspend fun obterEstatisticasAdmin(periodo: String): EstatisticasAdmin {
        val utilizadores = listarUtilizadores()
        val torneios = listarTorneios()
        val jogos = listarJogos()

        val totalUsers = utilizadores.size.coerceAtLeast(1).toFloat()

        val modalidadesCount = torneios
            .groupBy { it.modalidade }
            .map { (modalidade, lista) ->
                ParGrafico(modalidade, lista.size.toFloat())
            }

        val perfilBreakdown = listOf(
            ParGrafico(
                "Participantes",
                (utilizadores.count { it.tipo == TipoUtilizador.PARTICIPANTE } / totalUsers) * 100f
            ),
            ParGrafico(
                "Espectadores",
                (utilizadores.count { it.tipo == TipoUtilizador.ESPECTADOR } / totalUsers) * 100f
            ),
            ParGrafico(
                "Organizadores",
                (utilizadores.count { it.tipo == TipoUtilizador.ORGANIZADOR } / totalUsers) * 100f
            )
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

        return getArray(
            "torneio",
            mapOf(
                "select" to "id,nome,modalidade,regras,formato,organizador_id,active",
                "active" to "eq.true"
            )
        )
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
                    equipas = equipasPorTorneio[id] ?: 0,
                    organizadorId = if (json.isNull("organizador_id")) null else json.optInt("organizador_id"),
                    active = json.optBoolean("active", true)
                )
            }
    }

    override suspend fun listarTorneiosDoOrganizador(organizadorId: Int): List<Torneio> {
        return listarTorneios().filter { it.organizadorId == organizadorId }
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

    /**
     * ESTUDAR PARA A APRESENTAÇÃO (Mecanismo Offline):
     * A função `atualizarJogo` tenta atualizar o resultado do jogo no servidor Supabase.
     * Se falhar por erro de rede ( IOException ), captura o erro e guarda os dados localmente
     * no `syncQueue` (SharedPreferences) para envio posterior.
     * Devolve uma resposta "otimista" instantânea para a UI não bloquear.
     */
    override suspend fun atualizarJogo(
        id: Int,
        resultadoCasa: Int,
        resultadoFora: Int,
        estado: String,
        local: String?,
        data: String?,
        hora: String?,
        atualizarInicio: Boolean
    ): Jogo? = try {
        atualizarJogoInterno(id, resultadoCasa, resultadoFora, estado, local, data, hora, atualizarInicio)
    } catch (e: java.io.IOException) {
        // Captura falha de rede/Internet: Envia para a fila local pendente
        syncQueue?.enqueueResultUpdate(
            SyncQueueStore.PendingResultUpdate(
                jogoId = id,
                resultadoCasa = resultadoCasa,
                resultadoFora = resultadoFora,
                estado = estado
            )
        )
        // Cria e devolve um objeto Jogo simulado e otimista para a UI refletir a alteração local
        Jogo(
            id = id,
            torneioId = 0,
            casa = "",
            fora = "",
            equipaCasaId = 0,
            equipaForaId = 0,
            resultadoCasa = resultadoCasa,
            resultadoFora = resultadoFora,
            estado = estado,
            local = local.orEmpty(),
            data = data.orEmpty(),
            hora = hora.orEmpty()
        )
    }

    private suspend fun atualizarJogoInterno(
        id: Int,
        resultadoCasa: Int,
        resultadoFora: Int,
        estado: String,
        local: String?,
        data: String?,
        hora: String?,
        atualizarInicio: Boolean
    ): Jogo? {
        val estadoRaw = when (estado) {
            "Finalizado" -> "FINALIZADO"
            "A Decorrer" -> "EM_CURSO"
            "EM_CURSO" -> "EM_CURSO"
            "FINALIZADO" -> "FINALIZADO"
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
                val dataHora =
                    if (partes.size == 3) {
                        "${partes[2]}-${partes[1]}-${partes[0]}T${hora}:00"
                    } else {
                        "2026-01-01T00:00:00"
                    }

                put("data_hora", dataHora)
            }

            if (estadoRaw == "EM_CURSO" && atualizarInicio) {
                put("iniciado_em", java.time.Instant.now().toString())
            }
        }

        val response = patchObject("partida", id, json) ?: return null

        val teamAId = response.optInt("team_a_id")
        val teamBId = response.optInt("team_b_id")

        val equipasMap = runCatching {
            getArray(
                "equipa",
                mapOf(
                    "select" to "id,nome",
                    "id" to "in.($teamAId,$teamBId)"
                )
            )
                .toObjectList()
                .associate { it.optInt("id") to it.optString("nome") }
        }.getOrDefault(emptyMap())

        // Notificações automáticas para mudanças importantes
        runCatching {
            val nomeA = equipasMap[teamAId] ?: "Equipa"
            val nomeB = equipasMap[teamBId] ?: "Equipa"
            val torneioId = response.optInt("torneio_id")

            when (estadoRaw) {
                "FINALIZADO" -> {
                    criarNotificacaoParaTodos(
                        "Resultado final: $nomeA $resultadoCasa - $resultadoFora $nomeB"
                    )

                    criarNotificacaoParaOrganizadorDoTorneio(
                        torneioId = torneioId,
                        mensagem = "Jogo terminado no teu torneio: $nomeA $resultadoCasa - $resultadoFora $nomeB"
                    )
                }

                "EM_CURSO" -> if (atualizarInicio) {
                    criarNotificacaoParaTodos("$nomeA vs $nomeB começou agora!")

                    criarNotificacaoParaOrganizadorDoTorneio(
                        torneioId = torneioId,
                        mensagem = "Jogo iniciado no teu torneio: $nomeA vs $nomeB começou agora!"
                    )
                }
            }

            if (data != null && hora != null && estadoRaw == "AGENDADO") {
                criarNotificacaoParaTodos(
                    "Alteração no calendário: $nomeA vs $nomeB → $data às $hora"
                )

                criarNotificacaoParaOrganizadorDoTorneio(
                    torneioId = torneioId,
                    mensagem = "Alteração num jogo do teu torneio: $nomeA vs $nomeB → $data às $hora"
                )
            }
        }

        val dataHoraStr = response.optString("data_hora", "")
        val (dataVal, horaVal) = parseDataHora(dataHoraStr)

        return Jogo(
            id = response.optInt("id"),
            torneioId = response.optInt("torneio_id"),
            casa = equipasMap[teamAId] ?: "Equipa $teamAId",
            fora = equipasMap[teamBId] ?: "Equipa $teamBId",
            equipaCasaId = teamAId,
            equipaForaId = teamBId,
            resultadoCasa = response.optInt("resultado_a"),
            resultadoFora = response.optInt("resultado_b"),
            estado = response.optString("estado").toEstadoLegivel(),
            data = dataVal,
            hora = horaVal,
            local = response.optString("local", "A definir"),
            iniciado_em = response.optString("iniciado_em").takeIf { it.isNotBlank() && it != "null" }
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

        runCatching {
            val nomesMap = getArray(
                "equipa",
                mapOf("select" to "id,nome", "id" to "in.($equipaCasaId,$equipaForaId)")
            ).toObjectList().associate { it.optInt("id") to it.optString("nome") }
            val nomeA = nomesMap[equipaCasaId] ?: "Equipa"
            val nomeB = nomesMap[equipaForaId] ?: "Equipa"
            criarNotificacaoParaTodos(
                "Novo jogo agendado: $nomeA vs $nomeB · $data às $hora"
            )

            criarNotificacaoParaOrganizadorDoTorneio(
                torneioId = torneioId,
                mensagem = "Novo jogo agendado no teu torneio: $nomeA vs $nomeB · $data às $hora"
            )
        }

        return Jogo(
            id = response.optInt("id"),
            torneioId = torneioId,
            casa = "Equipa $equipaCasaId",
            fora = "Equipa $equipaForaId",
            equipaCasaId = equipaCasaId,
            equipaForaId = equipaForaId,
            resultadoCasa = 0,
            resultadoFora = 0,
            estado = "Agendado",
            local = localFinal
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

    private suspend fun deleteWhere(table: String, filters: Map<String, String>): Unit = withContext(Dispatchers.IO) {
        if (supabaseUrl.isBlank() || anonKey.isBlank()) {
            error("Configura SUPABASE_URL e SUPABASE_ANON_KEY no local.properties.")
        }

        val query = filters.entries.joinToString("&") { (k, v) ->
            "${URLEncoder.encode(k, "UTF-8")}=${URLEncoder.encode(v, "UTF-8")}"
        }
        val url = URL("${supabaseUrl.trimEnd('/')}/rest/v1/$table?$query")

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

    private suspend fun listarJogos(torneioId: Int? = null): List<Jogo> { // Declaração de listarJogos com torneioId opcional
        val query = // Declara query string para obter dados da partida no Supabase
            mutableMapOf("select" to "id,torneio_id,team_a_id,team_b_id,estado,resultado_a,resultado_b,data_hora,local,iniciado_em") // Campos solicitados
        if (torneioId != null) query["torneio_id"] = "eq.$torneioId" // Se houver torneioId, adiciona filtro de igualdade

        val equipasMap = runCatching { // Tenta obter o mapa de nomes de equipas com tratamento de erro
            getArray("equipa", mapOf("select" to "id,nome")) // Faz GET da tabela equipa com id e nome
                .toObjectList() // Converte resultado para lista de JSON
                .associate { it.optInt("id") to it.optString("nome") } // Cria mapa associativo ID -> Nome
        }.getOrDefault(emptyMap()) // Em caso de exceção, retorna mapa vazio

        return getArray("partida", query) // Obtém a lista de partidas correspondentes
            .toObjectList() // Converte JSONArray em lista de objetos JSON
            .map { json -> // Mapeia cada objeto JSON para o modelo Jogo
                val teamAId = json.optInt("team_a_id") // Extrai ID da equipa casa
                val teamBId = json.optInt("team_b_id") // Extrai ID da equipa fora
                val dataHoraStr = json.optString("data_hora", "") // Extrai string de data e hora
                val (dataVal, horaVal) = parseDataHora(dataHoraStr) // Decompõe em data e hora legíveis
                Jogo( // Instancia e retorna a representação de Jogo
                    id = json.optInt("id"), // ID da partida
                    torneioId = json.optInt("torneio_id"), // ID do torneio
                    casa = equipasMap[teamAId] ?: "Equipa $teamAId", // Nome da equipa casa ou padrão
                    fora = equipasMap[teamBId] ?: "Equipa $teamBId", // Nome da equipa fora ou padrão
                    equipaCasaId = teamAId, // ID da equipa casa
                    equipaForaId = teamBId, // ID da equipa fora
                    resultadoCasa = json.optInt("resultado_a"), // Pontuação da equipa casa
                    resultadoFora = json.optInt("resultado_b"), // Pontuação da equipa fora
                    estado = json.optString("estado", "AGENDADO").toEstadoLegivel(), // Estado traduzido
                    data = dataVal, // Data formatada
                    hora = horaVal, // Hora formatada
                    local = json.optString("local", "A definir"), // Local do jogo
                    iniciado_em = json.optString("iniciado_em") // Data/hora de início se existir
                        .takeIf { it.isNotBlank() && it != "null" } // Filtra strings inválidas ou nulas
                ) // Fim da criação do Jogo
            } // Fim do map
    } // Fim de listarJogos

    private suspend fun listarEquipasPorTorneio(): Map<Int, Int> { // Função para contar equipas agrupadas por torneio
        return getArray("equipa", mapOf("select" to "id,torneio_id")) // Carrega apenas ID e ID de torneio das equipas
            .toObjectList() // Converte para lista
            .groupingBy { it.optInt("torneio_id") } // Agrupa pelo ID do torneio
            .eachCount() // Conta elementos em cada grupo
    }

    private suspend fun listarGoleadores(torneioId: Int): List<Goleador> { // Função auxiliar para listar melhores marcadores
        val partidas = getArray( // Obtém partidas do torneio especificado
            "partida", // Tabela partida
            mapOf("select" to "id", "torneio_id" to "eq.$torneioId") // Filtro de ID e torneio
        ).toObjectList() // Converte para lista
        if (partidas.isEmpty()) return emptyList() // Retorna lista vazia se não existirem partidas

        val matchIds = partidas.map { it.optInt("id") } // Extrai lista de IDs das partidas
        val matchIdsFilter = matchIds.joinToString(",") // Junta IDs por vírgulas para a cláusula IN

        val eventos = getArray( // Obtém eventos do tipo golo nestas partidas
            "evento_jogo", // Tabela evento_jogo
            mapOf( // Filtros
                "select" to "user_id,tipo,match_id", // Campos selecionados
                "tipo" to "eq.GOLO", // Filtra apenas golos
                "match_id" to "in.($matchIdsFilter)" // Cláusula de inclusão nas partidas
            ) // Fim de filtros
        ).toObjectList() // Converte para lista
        if (eventos.isEmpty()) return emptyList() // Retorna lista vazia se não houver golos registados

        val golosPorUser = eventos.groupingBy { it.optInt("user_id") }.eachCount() // Conta golos por cada jogador

        val equipas = getArray( // Obtém as equipas que participam no torneio
            "equipa", // Tabela equipa
            mapOf("select" to "id,nome", "torneio_id" to "eq.$torneioId") // Seleção e filtro de torneio
        ).toObjectList() // Converte para lista
        val equipasMap = equipas.associate { it.optInt("id") to it.optString("nome") } // Mapa ID -> Nome da equipa

        val teamIds = equipas.map { it.optInt("id") } // Obtém IDs de todas as equipas do torneio
        val userTeamMap: Map<Int, String> = if (teamIds.isNotEmpty()) { // Se existirem equipas no torneio
            val members = getArray( // Obtém os membros dessas equipas
                "team_member", // Tabela team_member
                mapOf( // Filtros
                    "select" to "user_id,team_id", // Seleciona user e equipa
                    "team_id" to "in.(${teamIds.joinToString(",")})" // Cláusula IN para as equipas do torneio
                ) // Fim dos filtros
            ).toObjectList() // Converte para lista
            members.associate { // Associa utilizador à respetiva equipa
                it.optInt("user_id") to (equipasMap[it.optInt("team_id")] ?: "") // Associa ID de utilizador ao nome da equipa
            } // Fim de associação
        } else emptyMap() // Caso contrário, retorna mapa vazio

        val userIds = golosPorUser.keys.joinToString(",") // Junta IDs dos marcadores por vírgula
        val usersMap: Map<Int, String> = if (userIds.isNotEmpty()) { // Se houver marcadores identificados
            getArray( // Obtém os nomes dos marcadores
                "utilizador", // Tabela utilizador
                mapOf( // Filtros
                    "select" to "id,nome", // Seleciona ID e nome
                    "id" to "in.($userIds)" // Cláusula IN para os utilizadores
                ) // Fim de filtros
            ).toObjectList().associate { it.optInt("id") to it.optString("nome") } // Associa ID de utilizador ao nome correspondente
        } else emptyMap() // Caso contrário, retorna mapa vazio

        return golosPorUser.entries // Devolve a lista de goleadores mapeada
            .map { (userId, count) -> // Para cada entrada de utilizador e contagem de golos
                Goleador( // Instancia o modelo Goleador
                    nome = usersMap[userId] ?: "Utilizador $userId", // Nome do utilizador
                    golos = count, // Quantidade de golos
                    equipa = userTeamMap[userId] ?: "" // Nome da equipa a que pertence
                ) // Fim de Goleador
            } // Fim do map
            .sortedByDescending { it.golos } // Ordena por golos em ordem decrescente
            .take(10) // Limita aos 10 melhores marcadores do torneio
    }

    override suspend fun obterClassificacao(torneioId: Int): List<Classificacao> { // Declaração de obterClassificacao do torneio
        val equipas = getArray( // Obtém as equipas do torneio no Supabase
            table = "equipa", // Tabela equipa
            query = mapOf( // Filtros
                "select" to "id,nome", // Seleciona id e nome
                "torneio_id" to "eq.$torneioId" // Filtra pelo ID do torneio
            ) // Fim de query
        ).toObjectList() // Converte para lista

        // Fetch all finalized matches for this tournament
        val matches = getArray( // Obtém partidas finalizadas do torneio no Supabase
            table = "partida", // Tabela partida
            query = mapOf( // Filtros
                "select" to "team_a_id,team_b_id,resultado_a,resultado_b,estado", // Seleciona campos de resultado e equipas
                "torneio_id" to "eq.$torneioId", // Filtra pelo ID do torneio
                "estado" to "eq.FINALIZADO" // Filtra apenas jogos terminados
            ) // Fim de query
        ).toObjectList() // Converte para lista

        // Create classification map for all teams
        val statsMap = equipas.associate { it.optInt("id") to Classificacao( // Inicializa estatísticas de classificação para todas as equipas
            equipaId = it.optInt("id"), // ID da equipa
            nomeEquipa = it.optString("nome"), // Nome da equipa
            pontos = 0, // Pontos iniciais
            jogos = 0, // Jogos iniciais
            vitorias = 0, // Vitórias iniciais
            empates = 0, // Empates iniciais
            derrotas = 0, // Derrotas iniciais
            golosMarcados = 0, // Golos marcados iniciais
            golosSofridos = 0 // Golos sofridos iniciais
        ) }.toMutableMap() // Converte para mapa mutável para podermos alterar os valores

        // Process all matches
        for (match in matches) { // Itera sobre todas as partidas finalizadas
            val teamAId = match.optInt("team_a_id") // ID da equipa casa
            val teamBId = match.optInt("team_b_id") // ID da equipa fora
            val scoreA = match.optInt("resultado_a") // Golos da equipa casa
            val scoreB = match.optInt("resultado_b") // Golos da equipa fora

            val statA = statsMap[teamAId] // Estatísticas atuais da equipa casa
            val statB = statsMap[teamBId] // Estatísticas atuais da equipa fora

            if (statA != null && statB != null) { // Se ambas as estatísticas existirem no mapa
                val updatedA = statA.copy( // Cria cópia atualizada da equipa casa
                    jogos = statA.jogos + 1, // Incrementa total de jogos
                    golosMarcados = statA.golosMarcados + scoreA, // Soma golos marcados
                    golosSofridos = statA.golosSofridos + scoreB // Soma golos sofridos
                ) // Fim de copy casa
                val updatedB = statB.copy( // Cria cópia atualizada da equipa fora
                    jogos = statB.jogos + 1, // Incrementa total de jogos
                    golosMarcados = statB.golosMarcados + scoreB, // Soma golos marcados
                    golosSofridos = statB.golosSofridos + scoreA // Soma golos sofridos
                ) // Fim de copy fora

                val (newStatA, newStatB) = when { // Decide resultado com base nos golos
                    scoreA > scoreB -> { // Vitória da equipa casa
                        Pair( // Retorna par com estatísticas atualizadas
                            updatedA.copy(pontos = updatedA.pontos + 3, vitorias = updatedA.vitorias + 1), // Adiciona 3 pontos e 1 vitória
                            updatedB.copy(derrotas = updatedB.derrotas + 1) // Adiciona 1 derrota
                        ) // Fim de Pair
                    } // Fim de vitória casa
                    scoreA < scoreB -> { // Vitória da equipa fora
                        Pair( // Retorna par com estatísticas atualizadas
                            updatedA.copy(derrotas = updatedA.derrotas + 1), // Adiciona 1 derrota
                            updatedB.copy(pontos = updatedB.pontos + 3, vitorias = updatedB.vitorias + 1) // Adiciona 3 pontos e 1 vitória
                        ) // Fim de Pair
                    } // Fim de vitória fora
                    else -> { // Empate
                        Pair( // Retorna par com estatísticas atualizadas
                            updatedA.copy(pontos = updatedA.pontos + 1, empates = updatedA.empates + 1), // Adiciona 1 ponto e 1 empate
                            updatedB.copy(pontos = updatedB.pontos + 1, empates = updatedB.empates + 1) // Adiciona 1 ponto e 1 empate
                        ) // Fim de Pair
                    } // Fim de empate
                } // Fim do when

                statsMap[teamAId] = newStatA // Atualiza registo no mapa para equipa casa
                statsMap[teamBId] = newStatB // Atualiza registo no mapa para equipa fora
            } // Fim do if
        } // Fim do for

        // Return sorted by points desc, then goal difference desc, then goals marked desc
        return statsMap.values.sortedWith( // Retorna a lista de classificações ordenada
            compareByDescending<Classificacao> { it.pontos } // Ordenação primária: Pontos descrescente
                .thenByDescending { it.golosMarcados - it.golosSofridos } // Ordenação secundária: Diferença de golos descrescente
                .thenByDescending { it.golosMarcados } // Ordenação terciária: Golos marcados descrescente
        ) // Fim do sortedWith
    }

    private suspend fun getArray(table: String, query: Map<String, String>): JSONArray = // Declaração de getArray para obter lista de objetos do Supabase
        withContext(Dispatchers.IO) { // Altera o contexto de execução para o dispatcher de IO
            if (supabaseUrl.isBlank() || anonKey.isBlank()) { // Verifica se o URL ou a chave anónima estão em branco
                error("Configura SUPABASE_URL e SUPABASE_ANON_KEY no aplicacao/local.properties.") // Lança erro de configuração
            } // Fim do if

            val queryString = query.entries.joinToString("&") { (key, value) -> // Constrói string de parâmetros HTTP da query
                "${encode(key)}=${encode(value)}" // Codifica chave e valor e junta com sinal de igual
            } // Fim de joinToString
            val url = URL("${supabaseUrl.trimEnd('/')}/rest/v1/$table?$queryString") // Cria URL final com os parâmetros da query
            val connection = (url.openConnection() as HttpURLConnection).apply { // Configura ligação HTTP
                requestMethod = "GET" // Define método como GET
                connectTimeout = 10_000 // Define timeout de ligação de 10 segundos
                readTimeout = 10_000 // Define timeout de leitura de 10 segundos
                setRequestProperty("apikey", anonKey) // Passa chave anónima no cabeçalho apikey
                setRequestProperty("Authorization", "Bearer $anonKey") // Passa chave no cabeçalho de Autorização
                setRequestProperty("Accept", "application/json") // Aceita resposta em formato JSON
            } // Fim do apply

            val code = connection.responseCode // Obtém código de resposta HTTP
            val body = if (code in 200..299) { // Se código for de sucesso
                connection.inputStream.bufferedReader().use { it.readText() } // Lê o conteúdo da stream de entrada
            } else { // Se código for de erro
                connection.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty() // Lê stream de erro ou devolve vazio
            } // Fim do if
            connection.disconnect() // Termina a ligação com o servidor

            if (code !in 200..299) { // Se ocorreu um erro
                error("Erro Supabase ($code): $body") // Lança exceção detalhando o código e corpo do erro
            } // Fim do if
            JSONArray(body) // Retorna a resposta convertida em JSONArray
        } // Fim de getArray

    private suspend fun postObject(table: String, bodyJson: JSONObject): JSONObject? = // Declaração de postObject para inserção de registo
        withContext(Dispatchers.IO) { // Executa em thread assíncrona de IO
            if (supabaseUrl.isBlank() || anonKey.isBlank()) { // Verifica configuração das chaves
                error("Configura SUPABASE_URL e SUPABASE_ANON_KEY no aplicacao/local.properties.") // Lança erro
            } // Fim do if

            val url = URL("${supabaseUrl.trimEnd('/')}/rest/v1/$table") // Cria URL do endpoint da tabela
            val connection = (url.openConnection() as HttpURLConnection).apply { // Configura ligação HTTP
                requestMethod = "POST" // Método POST para criar
                connectTimeout = 10_000 // Timeout de ligação de 10 segundos
                readTimeout = 10_000 // Timeout de leitura de 10 segundos
                setRequestProperty("apikey", anonKey) // Adiciona apikey no cabeçalho
                setRequestProperty("Authorization", "Bearer $anonKey") // Adiciona Bearer token no cabeçalho
                setRequestProperty("Content-Type", "application/json") // Tipo do payload é JSON
                setRequestProperty("Prefer", "return=representation") // Solicita retorno do objeto inserido
                doOutput = true // Permite escrita de dados no output stream
            } // Fim do apply

            connection.outputStream.bufferedWriter().use { it.write(bodyJson.toString()) } // Escreve o objeto JSON na stream de saída

            val code = connection.responseCode // Guarda código HTTP da resposta
            val responseBody = if (code in 200..299) { // Se inserido com sucesso
                connection.inputStream.bufferedReader().use { it.readText() } // Lê resposta do servidor
            } else { // Caso ocorra erro
                connection.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty() // Lê mensagem de erro
            } // Fim do if
            connection.disconnect() // Fecha a ligação HTTP

            if (code !in 200..299) { // Se a resposta for erro
                error("Erro Supabase ($code): $responseBody") // Lança exceção
            } // Fim do if

            val array = JSONArray(responseBody) // Converte corpo da resposta para JSONArray
            if (array.length() > 0) array.getJSONObject(0) else null // Devolve primeiro objeto ou nulo
        }

    private suspend fun patchObject(table: String, id: Int, bodyJson: JSONObject): JSONObject? = // Declaração de patchObject para atualizar registo por ID
        withContext(Dispatchers.IO) { // Executa no contexto IO
            if (supabaseUrl.isBlank() || anonKey.isBlank()) { // Valida chaves do Supabase
                error("Configura SUPABASE_URL e SUPABASE_ANON_KEY no aplicacao/local.properties.") // Lança erro de configuração
            } // Fim do if

            val url = URL("${supabaseUrl.trimEnd('/')}/rest/v1/$table?id=eq.$id") // Cria URL filtrado por ID correspondente
            val connection = (url.openConnection() as HttpURLConnection).apply { // Configura ligação HTTP
                requestMethod = "PATCH" // Define método PATCH para modificação parcial
                connectTimeout = 10_000 // Timeout de ligação de 10 segundos
                readTimeout = 10_000 // Timeout de leitura de 10 segundos
                setRequestProperty("apikey", anonKey) // Cabeçalho apikey
                setRequestProperty("Authorization", "Bearer $anonKey") // Cabeçalho Authorization
                setRequestProperty("Content-Type", "application/json") // Payload formatado como JSON
                setRequestProperty("Prefer", "return=representation") // Pede representação atualizada de volta
                doOutput = true // Habilita envio de dados
            } // Fim do apply

            connection.outputStream.bufferedWriter().use { it.write(bodyJson.toString()) } // Envia corpo JSON na ligação

            val code = connection.responseCode // Obtém código de resposta HTTP
            val responseBody = if (code in 200..299) { // Se com sucesso
                connection.inputStream.bufferedReader().use { it.write(bodyJson.toString()); it.readText() } // Lê resposta (nota: it.readText() após escrita)
            } else { // Se com erro
                connection.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty() // Lê erro
            } // Fim do if
            connection.disconnect() // Termina ligação HTTP

            if (code !in 200..299) { // Se correu mal
                error("Erro Supabase ($code): $responseBody") // Lança exceção
            } // Fim do if

            val array = JSONArray(responseBody) // Converte JSON de resposta em array
            if (array.length() > 0) array.getJSONObject(0) else null // Devolve primeiro objeto ou nulo
        } // Fim de patchObject

    private companion object { // Declaração do objeto companheiro
        const val BCRYPT_COST = 10 // Constante de custo de encriptação do BCrypt definida a 10
    } // Fim do companion object

    private fun JSONObject.toUtilizador(): Utilizador { // Mapeador de JSONObject para Utilizador
        return Utilizador( // Instancia e devolve o modelo Utilizador
            id = optInt("id"), // Extrai o ID numérico
            nome = optString("nome"), // Extrai o nome de utilizador
            email = optString("email"), // Extrai o e-mail
            tipo = optString("tipo").toTipoUtilizador(), // Converte e mapeia o tipo de utilizador
            active = optBoolean("active", true), // Extrai estado ativo ou assume verdadeiro
            equipas = 0, // Inicializa contador de equipas a zero
            torneios = 0, // Inicializa contador de torneios a zero
            jogos = 0 // Inicializa contador de jogos a zero
        ) // Fim de Utilizador
    } // Fim da extensão toUtilizador

    private fun JSONArray.toObjectList(): List<JSONObject> { // Extensão para converter JSONArray em Lista de JSONObject
        return List(length()) { index -> getJSONObject(index) } // Cria lista iterando e obtendo objetos pelo índice
    } // Fim da extensão toObjectList

    private fun inferirEstado(jogos: List<Jogo>): String { // Função para inferir estado do torneio com base nos jogos
        if (jogos.isEmpty()) return "Por Iniciar" // Se não há jogos agendados, o torneio está por iniciar
        if (jogos.all { it.estado == "Finalizado" }) return "Finalizado" // Se todos os jogos terminaram, está finalizado
        return "A Decorrer" // Caso contrário, o torneio está em andamento (a decorrer)
    } // Fim de inferirEstado

    private fun calcularSerieJogos(total: Int): List<Float> { // Calcula distribuição de jogos para gráficos estatísticos
        if (total <= 0) return List(7) { 0.05f } // Caso não haja jogos, devolve valores mínimos nos 7 dias
        return List(7) { index -> // Itera preenchendo as 7 posições
            ((index + 1).coerceAtMost(total) / total.toFloat()).coerceIn( // Normaliza valor proporcional
                0.1f, // Limite mínimo
                1f // Limite máximo
            ) // Fim do coerceIn
        } // Fim do List
    } // Fim de calcularSerieJogos

    private fun String.toTipoUtilizador(): TipoUtilizador { // Mapeia string para o enum de TipoUtilizador
        return TipoUtilizador.entries.firstOrNull { // Procura primeiro elemento correspondente
            it.name.equals(this, ignoreCase = true) || it.descricao.equals(this, ignoreCase = true) // Compara nome ou descrição
        } ?: TipoUtilizador.PARTICIPANTE // Fallback se não encontrar correspondência
    } // Fim de toTipoUtilizador

    private fun String.toEstadoLegivel(): String { // Mapeia o estado retornado pela BD para texto legível
        return when (uppercase()) { // Avalia string convertida para maiúsculas
            "FINALIZADO" -> "Finalizado" // Retorna texto formatado de finalizado
            "EM_CURSO" -> "A Decorrer" // Retorna texto formatado de em curso
            else -> "Agendado" // Fallback para agendado
        } // Fim do when
    } // Fim de toEstadoLegivel

    private fun encode(value: String): String = // Codifica string para formato de URL segura
        URLEncoder.encode(value, "UTF-8") // Codifica usando charset UTF-8

    override suspend fun guardarEstatisticasJogo( // Declaração de guardarEstatisticasJogo
        partidaId: Int, // Parâmetro: ID da partida
        estatisticas: List<EstatisticaJogo> // Parâmetro: Lista de estatísticas
    ): Boolean { // Tipo de retorno: Booleano
        return withContext(Dispatchers.IO) { // Executa no dispatcher de IO
            deleteWhere( // Elimina estatísticas anteriores desta partida no Supabase
                table = "estatistica_jogo", // Tabela estatistica_jogo
                filters = mapOf("partida_id" to "eq.$partidaId") // Filtro por ID da partida
            ) // Fim de deleteWhere

            estatisticas.forEach { estatistica -> // Itera sobre cada nova estatística fornecida
                val json = JSONObject().apply { // Cria o objeto JSON correspondente
                    put("partida_id", partidaId) // Define partida_id
                    put("tipo", estatistica.tipo) // Define tipo de estatística (Ex: Faltas, Remates)
                    put("equipa", estatistica.equipa) // Define a equipa (casa ou fora)
                    put("valor", estatistica.valor) // Define o valor numérico
                } // Fim de apply

                postObject( // Envia o novo registo para o Supabase
                    table = "estatistica_jogo", // Tabela de destino
                    bodyJson = json // Dados formatados
                ) // Fim de postObject
            } // Fim do forEach

            true // Retorna verdadeiro indicando que a gravação ocorreu com sucesso
        } // Fim de withContext
    } // Fim de guardarEstatisticasJogo

    override suspend fun obterEstatisticasJogo(partidaId: Int): List<EstatisticaJogo> { // Declaração da função obterEstatisticasJogo
        return withContext(Dispatchers.IO) { // Executa em thread IO
            runCatching { // Bloco resiliente para capturar erros
                val array = getArray( // Solicita os registos correspondentes do Supabase
                    "estatistica_jogo", // Tabela estatistica_jogo
                    mapOf("select" to "tipo,equipa,valor", "partida_id" to "eq.$partidaId") // Seleciona campos e filtra por partida
                ).toObjectList() // Converte JSONArray para lista
                array.map { json -> // Mapeia cada JSON retornado para EstatisticaJogo
                    EstatisticaJogo( // Instancia o modelo
                        tipo = json.optString("tipo"), // Tipo da estatística
                        equipa = json.optString("equipa"), // Equipa correspondente
                        valor = json.optInt("valor") // Valor numérico
                    ) // Fim de EstatisticaJogo
                } // Fim do map
            }.getOrElse { // Em caso de erro
                it.printStackTrace() // Imprime o stack trace da exceção no logcat
                emptyList() // Devolve uma lista vazia
            } // Fim do getOrElse
        } // Fim de withContext
    }

    override suspend fun obterEventosJogo(partidaId: Int): List<EventoJogo> { // Declaração da função obterEventosJogo
        return withContext(Dispatchers.IO) { // Altera contexto para IO
            runCatching { // Envolve a consulta em tratamento de erros
                val array = getArray( // Obtém a lista de eventos associados à partida
                    "evento_jogo", // Tabela evento_jogo
                    mapOf("select" to "id,tipo,user_id,tempo,equipa", "match_id" to "eq.$partidaId") // Campos e filtro pelo ID da partida
                ).toObjectList() // Converte para lista
                if (array.isEmpty()) return@runCatching emptyList<EventoJogo>() // Aborda cedo se não existirem eventos

                val userIds = array.map { it.optInt("user_id") }.distinct().joinToString(",") // Extrai IDs únicos de jogadores envolvidos
                val usersMap = if (userIds.isNotEmpty()) { // Se houver jogadores identificados
                    getArray( // Procura os nomes correspondentes no Supabase
                        "utilizador", // Tabela utilizador
                        mapOf("select" to "id,nome", "id" to "in.($userIds)") // Filtra pelos IDs recolhidos
                    ).toObjectList().associate { it.optInt("id") to it.optString("nome") } // Cria mapa associativo ID -> Nome
                } else { // Caso contrário
                    emptyMap() // Retorna mapa vazio
                } // Fim do if

                val partidaJson = getArray( // Carrega informações adicionais sobre a partida
                    "partida", // Tabela partida
                    mapOf("select" to "team_a_id,team_b_id", "id" to "eq.$partidaId", "limit" to "1") // ID da equipa casa e fora
                ).optJSONObject(0) // Pega na primeira linha retornada

                val teamAId = partidaJson?.optInt("team_a_id") ?: -1 // ID da equipa casa
                val teamBId = partidaJson?.optInt("team_b_id") ?: -1 // ID da equipa fora

                val membersMap = if (userIds.isNotEmpty()) { // Se houver jogadores nos eventos
                    getArray( // Obtém a equipa correspondente a cada um deles
                        "team_member", // Tabela team_member
                        mapOf("select" to "user_id,team_id", "user_id" to "in.($userIds)") // IDs envolvidos
                    ).toObjectList().associate { it.optInt("user_id") to it.optInt("team_id") } // Mapa de Jogador -> Equipa
                } else { // Caso contrário
                    emptyMap() // Mapa vazio
                } // Fim do if

                array.map { json -> // Mapeia os eventos JSON obtidos para o modelo de dados EventoJogo
                    val id = json.optInt("id") // ID do evento
                    val tipo = json.optString("tipo") // Tipo do evento (Ex: GOLO, FALTA)
                    val userId = json.optInt("user_id") // ID do jogador interveniente
                    val tempo = json.optInt("tempo") // Minuto em que ocorreu
                    val userName = usersMap[userId] ?: "Jogador $userId" // Nome do jogador
                    val userTeamId = membersMap[userId] ?: -1 // ID da equipa do jogador
                    val equipaGuardada = json.optString("equipa") // Equipa guardada explicitamente no evento

                    val equipa = when { // Resolve a qual lado (casa/fora) associar o evento
                        equipaGuardada.equals("casa", ignoreCase = true) -> "casa" // Se explicitamente casa
                        equipaGuardada.equals("fora", ignoreCase = true) -> "fora" // Se explicitamente fora
                        userTeamId == teamAId -> "casa" // Se a equipa do jogador é a equipa da casa
                        userTeamId == teamBId -> "fora" // Se a equipa do jogador é a equipa de fora
                        else -> "center" // Fallback para neutro/desconhecido
                    } // Fim do when
                    EventoJogo( // Instancia o EventoJogo
                        id = id, // ID do evento
                        matchId = partidaId, // ID da partida
                        tipo = tipo, // Tipo do evento
                        userId = userId, // ID do jogador
                        userName = userName, // Nome do jogador
                        tempo = tempo, // Minuto
                        equipa = equipa // Lado da equipa (casa/fora/center)
                    ) // Fim de EventoJogo
                }.sortedBy { it.tempo } // Ordena os eventos cronologicamente pelo tempo de jogo
            }.getOrElse { // Se ocorreu um erro
                it.printStackTrace() // Log do erro
                emptyList() // Devolve lista vazia
            } // Fim do getOrElse
        } // Fim do withContext
    }

    override suspend fun registarEventoJogo( // Declaração de registarEventoJogo
        partidaId: Int, // Parâmetro: ID da partida
        tipo: String, // Parâmetro: Tipo (GOLO, FALTA, etc.)
        equipa: String, // Parâmetro: Lado da equipa (casa/fora)
        tempo: Int, // Parâmetro: Minuto
        userId: Int?, // Parâmetro: ID do jogador associado (opcional)
        jogadorSaiId: Int?, // Parâmetro: Jogador substituído (opcional)
        jogadorEntraId: Int? // Parâmetro: Jogador suplente que entra (opcional)
    ): Boolean { // Tipo de retorno: Booleano
        return withContext(Dispatchers.IO) { // Executa em thread assíncrona IO
            runCatching { // Bloco resiliente
                val partidaJson = getArray( // Obtém a partida para saber pontuação atual e ID das equipas
                    "partida", // Tabela partida
                    mapOf("select" to "team_a_id,team_b_id,resultado_a,resultado_b", "id" to "eq.$partidaId", "limit" to "1") // Filtro e seleção
                ).optJSONObject(0) // Primeiro objeto

                val teamAId = partidaJson?.optInt("team_a_id") ?: -1 // ID da equipa casa
                val teamBId = partidaJson?.optInt("team_b_id") ?: -1 // ID da equipa fora

                val json = JSONObject().apply { // Cria JSON do evento
                    put("match_id", partidaId) // Define partida
                    put("tipo", tipo.uppercase()) // Guarda tipo em maiúsculas
                    put("equipa", equipa) // Guarda equipa
                    put("tempo", tempo) // Guarda minuto

                    if (userId != null) { // Se jogador associado não for nulo
                        put("user_id", userId) // Associa ID do jogador
                    } // Fim do if
                    if (jogadorSaiId != null) { // Se for substituição e jogador que sai não for nulo
                        put("jogador_sai_id", jogadorSaiId) // Associa ID
                    } // Fim do if

                    if (jogadorEntraId != null) { // Se jogador que entra não for nulo
                        put("jogador_entra_id", jogadorEntraId) // Associa ID
                    } // Fim do if
                } // Fim do apply

                postObject( // Envia o evento para inserção no Supabase
                    table = "evento_jogo", // Tabela evento_jogo
                    bodyJson = json // Corpo JSON
                ) // Fim de postObject

                val tipoUpper = tipo.uppercase() // Tipo em maiúsculas para comparação simples
                val incremento = when (tipoUpper) { // Decide o valor do incremento no marcador
                    "GOLO", "ACE", "LANCE_LIVRE" -> 1 // Soma 1 ponto
                    "DOIS_PONTOS" -> 2 // Soma 2 pontos
                    "TRES_PONTOS" -> 3 // Soma 3 pontos
                    else -> 0 // Não pontua
                } // Fim do when

                if (incremento > 0) { // Se o evento gerou pontuação
                    var resA = partidaJson?.optInt("resultado_a") ?: 0 // Pontuação atual da casa
                    var resB = partidaJson?.optInt("resultado_b") ?: 0 // Pontuação atual de fora

                    if (equipa == "casa") { // Se foi marcado pela equipa casa
                        resA += incremento // Adiciona ao marcador da casa
                    } else { // Se foi da equipa visitante
                        resB += incremento // Adiciona ao marcador de fora
                    } // Fim do if-else

                    val updateJson = JSONObject().apply { // Cria JSON de atualização da partida
                        put("resultado_a", resA) // Novo resultado casa
                        put("resultado_b", resB) // Novo resultado fora
                    } // Fim do apply

                    patchObject("partida", partidaId, updateJson) // Envia PATCH para atualizar pontuação da partida no Supabase
                } // Fim do if de incremento

                true // Retorna sucesso
            }.getOrElse { // Em caso de exceção
                it.printStackTrace() // Mostra erro
                false // Retorna falha
            } // Fim do getOrElse
        } // Fim do withContext
    }
    private suspend fun patchObjectByColumn( // Declaração da função patchObjectByColumn
        table: String, // Parâmetro: Nome da tabela
        column: String, // Parâmetro: Nome da coluna de filtragem
        value: Int, // Parâmetro: Valor numérico da coluna
        bodyJson: JSONObject // Parâmetro: Corpo JSON para atualização
    ): JSONObject? = // Tipo de retorno opcional
        withContext(Dispatchers.IO) { // Executa no dispatcher de IO
            if (supabaseUrl.isBlank() || anonKey.isBlank()) { // Verifica se as chaves do Supabase estão em branco
                error("Configura SUPABASE_URL e SUPABASE_ANON_KEY no aplicacao/local.properties.") // Lança erro de configuração
            } // Fim do if

            val url = URL("${supabaseUrl.trimEnd('/')}/rest/v1/$table?$column=eq.$value") // Cria URL filtrado pelo valor da coluna fornecida
            val connection = (url.openConnection() as HttpURLConnection).apply { // Configura ligação HTTP
                requestMethod = "PATCH" // Define método como PATCH
                connectTimeout = 10_000 // Define timeout de ligação de 10 segundos
                readTimeout = 10_000 // Define timeout de leitura de 10 segundos
                setRequestProperty("apikey", anonKey) // Passa chave anónima no cabeçalho apikey
                setRequestProperty("Authorization", "Bearer $anonKey") // Passa chave no cabeçalho de Autorização
                setRequestProperty("Content-Type", "application/json") // Tipo do payload é JSON
                setRequestProperty("Prefer", "return=representation") // Solicita retorno do objeto atualizado
                doOutput = true // Habilita escrita no output stream
            } // Fim do apply

            connection.outputStream.bufferedWriter().use { // Abre stream de saída
                it.write(bodyJson.toString()) // Escreve o JSON
            } // Fim do use

            val code = connection.responseCode // Guarda código HTTP da resposta
            val responseBody = if (code in 200..299) { // Se correu bem
                connection.inputStream.bufferedReader().use { it.readText() } // Lê resposta do servidor
            } else { // Caso contrário
                connection.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty() // Lê erro
            } // Fim do if

            connection.disconnect() // Termina ligação HTTP

            if (code !in 200..299) { // Se a resposta for erro
                error("Erro Supabase ($code): $responseBody") // Lança exceção correspondente
            } // Fim do if

            val array = JSONArray(responseBody) // Converte corpo da resposta para JSONArray
            if (array.length() > 0) array.getJSONObject(0) else null // Devolve primeiro objeto ou nulo
        }

    override suspend fun listarNotificacoes(utilizadorId: Int): List<NotificacaoItem> { // Declaração de listarNotificacoes
        return withContext(Dispatchers.IO) { // Executa em thread assíncrona IO
            runCatching { // Bloco resiliente
                getArray( // Obtém a lista de notificações da tabela no Supabase
                    "notificacao", // Tabela notificacao
                    mapOf( // Filtros
                        "select" to "id,user_id,mensagem,data,lida", // Seleciona campos
                        "user_id" to "eq.$utilizadorId", // Filtra por utilizador
                        "order" to "data.desc", // Ordena por data decrescente
                        "limit" to "50" // Limita a 50 registos
                    ) // Fim de query
                ).toObjectList().map { // Converte JSON para NotificacaoItem
                    NotificacaoItem( // Cria instância do modelo
                        id = it.optInt("id"), // ID da notificação
                        utilizadorId = it.optInt("user_id"), // ID do utilizador destinatário
                        mensagem = it.optString("mensagem"), // Mensagem
                        data = it.optString("data"), // Data de registo
                        lida = it.optBoolean("lida", false) // Estado de leitura
                    ) // Fim de NotificacaoItem
                } // Fim do map
            }.getOrElse { // Em caso de exceção
                it.printStackTrace() // Log do erro
                emptyList() // Devolve lista vazia
            } // Fim do getOrElse
        } // Fim do withContext
    } // Fim da função listarNotificacoes

    override suspend fun listarNotificacoesAdmin(): List<NotificacaoItem> { // Declaração de listarNotificacoesAdmin
        return withContext(Dispatchers.IO) { // Executa em thread assíncrona IO
            runCatching { // Bloco resiliente
                getArray( // Obtém a lista geral de notificações do Supabase
                    "notificacao", // Tabela notificacao
                    mapOf( // Filtros
                        "select" to "id,user_id,mensagem,data,lida", // Seleciona campos
                        "order" to "data.desc", // Ordena por data decrescente
                        "limit" to "100" // Limita a 100 registos mais recentes
                    ) // Fim de query
                ).toObjectList().map { // Converte JSON para NotificacaoItem
                    NotificacaoItem( // Cria instância do modelo
                        id = it.optInt("id"), // ID
                        utilizadorId = it.optInt("user_id"), // Utilizador
                        mensagem = it.optString("mensagem"), // Mensagem
                        data = it.optString("data"), // Data
                        lida = it.optBoolean("lida", false) // Estado de lida
                    ) // Fim de NotificacaoItem
                } // Fim do map
            }.getOrElse { // Em caso de exceção
                it.printStackTrace() // Log do erro
                emptyList() // Devolve lista vazia
            } // Fim do getOrElse
        } // Fim do withContext
    } // Fim de listarNotificacoesAdmin

    override suspend fun marcarTodasNotificacoesAdminLidas(): Boolean { // Declaração de marcarTodasNotificacoesAdminLidas
        return withContext(Dispatchers.IO) { // Executa em thread assíncrona IO
            runCatching { // Bloco resiliente
                val notificacoes = listarNotificacoesAdmin() // Obtém a lista de notificações de admin

                notificacoes // Itera sobre notificações
                    .filter { !it.lida } // Filtra as que ainda não foram lidas
                    .forEach { notificacao -> // Para cada notificação não lida
                        marcarNotificacaoLida(notificacao.id) // Atualiza para lida no servidor
                    } // Fim do forEach

                true // Retorna verdadeiro indicando conclusão
            }.getOrDefault(false) // Devolve falso em caso de erro
        } // Fim do withContext
    } // Fim de marcarTodasNotificacoesAdminLidas

    override suspend fun marcarNotificacaoLida(notificacaoId: Int): Boolean { // Declaração de marcarNotificacaoLida
        return withContext(Dispatchers.IO) { // Executa em thread assíncrona IO
            runCatching { // Bloco resiliente
                val json = JSONObject().apply { put("lida", true) } // Cria JSON definindo campo lida a verdadeiro
                patchObject("notificacao", notificacaoId, json) != null // Envia pedido PATCH para a tabela notificacao
            }.getOrDefault(false) // Retorna falso se falhar
        } // Fim do withContext
    } // Fim de marcarNotificacaoLida

    override suspend fun marcarTodasNotificacoesLidas(utilizadorId: Int): Boolean { // Declaração de marcarTodasNotificacoesLidas de utilizador
        return withContext(Dispatchers.IO) { // Executa em thread assíncrona IO
            runCatching { // Bloco resiliente
                val json = JSONObject().apply { put("lida", true) } // Cria JSON definindo campo lida a verdadeiro
                patchObjectByColumn( // Atualiza todos os registos correspondentes à coluna user_id
                    table = "notificacao", // Tabela notificacao
                    column = "user_id", // Coluna de filtro
                    value = utilizadorId, // ID do utilizador correspondente
                    bodyJson = json // Dados a atualizar
                ) // Fim de patchObjectByColumn
                true // Retorna verdadeiro
            }.getOrDefault(false) // Retorna falso se falhar
        } // Fim do withContext
    }

    override suspend fun contarAlteracoesPendentes(): Int = syncQueue?.pendingCount() ?: 0 // Retorna a quantidade de atualizações guardadas localmente na fila de sincronização pendente

    override suspend fun sincronizarPendentes(): Int { // Declaração de sincronizarPendentes
        val store = syncQueue ?: return 0 // Obtém a fila local ou aborta retornando zero se nula
        var sincronizados = 0 // Inicializa contador de itens sincronizados com sucesso
        val pendentes = store.pending() // Recolhe todas as atualizações de resultados pendentes
        for (p in pendentes) { // Itera sobre cada alteração pendente
            try { // Bloco de tratamento de erros para cada pedido individual
                val resultado = atualizarJogoInterno( // Tenta enviar a alteração para o Supabase
                    id = p.jogoId, // ID do jogo
                    resultadoCasa = p.resultadoCasa, // Pontos/golos casa
                    resultadoFora = p.resultadoFora, // Pontos/golos fora
                    estado = p.estado, // Estado da partida
                    local = null, data = null, hora = null, // Não altera dados de agendamento na sincronização de resultados
                    atualizarInicio = false // Não força alteração de timestamp de início
                ) // Fim de atualizarJogoInterno
                if (resultado != null) { // Se a inserção/atualização remota foi bem-sucedida
                    store.remove(p.jogoId) // Remove o item da fila local pendente
                    sincronizados++ // Incrementa contador
                } // Fim do if
            } catch (_: java.io.IOException) { // Captura falhas de rede se ainda estiver offline
                // ainda sem rede; deixa na fila
                break // Aborta a sincronização para os restantes itens da fila
            } // Fim do try-catch
        } // Fim do for
        return sincronizados // Retorna o total de jogos sincronizados com sucesso nesta tentativa
    } // Fim de sincronizarPendentes

    override suspend fun criarNotificacaoParaTodos(mensagem: String): Boolean { // Declaração de criarNotificacaoParaTodos
        return withContext(Dispatchers.IO) { // Executa em thread assíncrona de IO
            runCatching { // Bloco resiliente
                val users = getArray( // Procura os utilizadores elegíveis para receber alertas gerais
                    "utilizador", // Tabela utilizador
                    mapOf( // Filtros
                        "select" to "id,tipo", // Seleciona ID e tipo
                        "tipo" to "in.(ESPECTADOR,PARTICIPANTE,ORGANIZADOR)" // Apenas utilizadores não administradores
                    ) // Fim de query
                ).toObjectList() // Converte JSONArray em Lista

                for (u in users) { // Itera sobre cada utilizador encontrado
                    val body = JSONObject().apply { // Cria o JSON de notificação individual
                        put("user_id", u.optInt("id")) // ID do destinatário
                        put("mensagem", mensagem) // Mensagem do alerta
                        put("lida", false) // Define notificação como por ler
                    } // Fim do apply

                    postObject("notificacao", body) // Insere a notificação na BD para este utilizador
                } // Fim do for

                true // Retorna verdadeiro indicando que as notificações foram criadas
            }.getOrDefault(false) // Retorna falso em caso de exceção/falha
        } // Fim do withContext
    } // Fim de criarNotificacaoParaTodos

    override suspend fun obterConfiguracaoNotificacoes( // Declaração de obterConfiguracaoNotificacoes
        utilizadorId: Int // Parâmetro: ID do utilizador
    ): ConfiguracaoNotificacoes { // Tipo de retorno do modelo
        val resultado = getArray( // Consulta as configurações de notificação do utilizador no Supabase
            table = "configuracao_notificacoes", // Tabela configuracao_notificacoes
            query = mapOf( // Filtros
                "select" to "*", // Seleciona todos os campos da tabela
                "utilizador_id" to "eq.$utilizadorId", // Filtra pelo utilizador especificado
                "limit" to "1" // Limita a 1 registo
            ) // Fim de query
        ) // Fim de getArray

        val json = resultado.optJSONObject(0) // Extrai o primeiro objeto JSON ou nulo se vazio

        return if (json != null) { // Se as configurações já existirem no servidor
            json.toConfiguracaoNotificacoes() // Mapeia o JSON para o modelo ConfiguracaoNotificacoes
        } else { // Caso contrário (utilizador novo ou sem registo de configuração)
            val nova = ConfiguracaoNotificacoes(utilizadorId = utilizadorId) // Cria configuração local predefinida
            criarConfiguracaoNotificacoes(nova) // Cria o registo correspondente na BD remota
            nova // Devolve a configuração predefinida criada
        } // Fim do if-else
    }

    override suspend fun atualizarConfiguracaoNotificacoes( // Declaração de atualizarConfiguracaoNotificacoes
        configuracao: ConfiguracaoNotificacoes // Parâmetro: Modelo com novas configurações
    ): ConfiguracaoNotificacoes? { // Tipo de retorno opcional
        deleteWhere( // Elimina a configuração antiga na BD
            table = "configuracao_notificacoes", // Tabela configuracao_notificacoes
            filters = mapOf("utilizador_id" to "eq.${configuracao.utilizadorId}") // Filtro de utilizador
        ) // Fim de deleteWhere

        val body = JSONObject().apply { // Cria JSON com novos valores da configuração
            put("utilizador_id", configuracao.utilizadorId) // Define utilizador
            put("notificacoes_jogos", configuracao.notificacoesJogos) // Ativa/desativa notificações de jogos
            put("notificacoes_golos", configuracao.notificacoesGolos) // Ativa/desativa notificações de golos
            put("notificacoes_cartoes", configuracao.notificacoesCartoes) // Ativa/desativa notificações de cartões
            put("notificacoes_fim_partida", configuracao.notificacoesFimPartida) // Ativa/desativa notificações de fim de jogo
            put("som_notificacao", configuracao.somNotificacao) // Ativa/desativa som
            put("futebol", configuracao.futebol) // Ativa futebol
            put("tenis", configuracao.tenis) // Ativa ténis
            put("basquetebol", configuracao.basquetebol) // Ativa basquetebol
            put("andebol", configuracao.andebol) // Ativa andebol
        } // Fim de apply

        return postObject( // Insere as novas configurações na base de dados do Supabase
            table = "configuracao_notificacoes", // Tabela configuracao_notificacoes
            bodyJson = body // Dados do payload
        )?.toConfiguracaoNotificacoes() // Devolve o resultado mapeado de volta para o modelo
    } // Fim da função atualizarConfiguracaoNotificacoes

    private suspend fun criarConfiguracaoNotificacoes( // Função auxiliar interna para criar registo inicial
        configuracao: ConfiguracaoNotificacoes // Parâmetro: Configuração a registar
    ) { // Sem retorno
        val body = JSONObject().apply { // Cria JSON
            put("utilizador_id", configuracao.utilizadorId) // Define utilizador
            put("notificacoes_jogos", configuracao.notificacoesJogos) // Jogos
            put("notificacoes_golos", configuracao.notificacoesGolos) // Golos
            put("notificacoes_cartoes", configuracao.notificacoesCartoes) // Cartões
            put("notificacoes_fim_partida", configuracao.notificacoesFimPartida) // Fim de jogo
            put("som_notificacao", configuracao.somNotificacao) // Som
            put("futebol", configuracao.futebol) // Futebol
            put("tenis", configuracao.tenis) // Ténis
            put("basquetebol", configuracao.basquetebol) // Basquetebol
            put("andebol", configuracao.andebol) // Andebol
        } // Fim do apply

        postObject( // Insere na base de dados
            table = "configuracao_notificacoes", // Tabela configuracao_notificacoes
            bodyJson = body // Dados a inserir
        ) // Fim de postObject
    } // Fim da função criarConfiguracaoNotificacoes

    private fun JSONObject.toConfiguracaoNotificacoes(): ConfiguracaoNotificacoes { // Extensão para mapear JSON para o modelo ConfiguracaoNotificacoes
        return ConfiguracaoNotificacoes( // Cria e retorna instância
            utilizadorId = optInt("utilizador_id"), // Utilizador
            notificacoesJogos = optBoolean("notificacoes_jogos", true), // Estado do alerta de jogos
            notificacoesGolos = optBoolean("notificacoes_golos", true), // Estado do alerta de golos
            notificacoesCartoes = optBoolean("notificacoes_cartoes", false), // Estado do alerta de cartões
            notificacoesFimPartida = optBoolean("notificacoes_fim_partida", true), // Estado do alerta de fim de jogo
            somNotificacao = optBoolean("som_notificacao", true), // Estado do som
            futebol = optBoolean("futebol", true), // Futebol ativo
            tenis = optBoolean("tenis", false), // Ténis ativo
            basquetebol = optBoolean("basquetebol", true), // Basquetebol ativo
            andebol = optBoolean("andebol", false) // Andebol ativo
        ) // Fim do ConfiguracaoNotificacoes
    } // Fim do mapeamento toConfiguracaoNotificacoes

    override suspend fun criarNotificacaoParaOrganizadorDoTorneio( // Declaração de criarNotificacaoParaOrganizadorDoTorneio
        torneioId: Int, // Parâmetro: ID do torneio
        mensagem: String // Parâmetro: Mensagem
    ): Boolean { // Retorna se foi enviado com sucesso
        return withContext(Dispatchers.IO) { // Executa em thread IO
            runCatching { // Bloco resiliente
                val torneioJson = getArray( // Procura os detalhes do torneio
                    "torneio", // Tabela torneio
                    mapOf( // Filtros
                        "select" to "organizador_id", // Apenas organizador_id
                        "id" to "eq.$torneioId", // Filtro do ID do torneio
                        "limit" to "1" // Apenas 1 registo
                    ) // Fim do query
                ).optJSONObject(0) ?: return@runCatching false // Obtém objeto JSON ou aborta se nulo

                val organizadorId = torneioJson.optInt("organizador_id", 0) // Extrai o ID do organizador

                if (organizadorId <= 0) return@runCatching false // Se ID do organizador for inválido, aborta

                val body = JSONObject().apply { // Cria JSON de notificação direcionada
                    put("user_id", organizadorId) // Define destinatário como o organizador
                    put("mensagem", mensagem) // Mensagem
                    put("lida", false) // Marca como por ler
                } // Fim do apply

                postObject("notificacao", body) // Envia para o Supabase

                true // Retorna sucesso
            }.getOrDefault(false) // Retorna falso se falhar
        } // Fim do withContext
    } // Fim de criarNotificacaoParaOrganizadorDoTorneio

    override suspend fun obterEquipaDoParticipante(utilizadorId: Int): Equipa? { // Declaração de obterEquipaDoParticipante
        return withContext(Dispatchers.IO) { // Executa em thread IO
            runCatching { // Bloco resiliente
                val membro = getArray( // Procura a associação do utilizador na tabela team_member
                    "team_member", // Tabela team_member
                    mapOf( // Filtros
                        "select" to "team_id", // Seleciona apenas o ID da equipa
                        "user_id" to "eq.$utilizadorId", // Filtro do jogador
                        "limit" to "1" // Limita a 1 registo
                    ) // Fim de query
                ).optJSONObject(0) ?: return@runCatching null // Obtém o primeiro objeto JSON ou retorna nulo se não houver associação

                val equipaId = membro.optInt("team_id") // Extrai ID numérico da equipa

                val equipaJson = getArray( // Procura os dados da equipa na BD
                    "equipa", // Tabela equipa
                    mapOf( // Filtros
                        "select" to "id,nome,torneio_id", // Seleciona id, nome e ID do torneio
                        "id" to "eq.$equipaId", // Filtro do ID da equipa
                        "limit" to "1" // Limita a 1 registo
                    ) // Fim de query
                ).optJSONObject(0) ?: return@runCatching null // Obtém os dados da equipa ou nulo

                Equipa( // Retorna a instância do modelo Equipa preenchida
                    id = equipaJson.optInt("id"), // ID
                    nome = equipaJson.optString("nome"), // Nome da equipa
                    torneioId = equipaJson.optInt("torneio_id") // ID do torneio em que joga
                ) // Fim do Equipa
            }.getOrElse { // Em caso de erro
                it.printStackTrace() // Log do erro
                null // Devolve nulo
            } // Fim do getOrElse
        } // Fim do withContext
    } // Fim de obterEquipaDoParticipante

    override suspend fun listarJogadoresEquipa(equipaId: Int): List<Utilizador> { // Declaração de listarJogadoresEquipa
        return withContext(Dispatchers.IO) { // Executa em thread IO
            runCatching { // Bloco resiliente
                val membros = getArray( // Obtém a lista de membros associados a esta equipa
                    "team_member", // Tabela team_member
                    mapOf( // Filtros
                        "select" to "user_id", // Seleciona ID dos utilizadores
                        "team_id" to "eq.$equipaId" // Filtra pela equipa fornecida
                    ) // Fim de query
                ).toObjectList() // Converte JSONArray em lista de JSONs

                if (membros.isEmpty()) return@runCatching emptyList<Utilizador>() // Aborta cedo se equipa estiver vazia

                val ids = membros // Recolhe os IDs dos utilizadores
                    .map { it.optInt("user_id") } // Transforma para lista de inteiros
                    .distinct() // Remove duplicados por segurança
                    .joinToString(",") // Concatena por vírgulas para a cláusula IN

                getArray( // Obtém os dados de perfil de todos os utilizadores da equipa
                    "utilizador", // Tabela utilizador
                    mapOf( // Filtros
                        "select" to "id,nome,email,tipo", // Seleciona campos básicos
                        "id" to "in.($ids)" // Filtra pelos IDs obtidos
                    ) // Fim de query
                ).toObjectList().map { it.toUtilizador() } // Converte para o modelo Utilizador e devolve a lista
            }.getOrElse { // Em caso de erro
                it.printStackTrace() // Log do erro
                emptyList() // Devolve lista vazia
            } // Fim do getOrElse
        } // Fim do withContext
    } // Fim de listarJogadoresEquipa

    override suspend fun obterClassificacaoEquipa( // Declaração de obterClassificacaoEquipa
        equipaId: Int, // Parâmetro: ID da equipa
        torneioId: Int // Parâmetro: ID do torneio
    ): Classificacao? { // Tipo de retorno opcional
        return runCatching { // Bloco resiliente
            obterClassificacao(torneioId) // Obtém a tabela classificativa do torneio
                .firstOrNull { it.equipaId == equipaId } // Procura a classificação correspondente à equipa fornecida
        }.getOrElse { // Em caso de erro
            it.printStackTrace() // Log do erro
            null // Devolve nulo
        } // Fim de runCatching
    } // Fim de obterClassificacaoEquipa

    override suspend fun listarJogosDaEquipa(equipaId: Int): List<Jogo> { // Declaração de listarJogosDaEquipa
        return withContext(Dispatchers.IO) { // Executa em thread IO
            runCatching { // Bloco resiliente
                val partidas = getArray( // Procura os jogos em que a equipa joga (em casa ou fora)
                    "partida", // Tabela partida
                    mapOf( // Filtros
                        "select" to "id,torneio_id,team_a_id,team_b_id,resultado_a,resultado_b,estado,data_hora,local", // Seleciona campos de jogo
                        "or" to "(team_a_id.eq.$equipaId,team_b_id.eq.$equipaId)" // Filtro lógico OU (casa OU fora)
                    ) // Fim de query
                ).toObjectList() // Converte para lista

                if (partidas.isEmpty()) return@runCatching emptyList<Jogo>() // Retorna lista vazia se não houver jogos

                val teamIds = partidas // Recolhe IDs de todas as equipas com quem joga
                    .flatMap { listOf(it.optInt("team_a_id"), it.optInt("team_b_id")) } // Junta IDs da equipa casa e fora
                    .distinct() // Filtra IDs únicos
                    .joinToString(",") // Concatena por vírgula

                val equipasMap = getArray( // Procura o nome das equipas recolhidas
                    "equipa", // Tabela equipa
                    mapOf( // Filtros
                        "select" to "id,nome", // Seleciona ID e nome
                        "id" to "in.($teamIds)" // Filtra pelos IDs da lista
                    ) // Fim de query
                ).toObjectList().associate { // Associa num mapa chave-valor
                    it.optInt("id") to it.optString("nome") // Mapa de ID -> Nome da equipa
                } // Fim de associate

                partidas.map { json -> // Mapeia cada partida JSON obtida para o modelo Jogo
                    val teamAId = json.optInt("team_a_id") // ID da equipa casa
                    val teamBId = json.optInt("team_b_id") // ID da equipa fora
                    val dataHoraStr = json.optString("data_hora", "") // Data e hora do agendamento
                    val (dataVal, horaVal) = parseDataHora(dataHoraStr) // Formata em data e hora legíveis

                    Jogo( // Instancia o modelo Jogo
                        id = json.optInt("id"), // ID da partida
                        torneioId = json.optInt("torneio_id"), // ID do torneio
                        casa = equipasMap[teamAId] ?: "Equipa $teamAId", // Nome da equipa casa
                        fora = equipasMap[teamBId] ?: "Equipa $teamBId", // Nome da equipa fora
                        equipaCasaId = teamAId, // ID casa
                        equipaForaId = teamBId, // ID fora
                        resultadoCasa = json.optInt("resultado_a"), // Pontos casa
                        resultadoFora = json.optInt("resultado_b"), // Pontos fora
                        estado = json.optString("estado", "AGENDADO").toEstadoLegivel(), // Estado traduzido
                        data = dataVal, // Data formatada
                        hora = horaVal, // Hora formatada
                        local = json.optString("local", "A definir") // Local da partida
                    ) // Fim do Jogo
                } // Fim do map
            }.getOrElse { // Em caso de exceção
                it.printStackTrace() // Mostra o erro
                emptyList() // Devolve lista vazia
            } // Fim do getOrElse
        } // Fim do withContext
    }

    override suspend fun obterEstatisticasParticipante( // Declaração de obterEstatisticasParticipante
        utilizadorId: Int, // Parâmetro: ID do utilizador (jogador)
        equipaId: Int // Parâmetro: ID da equipa
    ): ParticipantStatsData { // Tipo de retorno: Modelo de dados de estatísticas do participante
        return withContext(Dispatchers.IO) { // Executa em thread IO
            runCatching { // Bloco resiliente
                val jogosDaEquipa = listarJogosDaEquipa(equipaId) // Lista todos os jogos associados a esta equipa
                val jogoIds = jogosDaEquipa.map { it.id } // Transforma em lista com os IDs das partidas

                if (jogoIds.isEmpty()) { // Se a equipa ainda não tiver jogos
                    return@runCatching ParticipantStatsData(jogos = 0) // Retorna estatísticas zeradas
                } // Fim do if

                val eventos = getArray( // Consulta todos os eventos gerados por este jogador nessas partidas
                    "evento_jogo", // Tabela evento_jogo
                    mapOf( // Filtros
                        "select" to "tipo,user_id,match_id", // Seleciona tipo, utilizador e partida
                        "user_id" to "eq.$utilizadorId", // Filtro do utilizador
                        "match_id" to "in.(${jogoIds.joinToString(",")})" // Cláusula IN para os IDs das partidas da equipa
                    ) // Fim de query
                ).toObjectList() // Converte para lista

                var golos = 0 // Inicializa contador de golos
                var faltas = 0 // Inicializa contador de faltas
                var cartoes = 0 // Inicializa contador de cartões
                for (e in eventos) { // Itera sobre cada evento gerado pelo jogador
                    when (e.optString("tipo").uppercase()) { // Avalia o tipo de evento em maiúsculas
                        "GOLO" -> golos++ // Incrementa golo
                        "FALTA" -> faltas++ // Incrementa falta
                        "CARTAO", "CARTAO_AMARELO", "CARTAO_VERMELHO", "AMARELO", "VERMELHO" -> cartoes++ // Incrementa cartões
                    } // Fim do when
                } // Fim do for

                ParticipantStatsData( // Retorna os dados agregados
                    jogos = jogosDaEquipa.size, // Total de jogos disputados pela equipa
                    golos = golos, // Total de golos do jogador
                    faltas = faltas, // Total de faltas do jogador
                    cartoes = cartoes // Total de cartões do jogador
                ) // Fim do ParticipantStatsData
            }.getOrElse { // Em caso de erro
                it.printStackTrace() // Log do erro
                ParticipantStatsData() // Devolve estatísticas zeradas por defeito
            } // Fim do getOrElse
        } // Fim do withContext
    } // Fim de obterEstatisticasParticipante

    override suspend fun removerJogadorEquipa(equipaId: Int, utilizadorId: Int): Boolean { // Declaração de removerJogadorEquipa
        return withContext(Dispatchers.IO) { // Executa em thread IO
            runCatching { // Bloco resiliente
                deleteWhere( // Remove a associação na tabela team_member
                    "team_member", // Tabela team_member
                    mapOf( // Filtros de eliminação
                        "team_id" to "eq.$equipaId", // ID da equipa
                        "user_id" to "eq.$utilizadorId" // ID do jogador
                    ) // Fim de query
                ) // Fim de deleteWhere
                true // Retorna verdadeiro indicando remoção com sucesso
            }.getOrElse { // Em caso de erro
                it.printStackTrace() // Log do erro
                false // Retorna falso
            } // Fim do getOrElse
        } // Fim do withContext
    }

    override suspend fun juntarEquipaPorCodigo( // Declaração de juntarEquipaPorCodigo
        utilizadorId: Int, // Parâmetro: ID do utilizador (participante)
        codigo: String // Parâmetro: Código convite da equipa
    ): Result<Equipa> = withContext(Dispatchers.IO) { // Executa em thread IO com retorno do tipo Result contendo Equipa
        runCatching { // Bloco resiliente que captura e encapsula erros em Result
            val teamId = TeamCode.decode(codigo) // Descodifica o código convite em ID numérico de equipa
                ?: throw IllegalArgumentException("Código inválido.") // Lança exceção se código for incorreto

            val equipaJson = getArray( // Consulta os dados correspondentes da equipa no Supabase
                "equipa", // Tabela equipa
                mapOf( // Filtros
                    "select" to "id,nome,torneio_id", // Seleciona id, nome e torneio
                    "id" to "eq.$teamId", // ID da equipa descodificado
                    "limit" to "1" // Limita a 1 registo
                ) // Fim de query
            ).optJSONObject(0) // Extrai o objeto JSON
                ?: throw IllegalArgumentException("Equipa não encontrada.") // Lança erro se equipa não existir na base de dados

            val torneioId = equipaJson.optInt("torneio_id") // Extrai ID do torneio da equipa

            val equipasAtuais = listarEquipasDoParticipante(utilizadorId) // Lista as equipas a que o utilizador já pertence

            val jaTemEquipaNesteTorneio = equipasAtuais.any { // Verifica se já joga noutra equipa no mesmo torneio
                it.torneioId == torneioId && it.id != teamId // Outra equipa com mesmo torneioId mas ID diferente
            } // Fim de jaTemEquipaNesteTorneio

            if (jaTemEquipaNesteTorneio) { // Caso o utilizador já represente outra equipa no torneio
                throw IllegalArgumentException("Já pertence a uma equipa neste torneio.") // Impede a inscrição múltipla
            } // Fim do if

            val jaMembro = getArray( // Verifica se já está inscrito especificamente nesta equipa
                "team_member", // Tabela team_member
                mapOf( // Filtros
                    "select" to "id", // ID do membro
                    "user_id" to "eq.$utilizadorId", // ID do utilizador
                    "team_id" to "eq.$teamId", // ID da equipa
                    "limit" to "1" // Limita a 1
                ) // Fim de query
            ).optJSONObject(0) // Obtém registo se existir

            if (jaMembro == null) { // Se o utilizador ainda não estiver associado à equipa
                postObject( // Cria a nova associação de membro da equipa no Supabase
                    "team_member", // Tabela team_member
                    JSONObject().apply { // Payload JSON
                        put("user_id", utilizadorId) // Associa o utilizador
                        put("team_id", teamId) // Associa a equipa
                    } // Fim do apply
                ) ?: throw IllegalStateException("Não foi possível entrar na equipa.") // Lança erro se a inserção falhar
            } // Fim do if

            Equipa( // Retorna os dados da equipa em que entrou com sucesso
                id = equipaJson.optInt("id"), // ID
                nome = equipaJson.optString("nome"), // Nome da equipa
                torneioId = torneioId // ID do torneio correspondente
            ) // Fim do Equipa
        } // Fim de runCatching
    } // Fim de juntarEquipaPorCodigo

    override suspend fun listarEquipasDoParticipante(utilizadorId: Int): List<Equipa> { // Declaração de listarEquipasDoParticipante
        return withContext(Dispatchers.IO) { // Executa em thread IO
            runCatching { // Bloco resiliente
                val membros = getArray( // Consulta as equipas onde o utilizador está inscrito
                    "team_member", // Tabela team_member
                    mapOf( // Filtros
                        "select" to "team_id", // Seleciona apenas o ID da equipa
                        "user_id" to "eq.$utilizadorId" // Filtra pelo utilizador
                    ) // Fim de query
                ).toObjectList() // Converte para lista

                if (membros.isEmpty()) return@runCatching emptyList<Equipa>() // Retorna lista vazia se não pertencer a equipas

                val equipaIds = membros // Recolhe os IDs de todas as equipas
                    .map { it.optInt("team_id") } // Transforma para lista de inteiros
                    .distinct() // Filtra apenas IDs únicos
                    .joinToString(",") // Concatena por vírgula

                getArray( // Consulta os dados completos dessas equipas no Supabase
                    "equipa", // Tabela equipa
                    mapOf( // Filtros
                        "select" to "id,nome,torneio_id", // Seleciona id, nome e ID do torneio
                        "id" to "in.($equipaIds)" // Cláusula IN com os IDs das equipas
                    ) // Fim de query
                ).toObjectList().map { json -> // Mapeia cada JSON retornado para o modelo Equipa
                    Equipa( // Instancia e retorna a Equipa
                        id = json.optInt("id"), // ID
                        nome = json.optString("nome"), // Nome
                        torneioId = json.optInt("torneio_id") // ID do torneio
                    ) // Fim do Equipa
                } // Fim do map
            }.getOrElse { // Em caso de erro
                it.printStackTrace() // Log do erro
                emptyList() // Devolve lista vazia
            } // Fim do getOrElse
        } // Fim do withContext
    } // Fim de listarEquipasDoParticipante

    override suspend fun desativarTorneio(id: Int): Boolean { // Declaração de desativarTorneio
        val json = JSONObject().apply { // Cria o JSON de atualização
            put("active", false) // Define o estado ativo como falso para ocultar o torneio
        } // Fim de apply

        patchObject("torneio", id, json) // Envia PATCH para o Supabase atualizar o estado do torneio

        return true // Retorna verdadeiro indicando que foi desativado
    } // Fim de desativarTorneio
} // Fim da classe SupabaseLeagueMatchRepository

private fun parseDataHora(dataHoraStr: String?): Pair<String, String> { // Função utilitária para converter data/hora ISO-8601 em par de strings legíveis
    if (dataHoraStr.isNullOrBlank()) return Pair("", "") // Se string for vazia ou nula, retorna par com strings vazias
    return try { // Bloco de segurança para conversão de strings
        val tIndex = dataHoraStr.indexOf('T') // Procura a posição do caracter 'T' separador de data e hora
        if (tIndex != -1) { // Se o caracter 'T' for encontrado
            val datePart = dataHoraStr.substring(0, tIndex) // Extrai a parte da data (Ex: 2026-06-14)
            val timePart = dataHoraStr.substring(tIndex + 1, minOf(tIndex + 6, dataHoraStr.length)) // Extrai a hora até aos minutos (Ex: 18:30)
            val dateSplit = datePart.split("-") // Decompõe a data pelos hífenes
            val dateFormatted = if (dateSplit.size == 3) "${dateSplit[2]}/${dateSplit[1]}/${dateSplit[0]}" else datePart // Reorganiza para formato dia/mês/ano (Ex: 14/06/2026)
            Pair(dateFormatted, timePart) // Retorna o par formatado
        } else { // Caso não encontre o caracter 'T'
            Pair(dataHoraStr, "") // Devolve a data sem alteração e hora vazia
        } // Fim do if-else
    } catch (e: Exception) { // Captura qualquer erro de processamento de strings
        Pair(dataHoraStr, "") // Retorna a string original e hora vazia em caso de falha
    } // Fim do try-catch
}