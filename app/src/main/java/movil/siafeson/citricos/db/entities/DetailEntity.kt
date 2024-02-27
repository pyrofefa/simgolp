package movil.siafeson.citricos.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detalles")
data class DetailEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val punto: Int,
    val longitud: Double,
    val latitud: Double,
    val accuracy: Double,
    @ColumnInfo("distancia_qr") val distanciaQr: Double,
    val status: Int,
    @ColumnInfo("muestreo_id") val muestreoId: Int,
    @ColumnInfo("id_bd_cel") val idBdCel: Int,
    val adultos: Int,
    val fecha: String
)