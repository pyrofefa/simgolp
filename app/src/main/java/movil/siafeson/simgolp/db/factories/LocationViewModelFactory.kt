package movil.siafeson.simgolp.db.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import movil.siafeson.simgolp.db.repositories.LocationRepository
import movil.siafeson.simgolp.db.viewModels.LocationViewModel
import java.lang.IllegalArgumentException

class LocationViewModelFactory(private val repository: LocationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return LocationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}