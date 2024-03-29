package movil.siafeson.citricos.db.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movil.siafeson.citricos.db.daos.DetailDao
import movil.siafeson.citricos.db.entities.DetailEntity

class DetailRepository(private val detailDao: DetailDao) {
    suspend fun insertDetail(detail: DetailEntity){
        withContext(Dispatchers.IO){
            detailDao.insertDetail(detail)
        }
    }
    suspend fun getDetailsRecord(id: Int): List<DetailEntity>{
        return withContext(Dispatchers.IO){
            detailDao.getDetailsRecord(id)
        }
    }

    suspend fun deleteDetail(id: Int) {
        return withContext(Dispatchers.IO){
            detailDao.deleteDetail(id)
        }
    }

    suspend fun editDetail(id: Int, adults: Int){
        return withContext(Dispatchers.IO){
            detailDao.editDetail(id, adults)
        }
    }

    suspend fun deleteAllDetails(){
        return withContext(Dispatchers.IO){
            detailDao.deleteAllDetails()
        }
    }


}