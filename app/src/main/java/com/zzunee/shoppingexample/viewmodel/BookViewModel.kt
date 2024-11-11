package com.zzunee.shoppingexample.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzunee.shoppingexample.data.network.Book
import com.zzunee.shoppingexample.data.network.BookItem
import com.zzunee.shoppingexample.data.repository.BookRepository
import com.zzunee.shoppingexample.data.repository.Repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class BookViewModel(private val bookRepository: BookRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<Book>>>(UiState.Empty)
    val uiState: StateFlow<UiState<List<Book>>> = _uiState

    private val _favoriteUiState = MutableStateFlow<UiState<List<Book>>>(UiState.Empty)
    val favoriteUiState: StateFlow<UiState<List<Book>>> = _favoriteUiState

    private val _detailUiState = MutableStateFlow<UiState<Book>>(UiState.Empty)
    val detailUiState: StateFlow<UiState<Book>> = _detailUiState

    private val _selectedBook = MutableLiveData<Book>()
    val selectedBook = _selectedBook

    // 검색어 기반으로 책 목록 불러오기
    fun getBookList(input: String) {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            when (val result = bookRepository.searchBook(input)) {
                is Result.Empty -> _uiState.value = UiState.Empty
                is Result.Error -> _uiState.value = UiState.Error(result.msg)
                is Result.Success -> fetchFavoriteBook(result.data)
            }
        }
    }

    // 현재 책 목록과 즐겨찾기 한 책 매칭
    private fun fetchFavoriteBook(items: List<BookItem> = emptyList()) {
        viewModelScope.launch {
            bookRepository.fetchFavoriteBooks(items)
                .catch { _uiState.value = UiState.Error() }
                .collect { books ->
                    _uiState.value = if(books.isEmpty()) UiState.Empty else UiState.Success(books)
                }
        }
    }

    // 즐겨찾기 한 책 목록 불러오기
    fun getFavoriteBook() {
        viewModelScope.launch {
            bookRepository.getAllFavoriteBooks()
                .catch { _favoriteUiState.value = UiState.Error() }
                .collect { books ->
                    _favoriteUiState.value = if(books.isEmpty()) UiState.Empty else UiState.Success(books)
                }
        }
    }

    // 즐겨찾기 추가 & 삭제
    fun toggleFavorite(book: Book) {
        viewModelScope.launch {
            println("book.isFavorite = ${book.isFavorite}")
            bookRepository.toggleFavoriteBook(book)
        }
    }

    // 책 선택
    fun selectBook(book: Book) {
        _selectedBook.value = book
    }

    // 책 상세 내용 불러오기
    fun getBookDetail(book: Book) {
        _detailUiState.value = UiState.Loading

        viewModelScope.launch {
            _detailUiState.value = when (val result =
                bookRepository.searchBookDetail(book.bookItem.title, book.bookItem.isbn)) {
                is Result.Empty -> UiState.Error("요청한 데이터를 찾을 수 없습니다.")
                is Result.Error -> UiState.Error(result.msg)
                is Result.Success -> UiState.Success(Book(result.data.first(), book.isFavorite))
            }
        }
    }
}