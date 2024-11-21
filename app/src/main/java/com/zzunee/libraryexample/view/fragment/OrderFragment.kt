package com.zzunee.libraryexample.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.zzunee.libraryexample.common.Utils
import com.zzunee.libraryexample.common.ui.SpacingItemDecoration
import com.zzunee.libraryexample.model.db.entity.RentalBook
import com.zzunee.libraryexample.databinding.FragmentOrderBinding
import com.zzunee.libraryexample.view.adapter.RentalBookAdapter
import com.zzunee.libraryexample.viewmodel.RentalViewModel
import com.zzunee.libraryexample.viewmodel.UiState
import com.zzunee.libraryexample.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

/**
 * 대여 내역 화면
 * rentalViewModel > uiState
 * 대여한 책을 반납일 기준으로 확인
 * 반납일이 지나면 DB에서 삭제
 */
class OrderFragment : Fragment() {
    private lateinit var binding : FragmentOrderBinding
    private val rentalViewModel by viewModels<RentalViewModel> { ViewModelFactory }
    private val rentalAdapter by lazy { RentalBookAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                rentalViewModel.uiState.collect { state ->
                    when(state) {
                        is UiState.Success -> {
                            showContentsScreen(state.item)
                        }
                        else -> showEmptyScreen(true)
                    }
                }
            }
        }

        binding.listRental.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SpacingItemDecoration(Utils.dpToPx(16)))
            adapter = rentalAdapter
        }

        // 오늘 날짜 반납 확인
        rentalViewModel.returnBook()
    }

    private fun showContentsScreen(items: Map<String, List<RentalBook>>) {
        showEmptyScreen(false)
        binding.listRental.visibility = View.VISIBLE
        rentalAdapter.setList(items)
    }

    private fun showEmptyScreen(isVisible: Boolean) {
        binding.emptyLayout.visibility = if(isVisible) View.VISIBLE else View.GONE
    }
}