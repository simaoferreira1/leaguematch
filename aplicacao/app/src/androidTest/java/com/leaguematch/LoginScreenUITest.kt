package com.leaguematch

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.leaguematch.ui.auth.LoginScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LoginScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_rendersCorrectly() {
        composeTestRule.setContent {
            LoginScreen(
                erro = null,
                onLoginClick = { _, _ -> },
                onRegisterClick = {}
            )
        }

        // Assert greetings are rendered
        composeTestRule.onNodeWithText("Bem-vindo\nde volta.").assertExists()
        
        // Assert buttons are present
        composeTestRule.onNodeWithText("Entrar").assertExists()
        composeTestRule.onNodeWithText("Criar conta").assertExists()
    }

    @Test
    fun loginScreen_submittingCredentials_triggersCallback() {
        var emailInput = ""
        var passwordInput = ""

        composeTestRule.setContent {
            LoginScreen(
                erro = null,
                onLoginClick = { email, pass ->
                    emailInput = email
                    passwordInput = pass
                },
                onRegisterClick = {}
            )
        }

        // Input credentials using placeholders
        composeTestRule.onNodeWithText("exemplo@leaguematch.com").performTextInput("test@domain.com")
        composeTestRule.onNodeWithText("••••••••••").performTextInput("my_password")

        // Click the Entrar button
        composeTestRule.onNodeWithText("Entrar").performClick()

        // Assert callback values
        assertEquals("test@domain.com", emailInput)
        assertEquals("my_password", passwordInput)
    }

    @Test
    fun loginScreen_clickingRegister_triggersCallback() {
        var registerClicked = false

        composeTestRule.setContent {
            LoginScreen(
                erro = null,
                onLoginClick = { _, _ -> },
                onRegisterClick = { registerClicked = true }
            )
        }

        // Click register navigation
        composeTestRule.onNodeWithText("Criar conta").performClick()

        assertTrue(registerClicked)
    }
}
