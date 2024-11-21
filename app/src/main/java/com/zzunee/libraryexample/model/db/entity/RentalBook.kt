package com.zzunee.libraryexample.model.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zzunee.libraryexample.common.Utils
import java.util.Date

@Entity(tableName = "rental_book")
data class RentalBook(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val author: String,
    val image: String,
    val isbn: String,
    val rentalDate: Date,
    val returnDate: Date,
) {
    fun formattedRentalDate() = Utils.formattedDate(rentalDate)
}