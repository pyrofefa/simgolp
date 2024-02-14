package movil.siafeson.simgolp.fragments
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
import movil.siafeson.simgolp.app.MyApp
import movil.siafeson.simgolp.R
import movil.siafeson.simgolp.activities.LoginActivity
import movil.siafeson.simgolp.databinding.DialogInfoBinding
import movil.siafeson.simgolp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var dialogInfoBinding: DialogInfoBinding
    private lateinit var mContext: Context

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
                logOff()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
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