package movil.siafeson.citricos.db.repositories

 import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movil.siafeson.citricos.db.daos.RecordDao
import movil.siafeson.citricos.db.entities.RecordEntity
 import movil.siafeson.citricos.models.RecordData
 import movil.siafeson.citricos.models.RecordIdData
 import movil.siafeson.citricos.models.RecordsData
 import movil.siafeson.citricos.models.RequestObject
 import movil.siafeson.citricos.requests.RecordsRequests

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

    suspend fun updateRecord(status: Int, id: Int): Int? {
        return withContext(Dispatchers.IO){
            try {
                recordDao.updateRecord(status,id)
            }catch (e: Exception){
                null
            }
        }
    }

    suspend fun updateRecordTotal(id: Int, adults: Int, points: Int): Int? {
        return withContext(Dispatchers.IO){
            try {
                recordDao.updateRecordTotals(id, adults, points)
            }catch (e: Exception){
                null
            }
        }
    }

    suspend fun getRecord(id: Int): List<RecordData>? {
        return withContext(Dispatchers.IO){
            try {
                recordDao.getRecord(id)
            }catch (e: Exception){
                null
            }
        }
    }


    suspend fun getRecordId(id:Int, week: Int, year:Int):List<RecordIdData>? {
        return withContext(Dispatchers.IO){
            try {
                recordDao.getRecordId(id, week, year)
            }catch (e: Exception){
                null
            }

        }
    }
    suspend fun getCountRecords(date:String): Long ?{
        return withContext(Dispatchers.IO){
            try {
                recordDao.getCountRecords(date)
            }catch (e: Exception){
                null
            }
        }
    }
    suspend fun getCountRecordsPending(): Long ?{
        return withContext(Dispatchers.IO){
            try {
                recordDao.getCountRecordsPending()
            }catch (e: Exception){
                null
            }
        }
    }
    suspend fun getAllRecords(): List<RecordsData>{
        return withContext(Dispatchers.IO){
            recordDao.getAllRecords()
        }
    }

    suspend fun deleteAllRecord(){
        return withContext(Dispatchers.IO){
            recordDao.deleteAllRecords()
        }
    }
}