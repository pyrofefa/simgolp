package movil.siafeson.citricos.app

import android.content.Context
import movil.siafeson.citricos.db.AppDataBase

object DatabaseSingleton {
    lateinit var db: AppDataBase
        private set

    fun initialize(context: Context){
        db = AppDataBase.getDatabase(context)
    }
}