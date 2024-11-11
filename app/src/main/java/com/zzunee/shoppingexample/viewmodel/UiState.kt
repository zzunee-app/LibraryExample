package com.zzunee.shoppingexample.viewmodel

sealed class UiState<out T> {
    data object Loading: UiState<Nothing>()
    data class Error(val msg: String = ""): UiState<Nothing>()
    data object Empty: UiState<Nothing>()
    data class Success<T>(val item: T): UiState<T>()
}