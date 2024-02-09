package movil.siafeson.simgolp.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import movil.siafeson.simgolp.app.RetrofitHelper
import movil.siafeson.simgolp.interfaces.APIService
import movil.siafeson.simgolp.databinding.ActivityLoginBinding
import movil.siafeson.simgolp.app.SharedApp

class LoginActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)

        val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        binding.btnLogin.setOnClickListener {
            var username =  binding.editTextUserName.text.toString().trim()
            var password =  binding.editTextPassword.text.toString().trim()
            if (username.isEmpty()){
                Toast.makeText(this@LoginActivity,"Debe ingresar el nombre de usuario",Toast.LENGTH_SHORT).show()
            }else if(password.isEmpty()){
                Toast.makeText(this@LoginActivity,"Debe ingresar una contraseña",Toast.LENGTH_SHORT).show()
            }
            else{
                login(username, password)
            }
        }
    }

    private fun login(userName: String, password: String) {
        progressDialog.setTitle("Iniciando sesión")
        progressDialog.setMessage("Verificando datos de acceso")
        progressDialog.show()

        CoroutineScope(Dispatchers.IO).launch {
            var call = RetrofitHelper.getInstance()
                .create(APIService::class.java)
                .login("login/viejoEsquema",userName, password)
            runOnUiThread {
                progressDialog.dismiss()
                if (call.status) {
                    SharedApp.preferences.userId = call.data.user_id
                    SharedApp.preferences.userName = call.data.username
                    SharedApp.preferences.name = call.data.nombre
                    SharedApp.preferences.lastName = "${call.data.apellido_paterno} ${call.data.apellido_materno}"
                    SharedApp.preferences.juntaId = call.data.junta_id
                    SharedApp.preferences.personalId = call.data.personal_id
                    SharedApp.preferences.junta = call.data.junta_name
                    SharedApp.preferences.juntaSICAFIId = call.data.junta_id
                    SharedApp.preferences.email = call.data.email

                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finishAffinity()
                }else{
                    Toast.makeText(this@LoginActivity,"${call.message}",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}