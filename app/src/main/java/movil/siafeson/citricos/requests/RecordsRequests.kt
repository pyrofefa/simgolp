package movil.siafeson.citricos.requests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movil.siafeson.citricos.app.services
import movil.siafeson.citricos.models.RequestObject
import movil.siafeson.citricos.models.ResponseObject

class RecordsRequests() {

    suspend fun addRecord2(data: RequestObject): ResponseObject? =
        withContext(Dispatchers.IO) {
            try {
                val response = services.addRecord("simgolp/captura",data)
                if (response.isSuccessful) {
                    response.body() // Devuelve el objeto ResponseObject si la solicitud fue exitosa
                } else {
                    null // Devuelve null si la respuesta no fue exitosa
                }
            } catch (e: Exception) {
                null // Manejar excepciones y devolver null
            }
        }



}
