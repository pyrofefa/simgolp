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
import movil.siafeson.citricos.db.entities.LocationEntity
import movil.siafeson.citricos.db.repositories.DetailRepository
import movil.siafeson.citricos.models.DetailData

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DetailRepository = DetailRepository(DatabaseSingleton.db.detailDao())

    private val _listaDeItems = MutableLiveData<List<DetailData>>()
    val listaDeItems: LiveData<List<DetailData>> get() = _listaDeItems

    init {
        Log.i("DetailViewModel","ViewModel Creado")
    }

    fun insertDetail(detailEntity: DetailEntity){
        viewModelScope.launch {
            repository.insertDetail(detailEntity)
        }
    }
    fun getDetailsRecord(id: Int): LiveData<List<DetailEntity>> {
        return liveData(Dispatchers.IO) {
            val details = repository.getDetailsRecord(id)
            emit(details)
        }
    }
    fun deleteDetail(id: Int){
        viewModelScope.launch {
            repository.deleteDetail(id)
        }
    }
}