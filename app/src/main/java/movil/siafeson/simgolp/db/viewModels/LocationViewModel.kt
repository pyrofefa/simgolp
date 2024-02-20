package movil.siafeson.simgolp.db.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import movil.siafeson.simgolp.app.DatabaseSingleton
import movil.siafeson.simgolp.db.entities.LocationEntity
import movil.siafeson.simgolp.db.repositories.LocationRepository

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LocationRepository = LocationRepository(DatabaseSingleton.db.locationDao())

    init {
        Log.d("LocationViewModel", "ViewModel creado")
    }
    fun insertLocation(location: LocationEntity) {
        viewModelScope.launch {
            repository.insertLocation(location)
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