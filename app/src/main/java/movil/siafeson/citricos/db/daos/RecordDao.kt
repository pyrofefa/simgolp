package movil.siafeson.citricos.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import movil.siafeson.citricos.db.entities.RecordEntity
import movil.siafeson.citricos.models.RecordData
import movil.siafeson.citricos.models.RecordIdData
import movil.siafeson.citricos.models.RecordsData
import movil.siafeson.citricos.models.RequestObject

@Dao
interface RecordDao {
    @Insert
    fun insertRecord(recordEntity: RecordEntity): Long

    @Query("SELECT * FROM muestreo WHERE id = :id")
    fun getRecord(id: Int) : List<RecordData>

    @Query("UPDATE muestreo SET status = :status WHERE id = :id")
    fun updateRecord(status:Int, id: Int) : Int

    @Query("UPDATE muestreo SET total_arboles = :points, total_adultos = :adults WHERE id = :id")
    fun updateRecordTotals(id: Int, adults: Int, points: Int) : Int

    @Query("SELECT " +
            "COUNT(detalles.id) as punto, " +
            "muestreo.id, " +
            "muestreo.status, " +
            "muestreo.semana, " +
            "muestreo.recurso, muestreo.total_adultos as totalAdultos " +
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

    @Query("SELECT " +
            "muestreo.id, " +
            "muestreo.fecha_hora as fecha,  " +
            "muestreo.status, " +
            "ubicaciones.predio " +
            "FROM muestreo INNER JOIN ubicaciones ON muestreo.campo_id = ubicaciones.idBit " +
            "ORDER BY datetime(muestreo.fecha_hora) " +
            "DESC")
    suspend fun getAllRecords(): List<RecordsData>

    @Query("DELETE FROM muestreo")
    fun deleteAllRecords()

}


