package com.zzunee.libraryexample.model.repository

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data object Empty : Result<Nothing>()
    data class Error(val msg: String = "") : Result<Nothing>()
}