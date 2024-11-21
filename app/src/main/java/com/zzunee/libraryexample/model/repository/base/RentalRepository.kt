package com.zzunee.libraryexample.model.repository.base

import com.zzunee.libraryexample.model.db.entity.RentalBook
import com.zzunee.libraryexample.model.network.data.BookItem
import kotlinx.coroutines.flow.Flow

interface RentalRepository {
    fun getAllRentalBook(): Flow<Map<String, List<RentalBook>>>
    suspend fun rentBook(bookItem: BookItem, days: Int)
    suspend fun returnBook()
}