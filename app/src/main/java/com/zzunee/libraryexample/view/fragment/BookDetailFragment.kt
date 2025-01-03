package com.zzunee.libraryexample.view.fragment

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
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.zzunee.libraryexample.R
import com.zzunee.libraryexample.databinding.FragmentBookDetailBinding
import com.zzunee.libraryexample.model.network.data.Book
import com.zzunee.libraryexample.model.network.data.BookItem
import kotlinx.coroutines.launch
import com.zzunee.libraryexample.viewmodel.BookViewModel
import com.zzunee.libraryexample.viewmodel.RentalViewModel
import com.zzunee.libraryexample.viewmodel.UiState
import com.zzunee.libraryexample.viewmodel.ViewModelFactory

/**
 * 책 상세 화면
 * bookViewModel > detailUiState
 * 선택한 책의 상세 내용을 API 호출
 * 하단 플로팅 버튼을 통해 대여 가능
 */
class BookDetailFragment : Fragment() {
    private lateinit var binding: FragmentBookDetailBinding
    private val bookViewModel: BookViewModel by navGraphViewModels(R.id.home) { ViewModelFactory }
    private val rentalViewModel by viewModels<RentalViewModel> { ViewModelFactory }
    private var selectedBook: Book? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bookViewModel.detailUiState.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> showLoadingScreen(true)
                        is UiState.Success -> showContentsScreen(uiState.item.bookItem)
                        is UiState.Error -> showErrorScreen(uiState.msg)
                        else -> showErrorScreen(getString(R.string.common_error))
                    }
                }
            }
        }

        binding.btnRental.setOnClickListener {
            showRentalDialog()
        }

        // 선택한 책
        val args: BookDetailFragmentArgs by navArgs()
        selectedBook = args.book

        bookViewModel.getBookDetail(selectedBook)
    }

    private fun showRentalDialog() {

        if(parentFragmentManager.findFragmentByTag(BookRentalBottomFragment.TAG) == null) {
            val modalDialog = BookRentalBottomFragment { days ->
                // 대여하기
                selectedBook?.let {
                    rentalViewModel.rentBook(it.bookItem, days)
                } ?: Toast.makeText(requireContext(), getString(R.string.common_error), Toast.LENGTH_SHORT).show()
            }
            modalDialog.show(parentFragmentManager, BookRentalBottomFragment.TAG)
        }
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

    private fun showContentsScreen(book: BookItem) {
        showLoadingScreen(false)
        binding.contentsLayout.visibility = View.VISIBLE
        println(book)
        binding.book = book
    }
}