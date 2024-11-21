package com.zzunee.libraryexample.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zzunee.libraryexample.R
import com.zzunee.libraryexample.databinding.FragmentSearchBinding
import com.zzunee.libraryexample.view.adapter.HistoryAdapter
import com.zzunee.libraryexample.viewmodel.SearchViewModel
import com.zzunee.libraryexample.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

/**
 * 검색 화면
 * searchViewModel > histories
 * Room에서 검색 기록 조회/추가/삭제
 */
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by navGraphViewModels(R.id.nav_search) { ViewModelFactory }

    private val historyAdapter by lazy {
        HistoryAdapter(
            onItemClick = {
                search(it.title)
            },
            onDeleteClick = {
                searchViewModel.deleteHistory(it)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.histories.collect {
                    historyAdapter.submitList(it)
                }
            }
        }

        binding.fullSearchBar.requestFocus()
        binding.fullSearchBar.setOnEditorActionListener { textView, action, _ ->
            if (action == EditorInfo.IME_ACTION_SEARCH) {
                val inputText = textView.text.toString()

                if(inputText.isEmpty()) {
                    // 입력 텍스트 없음 > 홈 화면
                    findNavController().navigateUp()
                    hideKeyboard(textView)
                } else {
                    // 입력 텍스트 있음 > 책 검색 화면
                    searchViewModel.insertHistory(inputText)
                    search(inputText)
                }
                true
            } else {
                false
            }
        }

        binding.btnDeleteAll.setOnClickListener {
            searchViewModel.deleteHistory()
        }

        binding.historyListView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            adapter = historyAdapter
        }
    }

    private fun search(title: String = "") {
        val action = SearchFragmentDirections.actionSearchToResult(inputText = title)
        findNavController().navigate(action)
        hideKeyboard(binding.fullSearchBar)
    }

    private fun hideKeyboard(v: View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
}