package movil.siafeson.simgolp.app

import android.app.Application
import androidx.room.Room
import movil.siafeson.simgolp.db.AppDataBase

class MyDatabaseApp: Application() {
    companion object{
        lateinit var database: AppDataBase
    }

    override fun onCreate() {
        super.onCreate()
        MyDatabaseApp.database = Room.databaseBuilder(
            this,AppDataBase::class.java,
            "simgolp-db"
        ).build()
    }
}