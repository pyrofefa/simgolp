package movil.siafeson.citricos.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import movil.siafeson.citricos.db.daos.DetailDao
import movil.siafeson.citricos.db.daos.LocationDao
import movil.siafeson.citricos.db.daos.RecordDao
import movil.siafeson.citricos.db.entities.DetailEntity
import movil.siafeson.citricos.db.entities.LocationEntity
import movil.siafeson.citricos.db.entities.RecordEntity

@Database(entities = arrayOf(LocationEntity::class, RecordEntity::class, DetailEntity::class), version = 2)
abstract class AppDataBase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun recordDao(): RecordDao
    abstract fun detailDao(): DetailDao

    companion object{
        @Volatile
        private var INSTANCE: AppDataBase? = null

        private const val DATABASE_NAME = "simgolp.db"

        fun getDatabase(context: Context): AppDataBase{
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