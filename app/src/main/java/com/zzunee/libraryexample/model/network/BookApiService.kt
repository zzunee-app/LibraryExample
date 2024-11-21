package com.zzunee.libraryexample.model.network

import com.zzunee.libraryexample.model.network.data.BookResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApiService {
    @GET("search/book")
    suspend fun searchBook(
        @Query("query") query: String,
        @Query("display") display: Int? = null,
        @Query("start") start: Int? = null,
        @Query("sort") sort: String? = null
    ): Response<BookResponse>

    @GET("search/book_adv")
    suspend fun searchBookDetail(
        @Query("d_titl") title: String? = null,
        @Query("d_isbn") isbn: String? = null,
    ): Response<BookResponse>
}