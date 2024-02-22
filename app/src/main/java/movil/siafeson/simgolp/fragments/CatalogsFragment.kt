package movil.siafeson.simgolp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import movil.siafeson.simgolp.R
import movil.siafeson.simgolp.adapters.CatalogsAdapter
import movil.siafeson.simgolp.adapters.LocationListAdapter
import movil.siafeson.simgolp.databinding.FragmentCatalogsBinding
import movil.siafeson.simgolp.db.viewModels.LocationViewModel
import movil.siafeson.simgolp.models.CatalogData
import movil.siafeson.simgolp.utils.isOnlineNet

class CatalogsFragment : Fragment() {
    private lateinit var mContext: Context
    private lateinit var binding: FragmentCatalogsBinding

    private lateinit var locationViewModel: LocationViewModel
    private var locationListAdapter: CatalogsAdapter? = null

    private val available = ArrayList<CatalogData>()
    private var size:Int = 0

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentCatalogsBinding.inflate(inflater,container,false)
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val locations = locationViewModel.getAllLocations()
        locations.observe(viewLifecycleOwner){list->
            size = list.size
            loadGrid()
        }
    }

    private fun loadGrid() {
        Log.i("locations 4","${size}")

        available.add(CatalogData(1,"Campos",size))
        locationListAdapter = CatalogsAdapter(mContext,R.layout.list_locations,available)
        binding.catGvList.adapter = locationListAdapter
    }

    //OPERATIVOS
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_catalogs, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.acti_catalogos_update -> {
                if (isOnlineNet(mContext)) {
                    //obtenerCatalogos()
                } else {

                }
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }



}