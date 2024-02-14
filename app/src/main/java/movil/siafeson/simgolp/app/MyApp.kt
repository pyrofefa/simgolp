package movil.siafeson.simgolp.app

import android.app.Application
import movil.siafeson.simgolp.interfaces.APIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
val services: APIService by lazy { MyApp.service!! }

class MyApp: Application() {
    private lateinit var retrofit: Retrofit

    companion object {
        lateinit var preferences: Preferences
        lateinit var service: APIService
    }

    override fun onCreate() {
        super.onCreate()
        preferences = Preferences(applicationContext)
        DatabaseSingleton.initialize(this)
        initializeRetrofit()
    }
    private fun initializeRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl("https://aplicaciones.siafeson.org.mx/public/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(APIService::class.java)
    }
}