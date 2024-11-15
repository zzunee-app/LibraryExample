package com.zzunee.shoppingexample.model.repository.base

import com.zzunee.shoppingexample.model.db.entity.RentalBook
import com.zzunee.shoppingexample.model.network.data.BookItem
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface RentalRepository {
    fun getAllRentalBook(): Flow<Map<String, List<RentalBook>>>
    suspend fun rentBook(bookItem: BookItem, days: Int)
    suspend fun returnBook()
}