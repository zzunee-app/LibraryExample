package com.zzunee.shoppingexample.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zzunee.shoppingexample.common.ui.IntDiffCallback
import com.zzunee.shoppingexample.databinding.ItemSelectedBinding

class BookRentalAdapter(private val onItemClicked: (Int) -> Unit) :
    ListAdapter<Int, BookRentalAdapter.RentalViewHolder>(IntDiffCallback()) {
    private val items = listOf(1, 3, 7, 30) // 대여 일자
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentalViewHolder {
        val binding =
            ItemSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RentalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RentalViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class RentalViewHolder(private val binding: ItemSelectedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pos: Int) {
            binding.txtDay.text = "${items[pos]}일"
            binding.ivCheck.visibility = if (pos == selectedPosition) View.VISIBLE else View.INVISIBLE

            itemView.setOnClickListener {
                notifyItemChanged(selectedPosition)
                selectedPosition = pos
                notifyItemChanged(selectedPosition)
                onItemClicked(items[selectedPosition])
            }
        }
    }
}