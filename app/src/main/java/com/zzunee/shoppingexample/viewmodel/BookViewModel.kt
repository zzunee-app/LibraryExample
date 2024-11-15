package com.zzunee.shoppingexample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzunee.shoppingexample.model.network.data.Book
import com.zzunee.shoppingexample.model.network.data.BookItem
import com.zzunee.shoppingexample.model.repository.Result
import com.zzunee.shoppingexample.model.repository.base.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class BookViewModel(private val bookRepository: BookRepository) : ViewModel() {
    // 책 검색 결과
    private val _searchUiState = MutableStateFlow<UiState<List<Book>>>(UiState.Empty)
    val searchUiState: StateFlow<UiState<List<Book>>>
        get() = _searchUiState.asStateFlow()

    // 책 상세 검색 결과
    private val _detailUiState = MutableStateFlow<UiState<Book>>(UiState.Empty)
    val detailUiState: StateFlow<UiState<Book>>
        get() = _detailUiState.asStateFlow()

    // 좋아요 책 리스트
    private val _favoriteUiState = MutableStateFlow<UiState<List<Book>>>(UiState.Empty)
    val favoriteUiState: StateFlow<UiState<List<Book>>>
        get() = _favoriteUiState.asStateFlow()

    private var bookList: List<BookItem> = emptyList()

    init {
        viewModelScope.launch {
            bookRepository.getAllFavoriteBooks()
                .catch { _favoriteUiState.value = UiState.Empty }
                .collect { favorites ->
                    if(favorites.isEmpty()) {
                        _favoriteUiState.value = UiState.Empty
                    } else {
                        _favoriteUiState.value = UiState.Success(favorites)

                        // 책 검색 리스트 있으면 즐겨찾기 도서와 매칭
                        if(_searchUiState.value is UiState.Success) {
                            fetchSearchBooks(favorites)
                        }
                    }
                }
        }
    }

    // 검색어로 도서 리스트 호출
    fun getBookList(input: String) {
        _searchUiState.value = UiState.Loading

        viewModelScope.launch {
            when (val result = bookRepository.searchBook(input)) {
                is Result.Error -> _searchUiState.value = UiState.Error(result.msg)
                is Result.Empty -> _searchUiState.value = UiState.Empty
                is Result.Success -> {
                    bookList = result.data

                    if(_favoriteUiState.value is UiState.Success) {
                        fetchSearchBooks((_favoriteUiState.value as UiState.Success).item)
                    } else {
                        fetchSearchBooks()
                    }
                }
            }
        }
    }

    private fun fetchSearchBooks(favorites: List<Book> = emptyList()) {
        val books = bookRepository.fetchFavoriteBooks(bookList, favorites)
        _searchUiState.value = if(books.isEmpty()) UiState.Empty else UiState.Success(books)
    }

    // 즐겨찾기 추가 & 삭제
    fun toggleFavorite(book: Book) {
        viewModelScope.launch {
            bookRepository.toggleFavoriteBook(book)
        }
    }

    // 책 상세 내용 불러오기
    fun getBookDetail(book: Book?) {
        if (book == null) {
            _detailUiState.value = UiState.Empty
        } else {
            _detailUiState.value = UiState.Loading

            viewModelScope.launch {
                _detailUiState.value = when (val result = bookRepository.searchBookDetail(book.bookItem.title, book.bookItem.isbn)) {
                    is Result.Error -> UiState.Error(result.msg)
                    is Result.Empty -> UiState.Empty
                    is Result.Success -> UiState.Success(Book(result.data, book.isFavorite))
                }
            }
        }
    }
}