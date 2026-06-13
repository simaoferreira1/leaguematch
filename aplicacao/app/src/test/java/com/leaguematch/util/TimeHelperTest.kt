package com.leaguematch.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.time.Instant

class TimeHelperTest {

    @Test
    fun `parse returns null for null input`() {
        assertNull(parseIniciadoEmEpochMillis(null))
    }

    @Test
    fun `parse returns null for blank input`() {
        assertNull(parseIniciadoEmEpochMillis("   "))
    }

    @Test
    fun `parse returns null for completely invalid string`() {
        assertNull(parseIniciadoEmEpochMillis("not a date"))
    }

    @Test
    fun `parse ISO-8601 UTC with Z suffix returns correct epoch`() {
        val raw = "2026-06-13T12:00:00Z"
        val expected = Instant.parse(raw).toEpochMilli()
        assertEquals(expected, parseIniciadoEmEpochMillis(raw))
    }

    @Test
    fun `parse ISO-8601 UTC with millis and Z returns correct epoch`() {
        val raw = "2026-06-13T12:00:00.123Z"
        val expected = Instant.parse(raw).toEpochMilli()
        assertEquals(expected, parseIniciadoEmEpochMillis(raw))
    }

    @Test
    fun `parse ISO-8601 without Z assumes UTC and matches with-Z value`() {
        val withZ = "2026-06-13T12:00:00Z"
        val withoutZ = "2026-06-13T12:00:00"
        assertEquals(
            parseIniciadoEmEpochMillis(withZ),
            parseIniciadoEmEpochMillis(withoutZ)
        )
    }

    @Test
    fun `parse with space separator works (PostgREST style)`() {
        val withT = "2026-06-13T12:00:00"
        val withSpace = "2026-06-13 12:00:00"
        assertEquals(
            parseIniciadoEmEpochMillis(withT),
            parseIniciadoEmEpochMillis(withSpace)
        )
    }

    @Test
    fun `parse two equal moments yields identical millis regardless of system tz`() {
        // Regressão do bug onde Portugal (UTC+1) tinha 60 min de desfasamento
        val raw = "2026-07-13T12:00:00"
        val millis = parseIniciadoEmEpochMillis(raw)
        assertNotNull(millis)
        // Tem de bater certo com Instant.parse + Z
        assertEquals(
            Instant.parse("${raw}Z").toEpochMilli(),
            millis
        )
    }
}
