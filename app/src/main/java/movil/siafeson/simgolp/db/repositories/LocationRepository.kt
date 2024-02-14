package movil.siafeson.simgolp.db.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movil.siafeson.simgolp.db.daos.LocationDao
import movil.siafeson.simgolp.db.entities.LocationEntity

class LocationRepository(private val locationDao: LocationDao) {
    suspend fun insertLocation(location: LocationEntity){
        withContext(Dispatchers.IO){
            locationDao.insertLocation(location)
        }
    }
    suspend fun getAllLocations(): List<LocationEntity>{
        return withContext(Dispatchers.IO){
            locationDao.getAllLocations()
        }
    }
}