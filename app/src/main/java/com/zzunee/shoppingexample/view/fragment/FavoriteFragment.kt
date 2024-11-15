package com.zzunee.shoppingexample.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.zzunee.shoppingexample.R
import com.zzunee.shoppingexample.common.Utils
import com.zzunee.shoppingexample.common.ui.GridItemDecoration
import com.zzunee.shoppingexample.databinding.FragmentFavoriteBinding
import com.zzunee.shoppingexample.view.adapter.GridBookAdapter
import com.zzunee.shoppingexample.viewmodel.BookViewModel
import com.zzunee.shoppingexample.viewmodel.UiState
import com.zzunee.shoppingexample.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

/**
 * 홈화면
 * bookViewModel > favoriteUiState
 * 즐겨찾기한 책 목록이 그리드로 노출됨
 */
class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val bookViewModel by navGraphViewModels<BookViewModel>(R.id.home) { ViewModelFactory }
    private val favoriteAdapter by lazy {
        GridBookAdapter(
            onItemClick = { book ->
                // 홈 > 즐겨찾기 리스트 > 책 선택 > 상세 화면 랜딩
                val action = FavoriteFragmentDirections.actionFavoriteToDetail(book = book)
                findNavController().navigate(action)
            },
            onFavorite = { book ->
                // 홈 > 즐겨찾기 리스트 > 하트 선택 > 좋아요 삭제
                bookViewModel.toggleFavorite(book)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                //  검색 버튼 추가
                menuInflater.inflate(R.menu.home_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // 검색 버튼 선택
                findNavController().navigate(R.id.action_favorite_to_search)
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bookViewModel.favoriteUiState.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            showEmptyScreen(false)
                            favoriteAdapter.submitList(state.item)
                        }

                        else -> showEmptyScreen(true) // 빈화면
                    }
                }
            }
        }

        binding.favoriteListView.apply {
            val GRID_COUNT = 2
            layoutManager = GridLayoutManager(context, GRID_COUNT)
            addItemDecoration(GridItemDecoration(GRID_COUNT, Utils.dpToPx(16), true))
            adapter = favoriteAdapter
        }
    }


    // 빈 화면 로딩
    private fun showEmptyScreen(isVisible: Boolean) {
        if (isVisible) {
            binding.emptyLayout.visibility = View.VISIBLE
            binding.favoriteListView.visibility = View.GONE
        } else {
            binding.emptyLayout.visibility = View.GONE
            binding.favoriteListView.visibility = View.VISIBLE
        }
    }
}