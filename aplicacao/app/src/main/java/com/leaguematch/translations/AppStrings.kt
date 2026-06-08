package com.leaguematch.translations

interface AppStrings {
    //Participante
    //ParticipantHomeScreen
    val participantFallbackName: String
    val participantGreetingPrefix: String
    val participantHomeSubtitle: String

    val participantAreaTitle: String
    val participantAreaDescription: String

    val registeredTournaments: String
    val registeredTournamentsDescription: String

    val upcomingGames: String
    val upcomingGamesDescription: String

    val myTeam: String
    val myTeamDescription: String

    val statistics: String
    val statisticsDescription: String

    //ParticipantTournamentScreen
    val tournamentsAssociated: (Int) -> String
    val searchTournamentsPlaceholder: String
    val noRegisteredTournaments: String
    val teamsLabel: (Int) -> String

    //ParticipantTournamenteDetailsScreen
    val tournamentDetailsTitle: String
    val tournamentDetailsLoadError: String
    val standingsTitle: String
    val noStandingsYet: String
    val matchesTitle: String
    val noMatchesYet: String
    val topScorersTitle: String
    val noScorersYet: String
    val goalsLabel: (Int) -> String
    val pointsLabel: (Int) -> String
    val classificationRecord: (Int, Int, Int) -> String

    //ParticipantGamesScreen
    val myGamesTitle: String
    val myGamesSubtitle: String
    val upcomingTab: String
    val resultsTab: String
    val historyTab: String
    val noUpcomingGames: String
    val noResultsYet: String
    val noGameHistory: String

    //ParticipantJointTeamScreen
    val teamsTitle: String
    val joinTeamTitle: String
    val joinTeamDescription: String
    val confirmAndJoin: String
    val cancel: String

    //ParticipantTeamScreen
    val myTeamTitle: String
    val myTeamSubtitle: String
    val noTeamTitle: String
    val noTeamDescription: String
    val joinTeamButton: String
    val teamCode: String
    val teamName: String
    val playersTitle: String
    val playersCount: (Int) -> String
    val teamStandingTitle: String
    val noStandingYet: String
    val lastGamesTitle: String
    val noRegisteredGames: String
    val gamesFound: (Int) -> String
    val myTeamsTitle: String
    val selectedTeamTitle: String

    val selectedTeam: String
    val viewDetails: String
    val selectTeam: String
    val leaveTeam: String

    val tournamentIdLabel: String
    val codeLabel: String

    //ParticipantStatsScreen
    val myStatsTitle: String
    val myStatsSubtitle: String
    val gamesStat: String
    val goalsStat: String
    val assistsStat: String
    val mvpStat: String
    val overallPerformance: String
    val overallPerformanceSubtitle: String
    val notEnoughStats: String
    val performanceTitle: String
    val goalsPerGame: String
    val assistsPerGame: String
    val statsStartMessage: String
    val statsContinueMessage: String

    // Spectator

    val spectatorHomeTitle: String
    val spectatorHomeSubtitle: String

    val liveMatches: String
    val trendingTournaments: String

    val tournamentDetails: String
    val bestScorers: String
    val standings: String
    val teams: String
    val matches: String

    val matchStatistics: String
    val liveStatistics: String
    val completeStatistics: String

    val timeline: String
    val noEventsRegistered: String

    val live: String
    val finished: String
    val scheduled: String

    val homeTeam: String
    val awayTeam: String

    val possession: String
    val shots: String
    val shotsOnTarget: String
    val corners: String
    val fouls: String
    val yellowCards: String
    val redCards: String

    val noStatisticsAvailable: String

    val notifications: String
    val notificationSettings: String

    val gameDetails: String
    val matchResult: String

    //ClassificacaoScreen Spectator

    val classification: String
    val noClassificationAvailable: String
    val back: String
    val participantsTeams: (Int) -> String
    val positionShort: String
    val team: String
    val gamesShort: String
    val winsShort: String
    val drawsShort: String
    val lossesShort: String
    val goalDifferenceShort: String
    val pointsShort: String
    val noTeamsYet: String
}