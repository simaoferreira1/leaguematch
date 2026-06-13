package com.leaguematch.data.remote.model

import org.junit.Assert.assertEquals
import org.junit.Test

class LeagueModelsTest {

    @Test
    fun testDetalheTorneioTotalGolos() {
        val torneio = Torneio(
            id = 1,
            nome = "Test",
            modalidade = "Futebol",
            regras = "Default",
            formato = "Liga",
            estado = "Por iniciar"
        )
        val jogo1 = Jogo(
            id = 1,
            torneioId = 1,
            casa = "A",
            fora = "B",
            resultadoCasa = 2,
            resultadoFora = 1,
            estado = "Finalizado"
        )
        val jogo2 = Jogo(
            id = 2,
            torneioId = 1,
            casa = "B",
            fora = "C",
            resultadoCasa = 0,
            resultadoFora = 4,
            estado = "Finalizado"
        )
        val detalhe = DetalheTorneio(
            torneio = torneio,
            goleadores = emptyList(),
            jogos = listOf(jogo1, jogo2)
        )
        
        assertEquals(7, detalhe.totalGolos)
    }

    @Test
    fun testLeagueConverters() {
        val converters = LeagueConverters()
        
        // test fromTipoUtilizador
        assertEquals("ADMIN", converters.fromTipoUtilizador(TipoUtilizador.ADMIN))
        assertEquals("ORGANIZADOR", converters.fromTipoUtilizador(TipoUtilizador.ORGANIZADOR))
        assertEquals("PARTICIPANTE", converters.fromTipoUtilizador(TipoUtilizador.PARTICIPANTE))
        assertEquals("ESPECTADOR", converters.fromTipoUtilizador(TipoUtilizador.ESPECTADOR))

        // test toTipoUtilizador
        assertEquals(TipoUtilizador.ADMIN, converters.toTipoUtilizador("ADMIN"))
        assertEquals(TipoUtilizador.ORGANIZADOR, converters.toTipoUtilizador("ORGANIZADOR"))
        assertEquals(TipoUtilizador.PARTICIPANTE, converters.toTipoUtilizador("PARTICIPANTE"))
        assertEquals(TipoUtilizador.ESPECTADOR, converters.toTipoUtilizador("ESPECTADOR"))
    }
}
