package st.dominic.qrcodescanner.feature.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import st.dominic.qrcodescanner.core.data.repository.BookRepository
import javax.inject.Inject

@HiltViewModel
class ManagementViewModel @Inject constructor(private val bookRepository: BookRepository) :
    ViewModel() {

    val managementUiState =
        bookRepository.getBorrowedBooks().map(ManagementUiState::Success).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ManagementUiState.Loading
        )
}