package com.zzunee.shoppingexample.view.order

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
import com.zzunee.shoppingexample.common.Utils
import com.zzunee.shoppingexample.common.ui.SpacingItemDecoration
import com.zzunee.shoppingexample.data.db.entity.RentalBook
import com.zzunee.shoppingexample.databinding.FragmentOrderBinding
import com.zzunee.shoppingexample.viewmodel.RentalViewModel
import com.zzunee.shoppingexample.viewmodel.UiState
import com.zzunee.shoppingexample.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class OrderFragment : Fragment() {
    private lateinit var binding : FragmentOrderBinding

    private val rentalViewModel by viewModels<RentalViewModel> {
        ViewModelFactory
//        RentalViewModel.RentalViewModelFactory(
//            (requireActivity().application as ShoppingApplication).dataContainer.rentalRepository
//        )
    }

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
                        is UiState.Loading -> showLoadingScreen(true)
                        is UiState.Error -> {}
                        is UiState.Success -> {
                            if(state.item.isEmpty()) {
                                showEmptyScreen(true)
                            } else {
                                showContentsScreen(state.item)
                            }
                        }
                        else -> { }
                    }
                }
            }
        }

        // todo 날짜 지난 데이터 삭제하기

        rentalViewModel.returnBook()
    }

    private fun showContentsScreen(items: Map<String, List<RentalBook>>) {
        showEmptyScreen(false)
        binding.listRental.visibility = View.VISIBLE

        val rentalAdapter = RentalBookAdapter()
        binding.listRental.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SpacingItemDecoration(Utils.dpToPx(16)))
            adapter = rentalAdapter
        }

        rentalAdapter.setList(items)
    }

    private fun showEmptyScreen(isVisible: Boolean) {
        showLoadingScreen(false)
        binding.emptyLayout.visibility = if(isVisible) View.VISIBLE else View.GONE
    }

    private fun showLoadingScreen(isVisible: Boolean) {
        binding.loadingLayout.visibility = if(isVisible) View.VISIBLE else View.GONE
    }
}