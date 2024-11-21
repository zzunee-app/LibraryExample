package com.zzunee.libraryexample.common.utils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.zzunee.libraryexample.model.network.data.Book

abstract class DiffCallback<T : Any> : DiffUtil.ItemCallback<T>() {
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}

class BookDiffCallback : DiffCallback<Book>() {
    override fun areItemsTheSame(old: Book, new: Book): Boolean {
        val oldBookItem = old.bookItem
        val newBookItem = new.bookItem
        return (oldBookItem.title == newBookItem.title) && (oldBookItem.isbn == newBookItem.isbn)
    }
}

class IntDiffCallback : DiffCallback<Int>() {
    override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }
}