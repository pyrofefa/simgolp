package movil.siafeson.citricos.activities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.async
import movil.siafeson.citricos.databinding.ActivityLoginBinding
import movil.siafeson.citricos.requests.LoginRequests
import movil.siafeson.citricos.utils.isOnlineNet
import movil.siafeson.citricos.utils.showProgressDialog

class LoginActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var binding: ActivityLoginBinding

    private val CODE_PERMISSION_LOCATION = 2106
    private var hasGratedPermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        checkPermissions()

        val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        binding.btnLogin.setOnClickListener {
            var username =  binding.editTextUserName.text.toString().trim()
            var password =  binding.editTextPassword.text.toString().trim()
            if (isOnlineNet(this)){
                if (username.isEmpty()){
                    Toast.makeText(this@LoginActivity,"Debe ingresar el nombre de usuario",Toast.LENGTH_SHORT).show()
                }else if(password.isEmpty()){
                    Toast.makeText(this@LoginActivity,"Debe ingresar una contraseña",Toast.LENGTH_SHORT).show()
                }
                else{
                    login(username, password)
                }
            }
            else{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("¡Atención!")
                builder.setMessage("No cuenta con conexión a Internet.")
                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CODE_PERMISSION_LOCATION){
            val allPermisions = grantResults.all {
                it == PackageManager.PERMISSION_GRANTED
            }
            if (grantResults.isNotEmpty() && allPermisions){
                hasGratedPermission = true
            }
        }
    }

    private fun checkPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val permission = arrayListOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            val permissionsOfArray = permission.toTypedArray()
            if (hasPermissions(permissionsOfArray)){
                hasGratedPermission = true
            }
            else{
                requestPermissions(permissionsOfArray)
            }
        }
        else {
            hasGratedPermission = true
            Log.d("LOGIN", "Los permisos ya fueron concedidos")
        }
    }

    private fun requestPermissions(permissionsOfArray: Array<String>) {
        requestPermissions(permissionsOfArray,CODE_PERMISSION_LOCATION)
    }

    private fun hasPermissions(permissionsOfArray: Array<String>): Boolean {
        return permissionsOfArray.all {
            return ContextCompat.checkSelfPermission(
                this, it
            )== PackageManager.PERMISSION_GRANTED
        }
    }

    private fun login(userName: String, password: String) {
        progressDialog = showProgressDialog(
            "Iniciando sesión",
            "Verificando datos de acceso",
            this,
            ProgressDialog.STYLE_SPINNER,
        )
        progressDialog.show()

        lifecycleScope.async {
            val resp = LoginRequests().login(userName,password)
            if (resp == "success"){
                progressDialog.dismiss()
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finishAffinity()
            }else{
                progressDialog.dismiss()
                Toast.makeText(this@LoginActivity,"${resp}",Toast.LENGTH_SHORT).show()
            }
        }
    }
}