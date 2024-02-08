package movil.siafeson.simgolp.Interfaces
import movil.siafeson.simgolp.Models.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Url
import retrofit2.http.POST

interface APIService {

    @FormUrlEncoded
    @POST
    suspend fun login(
        @Url url:String,
        @Field("username") username: String,
        @Field("password") password: String,
    ): LoginResponse
}