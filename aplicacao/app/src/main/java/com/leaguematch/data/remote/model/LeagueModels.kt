package com.leaguematch.data.remote.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class TipoUtilizador(val descricao: String) {
    ADMIN("Administrador"),
    ORGANIZADOR("Organizador"),
    PARTICIPANTE("Participante"),
    ESPECTADOR("Espectador")
}

@Entity(tableName = "utilizadores")
data class Utilizador(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val email: String,
    val tipo: TipoUtilizador,
    val active: Boolean = true,
    val equipas: Int = 0,
    val torneios: Int = 0,
    val jogos: Int = 0
)

@Entity(tableName = "torneios")
data class Torneio(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val modalidade: String,
    val regras: String,
    val formato: String,
    val estado: String,
    val equipas: Int = 0,
    val active: Boolean = true
)

@Entity(tableName = "jogos")
data class Jogo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val torneioId: Int,
    val casa: String,
    val fora: String,
    val resultadoCasa: Int = 0,
    val resultadoFora: Int = 0,
    val estado: String,
    val active: Boolean = true
)

data class Classificacao(
    val equipaId: Int,
    val nomeEquipa: String,
    val pontos: Int,
    val jogos: Int,
    val vitorias: Int,
    val empates: Int,
    val derrotas: Int,
    val golosMarcados: Int,
    val golosSofridos: Int
)

data class Goleador(
    val nome: String,
    val golos: Int
)

data class ResumoDashboard(
    val totalUtilizadores: Int,
    val totalTorneios: Int,
    val torneiosEmCurso: Int,
    val alertasSistema: Int
)

data class ResumoModalidade(
    val nome: String,
    val totalTorneios: Int
)

data class EstatisticasAdmin(
    val totalUtilizadores: Int,
    val totalTorneios: Int,
    val totalJogos: Int,
    val alertas: Int,
    val jogosPorPeriodo: List<Float>,
    val torneiosPorEstado: List<ParGrafico>,
    val topTorneios: List<ParGrafico>
)

data class ParGrafico(
    val legenda: String,
    val valorNormalizado: Float
)

data class Equipa(
    val id: Int,
    val nome: String,
    val torneioId: Int
)

data class DetalheTorneio(
    val torneio: Torneio,
    val goleadores: List<Goleador>,
    val jogos: List<Jogo>
) {
    val totalGolos: Int = jogos.sumOf { it.resultadoCasa + it.resultadoFora }
}

@Dao
interface UtilizadorDao {
    @Query("SELECT * FROM utilizadores")
    fun listAll(): Flow<List<Utilizador>>

    @Query("SELECT * FROM utilizadores WHERE active = 1")
    fun listActive(): Flow<List<Utilizador>>

    @Query("SELECT * FROM utilizadores WHERE id = :id")
    suspend fun obterPorId(id: Int): Utilizador?

    @Insert
    suspend fun insert(utilizador: Utilizador)

    @Update
    suspend fun update(utilizador: Utilizador)

    @Delete
    suspend fun delete(utilizador: Utilizador)

    @Query("UPDATE utilizadores SET active = 0 WHERE id = :id")
    suspend fun deactivate(id: Int)

    @Query("UPDATE utilizadores SET active = 1 WHERE id = :id")
    suspend fun activate(id: Int)
}

@Dao
interface TorneioDao {
    @Query("SELECT * FROM torneios")
    fun listAll(): Flow<List<Torneio>>

    @Query("SELECT * FROM torneios WHERE active = 1")
    fun listActive(): Flow<List<Torneio>>

    @Query("SELECT * FROM torneios WHERE id = :id")
    suspend fun obterPorId(id: Int): Torneio?

    @Insert
    suspend fun insert(torneio: Torneio)

    @Update
    suspend fun update(torneio: Torneio)

    @Delete
    suspend fun delete(torneio: Torneio)

    @Query("UPDATE torneios SET active = 0 WHERE id = :id")
    suspend fun deactivate(id: Int)

    @Query("UPDATE torneios SET active = 1 WHERE id = :id")
    suspend fun activate(id: Int)
}

@Dao
interface JogoDao {
    @Query("SELECT * FROM jogos")
    fun listAll(): Flow<List<Jogo>>

    @Query("SELECT * FROM jogos WHERE active = 1")
    fun listActive(): Flow<List<Jogo>>

    @Query("SELECT * FROM jogos WHERE torneioId = :torneioId")
    fun listAllByTorneio(torneioId: Int): Flow<List<Jogo>>

    @Query("SELECT * FROM jogos WHERE torneioId = :torneioId AND active = 1")
    fun listActiveByTorneio(torneioId: Int): Flow<List<Jogo>>

    @Insert
    suspend fun insert(jogo: Jogo)

    @Update
    suspend fun update(jogo: Jogo)

    @Delete
    suspend fun delete(jogo: Jogo)

    @Query("UPDATE jogos SET active = 0 WHERE id = :id")
    suspend fun deactivate(id: Int)

    @Query("UPDATE jogos SET active = 1 WHERE id = :id")
    suspend fun activate(id: Int)
}

@Database(entities = [Utilizador::class, Torneio::class, Jogo::class], version = 1, exportSchema = false)
@TypeConverters(LeagueConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun utilizadorDao(): UtilizadorDao
    abstract fun torneioDao(): TorneioDao
    abstract fun jogoDao(): JogoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "league_match_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class LeagueConverters {
    @TypeConverter
    fun fromTipoUtilizador(value: TipoUtilizador): String = value.name

    @TypeConverter
    fun toTipoUtilizador(value: String): TipoUtilizador = TipoUtilizador.valueOf(value)
}

@Composable
fun LeagueManagementScreen(
    utilizadores: List<Utilizador>,
    torneios: List<Torneio>,
    onDeactivateTorneio: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Gestão de Liga", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(text = "Torneios Ativos", style = MaterialTheme.typography.titleLarge)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(torneios.filter { it.active }) { torneio ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = torneio.nome, style = MaterialTheme.typography.titleMedium)
                            Text(text = torneio.modalidade, style = MaterialTheme.typography.bodySmall)
                        }
                        Button(onClick = { onDeactivateTorneio(torneio.id) }) {
                            Text("Desativar")
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Utilizadores", style = MaterialTheme.typography.titleLarge)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(utilizadores) { utilizador ->
                ListItem(
                    headlineContent = { Text(utilizador.nome) },
                    supportingContent = { Text(utilizador.email) },
                    trailingContent = { Text(if (utilizador.active) "Ativo" else "Inativo") }
                )
            }
        }
    }
}
