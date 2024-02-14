package movil.siafeson.simgolp.app

import android.content.Context
import movil.siafeson.simgolp.db.AppDataBase

object DatabaseSingleton {
    lateinit var db: AppDataBase
        private set

    fun initialize(context: Context){
        db = AppDataBase.getDatabase(context)
    }
}