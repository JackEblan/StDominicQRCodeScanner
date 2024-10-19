package st.dominic.qrcodescanner.feature.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import st.dominic.qrcodescanner.core.domain.GetBooksUseCase
import st.dominic.qrcodescanner.core.model.GetBooksResult
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val getBooksUseCase: GetBooksUseCase,
) : ViewModel() {
    private val _bookUiState = MutableStateFlow<BookUiState?>(null)

    val bookUiState = _bookUiState.onStart { getBooks() }.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5_000), initialValue = null
    )

    private fun getBooks() {
        viewModelScope.launch {
            _bookUiState.update {
                BookUiState.Loading
            }

            when (val getBooksResult = getBooksUseCase()) {
                GetBooksResult.EmailVerify -> {
                    _bookUiState.update {
                        BookUiState.EmailVerify
                    }
                }

                GetBooksResult.Failed -> {
                    _bookUiState.update {
                        BookUiState.Failed
                    }
                }

                is GetBooksResult.Success -> {
                    _bookUiState.update {
                        BookUiState.Success(books = getBooksResult.books)
                    }
                }
            }
        }
    }
}