package movil.siafeson.simgolp.activities

import android.os.Bundle
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import movil.siafeson.simgolp.databinding.ActivityMainBinding
import movil.siafeson.simgolp.fragments.CatalogsFragment
import movil.siafeson.simgolp.fragments.HomeFragment
import movil.siafeson.simgolp.fragments.ProfileFragment
import movil.siafeson.simgolp.fragments.RegistersFragment
import movil.siafeson.simgolp.R
import movil.siafeson.simgolp.app.RetrofitHelper
import movil.siafeson.simgolp.app.SharedApp
import movil.siafeson.simgolp.interfaces.APIService
import movil.siafeson.simgolp.utils.ToolBarActivity

class MainActivity : ToolBarActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbarToLoad(toolbar = binding.toolbar.toolbarGlobal)

        viewFragmentHome()
        loadLocations()

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
        val userId = SharedApp.preferences.userId
        CoroutineScope(Dispatchers.IO).launch {
            var call = RetrofitHelper
                .getInstance()
                .create(APIService::class.java)
                .getLocations("simgolp/ubicaciones/${userId}")
            runOnUiThread {
                if (call.status == "success"){
                    Toast.makeText(this@MainActivity,"${call.message}", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@MainActivity,"${call.message}",Toast.LENGTH_SHORT).show()
                }
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