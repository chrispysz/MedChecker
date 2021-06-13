package com.example.cachingapp.data

import androidx.room.withTransaction
import com.example.cachingapp.api.DebunkApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DebunkRepository @Inject constructor(
    private val api: DebunkApi,
    private val db: DebunkDatabase
) {
    private val debunkDao = db.debunkDao()

    fun getDbDebunks() =
            debunkDao.getAllDebunks()

    suspend fun insertDebunk(d: Debunk){
        debunkDao.insertDebunk(listOf(d))
    }



    suspend fun getApiDebunks() = run {
        val debunks = api.getDebunks()
        delay(500)
        db.withTransaction {
            debunkDao.deleteDefaultDebunks()
            debunkDao.insertDebunk(debunks)
        }
    }


    fun searchDebunks(searchQuery: String): Flow<List<Debunk>>{
        return debunkDao.searchDebunks(searchQuery)
    }


}