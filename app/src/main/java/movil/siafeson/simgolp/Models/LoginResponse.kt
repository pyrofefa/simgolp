package movil.siafeson.simgolp.Models

import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @SerializedName("success") var status: Boolean,
    @SerializedName("msj") var message: String,
    @SerializedName("data") var data: UserData
)
