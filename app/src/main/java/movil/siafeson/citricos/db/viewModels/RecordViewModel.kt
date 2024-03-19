package movil.siafeson.citricos.db.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import movil.siafeson.citricos.app.DatabaseSingleton
import movil.siafeson.citricos.db.entities.DetailEntity
import movil.siafeson.citricos.db.entities.RecordEntity
import movil.siafeson.citricos.db.repositories.DetailRepository
import movil.siafeson.citricos.db.repositories.RecordRepository
import movil.siafeson.citricos.models.RecordData
import movil.siafeson.citricos.models.RecordIdData
import movil.siafeson.citricos.models.RecordsData
import movil.siafeson.citricos.models.RequestObject
import movil.siafeson.citricos.models.ResponseObject
import movil.siafeson.citricos.requests.RecordsRequests
import movil.siafeson.citricos.utils.Result

class RecordViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RecordRepository = RecordRepository(DatabaseSingleton.db.recordDao())
    private val repositoryDetail: DetailRepository = DetailRepository(DatabaseSingleton.db.detailDao())

    //Insertar muestreo
    private val _insertedRecordId = MutableLiveData<Long?>()
    val insertedRecordId: MutableLiveData<Long?>
        get() = _insertedRecordId

    //Muestreo Id
    private val _recordId = MutableLiveData<List<RecordIdData>?>()
    val recordId: MutableLiveData<List<RecordIdData>?> get() = _recordId

    //Numero de muestreos
    private val _countsRecords = MutableLiveData<Long?>()
    val countRecords: LiveData<Long?> get() = _countsRecords

    //Numero de muestreos
    private val _recordsPending = MutableLiveData<Long?>()
    val recordPending: LiveData<Long?> get() = _recordsPending

    //Enviando muestreo y recuperando la respuesta
    private val _responseLiveData = MutableLiveData<ResponseObject?>()
    val responseLiveData: LiveData<ResponseObject?>
        get() = _responseLiveData

    init {
        Log.i("RecordViewModel", "ViewModel creado")
    }

    fun insertRecord(record: RecordEntity) {
        viewModelScope.launch {
            try {
                val insertedId = repository.insertRecord(record)
                // Actualizar el LiveData con el ID del registro insertado
                _insertedRecordId.postValue(insertedId)
            } catch (e: Exception) {
                // Manejar la excepción según tus necesidades
                // Por ejemplo, mostrar un mensaje de error al usuario.
            }
        }
    }
    suspend fun updateRecord(status: Int, id: Int): Result<Int?> {
        return try {
            val updateId = repository.updateRecord(status, id)
            Result.Success(updateId)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun updateRecordTotals(id: Int, adults: Int, points: Int): Result<Int?> {
        return try {
            val updateId = repository.updateRecordTotal(id,adults, points)
            Result.Success(updateId)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    //Finalizar
    fun fetchRecord(id: Int) {
        viewModelScope.launch {
            try {
                val records = repository.getRecord(id)
                val details = repositoryDetail.getDetailsRecord(id)
                val request = transformData(records, details)
                val response = RecordsRequests().addRecord2(request)
                _responseLiveData.value = response
            } catch (e: Exception) {
                Log.e("Error", "Error al obtener el registro: ${e.message}")
                _responseLiveData.value = null
            }
        }
    }


    private fun transformData(result: List<RecordData>?, details: List<DetailEntity>) : RequestObject {
        return RequestObject(
            muestreo = result,
            pt_ind = details.joinToString(",") { detail ->
                "${detail.punto}"
            },
            pt_lan = details.joinToString(",") { detail ->
                "${detail.latitud}"
            },
            pt_lon = details.joinToString(",") { detail ->
                "${detail.longitud}"
            },
            pt_dist = details.joinToString(",") { detail ->
                "${detail.distanciaQr}"
            },
            pt_fecha = details.joinToString(",") { detail ->
                "${detail.fecha}"
            },
            pt_adultos = details.joinToString(",") { detail ->
                "${detail.adultos}"
            },
        )
    }

    fun getRecordId(id: Int, week: Int, year: Int){
        viewModelScope.launch {
            try {
                val result = repository.getRecordId(id, week, year)
                result.also { _recordId.value = it }
            }catch (e: Exception){
                _recordId.value = null
            }
        }
    }

    fun getCountRecords(date: String){
        viewModelScope.launch {
            try {
                val result = repository.getCountRecords(date)
                _countsRecords.value = result
            }catch (e:Exception){
                _countsRecords.value = null
            }
        }
    }

    fun getRecordsPending(){
        viewModelScope.launch {
            try {
                val result = repository.getCountRecordsPending()
                _recordsPending.value = result
            }catch (e: Exception){
                _recordsPending.value = null
            }
        }
    }

    fun getAllRecords(): LiveData<List<RecordsData>> {
        return liveData(Dispatchers.IO) {
            val records = repository.getAllRecords()
            emit(records)
        }
    }

    fun deleteAllRecords(){
        viewModelScope.launch {
            repository.deleteAllRecord()
        }
    }
}