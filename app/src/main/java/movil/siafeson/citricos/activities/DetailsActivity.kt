package movil.siafeson.citricos.activities

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.lifecycle.ViewModelProvider
import movil.siafeson.citricos.databinding.ActivityDetailsBinding
import movil.siafeson.citricos.db.viewModels.DetailViewModel
import movil.siafeson.citricos.utils.ToolBarActivity

class DetailsActivity : ToolBarActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val intent = intent
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbarToLoad(toolbar = binding.toolbar.toolbarGlobal, "Detalle")
        enableHomeDisplay(true)

        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        val recordId = intent.getLongExtra("muestreo_id", 0)

        getDetails(recordId)

    }

    private fun getDetails(recordId: Long) {
        detailViewModel.getDetailsRecord(recordId.toInt())
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