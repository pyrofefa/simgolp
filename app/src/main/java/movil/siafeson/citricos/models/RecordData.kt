package movil.siafeson.citricos.models

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class RecordData (
    val id: Long,
    @ColumnInfo(name = "user_id") @SerializedName("user_id") val userId: String,
    val fecha: String,
    @ColumnInfo(name = "fecha_hora") @SerializedName("fechaHora") val fechaHora: String,
    val longitud: Double,
    val latitud: Double,
    val accuracy: Double,
    val recurso: Int,
    @ColumnInfo(name = "distancia_qr") @SerializedName("distancia_qr") val distanciaQr: Double,
    @ColumnInfo(name = "campo_id") @SerializedName("campo_id") val campoId: Int,
    val ano: String,
    val semana: String,
    val status: Int,
    @ColumnInfo(name = "total_arboles") @SerializedName("total_arboles") val totalArboles: Int,
    @ColumnInfo(name = "total_adultos") @SerializedName("total_adultos") val totalAdultos: Int,
    @ColumnInfo(name = "created") val created: String,
    @ColumnInfo(name = "created_sat") val createdSat: String,
    @ColumnInfo(name = "modified") val modified: String,
    @ColumnInfo(name = "version") val version: String,
)
