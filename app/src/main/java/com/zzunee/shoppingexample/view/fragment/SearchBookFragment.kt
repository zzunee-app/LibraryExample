package com.zzunee.shoppingexample.view.fragment

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
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zzunee.shoppingexample.R
import com.zzunee.shoppingexample.databinding.FragmentSearchBookBinding
import com.zzunee.shoppingexample.view.adapter.ListBookAdapter
import com.zzunee.shoppingexample.viewmodel.BookViewModel
import com.zzunee.shoppingexample.viewmodel.UiState
import com.zzunee.shoppingexample.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

/**
 * 책 검색 결과 화면
 * bookViewModel > searchUiState
 * 네이버 도서 API 검색 결과에 따른 리스트 호출
 */
class SearchBookFragment : Fragment() {
    private lateinit var binding: FragmentSearchBookBinding
    private val bookViewModel: BookViewModel by navGraphViewModels(R.id.home) { ViewModelFactory }
    private var inputText = ""
    private val bookAdapter by lazy {
        ListBookAdapter(
            onItemClick = { book ->
                // 검색 결과 화면 > 책 상세 화면
                val action = SearchBookFragmentDirections.actionResultToDetail(book = book)
                findNavController().navigate(action)
            },
            onFavorite = { book ->
                // 즐겨찾기 전환 (추가 / 삭제)
                bookViewModel.toggleFavorite(book)
            }
        )
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bookViewModel.searchUiState.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> showLoadingScreen(true)
                        is UiState.Empty -> showListScreen(hasList = false)
                        is UiState.Success -> {
                            showListScreen(hasList = true)
                            bookAdapter.submitList(uiState.item)
                        }
                        is UiState.Error -> showErrorScreen(uiState.msg)
                    }
                }
            }
        }

        binding.resultListView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            setHasFixedSize(true)
            adapter = bookAdapter
        }

        // 검색어
        val args: SearchBookFragmentArgs by navArgs()
        inputText = args.inputText

        bookViewModel.getBookList(inputText)
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

        if(binding.btnRetry.hasOnClickListeners().not()) {
            binding.btnRetry.setOnClickListener {
                bookViewModel.getBookList(inputText)
            }
        }
    }
}