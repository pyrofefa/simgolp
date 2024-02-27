package movil.siafeson.citricos.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import movil.siafeson.citricos.R
import movil.siafeson.citricos.activities.LocationsActivity
import movil.siafeson.citricos.adapters.CatalogsAdapter
import movil.siafeson.citricos.databinding.FragmentCatalogsBinding
import movil.siafeson.citricos.db.viewModels.LocationViewModel
import movil.siafeson.citricos.models.CatalogData
import movil.siafeson.citricos.utils.goToActivity
import movil.siafeson.citricos.utils.isOnlineNet

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

        binding.catGvList.setOnItemClickListener { _, view, _, _ ->
            val tvID = view.findViewById<TextView>(R.id.ce_lbl_id)
            when (tvID.text.toString().toInt()) {
                1 -> goToActivity<LocationsActivity> { }
            }
        }
    }

    private fun loadGrid() {
        available.add(CatalogData(1,"Campos",size))
        locationListAdapter = CatalogsAdapter(mContext,R.layout.list_locations,available)
        binding.catGvList.adapter = locationListAdapter

        binding.lblNumCatalogosDisponibles.text = available.size.toString()
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