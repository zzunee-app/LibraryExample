package com.zzunee.shoppingexample.model.repository.impl

import com.zzunee.shoppingexample.model.db.dao.BookRentalDao
import com.zzunee.shoppingexample.model.db.entity.RentalBook
import com.zzunee.shoppingexample.model.network.data.Book
import com.zzunee.shoppingexample.model.network.data.BookItem
import com.zzunee.shoppingexample.model.network.data.toRentalBook
import com.zzunee.shoppingexample.model.repository.BaseRepository
import com.zzunee.shoppingexample.model.repository.base.RentalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RentalRepositoryImpl(
    private val bookRentalDao: BookRentalDao
) : RentalRepository, BaseRepository() {
    override fun getAllRentalBook(): Flow<Map<String, List<RentalBook>>> =
        bookRentalDao.getAll().map { books ->
            val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            books.groupBy {  dateFormat.format(it.returnDate) }
        }.catch {
            emit(emptyMap())
        }

    override suspend fun rentBook(bookItem: BookItem, days: Int) {
        val today = Date() // 대여일
        val calendar = Calendar.getInstance()
        calendar.time = today
        calendar.add(Calendar.DAY_OF_YEAR, days)
        val returnDate = calendar.time // 반납일

        callQuery { bookRentalDao.insert(bookItem.toRentalBook(today, returnDate)) }
    }
    override suspend fun returnBook() {
        val today = Calendar.getInstance().time
        callQuery { bookRentalDao.delete(today) }
    }
}
