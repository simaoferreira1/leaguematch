package com.leaguematch

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.leaguematch.ui.auth.RegisterScreen
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class RegisterScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun registerScreen_rendersCorrectly() {
        composeTestRule.setContent {
            RegisterScreen(
                erro = null,
                sucesso = false,
                onBackClick = {},
                onRegisterClick = { _, _, _, _ -> },
                onSuccessRedirect = {}
            )
        }

        // Verify title and subtitle are present
        composeTestRule.onNodeWithText("Criar conta.").assertExists()
        composeTestRule.onNodeWithText("Junta-te ao LeagueMatch e começa a competir.").assertExists()

        // Verify input fields/labels are present
        composeTestRule.onNodeWithText("NOME COMPLETO").assertExists()
        composeTestRule.onNodeWithText("EMAIL").assertExists()
        
        // Verify role selection buttons
        composeTestRule.onNodeWithText("Participante").assertExists()
        composeTestRule.onNodeWithText("Organizador").assertExists()
        composeTestRule.onNodeWithText("Espectador").assertExists()

        // Verify button
        composeTestRule.onNodeWithText("Registar").assertExists()
    }

    @Test
    fun registerScreen_submittingValidForm_triggersRegisterCallback() {
        var submittedName = ""
        var submittedEmail = ""
        var submittedPassword = ""
        var submittedRole = ""

        composeTestRule.setContent {
            RegisterScreen(
                erro = null,
                sucesso = false,
                onBackClick = {},
                onRegisterClick = { nome, email, pass, role ->
                    submittedName = nome
                    submittedEmail = email
                    submittedPassword = pass
                    submittedRole = role
                },
                onSuccessRedirect = {}
            )
        }

        // Fill out input fields
        composeTestRule.onNodeWithText("Nome Completo").performTextInput("John Doe")
        composeTestRule.onNodeWithText("exemplo@leaguematch.com").performTextInput("john@doe.com")
        
        // Input password fields (first is Password, second is Repeat Password)
        composeTestRule.onAllNodesWithText("••••••••••")[0].performTextInput("mypassword123")
        composeTestRule.onAllNodesWithText("••••••••••")[1].performTextInput("mypassword123")

        // Select Organizer role
        composeTestRule.onNodeWithText("Organizador").performClick()

        // Click the Registar button
        composeTestRule.onNodeWithText("Registar").performClick()

        // Assert callback values match user inputs
        assertEquals("John Doe", submittedName)
        assertEquals("john@doe.com", submittedEmail)
        assertEquals("mypassword123", submittedPassword)
        assertEquals("ORGANIZADOR", submittedRole)
    }
}
