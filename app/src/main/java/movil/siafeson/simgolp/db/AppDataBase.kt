package movil.siafeson.simgolp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import movil.siafeson.simgolp.db.daos.LocationDao
import movil.siafeson.simgolp.db.entities.LocationEntity

@Database(entities = arrayOf(LocationEntity::class), version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}