package movil.siafeson.citricos.activities

import DetailsAdapter
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import movil.siafeson.citricos.R
import movil.siafeson.citricos.databinding.ActivityDetailsBinding
import movil.siafeson.citricos.db.entities.DetailEntity
import movil.siafeson.citricos.db.viewModels.DetailViewModel
import movil.siafeson.citricos.models.DetailData
import movil.siafeson.citricos.utils.ToolBarActivity

class DetailsActivity : ToolBarActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //toolbarToLoad(toolbar = binding.toolbar.toolbarGlobal, "Detalle")
        enableHomeDisplay(true)

        val listView = binding.divDetails
        val recordId = intent.getLongExtra("muestreo_id", 0)
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        detailViewModel.getDetailsRecord(recordId.toInt()).observe(this, Observer {
            detailsEntityList ->
            val detailsDataList = detailsEntityList.map {  detailsEntity ->
                transformData(detailsEntity)
            }

            // Configura el adaptador con los detalles de datos
            val adapter = DetailsAdapter(this, detailsDataList)
            listView.adapter = adapter
        })
    }

    private fun transformData(detailsEntity: DetailEntity): DetailData {
        return DetailData(
            id = detailsEntity.id,
            punto = detailsEntity.punto,
            longitud = detailsEntity.longitud,
            latitud = detailsEntity.latitud,
            accuracy = detailsEntity.accuracy,
            distanciaQr = detailsEntity.distanciaQr,
            status = detailsEntity.status,
            muestreoId = detailsEntity.muestreoId,
            adultos = detailsEntity.adultos,
            fecha = detailsEntity.fecha
        )
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