package movil.siafeson.simgolp.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "ubicaciones")
data class LocationEntity (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @SerializedName("id_bit") val idBit: Int,
    @SerializedName("predio") val predio: String,
    @SerializedName("status") val status: Int,
    @SerializedName("latitud") val latitud: Double,
    @SerializedName("longitud") val longitud: Double,
    @SerializedName("superficie") val superficie: Double,
    @SerializedName("id_sicafi") val idSicafi: Int
)



