/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: strings_pt.kt
 * Tipo: Traduções e Dicionário (Localização)
 *
 * Descrição:
 * Este ficheiro fornece suporte de multi-idioma (Localização) na app.\n * Mapeia chaves de texto dinâmicas para Strings traduzidas em Português ou Inglês.
 */
package com.leaguematch.translations // Define o pacote deste ficheiro de código

object StringsPt : AppStrings { // Declaração de objeto estático / Singleton
    //Participante
    //ParticipantHomeScreen
    override val participantFallbackName = "Participante" // Declara constante local (leitura única)
    override val participantGreetingPrefix = "Olá" // Declara constante local (leitura única)
    override val participantHomeSubtitle = "Acompanha os teus torneios, jogos e estatísticas." // Declara constante local (leitura única)

    override val participantAreaTitle = "Área do Participante" // Declara constante local (leitura única)
    override val participantAreaDescription = "Tudo o que precisas para acompanhar a tua participação nos torneios." // Declara constante local (leitura única)

    override val registeredTournaments = "Torneios inscritos" // Declara constante local (leitura única)
    override val registeredTournamentsDescription = "Vê os torneios onde estás a participar." // Declara constante local (leitura única)

    override val upcomingGames = "Próximos jogos" // Declara constante local (leitura única)
    override val upcomingGamesDescription = "Consulta calendário, adversários e resultados." // Declara constante local (leitura única)

    override val myTeam = "A minha equipa" // Declara constante local (leitura única)
    override val myTeamDescription = "Vê informações da tua equipa e jogadores." // Declara constante local (leitura única)

    override val statistics = "Estatísticas" // Declara constante local (leitura única)
    override val statisticsDescription = "Consulta golos, jogos e desempenho." // Declara constante local (leitura única)

    //ParticipantTournamentScreen
    override val tournamentsAssociated: (Int) -> String = { total -> // Declara constante local (leitura única)
        "$total torneios associados à tua conta"
    }

    override val searchTournamentsPlaceholder = "Pesquisar torneios..." // Declara constante local (leitura única)

    override val noRegisteredTournaments = "Ainda não estás inscrito em nenhum torneio." // Declara constante local (leitura única)

    override val teamsLabel: (Int) -> String = { total -> // Declara constante local (leitura única)
        "$total equipas"
    }

    //ParticipantTournamenteDetailsScreen
    override val tournamentDetailsTitle = "Detalhes do torneio" // Declara constante local (leitura única)
    override val tournamentDetailsLoadError = "Não foi possível carregar os detalhes deste torneio." // Declara constante local (leitura única)
    override val standingsTitle = "Classificação" // Declara constante local (leitura única)
    override val noStandingsYet = "Ainda não existe classificação." // Declara constante local (leitura única)
    override val matchesTitle = "Jogos" // Declara constante local (leitura única)
    override val noMatchesYet = "Ainda não existem jogos neste torneio." // Declara constante local (leitura única)
    override val topScorersTitle = "Melhores marcadores" // Declara constante local (leitura única)
    override val noScorersYet = "Ainda não existem marcadores registados." // Declara constante local (leitura única)

    override val goalsLabel: (Int) -> String = { total -> // Declara constante local (leitura única)
        "$total golos"
    }

    override val pointsLabel: (Int) -> String = { total -> // Declara constante local (leitura única)
        "$total pts"
    }

    override val classificationRecord: (Int, Int, Int) -> String = { vitorias, empates, derrotas -> // Declara constante local (leitura única)
        "${vitorias}V ${empates}E ${derrotas}D"
    }

