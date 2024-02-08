package movil.siafeson.simgolp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import movil.siafeson.simgolp.databinding.ActivityMainBinding
import movil.siafeson.simgolp.Fragments.CatalogsFragment
import movil.siafeson.simgolp.Fragments.HomeFragment
import movil.siafeson.simgolp.Fragments.ProfileFragment
import movil.siafeson.simgolp.Fragments.RegistersFragment
import movil.siafeson.simgolp.R

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewFragmentHome()

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