package movil.siafeson.simgolp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import movil.siafeson.simgolp.databinding.ListLocationsBinding
import movil.siafeson.simgolp.models.LocationData

class LocationListAdapter(
    private val context: Context,
    val layout: Int,
    private var locations: List<LocationData>
) : BaseAdapter()
     {
         private val originalLocationDataList: List<LocationData> = ArrayList(locations)

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
            binding.listLocationPosition.text =  "${this.locations[position].distancia} Km. | Dir: ${this.locations[position].orientacion}"
            binding.listLocationHa.text = this.locations[position].superficie.toString()
            return binding.root
        }
         fun updateData(newLocationDataList: List<LocationData>) {
             locations = newLocationDataList
             notifyDataSetChanged()
         }
         // Función para realizar el filtrado
         fun filter(query: String) {
             //Filtrando en base a la lista original
             val filteredList = originalLocationDataList.filter { locationData ->
                 // Aplica lógica de filtrado aquí
                 Log.d("Entrando a filter", "$locationData")
                 locationData.predio.contains(query, ignoreCase = true)
              }
             Log.d("filteredList", "$filteredList")

             // Actualiza la lista filtrada y notifica el cambio
             locations = filteredList
             notifyDataSetChanged()
             Log.d("LocationListAdapter", "Lista después del filtrado: $locations")

         }
         // Funcion para restablecer la lista original
         fun restoreOriginalList(){
             locations = ArrayList(originalLocationDataList)
             notifyDataSetChanged()
         }

    }