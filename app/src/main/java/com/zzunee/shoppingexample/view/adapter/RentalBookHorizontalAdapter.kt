package com.zzunee.shoppingexample.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zzunee.shoppingexample.R
import com.zzunee.shoppingexample.model.db.entity.RentalBook
import com.zzunee.shoppingexample.databinding.ItemRentalBookBinding

class RentalBookHorizontalAdapter: RecyclerView.Adapter<HorizontalViewHolder>() {
    private var bookList: List<RentalBook> = emptyList()

    fun setList(items: List<RentalBook>) {
        bookList = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalViewHolder {
        val binding =
            ItemRentalBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HorizontalViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: HorizontalViewHolder, position: Int) {
        holder.bind(bookList[position])
    }
}

class HorizontalViewHolder(private val binding: ItemRentalBookBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(book: RentalBook) {
        binding.book = book
//            binding.executePendingBindings()
    }
}