package com.zzunee.libraryexample.model.repository.impl

import com.zzunee.libraryexample.model.db.dao.SearchHistoryDao
import com.zzunee.libraryexample.model.db.entity.SearchHistory
import com.zzunee.libraryexample.model.repository.BaseRepository
import com.zzunee.libraryexample.model.repository.base.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.util.Calendar

class SearchRepositoryImpl(private val historyDao: SearchHistoryDao) : SearchRepository,
    BaseRepository() {
    // 검색 결과
    override fun getAll(): Flow<List<SearchHistory>> = historyDao.getAll()
        .catch { emit(emptyList()) }

    override suspend fun deleteAll() = callQuery { historyDao.deleteAll() }
    override suspend fun delete(history: SearchHistory) = callQuery { historyDao.delete(history) }
    override suspend fun insert(searchText: String) = callQuery {
        historyDao.insert(
            SearchHistory(
                title = searchText,
                date = Calendar.getInstance().time
            )
        )
    }
}