package movil.siafeson.citricos.db.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import movil.siafeson.citricos.app.DatabaseSingleton
import movil.siafeson.citricos.db.entities.RecordEntity
import movil.siafeson.citricos.db.repositories.RecordRepository

class RecordViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RecordRepository = RecordRepository(DatabaseSingleton.db.recordDao())

    init {
        Log.i("RecordViewModel", "ViewModel creado")
    }

    fun insertRecord(recordEntity: RecordEntity){
        viewModelScope.launch {
            repository.insertRecord(recordEntity)
        }
    }

}