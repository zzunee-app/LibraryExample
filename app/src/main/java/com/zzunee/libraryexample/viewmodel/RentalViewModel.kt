package com.zzunee.libraryexample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzunee.libraryexample.model.db.entity.RentalBook
import com.zzunee.libraryexample.model.network.data.BookItem
import com.zzunee.libraryexample.model.repository.base.RentalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class RentalViewModel(private val rentalRepository: RentalRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<Map<String, List<RentalBook>>>>(UiState.Empty)
    val uiState: StateFlow<UiState<Map<String, List<RentalBook>>>>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            rentalRepository.getAllRentalBook()
                .catch { _uiState.value = UiState.Empty }
                .collect { books ->
                    _uiState.value = if (books.isEmpty()) UiState.Empty else UiState.Success(books)
                }
        }
    }

    // 책 대여
    fun rentBook(bookItem: BookItem, days: Int) {
        viewModelScope.launch {
            rentalRepository.rentBook(bookItem, days)
        }
    }

    // 책 반납
    fun returnBook() {
        viewModelScope.launch {
            rentalRepository.returnBook()
        }
    }
}