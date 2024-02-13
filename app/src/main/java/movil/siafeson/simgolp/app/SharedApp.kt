package movil.siafeson.simgolp.app

import android.app.Application

class SharedApp: Application() {
    companion object {
        lateinit var preferences: Preferences
    }

    override fun onCreate() {
        super.onCreate()
        preferences = Preferences(applicationContext)
    }
}