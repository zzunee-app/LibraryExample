package com.zzunee.shoppingexample.model.network.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Book(
    val bookItem: @RawValue BookItem,
    val isFavorite: Boolean = false
): Parcelable