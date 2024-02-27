package movil.siafeson.citricos.db.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import movil.siafeson.citricos.app.DatabaseSingleton
import movil.siafeson.citricos.db.entities.DetailEntity
import movil.siafeson.citricos.db.repositories.DetailRepository

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DetailRepository = DetailRepository(DatabaseSingleton.db.detailDao())

    init {
        Log.i("DetailViewModel","ViewModel Creado")
    }

    fun insertDetail(detailEntity: DetailEntity){
        viewModelScope.launch {
            repository.insertDetail(detailEntity)
        }
    }
}