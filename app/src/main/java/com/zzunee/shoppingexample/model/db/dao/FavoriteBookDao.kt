package com.zzunee.shoppingexample.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zzunee.shoppingexample.model.db.entity.FavoriteBook
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteBookDao {
    @Query("SELECT * FROM favorite_book")
    fun getAll(): Flow<List<FavoriteBook>>

    @Query("DELETE FROM favorite_book WHERE title=:title AND author=:author AND pubdate=:pubdate")
    suspend fun delete(title: String, author: String, pubdate: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: FavoriteBook)
}