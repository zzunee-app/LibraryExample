package com.zzunee.libraryexample.model.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zzunee.libraryexample.common.Util
import java.util.Date

@Entity(tableName = "search_history")
data class SearchHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val date: Date
) {
    fun formattedDate() = Util.formattedDateWithoutYear(date)
}