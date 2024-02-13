package movil.siafeson.simgolp.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "ubicaciones")
data class LocationEntity (
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id_bit") val idBit: Int,
    @SerializedName("predio") val predio: String,
    @SerializedName("status") val status: Int,
    @SerializedName("latitud") val latitud: String,
    @SerializedName("longitud") val longitud: String,
    @SerializedName("superficie") val superficie: String,
    @SerializedName("id_sicafi") val idSicafi: Int
)



