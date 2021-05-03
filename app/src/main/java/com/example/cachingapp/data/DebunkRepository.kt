package com.example.cachingapp.data

import androidx.room.withTransaction
import com.example.cachingapp.api.DebunkApi
import com.example.cachingapp.util.networkBoundResource
import kotlinx.coroutines.delay
import javax.inject.Inject

class DebunkRepository @Inject constructor(
    private val api: DebunkApi,
    private val db: DebunkDatabase
) {
    private val debunkDao = db.debunkDao()

    fun getDebunks() = networkBoundResource(
        query = {
            debunkDao.getAllDebunks()
        },
        fetch = {
            delay(2000)
            api.getDebunks()
        },
        saveFetchResult = { debunks ->
            db.withTransaction {
                debunkDao.deleteAllDebunks()
                debunkDao.insertDebunk(debunks)
            }
        }
    )

    fun getCategoryDebunks(cat: String) =
        debunkDao.getCategoryDebunks(cat)


}