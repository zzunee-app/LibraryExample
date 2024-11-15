package com.zzunee.shoppingexample.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zzunee.shoppingexample.common.ui.BookDiffCallback
import com.zzunee.shoppingexample.databinding.ItemListBookBinding
import com.zzunee.shoppingexample.model.network.data.Book

class ListBookAdapter(
    private val onItemClick: (Book) -> Unit,
    private val onFavorite: (Book) -> Unit,
) : ListAdapter<Book, ListBookViewHolder>(BookDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListBookViewHolder {
        val binding =
            ItemListBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListBookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListBookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book, onItemClick, onFavorite)
    }
}

class ListBookViewHolder(private val binding: ItemListBookBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(book: Book, onSelected: (Book) -> Unit, onFavorite: (Book) -> Unit) {
        binding.bookLayout.setOnClickListener { onSelected(book) }
        binding.ivFavorite.setOnClickListener { onFavorite(book) }
        binding.ivFavorite.isSelected = book.isFavorite

        binding.book = book.bookItem
        binding.executePendingBindings()
    }
}