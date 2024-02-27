package movil.siafeson.citricos.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import movil.siafeson.citricos.databinding.ActivityMainBinding
import movil.siafeson.citricos.fragments.CatalogsFragment
import movil.siafeson.citricos.fragments.HomeFragment
import movil.siafeson.citricos.fragments.ProfileFragment
import movil.siafeson.citricos.fragments.RegistersFragment
import movil.siafeson.citricos.R
import movil.siafeson.citricos.app.MyApp
import movil.siafeson.citricos.db.viewModels.LocationViewModel
import movil.siafeson.citricos.requests.LocationsRequests
import movil.siafeson.citricos.utils.ToolBarActivity
import movil.siafeson.citricos.utils.showAlertDialog
import movil.siafeson.citricos.utils.showProgressDialog

class MainActivity : ToolBarActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var locationViewModel: LocationViewModel

    private var currentProgress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)
        toolbarToLoad(toolbar = binding.toolbar.toolbarGlobal)

        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)

        viewFragmentHome()
        if (!MyApp.preferences.locationUpdate) {
            loadLocations()
        }
        binding.ButtonNavigationView.setItemIconTintList(null)
        binding.ButtonNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.item_home ->{
                    viewFragmentHome()
                    true
                }
                R.id.item_registers ->{
                    viewFragmentRegister()
                    true
                }
                R.id.item_catalogs ->{
                    viewFragmentCatalogs()
                    true
                }
                R.id.item_profile ->{
                    viewFragmentProfile()
                    true
                }
                else ->{
                    false
                }
            }
        }

    }

    private fun loadLocations() {
        progressDialog = showProgressDialog(
            "Obteniendo catálogos",
            "",
            this,
            ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.show()
        progressDialog.progress = currentProgress
        progressDialog.max = 1

        CoroutineScope(Dispatchers.IO).launch {
            getLocations()
        }
    }

    private fun getLocations() {
        lifecycleScope.async {
            try {
                val resp = LocationsRequests().locations()
                Thread(Runnable {
                    currentProgress++
                    if(progressDialog.max >= currentProgress) {
                        progressDialog.progress = currentProgress
                        resp.forEach {
                            locationViewModel.insertLocation(it)
                        }
                    }
                    try {
                        Thread.sleep(600)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    if (currentProgress >= progressDialog.max) {
                            progressDialog.dismiss()
                            MyApp.preferences.locationUpdate = true
                    }
                }).start()
            }catch (e:Exception){
                progressDialog.dismiss()
                Toast.makeText(this@MainActivity,"${e.message}",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun viewFragmentProfile() {
        val fragment = ProfileFragment()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentLayout1.id, fragment, "profileFragment")
        fragmentTransition.commit()
    }

    private fun viewFragmentCatalogs() {
        val fragment = CatalogsFragment()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentLayout1.id, fragment, "catalogsFragment")
        fragmentTransition.commit()
    }

    private fun viewFragmentRegister() {
        val fragment = RegistersFragment()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentLayout1.id, fragment, "registerFragment")
        fragmentTransition.commit()
    }

    private fun viewFragmentHome() {
        val fragment = HomeFragment()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentLayout1.id, fragment, "homeFragment")
        fragmentTransition.commit()
    }

    //Metodos para regresar
    override fun onSupportNavigateUp(): Boolean {
        showAlertDialog(
            "Salir",
            "¿Estás seguro de salir de la aplicación?",
            this,
            "Si",
            positiveButtonAction = {
                this.finish()
            },
            "Cancelar"
        )
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showAlertDialog(
                "Salir",
                "¿Estás seguro de salir de la aplicación?",
                this,
                "Si",
                positiveButtonAction = {
                    this.finish()
                },
                "Cancelar"
            )
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}