package movil.siafeson.citricos.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "muestreo")
data class RecordEntity (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo("user_id") val userId: String,
    val fecha: String,
    @ColumnInfo("fecha_hora") val fechaHora: String,
    val longitud: Double,
    val latitud: Double,
    val accuracy: Double,
    val recurso: Int,
    @ColumnInfo("distancia_qr") val distanciaQr: Double,
    @ColumnInfo("campo_id") val campoId: Int,
    val ano: String,
    val semana: String,
    @ColumnInfo(name = "status") @NotNull val status: Int,
    @ColumnInfo("total_arboles") val totalArboles: Int,
    @ColumnInfo("total_adultos") val totalAdultos: Int,
    @ColumnInfo(name = "created") @NotNull val created: String,
    @ColumnInfo(name = "created_sat") @NotNull val createdSat: String,
    @ColumnInfo(name = "modified") @NotNull val modified: String,
    @ColumnInfo(name = "version") @NotNull val version: String,
)
