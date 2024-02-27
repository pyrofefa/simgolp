package movil.siafeson.citricos.models

import android.os.Parcel
import android.os.Parcelable

data class LocationData(
    val id_bit: Int,
    val predio: String,
    val status: Int,
    val latitud: Double,
    val longitud: Double,
    val superficie: Double,
    val id_sicafi: Int,
    var distancia: Double,
    var orientacion: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readString().toString()
    )

    override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(id_bit)
            parcel.writeString(predio)
            parcel.writeInt(status)
            parcel.writeDouble(latitud)
            parcel.writeDouble(longitud)
            parcel.writeDouble(superficie)
            parcel.writeInt(id_sicafi)
            parcel.writeDouble(distancia)
            parcel.writeString(orientacion)
        }

    companion object CREATOR : Parcelable.Creator<LocationData> {
        override fun createFromParcel(parcel: Parcel): LocationData {
            return LocationData(parcel)
        }

        override fun newArray(size: Int): Array<LocationData?> {
            return arrayOfNulls(size)
        }
    }

}
