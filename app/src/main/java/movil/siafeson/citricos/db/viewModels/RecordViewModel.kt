package movil.siafeson.citricos.db.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import movil.siafeson.citricos.app.DatabaseSingleton
import movil.siafeson.citricos.db.entities.RecordEntity
import movil.siafeson.citricos.db.repositories.RecordRepository

class RecordViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RecordRepository = RecordRepository(DatabaseSingleton.db.recordDao())

    private val _insertedRecordId = MutableLiveData<Long?>()
    val insertedRecordId: MutableLiveData<Long?>
        get() = _insertedRecordId

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

}