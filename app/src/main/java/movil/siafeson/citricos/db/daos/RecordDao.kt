package movil.siafeson.citricos.db.daos

import androidx.room.Dao
import androidx.room.Insert
import movil.siafeson.citricos.db.entities.RecordEntity

@Dao
interface RecordDao {
    @Insert
    fun insertRecord(recordEntity: RecordEntity): Long
}