package com.leaguematch

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.ui.spectator.ExplorarScreen
import org.junit.Rule
import org.junit.Test

class ExplorarScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockLiveMatches = listOf(
        Jogo(
            id = 101,
            torneioId = 1,
            casa = "FC Porto",
            fora = "Sporting CP",
            resultadoCasa = 1,
            resultadoFora = 1,
            estado = "A Decorrer",
            active = true,
            data = "2026-06-02",
            hora = "19:00"
        )
    )

    private val mockTournaments = listOf(
        Torneio(
            id = 1,
            nome = "Taça de Viana",
            modalidade = "Futebol",
            regras = "Eliminatória",
            formato = "Liga",
            estado = "Em Curso",
            equipas = 8,
            active = true
        ),
        Torneio(
            id = 2,
            nome = "Liga Basquete",
            modalidade = "Basquetebol",
            regras = "Pontos",
            formato = "Grupos",
            estado = "Em Curso",
            equipas = 6,
            active = true
        )
    )

    @Test
    fun explorarScreen_rendersCorrectly() {
        composeTestRule.setContent {
            ExplorarScreen(
                liveMatches = mockLiveMatches,
                trendingTournaments = mockTournaments,
                onTorneioClick = {},
                onJogoClick = {}
            )
        }

        // Verify title & subtitle
        composeTestRule.onNodeWithText("Explorar").assertExists()
        composeTestRule.onNodeWithText("Descobre torneios e jogos em direto").assertExists()

        // Verify search bar placeholder
        composeTestRule.onNodeWithText("Pesquisar torneios, equipas...").assertExists()

        // Verify sport filter options are rendered
        composeTestRule.onNodeWithText("Todos").assertExists()
        composeTestRule.onNodeWithText("Futebol").assertExists()
        composeTestRule.onNodeWithText("Basquetebol").assertExists()

        // Verify live match is rendered
        composeTestRule.onNodeWithText("FC Porto").assertExists()
        composeTestRule.onNodeWithText("Sporting CP").assertExists()

        // Verify both popular tournaments are initially rendered
        composeTestRule.onNodeWithText("Taça de Viana").assertExists()
        composeTestRule.onNodeWithText("Liga Basquete").assertExists()
    }

    @Test
    fun explorarScreen_filteringBySportTab_updatesTournamentVisibility() {
        composeTestRule.setContent {
            ExplorarScreen(
                liveMatches = emptyList(),
                trendingTournaments = mockTournaments,
                onTorneioClick = {},
                onJogoClick = {}
            )
        }

        // Select the "Basquetebol" filter tab
        composeTestRule.onNodeWithText("Basquetebol").performClick()

        // Verify only the Basquetebol tournament is rendered, and Futebol is hidden
        composeTestRule.onNodeWithText("Liga Basquete").assertExists()
        composeTestRule.onNodeWithText("Taça de Viana").assertDoesNotExist()

        // Select "Todos" to bring back all tournaments
        composeTestRule.onNodeWithText("Todos").performClick()
        composeTestRule.onNodeWithText("Taça de Viana").assertExists()
        composeTestRule.onNodeWithText("Liga Basquete").assertExists()
    }

    @Test
    fun explorarScreen_searchingQuery_filtersTournamentsByName() {
        composeTestRule.setContent {
            ExplorarScreen(
                liveMatches = emptyList(),
                trendingTournaments = mockTournaments,
                onTorneioClick = {},
                onJogoClick = {}
            )
        }

        // Search for "Viana"
        composeTestRule.onNodeWithText("Pesquisar torneios, equipas...").performTextInput("Viana")

        // Verify "Taça de Viana" matches, but "Liga Basquete" is filtered out
        composeTestRule.onNodeWithText("Taça de Viana").assertExists()
        composeTestRule.onNodeWithText("Liga Basquete").assertDoesNotExist()
    }
}
