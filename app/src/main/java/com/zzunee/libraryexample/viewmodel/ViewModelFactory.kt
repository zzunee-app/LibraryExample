package com.zzunee.libraryexample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.zzunee.libraryexample.LibraryApplication

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val application = checkNotNull(extras[APPLICATION_KEY])
        val dataContainer = (application as LibraryApplication).dataContainer

        return when (modelClass) {
            BookViewModel::class.java -> {
                BookViewModel(bookRepository = dataContainer.bookRepository) as T
            }
            RentalViewModel::class.java -> {
                RentalViewModel(rentalRepository = dataContainer.rentalRepository) as T
            }
            SearchViewModel::class.java -> {
                SearchViewModel(searchRepository = dataContainer.searchRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}