    //ParticipantGamesScreen
    override val myGamesTitle = "Os meus jogos" // Declara constante local (leitura única)
    override val myGamesSubtitle = "Consulta próximos jogos, resultados e histórico." // Declara constante local (leitura única)
    override val upcomingTab = "Próximos" // Declara constante local (leitura única)
    override val resultsTab = "Resultados" // Declara constante local (leitura única)
    override val historyTab = "Histórico" // Declara constante local (leitura única)
    override val noUpcomingGames = "Ainda não existem próximos jogos associados à tua conta." // Declara constante local (leitura única)
    override val noResultsYet = "Ainda não existem resultados disponíveis." // Declara constante local (leitura única)
    override val noGameHistory = "Ainda não existe histórico de jogos." // Declara constante local (leitura única)

    //ParticipantJointTeamScreen
    override val teamsTitle = "Equipas" // Declara constante local (leitura única)
    override val joinTeamTitle = "Integrar Equipa" // Declara constante local (leitura única)
    override val joinTeamDescription = "Introduz o código que recebeste do organizador da equipa." // Declara constante local (leitura única)
    override val confirmAndJoin = "Confirmar e entrar" // Declara constante local (leitura única)
    override val cancel = "Cancelar" // Declara constante local (leitura única)


    //ParticipantTeamScreen
    override val myTeamTitle = "A minha equipa" // Declara constante local (leitura única)
    override val myTeamSubtitle = "Consulta jogadores, classificação e últimos jogos." // Declara constante local (leitura única)
    override val noTeamTitle = "Sem equipa associada" // Declara constante local (leitura única)
    override val noTeamDescription = "Ainda não foste associado a uma equipa." // Declara constante local (leitura única)
    override val joinTeamButton = "Integrar equipa" // Declara constante local (leitura única)
    override val teamCode = "Código de equipa" // Declara constante local (leitura única)
    override val teamName = "Nome da equipa" // Declara constante local (leitura única)
    override val playersTitle = "Jogadores" // Declara constante local (leitura única)
    override val playersCount: (Int) -> String = { total -> "$total jogadores na equipa" } // Declara constante local (leitura única)
    override val teamStandingTitle = "Classificação" // Declara constante local (leitura única)
    override val noStandingYet = "Ainda sem classificação" // Declara constante local (leitura única)
    override val lastGamesTitle = "Últimos jogos" // Declara constante local (leitura única)
    override val noRegisteredGames = "Ainda não existem jogos registados" // Declara constante local (leitura única)
    override val gamesFound: (Int) -> String = { total -> "$total jogos encontrados" } // Declara constante local (leitura única)
    override val myTeamsTitle = "As minhas equipas" // Declara constante local (leitura única)
    override val selectedTeamTitle = "Equipa selecionada" // Declara constante local (leitura única)

    override val selectedTeam = "Selecionada" // Declara constante local (leitura única)
    override val viewDetails = "A ver detalhes" // Declara constante local (leitura única)
    override val selectTeam = "Selecionar equipa" // Declara constante local (leitura única)
    override val leaveTeam = "Sair da equipa" // Declara constante local (leitura única)

    override val tournamentIdLabel = "Torneio ID" // Declara constante local (leitura única)
    override val codeLabel = "Código" // Declara constante local (leitura única)

    //ParticipantStatsScreen
    override val myStatsTitle = "As minhas estatísticas" // Declara constante local (leitura única)
    override val myStatsSubtitle = "Resumo do teu desempenho." // Declara constante local (leitura única)
    override val gamesStat = "Jogos" // Declara constante local (leitura única)
    override val goalsStat = "Golos" // Declara constante local (leitura única)
    override val assistsStat = "Faltas" // Declara constante local (leitura única)
    override val mvpStat = "Cartões" // Declara constante local (leitura única)
    override val overallPerformance = "Desempenho geral" // Declara constante local (leitura única)
    override val overallPerformanceSubtitle = "Comparação rápida das tuas estatísticas." // Declara constante local (leitura única)
    override val notEnoughStats = "Ainda não existem estatísticas suficientes para analisar o teu progresso." // Declara constante local (leitura única)
    override val performanceTitle = "Rendimento" // Declara constante local (leitura única)
    override val goalsPerGame = "Golos por jogo" // Declara constante local (leitura única)
    override val assistsPerGame = "Faltas por jogo" // Declara constante local (leitura única)
    override val statsStartMessage = "Participa em jogos para começares a ver o teu progresso." // Declara constante local (leitura única)
    override val statsContinueMessage = "Continua a jogar para melhorares as tuas estatísticas." // Declara constante local (leitura única)


