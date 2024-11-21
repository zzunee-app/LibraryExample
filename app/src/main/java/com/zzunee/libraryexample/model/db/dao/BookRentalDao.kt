package com.zzunee.libraryexample.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zzunee.libraryexample.model.db.entity.RentalBook
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface BookRentalDao {
    @Query("SELECT * FROM rental_book")
    fun getAll(): Flow<List<RentalBook>>

    @Insert
    suspend fun insert(book: RentalBook)

    @Query("DELETE FROM rental_book WHERE returnDate < :today")
    suspend fun delete(today: Date)
}