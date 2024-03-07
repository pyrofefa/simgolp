import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import movil.siafeson.citricos.databinding.ListDetailsBinding
import movil.siafeson.citricos.db.viewModels.DetailViewModel
import movil.siafeson.citricos.interfaces.DetailDelete
import movil.siafeson.citricos.models.DetailData
import movil.siafeson.citricos.utils.showAlertDialog

class DetailsAdapter(
    private val context: Context,
    private val viewModel: DetailViewModel,
    private var data: List<DetailData>,
    private val onItemDeleteListener: DetailDelete
) : BaseAdapter() {

    private var dataEmpty: List<DetailData> = emptyList()

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
        val binding: ListDetailsBinding

        if (convertView == null) {
            binding = ListDetailsBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            binding = ListDetailsBinding.bind(convertView)
        }

        // Acceder directamente a las vistas utilizando el binding
        val item = getItem(position) as DetailData
        binding.editTextNoAdults.setText(item.id.toString())
        binding.btnEdit.setOnClickListener {
            // Lógica para editar el item (usando item.id)
            val noAdults = item.adultos.toString()
            if (noAdults.isEmpty()){

            }else{

            }
        }
        binding.btnDelete.setOnClickListener {
            showAlertDialog(
                "¡Atención!",
                "¿Estás seguro de que quieres eliminar este registro?",
                context,
                "Si",
                positiveButtonAction = {
                    viewModel.deleteDetail(item.id.toInt())
                    onItemDeleteListener.onItemDelete(item.id.toInt())
                },
                "No"
            )

        }

        return binding.root
    }
    fun updateData(newData: List<DetailData>) {
        data = newData
        Log.i("Entrando a updateData","${data}")

        notifyDataSetChanged()
    }
}

