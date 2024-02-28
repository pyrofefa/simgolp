package movil.siafeson.citricos.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import movil.siafeson.citricos.R
import movil.siafeson.citricos.app.MyApp
import movil.siafeson.citricos.app.accurracyAlloWed
import movil.siafeson.citricos.databinding.ActivityGolpeteoBinding
import movil.siafeson.citricos.db.entities.RecordEntity
import movil.siafeson.citricos.db.viewModels.RecordViewModel
import movil.siafeson.citricos.models.LocationData
import movil.siafeson.citricos.utils.ToolBarActivity
import movil.siafeson.citricos.utils.Utileria
import movil.siafeson.citricos.utils.fechaHoraCompleta
import movil.siafeson.citricos.utils.getAppVersionInfo
import movil.siafeson.citricos.utils.getFormattedDate
import movil.siafeson.citricos.utils.getFormattedDateTime
import movil.siafeson.citricos.utils.getWeek
import movil.siafeson.citricos.utils.getYear
import movil.siafeson.citricos.utils.parseFecha
import movil.siafeson.citricos.utils.showAlertDialog
import java.util.Calendar
import java.util.Date

class GolpeteoActivity : ToolBarActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var binding: ActivityGolpeteoBinding
    private var location: LocationData? = null
    private var isButtonEnabled = true

    private lateinit var recordViewModel: RecordViewModel

    private var currentLatitude:Double = 0.0
    private var currentLongitude:Double = 0.0
    private var currentAccuracy:Double = 0.0
    private var timeNetwork:String = ""
    private var distanceNetwork:Double = 0.0

    private var points:Int = 0

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
        binding.editTextNoAdults.setText(points.toString())
        toolbarToLoad(toolbar = binding.toolbar.toolbarGlobal, "Golpeteo")
        enableHomeDisplay(true)

        startLocationUpdates()

        recordViewModel = ViewModelProvider(this).get(RecordViewModel::class.java)


        binding.btnAdd.setOnClickListener {
            addDetail()
        }

    }

    private fun addDetail() {
        val versionInfo = getAppVersionInfo()
        val calendar = Calendar.getInstance()
        val year = calendar.getYear()
        val week = calendar.getWeek()
        val formattedDate = calendar.getFormattedDate()
        val formattedDateTime = calendar.getFormattedDateTime()

        val parseDate = parseFecha(timeNetwork)


        if (points == 0){
            val resourceProducer = binding.switchExample.isChecked
            Log.i("resourceProducer","${resourceProducer}")
            val record = RecordEntity(
                userId = MyApp.preferences.userId.toString(),
                fecha = formattedDate,
                fechaHora = formattedDateTime,
                latitud = currentLatitude,
                longitud =  currentLongitude,
                accuracy = currentAccuracy,
                recurso = 1,
                distanciaQr = distanceNetwork,
                campoId = location!!.id_bit,
                ano = year.toString(),
                semana = week.toString(),
                status = 2,
                idBdCel = 1,
                totalArboles = 1,
                totalAdultos = 0,
                created = formattedDateTime,
                createdSat = parseDate,
                modified = formattedDateTime,
                version = versionInfo.first
            )
            val muestreoId = recordViewModel.insertRecord(record)
            Log.i("muestreoId","${muestreoId}")

        }

        points++
        binding.textViewNoPoints.text = points.toString()
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

        currentLatitude = myLocation.latitude
        currentLongitude = myLocation.longitude
        currentAccuracy = myLocation.accuracy.toDouble()

        val satelliteTime = Date(myLocation.time)



        timeNetwork = satelliteTime.fechaHoraCompleta()
        binding.tvFecha.text = timeNetwork

        val field = Location("location field")
        field.latitude = location?.latitud ?: 0.0
        field.longitude = location?.longitud ?: 0.0

        distanceNetwork = field.distanceTo(myLocation).toDouble()
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