package movil.siafeson.simgolp.models

data class LocationData(
    val id_bit: Int,
    val predio: String,
    val status: Int,
    val latitud: Double,
    val longitud: Double,
    val superficie: Double,
    val id_sicafi: Int,
    var distancia: Double,
    var orientacion: String)
