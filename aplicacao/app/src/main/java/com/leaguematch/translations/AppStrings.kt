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
}