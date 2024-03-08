package movil.siafeson.citricos.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import movil.siafeson.citricos.R
import movil.siafeson.citricos.databinding.ListRecordsBinding
import movil.siafeson.citricos.models.RecordsData
import movil.siafeson.citricos.utils.getFormattedDateTime
import java.text.SimpleDateFormat
import java.util.Calendar

class RecordsAdapter(
    private val context: Context,
    val layout: Int,
    private var data: List<RecordsData>,
) : BaseAdapter() {

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(p0: Int): Any {
        return data[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val binding: ListRecordsBinding

        if (p1 == null){
            binding = ListRecordsBinding.inflate(LayoutInflater.from(context), p2, false)
        }else {
            binding = ListRecordsBinding.bind(p1)
        }
        binding.listRegName.text = this.data[p0].predio

        val calendar: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        calendar.setTime(sdf.parse("${data[p0].fecha}"))
        binding.listRegCreacion.text = "${calendar.getFormattedDateTime()}"

        binding.listRegId.text = "${this.data[p0].id}"

        if (this.data[p0].status == 1) {
            binding.listRegStatus.text = "Enviado"
            binding.listRegStatus.setTextColor(context.resources.getColor(R.color.enviado))
        } else if (this.data[p0].status == 2) {
            binding.listRegStatus.text = "Sin enviar"
            binding.listRegStatus.setTextColor(context.resources.getColor(R.color.porenviar))
        } else if (this.data[p0].status == 3) {
            binding.listRegStatus.text = "Sin terminar"
            binding.listRegStatus.setTextColor(context.resources.getColor(R.color.pendiente))
        }

        return binding.root
    }

}