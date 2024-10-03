package st.dominic.qrcodescanner.feature.admin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import st.dominic.qrcodescanner.core.data.repository.BookRepository
import st.dominic.qrcodescanner.core.model.BookStatus
import st.dominic.qrcodescanner.feature.admin.navigation.AdminRouteData
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, private val bookRepository: BookRepository
) : ViewModel() {
    private val adminRouteData = savedStateHandle.toRoute<AdminRouteData>()

    private val _adminUiState = MutableStateFlow<AdminUiState?>(null)

    val adminUiState = _adminUiState.onStart { getBook() }.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5_000), initialValue = null
    )

    fun returnBook() {
        viewModelScope.launch {
            _adminUiState.update {
                AdminUiState.Loading
            }

            bookRepository.updateBookStatus(
                id = adminRouteData.id, bookStatus = BookStatus.Returned
            )

            _adminUiState.update {
                AdminUiState.Returned
            }
        }
    }

    private fun getBook() {
        viewModelScope.launch {
            _adminUiState.update {
                AdminUiState.Success(book = bookRepository.getBook(id = adminRouteData.id))
            }
        }
    }
}