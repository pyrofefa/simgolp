package movil.siafeson.citricos.activities

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import movil.siafeson.citricos.R
import movil.siafeson.citricos.app.MyApp
import movil.siafeson.citricos.app.accurracyAlloWed
import movil.siafeson.citricos.databinding.ActivityGolpeteoBinding
import movil.siafeson.citricos.db.entities.DetailEntity
import movil.siafeson.citricos.db.entities.RecordEntity
import movil.siafeson.citricos.db.viewModels.DetailViewModel
import movil.siafeson.citricos.db.viewModels.RecordViewModel
import movil.siafeson.citricos.models.LocationData
import movil.siafeson.citricos.utils.Result
import movil.siafeson.citricos.utils.ToolBarActivity
import movil.siafeson.citricos.utils.Utileria
import movil.siafeson.citricos.utils.calculatePointsRequired
import movil.siafeson.citricos.utils.fechaHoraCompleta
import movil.siafeson.citricos.utils.getAppVersionInfo
import movil.siafeson.citricos.utils.getFormattedDate
import movil.siafeson.citricos.utils.getFormattedDateTime
import movil.siafeson.citricos.utils.getWeek
import movil.siafeson.citricos.utils.getYear
import movil.siafeson.citricos.utils.parseFecha
import movil.siafeson.citricos.utils.showAlertDialog
import movil.siafeson.citricos.utils.showProgressDialog
import movil.siafeson.citricos.utils.showToast
import java.util.Calendar
import java.util.Date

