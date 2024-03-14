package movil.siafeson.citricos.requests

import android.app.Notification
import movil.siafeson.citricos.app.services
import movil.siafeson.citricos.models.RequestObject
import movil.siafeson.citricos.models.ResponseObject
import retrofit2.Response

class RecordsRequests() {

    suspend fun addRecord(data: RequestObject) {
        try {
            val response = services.addRecord("simgolp/captura", data)
            response // Devolver la respuesta directamente
        } catch (e: Exception) {
            null // Manejo de errores
        }
    }



}