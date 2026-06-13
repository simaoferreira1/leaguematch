package com.leaguematch

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.leaguematch.data.remote.model.ResumoModalidade
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.ui.organizer.OrgTournamentsScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class OrgTournamentsScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockModalidades = listOf(
        ResumoModalidade("Futebol", 1),
        ResumoModalidade("Basquetebol", 1)
    )

    private val mockTorneios = listOf(
        Torneio(
            id = 1,
            nome = "Torneio Inter-Freguesias",
            modalidade = "Futebol",
            regras = "Normais",
            formato = "Liga",
            estado = "A Decorrer",
            equipas = 10,
            active = true
        ),
        Torneio(
            id = 2,
            nome = "TaçaBasquete",
            modalidade = "Basquetebol",
            regras = "Default",
            formato = "Eliminatórias",
            estado = "Por iniciar",
            equipas = 6,
            active = true
        )
    )

    @Test
    fun orgTournamentsScreen_rendersCorrectly() {
        composeTestRule.setContent {
            OrgTournamentsScreen(
                modalidades = mockModalidades,
                torneios = mockTorneios,
                totalTorneios = 2,
                onNavigateToCreate = {},
                onNavigateToActions = {}
            )
        }

        // Verify title
        composeTestRule.onNodeWithText("Os meus torneios").assertExists()
        composeTestRule.onNodeWithText("2 torneios associados ao organizador").assertExists()

        // Verify tournaments are listed
        composeTestRule.onNodeWithText("Torneio Inter-Freguesias").assertExists()
        composeTestRule.onNodeWithText("TaçaBasquete").assertExists()

        // Verify filter chips
        composeTestRule.onNodeWithText("Todos").assertExists()
        composeTestRule.onNodeWithText("Futebol").assertExists()
        composeTestRule.onNodeWithText("Basquetebol").assertExists()
    }

    @Test
    fun orgTournamentsScreen_rendersEmptyState_whenNoTournaments() {
        composeTestRule.setContent {
            OrgTournamentsScreen(
                modalidades = emptyList(),
                torneios = emptyList(),
                totalTorneios = 0,
                onNavigateToCreate = {},
                onNavigateToActions = {}
            )
        }

        composeTestRule.onNodeWithText("Nenhum torneio encontrado.").assertExists()
    }

    @Test
    fun orgTournamentsScreen_clickingCreate_triggersCallback() {
        var createClicked = false
        composeTestRule.setContent {
            OrgTournamentsScreen(
                modalidades = emptyList(),
                torneios = emptyList(),
                totalTorneios = 0,
                onNavigateToCreate = { createClicked = true },
                onNavigateToActions = {}
            )
        }

        // Click on "Criar torneio" button
        composeTestRule.onNodeWithText("Criar torneio").performClick()

        assertTrue(createClicked)
    }
}
