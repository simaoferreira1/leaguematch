/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: AppStrings.kt
 * Tipo: Traduções e Dicionário (Localização)
 *
 * Descrição:
 * Este ficheiro fornece suporte de multi-idioma (Localização) na app.\n * Mapeia chaves de texto dinâmicas para Strings traduzidas em Português ou Inglês.
 */
package com.leaguematch.translations // Define o pacote deste ficheiro de código

interface AppStrings { // Declaração de interface (contrato de métodos)
    //Participante
    //ParticipantHomeScreen
    val participantFallbackName: String // Declara constante local (leitura única)
    val participantGreetingPrefix: String // Declara constante local (leitura única)
    val participantHomeSubtitle: String // Declara constante local (leitura única)

    val participantAreaTitle: String // Declara constante local (leitura única)
    val participantAreaDescription: String // Declara constante local (leitura única)

    val registeredTournaments: String // Declara constante local (leitura única)
    val registeredTournamentsDescription: String // Declara constante local (leitura única)

    val upcomingGames: String // Declara constante local (leitura única)
    val upcomingGamesDescription: String // Declara constante local (leitura única)

    val myTeam: String // Declara constante local (leitura única)
    val myTeamDescription: String // Declara constante local (leitura única)

    val statistics: String // Declara constante local (leitura única)
    val statisticsDescription: String // Declara constante local (leitura única)

    //ParticipantTournamentScreen
    val tournamentsAssociated: (Int) -> String // Declara constante local (leitura única)
    val searchTournamentsPlaceholder: String // Declara constante local (leitura única)
    val noRegisteredTournaments: String // Declara constante local (leitura única)
    val teamsLabel: (Int) -> String // Declara constante local (leitura única)

    //ParticipantTournamenteDetailsScreen
    val tournamentDetailsTitle: String // Declara constante local (leitura única)
    val tournamentDetailsLoadError: String // Declara constante local (leitura única)
    val standingsTitle: String // Declara constante local (leitura única)
    val noStandingsYet: String // Declara constante local (leitura única)
    val matchesTitle: String // Declara constante local (leitura única)
    val noMatchesYet: String // Declara constante local (leitura única)
    val topScorersTitle: String // Declara constante local (leitura única)
    val noScorersYet: String // Declara constante local (leitura única)
    val goalsLabel: (Int) -> String // Declara constante local (leitura única)
    val pointsLabel: (Int) -> String // Declara constante local (leitura única)
    val classificationRecord: (Int, Int, Int) -> String // Declara constante local (leitura única)

    //ParticipantGamesScreen
    val myGamesTitle: String // Declara constante local (leitura única)
    val myGamesSubtitle: String // Declara constante local (leitura única)
    val upcomingTab: String // Declara constante local (leitura única)
    val resultsTab: String // Declara constante local (leitura única)
    val historyTab: String // Declara constante local (leitura única)
    val noUpcomingGames: String // Declara constante local (leitura única)
    val noResultsYet: String // Declara constante local (leitura única)
    val noGameHistory: String // Declara constante local (leitura única)

    //ParticipantJointTeamScreen
    val teamsTitle: String // Declara constante local (leitura única)
    val joinTeamTitle: String // Declara constante local (leitura única)
    val joinTeamDescription: String // Declara constante local (leitura única)
    val confirmAndJoin: String // Declara constante local (leitura única)
    val cancel: String // Declara constante local (leitura única)

    //ParticipantTeamScreen
    val myTeamTitle: String // Declara constante local (leitura única)
    val myTeamSubtitle: String // Declara constante local (leitura única)
    val noTeamTitle: String // Declara constante local (leitura única)
    val noTeamDescription: String // Declara constante local (leitura única)
    val joinTeamButton: String // Declara constante local (leitura única)
    val teamCode: String // Declara constante local (leitura única)
    val teamName: String // Declara constante local (leitura única)
    val playersTitle: String // Declara constante local (leitura única)
    val playersCount: (Int) -> String // Declara constante local (leitura única)
    val teamStandingTitle: String // Declara constante local (leitura única)
    val noStandingYet: String // Declara constante local (leitura única)
    val lastGamesTitle: String // Declara constante local (leitura única)
    val noRegisteredGames: String // Declara constante local (leitura única)
    val gamesFound: (Int) -> String // Declara constante local (leitura única)
    val myTeamsTitle: String // Declara constante local (leitura única)
    val selectedTeamTitle: String // Declara constante local (leitura única)

