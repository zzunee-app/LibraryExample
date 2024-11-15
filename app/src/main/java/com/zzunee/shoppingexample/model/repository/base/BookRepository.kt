package com.zzunee.shoppingexample.model.repository.base

import com.zzunee.shoppingexample.model.network.data.Book
import com.zzunee.shoppingexample.model.network.data.BookItem
import com.zzunee.shoppingexample.model.repository.Result
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun searchBook(query: String): Result<List<BookItem>>
    suspend fun searchBookDetail(title: String? = null, isbn: String? = null): Result<BookItem>
    fun fetchFavoriteBooks(bookItems: List<BookItem>, favoriteBooks: List<Book>): List<Book>
    fun getAllFavoriteBooks(): Flow<List<Book>>
    suspend fun toggleFavoriteBook(book: Book)
}