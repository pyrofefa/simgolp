package movil.siafeson.citricos.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import movil.siafeson.citricos.db.entities.LocationEntity

@Dao
interface LocationDao {
    @Insert
    fun insertLocation(locationEntity: LocationEntity)

    @Query("SELECT * FROM ubicaciones")
    suspend fun getAllLocations(): List<LocationEntity>

    // TRUNCATE TABLE
    @Query("DELETE FROM ubicaciones")
    fun deleteLocation()

    @Query("SELECT * FROM ubicaciones ORDER BY predio")
    fun getOrderedElements(): List<LocationEntity>
}