package movil.siafeson.simgolp.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.*
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import movil.siafeson.simgolp.R
import movil.siafeson.simgolp.adapters.LocationListAdapter
import movil.siafeson.simgolp.databinding.FragmentHomeBinding
import movil.siafeson.simgolp.db.entities.LocationEntity
import movil.siafeson.simgolp.db.viewModels.LocationViewModel
import movil.siafeson.simgolp.models.LocationData
import movil.siafeson.simgolp.utils.fechaCompleta
import movil.siafeson.simgolp.utils.nombreDiaActual
import movil.siafeson.simgolp.utils.alertDialog
import java.util.Calendar

class HomeFragment : Fragment() {
    private lateinit var locationViewModel: LocationViewModel

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mContext: Context

    private val CODE_PERMISSION_LOCATION = 2106
    private var hasGratedPermission = false

    // NUEVO MANEJO DE COORDENADAS
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private lateinit var locationListAdapter: LocationListAdapter
    private val locationList = mutableListOf<LocationData>()

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
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
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDate()
        checkPermissions()
        loadListLocation()
    }

    private fun loadListLocation() {
        locationViewModel.getAllLocations().observe(viewLifecycleOwner, Observer { locationEntityList ->
            val locationDataList = locationEntityList.map { locationEntity ->
                transformLocationEntityToLocationData(locationEntity)
            }

            locationList.clear()
            locationList.addAll(locationDataList)

            locationListAdapter = LocationListAdapter(mContext,R.layout.list_locations,locationList)
            binding.DivLocationsNearBy.labelLocationsNearby.adapter = locationListAdapter

            if (locationList!!.isEmpty()) {
                binding.DivLocationsNearBy.labelLocationsEmpty.visibility = View.VISIBLE
                binding.DivLocationsNearBy.ivLogoNoHay.visibility = View.VISIBLE
            } else {
                binding.DivLocationsNearBy.labelLocationsEmpty.visibility = View.INVISIBLE
                binding.DivLocationsNearBy.ivLogoNoHay.visibility = View.INVISIBLE
            }
        })
    }

    private fun transformLocationEntityToLocationData(locationEntity: LocationEntity) : LocationData {
        return LocationData(
            id_bit = locationEntity.idBit,
            predio = locationEntity.predio,
            latitud = locationEntity.longitud,
            longitud = locationEntity.latitud,
            superficie =  locationEntity.superficie,
            status = locationEntity.predio.toIntOrNull() ?: 1,
            id_sicafi = locationEntity.idSicafi
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CODE_PERMISSION_LOCATION){
            val allPermisions = grantResults.all {
                it == PackageManager.PERMISSION_GRANTED
            }
            if (grantResults.isNotEmpty() && allPermisions){
                hasGratedPermission = true
                onPermissions()
            }
            else{
                alertDialog(
                    "Permisos denegados",
                    Html.fromHtml("Debe otorgar permisos manualmente desde:<br>" +
                            "<small><b>Ajustes/Aplicaciones/Simgolp/Permisos</small></b><br>" +
                            "<br> * <small><b>Atención:</b> Él acceso a permisos puede cambiar depediendo del equipo" +
                            "</small>").toString(),
                    "Entendido",
                    mContext)
            }
        }
    }
    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permission = arrayListOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            val permissionsOfArray = permission.toTypedArray()
            if (hasPermissions(permissionsOfArray)) {
                hasGratedPermission = true
                onPermissions()
            } else {
                requestPermissions(permissionsOfArray)
            }
        } else {
            hasGratedPermission = true
            onPermissions()
        }
    }
    private fun requestPermissions(permissionsOfArray: Array<String>) {
        requestPermissions(permissionsOfArray,CODE_PERMISSION_LOCATION)
    }
    private fun onPermissions() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        try {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                if (it != null){
                    getLocations(it)
                }else{
                    alertDialog(
                        "GPS Inactivo",
                        "Es necesario activar el sensor GPS",
                        "Entendido",
                        mContext
                    )
                }
            }
            val locationRequest = create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = PRIORITY_HIGH_ACCURACY
            }
            locationCallback = object : LocationCallback(){
                override fun onLocationResult(p0: LocationResult) {
                    p0?: return
                    for (location in p0.locations){
                        getLocations(location)
                    }
                }
            }
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }catch (e: SecurityException) {
            Log.d("", "Tal vez no solicitaste permiso antes")
        }
    }
    private fun hasPermissions(permissionsOfArray: Array<String>): Boolean {
        return permissionsOfArray.all {
            return ContextCompat.checkSelfPermission(
                mContext, it
            )== PackageManager.PERMISSION_GRANTED
        }
    }

    private fun getLocations(location: Location) {
        binding.textViewLatitude.text = "${String.format("%.6f",location.latitude)}"
        binding.textViewLongitude.text = "${String.format("%.6f",location.longitude)}"
        binding.textViewAccuracy.text = "${String.format("%.2f",location.accuracy)}"

    }

    private fun getDate() {
        binding.textViewDay.text = Calendar.getInstance().nombreDiaActual()
        binding.textViewCompleteDay.text = Calendar.getInstance().fechaCompleta()
    }
}