package com.zzunee.libraryexample.model.repository.impl

import com.zzunee.libraryexample.model.db.dao.FavoriteBookDao
import com.zzunee.libraryexample.model.network.data.Book
import com.zzunee.libraryexample.model.network.BookApiService
import com.zzunee.libraryexample.model.network.data.BookItem
import com.zzunee.libraryexample.model.network.data.toBookItem
import com.zzunee.libraryexample.model.network.data.toFavoriteBook
import com.zzunee.libraryexample.model.repository.Result
import com.zzunee.libraryexample.model.repository.BaseRepository
import com.zzunee.libraryexample.model.repository.base.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class BookRepositoryImpl(
    private val api: BookApiService,
    private val favoriteBookDao: FavoriteBookDao,
) : BookRepository, BaseRepository() {
    override suspend fun searchBook(query: String): Result<List<BookItem>> {
        val result = callApi { api.searchBook(query) }

        return when (result) {
            is Result.Success -> {
                val items = result.data.items
                Result.Success(items)
            }

            is Result.Empty -> Result.Empty
            is Result.Error -> Result.Error(result.msg)
        }
    }

    override suspend fun searchBookDetail(title: String?, isbn: String?): Result<BookItem> {
        val result = callApi { api.searchBookDetail(title, isbn) }

        return when (result) {
            is Result.Success -> {
                val item = result.data.items.first()
                Result.Success(item)
            }

            is Result.Empty -> Result.Empty
            is Result.Error -> Result.Error(result.msg)
        }
    }

    override fun fetchFavoriteBooks(bookItems: List<BookItem>, favoriteBooks: List<Book>): List<Book> =
        bookItems.map { bookItem ->
            val isFavorite = favoriteBooks.any { favoriteBook ->
                favoriteBook.bookItem.title == bookItem.title &&
                        favoriteBook.bookItem.isbn == bookItem.isbn
            }
            Book(bookItem, isFavorite)
        }

    override fun getAllFavoriteBooks(): Flow<List<Book>> =
        favoriteBookDao.getAll().map { favoriteBooks ->
            favoriteBooks.map {
                Book(it.toBookItem(), true)
            }
        }.catch {
            emit(emptyList())
        }

    override suspend fun toggleFavoriteBook(book: Book) {
        val favoriteBook = book.bookItem.toFavoriteBook()

        if (book.isFavorite) {
            favoriteBookDao.delete(favoriteBook.title, favoriteBook.author, favoriteBook.pubdate)
        } else {
            favoriteBookDao.insert(favoriteBook)
        }
    }
}
