package movil.siafeson.simgolp.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import movil.siafeson.simgolp.databinding.ActivityMainBinding
import movil.siafeson.simgolp.fragments.CatalogsFragment
import movil.siafeson.simgolp.fragments.HomeFragment
import movil.siafeson.simgolp.fragments.ProfileFragment
import movil.siafeson.simgolp.fragments.RegistersFragment
import movil.siafeson.simgolp.R
import movil.siafeson.simgolp.app.RetrofitHelper
import movil.siafeson.simgolp.app.MyApp
import movil.siafeson.simgolp.db.AppDataBase
import movil.siafeson.simgolp.db.entities.LocationEntity
import movil.siafeson.simgolp.db.factories.LocationViewModelFactory
import movil.siafeson.simgolp.db.repositories.LocationRepository
import movil.siafeson.simgolp.db.viewModels.LocationViewModel
import movil.siafeson.simgolp.interfaces.APIService
import movil.siafeson.simgolp.requests.LocationsRequests
import movil.siafeson.simgolp.utils.ToolBarActivity
import movil.siafeson.simgolp.utils.showProgressDialog
import kotlin.concurrent.thread

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

}