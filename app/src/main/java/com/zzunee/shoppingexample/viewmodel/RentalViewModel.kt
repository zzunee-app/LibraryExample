package com.zzunee.shoppingexample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzunee.shoppingexample.data.db.entity.RentalBook
import com.zzunee.shoppingexample.data.network.BookItem
import com.zzunee.shoppingexample.data.network.toRentalBook
import com.zzunee.shoppingexample.data.repository.RentalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RentalViewModel(private val rentalRepository: RentalRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<Map<String, List<RentalBook>>>>(UiState.Loading)
    val uiState: StateFlow<UiState<Map<String, List<RentalBook>>>> = _uiState
    
    fun rentBook(bookItem: BookItem, days: Int) {
        viewModelScope.launch {
            val today = Date() // 대여일
            val calendar = Calendar.getInstance()
            calendar.time = today
            calendar.add(Calendar.DAY_OF_YEAR, days)
            val returnDate = calendar.time // 반납일

            rentalRepository.rentBook(bookItem.toRentalBook(today, returnDate))
        }
    }

    fun returnBook() {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            val today = Calendar.getInstance().time
            println(today)
            rentalRepository.returnBook(today)
            getAllRentalBook()
        }
    }

    private fun getAllRentalBook() {
        viewModelScope.launch {
            val bookList = rentalRepository.getAllRentalBook().first()
            val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            val bookMap = bookList.groupBy { dateFormat.format(it.returnDate) }
            _uiState.value = UiState.Success(bookMap)
        }
    }
}