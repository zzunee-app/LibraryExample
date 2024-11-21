package com.zzunee.libraryexample.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zzunee.libraryexample.common.utils.DiffCallback
import com.zzunee.libraryexample.databinding.ItemRentalBookBinding
import com.zzunee.libraryexample.model.db.entity.RentalBook

class RentalBookHorizontalAdapter :
    ListAdapter<RentalBook, HorizontalViewHolder>(RentalBookDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalViewHolder {
        val binding =
            ItemRentalBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HorizontalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HorizontalViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }
}

class RentalBookDiffCallback : DiffCallback<RentalBook>() {
    override fun areItemsTheSame(old: RentalBook, new: RentalBook): Boolean {
        return old.id == new.id
    }
}

class HorizontalViewHolder(private val binding: ItemRentalBookBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(book: RentalBook) {
        binding.book = book
    }
}