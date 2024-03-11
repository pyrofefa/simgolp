package movil.siafeson.citricos.models

import androidx.room.ColumnInfo

data class RecordData (
    val id: Long,
    @ColumnInfo(name = "user_id") val userId: String,
    val fecha: String,
    @ColumnInfo(name = "fecha_hora") val fechaHora: String,
    val longitud: Double,
    val latitud: Double,
    val accuracy: Double,
    val recurso: Int,
    @ColumnInfo(name = "distancia_qr") val distanciaQr: Double,
    @ColumnInfo(name = "campo_id") val campoId: Int,
    val ano: String,
    val semana: String,
    val status: Int,
    @ColumnInfo(name = "total_arboles") val totalArboles: Int,
    @ColumnInfo(name = "total_adultos") val totalAdultos: Int,
)
