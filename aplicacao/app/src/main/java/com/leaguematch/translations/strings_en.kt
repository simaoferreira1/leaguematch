package com.leaguematch.translations

object StringsEn : AppStrings {
    //Participante
    //ParticipantHomeScreen
    override val participantFallbackName = "Participant"
    override val participantGreetingPrefix = "Hello"
    override val participantHomeSubtitle = "Follow your tournaments, games and statistics."

    override val participantAreaTitle = "Participant Area"
    override val participantAreaDescription = "Everything you need to follow your participation in tournaments."

    override val registeredTournaments = "Registered tournaments"
    override val registeredTournamentsDescription = "See the tournaments you are participating in."

    override val upcomingGames = "Upcoming games"
    override val upcomingGamesDescription = "Check schedule, opponents and results."

    override val myTeam = "My team"
    override val myTeamDescription = "View information about your team and players."

    override val statistics = "Statistics"
    override val statisticsDescription = "Check goals, games and performance."

    //ParticipantTournamentScreen
    override val tournamentsAssociated: (Int) -> String = { total ->
        "$total tournaments linked to your account"
    }

    override val searchTournamentsPlaceholder = "Search tournaments..."

    override val noRegisteredTournaments = "You are not registered in any tournament yet."

    override val teamsLabel: (Int) -> String = { total ->
        "$total teams"
    }

    //ParticipantTournamenteDetailsScreen
    override val tournamentDetailsTitle = "Tournament details"
    override val tournamentDetailsLoadError = "It was not possible to load this tournament details."
    override val standingsTitle = "Standings"
    override val noStandingsYet = "There are no standings yet."
    override val matchesTitle = "Matches"
    override val noMatchesYet = "There are no matches in this tournament yet."
    override val topScorersTitle = "Top scorers"
    override val noScorersYet = "There are no registered scorers yet."

    override val goalsLabel: (Int) -> String = { total ->
        "$total goals"
    }

    override val pointsLabel: (Int) -> String = { total ->
        "$total pts"
    }

    override val classificationRecord: (Int, Int, Int) -> String = { wins, draws, losses ->
        "${wins}W ${draws}D ${losses}L"
    }

    //ParticipantGamesScreen
    override val myGamesTitle = "My games"
    override val myGamesSubtitle = "Check upcoming games, results and history."
    override val upcomingTab = "Upcoming"
    override val resultsTab = "Results"
    override val historyTab = "History"
    override val noUpcomingGames = "There are no upcoming games linked to your account yet."
    override val noResultsYet = "There are no results available yet."
    override val noGameHistory = "There is no game history yet."

    //ParticipantJointTeamScreen
    override val teamsTitle = "Teams"
    override val joinTeamTitle = "Join Team"
    override val joinTeamDescription = "Enter the code you received from the team organizer."
    override val confirmAndJoin = "Confirm and join"
    override val cancel = "Cancel"

    //ParticipantTeamScreen
    override val myTeamTitle = "My team"
    override val myTeamSubtitle = "Check players, standings and recent games."
    override val noTeamTitle = "No team linked"
    override val noTeamDescription = "You have not been linked to a team yet."
    override val joinTeamButton = "Join team"
    override val teamCode = "Team code"
    override val teamName = "Team name"
    override val playersTitle = "Players"
    override val playersCount: (Int) -> String = { total -> "$total players in the team" }
    override val teamStandingTitle = "Standings"
    override val noStandingYet = "No standings yet"
    override val lastGamesTitle = "Recent games"
    override val noRegisteredGames = "There are no registered games yet"
    override val gamesFound: (Int) -> String = { total -> "$total games found" }

    override val myTeamsTitle = "My teams"
    override val selectedTeamTitle = "Selected team"

    override val selectedTeam = "Selected"
    override val viewDetails = "Seeing details"
    override val selectTeam = "Select team"
    override val leaveTeam = "Leave team"

    override val tournamentIdLabel = "Tournament ID"
    override val codeLabel = "Code"

    //ParticipantStatsScreen
    override val myStatsTitle = "My statistics"
    override val myStatsSubtitle = "Summary of your performance."
    override val gamesStat = "Games"
    override val goalsStat = "Goals"
    override val assistsStat = "Fouls"
    override val mvpStat = "Cards"
    override val overallPerformance = "Overall performance"
    override val overallPerformanceSubtitle = "Quick comparison of your statistics."
    override val notEnoughStats = "There are not enough statistics yet to analyze your progress."
    override val performanceTitle = "Performance"
    override val goalsPerGame = "Goals per game"
    override val assistsPerGame = "Fouls per game"
    override val statsStartMessage = "Play matches to start seeing your progress."
    override val statsContinueMessage = "Keep playing to improve your statistics."

    //
    override val spectatorHomeTitle = "Explore"
    override val spectatorHomeSubtitle = "Follow tournaments and live matches"

    override val liveMatches = "Live Matches"
    override val trendingTournaments = "Trending Tournaments"

    override val tournamentDetails = "Tournament Details"
    override val bestScorers = "Top Scorers"
    override val standings = "Standings"
    override val teams = "Teams"
    override val matches = "Matches"


    override val matchStatistics = "Match Statistics"
    override val liveStatistics = "Live Statistics"
    override val completeStatistics = "View Full Statistics"

    override val timeline = "Timeline"
    override val noEventsRegistered = "No events recorded for this match."

    override val live = "Live"
    override val finished = "Finished"
    override val scheduled = "Scheduled"

    override val homeTeam = "Home"
    override val awayTeam = "Away"

    override val possession = "Possession"
    override val shots = "Shots"
    override val shotsOnTarget = "Shots on Target"
    override val corners = "Corners"
    override val fouls = "Fouls"
    override val yellowCards = "Yellow Cards"
    override val redCards = "Red Cards"

    override val noStatisticsAvailable =
        "No statistics have been recorded for this match yet."

    override val notifications = "Notifications"
    override val notificationSettings = "Notification Settings"

    override val gameDetails = "Match Details"
    override val matchResult = "Result"

    //ClassificacaoScreen Spectator

    override val classification = "Standings"
    override val noClassificationAvailable = "No standings data available yet."
    override val back = "Back"
    override val participantsTeams: (Int) -> String = { total -> "Participants: $total teams" }
    override val positionShort = "Pos"
    override val team = "Team"
    override val gamesShort = "P"
    override val winsShort = "W"
    override val drawsShort = "D"
    override val lossesShort = "L"
    override val goalDifferenceShort = "GD"
    override val pointsShort = "Pts"
    override val noTeamsYet = "There are no teams yet."
}