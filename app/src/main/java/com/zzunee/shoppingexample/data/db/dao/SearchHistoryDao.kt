package com.zzunee.shoppingexample.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.zzunee.shoppingexample.data.db.entity.SearchHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM search_history")
    fun getAll(): Flow<List<SearchHistory>>

    @Query("DELETE FROM search_history")
    suspend fun delete()

    @Delete
    suspend fun delete(history: SearchHistory)

    @Insert
    suspend fun insert(history: SearchHistory)
}