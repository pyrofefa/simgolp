package movil.siafeson.citricos.db.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movil.siafeson.citricos.db.daos.LocationDao
import movil.siafeson.citricos.db.entities.LocationEntity
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

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
    suspend fun deleteLocations(){
        withContext(Dispatchers.IO){
            locationDao.deleteLocation()
        }
    }
}