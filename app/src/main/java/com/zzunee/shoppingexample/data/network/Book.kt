package com.zzunee.shoppingexample.data.network

import com.zzunee.shoppingexample.common.Utils
import com.zzunee.shoppingexample.data.db.entity.RentalBook
import com.zzunee.shoppingexample.data.db.entity.FavoriteBook
import java.util.Date

data class BookResponse(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<BookItem>
)

data class BookItem(
    val author: String,
    val description: String = "",
    val discount: String = "",
    val image: String,
    val isbn: String = "",
    val link: String = "",
    val pubdate: String,
    val publisher: String = "",
    val title: String
) {
    fun formattedPrice() = Utils.formattedPrice(discount)
    fun formattedPublisherWithDate() = String.format("%s | %s년 %s월 %s일", publisher, pubdate.substring(0, 4), pubdate.substring(4, 6), pubdate.substring(6, 8))
}

fun BookItem.toFavoriteBook(): FavoriteBook = FavoriteBook(
    title = this.title,
    author = this.author,
    pubdate = this.pubdate,
    image = this.image,
    isbn = this.isbn
)

fun BookItem.toRentalBook(rentDate: Date, returnDate: Date): RentalBook = RentalBook(
    title = this.title,
    author = this.author,
    image = this.image,
    isbn = this.isbn,
    rentalDate = rentDate,
    returnDate = returnDate
)

fun FavoriteBook.toBookItem(): BookItem = BookItem(
    title = this.title,
    author = this.author,
    pubdate = this.pubdate,
    image = this.image,
    isbn = this.isbn
)

data class Book(
    val bookItem: BookItem,
    val isFavorite: Boolean = false
)