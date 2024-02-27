package movil.siafeson.citricos.db.daos

import androidx.room.Dao
import androidx.room.Insert
import movil.siafeson.citricos.db.entities.DetailEntity

@Dao
interface DetailDao {
    @Insert
    fun insertDetail(detailEntity: DetailEntity)
}