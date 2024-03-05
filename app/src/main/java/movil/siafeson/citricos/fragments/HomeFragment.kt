package movil.siafeson.citricos.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.*
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import movil.siafeson.citricos.R
import movil.siafeson.citricos.activities.GolpeteoActivity
import movil.siafeson.citricos.adapters.LocationListAdapter
import movil.siafeson.citricos.app.distanceAllowed
import movil.siafeson.citricos.databinding.FragmentHomeBinding
import movil.siafeson.citricos.db.entities.LocationEntity
import movil.siafeson.citricos.db.viewModels.LocationViewModel
import movil.siafeson.citricos.db.viewModels.RecordViewModel
import movil.siafeson.citricos.models.LocationData
import movil.siafeson.citricos.utils.Utileria
import movil.siafeson.citricos.utils.fechaCompleta
import movil.siafeson.citricos.utils.getFormattedDate
import movil.siafeson.citricos.utils.getWeek
import movil.siafeson.citricos.utils.getYear
import movil.siafeson.citricos.utils.nombreDiaActual
import java.util.Calendar

class HomeFragment : Fragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var binding: FragmentHomeBinding

    private lateinit var locationViewModel: LocationViewModel
    private var locationListAdapter: LocationListAdapter? = null
    private lateinit var recordViewModel: RecordViewModel

    private var isButtonEnabled = true

    private lateinit var mContext: Context

    private var currentLatitude:Double = 0.0
    private var currentLongitude:Double = 0.0

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationCallback = createLocationCallback()

        // Verifica y solicita permisos de ubicación si es necesario
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        } else {
            // Si ya tienes permisos, inicia la actualización de ubicación
            startLocationUpdates()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        val view = binding.root
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        recordViewModel = ViewModelProvider(this).get(RecordViewModel::class.java)
        countRecords()
        binding.textViewDay.text = Calendar.getInstance().nombreDiaActual()
        binding.textViewCompleteDay.text = Calendar.getInstance().fechaCompleta()
        return view
    }

    private fun countRecords() {
        val calendar = Calendar.getInstance()
        val formattedDate = calendar.getFormattedDate()
        recordViewModel.getCountRecords(formattedDate)
        recordViewModel.countRecords.observe(viewLifecycleOwner, Observer { res->
            binding.tvTotRegistros.text = res.toString()
        })
    }

    private fun startLocationUpdates() {
        val locationRequest = create().apply {
            interval = 10000 // Intervalo en milisegundos
            fastestInterval = 5000 // El intervalo más rápido para recibir actualizaciones
            priority = PRIORITY_HIGH_ACCURACY
        }

        // Inicia las actualizaciones de ubicación
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun createLocationCallback(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    //ubicación actualizada
                    updateLocation(location)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso concedido, iniciar la actualización de ubicación
                    startLocationUpdates()
                } else {
                    // Permiso denegado, manejar el caso en consecuencia
                    // Puedes mostrar un mensaje al usuario o deshabilitar la funcionalidad relacionada con la ubicación
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
    private fun updateLocation(location: Location) {
        //Log.i("Cambio-ubicacion","Se ha detectado un cambio en la ubicacion")
        // Implementa la lógica para actualizar la ubicación en tu fragmento
        binding.textViewLatitude.text = "${String.format("%.6f",location.latitude)}"
        binding.textViewLongitude.text = "${String.format("%.6f",location.longitude)}"
        binding.textViewAccuracy.text = "${String.format("%.2f",location.accuracy)}"
        currentLatitude = location.latitude
        currentLongitude = location.longitude
        loadListLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Detiene las actualizaciones de ubicación cuando el fragmento se destruye
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onDestroyView() {
        // Detiene el observable cuando el fragmento se destruye
        parentFragment?.let { locationViewModel.getAllLocations().removeObservers(it.viewLifecycleOwner) }
        super.onDestroyView()
    }
    override fun onPause() {
        super.onPause()
        // Detener las actualizaciones de ubicación cuando el fragmento entra en pausa
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        // Reiniciar las actualizaciones de ubicación cuando el fragmento vuelve a estar en primer plano
        startLocationUpdates()
        countRecords()
    }

    companion object {
        const val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }
    private fun loadListLocation() {
        locationViewModel.getAllLocations().observe(viewLifecycleOwner, Observer { locationEntityList ->
            val locationDataList = locationEntityList.map { locationEntity ->
                transformLocationEntityToLocationData(locationEntity)
            }
            //Log.i("Cambio-observador","Se ha detectado un cambio en el observador")
            try {
                val myLocation = Location("My Locations")
                myLocation.latitude = currentLatitude
                myLocation.longitude = currentLongitude
                locationDataList.forEach { field ->
                    val location = Location("Location Field")
                    location.latitude = field.latitud
                    location.longitude = field.longitud
                    val distance = myLocation.distanceTo(location)
                    if (distance <= distanceAllowed) {
                        // Formatea la cadena de distancia, reemplazando caracteres no deseados
                        val distanceString = String.format("%.3f", (distance / 1000))
                        val cleanedDistanceString = distanceString.replace(Regex("[^\\d.]"), "")
                        val km = cleanedDistanceString.toDouble()
                        val bear = myLocation.bearingTo(location)
                        val orientation = Utileria().getOrien(bear)

                        // Actualiza la ubicación original con la distancia y orientación
                        field.distancia = km
                        field.orientacion = orientation
                    }
                }
                // Ordena la lista por distancia de menor a mayor
                val sortedLocationList = locationDataList.sortedBy { it.distancia }

                // Inicializa el adaptador si aún no ha sido inicializado
                if (locationListAdapter == null) {
                    locationListAdapter = LocationListAdapter(mContext, R.layout.list_locations, sortedLocationList)
                    binding.DivLocationsNearBy.labelLocationsNearby.adapter = locationListAdapter
                } else {
                    // Si ya ha sido inicializado, notifica que los datos han cambiado
                    locationListAdapter?.updateData(sortedLocationList)
                }
                if (isButtonEnabled) {
                    bloqueaAccion()
                    binding.DivLocationsNearBy.labelLocationsNearby.setOnItemClickListener { parent, view, position, id ->
                        val selectedLocation = sortedLocationList[position]
                        val intent = Intent(mContext, GolpeteoActivity::class.java)
                        intent.putExtra("location", selectedLocation)
                        startActivity(intent)
                    }
                }

            }catch (e: Exception){
                Log.e("Ubicaciones cercanas", "Error: ${e.message}")
            }

            // Actualiza la interfaz de usuario según si la lista está vacía o no
            if (locationDataList.isEmpty()) {
                binding.DivLocationsNearBy.labelLocationsEmpty.visibility = View.VISIBLE
                binding.DivLocationsNearBy.ivLogoNoHay.visibility = View.VISIBLE
            } else {
                binding.DivLocationsNearBy.labelLocationsEmpty.visibility = View.INVISIBLE
                binding.DivLocationsNearBy.ivLogoNoHay.visibility = View.INVISIBLE
            }
        })
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

    //Bloquear Boton de acción
    private fun bloqueaAccion() {
        isButtonEnabled = false
        //Desactiva el botón durante 1 segundo para evitar doble clic
        Handler(Looper.getMainLooper()).postDelayed({
            isButtonEnabled = true
        }, 1000)
    }
}