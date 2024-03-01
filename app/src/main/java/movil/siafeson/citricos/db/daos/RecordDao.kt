package movil.siafeson.citricos.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import movil.siafeson.citricos.db.entities.RecordEntity
import movil.siafeson.citricos.models.RecordIdData

@Dao
interface RecordDao {
    @Insert
    fun insertRecord(recordEntity: RecordEntity): Long

    @Query("SELECT " +
            "COUNT(detalles.id) as punto, " +
            "muestreo.id, " +
            "muestreo.status, " +
            "muestreo.semana, " +
            "muestreo.recurso " +
            "FROM muestreo " +
            "LEFT JOIN detalles ON detalles.muestreo_id = muestreo.id " +
            "WHERE muestreo.campo_id = :id " +
            "AND muestreo.semana = :week " +
            "AND muestreo.ano = :year"
    )
    fun getRecordId(id:Int, week: Int, year: Int): List<RecordIdData>

    @Query("SELECT COUNT(*) FROM muestreo WHERE fecha = :date")
    fun getCountRecords(date: String) : Long

    @Query("SELECT COUNT(*) FROM muestreo WHERE status = 2")
    fun getCountRecordsPending() : Long

    @Query("SELECT * FROM muestreo")
    suspend fun getAllRecords(): List<RecordEntity>
}


