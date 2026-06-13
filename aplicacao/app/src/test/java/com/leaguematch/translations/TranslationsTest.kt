package com.leaguematch.translations

import org.junit.Assert.assertEquals
import org.junit.Test

class TranslationsTest {

    @Test
    fun testPortugueseTranslations() {
        val strings = StringsPt
        assertEquals("5 torneios associados à tua conta", strings.tournamentsAssociated(5))
        assertEquals("8 equipas", strings.teamsLabel(8))
        assertEquals("12 golos", strings.goalsLabel(12))
        assertEquals("15 pts", strings.pointsLabel(15))
        assertEquals("3V 2E 1D", strings.classificationRecord(3, 2, 1))
        assertEquals("11 jogadores na equipa", strings.playersCount(11))
        assertEquals("6 jogos encontrados", strings.gamesFound(6))
        assertEquals("Participantes: 4 equipas", strings.participantsTeams(4))
    }

    @Test
    fun testEnglishTranslations() {
        val strings = StringsEn
        assertEquals("5 tournaments linked to your account", strings.tournamentsAssociated(5))
        assertEquals("8 teams", strings.teamsLabel(8))
        assertEquals("12 goals", strings.goalsLabel(12))
        assertEquals("15 pts", strings.pointsLabel(15))
        assertEquals("3W 2D 1L", strings.classificationRecord(3, 2, 1))
        assertEquals("11 players in the team", strings.playersCount(11))
        assertEquals("6 games found", strings.gamesFound(6))
        assertEquals("Participants: 4 teams", strings.participantsTeams(4))
    }
}
