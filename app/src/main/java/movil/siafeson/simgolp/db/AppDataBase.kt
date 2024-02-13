package movil.siafeson.simgolp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import movil.siafeson.simgolp.db.daos.LocationDao
import movil.siafeson.simgolp.db.entities.LocationEntity

@Database(entities = arrayOf(LocationEntity::class), version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun locationDao(): LocationDao

    companion object{
        private const val DATABASE_NAME = "simgolp.db"

        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java,
                        DATABASE_NAME
                    ).build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}