package movil.siafeson.simgolp.db.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import movil.siafeson.simgolp.db.repositories.LocationRepository
import movil.siafeson.simgolp.db.viewModels.LocationViewModel
import java.lang.IllegalArgumentException

class LocationViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    /*override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocationViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }*/
}