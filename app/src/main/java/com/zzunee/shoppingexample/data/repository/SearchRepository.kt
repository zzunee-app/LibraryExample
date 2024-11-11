package com.zzunee.shoppingexample.data.repository

import com.zzunee.shoppingexample.data.db.dao.SearchHistoryDao
import com.zzunee.shoppingexample.data.db.entity.SearchHistory
import kotlinx.coroutines.flow.Flow

class SearchRepository(private val historyDao: SearchHistoryDao) {
    // 검색 결과
    fun getAll(): Flow<List<SearchHistory>> = historyDao.getAll()
    suspend fun delete() = historyDao.delete()
    suspend fun delete(history: SearchHistory) = historyDao.delete(history)
    suspend fun insert(history: SearchHistory) = historyDao.insert(history)
}