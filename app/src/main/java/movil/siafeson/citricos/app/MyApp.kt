package movil.siafeson.citricos.app

import android.app.Application
import movil.siafeson.citricos.interfaces.APIService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val services: APIService by lazy { MyApp.service!! }
val distanceAllowed: Double = 150000.0
val accurracyAlloWed: Float = 16.toFloat()

class MyApp: Application() {
    private var retrofit: Retrofit? = null

    companion object {
        lateinit var preferences: Preferences
        lateinit var service: APIService
    }

    override fun onCreate() {
        super.onCreate()
        preferences = Preferences(applicationContext)
        DatabaseSingleton.initialize(this)
        getHttpIntance()
    }

    fun getHttpIntance() {
        if (retrofit == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(0, TimeUnit.SECONDS)
                .readTimeout(0, TimeUnit.SECONDS).build()

            retrofit = Retrofit.Builder()
                .baseUrl("https://aplicaciones.siafeson.org.mx/public/api/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        service = retrofit!!.create(APIService::class.java)
    }
}


