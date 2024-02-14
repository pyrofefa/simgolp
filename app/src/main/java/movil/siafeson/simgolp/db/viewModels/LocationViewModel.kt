package movil.siafeson.simgolp.db.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import movil.siafeson.simgolp.db.entities.LocationEntity
import movil.siafeson.simgolp.db.repositories.LocationRepository

class LocationViewModel(private val repository: LocationRepository) : ViewModel() {
    private val _locations = MutableLiveData<List<LocationEntity>>()
    val locations: LiveData<List<LocationEntity>> get() = _locations

    fun insertLocation(location: LocationEntity){
        viewModelScope.launch {
            repository.insertLocation(location)
        }
    }
}