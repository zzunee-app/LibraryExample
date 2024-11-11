package com.zzunee.shoppingexample.data.repository

import com.zzunee.shoppingexample.data.db.dao.FavoriteBookDao
import com.zzunee.shoppingexample.data.network.Book
import com.zzunee.shoppingexample.data.network.BookApiService
import com.zzunee.shoppingexample.data.network.BookItem
import com.zzunee.shoppingexample.data.network.toBookItem
import com.zzunee.shoppingexample.data.network.toFavoriteBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class BookRepository(
    private val api: BookApiService,
    private val favoriteBookDao: FavoriteBookDao
) : Repository() {
    suspend fun searchBook(query: String): Result<List<BookItem>> =
        withContext(Dispatchers.IO) {
            val response = api.searchBook(query)

            if (response.isSuccessful) {
                if (response.body() == null || response.body()?.items?.isEmpty() == true) {
                    Result.Empty
                } else {
                    Result.Success(response.body()!!.items)
                }
            } else {
                Result.Error(apiErrorMessage(response.code()))
            }
        }

    suspend fun searchBookDetail(title: String? = null, isbn: String? = null): Result<List<BookItem>> =
        withContext(Dispatchers.IO) {
            val response = api.searchBookDetail(title, isbn)

            if (response.isSuccessful) {
                if (response.body() == null || response.body()?.items?.isEmpty() == true) {
                    Result.Empty
                } else {
                    Result.Success(response.body()!!.items)
                }
            } else {
                Result.Error(apiErrorMessage(response.code()))
            }
        }

    fun fetchFavoriteBooks(bookItems: List<BookItem>): Flow<List<Book>> = favoriteBookDao.getAll().map { favoriteBooks ->
        val books = bookItems.map { bookItem ->
            val isFavorite = favoriteBooks.any { favoriteBook ->
                favoriteBook.title == bookItem.title &&
                        favoriteBook.author == bookItem.author &&
                        favoriteBook.pubdate == bookItem.pubdate
            }
            Book(bookItem, isFavorite)
        }
        books
    }

    fun getAllFavoriteBooks(): Flow<List<Book>> = favoriteBookDao.getAll().map { favoriteBooks ->
            favoriteBooks.map {
                Book(it.toBookItem(), true)
            }
        }

    suspend fun toggleFavoriteBook(book: Book) {
        val favoriteBook = book.bookItem.toFavoriteBook()

        if (book.isFavorite) {
            favoriteBookDao.delete(favoriteBook.title, favoriteBook.author, favoriteBook.pubdate)
        } else {
            favoriteBookDao.insert(favoriteBook)
        }
    }
//    suspend fun removeFavoriteBook(book: FavoriteBook) = favoriteBookDao.delete(book.title, book.author, book.pubdate)
//    suspend fun addFavoriteBook(book: FavoriteBook) = favoriteBookDao.insert(book)
}
