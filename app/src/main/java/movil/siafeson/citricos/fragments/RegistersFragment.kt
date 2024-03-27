package movil.siafeson.citricos.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import movil.siafeson.citricos.R
import movil.siafeson.citricos.adapters.RecordsAdapter
import movil.siafeson.citricos.databinding.FragmentRegistersBinding
import movil.siafeson.citricos.db.entities.RecordEntity
import movil.siafeson.citricos.db.viewModels.LocationViewModel
import movil.siafeson.citricos.db.viewModels.RecordViewModel
import movil.siafeson.citricos.models.RecordData
import movil.siafeson.citricos.models.RecordsData
import movil.siafeson.citricos.utils.fechaCompleta
import movil.siafeson.citricos.utils.parseFecha
import movil.siafeson.citricos.utils.parseFechaInRecods
import movil.siafeson.citricos.utils.showDatePickerDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class RegistersFragment : Fragment() {
    private lateinit var mContext: Context
    private lateinit var binding: FragmentRegistersBinding
    private lateinit var recordsViewModel: RecordViewModel
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var recordsAdapter: RecordsAdapter


    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentRegistersBinding.inflate(inflater,container,false)
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        recordsViewModel = ViewModelProvider(this).get(RecordViewModel::class.java)
        loadRecords()
        return binding.root
    }

    private fun loadRecords() {
        recordsViewModel.getAllRecords().observe(viewLifecycleOwner, Observer {
            recordsList->
            recordsAdapter = RecordsAdapter(mContext, R.layout.list_records,recordsList)
            binding.DivRegsMuestreos.lvRegMuestreo.adapter = recordsAdapter

            if (recordsList.isEmpty()) {
                binding.DivRegsMuestreos.lblNoHayUbicaciones.visibility = View.VISIBLE
                binding.DivRegsMuestreos.ivLogoNoHay.visibility = View.VISIBLE
            } else {
                binding.DivRegsMuestreos.lblNoHayUbicaciones.visibility = View.INVISIBLE
                binding.DivRegsMuestreos.ivLogoNoHay.visibility = View.INVISIBLE
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recordsViewModel.getRecordsPending()
        recordsViewModel.recordPending.observe(viewLifecycleOwner, Observer { res ->
            binding.tvNumeroRegistrosFaltantes.text = res.toString()
        })

        val date = Calendar.getInstance().fechaCompleta()
        binding.fIni.setText(date)

        val datePickerButton = binding.getfIni
        datePickerButton.setOnClickListener { showDatePicker(datePickerButton) }
    }

    private fun showDatePicker(datePickerButton: ImageButton) {
        showDatePickerDialog(mContext,datePickerButton){ selectedDate ->
            binding.fIni.setText(parseFechaInRecods(selectedDate))
        }
    }
}