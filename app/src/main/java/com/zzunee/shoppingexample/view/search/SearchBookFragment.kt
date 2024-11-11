package com.zzunee.shoppingexample.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zzunee.shoppingexample.R
import com.zzunee.shoppingexample.databinding.FragmentSearchBookBinding
import com.zzunee.shoppingexample.viewmodel.BookViewModel
import com.zzunee.shoppingexample.viewmodel.SearchViewModel
import com.zzunee.shoppingexample.viewmodel.UiState
import com.zzunee.shoppingexample.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class SearchBookFragment : Fragment() {
    private lateinit var binding: FragmentSearchBookBinding

    private val searchViewModel: SearchViewModel by navGraphViewModels(R.id.nav_search) {
        ViewModelFactory
    }

    private val bookViewModel: BookViewModel by navGraphViewModels(R.id.home) {
        ViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookAdapter = SearchBookAdapter(
            onItemClick = { book ->
                // 검색 결과 > 책 선택
                bookViewModel.selectBook(book)
                findNavController().navigate(R.id.action_result_to_detail)
            },
            onFavorite = { book ->  // 선택 or 삭제
                bookViewModel.toggleFavorite(book)
            }
        )

        binding.resultListView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            setHasFixedSize(true)
            adapter = bookAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    searchViewModel.inputText.collect { input ->
                        bookViewModel.getBookList(input)
                    }
                }

                launch {
                    bookViewModel.uiState.collect { uiState ->
                        when (uiState) {
                            is UiState.Loading -> showLoadingScreen(true)
                            is UiState.Success -> {
                                if (uiState.item.isEmpty()) {
                                    showListScreen(hasList = false)
                                } else {
                                    showListScreen(hasList = true)
                                    bookAdapter.submitList(uiState.item)
                                }
                            }

                            is UiState.Error -> showErrorScreen(uiState.msg)
                            else -> { }
                        }
                    }
                }
            }
        }

        binding.btnRetry.setOnClickListener {
            bookViewModel.getBookList(searchViewModel.inputText.value)
        }
    }

    private fun showLoadingScreen(isVisible: Boolean) {
        binding.loadingLayout.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showListScreen(hasList: Boolean = false) {
        showLoadingScreen(false)
        if(hasList) {
            binding.emptyLayout.visibility = View.GONE
            binding.resultListView.visibility = View.VISIBLE
        } else {
            binding.emptyLayout.visibility = View.VISIBLE
            binding.resultListView.visibility = View.GONE
        }
    }

    private fun showErrorScreen(msg: String) {
        showLoadingScreen(false)
        binding.emptyLayout.visibility = View.GONE
        binding.resultListView.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE

        binding.txtError.text = msg
        binding.btnRetry.setOnClickListener {
            bookViewModel.getBookList(searchViewModel.inputText.value)
        }
    }
}