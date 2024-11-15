package com.zzunee.shoppingexample.model

import android.content.Context
import com.zzunee.shoppingexample.model.db.ShoppingDatabase
import com.zzunee.shoppingexample.model.repository.impl.BookRepositoryImpl
import com.zzunee.shoppingexample.model.repository.impl.SearchRepositoryImpl
import com.zzunee.shoppingexample.model.network.NetworkModule
import com.zzunee.shoppingexample.model.repository.impl.RentalRepositoryImpl

class DataContainer(context: Context) {
    val searchRepository by lazy {
        SearchRepositoryImpl(ShoppingDatabase.getDatabase(context).historyDao)
    }

    val bookRepository by lazy {
        BookRepositoryImpl(
            NetworkModule.apiService,
            ShoppingDatabase.getDatabase(context).favoriteBookDao
        )
    }

    val rentalRepository by lazy {
        RentalRepositoryImpl(
            ShoppingDatabase.getDatabase(context).bookRentalDao
        )
    }
}