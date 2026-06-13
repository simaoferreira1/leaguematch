package com.leaguematch

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.leaguematch.data.sync.SyncQueueStore
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Testes ao SyncQueueStore corridos com SharedPreferences reais no emulador.
 *
 * Razão para androidTest e não JVM: o store usa android.content.SharedPreferences,
 * que não está disponível no classpath de testes unitários puros sem mockar.
 */
@RunWith(AndroidJUnit4::class)
class SyncQueueStoreInstrumentedTest {

    private lateinit var store: SyncQueueStore

    @Before
    fun setUp() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        store = SyncQueueStore(ctx)
        store.clear() // garante estado limpo
    }

    @After
    fun tearDown() {
        store.clear()
    }

    @Test
    fun initialQueueIsEmpty() {
        assertEquals(0, store.pendingCount())
        assertTrue(store.pending().isEmpty())
    }

    @Test
    fun enqueueAddsItem() {
        store.enqueueResultUpdate(
            SyncQueueStore.PendingResultUpdate(
                jogoId = 42, resultadoCasa = 2, resultadoFora = 1, estado = "FINALIZADO"
            )
        )
        assertEquals(1, store.pendingCount())
        val item = store.pending().first()
        assertEquals(42, item.jogoId)
        assertEquals(2, item.resultadoCasa)
        assertEquals(1, item.resultadoFora)
        assertEquals("FINALIZADO", item.estado)
    }

    @Test
    fun enqueueReplacesExistingForSameMatch() {
        store.enqueueResultUpdate(
            SyncQueueStore.PendingResultUpdate(jogoId = 1, resultadoCasa = 0, resultadoFora = 0, estado = "EM_CURSO")
        )
        store.enqueueResultUpdate(
            SyncQueueStore.PendingResultUpdate(jogoId = 1, resultadoCasa = 3, resultadoFora = 2, estado = "FINALIZADO")
        )
        assertEquals(1, store.pendingCount())
        val item = store.pending().first()
        assertEquals(3, item.resultadoCasa)
        assertEquals(2, item.resultadoFora)
        assertEquals("FINALIZADO", item.estado)
    }

    @Test
    fun multipleDifferentMatchesCoexist() {
        store.enqueueResultUpdate(SyncQueueStore.PendingResultUpdate(1, 1, 0, "EM_CURSO"))
        store.enqueueResultUpdate(SyncQueueStore.PendingResultUpdate(2, 2, 2, "FINALIZADO"))
        store.enqueueResultUpdate(SyncQueueStore.PendingResultUpdate(3, 0, 1, "EM_CURSO"))
        assertEquals(3, store.pendingCount())
    }

    @Test
    fun removeDropsOnlyMatching() {
        store.enqueueResultUpdate(SyncQueueStore.PendingResultUpdate(1, 1, 0, "EM_CURSO"))
        store.enqueueResultUpdate(SyncQueueStore.PendingResultUpdate(2, 2, 1, "EM_CURSO"))
        store.remove(1)
        assertEquals(1, store.pendingCount())
        assertEquals(2, store.pending().first().jogoId)
    }

    @Test
    fun clearEmptiesEverything() {
        store.enqueueResultUpdate(SyncQueueStore.PendingResultUpdate(1, 1, 0, "EM_CURSO"))
        store.enqueueResultUpdate(SyncQueueStore.PendingResultUpdate(2, 2, 1, "EM_CURSO"))
        store.clear()
        assertEquals(0, store.pendingCount())
    }

    @Test
    fun queuePersistsAcrossStoreInstances() {
        store.enqueueResultUpdate(SyncQueueStore.PendingResultUpdate(7, 5, 5, "FINALIZADO"))
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val novo = SyncQueueStore(ctx)
        assertEquals(1, novo.pendingCount())
        assertEquals(7, novo.pending().first().jogoId)
    }
}
