package movil.siafeson.citricos.activities

import DetailsAdapter
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.ListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import movil.siafeson.citricos.R
import movil.siafeson.citricos.databinding.ActivityDetailsBinding
import movil.siafeson.citricos.db.entities.DetailEntity
import movil.siafeson.citricos.db.viewModels.DetailViewModel
import movil.siafeson.citricos.interfaces.DetailDelete
import movil.siafeson.citricos.models.DetailData
import movil.siafeson.citricos.utils.ToolBarActivity

class DetailsActivity : ToolBarActivity(), DetailDelete {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarToLoad(toolbar = binding.toolbar.toolbarGlobal, "Detalle")
        enableHomeDisplay(true)

        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        listView = binding.divDetails

        getDetails()
    }

    private fun getDetails() {
        val recordId = intent.getLongExtra("muestreo_id", 0)
        detailViewModel.getDetailsRecord(recordId.toInt()).observe(this, Observer { detailsEntityList ->
            Log.i("detailsEntityList","${detailsEntityList}")
            Log.i("Cambio-observador","Se ha detectado un cambio en el observador")
            val detailsDataList = detailsEntityList.map {  detailsEntity ->
                transformData(detailsEntity)
            }

            // Configura el adaptador con los detalles de datos
            val adapter = DetailsAdapter(this,detailViewModel, detailsDataList,this)
            listView.adapter = adapter

            adapter.updateData(detailsDataList)
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

    override fun onItemDelete(itemId: Int) {
        Log.i("itemId:","${itemId}")
        detailViewModel.deleteDetail(itemId)
        getDetails()
    }
}

