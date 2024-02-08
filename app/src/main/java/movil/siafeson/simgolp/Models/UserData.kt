package movil.siafeson.simgolp.Models

data class UserData (
    val user_id: Int,
    val username: String,
    val nombre: String?,
    val apellido_paterno: String?,
    val apellido_materno: String?,
    val junta_id: Int,
    val estado_id: Int,
    val junta_name: String,
    val personal_id: Int,
    val junta_sicafi_id: Int,
    val email: String
)