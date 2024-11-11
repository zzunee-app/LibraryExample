package com.zzunee.shoppingexample.data.repository

import com.zzunee.shoppingexample.data.db.dao.BookRentalDao
import com.zzunee.shoppingexample.data.db.entity.RentalBook
import kotlinx.coroutines.flow.Flow
import java.util.Date

class RentalRepository(
    private val bookRentalDao: BookRentalDao
) : Repository() {
    fun getAllRentalBook(): Flow<List<RentalBook>> = bookRentalDao.getAll()
    suspend fun rentBook(rentalBook: RentalBook) = bookRentalDao.insert(rentalBook)
    suspend fun returnBook(today: Date) = bookRentalDao.delete(today)
}
