package com.zzunee.libraryexample.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zzunee.libraryexample.common.ui.BookDiffCallback
import com.zzunee.libraryexample.databinding.ItemGridBookBinding
import com.zzunee.libraryexample.model.network.data.Book

class GridBookAdapter(
    private val onItemClick: (Book) -> Unit,
    private val onFavorite: (Book) -> Unit,
) : ListAdapter<Book, GridBookViewHolder>(BookDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridBookViewHolder {
        val binding =
            ItemGridBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridBookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GridBookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book, onItemClick, onFavorite)
    }
}

class GridBookViewHolder(private val binding: ItemGridBookBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(book: Book, onSelected: (Book) -> Unit, onFavorite: (Book) -> Unit) {
        binding.bookLayout.setOnClickListener { onSelected(book) }
        binding.ivFavorite.setOnClickListener { onFavorite(book) }
        binding.ivFavorite.isSelected = book.isFavorite

        binding.book = book.bookItem
        binding.executePendingBindings()
    }
}