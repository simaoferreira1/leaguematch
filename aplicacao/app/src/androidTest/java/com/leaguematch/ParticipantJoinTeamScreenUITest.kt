package com.leaguematch

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.leaguematch.translations.StringsPt
import com.leaguematch.ui.participant.ParticipantJoinTeamScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ParticipantJoinTeamScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val strings = StringsPt
    private val primaryColor = Color(0xFFE53935)

    @Test
    fun participantJoinTeamScreen_rendersCorrectly() {
        composeTestRule.setContent {
            ParticipantJoinTeamScreen(
                isLoading = false,
                erro = null,
                strings = strings,
                primaryColor = primaryColor,
                onBackClick = {},
                onConfirmClick = {}
            )
        }

        // Verify title & details from strings
        composeTestRule.onNodeWithText(strings.teamsTitle).assertExists()
        composeTestRule.onNodeWithText(strings.joinTeamTitle).assertExists()
        composeTestRule.onNodeWithText(strings.joinTeamDescription).assertExists()
        composeTestRule.onNodeWithText(strings.confirmAndJoin).assertExists()
        composeTestRule.onNodeWithText(strings.cancel).assertExists()
    }

    @Test
    fun participantJoinTeamScreen_showsError_whenErroNotNull() {
        val errorMessage = "Código inválido ou expirado"
        composeTestRule.setContent {
            ParticipantJoinTeamScreen(
                isLoading = false,
                erro = errorMessage,
                strings = strings,
                primaryColor = primaryColor,
                onBackClick = {},
                onConfirmClick = {}
            )
        }

        // Verify error message is rendered
        composeTestRule.onNodeWithText(errorMessage).assertExists()
    }

    @Test
    fun participantJoinTeamScreen_clickingCancel_triggersBackCallback() {
        var backClicked = false
        composeTestRule.setContent {
            ParticipantJoinTeamScreen(
                isLoading = false,
                erro = null,
                strings = strings,
                primaryColor = primaryColor,
                onBackClick = { backClicked = true },
                onConfirmClick = {}
            )
        }

        // Click cancel button
        composeTestRule.onNodeWithText(strings.cancel).performClick()

        assertTrue(backClicked)
    }
}
