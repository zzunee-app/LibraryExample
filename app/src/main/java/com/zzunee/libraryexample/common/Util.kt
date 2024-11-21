package com.zzunee.libraryexample.common

import android.content.res.Resources
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object Util {
    fun dpToPx(dp: Int): Int {
        val metrics = Resources.getSystem().displayMetrics
        return (dp * metrics.density).toInt()
    }

    fun formattedPrice(discount: String): String {
        return String.format(Locale.US, "%,d원", discount.toInt())
    }

    fun formattedDate(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun formattedDateWithoutYear(date: Date): String {
        val dateFormat = SimpleDateFormat("MM.dd", Locale.getDefault())
        return dateFormat.format(date)
    }
}