package movil.siafeson.citricos.db.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movil.siafeson.citricos.db.daos.RecordDao
import movil.siafeson.citricos.db.entities.RecordEntity

class RecordRepository(private val recordDao: RecordDao){

    suspend fun insertRecord(record: RecordEntity): Long? {
        return withContext(Dispatchers.IO){
            try {
                recordDao.insertRecord(record)
            }catch (e: Exception){
                null
            }
        }
    }
}