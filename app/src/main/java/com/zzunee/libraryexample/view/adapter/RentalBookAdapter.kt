package com.zzunee.libraryexample.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zzunee.libraryexample.common.Utils
import com.zzunee.libraryexample.common.ui.SpacingItemDecoration
import com.zzunee.libraryexample.model.db.entity.RentalBook
import com.zzunee.libraryexample.databinding.ItemRentalVerticalBinding

class RentalBookAdapter: RecyclerView.Adapter<VerticalHolder>() {
    private var bookList: Map<String, List<RentalBook>> = emptyMap() // map 으로 넘길것
    private var sortedKeys: List<String> = emptyList()

    fun setList(items: Map<String, List<RentalBook>>) {
        bookList = items
        sortedKeys = bookList.keys.sorted()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalHolder {
        val binding = ItemRentalVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VerticalHolder(binding)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: VerticalHolder, position: Int) {
        holder.bind(sortedKeys[position], bookList)
    }
}

class VerticalHolder(private val binding: ItemRentalVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(key: String, bookList: Map<String, List<RentalBook>>) {
        binding.txtDate.text = "반납일 : $key"

        val horizontalAdapter = RentalBookHorizontalAdapter()
        binding.listHorizontal.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(SpacingItemDecoration(Utils.dpToPx(16), true))
            adapter = horizontalAdapter
        }

        horizontalAdapter.setList(bookList[key]!!)
    }
}