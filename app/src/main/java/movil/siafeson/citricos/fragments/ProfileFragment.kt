package movil.siafeson.citricos.fragments
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import movil.siafeson.citricos.app.MyApp
import movil.siafeson.citricos.R
import movil.siafeson.citricos.activities.LoginActivity
import movil.siafeson.citricos.databinding.DialogInfoBinding
import movil.siafeson.citricos.databinding.FragmentProfileBinding
import movil.siafeson.citricos.db.viewModels.DetailViewModel
import movil.siafeson.citricos.db.viewModels.LocationViewModel
import movil.siafeson.citricos.db.viewModels.RecordViewModel
import movil.siafeson.citricos.utils.showAlertDialog

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var dialogInfoBinding: DialogInfoBinding
    private lateinit var mContext: Context

    private lateinit var locationViewModel: LocationViewModel
    private lateinit var recordViewModel: RecordViewModel
    private lateinit var detailViewModel: DetailViewModel

    private lateinit var alertDialog: AlertDialog

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater,container,false)
        setHasOptionsMenu(true)
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        recordViewModel = ViewModelProvider(this).get(RecordViewModel::class.java)
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_info -> {
                createDialogInfo();
                return true
            }
            R.id.menu_logout -> {
                createDialogLogOff()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun createDialogLogOff() {
        showAlertDialog(
            "Cerrar sesión",
            "Los registros del móvil serán eliminados al cerrar sesión, ¿Seguro que desea cerrar sesión?",
            mContext,
            "Si, continuar",
            positiveButtonAction = {
                logOff()
            },
            "Cancelar"
        )

    }

    private fun createDialogInfo() {
        val dialogBuilder = AlertDialog.Builder(mContext)
        dialogInfoBinding = DialogInfoBinding.inflate(layoutInflater)
        dialogBuilder.setView(dialogInfoBinding.root)
        val versionName = getVersion()

        dialogInfoBinding.infoVersion.text = versionName
        alertDialog = dialogBuilder.setCancelable(false).create()
        dialogInfoBinding.imageViewCloseDialog.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun getData() {
        binding.textViewUserName.text = MyApp.preferences.name
        binding.textViewJunta.text = MyApp.preferences.junta
        binding.textViewUserId.text = MyApp.preferences.userId.toString()
        binding.textViewPersonalId.text = MyApp.preferences.personalId.toString()
        binding.textViewEmail.text = MyApp.preferences.email
        binding.textViewJuntaId.text = MyApp.preferences.juntaId.toString()
    }
    private fun logOff() {
        MyApp.preferences.userName = ""
        MyApp.preferences.name = ""
        MyApp.preferences.junta = ""
        MyApp.preferences.userId = 0
        MyApp.preferences.personalId = 0
        MyApp.preferences.email = ""
        MyApp.preferences.juntaId = 0
        MyApp.preferences.locationUpdate = false

        locationViewModel.deleteLocation()
        recordViewModel.deleteAllRecords()
        detailViewModel.deleteAllDetails()

        startActivity(Intent(mContext, LoginActivity::class.java))
        activity?.finishAffinity()
    }

    private fun getVersion(): String{
        var version = ""
        try {
            val packageInfo: PackageInfo =
                requireActivity().packageManager.getPackageInfo(requireActivity().packageName,0)
            version = packageInfo.versionName
        }catch (e: PackageManager.NameNotFoundException){
            e.printStackTrace()
        }
        return version
    }
}