package movil.siafeson.citricos.models

data class RecordData (
    val id: Long,
    val userId: String,
    val fecha: String,
    val fechaHora: String,
    val longitud: Double,
    val latitud: Double,
    val accuracy: Double,
    val recurso: Int,
    val distanciaQr: Double,
    val campoId: Int,
    val ano: String,
    val semana: String,
    val status: Int,
    val totalArboles: Int,
    val totalAdultos: Int,
)
