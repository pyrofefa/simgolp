package movil.siafeson.simgolp.models

data class LocationData(
    val id_bit: Int,
    val predio: String,
    val status: Int,
    val latitud: String,
    val longitud: String,
    val superficie: String,
    val id_sicafi: Int,
    var distancia: Double? = null,
    var cardinal: String? = null
)
