package movil.siafeson.citricos.requests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movil.siafeson.citricos.db.entities.LocationEntity
import movil.siafeson.citricos.app.MyApp
import movil.siafeson.citricos.app.services

class LocationsRequests {
    private val userId = MyApp.preferences.userId

    suspend fun locations(): List<LocationEntity> =
        withContext(Dispatchers.IO) {
            try {
                val response = services.getLocations("simgolp/ubicaciones/${userId}")
                if (response.isSuccessful) {
                    response.body()?.data?.map { locationData ->
                        LocationEntity(
                            idBit = locationData.id_bit,
                            predio = locationData.predio,
                            status = locationData.status,
                            latitud = locationData.latitud,
                            longitud = locationData.longitud,
                            superficie = locationData.superficie,
                            idSicafi = locationData.id_sicafi
                        )
                    } ?: emptyList()
                } else {
                    // Manejar el caso en que la respuesta no es exitosa
                    emptyList()
                }
            } catch (e: Exception) {
                // Manejar excepciones, por ejemplo, problemas de red
                emptyList()
            }
        }
}