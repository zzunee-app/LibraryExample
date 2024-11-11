package com.zzunee.shoppingexample.data.repository

abstract class Repository {
    sealed class Result<out T> {
        data class Success<out T>(val data: T) : Result<T>()
        data object Empty : Result<Nothing>()
        data class Error(val msg: String = "") : Result<Nothing>()
    }

    fun apiErrorMessage(code: Int): String = when (code) {
        400 -> "[$code] API 필수 파라미터를 확인해주세요."
        401 -> "[$code] 헤더 내 API 접근 토큰 유효성을 확인해주세요."
        403 -> "[$code] 서버가 허용하지 않는 호출입니다."
        404 -> "[$code] API를 다시 확인해주세요."
        500 -> "[$code] 서버 오류입니다."
        else -> "문제가 발생했습니다. 잠시 후 다시 시도해주세요."
    }
}