class GolpeteoActivity : ToolBarActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var binding: ActivityGolpeteoBinding
    private var location: LocationData? = null
    private var isButtonEnabled = true

    private lateinit var progressDialog: ProgressDialog

    private lateinit var recordViewModel: RecordViewModel
    private lateinit var detailViewModel: DetailViewModel

    private var currentLatitude:Double = 0.0
    private var currentLongitude:Double = 0.0
    private var currentAccuracy:Double = 0.0
    private var timeNetwork:String = ""
    private var distanceNetwork:Double = 0.0

    private var points:Int = 0
    private var pointsRequired:Int = 0
    private var recordId: Long? = null
    private var parseResourceProducer:Int = 0

    private var noAdults = 0

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
        binding.editTextNoAdults.setText("0")
        toolbarToLoad(toolbar = binding.toolbar.toolbarGlobal, "Golpeteo")
        enableHomeDisplay(true)

        progressDialog = ProgressDialog(this)

        validationHas()
        startLocationUpdates()

        recordViewModel = ViewModelProvider(this).get(RecordViewModel::class.java)
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        getRecordId()

        if (location?.superficie?.toInt() != 0){
            if (points >= pointsRequired){
                binding.btnSave.isEnabled = true
                binding.btnAdd.isEnabled = false
                binding.labelHas.text = "Llegó al límite de puntos"
            }
        }

        if(points == 0){
            binding.btnView.isEnabled = false
        }

        //Agregar punto
        binding.btnAdd.setOnClickListener {
            val numberAdults = binding.editTextNoAdults.text.toString()
            if(recordId != null){
                if (currentAccuracy > accurracyAlloWed){
                    showToast(this,"La precisión debe de ser menor a ${accurracyAlloWed.toInt()} para poder guardar el registro")
                }else if (numberAdults.isEmpty()){
                    showToast(this,"Debe ingresar número de adultos")
                }else{
                    addDetail(recordId!!)
                }
            }
            else{
                if (currentAccuracy > accurracyAlloWed){
                    showToast(this,"La precisión debe de ser menor a ${accurracyAlloWed.toInt()} para poder guardar el registro")
                }else if (numberAdults.isEmpty()){
                    showToast(this,"Debe ingresar número de adultos")
                }
                else{
                    addRecord()
                }
            }
        }
        binding.btnView.setOnClickListener {
            val intent = Intent(this@GolpeteoActivity, DetailsActivity::class.java)
            intent.putExtra("muestreo_id", recordId)
            startActivity(intent)
        }

        binding.btnSave.setOnClickListener {
            progressDialog = showProgressDialog(
                "Guardando",
                "",
                this,
                ProgressDialog.STYLE_HORIZONTAL

            )
            progressDialog.max = 2
            progressDialog.show()


            lifecycleScope.launch {
                recordViewModel.fetchRecord(recordId!!.toInt())
                progressDialog.setMessage("Conectando con el servidor")
                progressDialog.progress = 1
                recordViewModel.responseLiveData.observe(this@GolpeteoActivity, Observer {
                    response ->
                    Log.i("Respuesta:", "$response")
                    if(response != null) {
                        if (response!!.status == "success"){
                            progressDialog.setMessage("Actualizando registro")
                            progressDialog.progress = 2
                            updateRecordStatus(1, message = response.message)

                        }else{
                            progressDialog.setMessage("Actualizando registro")
                            progressDialog.progress = 2
                            updateRecordStatus(2, message = response.message)
                        }
                    }else{
                        progressDialog.setMessage("Actualizando registro")
                        progressDialog.progress = 2
                        updateRecordStatus(2, message = "Registro guardado localmente")
                    }
                })
            }
        }
        // Variable para controlar si el contenido ya se borró al obtener el foco
        val contentDelete = false
        val editText = binding.editTextNoAdults
        editText.setOnTouchListener{ _, event ->
            if (event.action == MotionEvent.ACTION_UP){
                // Obtén el valor actual del EditText
                val valueText = editText.text.toString()
                // Borra el contenido si es "0" y aún no se ha borrado
                if (valueText == "0" && !contentDelete){
                    editText.text.clear()
                }
            }
            false
        }
    }

    private fun updateRecordStatus(status: Int, message: String) {
        lifecycleScope.launch {
            try {
                when(val result = recordViewModel.updateRecord(status, recordId!!.toInt())){
                    is Result.Success -> {
                        val updateRecordId = result.data
                        if (updateRecordId != null) {
                            progressDialog.dismiss()
                            showToast(this@GolpeteoActivity,"${message}")
                            startActivity(Intent(this@GolpeteoActivity, MainActivity::class.java))
                            finishAffinity()
                        } else {
                            showToast(this@GolpeteoActivity,"Ocurrió un error al actualizar registro")
                        }
                    }
                    is Result.Error ->{
                        val exception = result.exception
                        showToast(this@GolpeteoActivity,"Ocurrió un error al actualizar registro: $exception")
                    }
                }
            }catch (e: Exception) {
                showToast(this@GolpeteoActivity,"Ocurrió un error al actualizar registro: ${e.message}")
            }
        }
    }

    private fun getRecordId() {
        val calendar = Calendar.getInstance()
        val year = calendar.getYear()
        val week = calendar.getWeek()
        recordViewModel.getRecordId(location!!.id_bit,week,year)
        recordViewModel.recordId.observe(this, Observer { res ->
            res?.map { detail ->
                if (detail.id > 0){
                    recordId = detail.id.toLong()
                    points = detail.punto
                    noAdults = detail.totalAdultos
                    binding.textViewNoPoints.text = points.toString()
                    binding.textViewNoAdults.text = noAdults.toString()
                    if (detail.recurso == 1){
                        binding.switchResource.isChecked = true
                    }
                    if (location?.superficie?.toInt() != 0) {
                        if (points >= pointsRequired) {
                            binding.btnSave.isEnabled = true
                            binding.btnAdd.isEnabled = false
                            binding.labelHas.text = "Llegó al límite de puntos"
                        }
                        binding.btnView.isEnabled = points != 0
                    }
                    else{
                        binding.labelHas.text = ""
                        binding.btnView.isEnabled = true
                        binding.btnSave.isEnabled = true
                    }
                }
            }
        })
    }

    private fun validationHas() {
        val ha = location?.superficie?.toInt()
        pointsRequired = ha?.let { calculatePointsRequired(it)} ?: 0

        Log.i("Puntos:", "$points")
        Log.i("Puntos Requeridos:", "$pointsRequired")

        Log.i("Superficie","${ha}")

        if(ha != 0) {
            if (points >= pointsRequired) {
                binding.btnSave.isEnabled = true
                binding.btnAdd.isEnabled = false
                binding.labelHas.text = "Llegó al límite de puntos"
            } else {
                binding.btnSave.isEnabled = false
                binding.btnAdd.isEnabled = true
                binding.labelHas.text =
                    "-Necesita completar un mínimo de: ${pointsRequired} puntos para finalizar-"
            }
        }
    }

    private fun addRecord() {
        val versionInfo = getAppVersionInfo()
        val calendar = Calendar.getInstance()
        val year = calendar.getYear()
        val week = calendar.getWeek()
        val formattedDate = calendar.getFormattedDate()
        val formattedDateTime = calendar.getFormattedDateTime()

        val parseDate = parseFecha(timeNetwork)
        val resourceProducer = binding.switchResource.isChecked

        if(resourceProducer){
            parseResourceProducer = 1
        }else{
            parseResourceProducer = 0
        }

        val numberAdults = binding.editTextNoAdults.text.toString()
        noAdults += numberAdults.toInt()

        val record = RecordEntity(
            userId = MyApp.preferences.userId.toString(),
            fecha = formattedDate,
            fechaHora = formattedDateTime,
            latitud = currentLatitude,
            longitud =  currentLongitude,
            accuracy = currentAccuracy,
            recurso = parseResourceProducer,
            distanciaQr = distanceNetwork,
            campoId = location!!.id_bit,
            ano = year.toString(),
            semana = week.toString(),
            status = 3,
            totalArboles = 1,
            totalAdultos = numberAdults.toInt(),
            created = formattedDateTime,
            createdSat = parseDate,
            modified = formattedDateTime,
            version = versionInfo.first
        )
        recordViewModel.insertRecord(record)
        recordViewModel.insertedRecordId.observe(this, Observer { res ->
            if (res != null) {
                recordId = res
                addDetail(recordId!!)
            } else {
                showToast(this, "Ocurrió un error al agregar punto")
            }
        })
        binding.textViewNoPoints.text = points.toString()
        binding.textViewNoAdults.text = noAdults.toString()

        if(location?.superficie?.toInt() != 0){
            if (points == pointsRequired){
                binding.btnAdd.isEnabled = false
                binding.labelHas.text = "Llegó al límite de puntos"
            }
        }
    }

    private fun addDetail(id: Long) {
        val calendar = Calendar.getInstance()
        val formattedDateTime = calendar.getFormattedDateTime()
        val numberAdults = binding.editTextNoAdults.text.toString()
        points++

        val detail = DetailEntity(
            punto = points,
            longitud = currentLongitude,
            latitud = currentLatitude,
            accuracy = currentAccuracy,
            distanciaQr = distanceNetwork,
            status = 3,
            muestreoId = id.toInt(),
            adultos = numberAdults.toInt(),
            fecha = formattedDateTime
        )
        detailViewModel.insertDetail(detail)
        binding.textViewNoPoints.text = points.toString()
        binding.editTextNoAdults.setText("0")

        if(location?.superficie?.toInt() != 0){
            if (points >= pointsRequired){
                binding.btnSave.isEnabled = true
                binding.btnAdd.isEnabled = false
                binding.labelHas.text = "Llegó al límite de puntos"
            }
        }

        binding.btnView.isEnabled = true
        noAdults += numberAdults.toInt()
        binding.textViewNoAdults.text = noAdults.toString()
        updateTotals(id.toInt())

    }

    private fun updateTotals(id: Int) {
        lifecycleScope.launch {
            recordViewModel.updateRecordTotals(id, noAdults, points)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Detiene las actualizaciones de ubicación cuando el fragmento se destruye
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
    override fun onResume() {
        super.onResume()
        getRecordId()
        validationHas()
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
        // Implementa la lógica para actualizar la ubicación en el fragmento
        binding.tvPosLlat.text = "${String.format("%.6f", myLocation.latitude)}"
        binding.tvPosLon.text = "${String.format("%.6f", myLocation.longitude)}"
        binding.tvPosAcc.text = "${String.format("%.2f", myLocation.accuracy)}"

        currentLatitude = myLocation.latitude
        currentLongitude = myLocation.longitude
        currentAccuracy = myLocation.accuracy.toDouble()

        val satelliteTime = Date(myLocation.time)

        timeNetwork = satelliteTime.fechaHoraCompleta()
        binding.tvFecha.text = timeNetwork

        val field = Location("location field")

        field.latitude = location?.latitud ?: 0.0
        field.longitude = location?.longitud ?: 0.0

        Log.i("field.latitude","${field.latitude}")
        Log.i("field.longitude","${field.longitude}")

        distanceNetwork = field.distanceTo(myLocation).toDouble()
        val bear = field.bearingTo(myLocation)
        val orientation = Utileria().getOrien(bear)

        if (myLocation.accuracy <= accurracyAlloWed) {
            subTitulo(getString(R.string.form_lbl_info_listo))
        } else {
            subTitulo(getString(R.string.form_lbl_info_espera))
        }
        binding.tvDistancia.text = "${String.format("%.1f", distanceNetwork)} m. | Dir: $orientation"
    }
    override fun onSupportNavigateUp(): Boolean {
        if (points > 0) {
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
        }else{
            onBackPressed()
        }
        return true
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (points > 0) {
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