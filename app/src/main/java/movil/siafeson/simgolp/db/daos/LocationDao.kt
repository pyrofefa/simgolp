package movil.siafeson.simgolp.db.daos

import androidx.room.Insert
import movil.siafeson.simgolp.db.entities.LocationEntity

interface LocationDao {
    @Insert
    fun insert(location: LocationEntity)
}