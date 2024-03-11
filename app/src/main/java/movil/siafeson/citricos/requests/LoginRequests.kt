package movil.siafeson.citricos.requests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movil.siafeson.citricos.app.MyApp
import movil.siafeson.citricos.app.services

class LoginRequests {

    suspend fun login(username:String, password:String) : String {
        return withContext(Dispatchers.IO){
            try {
                val response = services.login("login/viejoEsquema",username,password)
                if (response.status){
                    MyApp.preferences.userId = response.data.user_id
                    MyApp.preferences.userName = response.data.username
                    MyApp.preferences.name = response.data.nombre
                    MyApp.preferences.lastName = "${response.data.apellido_paterno} ${response.data.apellido_materno}"
                    MyApp.preferences.juntaId = response.data.junta_id
                    MyApp.preferences.personalId = response.data.personal_id
                    MyApp.preferences.junta = response.data.junta_name
                    MyApp.preferences.juntaSICAFIId = response.data.junta_id
                    MyApp.preferences.email = response.data.email

                    "success"
                }else{
                    response.message
                }
            }catch (e: Exception){
                "Error durante el login: ${e.message}"
            }
        }
    }

}