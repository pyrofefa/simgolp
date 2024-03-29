package movil.siafeson.citricos.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import movil.siafeson.citricos.db.entities.DetailEntity

@Dao
interface DetailDao {
    @Insert
    fun insertDetail(detailEntity: DetailEntity)

    @Query("SELECT * FROM detalles WHERE muestreo_id = :id")
    fun getDetailsRecord(id: Int): List<DetailEntity>

    @Query("DELETE FROM detalles WHERE id = :id")
    fun deleteDetail(id: Int)

    @Query("UPDATE detalles SET adultos = :adults WHERE id = :id")
    fun editDetail(id: Int, adults: Int)

    @Query("DELETE FROM detalles")
    fun deleteAllDetails()
}