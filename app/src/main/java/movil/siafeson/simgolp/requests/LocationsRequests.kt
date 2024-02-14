package movil.siafeson.simgolp.requests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movil.siafeson.simgolp.db.entities.LocationEntity
import movil.siafeson.simgolp.app.MyApp
import movil.siafeson.simgolp.app.services

class LocationsRequests {
    val userId = MyApp.preferences.userId

    suspend fun locations():List<LocationEntity> =
        withContext(Dispatchers.IO){
            val response = services.getLocations("simgolp/ubicaciones/${userId}")
            if (response.status == "success"){
                emptyList<LocationEntity>()
            }else{
                emptyList<LocationEntity>()
            }
        }
}