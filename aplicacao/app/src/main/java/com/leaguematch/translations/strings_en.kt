/**
 * ESTUDAR PARA A APRESENTAÇÃO:
 * Ficheiro: strings_en.kt
 * Tipo: Traduções e Dicionário (Localização)
 *
 * Descrição:
 * Este ficheiro fornece suporte de multi-idioma (Localização) na app.\n * Mapeia chaves de texto dinâmicas para Strings traduzidas em Português ou Inglês.
 */
package com.leaguematch.translations // Define o pacote deste ficheiro de código

object StringsEn : AppStrings { // Declaração de objeto estático / Singleton
    //Participante
    //ParticipantHomeScreen
    override val participantFallbackName = "Participant" // Declara constante local (leitura única)
    override val participantGreetingPrefix = "Hello" // Declara constante local (leitura única)
    override val participantHomeSubtitle = "Follow your tournaments, games and statistics." // Declara constante local (leitura única)

    override val participantAreaTitle = "Participant Area" // Declara constante local (leitura única)
    override val participantAreaDescription = "Everything you need to follow your participation in tournaments." // Declara constante local (leitura única)

    override val registeredTournaments = "Registered tournaments" // Declara constante local (leitura única)
    override val registeredTournamentsDescription = "See the tournaments you are participating in." // Declara constante local (leitura única)

    override val upcomingGames = "Upcoming games" // Declara constante local (leitura única)
    override val upcomingGamesDescription = "Check schedule, opponents and results." // Declara constante local (leitura única)

    override val myTeam = "My team" // Declara constante local (leitura única)
    override val myTeamDescription = "View information about your team and players." // Declara constante local (leitura única)

    override val statistics = "Statistics" // Declara constante local (leitura única)
    override val statisticsDescription = "Check goals, games and performance." // Declara constante local (leitura única)

    //ParticipantTournamentScreen
    override val tournamentsAssociated: (Int) -> String = { total -> // Declara constante local (leitura única)
        "$total tournaments linked to your account"
    }

    override val searchTournamentsPlaceholder = "Search tournaments..." // Declara constante local (leitura única)

    override val noRegisteredTournaments = "You are not registered in any tournament yet." // Declara constante local (leitura única)

    override val teamsLabel: (Int) -> String = { total -> // Declara constante local (leitura única)
        "$total teams"
    }

    //ParticipantTournamenteDetailsScreen
    override val tournamentDetailsTitle = "Tournament details" // Declara constante local (leitura única)
    override val tournamentDetailsLoadError = "It was not possible to load this tournament details." // Declara constante local (leitura única)
    override val standingsTitle = "Standings" // Declara constante local (leitura única)
    override val noStandingsYet = "There are no standings yet." // Declara constante local (leitura única)
    override val matchesTitle = "Matches" // Declara constante local (leitura única)
    override val noMatchesYet = "There are no matches in this tournament yet." // Declara constante local (leitura única)
    override val topScorersTitle = "Top scorers" // Declara constante local (leitura única)
    override val noScorersYet = "There are no registered scorers yet." // Declara constante local (leitura única)

    override val goalsLabel: (Int) -> String = { total -> // Declara constante local (leitura única)
        "$total goals"
    }

    override val pointsLabel: (Int) -> String = { total -> // Declara constante local (leitura única)
        "$total pts"
    }

    override val classificationRecord: (Int, Int, Int) -> String = { wins, draws, losses -> // Declara constante local (leitura única)
        "${wins}W ${draws}D ${losses}L"
    }

    //ParticipantGamesScreen
    override val myGamesTitle = "My games" // Declara constante local (leitura única)
    override val myGamesSubtitle = "Check upcoming games, results and history." // Declara constante local (leitura única)
    override val upcomingTab = "Upcoming" // Declara constante local (leitura única)
    override val resultsTab = "Results" // Declara constante local (leitura única)
    override val historyTab = "History" // Declara constante local (leitura única)
    override val noUpcomingGames = "There are no upcoming games linked to your account yet." // Declara constante local (leitura única)
    override val noResultsYet = "There are no results available yet." // Declara constante local (leitura única)
    override val noGameHistory = "There is no game history yet." // Declara constante local (leitura única)

    //ParticipantJointTeamScreen
    override val teamsTitle = "Teams" // Declara constante local (leitura única)
    override val joinTeamTitle = "Join Team" // Declara constante local (leitura única)
    override val joinTeamDescription = "Enter the code you received from the team organizer." // Declara constante local (leitura única)
    override val confirmAndJoin = "Confirm and join" // Declara constante local (leitura única)
    override val cancel = "Cancel" // Declara constante local (leitura única)

    //ParticipantTeamScreen
    override val myTeamTitle = "My team" // Declara constante local (leitura única)
    override val myTeamSubtitle = "Check players, standings and recent games." // Declara constante local (leitura única)
    override val noTeamTitle = "No team linked" // Declara constante local (leitura única)
    override val noTeamDescription = "You have not been linked to a team yet." // Declara constante local (leitura única)
    override val joinTeamButton = "Join team" // Declara constante local (leitura única)
    override val teamCode = "Team code" // Declara constante local (leitura única)
    override val teamName = "Team name" // Declara constante local (leitura única)
    override val playersTitle = "Players" // Declara constante local (leitura única)
    override val playersCount: (Int) -> String = { total -> "$total players in the team" } // Declara constante local (leitura única)
    override val teamStandingTitle = "Standings" // Declara constante local (leitura única)
    override val noStandingYet = "No standings yet" // Declara constante local (leitura única)
    override val lastGamesTitle = "Recent games" // Declara constante local (leitura única)
    override val noRegisteredGames = "There are no registered games yet" // Declara constante local (leitura única)
    override val gamesFound: (Int) -> String = { total -> "$total games found" } // Declara constante local (leitura única)

