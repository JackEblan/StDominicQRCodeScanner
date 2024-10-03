package st.dominic.qrcodescanner.feature.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import st.dominic.qrcodescanner.core.data.repository.AdminRepository
import st.dominic.qrcodescanner.core.data.repository.BookRepository
import st.dominic.qrcodescanner.core.data.repository.EmailPasswordAuthenticationRepository
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    bookRepository: BookRepository,
    private val emailPasswordAuthenticationRepository: EmailPasswordAuthenticationRepository,
    private val adminRepository: AdminRepository,
) : ViewModel() {
    private val _isAdmin = MutableStateFlow<Boolean?>(null)

    val isAdmin = _isAdmin.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val bookUiState = _isAdmin.onStart { checkAdmin() }.filterNotNull().flatMapLatest { isAdmin ->
        val studentId = emailPasswordAuthenticationRepository.getCurrentUser().getOrNull()?.uid

        if (isAdmin) {
            return@flatMapLatest bookRepository.getBorrowedBooks()
        }

        if (studentId != null) {
            bookRepository.getBorrowedBooksByStudentId(studentId = studentId)
        } else {
            flowOf(emptyList())
        }
    }.map(BookUiState::Success).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BookUiState.Loading
    )

    private fun checkAdmin() {
        viewModelScope.launch {
            val studentId = emailPasswordAuthenticationRepository.getCurrentUser().getOrNull()?.uid

            _isAdmin.update {
                studentId != null && adminRepository.isAdmin(id = studentId)
            }
        }
    }
}