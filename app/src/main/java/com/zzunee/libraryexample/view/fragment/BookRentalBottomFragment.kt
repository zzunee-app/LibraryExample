package com.zzunee.libraryexample.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zzunee.libraryexample.databinding.FragmentRentalBinding
import com.zzunee.libraryexample.view.adapter.BookRentalAdapter

/**
 * 책 대여 화면 (모달 바텀 프래그먼트)
 * 선택한 책의 대여 기간 선택
 */
class BookRentalBottomFragment(val onItemClicked: (Int) -> Unit) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentRentalBinding
    private var days = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRentalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rentalAdapter = BookRentalAdapter(onItemClicked = { selectedDays ->
            days = selectedDays
        })

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnConfirm.setOnClickListener {
            onItemClicked(days)
            dismiss()
        }

        binding.listPeriod.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rentalAdapter
        }
    }

    companion object {
        const val TAG = "RentalBottomSheet"
    }
}