package com.zzunee.libraryexample.model.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_book")
data class FavoriteBook(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val author: String,
    val pubdate: String,
    val image: String,
    val isbn: String,
)