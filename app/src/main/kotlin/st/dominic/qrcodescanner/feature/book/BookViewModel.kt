package st.dominic.qrcodescanner.feature.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import st.dominic.qrcodescanner.core.data.repository.BookRepository
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    bookRepository: BookRepository,
) : ViewModel() {
    val bookUiState =
        bookRepository.getBorrowedBooksDocuments(studentId = "id").map(BookUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = BookUiState.Loading
            )
}