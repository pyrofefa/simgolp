package movil.siafeson.citricos.interfaces
import movil.siafeson.citricos.models.LocationsResponse
import movil.siafeson.citricos.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
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

    @GET
    suspend fun getLocations(@Url url:String) : Response<LocationsResponse>

    @POST
    suspend fun addRecord(
        @Url url: String
    )
}