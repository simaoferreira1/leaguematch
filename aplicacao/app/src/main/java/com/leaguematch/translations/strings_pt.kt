package com.leaguematch.translations

object StringsPt : AppStrings {
    //Participante
    //ParticipantHomeScreen
    override val participantFallbackName = "Participante"
    override val participantGreetingPrefix = "Olá"
    override val participantHomeSubtitle = "Acompanha os teus torneios, jogos e estatísticas."

    override val participantAreaTitle = "Área do Participante"
    override val participantAreaDescription = "Tudo o que precisas para acompanhar a tua participação nos torneios."

    override val registeredTournaments = "Torneios inscritos"
    override val registeredTournamentsDescription = "Vê os torneios onde estás a participar."

    override val upcomingGames = "Próximos jogos"
    override val upcomingGamesDescription = "Consulta calendário, adversários e resultados."

    override val myTeam = "A minha equipa"
    override val myTeamDescription = "Vê informações da tua equipa e jogadores."

    override val statistics = "Estatísticas"
    override val statisticsDescription = "Consulta golos, jogos e desempenho."

    //ParticipantTournamentScreen
    override val tournamentsAssociated: (Int) -> String = { total ->
        "$total torneios associados à tua conta"
    }

    override val searchTournamentsPlaceholder = "Pesquisar torneios..."

    override val noRegisteredTournaments = "Ainda não estás inscrito em nenhum torneio."

    override val teamsLabel: (Int) -> String = { total ->
        "$total equipas"
    }

    //ParticipantTournamenteDetailsScreen
    override val tournamentDetailsTitle = "Detalhes do torneio"
    override val tournamentDetailsLoadError = "Não foi possível carregar os detalhes deste torneio."
    override val standingsTitle = "Classificação"
    override val noStandingsYet = "Ainda não existe classificação."
    override val matchesTitle = "Jogos"
    override val noMatchesYet = "Ainda não existem jogos neste torneio."
    override val topScorersTitle = "Melhores marcadores"
    override val noScorersYet = "Ainda não existem marcadores registados."

    override val goalsLabel: (Int) -> String = { total ->
        "$total golos"
    }

    override val pointsLabel: (Int) -> String = { total ->
        "$total pts"
    }

    override val classificationRecord: (Int, Int, Int) -> String = { vitorias, empates, derrotas ->
        "${vitorias}V ${empates}E ${derrotas}D"
    }

    //ParticipantGamesScreen
    override val myGamesTitle = "Os meus jogos"
    override val myGamesSubtitle = "Consulta próximos jogos, resultados e histórico."
    override val upcomingTab = "Próximos"
    override val resultsTab = "Resultados"
    override val historyTab = "Histórico"
    override val noUpcomingGames = "Ainda não existem próximos jogos associados à tua conta."
    override val noResultsYet = "Ainda não existem resultados disponíveis."
    override val noGameHistory = "Ainda não existe histórico de jogos."

    //ParticipantJointTeamScreen
    override val teamsTitle = "Equipas"
    override val joinTeamTitle = "Integrar Equipa"
    override val joinTeamDescription = "Introduz o código que recebeste do organizador da equipa."
    override val confirmAndJoin = "Confirmar e entrar"
    override val cancel = "Cancelar"


    //ParticipantTeamScreen
    override val myTeamTitle = "A minha equipa"
    override val myTeamSubtitle = "Consulta jogadores, classificação e últimos jogos."
    override val noTeamTitle = "Sem equipa associada"
    override val noTeamDescription = "Ainda não foste associado a uma equipa."
    override val joinTeamButton = "Integrar equipa"
    override val teamCode = "Código de equipa"
    override val teamName = "Nome da equipa"
    override val playersTitle = "Jogadores"
    override val playersCount: (Int) -> String = { total -> "$total jogadores na equipa" }
    override val teamStandingTitle = "Classificação"
    override val noStandingYet = "Ainda sem classificação"
    override val lastGamesTitle = "Últimos jogos"
    override val noRegisteredGames = "Ainda não existem jogos registados"
    override val gamesFound: (Int) -> String = { total -> "$total jogos encontrados" }
    override val myTeamsTitle = "As minhas equipas"
    override val selectedTeamTitle = "Equipa selecionada"

    override val selectedTeam = "Selecionada"
    override val viewDetails = "A ver detalhes"
    override val selectTeam = "Selecionar equipa"
    override val leaveTeam = "Sair da equipa"

    override val tournamentIdLabel = "Torneio ID"
    override val codeLabel = "Código"

    //ParticipantStatsScreen
    override val myStatsTitle = "As minhas estatísticas"
    override val myStatsSubtitle = "Resumo do teu desempenho."
    override val gamesStat = "Jogos"
    override val goalsStat = "Golos"
    override val assistsStat = "Faltas"
    override val mvpStat = "Cartões"
    override val overallPerformance = "Desempenho geral"
    override val overallPerformanceSubtitle = "Comparação rápida das tuas estatísticas."
    override val notEnoughStats = "Ainda não existem estatísticas suficientes para analisar o teu progresso."
    override val performanceTitle = "Rendimento"
    override val goalsPerGame = "Golos por jogo"
    override val assistsPerGame = "Faltas por jogo"
    override val statsStartMessage = "Participa em jogos para começares a ver o teu progresso."
    override val statsContinueMessage = "Continua a jogar para melhorares as tuas estatísticas."


    //
    override val spectatorHomeTitle = "Explorar"
    override val spectatorHomeSubtitle = "Acompanha torneios e jogos em direto"

    override val liveMatches = "Jogos em direto"
    override val trendingTournaments = "Torneios em destaque"

    override val tournamentDetails = "Detalhes do torneio"
    override val bestScorers = "Melhores marcadores"
    override val standings = "Classificação"
    override val teams = "Equipas"
    override val matches = "Jogos"

    override val classification = "Classificação"
    override val noClassificationAvailable = "Ainda não existe classificação."

    override val matchStatistics = "Estatísticas do Jogo"
    override val liveStatistics = "Estatísticas em direto"
    override val completeStatistics = "Ver Estatísticas Completas"

    override val timeline = "Cronologia"
    override val noEventsRegistered = "Nenhum evento registado neste jogo."

    override val live = "Em direto"
    override val finished = "Terminado"
    override val scheduled = "Agendado"

    override val homeTeam = "Casa"
    override val awayTeam = "Fora"

    override val possession = "Posse de Bola"
    override val shots = "Remates"
    override val shotsOnTarget = "Remates à Baliza"
    override val corners = "Cantos"
    override val fouls = "Faltas"
    override val yellowCards = "Cartões Amarelos"
    override val redCards = "Cartões Vermelhos"

    override val noStatisticsAvailable =
        "Ainda não existem estatísticas registadas para este jogo."

    override val notifications = "Notificações"
    override val notificationSettings = "Configuração de notificações"

    override val gameDetails = "Detalhes do Jogo"
    override val matchResult = "Resultado"

    //ClassificacaoScreen Spectator


    override val back = "Voltar"
    override val participantsTeams: (Int) -> String = { total -> "Participantes: $total equipas" }
    override val positionShort = "Pos"
    override val team = "Equipa"
    override val gamesShort = "J"
    override val winsShort = "V"
    override val drawsShort = "E"
    override val lossesShort = "D"
    override val goalDifferenceShort = "DG"
    override val pointsShort = "Pts"
    override val noTeamsYet = "Ainda não existem equipas."
}