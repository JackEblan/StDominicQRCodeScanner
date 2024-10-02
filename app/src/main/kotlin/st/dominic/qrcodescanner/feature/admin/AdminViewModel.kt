package st.dominic.qrcodescanner.feature.admin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import st.dominic.qrcodescanner.core.data.repository.BookRepository
import st.dominic.qrcodescanner.core.model.Book
import st.dominic.qrcodescanner.core.model.BookStatus
import st.dominic.qrcodescanner.feature.admin.navigation.AdminRouteData
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, private val bookRepository: BookRepository
) : ViewModel() {
    private val adminRouteData = savedStateHandle.toRoute<AdminRouteData>()

    private val _book = MutableStateFlow<Book?>(null)

    val book = _book.onStart { getBook() }.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5_000), initialValue = null
    )

    private val _isReturned = MutableStateFlow<Boolean?>(null)

    val isReturned = _isReturned.asStateFlow()

    fun returnBook() {
        viewModelScope.launch {
            bookRepository.updateBookStatus(
                id = adminRouteData.id, bookStatus = BookStatus.Returned
            )

            _isReturned.update {
                true
            }
        }
    }

    private fun getBook() {
        viewModelScope.launch {
            _book.update {
                bookRepository.getBook(id = adminRouteData.id)
            }
        }
    }
}