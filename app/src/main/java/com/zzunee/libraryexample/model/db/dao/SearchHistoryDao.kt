package com.zzunee.libraryexample.model.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zzunee.libraryexample.model.db.entity.SearchHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM search_history")
    fun getAll(): Flow<List<SearchHistory>>

    @Query("DELETE FROM search_history")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(history: SearchHistory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: SearchHistory)
}