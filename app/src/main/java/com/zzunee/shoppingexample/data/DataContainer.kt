package com.zzunee.shoppingexample.data

import android.content.Context
import com.zzunee.shoppingexample.data.db.ShoppingDatabase
import com.zzunee.shoppingexample.data.repository.BookRepository
import com.zzunee.shoppingexample.data.repository.SearchRepository
import com.zzunee.shoppingexample.data.network.NetworkModule
import com.zzunee.shoppingexample.data.repository.RentalRepository

class DataContainer(context: Context) {
    val searchRepository by lazy {
        SearchRepository(ShoppingDatabase.getDatabase(context).historyDao)
    }

    val bookRepository by lazy {
        BookRepository(
            NetworkModule.apiService,
            ShoppingDatabase.getDatabase(context).favoriteBookDao
        )
    }

    val rentalRepository by lazy {
        RentalRepository(
            ShoppingDatabase.getDatabase(context).bookRentalDao
        )
    }
}