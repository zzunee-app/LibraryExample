package com.zzunee.shoppingexample.view.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zzunee.shoppingexample.common.ui.DiffCallback
import com.zzunee.shoppingexample.databinding.ItemFavoriteBinding
import com.zzunee.shoppingexample.data.network.Book

class FavoriteAdapter(
    private val onItemClick: (Book) -> Unit,
    private val onFavorite: (Book) -> Unit,
) : ListAdapter<Book, FavoriteAdapter.BookViewHolder>(
    DiffCallback(
        areItemsSame = { old, new -> old.bookItem.title == new.bookItem.title }
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding =
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    inner class BookViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.bookLayout.setOnClickListener { onItemClick(book) }
            binding.ivFavorite.setOnClickListener { onFavorite(book) }
            binding.ivFavorite.isSelected = book.isFavorite
            binding.book = book.bookItem
        }
    }
}