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
import movil.siafeson.citricos.db.entities.RecordEntity
import movil.siafeson.citricos.db.repositories.RecordRepository
import movil.siafeson.citricos.models.RecordIdData

class RecordViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RecordRepository = RecordRepository(DatabaseSingleton.db.recordDao())

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

    fun getAllRecords(): LiveData<List<RecordEntity>> {
        return liveData(Dispatchers.IO) {
            val locations = repository.getAllRecords()
            emit(locations)
        }
    }

}