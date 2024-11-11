package com.zzunee.shoppingexample.data.util

import androidx.room.TypeConverter
import java.util.Date

class Converter {
    @TypeConverter
    fun timeToDate(time: Long): Date {
        return Date(time)
    }

    @TypeConverter
    fun dateToTime(date: Date): Long {
        return date.time
    }
}