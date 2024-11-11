package com.zzunee.shoppingexample.view.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zzunee.shoppingexample.R
import com.zzunee.shoppingexample.databinding.FragmentSearchBinding
import com.zzunee.shoppingexample.viewmodel.SearchViewModel
import com.zzunee.shoppingexample.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by navGraphViewModels(R.id.nav_search) {
        ViewModelFactory
//        SearchViewModel.SearchViewModelFactory(
//            (requireActivity().application as ShoppingApplication).dataContainer.searchRepository
//        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fullSearchBar.requestFocus()

        binding.fullSearchBar.setOnEditorActionListener { textView, action, _ ->
            if(action == EditorInfo.IME_ACTION_SEARCH) {
                val input = textView.text.toString()
                // inputText Room 저장
                if(input.isNotEmpty()) {
                    searchViewModel.insertHistory(input)
                }

                search(input)
                true
            } else {
                false
            }
        }

        binding.btnDeleteAll.setOnClickListener {
            searchViewModel.deleteAllHistory()
        }

        fetchSearchHistory()
    }

    private fun fetchSearchHistory() {
        val historyAdapter = HistoryAdapter(
            onItemClick = {
                search(it.title)
            },
            onDeleteClick = {
                searchViewModel.deleteHistory(it)
            }
        )

        binding.historyListView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            adapter = historyAdapter
        }

        // 리스트 띄우기
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.histories.collect {
                    historyAdapter.submitList(it)
                }
            }
        }
    }

    private fun search(title: String = "") {
        if(title.isEmpty()) {
            // 입력 텍스트 없으면 홈
            findNavController().navigateUp()
        } else {
            // 입력 텍스트 있으면 API 검색
            searchViewModel.updateInputText(title)
            findNavController().navigate(R.id.action_search_to_result)
        }

        hideKeyboard(binding.fullSearchBar)
    }

    private fun hideKeyboard(v: View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}