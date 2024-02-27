package movil.siafeson.citricos.db.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import movil.siafeson.citricos.app.DatabaseSingleton
import movil.siafeson.citricos.db.entities.LocationEntity
import movil.siafeson.citricos.db.repositories.LocationRepository

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LocationRepository = LocationRepository(DatabaseSingleton.db.locationDao())

    init {
        Log.i("LocationViewModel", "ViewModel creado")
    }
    fun insertLocation(locationEntity: LocationEntity) {
        viewModelScope.launch {
            repository.insertLocation(locationEntity)
        }
    }
    fun getAllLocations(): LiveData<List<LocationEntity>> {
        return liveData(Dispatchers.IO) {
            val locations = repository.getAllLocations()
            emit(locations)
        }
    }
    fun deleteLocation(){
        viewModelScope.launch {
            repository.deleteLocations()
        }
    }
}