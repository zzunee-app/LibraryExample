package com.zzunee.shoppingexample.model.repository.base

import com.zzunee.shoppingexample.model.db.entity.SearchHistory
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    // 검색 결과
    fun getAll(): Flow<List<SearchHistory>>
    suspend fun deleteAll()
    suspend fun delete(history: SearchHistory)
    suspend fun insert(searchText: String)
}