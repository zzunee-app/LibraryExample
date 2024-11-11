package com.zzunee.shoppingexample.view.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.zzunee.shoppingexample.R
import com.zzunee.shoppingexample.databinding.FragmentBookDetailBinding
import com.zzunee.shoppingexample.data.network.BookItem
import kotlinx.coroutines.launch
import com.zzunee.shoppingexample.viewmodel.BookViewModel
import com.zzunee.shoppingexample.viewmodel.RentalViewModel
import com.zzunee.shoppingexample.viewmodel.UiState
import com.zzunee.shoppingexample.viewmodel.ViewModelFactory

class BookDetailFragment : Fragment(), BookRentalBottomFragment.OnDialogClickListener {
    private var _binding: FragmentBookDetailBinding? = null
    private val binding get() = _binding!!

//    private val bookViewModel: BookViewModel by navGraphViewModels(R.id.home) {
//        ViewModelFactory
////        BookViewModel.BookViewModelFactory(
////            (requireActivity().application as ShoppingApplication).dataContainer.bookRepository
////        )
//    }
    private val bookViewModel: BookViewModel by navGraphViewModels(R.id.home) {
        ViewModelFactory
//        BookViewModel.BookViewModelFactory(
//            (requireActivity().application as ShoppingApplication).dataContainer.bookRepository
//        )
    }

    private val rentalViewModel by viewModels<RentalViewModel> {
        ViewModelFactory
//        RentalViewModel.RentalViewModelFactory(
//            (requireActivity().application as ShoppingApplication).dataContainer.rentalRepository
//        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookViewModel.selectedBook.observe(viewLifecycleOwner) { book ->
            bookViewModel.getBookDetail(book)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bookViewModel.detailUiState.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> showLoadingScreen(true)
                        is UiState.Success -> updateDetailLayout(uiState.item.bookItem)
                        is UiState.Error -> showErrorScreen(uiState.msg)
                        else -> {}
                    }
                }
            }
        }

        binding.btnRental.setOnClickListener {
            showRentalDialog()
        }
    }

    private fun showRentalDialog() {
        val modalDialog = BookRentalBottomFragment()
        modalDialog.setDialogClickListener(this)
        modalDialog.show(parentFragmentManager, BookRentalBottomFragment.TAG)
    }

    private fun showLoadingScreen(isVisible: Boolean) {
        binding.loadingLayout.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showErrorScreen(msg: String) {
        showLoadingScreen(false)
        binding.contentsLayout.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE
        binding.txtError.text = msg
    }

    private fun updateDetailLayout(book: BookItem) {
        showLoadingScreen(false)
        binding.contentsLayout.visibility = View.VISIBLE

        Glide.with(binding.imgCover.context)
            .load(book.image)
            .placeholder(R.drawable.ic_image_loading)
            .error(R.drawable.ic_image_broken)
            .into(binding.imgCover)

        binding.book = book
    }

    override fun onConfirm(days: Int) {
        // 대여하기
        val book = bookViewModel.selectedBook.value

        if(book != null) {
            rentalViewModel.rentBook(book.bookItem, days)
        } else {
            Toast.makeText(context, getString(R.string.common_error), Toast.LENGTH_SHORT).show()
        }
    }
}