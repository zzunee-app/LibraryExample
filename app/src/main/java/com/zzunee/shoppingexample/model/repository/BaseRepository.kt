package com.zzunee.shoppingexample.model.repository

import android.database.sqlite.SQLiteException
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

open class BaseRepository {
    suspend fun <T> callApi(call: suspend () -> Response<T>): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = call()

                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        Result.Success(body)
                    } ?: Result.Empty
                } else {
                    Result.Error(networkErrorMsg(response.code()))
                }
            } catch (e: HttpException) {
                Result.Error(networkErrorMsg(e.code()))
            } catch (e: Exception) {
                Result.Error(networkErrorMsg())
            }
        }
    }

    suspend fun callQuery(query: suspend () -> Unit) {
        try {
            query()
        } catch (e: SQLiteException) {
            Log.e("SQLiteException", queryErrorMsg(e.message))
        } catch (e: Exception) {
            Log.e("Exception", queryErrorMsg(e.message))
        }
    }

    private fun networkErrorMsg(code: Int = 0): String = when (code) {
        400 -> "[$code] API 필수 파라미터를 확인해주세요."
        401 -> "[$code] 헤더 내 API 접근 토큰 유효성을 확인해주세요."
        403 -> "[$code] 서버가 허용하지 않는 호출입니다."
        404 -> "[$code] API를 다시 확인해주세요."
        500 -> "[$code] 서버 오류입니다."
        else -> "[$code] 문제가 발생했습니다. 잠시 후 다시 시도해주세요."
    }

    private fun queryErrorMsg(msg: String?) = msg ?: "데이터 처리에 문제가 발생했습니다."
}