    val selectedTeam: String // Declara constante local (leitura única)
    val viewDetails: String // Declara constante local (leitura única)
    val selectTeam: String // Declara constante local (leitura única)
    val leaveTeam: String // Declara constante local (leitura única)

    val tournamentIdLabel: String // Declara constante local (leitura única)
    val codeLabel: String // Declara constante local (leitura única)

    //ParticipantStatsScreen
    val myStatsTitle: String // Declara constante local (leitura única)
    val myStatsSubtitle: String // Declara constante local (leitura única)
    val gamesStat: String // Declara constante local (leitura única)
    val goalsStat: String // Declara constante local (leitura única)
    val assistsStat: String // Declara constante local (leitura única)
    val mvpStat: String // Declara constante local (leitura única)
    val overallPerformance: String // Declara constante local (leitura única)
    val overallPerformanceSubtitle: String // Declara constante local (leitura única)
    val notEnoughStats: String // Declara constante local (leitura única)
    val performanceTitle: String // Declara constante local (leitura única)
    val goalsPerGame: String // Declara constante local (leitura única)
    val assistsPerGame: String // Declara constante local (leitura única)
    val statsStartMessage: String // Declara constante local (leitura única)
    val statsContinueMessage: String // Declara constante local (leitura única)

    // Spectator

    val spectatorHomeTitle: String // Declara constante local (leitura única)
    val spectatorHomeSubtitle: String // Declara constante local (leitura única)

    val liveMatches: String // Declara constante local (leitura única)
    val trendingTournaments: String // Declara constante local (leitura única)

    val tournamentDetails: String // Declara constante local (leitura única)
    val bestScorers: String // Declara constante local (leitura única)
    val standings: String // Declara constante local (leitura única)
    val teams: String // Declara constante local (leitura única)
    val matches: String // Declara constante local (leitura única)

    val matchStatistics: String // Declara constante local (leitura única)
    val liveStatistics: String // Declara constante local (leitura única)
    val completeStatistics: String // Declara constante local (leitura única)

    val timeline: String // Declara constante local (leitura única)
    val noEventsRegistered: String // Declara constante local (leitura única)

    val live: String // Declara constante local (leitura única)
    val finished: String // Declara constante local (leitura única)
    val scheduled: String // Declara constante local (leitura única)

    val homeTeam: String // Declara constante local (leitura única)
    val awayTeam: String // Declara constante local (leitura única)

    val possession: String // Declara constante local (leitura única)
    val shots: String // Declara constante local (leitura única)
    val shotsOnTarget: String // Declara constante local (leitura única)
    val corners: String // Declara constante local (leitura única)
    val fouls: String // Declara constante local (leitura única)
    val yellowCards: String // Declara constante local (leitura única)
    val redCards: String // Declara constante local (leitura única)

    val noStatisticsAvailable: String // Declara constante local (leitura única)

    val notifications: String // Declara constante local (leitura única)
    val notificationSettings: String // Declara constante local (leitura única)

    val gameDetails: String // Declara constante local (leitura única)
    val matchResult: String // Declara constante local (leitura única)

    //ClassificacaoScreen Spectator

    val classification: String // Declara constante local (leitura única)
    val noClassificationAvailable: String // Declara constante local (leitura única)
    val back: String // Declara constante local (leitura única)
    val participantsTeams: (Int) -> String // Declara constante local (leitura única)
    val positionShort: String // Declara constante local (leitura única)
    val team: String // Declara constante local (leitura única)
    val gamesShort: String // Declara constante local (leitura única)
    val winsShort: String // Declara constante local (leitura única)
    val drawsShort: String // Declara constante local (leitura única)
    val lossesShort: String // Declara constante local (leitura única)
    val goalDifferenceShort: String // Declara constante local (leitura única)
    val pointsShort: String // Declara constante local (leitura única)
    val noTeamsYet: String // Declara constante local (leitura única)
}