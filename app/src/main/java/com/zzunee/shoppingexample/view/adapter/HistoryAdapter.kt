package com.zzunee.shoppingexample.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zzunee.shoppingexample.common.ui.DiffCallback
import com.zzunee.shoppingexample.model.db.entity.SearchHistory
import com.zzunee.shoppingexample.databinding.ItemSearchHistoryBinding

class HistoryAdapter(
    private val onItemClick: (SearchHistory) -> Unit,
    private val onDeleteClick: (SearchHistory) -> Unit,
) : ListAdapter<SearchHistory, HistoryViewHolder>(HistoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding =
            ItemSearchHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history, onDeleteClick, onItemClick)
    }
}

class HistoryViewHolder(private val binding: ItemSearchHistoryBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(history: SearchHistory, onDelete: (SearchHistory) -> Unit, onSelected: (SearchHistory) -> Unit) {
        binding.btnDelete.setOnClickListener {
            onDelete(history)
        }

        binding.historyLayout.setOnClickListener {
            onSelected(history)
        }

        binding.history = history
//        binding.executePendingBindings()
    }
}

class HistoryDiffCallback: DiffCallback<SearchHistory>() {
    override fun areItemsTheSame(oldItem: SearchHistory, newItem: SearchHistory): Boolean {
        return oldItem.id == newItem.id
    }
}