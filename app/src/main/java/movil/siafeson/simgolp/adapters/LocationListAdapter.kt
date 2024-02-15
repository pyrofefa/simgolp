package movil.siafeson.simgolp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import movil.siafeson.simgolp.R
import movil.siafeson.simgolp.databinding.ListLocationsBinding
import movil.siafeson.simgolp.db.entities.LocationEntity
import movil.siafeson.simgolp.models.LocationData

class LocationListAdapter(
    private val context: Context,
    val layout: Int,
    private val locations: List<LocationData>
) : BaseAdapter()
     {
         override fun getCount(): Int {
             return locations.size
         }

         override fun getItem(position: Int): Any {
             return locations[position]
         }

         override fun getItemId(position: Int): Long {
             return position.toLong()
         }

         override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val binding: ListLocationsBinding

            if (convertView == null) {
                binding = ListLocationsBinding.inflate(LayoutInflater.from(context), parent, false)
            } else {
                binding = ListLocationsBinding.bind(convertView)
            }

            binding.listLocationName.text = this.locations[position].predio ?: "N/A"
            binding.listLocationPosition.text =  "${this.locations[position].distancia} Km. | Dir: ${this.locations[position].cardinal}"
            binding.listLocationHa.text = this.locations[position].superficie
        return binding.root
    }
}