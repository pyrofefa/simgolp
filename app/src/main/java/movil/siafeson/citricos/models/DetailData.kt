package movil.siafeson.citricos.models

data class DetailData(
    val id: Long,
    val punto: Int,
    val longitud: Double,
    val latitud: Double,
    val accuracy: Double,
    val distanciaQr: Double,
    val status: Int,
    val muestreoId: Int,
    val adultos: Int,
    val fecha: String
)
