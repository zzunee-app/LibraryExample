package com.zzunee.libraryexample.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zzunee.libraryexample.common.Util
import com.zzunee.libraryexample.common.ui.SpacingItemDecoration
import com.zzunee.libraryexample.model.db.entity.RentalBook
import com.zzunee.libraryexample.databinding.ItemRentalVerticalBinding

class RentalBookAdapter: RecyclerView.Adapter<VerticalHolder>() {
    private var bookList: Map<String, List<RentalBook>> = emptyMap() // map 으로 넘길것
    private var sortedKeys: List<String> = emptyList()

    fun setList(items: Map<String, List<RentalBook>>) {
        val diffCallback = RentalBookMapDiffCallback(bookList, items)
        val result = DiffUtil.calculateDiff(diffCallback)
        bookList = items
        sortedKeys = bookList.keys.toList()
        result.dispatchUpdatesTo(this)
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
    private val horizontalAdapter by lazy { RentalBookHorizontalAdapter() }

    init {
        binding.listHorizontal.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(SpacingItemDecoration(Util.dpToPx(16), true))
            adapter = horizontalAdapter
        }
    }

    fun bind(key: String, bookList: Map<String, List<RentalBook>>) {
        binding.txtDate.text = "반납일 : $key"
        horizontalAdapter.submitList(bookList[key])
    }
}

class RentalBookMapDiffCallback(
    private val oldMap: Map<String, List<RentalBook>>,
    private val newMap: Map<String, List<RentalBook>>
) : DiffUtil.Callback() {

    private val oldKeys = oldMap.keys.toList()
    private val newKeys = newMap.keys.toList()

    override fun getOldListSize(): Int = oldKeys.size
    override fun getNewListSize(): Int = newKeys.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldKeys[oldItemPosition] == newKeys[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldKey = oldKeys[oldItemPosition]
        val newKey = newKeys[newItemPosition]

        return oldMap[oldKey] == newMap[newKey]
    }
}
