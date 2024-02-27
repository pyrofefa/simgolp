package movil.siafeson.citricos.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.lifecycle.ViewModelProvider
import movil.siafeson.citricos.R
import movil.siafeson.citricos.adapters.LocationListAdapter
import movil.siafeson.citricos.databinding.ActivityLocationsBinding
import movil.siafeson.citricos.db.entities.LocationEntity
import movil.siafeson.citricos.db.viewModels.LocationViewModel
import movil.siafeson.citricos.models.LocationData
import movil.siafeson.citricos.utils.ToolBarActivity

class LocationsActivity : ToolBarActivity() {

    private lateinit var binding: ActivityLocationsBinding
    private lateinit var locationViewModel: LocationViewModel
    private var locationListAdapter: LocationListAdapter? = null
    private var size:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        binding = ActivityLocationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarToLoad(toolbar = binding.toolbar.toolbarGlobal, "Campos")
        enableHomeDisplay(true)
        val locations = locationViewModel.getAllLocations()
        locations.observe(this){list->
            size = list.size
            loadLocations(list)
        }
    }
    private fun loadLocations(list: List<LocationEntity>) {
        val locationDataList = list.map { locationEntity ->
            transformLocationEntityToLocationData(locationEntity)
        }
        locationListAdapter = LocationListAdapter(this, R.layout.list_locations,locationDataList)
        binding.catGvListCampos.adapter = locationListAdapter
        binding.tvNumCamposAsig.text = "${locationListAdapter?.count}"
        filter()
    }
    private fun transformLocationEntityToLocationData(locationEntity: LocationEntity): LocationData {
        return LocationData(
            id_bit = locationEntity.idBit,
            predio = locationEntity.predio,
            latitud = locationEntity.latitud,
            longitud = locationEntity.longitud,
            superficie = locationEntity.superficie,
            status = locationEntity.predio.toIntOrNull() ?: 1,
            id_sicafi = locationEntity.idSicafi,
            distancia = 0.0,
            orientacion = ""
        )
    }
    private fun filter() {
        binding.etSitiosBuscar.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                val query = p0.toString().trim()
                if (query.isEmpty()) {
                    // Si el texto está vacío, restaura la lista completa
                    locationListAdapter?.restoreOriginalList()
                    binding.tvNumCamposAsig.text = "${locationListAdapter?.count}"
                } else {
                    // Filtra la lista con el texto actual
                    locationListAdapter?.filter(query)
                    binding.tvNumCamposAsig.text = "${locationListAdapter?.count}"
                }
            }
        })
    }
    //Metodos para regresar
    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            super.onBackPressed()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}