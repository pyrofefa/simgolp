package movil.siafeson.citricos.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import movil.siafeson.citricos.R
import movil.siafeson.citricos.adapters.RecordsAdapter
import movil.siafeson.citricos.databinding.FragmentRegistersBinding
import movil.siafeson.citricos.db.entities.RecordEntity
import movil.siafeson.citricos.db.viewModels.RecordViewModel
import movil.siafeson.citricos.models.RecordData


class RegistersFragment : Fragment() {
    private lateinit var mContext: Context
    private lateinit var binding: FragmentRegistersBinding
    private lateinit var recordsViewModel: RecordViewModel
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
        recordsViewModel = ViewModelProvider(this).get(RecordViewModel::class.java)
        loadRecords()
        return binding.root
    }

    private fun loadRecords() {
        recordsViewModel.getAllRecords().observe(viewLifecycleOwner, Observer {
            recordsEntityList->
            val recordsDataList = recordsEntityList.map { recordsEntity ->
                transformRecordEntityToData(recordsEntity)
            }
            Log.i("records","${recordsDataList}")
            recordsAdapter = RecordsAdapter(mContext, R.layout.list_records,recordsDataList)
            binding.DivRegsMuestreos.lvRegMuestreo.adapter = recordsAdapter

            if (recordsDataList.isEmpty()) {
                binding.DivRegsMuestreos.lblNoHayUbicaciones.visibility = View.VISIBLE
                binding.DivRegsMuestreos.ivLogoNoHay.visibility = View.VISIBLE
            } else {
                binding.DivRegsMuestreos.lblNoHayUbicaciones.visibility = View.INVISIBLE
                binding.DivRegsMuestreos.ivLogoNoHay.visibility = View.INVISIBLE
            }

        })
    }

    private fun transformRecordEntityToData(recordsEntity: RecordEntity): RecordData {
        return RecordData(
            id = recordsEntity.id,
            userId = recordsEntity.userId,
            fecha = recordsEntity.fecha,
            fechaHora = recordsEntity.fechaHora,
            longitud = recordsEntity.longitud,
            latitud = recordsEntity.latitud,
            accuracy = recordsEntity.accuracy,
            recurso = recordsEntity.recurso,
            distanciaQr = recordsEntity.distanciaQr,
            campoId = recordsEntity.campoId,
            ano = recordsEntity.ano,
            semana = recordsEntity.semana,
            status = recordsEntity.status,
            totalArboles = recordsEntity.totalArboles,
            totalAdultos = recordsEntity.totalAdultos

        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recordsViewModel.getRecordsPending()
        recordsViewModel.recordPending.observe(viewLifecycleOwner, Observer { res ->
            binding.tvNumeroRegistrosFaltantes.text = res.toString()
        })
    }

}