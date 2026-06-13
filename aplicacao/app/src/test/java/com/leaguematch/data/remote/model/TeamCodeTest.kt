package com.leaguematch.data.remote.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TeamCodeTest {

    @Test
    fun testEncode() {
        // T + 5 chars padded with 0
        assertEquals("T00001", TeamCode.encode(1))
        assertEquals("T0000A", TeamCode.encode(10))
        assertEquals("T0002S", TeamCode.encode(100))
    }

    @Test
    fun testDecodeValid() {
        assertEquals(1, TeamCode.decode("T00001"))
        assertEquals(10, TeamCode.decode("T0000a")) // should be case-insensitive
        assertEquals(100, TeamCode.decode(" T0002S ")) // should trim whitespace
    }

    @Test
    fun testDecodeInvalid() {
        assertNull(TeamCode.decode(""))
        assertNull(TeamCode.decode("   "))
        assertNull(TeamCode.decode("INVALID"))
        assertNull(TeamCode.decode("T-0001")) // negative/invalid radix characters
    }

    @Test
    fun testRoundtrip() {
        val testIds = listOf(1, 42, 999, 12345, 99999)
        for (id in testIds) {
            val encoded = TeamCode.encode(id)
            val decoded = TeamCode.decode(encoded)
            assertEquals(id, decoded)
        }
    }
}
