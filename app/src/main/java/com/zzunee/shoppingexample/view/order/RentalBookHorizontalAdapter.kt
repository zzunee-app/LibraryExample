package com.zzunee.shoppingexample.view.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zzunee.shoppingexample.R
import com.zzunee.shoppingexample.data.db.entity.RentalBook
import com.zzunee.shoppingexample.databinding.ItemRentalBookBinding

class RentalBookHorizontalAdapter: RecyclerView.Adapter<RentalBookHorizontalAdapter.HorizontalViewHolder>() {
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

    inner class HorizontalViewHolder(private val binding: ItemRentalBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: RentalBook) {
            Glide.with(binding.ivThumbnail.context)
                .load(book.image)
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_image_broken)
                .into(binding.ivThumbnail)

            binding.book = book
//            binding.executePendingBindings()
        }
    }
}