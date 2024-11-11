package com.zzunee.shoppingexample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzunee.shoppingexample.data.repository.SearchRepository
import com.zzunee.shoppingexample.data.db.entity.SearchHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {
    val histories: StateFlow<List<SearchHistory>> = searchRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText

    fun updateInputText(input: String) {
        _inputText.value = input
    }

    fun insertHistory(input: String) {
        viewModelScope.launch {
            searchRepository.insert(
                SearchHistory(
                    title = input,
                    date = Calendar.getInstance().time
                )
            )
        }
    }

    fun deleteAllHistory() {
        viewModelScope.launch {
            searchRepository.delete()
        }
    }

    fun deleteHistory(history: SearchHistory) {
        viewModelScope.launch {
            searchRepository.delete(history)
        }
    }
}