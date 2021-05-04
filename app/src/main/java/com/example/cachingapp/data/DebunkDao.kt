package com.example.cachingapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DebunkDao {

    @Query("SELECT * FROM debunks")
    fun getAllDebunks(): Flow<List<Debunk>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDebunk(debunks: List<Debunk>)

    @Query("DELETE FROM debunks")
    suspend fun deleteAllDebunks()

    @Query("SELECT * FROM debunks WHERE category LIKE :searchQuery OR question LIKE :searchQuery OR answer like :searchQuery")
    fun searchDebunks(searchQuery: String): Flow<List<Debunk>>
}