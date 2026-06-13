package com.leaguematch

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.leaguematch.data.remote.model.Jogo
import com.leaguematch.data.remote.model.Torneio
import com.leaguematch.ui.spectator.JogosScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class JogosScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockTorneio = Torneio(
        id = 1,
        nome = "Taça de Basquete",
        modalidade = "Basquetebol",
        regras = "Pontos",
        formato = "Liga",
        estado = "Em Curso",
        equipas = 6,
        active = true
    )

    private val mockJogos = listOf(
        Jogo(
            id = 20,
            torneioId = 1,
            casa = "Seixal Basket",
            fora = "Porto Basket",
            resultadoCasa = 78,
            resultadoFora = 82,
            estado = "Finalizado",
            active = true,
            data = "2026-06-03",
            hora = "20:00"
        ),
        Jogo(
            id = 21,
            torneioId = 1,
            casa = "Braga Basket",
            fora = "Lisboa Basket",
            resultadoCasa = 45,
            resultadoFora = 40,
            estado = "A Decorrer",
            active = true,
            data = "2026-06-13",
            hora = "12:00"
        )
    )

    @Test
    fun jogosScreen_emptyState_showsNoGamesMessage() {
        composeTestRule.setContent {
            JogosScreen(
                torneio = mockTorneio,
                jogos = emptyList(),
                onHomeClick = {},
                onClassificacaoClick = {},
                onJogosClick = {},
                onEquipasClick = {},
                onPerfilClick = {}
            )
        }

        // Verify tournament header details
        composeTestRule.onNodeWithText("Taça de Basquete").assertExists()
        composeTestRule.onNodeWithText("Basquetebol • Em Curso").assertExists()

        // Verify empty state card
        composeTestRule.onNodeWithText("Ainda não existem jogos.").assertExists()
    }

    @Test
    fun jogosScreen_rendersGames_andTriggersClickCallback() {
        var clickedJogo: Jogo? = null

        composeTestRule.setContent {
            JogosScreen(
                torneio = mockTorneio,
                jogos = mockJogos,
                onHomeClick = {},
                onClassificacaoClick = {},
                onJogosClick = {},
                onEquipasClick = {},
                onPerfilClick = {},
                onJogoClick = { clickedJogo = it }
            )
        }

        // Verify both matches are rendered
        composeTestRule.onNodeWithText("Seixal Basket").assertExists()
        composeTestRule.onNodeWithText("Porto Basket").assertExists()
        composeTestRule.onNodeWithText("78 - 82").assertExists()

        composeTestRule.onNodeWithText("Braga Basket").assertExists()
        composeTestRule.onNodeWithText("Lisboa Basket").assertExists()
        composeTestRule.onNodeWithText("45 - 40").assertExists()

        // Click on the first match
        composeTestRule.onNodeWithText("Seixal Basket").performClick()

        // Assert that the callback was called with the correct match
        assertNotNull(clickedJogo)
        assertEquals(20, clickedJogo?.id)
        assertEquals("Seixal Basket", clickedJogo?.casa)
    }
}
