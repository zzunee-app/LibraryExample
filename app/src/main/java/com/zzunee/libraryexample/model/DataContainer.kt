package com.zzunee.libraryexample.model

import android.content.Context
import com.zzunee.libraryexample.model.db.LibraryDatabase
import com.zzunee.libraryexample.model.repository.impl.BookRepositoryImpl
import com.zzunee.libraryexample.model.repository.impl.SearchRepositoryImpl
import com.zzunee.libraryexample.model.network.NetworkModule
import com.zzunee.libraryexample.model.repository.impl.RentalRepositoryImpl

class DataContainer(context: Context) {
    val searchRepository by lazy {
        SearchRepositoryImpl(LibraryDatabase.getDatabase(context).historyDao)
    }

    val bookRepository by lazy {
        BookRepositoryImpl(
            NetworkModule.apiService,
            LibraryDatabase.getDatabase(context).favoriteBookDao
        )
    }

    val rentalRepository by lazy {
        RentalRepositoryImpl(
            LibraryDatabase.getDatabase(context).bookRentalDao
        )
    }
}