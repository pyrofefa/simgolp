package movil.siafeson.simgolp.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import movil.siafeson.simgolp.R
import movil.siafeson.simgolp.app.accurracyAlloWed
import movil.siafeson.simgolp.databinding.ActivityGolpeteoBinding
import movil.siafeson.simgolp.models.LocationData
import movil.siafeson.simgolp.utils.ToolBarActivity
import movil.siafeson.simgolp.utils.Utileria
import movil.siafeson.simgolp.utils.fechaHoraCompleta
import movil.siafeson.simgolp.utils.showAlertDialog
import java.util.Date

class GolpeteoActivity : ToolBarActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var binding: ActivityGolpeteoBinding
    private var location: LocationData? = null
    private var isButtonEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@GolpeteoActivity)
        locationCallback = createLocationCallback()

        binding = ActivityGolpeteoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        location = intent.getParcelableExtra("location")
        binding.tvCampo.text = location?.predio
        binding.tvGeopos.text = "${location?.latitud}, ${location?.longitud}"
        toolbarToLoad(toolbar = binding.toolbar.toolbarGlobal, "Golpeteo")
        enableHomeDisplay(true)

        startLocationUpdates()
    }
    override fun onDestroy() {
        super.onDestroy()
        // Detiene las actualizaciones de ubicación cuando el fragmento se destruye
        fusedLocationClient.removeLocationUpdates(locationCallback)
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
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // Intervalo en milisegundos
            fastestInterval = 5000 // El intervalo más rápido para recibir actualizaciones
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        // Inicia las actualizaciones de ubicación
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }
    private fun updateLocation(myLocation: Location) {
        // Implementa la lógica para actualizar la ubicación en tu fragmento
        binding.tvPosLlat.text = "${String.format("%.6f",myLocation.latitude)}"
        binding.tvPosLon.text = "${String.format("%.6f",myLocation.longitude)}"
        binding.tvPosAcc.text = "${String.format("%.2f",myLocation.accuracy)}"
        val satelliteTime = Date(myLocation.time)

        val timeNetwork = satelliteTime.fechaHoraCompleta()
        binding.tvFecha.text = timeNetwork

        val field = Location("location field")
        field.latitude = location?.latitud ?: 0.0
        field.longitude = location?.longitud ?: 0.0

        val distanceNetwork = field.distanceTo(myLocation).toDouble()
        val bear = field.bearingTo(field)
        val orientation = Utileria().getOrien(bear)

        if (myLocation.accuracy <= accurracyAlloWed) {
            subTitulo(getString(R.string.form_lbl_info_listo))
        } else {
            subTitulo(getString(R.string.form_lbl_info_espera))
        }
        binding.tvDistancia.text = "${String.format("%.1f", distanceNetwork)} m. | Dir: $orientation"

    }
    override fun onSupportNavigateUp(): Boolean {
        showAlertDialog(
            "¡Atención!",
            "Dejará un registro pendiente de terminar, para el campo: ${location?.predio} ¿Desea continuar?",
            this,
            "Si",
            positiveButtonAction = {
                onBackPressed()
            },
            "Cancelar"
        )
        return true
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showAlertDialog(
                "¡Atención!",
                "Dejará un registro pendiente de terminar, para el campo: ${location?.predio} ¿Desea continuar?",
                this,
                "Si",
                positiveButtonAction = {
                    onBackPressed()
                },
                "Cancelar"
            )
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    //Bloquear Boton de acción
    private fun bloqueaAccion() {
        isButtonEnabled = false
        //Desactiva el botón durante 1 segundo para evitar doble clic
        Handler(Looper.getMainLooper()).postDelayed({
            isButtonEnabled = true
        }, 700)
    }
}