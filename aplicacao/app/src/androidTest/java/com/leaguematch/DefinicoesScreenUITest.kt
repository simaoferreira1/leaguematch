package com.leaguematch

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import com.leaguematch.data.remote.model.TipoUtilizador
import com.leaguematch.data.remote.model.Utilizador
import com.leaguematch.ui.admin.DefinicoesScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DefinicoesScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockUser = Utilizador(
        id = 10,
        nome = "Simão Ferreira",
        email = "simao@leaguematch.com",
        tipo = TipoUtilizador.ADMIN
    )

    @Test
    fun definicoesScreen_rendersCorrectly() {
        composeTestRule.setContent {
            DefinicoesScreen(
                utilizadorLogado = mockUser,
                onTerminarSessaoClick = {},
                onEditarPerfilClick = { _, _ -> },
                onGerirNotificacoesClick = {}
            )
        }

        // Verify top bar title
        composeTestRule.onNodeWithText("Perfil").assertExists()

        // Verify user details card
        composeTestRule.onNodeWithText("Simão Ferreira").assertExists()
        composeTestRule.onNodeWithText("Administrador · LeagueMatch").assertExists()
        composeTestRule.onNodeWithText("Editar perfil").assertExists()

        // Verify settings options are rendered
        composeTestRule.onNodeWithText("Notificações").assertExists()
        composeTestRule.onNodeWithText("Idioma").assertExists()
        composeTestRule.onNodeWithText("Aparência").assertExists()
        composeTestRule.onNodeWithText("Terminar sessão").assertExists()
    }

    @Test
    fun definicoesScreen_clickingLogout_triggersCallback() {
        var logoutClicked = false

        composeTestRule.setContent {
            DefinicoesScreen(
                utilizadorLogado = mockUser,
                onTerminarSessaoClick = { logoutClicked = true },
                onEditarPerfilClick = { _, _ -> },
                onGerirNotificacoesClick = {}
            )
        }

        // Click on logout option row
        composeTestRule.onNodeWithText("Terminar sessão").performClick()

        assertTrue(logoutClicked)
    }

    @Test
    fun definicoesScreen_editingProfile_savesUpdatedValues() {
        var updatedName = ""
        var updatedPassword: String? = null

        composeTestRule.setContent {
            DefinicoesScreen(
                utilizadorLogado = mockUser,
                onTerminarSessaoClick = {},
                onEditarPerfilClick = { nome, senha ->
                    updatedName = nome
                    updatedPassword = senha
                },
                onGerirNotificacoesClick = {}
            )
        }

        // Click to open edit dialog
        composeTestRule.onNodeWithText("Editar perfil").performClick()

        // Verify edit profile dialog is shown
        composeTestRule.onNodeWithText("Editar Perfil").assertExists()

        // Replace name in field (it already contains "Simão Ferreira" as initial value)
        composeTestRule.onNodeWithText("Nome completo").performTextReplacement("Simão Silva")

        // Input optional password
        composeTestRule.onNodeWithText("Mudar palavra-passe").performTextReplacement("new_password_123")

        // Click Guardar to submit changes
        composeTestRule.onNodeWithText("Guardar").performClick()

        // Assert the update callback triggers with new credentials
        assertEquals("Simão Silva", updatedName)
        assertEquals("new_password_123", updatedPassword)
    }
}
