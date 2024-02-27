package movil.siafeson.citricos.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import movil.siafeson.citricos.R
import movil.siafeson.citricos.databinding.CatalogsEntryBinding
import movil.siafeson.citricos.models.CatalogData

class CatalogsAdapter(
    private val context: Context,
    val layout: Int,
    private var data: ArrayList<CatalogData>
) : BaseAdapter() {

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: CatalogsEntryBinding
        if (convertView == null){
            binding = CatalogsEntryBinding.inflate(LayoutInflater.from(context),parent,false)
        } else {
            binding = CatalogsEntryBinding.bind(convertView)
        }
        binding.ceLblNombre.text = "${data[position].nombre}"
        binding.ceLblTotal.text = "${data[position].total}"
        binding.ceLblId.text = "${data[position].id}"

        when(data[position].id.toInt()){
            1 -> binding.ceIvIcon.setImageResource(R.drawable.campo)
            else -> binding.ceIvIcon.setImageResource(R.drawable.logo_app)
        }

        return binding.root
    }
}