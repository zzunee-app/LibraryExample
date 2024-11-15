package com.zzunee.shoppingexample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzunee.shoppingexample.model.db.entity.SearchHistory
import com.zzunee.shoppingexample.model.repository.base.SearchRepository
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

    fun insertHistory(input: String) {
        viewModelScope.launch {
            searchRepository.insert(input)
        }
    }

    fun deleteHistory(history: SearchHistory? = null) {
        viewModelScope.launch {
            if(history == null) {
                searchRepository.deleteAll()
            } else {
                searchRepository.delete(history)
            }
        }
    }
}