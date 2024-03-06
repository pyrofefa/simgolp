import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import movil.siafeson.citricos.R
import movil.siafeson.citricos.models.DetailData

class DetailsAdapter(
    private val context: Context,
    private val data: List<DetailData>
) : BaseAdapter() {

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            // Inflar la vista si es nula
            view = LayoutInflater.from(context).inflate(R.layout.list_details, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            // Reutilizar la vista existente
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        // Enlazar los datos al ViewHolder
        val item = getItem(position) as DetailData
        viewHolder.bind(item)

        return view
    }

    // ViewHolder para mantener las referencias de las vistas
    private class ViewHolder(view: View) {
        // Define las vistas de tu elemento de lista aquí
        // Ejemplo: val textViewName = view.findViewById<TextView>(R.id.textViewName)

        fun bind(item: DetailData) {
            // Enlaza los datos a las vistas
            // Ejemplo: textViewName.text = item.name
        }
    }
}
