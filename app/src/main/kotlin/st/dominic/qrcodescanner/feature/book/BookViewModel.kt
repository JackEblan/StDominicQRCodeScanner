package st.dominic.qrcodescanner.feature.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import st.dominic.qrcodescanner.core.data.repository.BookRepository
import st.dominic.qrcodescanner.core.domain.GetProfileUseCase
import st.dominic.qrcodescanner.core.model.GetProfileResult
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val bookRepository: BookRepository,
) : ViewModel() {
    private val _profileUiState = MutableStateFlow<ProfileUiState?>(null)

    val profileUiState = _profileUiState.onStart { getUser() }.stateIn(
        scope = viewModelScope, started = SharingStarted.Lazily, initialValue = null
    )

    private val _id = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val bookUiState =
        _id.filterNotNull().flatMapLatest { bookRepository.getBorrowedBooksByStudentId(it) }
            .map(BookUiState::Success).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    private fun getUser() {
        viewModelScope.launch {
            _profileUiState.update {
                ProfileUiState.Loading
            }

            when (val result = getProfileUseCase()) {
                GetProfileResult.EmailVerify -> {
                    _profileUiState.update {
                        ProfileUiState.EmailVerify
                    }
                }

                GetProfileResult.Failed -> {
                    _profileUiState.update {
                        ProfileUiState.Failed
                    }
                }

                is GetProfileResult.Success -> {
                    _profileUiState.update {
                        ProfileUiState.Success
                    }

                    _id.update {
                        result.authCurrentUser.uid
                    }
                }
            }
        }
    }
}