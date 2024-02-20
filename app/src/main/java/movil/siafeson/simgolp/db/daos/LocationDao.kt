package movil.siafeson.simgolp.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import movil.siafeson.simgolp.db.entities.LocationEntity

@Dao
interface LocationDao {
    @Insert
    fun insertLocation(location: LocationEntity)

    @Query("SELECT * FROM ubicaciones")
    suspend fun getAllLocations(): List<LocationEntity>

    // TRUNCATE TABLE
    @Query("DELETE FROM ubicaciones")
    fun deleteLocation()
}