package com.leaguematch

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.leaguematch.data.remote.model.NotificacaoItem
import com.leaguematch.ui.spectator.InboxNotificacoesScreen
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class InboxNotificacoesScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun emptyState_showsPlaceholderMessage() {
        composeTestRule.setContent {
            InboxNotificacoesScreen(
                notificacoes = emptyList(),
                onBackClick = {},
                onMarcarTodasLidas = {},
                onNotificacaoClick = {}
            )
        }
        composeTestRule.onNodeWithText("Sem notificações por agora.").assertIsDisplayed()
    }

    @Test
    fun withUnread_showsCountAndMarkAllButton() {
        val items = listOf(
            NotificacaoItem(id = 1, utilizadorId = 1, mensagem = "Jogo A vs B começou", data = "", lida = false),
            NotificacaoItem(id = 2, utilizadorId = 1, mensagem = "Resultado final: A 2-1 B", data = "", lida = false),
            NotificacaoItem(id = 3, utilizadorId = 1, mensagem = "Old", data = "", lida = true)
        )

        composeTestRule.setContent {
            InboxNotificacoesScreen(
                notificacoes = items,
                onBackClick = {},
                onMarcarTodasLidas = {},
                onNotificacaoClick = {}
            )
        }

        composeTestRule.onNodeWithText("2 por ler").assertIsDisplayed()
        composeTestRule.onNodeWithText("Marcar todas").assertIsDisplayed()
        composeTestRule.onNodeWithText("Jogo A vs B começou").assertIsDisplayed()
        composeTestRule.onNodeWithText("Resultado final: A 2-1 B").assertIsDisplayed()
    }

    @Test
    fun clickingMarkAll_triggersCallback() {
        var clicked = false
        val items = listOf(
            NotificacaoItem(id = 1, utilizadorId = 1, mensagem = "x", data = "", lida = false)
        )

        composeTestRule.setContent {
            InboxNotificacoesScreen(
                notificacoes = items,
                onBackClick = {},
                onMarcarTodasLidas = { clicked = true },
                onNotificacaoClick = {}
            )
        }

        composeTestRule.onNodeWithText("Marcar todas").performClick()
        assertEquals(true, clicked)
    }

    @Test
    fun allRead_doesNotShowMarkAllButton() {
        val items = listOf(
            NotificacaoItem(id = 1, utilizadorId = 1, mensagem = "x", data = "", lida = true)
        )

        composeTestRule.setContent {
            InboxNotificacoesScreen(
                notificacoes = items,
                onBackClick = {},
                onMarcarTodasLidas = {},
                onNotificacaoClick = {}
            )
        }

        composeTestRule.onNodeWithText("0 por ler").assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Marcar todas").assertCountEquals(0)
    }
}