    override val myTeamsTitle = "My teams" // Declara constante local (leitura única)
    override val selectedTeamTitle = "Selected team" // Declara constante local (leitura única)

    override val selectedTeam = "Selected" // Declara constante local (leitura única)
    override val viewDetails = "Seeing details" // Declara constante local (leitura única)
    override val selectTeam = "Select team" // Declara constante local (leitura única)
    override val leaveTeam = "Leave team" // Declara constante local (leitura única)

    override val tournamentIdLabel = "Tournament ID" // Declara constante local (leitura única)
    override val codeLabel = "Code" // Declara constante local (leitura única)

    //ParticipantStatsScreen
    override val myStatsTitle = "My statistics" // Declara constante local (leitura única)
    override val myStatsSubtitle = "Summary of your performance." // Declara constante local (leitura única)
    override val gamesStat = "Games" // Declara constante local (leitura única)
    override val goalsStat = "Goals" // Declara constante local (leitura única)
    override val assistsStat = "Fouls" // Declara constante local (leitura única)
    override val mvpStat = "Cards" // Declara constante local (leitura única)
    override val overallPerformance = "Overall performance" // Declara constante local (leitura única)
    override val overallPerformanceSubtitle = "Quick comparison of your statistics." // Declara constante local (leitura única)
    override val notEnoughStats = "There are not enough statistics yet to analyze your progress." // Declara constante local (leitura única)
    override val performanceTitle = "Performance" // Declara constante local (leitura única)
    override val goalsPerGame = "Goals per game" // Declara constante local (leitura única)
    override val assistsPerGame = "Fouls per game" // Declara constante local (leitura única)
    override val statsStartMessage = "Play matches to start seeing your progress." // Declara constante local (leitura única)
    override val statsContinueMessage = "Keep playing to improve your statistics." // Declara constante local (leitura única)

    //
    override val spectatorHomeTitle = "Explore" // Declara constante local (leitura única)
    override val spectatorHomeSubtitle = "Follow tournaments and live matches" // Declara constante local (leitura única)

    override val liveMatches = "Live Matches" // Declara constante local (leitura única)
    override val trendingTournaments = "Trending Tournaments" // Declara constante local (leitura única)

    override val tournamentDetails = "Tournament Details" // Declara constante local (leitura única)
    override val bestScorers = "Top Scorers" // Declara constante local (leitura única)
    override val standings = "Standings" // Declara constante local (leitura única)
    override val teams = "Teams" // Declara constante local (leitura única)
    override val matches = "Matches" // Declara constante local (leitura única)


    override val matchStatistics = "Match Statistics" // Declara constante local (leitura única)
    override val liveStatistics = "Live Statistics" // Declara constante local (leitura única)
    override val completeStatistics = "View Full Statistics" // Declara constante local (leitura única)

    override val timeline = "Timeline" // Declara constante local (leitura única)
    override val noEventsRegistered = "No events recorded for this match." // Declara constante local (leitura única)

    override val live = "Live" // Declara constante local (leitura única)
    override val finished = "Finished" // Declara constante local (leitura única)
    override val scheduled = "Scheduled" // Declara constante local (leitura única)

    override val homeTeam = "Home" // Declara constante local (leitura única)
    override val awayTeam = "Away" // Declara constante local (leitura única)

    override val possession = "Possession" // Declara constante local (leitura única)
    override val shots = "Shots" // Declara constante local (leitura única)
    override val shotsOnTarget = "Shots on Target" // Declara constante local (leitura única)
    override val corners = "Corners" // Declara constante local (leitura única)
    override val fouls = "Fouls" // Declara constante local (leitura única)
    override val yellowCards = "Yellow Cards" // Declara constante local (leitura única)
    override val redCards = "Red Cards" // Declara constante local (leitura única)

    override val noStatisticsAvailable = // Declara constante local (leitura única)
        "No statistics have been recorded for this match yet."

    override val notifications = "Notifications" // Declara constante local (leitura única)
    override val notificationSettings = "Notification Settings" // Declara constante local (leitura única)

    override val gameDetails = "Match Details" // Declara constante local (leitura única)
    override val matchResult = "Result" // Declara constante local (leitura única)

    //ClassificacaoScreen Spectator

    override val classification = "Standings" // Declara constante local (leitura única)
    override val noClassificationAvailable = "No standings data available yet." // Declara constante local (leitura única)
    override val back = "Back" // Declara constante local (leitura única)
    override val participantsTeams: (Int) -> String = { total -> "Participants: $total teams" } // Declara constante local (leitura única)
    override val positionShort = "Pos" // Declara constante local (leitura única)
    override val team = "Team" // Declara constante local (leitura única)
    override val gamesShort = "P" // Declara constante local (leitura única)
    override val winsShort = "W" // Declara constante local (leitura única)
    override val drawsShort = "D" // Declara constante local (leitura única)
    override val lossesShort = "L" // Declara constante local (leitura única)
    override val goalDifferenceShort = "GD" // Declara constante local (leitura única)
    override val pointsShort = "Pts" // Declara constante local (leitura única)
    override val noTeamsYet = "There are no teams yet." // Declara constante local (leitura única)
}