    //
    override val spectatorHomeTitle = "Explorar" // Declara constante local (leitura única)
    override val spectatorHomeSubtitle = "Acompanha torneios e jogos em direto" // Declara constante local (leitura única)

    override val liveMatches = "Jogos em direto" // Declara constante local (leitura única)
    override val trendingTournaments = "Torneios em destaque" // Declara constante local (leitura única)

    override val tournamentDetails = "Detalhes do torneio" // Declara constante local (leitura única)
    override val bestScorers = "Melhores marcadores" // Declara constante local (leitura única)
    override val standings = "Classificação" // Declara constante local (leitura única)
    override val teams = "Equipas" // Declara constante local (leitura única)
    override val matches = "Jogos" // Declara constante local (leitura única)

    override val classification = "Classificação" // Declara constante local (leitura única)
    override val noClassificationAvailable = "Ainda não existe classificação." // Declara constante local (leitura única)

    override val matchStatistics = "Estatísticas do Jogo" // Declara constante local (leitura única)
    override val liveStatistics = "Estatísticas em direto" // Declara constante local (leitura única)
    override val completeStatistics = "Ver Estatísticas Completas" // Declara constante local (leitura única)

    override val timeline = "Cronologia" // Declara constante local (leitura única)
    override val noEventsRegistered = "Nenhum evento registado neste jogo." // Declara constante local (leitura única)

    override val live = "Em direto" // Declara constante local (leitura única)
    override val finished = "Terminado" // Declara constante local (leitura única)
    override val scheduled = "Agendado" // Declara constante local (leitura única)

    override val homeTeam = "Casa" // Declara constante local (leitura única)
    override val awayTeam = "Fora" // Declara constante local (leitura única)

    override val possession = "Posse de Bola" // Declara constante local (leitura única)
    override val shots = "Remates" // Declara constante local (leitura única)
    override val shotsOnTarget = "Remates à Baliza" // Declara constante local (leitura única)
    override val corners = "Cantos" // Declara constante local (leitura única)
    override val fouls = "Faltas" // Declara constante local (leitura única)
    override val yellowCards = "Cartões Amarelos" // Declara constante local (leitura única)
    override val redCards = "Cartões Vermelhos" // Declara constante local (leitura única)

    override val noStatisticsAvailable = // Declara constante local (leitura única)
        "Ainda não existem estatísticas registadas para este jogo."

    override val notifications = "Notificações" // Declara constante local (leitura única)
    override val notificationSettings = "Configuração de notificações" // Declara constante local (leitura única)

    override val gameDetails = "Detalhes do Jogo" // Declara constante local (leitura única)
    override val matchResult = "Resultado" // Declara constante local (leitura única)

    //ClassificacaoScreen Spectator


    override val back = "Voltar" // Declara constante local (leitura única)
    override val participantsTeams: (Int) -> String = { total -> "Participantes: $total equipas" } // Declara constante local (leitura única)
    override val positionShort = "Pos" // Declara constante local (leitura única)
    override val team = "Equipa" // Declara constante local (leitura única)
    override val gamesShort = "J" // Declara constante local (leitura única)
    override val winsShort = "V" // Declara constante local (leitura única)
    override val drawsShort = "E" // Declara constante local (leitura única)
    override val lossesShort = "D" // Declara constante local (leitura única)
    override val goalDifferenceShort = "DG" // Declara constante local (leitura única)
    override val pointsShort = "Pts" // Declara constante local (leitura única)
    override val noTeamsYet = "Ainda não existem equipas." // Declara constante local (leitura única)
}