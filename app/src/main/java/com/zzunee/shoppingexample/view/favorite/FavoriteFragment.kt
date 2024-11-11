package com.zzunee.shoppingexample.view.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.zzunee.shoppingexample.viewmodel.BookViewModel
import com.zzunee.shoppingexample.viewmodel.UiState
import com.zzunee.shoppingexample.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

/**
 * 홈화면
 */
class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val GRID_COUNT = 2

    //    private val bookViewModel: BookViewModel by viewModels {
//        ViewModelFactory
//    }
    private val bookViewModel by navGraphViewModels<BookViewModel>(R.id.home) { ViewModelFactory }

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

        val favoriteAdapter = FavoriteAdapter(
            onItemClick = { book ->
                // 홈 > 즐겨찾기 리스트 > 책 선택 > 상세 화면 랜딩
                bookViewModel.selectBook(book)
                findNavController().navigate(R.id.action_favorite_to_detail)
            },
            onFavorite = { book ->
                // 홈 > 즐겨찾기 리스트 > 하트 선택 > 좋아요 삭제
//                bookViewModel.deleteFavorite(book.bookItem.toFavoriteBook())
                bookViewModel.toggleFavorite(book)
            }
        )

        binding.favoriteListView.apply {
            layoutManager = GridLayoutManager(context, GRID_COUNT)
            addItemDecoration(GridItemDecoration(GRID_COUNT, Utils.dpToPx(16), true))
            adapter = favoriteAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bookViewModel.favoriteUiState.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            showEmptyScreen(false)
                            favoriteAdapter.submitList(state.item)
                        }

                        is UiState.Error -> {
                            Toast.makeText(
                                context,
                                resources.getString(R.string.common_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            showEmptyScreen(true)
                        }
                    }
                }
            }
        }

        bookViewModel.getFavoriteBook()
    }

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