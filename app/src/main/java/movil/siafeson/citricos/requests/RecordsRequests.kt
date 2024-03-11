package movil.siafeson.citricos.requests

import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movil.siafeson.citricos.app.services
import movil.siafeson.citricos.models.RecordData

class RecordsRequests {

    suspend fun addRecord(data: LiveData<List<RecordData>>) : String  {
        return withContext(Dispatchers.IO){
            try {
                val response = services.addRecord("simgolp/captura",data)
                Log.i("Entrando a data","${data}")
                ""
            }catch (e: Exception){
                "Error al guardar registro: ${e.message}"
            }
        }
    }

}