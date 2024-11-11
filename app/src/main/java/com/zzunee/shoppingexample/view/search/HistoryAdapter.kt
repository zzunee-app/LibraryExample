package com.zzunee.shoppingexample.view.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zzunee.shoppingexample.common.ui.DiffCallback
import com.zzunee.shoppingexample.data.db.entity.SearchHistory
import com.zzunee.shoppingexample.databinding.ItemSearchHistoryBinding

class HistoryAdapter(
    private val onItemClick: (SearchHistory) -> Unit,
    private val onDeleteClick: (SearchHistory) -> Unit,
) : ListAdapter<SearchHistory, HistoryAdapter.HistoryViewHolder>(
    DiffCallback(
        areItemsSame = { old, new -> old.id == new.id }
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding =
            ItemSearchHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

    inner class HistoryViewHolder(private val binding: ItemSearchHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: SearchHistory) {
            binding.btnDelete.setOnClickListener {
                onDeleteClick(history)
            }

            binding.historyLayout.setOnClickListener {
                onItemClick(history)
            }

            binding.history = history
            binding.executePendingBindings()
        }
    }
}