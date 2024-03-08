import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import movil.siafeson.citricos.databinding.ListDetailsBinding
import movil.siafeson.citricos.db.viewModels.DetailViewModel
import movil.siafeson.citricos.interfaces.DetailInterface
import movil.siafeson.citricos.models.DetailData
import movil.siafeson.citricos.utils.showAlertDialog
import movil.siafeson.citricos.utils.showToast

class DetailsAdapter(
    private val context: Context,
    private val viewModel: DetailViewModel,
    private var data: List<DetailData>,
    private val onItemListener: DetailInterface
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
        val binding: ListDetailsBinding

        if (convertView == null) {
            binding = ListDetailsBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            binding = ListDetailsBinding.bind(convertView)
        }

        // Acceder directamente a las vistas utilizando el binding
        val item = getItem(position) as DetailData
        binding.editTextNoAdults.setText(item.adultos.toString())

        //Poner el cursor al final del texto
        binding.editTextNoAdults.setSelection(item.adultos.toString().length)

        binding.btnEdit.setOnClickListener {
            // Lógica para editar el item (usando item.id)
            val noAdults = binding.editTextNoAdults.text.toString()
            // Mueve el cursor al final del texto
            if (noAdults.isEmpty()){
                showToast(context,"Debe ingresar número de adultos")
            }else{
                onItemListener.onItemEdit(item.id.toInt(), noAdults.toInt())
            }
        }
        binding.btnDelete.setOnClickListener {
            showAlertDialog(
                "¡Atención!",
                "¿Estás seguro de que quieres eliminar este registro?",
                context,
                "Si",
                positiveButtonAction = {
                    onItemListener.onItemDelete(item.id.toInt())
                },
                "No"
            )

        }

        return binding.root
    }
    fun updateData(newData: List<DetailData>) {
        data = newData
        notifyDataSetChanged()
    }
}

